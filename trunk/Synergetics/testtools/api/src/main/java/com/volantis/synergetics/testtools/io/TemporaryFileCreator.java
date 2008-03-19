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

import java.io.File;
import java.io.IOException;

/**
 * Instances of this interface create temporary files.
 * <p>
 * NOTE: instances of this class are usually used by 
 * {@link TemporaryFileManager} rather than being used directly.
 */ 
public interface TemporaryFileCreator {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create a temporary File.
     * <p>
     * This either points to a physical file which exists, or to a file which 
     * may be created by subsequent processing.
     * 
     * @return the file created.
     * @throws java.io.IOException if there was a problem creating the file.
     */ 
    File createTemporaryFile() throws IOException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Oct-03	1729/1	geoff	VBM:2003102302 Handle device repository versions

 29-Oct-03	1599/2	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 ===========================================================================
*/
