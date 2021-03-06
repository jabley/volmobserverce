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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.impl.url.http;

import com.volantis.shared.net.url.http.ResponseHeaderAccessorFactory;
import com.volantis.shared.net.url.http.HttpResponseHeaderAccessor;

import org.apache.commons.httpclient.HttpMethod;

/**
 * Default implementation of the factory.
 */
public class DefaultResponseHeaderAccessorFactory extends
    ResponseHeaderAccessorFactory {

    /**
     * Return a
     * @param method
     * @return
     */
    public HttpResponseHeaderAccessor createHttpClientResponseHeaderAccessor(
        HttpMethod method) {
        return new HttpClientResponseHeaderAccessor(method);
    }
}
