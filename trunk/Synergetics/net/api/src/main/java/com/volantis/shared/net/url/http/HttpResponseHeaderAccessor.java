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

/**
 * Accessor interface to read HTTP headers from an HTTP response
 */
public interface HttpResponseHeaderAccessor {
    /**
     * The protocol used.
     *
     * @return the protocol used
     */
    String getProtocol();

    /**
     * Returns the value of the header with the specified name.
     *
     * <p>If there was no such header in the response, the method returns
     * null.</p>
     *
     * <p>If the response contains more than one header with the specified name,
     * the returned value contains all of those header values concatenated by
     * commas (",").</p>
     *
     * @param headerName the name of the header
     * @return the value of the header or null
     */
    String getResponseHeaderValue(String headerName);
}
