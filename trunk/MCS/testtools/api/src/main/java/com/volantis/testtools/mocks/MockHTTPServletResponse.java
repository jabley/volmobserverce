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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.mocks;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TimeZone;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Supporting class for unit tests
 */
public class MockHTTPServletResponse implements HttpServletResponse {
    private static final DateFormat RFC1123 =
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.UK);

    static {
        RFC1123.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private Map headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    private int statusCode;
    private String statusMessage;

    /**
     * Adds the given value to the set of values for the given header.
     *
     * @param headerName name of the header
     * @param headerValue value to add to the header
     */
    public void addHeader(final String headerName, final String headerValue) {
        List values = (List) headers.get(headerName);
        if (values == null) {
            values = new ArrayList();

            headers.put(headerName, values);
        }

        values.add(headerValue);
    }

    public Iterator getHeaderNames() {
        return headers.keySet().iterator();
    }

    public Iterator getHeaders(final String s) {
        if (headers.get(s) == null) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            return ((List) headers.get(s)).iterator();
        }
    }

    public void addCookie(final Cookie cookie) {
        throw new UnsupportedOperationException();
    }

    public boolean containsHeader(final String name) {
        return headers.containsKey(name);
    }

    public String encodeURL(final String url) {
        return url;
    }

    public String encodeRedirectURL(final String url) {
        return url;
    }

    public String encodeUrl(final String url) {
        return url;
    }

    public String encodeRedirectUrl(final String url) {
        return url;
    }

    public void sendError(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.statusMessage = message;
    }

    public void sendError(final int statusCode) throws IOException {
        this.statusCode = statusCode;
    }

    public void sendRedirect(String uri) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void setDateHeader(final String name, final long inMillis) {
        headers.remove(name);
        synchronized (RFC1123) {
            addHeader(name, RFC1123.format(new Date(inMillis)));
        }
    }

    public void addDateHeader(final String name, final long inMillis) {
        synchronized (RFC1123) {
            addHeader(name, RFC1123.format(new Date(inMillis)));
        }
    }

    public void setHeader(final String name, final String value) {
        headers.remove(name);
        addHeader(name, value);
    }

    public void setIntHeader(final String name, final int value) {
        headers.remove(name);
        addHeader(name, Integer.toString(value));
    }

    public void addIntHeader(final String name, final int value) {
        addHeader(name, Integer.toString(value));
    }

    public void setStatus(final int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatus(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.statusMessage = message;
    }

    public int getStatus() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void flushBuffer() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void resetBuffer() {
        throw new UnsupportedOperationException();
    }

    public int getBufferSize() {
        throw new UnsupportedOperationException();
    }

    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    public PrintWriter getWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        throw new UnsupportedOperationException();
    }

    public void setBufferSize(final int size) {
        throw new UnsupportedOperationException();
    }

    public void setContentLength(final int length) {
        throw new UnsupportedOperationException();
    }

    public void setContentType(final String contentType) {
        throw new UnsupportedOperationException();
    }

    public void setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    public void setCharacterEncoding(String charset) {
        throw new UnsupportedOperationException();
    }
}
