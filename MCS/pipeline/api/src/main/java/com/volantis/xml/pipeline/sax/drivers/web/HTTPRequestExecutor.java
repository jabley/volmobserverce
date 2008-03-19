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

import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.cookies.Cookie;

/**
 * Command Interface that allows a HTTP request to be performed.
 */
public interface HTTPRequestExecutor {

    /**
     * Executes the HTTP request
     * @return a <code>HTTPResponseAccessor</code> instance that allows
     * the response to be accessed.
     * @throws HTTPException if an error occurs
     */
    public HTTPResponseAccessor execute() throws HTTPException;

    /**
     * Resease the HTTPRequestExecutor after the request has been exectuted.
     * @throws HTTPException if an error occurs
     */
    public void release() throws HTTPException;

    /**
     * Adds a {@link com.volantis.shared.net.http.parameters.RequestParameter} to the request before it is executed
     * @param parameter the parameter that is to be added.
     * @throws HTTPException if an error occurs.
     */
    public void addRequestParameter(RequestParameter parameter)
            throws HTTPException;

    /**
     * Adds a {@link com.volantis.shared.net.http.headers.Header} to the request before it is executed
     * @param header the header that is to be added.
     * @throws HTTPException if an error occurs
     */
    public void addRequestHeader(Header header) throws HTTPException;

    /**
     * Adds the {@link com.volantis.shared.net.http.cookies.Cookie} to the request before it is executed
     * @param cookie the cookie that is  to be added.
     * @throws HTTPException if an error occurs
     */
    public void addRequestCookie(Cookie cookie) throws HTTPException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Feb-05	6976/1	matthew	VBM:2005020308 Add HTTP Caching mechanism

 10-Feb-05	1005/1	matthew	VBM:2005020308 Added http cache and refactored AbtractPluggableHTTPManager

 ===========================================================================
*/
