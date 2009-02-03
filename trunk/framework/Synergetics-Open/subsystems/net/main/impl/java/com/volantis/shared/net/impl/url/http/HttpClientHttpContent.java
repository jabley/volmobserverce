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

package com.volantis.shared.net.impl.url.http;

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.UncacheableDependency;
import com.volantis.shared.net.url.URLContentImpl;
import com.volantis.shared.net.url.http.Cookie;
import com.volantis.shared.net.url.http.Header;
import com.volantis.shared.net.url.http.HttpContent;
import com.volantis.shared.net.url.http.utils.HttpClientUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.MalformedCookieException;

/**
 * HttpClient-based HttpContent implementation that reads an HttpMethod to serve
 * the requests.
 */
public class HttpClientHttpContent extends URLContentImpl implements HttpContent {

    /**
     * List of headers. The elements of the list are {@link Header} objects.
     */
    private final List headers;

    /**
     * List of cookies. The elements of the list are {@link Cookie} objects.
     */
    private final LinkedList cookies;

    /**
     * The HttpMethod that provides the response values.
     */
    private final HttpMethod method;
    
    /**
     * Initialise.
     *
     * @param method The method that contains the response.
     */
    public HttpClientHttpContent(final HttpMethod method) throws IOException {
        this.method = method;
        headers = new LinkedList();
        cookies = new LinkedList();
        processHeaders(method);
    }

    /**
     * Reads the response headers and either creates Cookie objects or Header
     * objects of them.
     *
     * @param method the HttpMethod with the response headers
     * @throws MalformedCookieException if any of the cookies has invalid format
     */
    private void processHeaders(final HttpMethod method)
            throws MalformedCookieException {
        final org.apache.commons.httpclient.Header[] httpHeaders =
            method.getResponseHeaders();
        for (int i= 0; i < httpHeaders.length; i++) {
            final org.apache.commons.httpclient.Header httpHeader =
                httpHeaders[i];
            final String headerName = httpHeader.getName();
            if ("Set-Cookie".equalsIgnoreCase(headerName)) {
                final org.apache.commons.httpclient.Cookie[] httpCookies =
                    HttpClientUtils.createCookieArray(method, httpHeader);
                final long baseTime = System.currentTimeMillis();
                for (int j = 0; j < httpCookies.length; j++) {
                    final org.apache.commons.httpclient.Cookie httpCookie =
                        httpCookies[j];
                    final Cookie cookie = new CookieImpl(httpCookie, baseTime);
                    cookies.add(cookie);
                }
            } else {
                final String headerValue = httpHeader.getValue();
                final Header header = new HeaderImpl(headerName, headerValue);
                headers.add(header);
            }
        }
    }

    // Javadoc inherited.
    public InputStream getInputStream() throws IOException {
        return method.getResponseBodyAsStream();
    }

    // Javadoc inherited.
    public String getCharacterEncoding() {
        // todo parse the Content-Type header correctly.
        return null;
    }

    // Javadoc inherited.
    public String toString() {
        String uri;
        try {
            uri = method.getURI().getURI();
        } catch (URIException e) {
            uri = e.getMessage();
        }
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) +
                " [" + uri + "]";
    }

    // Javadoc inherited.
    public int getStatusCode() {
        return method.getStatusCode();
    }

    // Javadoc inherited.
    public Iterator getCookies() {
        return Collections.unmodifiableList(cookies).iterator();
    }

    // Javadoc inherited.
    public Iterator getHeaders() {
        return Collections.unmodifiableList(headers).iterator();
    }

    // Javadoc inherited.
    public String getHttpVersion() {
        return method.getStatusLine().getHttpVersion();
    }

    // Javadoc inherited.
    public Dependency getDependency() {
        return UncacheableDependency.getInstance();
    }
}
