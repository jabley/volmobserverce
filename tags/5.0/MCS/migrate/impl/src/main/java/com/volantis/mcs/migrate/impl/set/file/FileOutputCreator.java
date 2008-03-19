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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.impl.set.file;

import com.volantis.mcs.migrate.api.framework.OutputCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * An output creator based on simple {@link java.io.File} objects. Generates an
 * output stream for the associated file on request.
 */
public class FileOutputCreator implements OutputCreator {
    /**
     * The file underlying this FileOutputCreator.
     */
    private File underlyingFile;

    /**
     * Create an output creator that generates output streams for the specified
     * file.
     *
     * @param file The file for which output streams should be created
     */
    public FileOutputCreator(File file) {
        underlyingFile = file;
    }

    /**
     * Generate an output stream for the underlying file.
     *
     * @return An output stream for the file underlying this FileOutputCreator
     */
    public OutputStream createOutputStream() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(underlyingFile);
        } catch (FileNotFoundException fnfe) {
            // If the file doesn't exist, return null.
        }
        return fos;
    }

    // Javadoc inherited
    public String toString() {
        return "[File output creator for file '" + underlyingFile + "']";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8181/4	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/
