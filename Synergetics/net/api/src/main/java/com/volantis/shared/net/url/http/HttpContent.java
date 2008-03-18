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
package com.volantis.shared.net.url.http;

import com.volantis.shared.net.url.URLContent;

import java.util.Iterator;

/**
 * Extension of the URLContent interface for HTTP contents.
 */
public interface HttpContent extends URLContent {
    /**
     * The HTTP status code of the response.
     *
     * @return the status code
     */
    public int getStatusCode();

    /**
     * Iterator over the cookies sent with the HTTP response. The elements of
     * the iterator are {@link Cookie} objects.
     *
     * <p>Never returns null.</p>
     *
     * @return the iterator over the cookies
     */
    public Iterator getCookies();

    /**
     * Iterator over the headers sent with the HTTP response. The elements of
     * the iterator are {@link Header} objects.
     *
     * <p>Never returns null.</p>
     *
     * @return the iterator over the headers
     */
    public Iterator getHeaders();

    /**
     * The version of the HTTP response (e.g. "HTTP/1.0" or "HTTP/1.1").
     *
     * <p>Never returns null.</p>
     *
     * @return the http version
     */
    public String getHttpVersion();
}
