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

package com.volantis.shared.net.impl.http.client;

import com.volantis.shared.net.http.client.HttpClient;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.net.http.client.HttpClientFactory;

/**
 * Base class for all {@link HttpClientFactory}.
 */
public abstract class AbstractHttpClientFactory
        extends HttpClientFactory {

    // Javadoc inherited.
    public HttpClientBuilder createClientBuilder() {
        return new HttpClientBuilderImpl(this);
    }

    /**
     * Create the client from the builder.
     *
     * @param builder The builder.
     * @return The newly constructed client.
     */
    public abstract HttpClient createClient(HttpClientBuilder builder);
}
