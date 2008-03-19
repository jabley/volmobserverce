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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Creates a temporary file from a jar resource.
 */ 
public class ResourceTemporaryFileCreator implements TemporaryFileCreator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Class to look up the resource relative to.
     */ 
    private Class clazz;
    
    /**
     * Path to the jar resource.
     */ 
    private String resource;

    /**
     * Create an instance of this class, using the path to the jar resource
     * provided.
     * 
     * @param resource the path to the jar resource that this class creates
     *      temporary files from.
     */ 
    public ResourceTemporaryFileCreator(Class clazz, String resource) {
        
        this.clazz = clazz;
        this.resource = resource;
        
    }

    public File createTemporaryFile() throws IOException {
        
        // Create a jar URL to the resource we want to extract.
        URL url = clazz.getResource(resource);
        if (url == null) {
            throw new IOException("No resource available from '" +
                    clazz.getName() + "' for '" + resource + "'");
        }

        // Figure out the extension to use for the temporary file.
        String extension = getExtension(resource);

        // Create the temporary file.
        String prefix = "resource-temporary-file-creator";
        File file = File.createTempFile(prefix, extension);
        
        // Copy the resource input to the file output.
        InputStream input = url.openConnection().getInputStream();
        FileOutputStream output = new FileOutputStream(file);
        com.volantis.synergetics.io.IOUtils.copyAndClose(input, output);

        return file;
        
    }

    /**
     * Calculate and return the extension of the filename.
     * <p>
     * If one cannot be found an empty string will be returned. This is so that
     * {@link File#createTempFile} does not use a default extension in this
     * case.
     *
     * @param fileName the name of the file
     * @return the extension of the file, or an empty string.
     */
    private String getExtension(String fileName) {

        String extension = null;

        int dot = fileName.lastIndexOf('.');
        if (dot > 0) {
            // Extract the extension.
            extension = fileName.substring(dot);
        } else {
            // Else explicitly use no extension. Otherwise createTempFile
            // would give us a default of .tmp.
            extension = "";
        }

        return extension;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Jul-05	9033/1	allan	VBM:2005071312 Move IOUtils.java that is in cornerstone into Synergetics

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 16-Apr-04	3740/3	allan	VBM:2004040508 UpdateClient/Server enhancements & fixes.

 29-Oct-03	1599/2	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 ===========================================================================
*/
