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
package com.volantis.shared.net.impl.url.http;

import com.volantis.shared.net.url.http.HttpResponseHeaderAccessor;

import our.apache.commons.httpclient.HttpMethod;
import our.apache.commons.httpclient.URIException;
import our.apache.commons.httpclient.Header;

/**
 * HttpClient-based HttpResponseHeaderAccessor implementation
 */
public class HttpClientResponseHeaderAccessor implements HttpResponseHeaderAccessor {
    private final HttpMethod method;

    /**
     * Creates a new instance.
     *
     * @param method the HttpMethod that contains the response headers
     */
    public HttpClientResponseHeaderAccessor(final HttpMethod method) {
        this.method = method;
    }

    // javadoc inherited
    public String getProtocol() {
        try {
            return method.getURI().getScheme();
        } catch (URIException e) {
            throw new IllegalStateException("Cannot create URI.");
        }
    }

    // javadoc inherited
    public String getResponseHeaderValue(final String headerName) {
        final Header header = method.getResponseHeader(headerName);
        final String headerValue;
        if (header != null) {
            headerValue = header.getValue();
        } else {
            headerValue = null;
        }
        return headerValue;
    }
}
