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
package com.volantis.shared.net.url.http;

import com.volantis.synergetics.factory.MetaDefaultFactory;

import our.apache.commons.httpclient.HttpMethod;

/**
 * A general response header accessor factory designed to produce
 * responseHeaderAccessors for a variety of reponse types.
 */
public abstract class ResponseHeaderAccessorFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
            new MetaDefaultFactory(
                "com.volantis.shared.net.impl.url.http." +
                    "DefaultResponseHeaderAccessorFactory",
                ResponseHeaderAccessorFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ResponseHeaderAccessorFactory getDefaultInstance() {
        return (ResponseHeaderAccessorFactory) metaDefaultFactory
            .getDefaultFactoryInstance();
    }

    /**
     * Create a {@link HttpResponseHeaderAccessor}.
     *
     * @param method the Http method to use
     * @return A {@link HttpResponseHeaderAccessor}.
     */
    public abstract HttpResponseHeaderAccessor
    createHttpClientResponseHeaderAccessor(HttpMethod method);
}
