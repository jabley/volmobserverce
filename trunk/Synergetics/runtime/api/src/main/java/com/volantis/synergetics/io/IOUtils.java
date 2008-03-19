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
 * $Header: /src/voyager/com/volantis/mcs/utilities/IOUtils.java,v 1.5 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-May-01    Steve           VBM:2001051702 - Created.
 * 04-Jun-01    Paul            VBM:2001050804 - Added this header and fixed a
 *                              problem with javadoc.
 * 10-Aug-01    Paul            VBM:2001072505 - Added getExtension method.
 * 08-Mar-02    Paul            VBM:2002030607 - Added deleteDirectoryContents
 *                              method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.io;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.net.URL;

/**
 * Generic IO utilities.
 */
public abstract class IOUtils {

    /**
     * Localize the exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(IOUtils.class);

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(IOUtils.class);
                          
    /**
     * Ensure this class cannot be specialized.
     */
    private IOUtils() {
    }

    /**
     * Get the extension from a string file name.
     */
    public static String getExtension(String filename) {
        String ext = null;
        int index = filename.lastIndexOf('.');
        if (index > 0 && index < filename.length() - 1) {
            ext = filename.substring(index + 1).toLowerCase();
        }

        return ext;
    }

    /**
     * Get the extension from a string file name.
     */
    public static String getExtension(File f) {
        return getExtension(f.getName());
    }

    /**
     * Delete the contents of the specified directory.
     *
     * @param directory The directory whose contents should be deleted.
     */
    public static void deleteDirectoryContents(File directory) {

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (int i = 0; i < files.length; i += 1) {
            File file = files[i];
            if (file.isDirectory()) {
                deleteDirectoryContents(file);
            }

            file.delete();
        }
    }

    /**
     * Copy one File to another overwriting the target if it already exists.
     *
     * @param source the source File.
     * @param target the target File.
     * @throws IOException If there was a problem with the copy.
     */
    public static void copyFiles(File source, File target) throws IOException {
        FileInputStream input = new FileInputStream(source);
        FileOutputStream output = new FileOutputStream(target);

        copyAndClose(input, output);
    }

    /**
     * Recursively copy the contents of the srcDir into the dstDir.
     *
     * @param srcDir the source directory whose contents you wish to copy
     * @param dstDir the destination directory to which you want to copy the
     *               contents of the src directory
     * @return true if the copy succeds, false otherwise
     *
     * @throws IOException              on error
     * @throws IllegalArgumentException if the parameters are incorrect
     */
    public static boolean copyDirectoryContent(File srcDir, File dstDir)
        throws IOException, IllegalArgumentException {
        return copyDirectoryContent(srcDir, dstDir, false);
    }

    /**
     * Recursively copy the contents of the srcDir into the dstDir. Also can
     * save last modification time of copied files/directories.
     *
     * @param srcDir the source directory whose contents you wish to copy
     * @param dstDir the destination directory to which you want to copy the
     *               contents of the src directory
     * @param preserveModificationTime if modification time of files/dirs
     *                                 should stay like in source
     * @return true if the copy succeds, false otherwise
     *
     * @throws IOException              on error
     * @throws IllegalArgumentException if the parameters are incorrect
     */
    public static boolean copyDirectoryContent(File srcDir, File dstDir,
            boolean preserveModificationTime)
        throws IOException, IllegalArgumentException {

        if (!srcDir.isDirectory()) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("file-is-not-directory", srcDir));
        }
        if (!dstDir.isDirectory()) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("file-is-not-directory", dstDir));
        }

        File[] children = srcDir.listFiles();
        boolean result = true;
        for (int i = 0; i < children.length && result; i++) {
            File child = new File(dstDir, children[i].getName());
            if (children[i].isDirectory()) {
                result = child.mkdir();
                if (result) {
                    result = copyDirectoryContent(children[i], child,
                            preserveModificationTime);
                }
            } else {
                result = child.createNewFile();
                if (result) {
                    copyFileChannel(children[i], child, preserveModificationTime);
                }
            }
            if (preserveModificationTime && result) {
                child.setLastModified(children[i].lastModified());
            }
        }
        return result;
    }

    /**
     * Copy the src file to the dst file using FileChannls. This could
     * potentially be more efficient then using streams.
     *
     * @param src the src file
     * @param dst the dst file
     * @throws IOException
     */
    public static void copyFileChannel(File src, File dst)
        throws IOException {
        copyFileChannel(src, dst, false);
    }
    
    /**
     * Copy the src file to the dst file using FileChannls. This could
     * potentially be more efficient then using streams. It can also
     * save last modification time of specified file.
     *
     * @param src the src file
     * @param dst the dst file
     * @param preserveModificationTime if should copy also last modification time
     * @throws IOException
     */
    public static void copyFileChannel(File src, File dst,
            boolean preserveModificationTime)
        throws IOException {

        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        long length = 0;
        try {
            inputChannel = new FileInputStream(src).getChannel();
            length = inputChannel.size();
            outputChannel = new FileOutputStream(dst).getChannel();

            long total = 0;
            while (total < length) {
                total += inputChannel.transferTo(0, length, outputChannel);
            }
            if (preserveModificationTime) {
                dst.setLastModified(src.lastModified());
            }
        } finally {
            if (inputChannel != null) {
                inputChannel.close();
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        }
    }

    /**
     * Copy bytes from an InputStream to an OutputStream and close both streams
     * at the end of the copy or if an exception is thrown during the copy
     * attempt.
     *
     * @param in  the source InputStream
     * @param out the target OutputStream
     * @throws IOException if there is a problem with the copy
     */
    public static void copyAndClose(InputStream in, OutputStream out)
        throws IOException {
        IOException copyException = null;
        RuntimeException runtimeException = null;
        IOException closeException = null;
        try {
            copy(in, out);
        } catch (IOException e) {
            copyException = e;
        } catch (RuntimeException e) {
            runtimeException = e;
        } finally {
            // Ensure we close the input.
            try {
                in.close();
            } catch (IOException e) {
                // Don't throw this exception to avoid masking a possible
                // exception from the copy.
                logger.error("unexpected-ioexception", e);
                if (closeException == null) {
                    closeException = e;
                }
            }
            // Ensure we close the output.
            try {
                out.close();
            } catch (IOException e) {
                // Don't throw this exception to avoid masking a possible
                // exception from the copy.
                logger.error("unexpected-ioexception", e);
                if (closeException == null) {
                    closeException = e;
                }
            }
            // Finally throw the first exception we encountered, if any.
            if (copyException != null) {
                throw copyException;
            }
            if (runtimeException != null) {
                throw runtimeException;
            }
            if (closeException != null) {
                throw closeException;
            }
        }
    }

    /**
     * Copy bytes from an InputStream an OutputStream.
     *
     * @param in  the source InputStream.
     * @param out the target OutputStream.
     * @return the number of bytes copied
     *
     * @throws IOException If there was a problem with the copy.
     */
    public static int copy(InputStream in, OutputStream out)
        throws IOException {

        byte buf[] = new byte[1024];
        int len;
        int totalBytesCopied = 0;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
            totalBytesCopied += len;
        }
        out.flush();

        return totalBytesCopied;
    }

    /**
     * Determine whether the contents of two InputStreams are equal.
     *
     * @param is1 an InputStream.
     * @param is2 another InputStream.
     * @return true if the contents of is1 are the same byte for byte as the
     *         contents of is2; false otherwise.
     *
     * @throws IOException if there was a problem reading either InputStream
     */
    public static boolean inputStreamsEqual(InputStream is1, InputStream is2)
        throws IOException {

        byte b1[] = new byte[1024];
        byte b2[] = new byte[1024];

        int count;

        boolean equals;

        do {
            count = is1.read(b1, 0, 1024);
            is2.read(b2, 0, 1024);
            equals = Arrays.equals(b1, b2);
        } while (equals && count != -1);

        return equals;
    }

    /**
     * Provide an InputStream for reading a Document.
     *
     * @param document The Document to read.
     * @return An InputStream for reading the Document.
     */
    public static InputStream createDocumentInputStream(Document document) {
        //Format format = Format.getRawFormat();
        //format.setTextMode(Format.TextMode.TRIM_FULL_WHITE);
        XMLOutputter out = new XMLOutputter();
        String docString = out.outputString(document);
        return new ByteArrayInputStream(docString.getBytes());
    }

    /**
     * Safely close the specified InputStream, ignoring any exceptions. The
     * InputStream may be already closed, or the reference may be null.
     *
     * @param is an InputStream. May be null, or already closed.
     */
    public static void closeQuietly(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                logger.warn("problem-closing-quietly", is, e);
            }
        }
    }

    /**
     * Safely close the specified OutputStream, ignoring any exceptions. The
     * OutputStream may be already closed, or the reference may be null.
     *
     * @param os an OutputStream. May be null, or already closed.
     */
    public static void closeQuietly(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                logger.warn("problem-closing-quietly", os, e);
            }
        }
    }

    /**
     * Safely close the specified Reader, ignoring any exceptions. The
     * Reader may be already closed, or the reference may be null.
     *
     * @param reader a Reader. May be null, or already closed.
     */
    public static void closeQuietly(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                logger.warn("problem-closing-quietly", reader, e);
            }
        }
    }

    /**
     * Safely close the specified Writer, ignoring any exceptions. The
     * Writer may be already closed, or the reference may be null.
     *
     * @param writer a Writer. May be null, or already closed.
     */
    public static void closeQuietly(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.warn("problem-closing-quietly", writer, e);
            }
        }
    }

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

 13-Jul-05	498/1	allan	VBM:2005071312 Move IOUtils into Synergetics

 11-Apr-05	7376/3	allan	VBM:2005031101 SmartClient bundler - commit for testing

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 10-Nov-04	6156/1	geoff	VBM:2004110904 ICS GIF support shopuld be on by default in MCS v3.2.3

 14-May-04	4346/1	mat	VBM:2004051111 Delete external themes on theme cache flush

 16-Apr-04	3740/1	allan	VBM:2004040508 UpdateClient/Server enhancements & fixes.

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 02-Mar-04	3200/3	allan	VBM:2004022410 Added file copying utils.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
