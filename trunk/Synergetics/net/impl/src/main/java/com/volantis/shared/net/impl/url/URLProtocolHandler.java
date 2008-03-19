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

package com.volantis.shared.net.impl.url;

import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.time.Period;

import java.io.IOException;
import java.net.URL;

/**
 * Abstracts the retrieval of content from a URL.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface URLProtocolHandler {

    /**
     * Connect to the remote resource, retrieve the content and encapsulate it
     * within a {@link URLContent} instance.
     *
     * @param url     The URL of the resource.
     * @param timeout The timeout, will not be null.
     * @param configuration additional information to get the content, may be
     * null
     * @return The {@link URLContent}.
     * @throws IOException If there was a problem retrieving the content.
     */
    URLContent connect(URL url, Period timeout, URLConfiguration configuration)
            throws IOException;      
}
