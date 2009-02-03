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

package com.volantis.shared.net.http.client;

import org.apache.commons.httpclient.HttpMethod;

import java.io.IOException;

import com.volantis.shared.net.http.HttpStatusCode;

/**
 * A wrapper around the Apache HTTP Client that supports a round trip timeout.
 *
 * <p>As the Apache HTTP Client does not support round trip timeouts and has
 * poor support for connection timoouts (it creates a thread for each request
 * which may hang around for a long time) we need to build support for them on
 * top of the HTTP Client. Unfortunately, JDK 1.3 does not provide the
 * necessary low level capability (interruptible I/O) to do this properly.
 * Therefore, there are two specialisations of this one for JDK 1.3 and the
 * other for JDK 1.4 and above.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface HttpClient {

    /**
     * Execute the HTTP Method.
     *
     * <p>Once the caller has finished with the method it must call
     * {@link HttpMethod#releaseConnection()}.</p> 
     *
     * @param method The method to execute.
     * @return The HTTP status code, e.g. 200 for success, 404 for missing
     *         resource.
     * @throws java.io.InterruptedIOException If the execution timed out.
     * @throws java.io.IOException            For any other I/O problem.
     */
    HttpStatusCode executeMethod(HttpMethod method)
            throws IOException;
}
