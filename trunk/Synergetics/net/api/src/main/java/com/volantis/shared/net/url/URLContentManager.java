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

package com.volantis.shared.net.url;

import com.volantis.shared.time.Period;

import java.io.IOException;
import java.net.URL;

/**
 * Provides a single point of access to content accessed via a URL.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface URLContentManager {

    /**
     * Get the content from the URL specified as a string.
     *
     * @param url     The URL as a string.
     * @param timeout The round trip timeout, that will limit the time spent
     *                attempting to retrieve the content. If null then it uses
     *                the default timeout associated with this manager.
     * @param configuration additional information to get the content, may be
     *                null
     * @return The content.
     */
    URLContent getURLContent(String url, Period timeout,
                             URLConfiguration configuration)
            throws IOException;


    /**
     * Get the content from the specified URL.
     *
     * @param url     The URL whose content is requested.
     * @param timeout The round trip timeout, that will limit the time spent
     *                attempting to retrieve the content. If null then it uses
     *                the default timeout associated with this manager.
     * @param configuration additional information to get the content, may be
     *                null
     * @return The content.
     */
    URLContent getURLContent(URL url, Period timeout,
                             URLConfiguration configuration)
            throws IOException;
}
