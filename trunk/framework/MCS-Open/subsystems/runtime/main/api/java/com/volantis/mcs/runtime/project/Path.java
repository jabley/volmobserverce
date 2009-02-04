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

package com.volantis.mcs.runtime.project;

import java.io.IOException;
import java.io.InputStream;

/**
 * An abstract representation of a file system like path.
 *
 * @mock.generate
 */
public interface Path {

    /**
     * Get the parent path.
     *
     * <p>The parent path is the path that contains this path.</p>
     *
     * @return The parent path, or null if it could not be found.
     */
    Path getParentPath();

    /**
     * Get the path for the child with the specified name.
     *
     * @param name The name of the child of this path.
     * @return The child path.
     */
    Path getChild(String name);

    /**
     * Open a stream to the path.
     *
     * @return The stream to the path.
     * @throws IOException If there was a problem opening the stream.
     */
    InputStream openStream() throws IOException;

    /**
     * Get a string representation of the path.
     *
     * @return The string representation of the path.
     */
    String toExternalForm() throws IOException;

}
