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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Class for general IO utilities.
 */
public class IOUtils {
    /**
     * Extract the zip file from a jar file (copy zip file to temporary
     * directory). Note that the returned file provides access to this
     * temporary file, which is itself marked for deletion on JVM exit.
     * <p>
     * NOTE: ResourceTemporaryFileCreator does the same thing, but does not rely
     * on deleteOnExit().
     *
     * @param clazz the class who's <code>getResource</code> method will be
     *              invoked to access the named zip file
     * @param zipFilename The name of the zip file to extract.
     * @param suffix The file suffix for the extracted zip file (e.g. zip).
     * @return the file that points to the copied zip file.
     */
    public static File extractTempZipFromJarFile(Class clazz,
                                                 String zipFilename,
                                                 String suffix)
            throws Exception {
        URL url = clazz.getResource(zipFilename);

        // Copy the zip file to a local temporary location.
        InputStream in = url.openConnection().getInputStream();

        File file = File.createTempFile("testZipFile", "." + suffix,
                new File(System.getProperty("java.io.tmpdir")));

        FileOutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();

        file.deleteOnExit();

        return file;
    }

    /**
     * Deletes all files in the specified directory and sub-directories
     *
     * @param dir to delete
     * @throws IllegalStateException if one or more file or directory cannot be
     *                               deleted
     */
    public static void deleteDir(File dir) throws IllegalStateException {
        String list[] = dir.list();

        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File file = new File(dir, list[i]);

                if (file.isDirectory()) {
                    deleteDir(file);
                } else if (!file.delete()) {
                    throw new IllegalStateException(
                            "Could not delete test file: " +
                            file.getAbsolutePath());
                }
            }

            if (!dir.delete()) {
                throw new IllegalStateException(
                        "Could not delete test directory: " +
                        dir.getAbsolutePath());
            }
        }
    }

    /**
     * Basic utility method to convert a file to a directory.
     * There is a very small chance that an intermittent exception could be
     * thrown - if the file in questions is recreated by someone else between
     * the File#delete and File#mkdirs calls.
     *
     * @param tempDir file to convert to a directory
     * @return a directory that exists
     * @throws IllegalStateException
     */
    public static File createDirectory(File tempDir) throws IllegalStateException{

        if (tempDir.exists()) {
            if (!tempDir.delete()) {
                throw new IllegalStateException(
                        "Could not delete temporary test file: " + tempDir);
            }
        }
        if (!tempDir.mkdirs()) {
            throw new IllegalStateException(
                    "Could not create temporary test directory: " + tempDir);
        }
        return tempDir;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Mar-05	6842/1	emma	VBM:2005020302 Making file references in config files relative to those files

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 18-Feb-04	3060/1	philws	VBM:2004021701 Implement runtime device repository accessor

 11-Feb-04	2862/1	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
