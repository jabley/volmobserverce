/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.url.http;

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.net.url.URLContentImpl;
import com.volantis.shared.net.url.http.utils.HttpClientUtils;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpException;


/**
 * Class to hold a cached HTTPContent object. Status code, HTTP version,
 * response headers, cookies and response body are copied from an HTTP content
 * and stored for later use.
 *
 * <p>The constructor requires a {@link CachedHttpContentState} object to
 * continuously update the value of the Age header at each {@link #getHeaders()}
 * request. This state is also used to determine if the cached HTTP object is
 * used to store cached responses. If the content is cacheable according to the
 * state then hop-by-hop headers are not copied.</p>
 */
public class CachedHttpContent extends URLContentImpl implements HttpContent{

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(CachedHttpContent.class);

    //@todo Removal of hop-by-hop headers should be done outside of the cache
    //@todo because the code that uses the cache might need those headers before
    //@todo sending back the response without the hop-by-hop headers.
    //@todo But ATM we don't use these headers so removing the headers here
    //@todo helps to make the code cleaner around the cache.
    /**
     * Set of hop-by-hop headers, according to the HTTP 1.1 specification.
     * Proxies must remove hop-by-hop headers from the cached response.
     */
    private static final Set HOP_BY_HOP_HEADERS;

    static {
        HOP_BY_HOP_HEADERS = new HashSet();
        HOP_BY_HOP_HEADERS.add("Connection".toLowerCase());
        HOP_BY_HOP_HEADERS.add("Keep-Alive".toLowerCase());
        HOP_BY_HOP_HEADERS.add("Proxy-Authenticate".toLowerCase());
        HOP_BY_HOP_HEADERS.add("Proxy-Authorization".toLowerCase());
        HOP_BY_HOP_HEADERS.add("TE".toLowerCase());
        HOP_BY_HOP_HEADERS.add("Trailers".toLowerCase());
        HOP_BY_HOP_HEADERS.add("Transfer-Encoding".toLowerCase());
        HOP_BY_HOP_HEADERS.add("Upgrade".toLowerCase());
    }

    /**
     * The stored status code.
     */
    private final int statusCode;

    /**
     * The stored character encoding.
     */
    private final String characterEncoding;

    /**
     * The stored HTTP version.
     */
    private final String httpVersion;

    /**
     * The stored cookies.
     */
    private final List cookies;

    /**
     * The stored headers.
     */
    private final List headers;

    /**
     * Map between header names and lists headers with those names for easier
     * header look-up.
     */
    private final Map headersMap;

    /**
     * The stored response body.
     */
    private final byte[] content;

    /**
     * The state of the cached HTTP content
     */
    private CachedHttpContentState state;

    /**
     * The clock to be used to compute the age value.
     */
    private final SystemClock clock;

    /**
     * Dependency information for this cached content
     */
    private final CacheableDependency dependency;
    
    /**
     * Creates a cached HTTP content. All of the values stored in the specified
     * HTTP content are stored.
     *
     * @param content the HTTP content to cache
     * @param state the state of the HTTP content
     * @param clock the clock to be used to compute the age header value (this
     * clock should be the same as the one used by the cache)
     * @param dependency the dependency information for this content
     * @throws IOException
     */
    public CachedHttpContent(final HttpContent content,
                             final CachedHttpContentState state,
                             final SystemClock clock,
                             final CacheableDependency dependency)
            throws IOException {
        statusCode = content.getStatusCode();
        characterEncoding = content.getCharacterEncoding();
        httpVersion = content.getHttpVersion();
        cookies = new LinkedList();
        for (Iterator iter = content.getCookies(); iter.hasNext(); ) {
            cookies.add(iter.next());
        }
        headers = new LinkedList();
        headersMap = new HashMap();
        this.content = saveToByteArray(content.getInputStream());
        this.state = state;
        this.clock = clock;
        this.dependency = dependency;
        addHeaders(content);
    }

    /**
     * Copies the headers from the HTTP content
     *
     * @param content the HTTP content to copy the headers from
     */
    private void addHeaders(final HttpContent content) {
        for (Iterator iter = content.getHeaders(); iter.hasNext(); ) {
            final Header header = (Header) iter.next();
            final String name = header.getName().toLowerCase();
            if (!state.isCacheable() || !HOP_BY_HOP_HEADERS.contains(name)) {
                headers.add(header);
                List headerList = (List) headersMap.get(name);
                if (headerList == null) {
                    headerList = new LinkedList();
                    headersMap.put(name, headerList);
                }
                headerList.add(header);
            }
        }
    }

    // javadoc inherited
    public int getStatusCode() {
        return statusCode;
    }

    // javadoc inherited
    public Iterator getCookies() {
        Iterator iterator;
        synchronized(cookies) {
            iterator = Collections.unmodifiableList(new LinkedList(cookies)).iterator();
        }
        return iterator;
    }

    // javadoc inherited
    public Iterator getHeaders() {
        Iterator iterator;
        synchronized(headers) {
            if (state.isCacheable()) {
                updateAgeHeader();
            }
            iterator = Collections.unmodifiableList(new LinkedList(headers)).iterator();
        }
        return iterator;
    }

    /**
     * Updates the Age header to reflect the current age.
     */
    private void updateAgeHeader() {
        // remove existing age header(s)
        final List list = (List) headersMap.remove("age");
        if (list != null) {
            for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                final Object oldHeader = iter.next();
                iter.remove();
                headers.remove(oldHeader);
            }
        }
        // compute the current age
        final Period currentAge = state.getCurrentAge(clock.getCurrentTime());
        final long currentAgeLong = currentAge.inMillis() / 1000;
        final int currentAgeInt;
        if (currentAgeLong > Integer.MAX_VALUE) {
            currentAgeInt = Integer.MAX_VALUE;
        } else {
            currentAgeInt = (int) currentAgeLong;
        }
        // set the new Age header
        final Header header = createHeader("Age", Integer.toString(currentAgeInt));
        headers.add(header);
        final List ageList = new LinkedList();
        ageList.add(header);
        headersMap.put("age", ageList);
    }

    /**
     * Creates a new Header implementation using the specified name and value.
     * @param name the name of the header
     * @param value the value of the header
     * @return the created Header implementation
     */
    private Header createHeader(final String name, final String value) {
        return new Header() {
            // javadoc inherited
            public String getName() {
                return name;
            }
            // javadoc inherited
            public String getValue() {
                return value;
            }
        };
    }

    // javadoc inherited
    public String getHttpVersion() {
        return httpVersion;
    }

    // javadoc inherited
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    // javadoc inherited
    public String getCharacterEncoding() throws IOException {
        return characterEncoding;
    }

    /**
     * Saves an input stream into a byte array.
     *
     * @param is the input stream to store
     * @return the created byta array with the response body
     * @throws IOException if response body cannot be read
     */
    private byte[] saveToByteArray(final InputStream is) throws IOException {
        final int length = getContentLength();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
        final byte[] buffer = new byte[4096];
        int count = is.read(buffer);
        while (count != -1) {
            baos.write(buffer, 0, count);
            count = is.read(buffer);
        }
        return baos.toByteArray();
    }

    /**
     * Returns the content length according to the Content-Length header. If no
     * such header is present in the headers or the header value is invalid then
     * it returns the default length (128).
     *
     * @return the content length
     */
    private int getContentLength() {
        int length = 128;
        final List list = (List) headersMap.get("content-length");
        if (list != null) {
            final Header contentLengthHeader = (Header) list.get(0);
            final String contentLengthStr = contentLengthHeader.getValue();
            try {
                length = Integer.parseInt(contentLengthStr);
            } catch (NumberFormatException e) {
                // cannot read content length, use the default value
            }
        }
        return length;
    }

    /**
     * Combines the response headers of the specified HTTP content with the ones
     * stored in the cached HTTP content according to the HTTP specification.
     *
     * @param content the content with the new headers
     * @param state the new state
     */
    public void combineHeaders(final HttpContent content,
                               final CachedHttpContentState state) {
        final Set processedHeaders = new HashSet();
        // remove existing headers with matching names
        for (Iterator iter = content.getHeaders(); iter.hasNext(); ) {
            final Header header = (Header) iter.next();
            final String name = header.getName().toLowerCase();
            if ((!state.isCacheable() || !HOP_BY_HOP_HEADERS.contains(name)) &&
                    !processedHeaders.contains(name)) {

                final List headerList = (List) headersMap.get(name);
                if (headerList != null) {
                    for (ListIterator headersIter = headerList.listIterator();
                         headersIter.hasNext(); ) {
                        final Header existing = (Header) headersIter.next();
                        if (name.equals("warning")) {
                            // keep 2xx warnings
                            // need to split the header as it might contain a
                            // value list
                            org.apache.commons.httpclient.Header httpHeader =
                                new org.apache.commons.httpclient.Header(
                                    existing.getName(), existing.getValue());
                            try {
                                final HeaderElement[] elements =
                                    httpHeader.getValues();
                                final StringBuffer newValue = new StringBuffer(
                                    existing.getValue().length());
                                for (int i = 0; i < elements.length; i++) {
                                    final HeaderElement element = elements[i];
                                    // check if this element is a 2xx warning
                                    if (isWarningElement(element, '2')) {
                                        // append it to the new value of the
                                        // header
                                        if (newValue.length() > 0) {
                                            newValue.append(',');
                                        }
                                        newValue.append(
                                            HttpClientUtils.toString(element));
                                    }
                                }
                                if (newValue.length() > 0) {
                                    // create a new Header, replace the existing
                                    // header with it
                                    final Header newHeader = createHeader(
                                        existing.getName(), newValue.toString());
                                    final int index = headers.indexOf(existing);
                                    headers.set(index, newHeader);
                                    headersIter.set(newHeader);
                                } else {
                                    headers.remove(existing);
                                    headersIter.remove();
                                }
                            } catch (HttpException e) {
                                LOGGER.error("parse-header-error",
                                    existing.getName(), e);
                                headers.remove(existing);
                                headersIter.remove();
                            }
                        } else {
                            headers.remove(existing);
                            headersIter.remove();
                        }
                    }
                }
                processedHeaders.add(name);
            }
        }
        // remove 1xx warnings
        if (!processedHeaders.contains("warning")) {
            final List list = (List) headersMap.get("warning");
            if (list != null) {
                for (ListIterator iter = list.listIterator(); iter.hasNext(); ) {
                    final Header existing = (Header) iter.next();
                    // delete 1xx warnings
                    // need to split the header as it might contain a
                    // value list
                    org.apache.commons.httpclient.Header httpHeader =
                        new org.apache.commons.httpclient.Header(
                            existing.getName(), existing.getValue());
                    try {
                        final HeaderElement[] elements =
                            httpHeader.getValues();
                        final StringBuffer newValue =
                            new StringBuffer(existing.getValue().length());
                        for (int i = 0; i < elements.length; i++) {
                            final HeaderElement element = elements[i];
                            // check if this element is a 1xx warning
                            if (!isWarningElement(element, '1')) {
                                // append it to the new value of the header
                                if (newValue.length() > 0) {
                                    newValue.append(',');
                                }
                                newValue.append(
                                    HttpClientUtils.toString(element));
                            }
                        }
                        if (newValue.length() > 0) {
                            // create a new Header, replace the existing
                            // header with it
                            final Header newHeader = createHeader(
                                existing.getName(), newValue.toString());
                            final int index = headers.indexOf(existing);
                            headers.set(index, newHeader);
                            iter.set(newHeader);
                        } else {
                            headers.remove(existing);
                            iter.remove();
                        }
                    } catch (HttpException e) {
                        LOGGER.error("parse-header-error",
                            existing.getName(), e);
                        headers.remove(existing);
                        iter.remove();
                    }
                }
                // clean-up
                if (list.size() == 0) {
                     headersMap.remove("warning");
                }
            }
        }
        if (!state.isCacheable()) {
            // if the content become uncacheable then we should remove the
            // outdated age header
            final List list = (List) headersMap.remove("age");
            if (list != null) {
                for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                    headers.remove(iter.next());
                }
            }
        }
        this.state = state;
        addHeaders(content);

        // replace cookies if the new content has cookies
        final Iterator cookiesIter = content.getCookies();
        if (cookiesIter.hasNext()) {
            cookies.clear();
            while (cookiesIter.hasNext()) {
                cookies.add(cookiesIter.next());
            }
        }

        // update the state in the dependency
        dependency.setState(state);
    }

    /**
     * Returns true if the specified header element is a valid warning header of
     * the specified level.
     *
     * @param element the element to check
     * @param level the expected level
     * @return true iff the header element is a <code>level</code> level warning
     * header element
     */
    private boolean isWarningElement(final HeaderElement element,
                                     final char level) {
        final String name = element.getName();
        boolean matches = false;
        // check if the name of the element starts with "NNN "
        if (name.length() > 4) {
            matches = name.charAt(0) == level &&
                Character.isDigit(name.charAt(1)) &&
                Character.isDigit(name.charAt(2)) &&
                Character.isSpaceChar(name.charAt(3));
        }
        return matches;
    }

    // javadoc inherited
    public Dependency getDependency() {
        return dependency;
    }
}
