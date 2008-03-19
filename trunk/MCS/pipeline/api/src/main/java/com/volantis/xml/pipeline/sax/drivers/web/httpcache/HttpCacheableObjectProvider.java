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
package com.volantis.xml.pipeline.sax.drivers.web.httpcache;

import com.volantis.cache.CacheEntry;
import com.volantis.cache.Cache;
import com.volantis.cache.group.Group;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.UncacheableDependency;
import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.SimpleHTTPFactory;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.headers.HeaderImpl;
import com.volantis.shared.net.http.headers.HeaderNames;
import com.volantis.shared.net.url.URLContentImpl;
import com.volantis.shared.net.url.http.CacheableDependency;
import com.volantis.shared.net.url.http.CachedHttpContent;
import com.volantis.shared.net.url.http.CachedHttpContentState;
import com.volantis.shared.net.url.http.CachedHttpContentStateBuilder;
import com.volantis.shared.net.url.http.HttpContent;
import com.volantis.shared.net.url.http.HttpResponseHeaderAccessor;
import com.volantis.shared.net.url.http.RuntimeHttpException;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Time;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPException;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestExecutor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPResponseAccessor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPVersion;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * CacheableObjectProvider implementation for webd:get.
 */
public class HttpCacheableObjectProvider implements CacheableObjectProvider {

    /**
     * The date format to be used
     */
    private static final DateFormat RFC1123 =
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.UK);

    static {
        RFC1123.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Static response to be used for general server errors.
     */
    private static final HTTPResponseAccessor ERROR_500_RESPONSE_ACCESSOR =
            new ErrorHTTPResponseAccessor(500);

    /**
     * The executor to be used to retrieve content from the origin server.
     */
    private final HTTPRequestExecutor executor;

    /**
     * The cache group where the new entries should be added to.
     */
    private final Group group;
    
    /**
     * A reference to the cache.
     */
    private Cache cache;

    /**
     * Creates a new instance
     *
     * @param executor to get the response from the origin server
     * @param group the cache group to be used for new cache entries
     * @param cache a reference to the cache
     */
    public HttpCacheableObjectProvider(final HTTPRequestExecutor executor,
                                       final Group group,
                                       final Cache cache) {
        this.executor = executor;
        this.group = group;
        this.cache = cache;
    }

    /**
     * Override default implementation to allow HTTP caching to work correctly.
     *
     * NOTE: This is not synchronized but is thread safe.
     * @return Value of value.
     */
    public ProviderResult retrieve(final SystemClock clock,
                                   final Object key,
                                   final CacheEntry entry) {

        HttpContent httpContent;
        CachedHttpContentState state = null;
        HTTPResponseAccessor accessor;
        boolean cacheable = false;
        ProviderResult result = null;
        try {
            // check if validation headers can be added
            if (entry != null) {
                final CachedHttpContentState existingState =
                    (CachedHttpContentState) entry.getExtensionObject();
                if (existingState != null) {
                    // this is a validation
                    // set the If-Modified-Since header using the stored
                    // last modified value (if there is any)
                    final Time lastModified = existingState.getLastModified();
                    if (lastModified != null) {
                        final String lastModifiedAsString;
                        // SimpleDateFormat is not thread safe
                        synchronized (RFC1123) {
                            lastModifiedAsString = RFC1123.format(
                                new Date(lastModified.inMillis()));
                        }
                        final Header header =
                            new HeaderImpl(HeaderNames.IF_MODIFIED_SINCE_HEADER);
                        header.setValue(lastModifiedAsString);
                        executor.addRequestHeader(header);
                    }

                    // set the If-None-Match header with the eTag value, if
                    // there is any
                    final String eTag = existingState.getETag();
                    if (eTag != null && eTag.trim().length() > 0) {
                        final Header header =
                            new HeaderImpl(HeaderNames.IF_NONE_MATCH_HEADER);
                        header.setValue(eTag);
                        executor.addRequestHeader(header);
                    }
                }
            }

            // record the request time
            final CachedHttpContentStateBuilder builder =
                new CachedHttpContentStateBuilder();
            builder.setRequestTime(clock.getCurrentTime());
            accessor = executor.execute();
            // record response time
            builder.setResponseTime(clock.getCurrentTime());

            // process the response
            builder.setMethodAccessor(
                new HttpResponseHeaderAccessorWrapper(accessor));
            httpContent = new HTTPResponseAccessorWrapper(accessor);
            final int statusCode = accessor.getStatusCode();
            if (isStatusCodeCachable(statusCode)) {
                state = builder.build();
                if (state != null && state.isCacheable()) {
                    httpContent = new CachedHttpContent(httpContent, state, clock,
                        new CacheableDependency(key, state, clock,
                            cache, this));
                    cacheable = true;
                }
            } else if (statusCode == 304) {
                // 304 Not Modified
                // it is only possible if it is a revalidation cache entry holds
                // the exisiting cache config as an extension object
                state = builder.merge(
                    ((CachedHttpContentState) entry.getExtensionObject()));
                cacheable = state.isCacheable();
                // entry must have a CachedHttpContent
                final CachedHttpContent originalContent =
                    (CachedHttpContent) entry.getValue();
                originalContent.combineHeaders(httpContent, state);
                httpContent = originalContent;
            }

            result = new ProviderResult(httpContent, group, cacheable, state);
        } catch (Throwable e) {
            // Yep, thats right. We do want to catch all exceptions. Return a
            // 500 error (internal server error).
            try {
                httpContent = new HTTPResponseAccessorWrapper(
                    ERROR_500_RESPONSE_ACCESSOR);
            } catch (HTTPException e1) {
                throw new IllegalStateException(
                    "Wrapping ERROR_500 Accessor failed");
            }
            result = new ProviderResult(e, group, false, null);
        }
        
        return result;
    }

    /**
     * Return true is the status code in the response allows that response to
     * be cached. This is a fast check that indicates it may be possible to
     * cache the page. Subsequent tests should check for cache control
     * directives that may make the page uncachable.
     *
     * @param statusCode the code to check for permission to cache.
     * @return true if the status code allows the response to be cached.
     */
    private boolean isStatusCodeCachable(int statusCode) {
        // The cache does not support range/partial requests so 206
        // responses cannot be cached
        return statusCode == 200
                || statusCode == 203
                // || statusCode == 206
                || statusCode == 300
                || statusCode == 301
                || statusCode == 410;
    }

    /**
     * This class is used to provide error responses to the requestor.
     */
    private static class ErrorHTTPResponseAccessor
            implements HTTPResponseAccessor {

        /**
         * Create a static empty InputStream for use when errors occur.
         */
        private static final InputStream EMPTY_INPUT_STREAM =
                new InputStream() {
                    public int read() {
                        return -1;
                    }
                };

        /**
         * The factory to use when creating the Age headers.
         */
        private static final HTTPFactory factory =
                SimpleHTTPFactory.getDefaultInstance();
        /**
         * The status code to return
         */
        private int statusCode;

        ErrorHTTPResponseAccessor(final int statusCode) {
            this.statusCode = statusCode;
        }

        // javadoc inherited
        public HTTPMessageEntities getCookies() throws HTTPException {
            return factory.createHTTPMessageEntities();
        }

        // javadoc inherited
        public HTTPMessageEntities getHeaders() throws HTTPException {
            return factory.createHTTPMessageEntities();
        }

        // javadoc inherited
        public int getStatusCode() {
            return statusCode;
        }

        // javadoc inherited
        public HTTPVersion getHTTPVersion() {
            return HTTPVersion.HTTP_1_0;
        }

        // javadoc inherited
        public InputStream getResponseStream() throws HTTPException {
            return EMPTY_INPUT_STREAM;
        }
    }

    /**
     * HTTPResponseAccessor wrapper to implement HttpContent interface.
     */
    private static class HTTPResponseAccessorWrapper extends URLContentImpl implements HttpContent {
        /**
         * The wrapped HTTP response accessor.
         */
        private final HTTPResponseAccessor accessor;

        /**
         * The list of cookies.
         */
        private List cookies;

        /**
         * The list of headers.
         */
        private List headers;

        /**
         * Creates the wrapper.
         *
         * @param accessor the HTTPResponseAccessor to wrap
         * @throws HTTPException
         */
        public HTTPResponseAccessorWrapper(final HTTPResponseAccessor accessor)
                throws HTTPException {
            this.accessor = accessor;
            storeCookies();
            storeHeaders();
        }

        // javadoc inherited
        public int getStatusCode() {
            return accessor.getStatusCode();
        }

        // javadoc inherited
        public Iterator getCookies() {
            return cookies.iterator();
        }

        /**
         * Copies cookies from the HTTPResponseAccessor.
         *
         * @throws HTTPException
         */
        private void storeCookies() throws HTTPException {
            final HTTPMessageEntities cookieEntities = accessor.getCookies();
            if (cookieEntities != null) {
                cookies = new LinkedList();
                for (Iterator iter = cookieEntities.iterator(); iter.hasNext(); ) {
                    final Cookie cookie = (Cookie) iter.next();
                    final com.volantis.shared.net.url.http.Cookie dstCookie =
                        new com.volantis.shared.net.url.http.Cookie() {
                            public boolean isSecure() {
                                return cookie.isSecure();
                            }
                            public String getComment() {
                                return cookie.getComment();
                            }
                            public String getPath() {
                                return cookie.getPath();
                            }
                            public String getDomain() {
                                return cookie.getDomain();
                            }
                            public String getName() {
                                return cookie.getName();
                            }
                            public String getValue() {
                                return cookie.getValue();
                            }
                            public int getVersion() {
                                return cookie.getVersion().getNumber();
                            }
                            public int getMaxAge() {
                                return cookie.getMaxAge();
                            }
                        };
                    cookies.add(dstCookie);
                }
            } else {
                cookies = Collections.EMPTY_LIST;
            }
        }

        // javadoc inherited
        public Iterator getHeaders() {
            return headers.iterator();
        }

        /**
         * Copies headers from the HTTPResponseAccessor.
         *
         * @throws HTTPException
         */
        private void storeHeaders() throws HTTPException {
            final HTTPMessageEntities headerEntities = accessor.getHeaders();
            if (headerEntities != null) {
                headers = new LinkedList();
                for (Iterator iter = headerEntities.iterator(); iter.hasNext(); ) {
                    final Header header = (Header) iter.next();
                    final com.volantis.shared.net.url.http.Header dstCookie =
                        new com.volantis.shared.net.url.http.Header() {
                            public String getName() {
                                return header.getName();
                            }
                            public String getValue() {
                                return header.getValue();
                            }
                        };
                    headers.add(dstCookie);
                }
            } else {
                headers = Collections.EMPTY_LIST;
            }
        }

        // javadoc inherited
        public String getHttpVersion() {
            return accessor.getHTTPVersion().getFullName();
        }

        // javadoc inherited
        public InputStream getInputStream() throws IOException {
            try {
                return accessor.getResponseStream();
            } catch (HTTPException e) {
                throw new IOException(e.getMessage());
            }
        }

        // javadoc inherited
        public String getCharacterEncoding() throws IOException {
            return null;
        }

        public Dependency getDependency() {
            return UncacheableDependency.getInstance();
        }
    }

    /**
     * Wrapper for HTTPResponseAccessor objects to implement the
     * HttpResponseHeaderAccessor interface.
     */
    private static class HttpResponseHeaderAccessorWrapper
            implements HttpResponseHeaderAccessor {

        private static final HTTPFactory HTTP_FACTORY =
            SimpleHTTPFactory.getDefaultInstance();

        /**
         * The wrapped HTTPResponseAccessor object.
         */
        private final HTTPResponseAccessor accessor;

        /**
         * Creates a wrapper object.
         *
         * @param accessor the accessor to wrap.
         */
        public HttpResponseHeaderAccessorWrapper(
                final HTTPResponseAccessor accessor) {
            this.accessor = accessor;
        }

        // javadoc inherited
        public String getProtocol() {
            return "http";
        }

        // javadoc inherited
        public String getResponseHeaderValue(final String headerName) {
            // create the identity for the given header name
            final HTTPMessageEntityIdentity identity =
                HTTP_FACTORY.createHeader(headerName).getIdentity();
            final String result;
            try {
                // append the values of the headers that have the same name
                final Iterator iter =
                    accessor.getHeaders().valuesIterator(identity);
                if (iter.hasNext()) {
                    final StringBuffer buffer = new StringBuffer();
                    while (iter.hasNext()) {
                        final String value = ((String) iter.next()).trim();
                        if (value != null && value.length() > 0) {
                            if (buffer.length() > 0) {
                                buffer.append(",");
                            }
                            buffer.append(value);
                        }
                    }
                    result = buffer.toString();
                } else {
                    result = null;
                }
            } catch (HTTPException e) {
                throw new IllegalStateException("Invalid response headers.");
            }
            return result;
        }
    }
}
