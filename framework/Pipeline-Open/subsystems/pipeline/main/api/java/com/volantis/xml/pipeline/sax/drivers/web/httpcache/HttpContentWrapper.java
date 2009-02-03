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

import com.volantis.xml.pipeline.sax.drivers.web.HTTPException;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPResponseAccessor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPVersion;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.cookies.CookieVersion;
import com.volantis.shared.net.url.http.HttpContent;
import com.volantis.shared.net.url.http.Cookie;
import com.volantis.shared.net.url.http.Header;
import com.volantis.shared.net.url.http.utils.HttpClientUtils;

import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpException;

/**
 * HttpContent wrapper to implement the HTTPResponseAccessor interface.
 */
public class HttpContentWrapper implements HTTPResponseAccessor {
    private static final HTTPFactory FACTORY = HTTPFactory.getDefaultInstance();

    /**
     * The wrapped HttpContent object.
     */
    private final HttpContent httpContent;

    /**
     * The extracted cookies (lazily initiated).
     */
    private HTTPMessageEntities cookies;

    /**
     * The extracted headers (lazily initiated).
     */
    private HTTPMessageEntities headers;

    /**
     * Creates a new wrapper.
     *
     * @param httpContent the HttpContent to wrap
     */
    public HttpContentWrapper(final HttpContent httpContent) {
        this.httpContent = httpContent;
    }

    // javadoc inherited
    public HTTPMessageEntities getCookies() throws HTTPException {
        if (cookies == null) {
            cookies = FACTORY.createHTTPMessageEntities();
            for (Iterator iter = httpContent.getCookies(); iter.hasNext(); ) {
                final Cookie cookie = (Cookie) iter.next();
                final com.volantis.shared.net.http.cookies.Cookie cookieEntity =
                    FACTORY.createCookie(
                        cookie.getName(), cookie.getDomain(), cookie.getPath());
                cookieEntity.setComment(cookie.getComment());
                cookieEntity.setMaxAge(cookie.getMaxAge());
                cookieEntity.setSecure(cookie.isSecure());
                cookieEntity.setValue(cookie.getValue());
                cookieEntity.setVersion(
                    CookieVersion.getCookieVersion(cookie.getVersion()));
                cookies.add(cookieEntity);
            }
        }
        return cookies;
    }

    // javadoc inherited
    public HTTPMessageEntities getHeaders() throws HTTPException {
        if (headers == null) {
            headers = FACTORY.createHTTPMessageEntities();
            for (Iterator iter = httpContent.getHeaders(); iter.hasNext(); ) {
                final Header header = (Header) iter.next();
                final List values;
                try {
                    values = HttpClientUtils.getHeaderValueList(
                        new org.apache.commons.httpclient.Header(
                            header.getName(), header.getValue()));
                } catch (HttpException e) {
                    throw new HTTPException(e);
                }
                for (Iterator valuesIter = values.iterator();
                     valuesIter.hasNext(); ) {
                    final com.volantis.shared.net.http.headers.Header entity =
                        FACTORY.createHeader(header.getName());
                    final String value = (String) valuesIter.next();
                    entity.setValue(value);
                    headers.add(entity);
                }
            }
        }
        return headers;
    }

    // javadoc inherited
    public int getStatusCode() {
        return httpContent.getStatusCode();
    }

    // javadoc inherited
    public HTTPVersion getHTTPVersion() {
        return HTTPVersion.httpVersion(httpContent.getHttpVersion());
    }

    // javadoc inherited
    public InputStream getResponseStream() throws HTTPException {
        try {
            return httpContent.getInputStream();
        } catch (IOException e) {
            throw new HTTPException(e);
        }
    }
}
