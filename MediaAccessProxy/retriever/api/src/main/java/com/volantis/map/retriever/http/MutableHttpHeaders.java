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

/**
 * This interface represents a set of HTTP Headers that may be modified.
 * 
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong></p>
 */
public interface MutableHttpHeaders extends HttpHeaders {

    /**
     * Sets a single HTTP Header or adds multiple values to a header.
     *
     * <p>If this method is called multiple times with the same name it creates
     * a multi value header.</>
     * 
     * @param name The name of the header
     * @param value The value of the header
     */
    public void addHeader(String name, String value);

    /**
     * Remove a single or multi value HTTP Header with the given name.
     * 
     * @param name The name of the header
     */
    public void removeHeader(String name);

}
