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

package com.volantis.mps.servlet;

import java.io.File;

/**
 * This helper class provides some common functionality WRT creating and
 * managing test message stores for the unit and integration tests that
 * require it.
 */
public class MessageStoreTestHelper {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The temporary directory defined in the current system.  This is used
     * for any of the tests that need to generate files.
     */
    public static final String TMPDIR = System.getProperty("java.io.tmpdir");
    
    /**
     * The message store location used for the tests.  It exists within the
     * {@link #TMPDIR} and is in a subdirectory whose name includes a random
     * number to try and ensure uniqueness.
     */
    public static final String MESSAGE_STORE_LOCATION =
            TMPDIR + System.getProperty("file.separator") +
            "store" + Math.random();

    /**
     * A file that represents the {@link #MESSAGE_STORE_LOCATION} which can
     * be used to create/delete the contents without creating a new file
     * reference each time.
     */
    public static final File MESSAGE_STORE_DIR =
            new File(MESSAGE_STORE_LOCATION);

    /**
     * The file separator used for building file paths for the current system. 
     */
    public static final String FILE_SEPARATOR =
            System.getProperty("file.separator");

    /**
     * A utility method that given a file system reference as a File will delete
     * it and if it is a directory recusively delete the contents too.
     * <p>
     * <strong>Limited error checking or validation is done</strong> so use
     * this method with care!
     * </p>
     *
     * @param path       The file/directory to delete
     */
    public static void deleteDir(File path) throws Exception {
        if (path != null) {
            // Handle deleting contents (if any)
            File[] files = path.listFiles();

            if (files != null) {
                for (int i = 0; i < files.length; ++i) {
                    if(files[i].isDirectory()) {
                        deleteDir(files[i]);
                    }
                    files[i].delete();
                }
            }
            // Now delete the directory itself
            path.delete();
        }
    }

    /**
     * This creates a directory as specified by the path.  If any other
     * directories in the hierarchy are also required it will create those.
     * <p>
     * <strong>Limited error checking or validation is done</strong> so use
     * this method with care!
     * </p>
     *
     * @param path The directory path to create.
     */
    public static void createDir(File path) throws Exception {
        if (path != null) {
            path.mkdirs();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-04	236/1	philws	VBM:2004111209 Re-worked MPS to use new build

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 ===========================================================================
*/
