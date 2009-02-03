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

import com.volantis.shared.net.url.http.Cookie;
import com.volantis.shared.net.url.http.utils.HttpClientUtils;


/**
 * Implementation of the {@link Cookie} interface that delegates calls to an
 * HttpClient cookie.
 */
public class CookieImpl implements Cookie {
    /**
     * The HttpClient cookie to delegate the calls to.
     */
    private final org.apache.commons.httpclient.Cookie cookie;

    /**
     * Base time to be used for max-age calculations.
     */
    private final long baseTime;

    /**
     * Creates a delegating Cookie implementation.
     *
     * @param cookie the HttpCookie to delegate to
     * @param baseTime the base time to be used to compute max-age
     */
    public CookieImpl(org.apache.commons.httpclient.Cookie cookie,
                      long baseTime) {
        this.cookie = cookie;
        this.baseTime = baseTime;
    }

    // javadoc inherited
    public boolean isSecure() {
        return cookie.getSecure();
    }

    // javadoc inherited
    public String getComment() {
        return cookie.getComment();
    }

    // javadoc inherited
    public String getPath() {
        return cookie.getPath();
    }

    // javadoc inherited
    public String getDomain() {
        return cookie.getDomain();
    }

    // javadoc inherited
    public String getName() {
        return cookie.getName();
    }

    // javadoc inherited
    public String getValue() {
        return cookie.getValue();
    }

    // javadoc inherited
    public int getVersion() {
        return cookie.getVersion();
    }

    // javadoc inherited
    public int getMaxAge() {
        return HttpClientUtils.calculateResponseCookieMaxAge(cookie, baseTime);
    }
}
