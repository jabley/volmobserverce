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
package com.volantis.map.ics.imageprocessor.utilities;

import java.io.File;

/**
 * This class is responsible for providing convenience methods for use on
 * instances of File that are not directly supported in <code>File</code>
 */
public class FileUtilities {

    /**
     * Returns the extension of the supplied file.
     *
     * @param theFile file for which the extension is required
     * @return the extension of the supplied file, eg, svg, png, doc
     */
    public static String getExtension(File theFile) {

        String path = theFile.getPath();
        // find the last occurence of '.'
        int indexOfLastDot = path.lastIndexOf('.');
        // get the extension
        return path.substring(indexOfLastDot + 1);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Mar-05	311/3	rgreenall	VBM:2005012701 Calling writeImage in concrete implementations of AbstractImageWriter no longer results in the image being converted twice.

 21-Feb-05	311/1	rgreenall	VBM:2005012701 Resolved conflicts

 ===========================================================================
*/
