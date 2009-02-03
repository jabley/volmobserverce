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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntities;

import java.io.InputStream;

/**
 * Adapter interface that allows the Response of a HTTP Request to be read
 */
public interface HTTPResponseAccessor {

    /**
     * Get the cookies jar into which the web driver should add cookies returned
     * by the server.
     * @return The CookieJar that the web driver should use.
     */
    public HTTPMessageEntities getCookies() throws HTTPException;

    /**
     * Get the headers into which the web driver should add headers returned
     * by the server.
     * @return The headers that the web driver should use.
     */
    public HTTPMessageEntities getHeaders() throws HTTPException;

    /**
     * Get the status code returned by the server.
     * @return The status code returned by the server.
     */
    public int getStatusCode();

    /**
     * Return the HTTP version returned by the server.
     * @return The HTTP version returned by the server.
     */
    public HTTPVersion getHTTPVersion();

    /**
     * Allows the body of the response to be accessed.
     * @return an InputStream that allows the body of the response to be
     * accessed.
     */
    public InputStream getResponseStream() throws HTTPException;
}
