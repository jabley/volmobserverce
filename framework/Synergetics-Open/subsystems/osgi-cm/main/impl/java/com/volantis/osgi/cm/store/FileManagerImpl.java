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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.cm.store;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link FileManager}.
 */
public class FileManagerImpl
        implements FileManager {

    /**
     * The maximum supported depth of the directory hierarchy.
     */
    private static final int MAX_DEPTH = 100;

    /**
     * The maximum number of files per directory.
     */
    private final int maxFilesPerDir;

    /**
     * The maximum number of sub directories per directory.
     */
    private final int maxDirsPerDir;

    /**
     * The mask that indicates that all allowed files are present.
     */
    private final int allFilesMask;

    /**
     * The mask that indicates that all allowed sub directories are present.
     */
    private final int allDirsMask;

    /**
     * The root directory of the files that are being managed.
     */
    private final File rootDir;

    /**
     * The set of file names.
     */
    private final String[] fileNames;

    /**
     * The set of directory names.
     */
    private final String[] dirNames;

    /**
     * A holder for a boolean that indicates whether resynchronization is
     * required.
     *
     * <p>An atomic value is used to save synchronizing when changing its state,
     * although it is only queried while synchronized in order to ensure that
     * changes to other fields are protected.</p>
     *
     * <p>A new instance of this is created each time that a scan completes so
     * that once it has been set to true it's state is never changed. This helps
     * to eliminate a number of possible race conditions.</p>
     */
    private AtomicBoolean resyncRequired;

    /**
     * The cached representation of the state of the root directory.
     */
    private Directory rootDirectory;

    /**
     * Initialise.
     *
     * @param rootDir        The root directory within which the
     * @param maxFilesPerDir
     * @param maxDirsPerDir
     */
    public FileManagerImpl(
            File rootDir, int maxFilesPerDir, int maxDirsPerDir) {
        if (!rootDir.exists()) {
            throw new IllegalArgumentException(
                    "Root directory '" + rootDir + "' does not exist");
        }

        if (maxFilesPerDir < 2 || maxFilesPerDir > 26) {
            throw new IllegalArgumentException(
                    "maxFilesPerDir must be in the range [2..26]");
        }

        if (maxDirsPerDir < 2 || maxDirsPerDir > 10) {
            throw new IllegalArgumentException(
                    "maxDirsPerDir must be in the range [2..10]");
        }

        this.rootDir = rootDir;
        this.maxFilesPerDir = maxFilesPerDir;
        this.allFilesMask = (1 << maxFilesPerDir) - 1;
        this.maxDirsPerDir = maxDirsPerDir;
        this.allDirsMask = (1 << maxDirsPerDir) - 1;

        fileNames = new String[maxFilesPerDir];
        for (int i = 0; i < fileNames.length; i++) {
            fileNames[i] = createFileName(i);
        }

        dirNames = new String[maxDirsPerDir];
        for (int i = 0; i < dirNames.length; i++) {
            dirNames[i] = getDirectoryName(i);
        }

        // Scan the directories.
        scanDirectories();
    }

    /**
     * Update our in memory representation of the directories.
     */
    private void scanDirectories() {
        synchronized (this) {
            resyncRequired = new AtomicBoolean(false);
            rootDirectory = scanDirectories(rootDir, resyncRequired);
        }
    }

    /**
     * Scan the specified directory and create a {@link Directory}
     * representation.
     *
     * @param dir            The directory to scan.
     * @param resyncRequired The flag that determines whether resynchronization
     *                       is required.
     * @return The {@link Directory} for the directory.
     */
    private Directory scanDirectories(File dir, AtomicBoolean resyncRequired) {

        File[] files = dir.listFiles();

        // Iterate over the files seeing whether they are directories, or just
        // plain files.
        Directory[] directories = new Directory[maxDirsPerDir];
        int fileSet = 0;
        int dirSet = 0;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                int index = getDirectoryIndex(file);
                if (index < 0) {
                    continue;
                }

                dirSet |= (1 << index);

                // Recurse down through the hierarchy.
                directories[index] = scanDirectories(file, resyncRequired);

            } else {
                int index = getFileIndex(file);
                if (index < 0) {
                    continue;
                }

                fileSet |= (1 << index);
            }
        }

        return new Directory(resyncRequired, dir, directories, dirSet, fileSet);
    }

    // Javadoc inherited.
    public File allocateFile() throws IOException {

        Directory root;
        synchronized (this) {
            if (resyncRequired.get()) {
                scanDirectories();
            }

            root = rootDirectory;
        }

        // Use a queue to ensure depth first searching across the directories.
        // Without this it would lead to a unbalanced structure.
        Queue queue = new LinkedList();
        Directory directory;

        // Try all of the directories in a breadth first order, hence the
        // reason for the queue to see if any of them can allocate a file.
        queue.add(root);
        while ((directory = (Directory) queue.poll()) != null) {
            File newFile = directory.allocateFile(queue);
            if (newFile != null) {
                return newFile;
            }
        }

        // None of them could so now try all of the directories to see if any
        // of them can allocate a directory.
        queue.add(root);
        while ((directory = (Directory) queue.poll()) != null) {

            Directory newDirectory = directory.allocateDirectory(queue);
            if (newDirectory != null) {
                File newFile = newDirectory.allocateFile(null);
                if (newFile != null) {
                    return newFile;
                }
            }
        }

        throw new IOException("Could not allocate file");
    }

    // Javadoc inherited.
    public boolean releaseFile(File file) throws IOException {
        if (!file.exists()) {
            // File does not exist anyway.
            return true;
        } else if (file.isDirectory()) {
            // Directories cannot be released.
            return true;
        }

        Directory root;
        synchronized (this) {
            if (resyncRequired.get()) {
                // Resynchronization is required so might as well just delete
                // the file anyway as the change will be picked up next time.
                file.delete();
                return true;
            }

            root = rootDirectory;
        }

        // Separate the file into separate components. Add the components at
        // the start of the list so that when iterating they appear from top
        // down.
        int[] directoryIndeces = new int[MAX_DEPTH];

        int fileIndex = getFileIndex(file);
        if (fileIndex == -1) {
            // Not know file name.
            throw new IOException(
                    "File '" + file + "' is not of correct structure");
        }
        File dir = file.getParentFile();
        int index = directoryIndeces.length;
        while (dir != null && !dir.equals(rootDir)) {
            int dirIndex = getDirectoryIndex(dir);
            if (dirIndex == -1) {
                // Not known directory.
                throw new IOException(
                        "Directory '" + dir + "' is not of correct structure");
            }

            index -= 1;
            directoryIndeces[index] = dirIndex;

            dir = dir.getParentFile();
        }

        if (dir == null) {
            // Not part of the files being managed.
            throw new IllegalArgumentException("File '" + file +
                    "' is not within the directory '" + rootDir + "'");
        }

        root.releaseFile(file, index,
                directoryIndeces, fileIndex);

        return true;
    }

    /**
     * Create the file name for the index.
     *
     * @param index The index.
     * @return The file name.
     */
    private String createFileName(int index) {
        return new String(new char[]{(char) ('a' + index)});
    }

    /**
     * Get the index for the file name.
     *
     * @param file The file name.
     * @return The index, or -1 if the file name does not match one of the
     *         allowed names.
     */
    private int getFileIndex(File file) {
        String name = file.getName();
        if (name.length() > 1) {
            // Ignore unknown directory.
            return -1;
        }

        char c = name.charAt(0);
        if (c < 'a' || c >= ('a' + maxDirsPerDir)) {
            // Ignore unknown directory.
            return -1;
        }
        return c - 'a';
    }

    /**
     * Create the directory name for the index.
     *
     * @param index The index.
     * @return The file name.
     */
    private String getDirectoryName(int index) {
        return new String(new char[]{(char) ('0' + index)});
    }

    /**
     * Get the index for the directory name.
     *
     * @param dir The directory name.
     * @return The index, or -1 if the directory name does not match one of the
     *         allowed names.
     */
    private int getDirectoryIndex(File dir) {
        String name = dir.getName();
        return getDirectoryIndex(name);
    }

    /**
     * Get the index for the directory name.
     *
     * @param name The directory name.
     * @return The index, or -1 if the directory name does not match one of the
     *         allowed names.
     */
    private int getDirectoryIndex(String name) {
        if (name.length() > 1) {
            // Ignore unknown file.
            return -1;
        }

        char c = name.charAt(0);
        if (c < '0' || c >= ('0' + maxFilesPerDir)) {
            // Ignore unknown file.
            return -1;
        }

        return c - '0';
    }

    // Javadoc inherited.
    public String getRelativePath(File file) {
        String rootAsString = rootDir.toString() + "/";
        String fileAsString = file.toString();
        if (fileAsString.startsWith(rootAsString)) {
            return fileAsString.substring(rootAsString.length());
        } else {
            return null;
        }
    }

    // Javadoc inherited.
    public List listFiles() {
        List list = new ArrayList();
        addFiles(rootDir, list);
        return list;
    }

    /**
     * Recursively add the files in the specified directory and all its sub
     * directories into the list in depth first order.
     *
     * @param dir  The directory whose files are to be added.
     * @param list The list into which the files are added.
     */
    private void addFiles(File dir, List list) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                if (getDirectoryIndex(file) >= 0) {
                    addFiles(file, list);
                }
            } else if (getFileIndex(file) >= 0) {
                list.add(file);
            }
        }
    }

    /**
     * Representation of a directory that is being managed by this class.
     */
    private class Directory {

        /**
         * The flag that when set will cause the next request to allocate a file
         * to resync the in memory representation.
         */
        private final AtomicBoolean resyncRequired;

        /**
         * The actual directory this is representing.
         */
        private final File dir;

        /**
         * The set of sub directories.
         */
        private final Directory[] directories;

        /**
         * The bit set that indicates which of the sub directories are present.
         */
        private int dirSet;

        /**
         * The bit set that indicates which of the files are present.
         */
        private int fileSet;

        /**
         * Initialise.
         *
         * @param resyncRequired The flag that when set will cause the next
         *                       request to allocate a file to resync the in
         *                       memory representation.
         * @param dir            The actual directory this is representing.
         * @param directories    The set of sub directories.
         * @param dirSet         The bit set that indicates which of the sub
         *                       directories are present.
         * @param fileSet        The bit set that indicates which of the files
         *                       are present.
         */
        public Directory(
                AtomicBoolean resyncRequired, File dir, Directory[] directories,
                int dirSet, int fileSet) {
            this.resyncRequired = resyncRequired;

            this.dir = dir;
            this.directories = directories;
            this.dirSet = dirSet;
            this.fileSet = fileSet;
        }

        /**
         * Allocate a file.
         *
         * <p>If a file could not be allocated then add any of its sub
         * directories to the queue so that they can be asked if necessary.</p>
         *
         * @param queue The queue to which sub directories should be added to
         *              continue the breadth first search.
         * @return The file that was allocated, or null it the directory had no
         *         space.
         * @throws IOException If there was a problem allocating the file.
         */
        public synchronized File allocateFile(Queue queue)
                throws IOException {

            if (fileSet != allFilesMask) {
                // There is space for a new file in this directory.
                for (int i = 0; i < maxFilesPerDir; i += 1) {
                    int bit = (1 << i);
                    if ((fileSet & bit) == 0) {
                        File newFile = new File(dir, fileNames[i]);
                        if (newFile.createNewFile()) {
                            fileSet |= bit;
                            return newFile;
                        } else if (newFile.exists()) {
                            // Should have space to create it but the file already
                            // exists so we need to resync.
                            resyncRequired();
                        }
                    }
                }
            }

            // Could not allocate a file so add the sub directories.
            addSubDirectories(queue);
            return null;
        }

        /**
         * Add the sub directories to the queue.
         *
         * @param queue The queue that is used to support the breadth first
         *              search.
         */
        private void addSubDirectories(Queue queue) {
            for (int i = 0; i < directories.length; i++) {
                Directory directory = directories[i];
                if (directory != null) {
                    queue.add(directory);
                }
            }
        }

        /**
         * Allocate a directory.
         *
         * <p>If a directory could not be allocated then add any of its sub
         * directories to the queue so that they can be asked if necessary.</p>
         *
         * @param queue The queue to which sub directories should be added to
         *              continue the breadth first search.
         * @return The directory that was allocated, or null it the directory
         *         had no space.
         * @throws IOException If there was a problem allocating the directory.
         */
        public synchronized Directory allocateDirectory(Queue queue) {

            if (dirSet != allDirsMask) {
                // There is space for a new dir in this directory.
                for (int i = 0; i < maxDirsPerDir; i += 1) {
                    int bit = (1 << i);
                    if ((dirSet & bit) == 0) {
                        File newDir = new File(dir, dirNames[i]);
                        if (newDir.mkdir()) {
                            dirSet |= bit;
                            Directory directory = new Directory(
                                    resyncRequired, newDir,
                                    new Directory[maxDirsPerDir], 0, 0);
                            directories[i] = directory;
                            return directory;
                        } else if (newDir.exists()) {
                            // Should have space to create it but the dir already
                            // exists so we need to resync.
                            resyncRequired();
                        }
                    }
                }
            }

            // Could not allocate a directory so add the sub directories.
            addSubDirectories(queue);
            return null;
        }

        /**
         * Called to indicate that an inconsistency between the memory
         * representation has been detected and the memory representation should
         * be discarded and recreated.
         */
        private void resyncRequired() {
            if (resyncRequired.compareAndSet(false, true)) {
                // Resync required.
            }
        }

        /**
         * Release the file.
         *
         * @param file       The file to release.
         * @param index      The index within the <code>dirIndeces</code>
         *                   array.
         * @param dirIndeces The array of directory indeces from root directory
         *                   down to the directory containing the file.
         * @param fileIndex  The index of the file.
         * @throws IOException If there was a problem releasing the file.
         */
        public void releaseFile(
                File file, int index, int[] dirIndeces, int fileIndex)
                throws IOException {

            if (index < dirIndeces.length) {
                int dirIndex = dirIndeces[index];
                Directory directory;
                synchronized (this) {
                    directory = directories[dirIndex];
                }

                if (directory == null) {
                    throw new IOException(
                            "Could not find directory " +
                                    dirNames[dirIndex] + " within " +
                                    dir + " when releasing '" + file + "'");
                } else {
                    directory.releaseFile(
                            file, index + 1, dirIndeces, fileIndex);
                }
            } else {
                synchronized (this) {
                    int bit = 1 << fileIndex;
                    if ((fileSet & bit) != 0) {
                        if (!file.delete()) {
                            resyncRequired();
                        } else {
                            fileSet &= ~bit;
                        }
                    }
                }
            }
        }
    }
}
