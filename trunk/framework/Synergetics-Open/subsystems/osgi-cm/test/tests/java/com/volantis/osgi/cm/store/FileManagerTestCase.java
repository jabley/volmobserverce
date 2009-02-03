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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.util.concurrent.CountDownLatch;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class FileManagerTestCase
        extends TestCaseAbstract {

    private File rootDir;

    protected void setUp() throws Exception {
        super.setUp();

        this.rootDir = createTempDir("filemanager");
    }

    /**
     * A stress test for the file manager.
     */
    public void testStress() throws Exception {

        final Set filesState = new HashSet();
        final FileManager fileManager = new FileManagerImpl(rootDir, 5, 5);

        Thread[] threads = new Thread[40];
        final CountDownLatch startSignal = new CountDownLatch(threads.length);
        final CountDownLatch endSignal = new CountDownLatch(threads.length);

        for (int i = 0; i < threads.length; i++) {
            Thread thread = new Thread(new FileManipulator(startSignal,
                    endSignal, fileManager, filesState));

            threads[i] = thread;
            thread.setDaemon(true);
            thread.start();
        }

        endSignal.await();
        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            thread.join();
        }

        Set found = new HashSet();
        findFiles(rootDir, found);

        Set extraFiles = new HashSet(found);
        extraFiles.removeAll(filesState);
        if (!extraFiles.isEmpty()) {
            System.out.println("The following extra files were found");
            for (Iterator i = extraFiles.iterator(); i.hasNext();) {
                File file = (File) i.next();
                System.out.println("  " + file);
            }
        }

        Set missingFiles = new HashSet(filesState);
        missingFiles.removeAll(found);
        if (!missingFiles.isEmpty()) {
            System.out.println("The following files were missing");
            for (Iterator i = missingFiles.iterator(); i.hasNext();) {
                File file = (File) i.next();
                System.out.println("  " + file);
            }
        }

        assertEquals(filesState, found);
    }

    private void findFiles(File dir, Set found) {
        findFiles2(dir, found, "");
    }

    private void findFiles2(File dir, Set found, String indent) {
        File[] files = dir.listFiles();
        Arrays.sort(files);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                System.out.println(indent + file.getName() + "/");
                findFiles2(file, found, indent + "    ");
            } else {
                found.add(file);
                System.out.println(indent + file.getName());
            }
        }
    }

    private static class FileManipulator
            implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch endSignal;
        private final FileManager fileManager;
        private final Set filesState;
        private final Random random;

        public FileManipulator(
                CountDownLatch startSignal, CountDownLatch endSignal,
                FileManager fileManager, Set filesState) {
            this.startSignal = startSignal;
            this.endSignal = endSignal;
            this.fileManager = fileManager;
            this.filesState = filesState;
            random = new Random();
        }

        public void run() {
            try {
                startSignal.countDown();
                startSignal.await();

                for (int l = 0; l < 200; l += 1) {
                    int action = random.nextInt(10);
                    if (action < 3) {
                        // Delete a file at random.
                        File fileToDelete = selectFileToDelete();
                        if (fileToDelete != null) {
                            System.out.println(
                                    "Deleted file: " + fileToDelete);
                            fileToDelete.delete();
                        }

                    } else if (action > 6) {
                        File file = fileManager.allocateFile();
                        synchronized (filesState) {
                            filesState.add(file);
                        }
                        System.out.println("Added file: " + file);
                    } else {
                        File fileToDelete = selectFileToDelete();
                        if (fileToDelete != null) {
                            System.out.println(
                                    "Released file: " + fileToDelete);
                            fileManager.releaseFile(fileToDelete);
                        }
                    }
                }

            } catch (Throwable t) {
                throw new UndeclaredThrowableException(t);
            } finally {
                endSignal.countDown();
            }
        }

        private File selectFileToDelete() {
            File fileToDelete = null;
            synchronized (filesState) {
                int count = filesState.size();
                if (count > 0) {
                    int index = random.nextInt(count);
                    for (Iterator i =
                            filesState.iterator();
                         i.hasNext();) {
                        File file = (File) i.next();
                        if (index == 0) {
                            fileToDelete = file;
                            i.remove();
                            break;
                        }

                        index -= 1;
                    }
                }
            }
            return fileToDelete;
        }
    }

    public void testStructure() throws Exception {

        long start = System.currentTimeMillis();
        final FileManager fileManager = new FileManagerImpl(rootDir, 2, 2);
        for (int i = 0; i < 32; i += 1) {
            fileManager.allocateFile();
        }
        long end = System.currentTimeMillis();

        Set found = findFiles();
        System.out.println("Took " + (end - start) / 1000 + "s");
        System.out.println("Found " + found.size());

        Set expected = new HashSet();
        String[] expectedPaths = new String[]{
                "a",
                "b",
                "0/a", "0/b",
                "1/a", "1/b",
                "0/0/a", "0/0/b",
                "0/1/a", "0/1/b",
                "1/0/a", "1/0/b",
                "1/1/a", "1/1/b",
                "0/0/0/a", "0/0/0/b", "0/0/1/a", "0/0/1/b",
                "0/1/0/a", "0/1/0/b", "0/1/1/a", "0/1/1/b",
                "1/0/0/a", "1/0/0/b", "1/0/1/a", "1/0/1/b",
                "1/1/0/a", "1/1/0/b", "1/1/1/a", "1/1/1/b",
                "0/0/0/0/a", "0/0/0/0/b"
        };
        for (int i = 0; i < expectedPaths.length; i++) {
            String expectedPath = expectedPaths[i];
            expected.add(new File(rootDir, expectedPath));
        }
        assertEquals(expected, found);

        for (Iterator i = found.iterator(); i.hasNext();) {
            File file = (File) i.next();
            fileManager.releaseFile(file);
        }

        found = findFiles();
        assertEquals(new TreeSet(), found);
    }

    private Set findFiles() {
        Set files = new TreeSet();
        findFiles(rootDir, files);
        return files;
    }

    public void testGetRelativePath() throws IOException {

        FileManager manager = new FileManagerImpl(rootDir, 2, 2);
        checkRelativePath(manager, "a");
        checkRelativePath(manager, "b");
        checkRelativePath(manager, "0/a");
        checkRelativePath(manager, "0/b");
    }

    private void checkRelativePath(FileManager manager, String expectedPath)
            throws IOException {
        File file = manager.allocateFile();
        String relative = manager.getRelativePath(file);
        assertEquals(expectedPath, relative);
    }
}
