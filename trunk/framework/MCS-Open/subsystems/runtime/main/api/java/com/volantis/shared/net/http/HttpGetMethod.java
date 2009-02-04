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

package com.volantis.shared.net.http;

import org.apache.commons.httpclient.Header;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstraction of a HTTP GET Method.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface HttpGetMethod {

    /**
     * Add the header to the request,
     *
     * <p>Must be called before {@link #execute()}.</p>
     *
     * @param headerName The name of the header.
     * @param value      The value.
     */
    void addRequestHeader(String headerName, String value);

    /**
     * Execute the method.
     *
     * @return The status code.
     * @throws IOException If there was a problem.
     */
    HttpStatusCode execute()
            throws IOException;

    /**
     * Get the body of the response as a stream.
     *
     * <p>Must only be called after {@link #execute()}.</p>
     *
     * @return A stream that provides access to the bodt.
     * @throws IOException If there was a problem.
     */
    InputStream getResponseBodyAsStream()
            throws IOException;

    /**
     * Get the header from the response.
     * <p>Must only be called after {@link #execute()}.</p>
     *
     * @param headerName The name of the header.
     * @return The contents of the header.
     */
    Header getResponseHeader(String headerName);

    /**
     * Release the connection.
     *
     * <p>Must always be called after the method has been created,
     */
    void releaseConnection();
}
