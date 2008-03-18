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
package com.volantis.mcs.accessors.xml;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Provides the ability to both read and write the contents of a zip file
 * archive in a "random access" fashion.
 * <p>
 * This is required because Java provides "serial" read and write access and 
 * "random access" for read only.
 * <p>
 * One can also think of this as a very simple read/write filesystem like 
 * interface to a zip file.
 * <p>
 * Note that this class caches all modifications until the {@link #save} 
 * method is called. 
 * <p>
 * NOTE: this class is NOT safe for multithreading. It assumes that it is only
 * accessed by one thread.
 * 
 * @todo this class could use some logging.
 */ 
public class ZipArchive {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ZipArchive.class);

    /**
     * The zip file we are managing.
     */ 
    private File archiveFile;
    
    /**
     * The map of name -> archive entries for the individual files present
     * in the zip file.
     */ 
    private Map archiveEntries;
    
    /**
     * Create an instance of this class, which manages the zip archive file of
     * the name provided.
     * <p>
     * If the zip file does not exist, it will be created when {@link #save}
     * is called.
     * 
     * @param archiveFileName the name of the zip archive file to open.
     * @throws IOException If the file cannot be read for some reason.
     */ 
    public ZipArchive(String archiveFileName) throws IOException {
        
        // Initialise our map of archive entries.
        this.archiveEntries = new HashMap();
        
        // Save away the file name of the archive.
        this.archiveFile = new File(archiveFileName);

        if (logger.isDebugEnabled()) {
            logger.debug("Loading archive file: " + archiveFile + ", " +
                    new Date(archiveFile.lastModified()));
        }

        // If the archive file exists,
        if (archiveFile.exists() && archiveFile.length()>0) {

            // Then try and parse it into our internal data structure.
            ZipFile zipFile = new ZipFile(archiveFile);
            try {
                Enumeration zipEntries = zipFile.entries();
                while (zipEntries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();
                    if (!zipEntry.isDirectory()) {
                        InputStream input = zipFile.getInputStream(zipEntry);

                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        byte[] buffer = new byte[2048];
                        int readBytes = input.read(buffer);
                        while (readBytes != -1) {
                            output.write(buffer, 0, readBytes);
                            readBytes = input.read(buffer);
                        }
                        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(
                                zipEntry.getName(), output.toByteArray(),
                                zipEntry.getTime());
                        output.close();
                        input.close();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Loading archive entry: " +
                                    new Date(archiveEntry.getTime()) + " " +
                                    archiveEntry.getName());
                        }
                        archiveEntries.put(archiveEntry.getName(),
                                archiveEntry);
                    }
                    // else, don't expect directories, ignore them anyway.
                }
            } finally {
                // Best be careful with cleanup for zip files since they are 
                // implemented with native code in existing VMs.
                zipFile.close();
            }
            
        }
        // else, it's up to the client to add some content to the archive
        // before they save it since a ZIP file must have at least one entry
        // to be valid, apparently.
        
    }

    /**
     * Constructs a new archive with the same entries as the specified archive.
     * 
     * @param archive the archive who's entries are to be placed in this 
     *      archive.
     */ 
    public ZipArchive(ZipArchive archive) {
        this.archiveEntries = new HashMap(archive.archiveEntries);
        this.archiveFile = archive.archiveFile;
    }

    /**
     * Tests to see if the file exists in the archive.
     * @param name
     * @return boolean flag
     */ 
    public boolean exists(String name) {
        return archiveEntries.containsKey(name);
    }
    
    /**
     * Retrieve an InputStream to read the content of a file in the 
     * zip archive.
     * 
     * @param name the name of the file in the zip archive.
     * @return an input stream from the created file, or null if the file
     *      does not exist in the zip archive.
     */ 
    public InputStream getInputFrom(String name) {
        
        InputStream input = null;
        ZipArchiveEntry entry = (ZipArchiveEntry) archiveEntries.get(name);
        if (entry != null) {
            input = new ByteArrayInputStream(entry.getContent());
        }
        return input;
        
    }

    /**
     * Retrieve an OutputStream to write the content of an file in 
     * the zip archive.
     * <p>
     * If the file does not exist, it will be created.
     * 
     * @param name the name of the file in the zip archive.
     * @return an output stream to the existing file.
     */ 
    public OutputStream getOutputTo(final String name) {
        return new ByteArrayOutputStream() {
            public synchronized void close() throws IOException {
                ZipArchiveEntry entry = (ZipArchiveEntry) archiveEntries.get(name);
                if (entry != null) {
                    archiveEntries.remove(name);
                }
                // Get the byte array which has been written to this stream
                // and use it to create the ZipArchiveEntry.
                ZipArchiveEntry newEntry =
                        new ZipArchiveEntry(name, toByteArray());
                archiveEntries.put(name, newEntry);
                super.close();
            }
        };
    }
    
    /**
     * Delete a file from the zip archive.
     * 
     * @param name the name of the file in the zip archive.
     * @return true if the file was deleted, or false it the file does not
     *      exist in the zip archive.
     */ 
    public boolean delete(String name) {
        
        return archiveEntries.remove(name) != null;
        
    }
    
    /**
     * Rename a file in the zip archive.
     * 
     * @param from the file to rename.
     * @param to the new name for the file.
     * @return true if the file was renamed, or false if not (i.e. the from 
     *      file did not exist or the to file already exists).
     */ 
    public boolean rename(String from, String to) {
        
        boolean success = false;
        ZipArchiveEntry toEntry = (ZipArchiveEntry) archiveEntries.get(to);
        if (toEntry == null) {
            ZipArchiveEntry fromEntry = (ZipArchiveEntry)
                    archiveEntries.get(from);
            if (fromEntry != null) {
                archiveEntries.remove(from);
                toEntry = new ZipArchiveEntry(to, fromEntry.getContent());
                archiveEntries.put(to, toEntry);
                success = true;
            }
            // else, can't rename, from doesn't exist.
        }
        // else, can't rename, to exists.
        return success;
    }

    /**
     * Set the name of the ZipArchive file.
     * @param filename the full path of the proposed ZipArchive file name.
     * @throws IllegalArgumentException if the proposed filename would
     * create a ZipArchive that is not writeable.
     */
    public void setArchiveFilename(String filename) {
        File proposedFile = new File(filename);
        if(proposedFile.isDirectory()) {
            throw new IllegalArgumentException("File " + filename +
                    " is a directory.");
        }

        if(proposedFile.exists()) {
            if(!proposedFile.canWrite()) {
                throw new IllegalArgumentException("Cannot write to file: " +
                        filename);
            }
        } else {
            File proposedDir = proposedFile.getParentFile();

            if(!proposedDir.canWrite()) {
                throw new IllegalArgumentException("Cannot write to directory" +
                        ": " + proposedDir.getAbsolutePath());
            }
        }

        archiveFile = proposedFile;
    }

    /**
     * Create a new version of the zip archive, with all the changes made via 
     * other methods since the last successful save operation. 
     * <p>
     * This will attempt to ensure that the operation is atomic by creating 
     * the new zip archive in a temporary file and then renaming it to the
     * proper name once it is complete.
     * <p>
     * If the zip file did not exist when the archive was created, this will
     * fail if at least one file was not added to the archive.
     *  
     * @throws IOException if there was a problem during the file operations.
     */ 
    public void save() throws IOException {

        //
        // First, serialise our state to a new, temporary zip file.
        //

        // Create a temporary file to write our new archive to.
        // We create it in the same directory as the archive file rather than
        // in the default temp directory because we need to rename it later 
        // and renames may fail across filesystems.
        File parentDirectory = archiveFile.getCanonicalFile().getParentFile();
        File tempFile = File.createTempFile("archive", null, parentDirectory);
        // Note: we could use a TemporaryFileManager to manage the new archive
        // file, but it's tricky to know when to delete it, if there was a
        // failure. We'd need some kind of context object to say we still 
        // need it. We would also have to solve the problem of how to route
        // exceptions through the TemporaryFileExecutor (Use a checked 
        // TemporaryFileException to tunnel the exception, then unwrap the
        // original exception, and re-throw?)
        
        // Create a zpecial zip output ztream to create a zip file.
        // This works in a rather nasty way such that "special" methods on the
        // stream get it to start and end the individual files within it.
        ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(tempFile));
        try {
            // Create the list of file names.
            List nameList = new ArrayList(archiveEntries.keySet());
            // Sort it in default alphabetic order.
            // This is not strictly required but it seems like a friendly 
            // thing to do since there may be hundreds of entries.
            Collections.sort(nameList);
        
            // Iterate over the zorted names, adding each file into the new 
            // zip. 
            Iterator names = nameList.iterator();
            while (names.hasNext()) {
                String name = (String) names.next();
                ZipArchiveEntry entry = (ZipArchiveEntry) 
                        archiveEntries.get(name);
                if (entry != null) {
                    // Ztart the new zip entry
                    ZipEntry zipEntry = new ZipEntry(entry.getName());
                    zipEntry.setTime(entry.getTime());
                    zos.putNextEntry(zipEntry);
                    zos.write(entry.getContent());
                    // End the entry
                    zos.closeEntry();
                } 
                // else, null key, we will ignore it.
            }
        } finally {
            // Close the zip output ztream.
            // Best be careful with cleanup for zip files since they are 
            // implemented with native code in existing VMs.
            zos.flush();
            zos.close();
        }
        
        //
        // OK, now we have created our new zip file. Now we want to zwap out
        // the old one and replace it with the new one. Here we have to be
        // careful not to stuff things up if something goes wrong.
        // Note: perhaps we could use a FileReplacementManager here?
        //
        
        // If we have an existing archive file,
        if (archiveFile.exists()) {
            
            // Then we need to back it up before we rename our new archive 
            // to that name.
            File backupArchiveFile = new File(archiveFile + ".bak");
            if (backupArchiveFile.exists()) {
                if (!backupArchiveFile.delete()) {
                    throw new IOException("Unable to delete old backup file " +
                            backupArchiveFile);
                }
            }
            // Necessary to force file handle releasing on Windows. Yep, it's
            // horrible, but see http://forum.java.sun.com/thread.jsp?forum=1&thread=166271&start=15&range=15&tstart=45&trange=15
            // for details (penultimate post at time of fix).
            System.gc();
            
            if (!archiveFile.renameTo(backupArchiveFile)) {
                throw new IOException("Unable to rename archive file " +
                        archiveFile + " to backup file " + backupArchiveFile);
            }
            if (!tempFile.renameTo(archiveFile)) {
                // This is serious. 
                // We have renamed the old archive file to backup, but we 
                // can't rename the temp file to be the new archive file.
                // All we can really do is try and rename the old one back.
            
                if (backupArchiveFile.renameTo(archiveFile)) {
                    throw new IOException("Unable to rename temporary file " +
                            tempFile + " to new archive file " + archiveFile +
                            ", restored old archive file.");
                } else {
                    throw new IOException("Unable to rename temporary file " +
                            tempFile + " to archive file " + archiveFile +
                            ", and unable to restore old archive file from " +
                            "backup file " + backupArchiveFile + ". " +
                            "Please record this message and contact technical " +
                            "support.");
                }
            } else {
                // Rename was OK, so we can delete the backup file.
                backupArchiveFile.delete();
            }
            
        } else {
            
            // No existing archive file, so we are creating a new archive for 
            // the first time. In this case all we need to do is rename the 
            // temp file to the archive file.
            if (!tempFile.renameTo(archiveFile)) {
                throw new IOException("Unable to rename temporary file " +
                        tempFile + " to new archive file " + archiveFile);
            }
            
        }
    }

    /**
     * An entry in the files map. Vaguely corresponds to a ZipEntry, but it
     * is immutable and contains the content of the file as well.
     */ 
    static class ZipArchiveEntry {

        /**
         * The name of the archive entry
         */
        private String name;

        /**
         * The creation time of the archive entry
         */
        private long time;

        /**
         * The content of the archive entry.
         */
        private byte[] content;

        /**
         * Construct an entry for a new file.
         * 
         * @param name the name of the file.
         * @param content the content of the file.
         */ 
        ZipArchiveEntry(String name, byte[] content) {
            this.name = name;
            this.content = content;
            this.time = System.currentTimeMillis();
        }

        /**
         * Construct an entry for an existing file.
         * 
         * @param name the name of the file.
         * @param content the content of the file.
         * @param time the creation time of the file.
         */ 
        ZipArchiveEntry(String name, byte[] content, long time) {
            this.name = name;
            this.content = content;
            this.time = time;
        }

        /**
         * Get the name of this archive entry.
         * @return the name of this archive entry.
         */
        String getName() {
            return name;
        }

        /**
         * Get the creation time of this archive entry.
         * @return the creation time of this archive entry.
         */
        long getTime() {
            return time;
        }

        /**
         * Get the content of this archive entry
         * @return Get the content of this archive entry
         */
        byte[] getContent() {
            byte[] result = new byte[content.length];
            for (int i = 0; i < content.length; i++) {
                result[i] = content[i];
            }
            return result;
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

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 11-Nov-04	6156/1	geoff	VBM:2004110904 ICS GIF support shopuld be on by default in MCS v3.2.3

 08-Nov-04	6131/1	byron	VBM:2004110204 Eclipse plugins throw exception when saving device repository

 05-Nov-04	6122/1	byron	VBM:2004110204 Eclipse plugins throw exception when saving device repository

 21-Sep-04	5567/1	allan	VBM:2004092010 Handle multi-valued device policy selection.

 10-May-04	4239/1	allan	VBM:2004042207 SaveAs on DeviceEditor.

 21-Apr-04	3016/2	adrian	VBM:2004021301 Fixed merge problems with updated XMLDeviceRepositoryAccessor

 11-Mar-04	3018/3	adrian	VBM:2004021302 Rework issues reimplemented

 03-Mar-04	3018/1	adrian	VBM:2004021302 Updated XMLDeviceRepositoryAccessor with new write methods

 16-Apr-04	3740/7	allan	VBM:2004040508 Merge issue.

 16-Apr-04	3740/3	allan	VBM:2004040508 UpdateClient/Server enhancements & fixes.

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 01-Apr-04	3574/1	allan	VBM:2004032401 Implement merging of device hierarchies.

 31-Oct-03	1729/4	geoff	VBM:2003102302 Handle device repository versions (supermerge)

 30-Oct-03	1729/1	geoff	VBM:2003102302 Handle device repository versions

 30-Oct-03	1716/1	geoff	VBM:2003102915 Enhance DeviceRepositoryBuilder to use new Device methods on new accessor.

 30-Oct-03	1716/1	geoff	VBM:2003102915 Enhance DeviceRepositoryBuilder to use new Device methods on new accessor.

 29-Oct-03	1599/6	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor (rework issues from Allan)

 29-Oct-03	1599/2	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 ===========================================================================
*/
