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
package com.volantis.testtools.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @todo Move to Synergetics TestTools
 */

/**
 * Provides useful utilities to manipulate resources.
 * <p>
 * NOTE: ResourceTemporaryFileCreator does the same thing, but does not rely
 * on deleteOnExit().
 * <p>
 * NOTE: this class doesn't seem to work when run in a testcase under IDEA.
 */
public class ResourceUtilities {

    /**
     * Creates a temporary file in the system's default temporary files directory,
     * based on the name of the resource. The temporary file contains a copy of
     * the data in the resource. The file's corresponding File object is returned.
     * The removal of the temporary file is automatically handled. If a file cannot
     * be generated, null is returned.
     * @param resourcePath The pathname of the resource.
     * @return The File object encapsulating the temporary file.
     */
    public static File getResourceAsFile(String resourcePath)
            throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        URL url = loader.getResource(resourcePath);
        if (url == null) {
            url = ResourceUtilities.class.getClassLoader().getResource(
                resourcePath);
        }
        if (url == null) {
            return null;
        }
        try {
            File file = new File(resourcePath);
            String filename = file.getName();
            String suffix = null;
            String prefix = null;

            int idx = filename.lastIndexOf('.');
            if (idx > -1) {
                suffix = ".tmp" + filename.substring(idx);
                prefix = "temp." + filename.substring(0, idx);
            } else {
                suffix = null;
                prefix = "temp." + filename;
            }

            File tmpFile = File.createTempFile(prefix, suffix);
            tmpFile.deleteOnExit();

            is = url.openStream();
            fos = new FileOutputStream(tmpFile);
            copy(is, fos);
            fos.flush();
            fos.close();
            is.close();
            return tmpFile;
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Copies a stream of bytes from an InputStream to an OutputStream until EOF.
     * @param from The stream from which to read the data.
     * @param to The stream to write to.
     * @throws java.io.IOException
     */
    private static void copy(InputStream from, OutputStream to) throws IOException {
        final int BUF_SIZE = 4096;
        byte[] b = new byte[BUF_SIZE];
        int numRead;
        while ((numRead = from.read(b)) != -1) {
            to.write(b, 0, numRead);
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 01-Dec-03	2067/1	allan	VBM:2003111911 Rework design making ODOMEditorContext immutable.

 12-Nov-03	1823/2	steve	VBM:2003110513 ImageAssetDetailsFactory implementation

 06-Nov-03	1795/1	pcameron	VBM:2003102804 Added ImageProvider infrastructure and implementation

 ===========================================================================
*/
