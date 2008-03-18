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
import com.volantis.shared.net.impl.url.URLProtocolHandler;
import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.http.HttpUrlConfiguration;
import com.volantis.shared.time.Period;

import java.io.IOException;
import java.net.URL;

/**
 * The handler for the 'http' and 'https' protocols.
 */
public class HttpProtocolHandler
        implements URLProtocolHandler {

    /**
     * The content retriever.
     */
    private HttpContentRetriever contentRetriever;

    /**
     * Initialise.
     *
     * @param manager The manager.
     */
    public HttpProtocolHandler(final InternalURLContentManager manager,
                               final Cache cache) {
        if (manager == null) {
            throw new IllegalArgumentException("manager cannot be null");
        }
        if (cache != null) {
            contentRetriever =
                new CachingHttpContentRetriever(manager, cache);
        } else {
            contentRetriever = new DefaultHttpContentRetriever(manager);
        }
    }

    // Javadoc inherited.
    public URLContent connect(URL url, Period timeout,
                              URLConfiguration urlConfiguration)
            throws IOException {
        return contentRetriever.retrieve(url, timeout,
            ((HttpUrlConfiguration) urlConfiguration));
    }
}
