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

package com.volantis.mcs.runtime;

import java.net.URL;

/**
 * Provides facilities to map from external path to the internal path.
 *
 * <p>An external path forms part of the URL that a client may use to access
 * a resource on the server. An internal URL is the URL used within the server
 * to access a resource.</p>
 *
 * @mock.generate 
 */
public interface ExternalPathToInternalURLMapper {

    /**
     * Map an external relative path to an internal URL.
     *
     * @param path The external path.
     * @return The internal URL.
     */
    URL mapExternalPathToInternalURL(String path);

    /**
     * Map an internal URL to an external URL.
     *
     * @param urlAsString The internal URL.
     * @return The external path.
     */
    String mapInternalURLToExternalPath(String urlAsString);
}
