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
package com.volantis.map.retriever.http;

import java.util.Enumeration;

/**
 * This interface represents a set of HTTP Headers.
 * 
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong></p>
 */
public interface HttpHeaders {

    /**
     * Returns the value of the specified request header as a String.
     * 
     * <p>If the request did not include a header of the specified name, this
     * method returns null. The header name is case insensitive. You can use
     * this method with any request header.</p>
     * 
     * @param name a String specifying the header name
     * @return a String containing the value of the requested header, or null if
     *         the request does not have a header of that name
     */
    public String getHeader(String name);

    /**
     * Returns all the values of the specified request header as an Enumeration
     * of String objects.
     * 
     * <p>Some headers, such as Accept-Language can be sent by clients as
     * several headers each with a different value rather than sending the
     * header as a comma separated list.</p>
     *
     * <p>If the request did not include any headers of the specified name,
     * this method returns an empty Enumeration. The header name is case
     * insensitive. You can use this method with any request header.</>
     * 
     * @param name a String specifying the header name
     * @return an Enumeration containing the values of the requested header. If
     *         the request does not have any headers of that name return an
     *         empty enumeration. If the container does not allow access to
     *         header information, return null
     */
    public Enumeration getHeaders(String name);

    /**
     * Returns an enumeration of all the header names this request contains.
     *
     * <p>If the request has no headers, this method returns an empty
     * enumeration.</p>
     * 
     * @return an enumeration of all the header names sent with this request; if
     *         the request has no headers, an empty enumeration; if the servlet
     *         container does not allow servlets to use this method, null
     */
    public Enumeration getHeaderNames();

}
