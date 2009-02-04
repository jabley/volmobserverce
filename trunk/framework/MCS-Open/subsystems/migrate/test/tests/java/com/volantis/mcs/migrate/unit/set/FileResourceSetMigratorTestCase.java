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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.unit.set;

import com.volantis.mcs.migrate.api.framework.OutputCreator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.impl.set.file.FileResourceSetMigrator;
import com.volantis.mcs.migrate.impl.notification.reporter.SimpleCLINotificationReporter;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests the file-based resource set migrator implementation, using test
 * implementations of the rest of the framework.
 */
public class FileResourceSetMigratorTestCase extends TestCaseAbstract {
    /**
     * Test migration of a simple directory structure. Ensures that each of
     * the files within the directory structure is passed to the
     * ResourceMigrator once and once only.
     * <p>Note that the FileResourceSetMigrator does not impose any ordering
     * on the files it processes, in accordance with the methods provided by
     * the {@link File} class.</p>
     *
     * @throws Exception if an error occurs
     */
    public void testMigrateDirectory() throws Exception {
        String resourcePath = "/fileResourceSetMigratorDir";
        String[] paths = {
            "fileResourceSetMigratorDir/file1.txt",
            "fileResourceSetMigratorDir/file2.txt",
            "fileResourceSetMigratorDir/subdir1/file3.txt",
            "fileResourceSetMigratorDir/subdir1/subdir2/file4.txt"
        };
        doTestFileResource(resourcePath, paths);
    }

    /**
     * Test migration of a single file using the FileResourceSetMigrator.
     * Although the expectation is that it will normally be used to migrate30
     * entire directories, it should also be capable of upgrading a single
     * resource if required.
     *
     * @throws Exception if an error occurs
     */
    public void testMigrateFile() throws Exception {
        String resourcePath = "/fileResourceSetMigratorDir/file1.txt";
        String[] paths = {
            "file1.txt"
        };
        doTestFileResource(resourcePath, paths);
    }

    /**
     * Helper method to test use of a FileResourceSetMigrator, given an input
     * location and a specified expected output.
     *
     * @param resourcePath The resource path for the item (file or directory)
     *                     to be migrated
     * @param paths An array of expected input paths to be migrated
     * @throws Exception if an error occurs
     */
    private void doTestFileResource(String resourcePath, String[] paths) throws Exception {
        File file = getResourceAsFile(resourcePath);
        File outputDir = File.createTempFile("testFRSM", "");
        outputDir.delete();
        outputDir.deleteOnExit();
        FileResourceSetMigrator frsm = new FileResourceSetMigrator(file, outputDir,
                                                                   new SimpleCLINotificationReporter());
        PathIdentifyingResourceMigrator migrator =
                new PathIdentifyingResourceMigrator(paths);
        frsm.migrate(migrator);
        if (migrator.failed()) {
            fail(migrator.getFailures().toString());
        }
    }

    /**
     * Get a File corresponding to the specified resource name.
     *
     * @param resourcePath The resource path to locate as a File
     * @return The File corresponding to the specified resource
     * @throws Exception if an error occurs
     */
    private File getResourceAsFile(String resourcePath) throws Exception {
        URL url = getClass().getResource(resourcePath);
        URI uri = new URI(url.toExternalForm());
        return new File(uri);
    }

    /**
     * Fake implementation of the ResourceMigrator interface to validate the
     * details passed into the migration process.
     */
    private class PathIdentifyingResourceMigrator implements ResourceMigrator {
        /**
         * The paths expected by the migration process.
         */
        private List expectedPaths;

        /**
         * An array to keep track of which paths have been located so far.
         */
        private boolean[] pathsLocated;

        /**
         * A list of failure messages.
         */
        private List failures;

        /**
         * Construct a fake resource migrator expecting the specified paths.
         *
         * @param paths The paths to expect
         */
        public PathIdentifyingResourceMigrator(String[] paths) {
            expectedPaths = Arrays.asList(paths);
            pathsLocated = new boolean[paths.length];
            failures = new ArrayList();
        }

        /**
         * Checks to see whether failures have taken place - if any paths have
         * not been located, this is specified as an error. Should only be
         * called once migration is complete.
         *
         * @return True if any errors have occurred.
         */
        public boolean failed() {
            boolean missingPaths = false;
            StringBuffer missing = new StringBuffer();
            for (int i = 0; i < pathsLocated.length; i++) {
                missingPaths = missingPaths && !pathsLocated[i];
                if (!pathsLocated[i]) {
                    if (missing.length() > 0) {
                        missing.append(", ");
                    }
                    missing.append(expectedPaths.get(i));
                }
            }
            if (missingPaths) {
                failures.add("Some paths were missing: " + missing);
            }
            return !failures.isEmpty();
        }

        // Javadoc unnecessary
        public List getFailures() {
            return failures;
        }

        /**
         * Carries out the 'migration' process in which the retrieved input
         * paths are validated.
         *
         * @param meta The input path
         * @param input The input resource as a stream
         * @param outputCreator An output creator
         * @throws IOException if an I/O error occurs
         */
        public void migrate(InputMetadata meta, InputStream input,
                            OutputCreator outputCreator) throws IOException {
            int index = expectedPaths.indexOf(meta.getURI());
            if (index < 0) {
                failures.add("Unexpected resource path: " + meta.getURI());
            } else {
                boolean alreadyLocated = pathsLocated[index];
                if (alreadyLocated) {
                    failures.add("Duplicate resource path: " + meta.getURI());
                } else {
                    pathsLocated[index] = true;
                }
            }

            if (input == null) {
                failures.add("Input stream was not available for resource " +
                             meta.getURI());
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8181/3	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/
