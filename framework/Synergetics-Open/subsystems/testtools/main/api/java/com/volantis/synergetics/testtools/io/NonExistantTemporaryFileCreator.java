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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools.io;

import com.volantis.synergetics.testtools.io.TemporaryFileCreator;

import java.io.File;
import java.io.IOException;

/**
 * Create a temporary File which points to a file which does not exist.
 */ 
public class NonExistantTemporaryFileCreator 
        implements TemporaryFileCreator {
        
    public File createTemporaryFile() throws IOException {
        // Create a temporary file as per normal.
        String prefix = "non-existant-temporary-file-creator";
        File tempFile = File.createTempFile(prefix, null, null);
        // Now delete it.
        if (!tempFile.delete()) {
            throw new IllegalStateException("Can't delete temporary file");
        }
        // And return the path to the non-existant temporary file.
        return tempFile;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Oct-03	1729/1	geoff	VBM:2003102302 Handle device repository versions

 ===========================================================================
*/
