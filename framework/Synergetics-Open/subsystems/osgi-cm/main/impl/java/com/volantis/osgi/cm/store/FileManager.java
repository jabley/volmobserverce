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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.cm.store;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Manages files within a specific directory.
 *
 * <p> <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong> </p>
 *
 * <p>Creates files automatically within a directory for the purposes of
 * persisting information. This creates a hierarchy of directories and files
 * such that each directory has at most a small number of files and a small
 * number of directories. The reason for this is that directories with large
 * numbers of files is very inefficient to search and manage.</p>
 *
 * <p>This maintains a cached representation of the file system. If when it
 * attempts to access the file system it detects an inconsistency with its in
 * memory state then it resynchronizes the cached representation, which involves
 * discarding it all and rebuilding. This could get very expensive if a lot of
 * external changes are made to the file system but that is not a supported use
 * case for this class.</p>
 *
 * @mock.generate
 */
public interface FileManager {

    /**
     * Allocate a file.
     *
     * @return The file that was allocated and created.
     * @throws java.io.IOException If there was a problem allocating the file.
     */
    File allocateFile() throws IOException;

    /**
     * Release a file that was allocated.
     *
     * @param file The file to release (and delete).
     * @return True if the file was released successfully, false if it failed
     *         for a not unexpected reason.
     * @throws java.io.IOException If there was a problem releasing the file.
     */
    boolean releaseFile(File file) throws IOException;

    /**
     * Get a relative path to the file.
     *
     * @param file The file that was allocated.
     * @return The relative path, or null if the specified file was not
     *         previously allocated.
     */
    String getRelativePath(File file);

    /**
     * List all the files managed by this.
     *
     * @return The list of files managed by this.
     */
    List listFiles();
}
