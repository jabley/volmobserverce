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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.impl.directory.monitor;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.FileFilter;
import java.util.Properties;

public abstract class TimestampPersistenceTestCaseAbstract extends TestCaseAbstract {

    String directory;
    String configDirectory;

    // Javadoc inherited.
    public void setUp() throws IOException {
        directory = createTempDirectory("tspersistence");
        configDirectory = createTempDirectory("config");
    }

    // Javadoc inherited.
    public void tearDown() {
        deleteTempDirectory(directory);
        deleteTempDirectory(configDirectory);
    }

    // Javadoc unnecessary.
    private String createTempDirectory(String name) throws IOException {
        // create tmp file
        File tmp = File.createTempFile(name, null);
        // delete it
        tmp.delete();
        //create it as a directory
        tmp.mkdirs();
        return tmp.getCanonicalPath();
    }

    // Javadoc unnecessary.
    private void deleteTempDirectory(String name) {
        File dir = new File(directory);
        IOUtils.deleteDirectoryContents(dir);
        dir.delete();
    }

    /**
     * Override to return the instance to test.
     *
     * @param directoryName     name of the directory in which timestamp data
     *                          is persisted (if on disk representation).
     * @param recursive         true if directories should be included in the
     *                          timestamp monitoring, false otherwise.
     * @return TimestampPersistence
     */
    public abstract TimestampPersistence getTimestampPersistence(
            String directoryName, boolean recursive);

    /**
     * This test ensures that the data is modifiable and storeable (but not
     * permenantly persisted
     * @throws Exception if there was a problem running the test
     */
    public void testPersistenceReturnsCorrectValue() throws Exception {

        TimestampPersistence tsp = getTimestampPersistence(directory, false);

        Properties props = tsp.getTimestamps();
        assertTrue("should be empty", props.isEmpty());
        props.setProperty("testfile", "1231234124");

        tsp.storeTimestamps(props);

        props = tsp.getTimestamps();
        assertTrue(props.containsKey("testfile"));
        assertEquals("1231234124", props.getProperty("testfile"));
    }

    /**
     * Verify that the file filter returned by the timestamp persistence
     * does filters out directories if recursive.
     */
    public void testPersistenceWhenRecursive() {
        TimestampPersistence tsp = getTimestampPersistence(directory, true);
        FileFilter filter = tsp.getFileFilter();
        assertTrue(filter.accept(new File(configDirectory, "testfile")));
        assertTrue(filter.accept(new File(configDirectory)));

        File nestedDir = new File(configDirectory, "nestedDir");
        nestedDir.delete();
        nestedDir.mkdirs();
        assertTrue(nestedDir.isDirectory());
        assertTrue(filter.accept(nestedDir));
        File nestedFile = new File(nestedDir, "nestedFile");
        assertTrue(filter.accept(nestedFile));
    }
    
    /**
     * Verify that the file filter returned by the timestamp persistence
     * filters out directories if not recursive.
     */
    public void testPersistenceWhenNotRecursive() {
        TimestampPersistence tsp = getTimestampPersistence(directory, false);
        FileFilter filter = tsp.getFileFilter();
        assertTrue(filter.accept(new File(configDirectory, "testfile")));
        assertFalse(filter.accept(new File(configDirectory)));
        File nestedDir = new File(configDirectory, "nestedDir");
        nestedDir.delete();
        nestedDir.mkdirs();
        assertTrue(nestedDir.isDirectory());
        assertFalse(filter.accept(nestedDir));
        File nestedFile = new File(nestedDir, "nestedFile");
        assertTrue(filter.accept(nestedFile));
    }
}
