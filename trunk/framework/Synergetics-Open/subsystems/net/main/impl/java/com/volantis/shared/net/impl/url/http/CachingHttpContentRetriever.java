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

import com.volantis.cache.Cache;
import com.volantis.shared.net.impl.url.InternalURLContentManager;
import com.volantis.shared.net.url.http.HttpContent;
import com.volantis.shared.net.url.http.HttpUrlConfiguration;
import com.volantis.shared.time.Period;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.throwable.ExtendedRuntimeException;

import java.io.IOException;
import java.net.URL;

/**
 * HttpContentRetriever that uses a cache to store the responses.
 */
public class CachingHttpContentRetriever implements HttpContentRetriever {

    /**
     * The content manager to retrieve the content.
     */
    private final InternalURLContentManager manager;

    /**
     * The cache to store the HTTP contents.
     */
    private final Cache cache;

    /**
     * Creates a CachingHttpContentRetriever instance.
     *
     * @param manager the manager to get the content
     * @param cache the cache to store the content
     */
    public CachingHttpContentRetriever(final InternalURLContentManager manager,
                                       final Cache cache) {
        this.cache = cache;
        this.manager = manager;
    }

    // javadoc inherited
    public HttpContent retrieve(final URL url, final Period timeout,
                                final HttpUrlConfiguration httpConfig)
            throws IOException {
        HttpContent httpContent = null;
        // check the dependency context first as the dependency might stored
        // the content there on a revalidate
        if (httpConfig != null) {
            final DependencyContext dependencyContext =
                httpConfig.getDependencyContext();
            if (dependencyContext != null) {
                httpContent = (HttpContent)
                    dependencyContext.removeProperty(url);
            }
        }
        if (httpContent == null) {
            try {
                httpContent = (HttpContent) cache.retrieve(url,
                    new CachingObjectProvider(cache, cache.getRootGroup(), manager,
                        timeout, httpConfig));
            } catch(Exception e) {                
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else if(cause instanceof Error) {
                    throw (Error) cause;
                } else if (cause instanceof IOException) {
                    throw (IOException) cause;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
        return httpContent;
    }
}
