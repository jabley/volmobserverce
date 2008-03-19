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

package com.volantis.mcs.servlet;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

/**
 * Provides methods for mapping from external URLs / paths to URLs to
 * resources.
 *
 * todo This is similar to ExternalPathToInternalURLMapper.
 *
 * @mock.generate
 */
public interface ServletResourceMapper {

    /**
     * Returns a system ID appropriate to the given request.
     *
     * @param request the request for which a system ID is required.
     * @return a system ID representing the given request
     * @throws java.net.MalformedURLException if the ID cannot be resolved
     * correctly
     */
    String getSystemIDForRequest(HttpServletRequest request)
            throws MalformedURLException;

    /**
     * Returns a system ID appropriate to the given request.
     *
     * @param contextRelativePath the context relative path to map.
     * @return a URL to the local resource.
     * @throws java.net.MalformedURLException if it could not be resolved.
     */
    String getLocalURL(String contextRelativePath)
            throws MalformedURLException;
}
