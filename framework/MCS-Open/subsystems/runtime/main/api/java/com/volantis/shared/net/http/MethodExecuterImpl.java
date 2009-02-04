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

import com.volantis.shared.net.http.client.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

/**
 * Default implementation of {@link MethodExecuter}.
 */
public class MethodExecuterImpl
        implements MethodExecuter {

    /**
     * Underlying HTTP client.
     */
    private final HttpClient client;

    /**
     * Initialise.
     *
     * @param client The underlying HTTP client.
     */
    public MethodExecuterImpl(HttpClient client) {
        this.client = client;
    }

    // Javadoc inherited.
    public HttpStatusCode execute(GetMethod method)
            throws IOException {

        return client.executeMethod(method);
    }
}
