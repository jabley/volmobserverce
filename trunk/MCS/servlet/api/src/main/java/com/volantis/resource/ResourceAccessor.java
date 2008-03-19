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

package com.volantis.resource;

import java.io.InputStream;

/**
 * Implementations of this are responsible for providing access to resources.
 */
public interface ResourceAccessor {

    /**
     * Open an input stream to the resource.
     *
     * <p>If no such stream could be opened then return null.</p>
     *
     * @param projectRelativePath The project relative path to the resource.
     * @return The stream, or null if it could not be opened.
     */
    InputStream getResourceAsStream(String projectRelativePath);
}
