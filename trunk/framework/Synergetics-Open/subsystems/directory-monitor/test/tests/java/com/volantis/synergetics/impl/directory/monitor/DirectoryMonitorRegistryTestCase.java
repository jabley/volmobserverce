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

import com.volantis.synergetics.directory.monitor.DirectoryMonitorCallback;
import com.volantis.synergetics.directory.monitor.RegistrationException;
import com.volantis.synergetics.directory.monitor.DirectoryMonitorCallbackImpl;
import com.volantis.synergetics.io.IOUtils;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.util.List;
import java.util.Iterator;

public class DirectoryMonitorRegistryTestCase extends TestCaseAbstract {

    String directory;

    public void setUp() throws Exception {
        super.setUp();
        // create a tmp file
        File tmp = File.createTempFile("tspersistence", null);
        // delete it
        tmp.delete();
        //create it as a directory
        tmp.mkdirs();
        directory = tmp.getCanonicalPath();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        // remove the directory here
        File dir = new File(directory);
        IOUtils.deleteDirectoryContents(dir);
        dir.delete();
    }

    public void testAddFile() throws Exception {
        File dir = new File(directory);
        String testFile = "bob.xml";
        
        DirectoryMonitorCallback dmc = new TestDirectoryMonitorCallback();

        InMemoryTimestampPersistenceFactory factory =
                new InMemoryTimestampPersistenceFactory();

        // create it without its timertask being registered with the internal
        // timer as this allows us to predictably test it
        DirectoryMonitorRegistry dmr = new DirectoryMonitorRegistry(false);
        dmr.register(7L, directory, dmc, factory, false);
        TimerTask scanner = dmr.getScanner();
        TimestampPersistence tsp = getPersistence(dmr, directory);

        // create a file
        File addFile = new File(dir,testFile);
        addFile.createNewFile();
        long createdTS = addFile.lastModified();
        // should run the timertask once
        scanner.run();
        assertFalse(tsp.getTimestamps().isEmpty());
        assertTrue("expect the added file in the Timestamp persistence",
                   tsp.getTimestamps().containsKey(testFile));
        long modifiedTS = createdTS + 1000;
        addFile.setLastModified(modifiedTS);
        // should run the timertask once
        scanner.run();
        Long persistedTS = new Long(tsp.getTimestamps().getProperty(testFile));
        assertTrue("expect the new Timestamp in the persistence object",
                   persistedTS.longValue() > createdTS);
        addFile.delete();
        // should run the timertask once
        scanner.run();
        Thread.sleep(500);
        assertTrue("timestamps should be empty", tsp.getTimestamps().isEmpty());
    }

    public void testAddDirectory() throws Exception {
        File dir = new File(directory);
        String testDir = "nestedDirectory";
        String testFile = "bob.xml";

        DirectoryMonitorCallback dmc = new TestDirectoryMonitorCallback();

        InMemoryTimestampPersistenceFactory factory =
                new InMemoryTimestampPersistenceFactory();

        // create it without its timertask being registered with the internal
        // timer as this allows us to predictably test it
        DirectoryMonitorRegistry dmr = new DirectoryMonitorRegistry(false);
        dmr.register(7L, directory, dmc, factory, true);
        TimerTask scanner = dmr.getScanner();
        TimestampPersistence rootTSP = getPersistence(dmr, directory);

        // create a directory
        File addDir = new File(dir,testDir);
        addDir.delete();
        addDir.mkdirs();
        long createdTS = addDir.lastModified();

        // Check that it's been noticed -  should run the timertask once
        scanner.run();
        assertFalse(rootTSP.getTimestamps().isEmpty());
        assertTrue("expect the added directory in the Timestamp persistence",
                   rootTSP.getTimestamps().containsKey(testDir));
        long modifiedTS = createdTS + 1000;
        addDir.setLastModified(modifiedTS);
        // should run the timertask once
        scanner.run();
        Long persistedTS = new Long(rootTSP.getTimestamps().getProperty(testDir));
        assertTrue("expect the new Timestamp in the persistence object",
                   persistedTS.longValue() > createdTS);

        // Add a file to the nested directory, and check it gets noticed.
        File addFile = new File(addDir,testFile);
        addFile.createNewFile();
        long createdFileTS = addFile.lastModified();
        TimestampPersistence nestedTSP =
                getPersistence(dmr, addDir.getCanonicalPath());
        // should run the timertask once
        scanner.run();
        assertFalse(rootTSP.getTimestamps().isEmpty());
        assertTrue("expect the added directory in the Timestamp persistence",
                   rootTSP.getTimestamps().containsKey(testDir));
        assertTrue("expect the added file in the Timestamp persistence",
                   nestedTSP.getTimestamps().containsKey(testFile));
        long modifiedFileTS = createdTS + 1000;
        addFile.setLastModified(modifiedFileTS);
        // should run the timertask once
        scanner.run();
        Long persistedFileTS = new Long(nestedTSP.getTimestamps().getProperty(testFile));
        assertTrue("expect the new Timestamp in the persistence object",
                   persistedFileTS.longValue() > createdFileTS);

        // Delete the file and check it is noticed - should run the timertask once
        addFile.delete();
        scanner.run();
        Thread.sleep(500);
        assertFalse(rootTSP.getTimestamps().isEmpty());
        assertTrue("expect the added directory in the Timestamp persistence",
                   rootTSP.getTimestamps().containsKey(testDir));
        assertFalse("expect the added file in the Timestamp persistence",
                   nestedTSP.getTimestamps().containsKey(testFile));

        // Delete the nested directory, and check it gets noticed.
        addDir.delete();
        // should run the timertask once
        scanner.run();
        Thread.sleep(500);
        assertTrue("timestamps should be empty", rootTSP.getTimestamps().isEmpty());
    }

    public void testUnregisterBundle() throws Exception {
        File dir = new File(directory);
        DirectoryMonitorCallback dmc = new TestDirectoryMonitorCallback();

        InMemoryTimestampPersistenceFactory factory =
                new InMemoryTimestampPersistenceFactory();

        // create it without its timertask being registered with the internal
        // timer as this allows us to predictably test it
        DirectoryMonitorRegistry dmr = new DirectoryMonitorRegistry(false);
        dmr.register(7L, directory, dmc, factory, false);
        // this will fail quietly as bundle 8 does not own that file
        dmr.unregister(8L);
        // therefore this should still cause an error as the file is still
        // registered.
        try {
            dmr.register(7L, directory, dmc, factory, false);
            fail("exception should have been thrown");
        } catch (RegistrationException re) {
            // success
        }
    }

    public void testUnregisterAllBundle() throws Exception {
        // create a tmp file
        File tmp = File.createTempFile("tspersistence", null);
        // delete it
        tmp.delete();
        //create it as a directory
        tmp.mkdirs();
        String directory2 = tmp.getCanonicalPath();

        try {
        File dir = new File(directory);
        DirectoryMonitorCallback dmc = new TestDirectoryMonitorCallback();

        InMemoryTimestampPersistenceFactory factory =
                new InMemoryTimestampPersistenceFactory();

        // create it without its timertask being registered with the internal
        // timer as this allows us to predictably test it
        DirectoryMonitorRegistry dmr = new DirectoryMonitorRegistry(false);
        // register 2 dirs to the same bundle
        dmr.register(7L, directory, dmc, factory, false);
        dmr.register(7L, directory2, dmc, factory, false);

        // unregister the bundle
        dmr.unregister(7L);
        //should succed as the bundle unregistered
        dmr.register(7L, directory, dmc, factory, false);
        dmr.register(7L, directory2, dmc, factory, false);
        } finally {
            IOUtils.deleteDirectoryContents(tmp);
            tmp.delete();
        }
    }

    public void testUnregisterFile() throws Exception {
        File dir = new File(directory);
        DirectoryMonitorCallback dmc = new TestDirectoryMonitorCallback();

        InMemoryTimestampPersistenceFactory factory =
                new InMemoryTimestampPersistenceFactory();

        // create it without its timertask being registered with the internal
        // timer as this allows us to predictably test it
        DirectoryMonitorRegistry dmr = new DirectoryMonitorRegistry(false);
        dmr.register(7L, directory, dmc, factory, false);

        // this will fail quietly as bundle 8 does not own that file
        dmr.unregister(8L, directory);
        // therefore this should still cause an error as the file is still
        // registered.
        try {
            dmr.register(7L, directory, dmc, factory, false);
            fail("exception should have been thrown");
        } catch (RegistrationException re) {
            // success
        }
    }

    private TimestampPersistence getPersistence(DirectoryMonitorRegistry dmr,
                                                String dirName) {
        TimestampPersistence persistence = null;
        Iterator i = dmr.cloneMonitoredDirectories().iterator();
        while (i.hasNext()) {
            MonitoredDirectoryInfo mdi = (MonitoredDirectoryInfo) i.next();
            if (mdi.getCanonicalDirname().equals(dirName)) {
                persistence = mdi.getPersistence();
            }
        }
        return persistence;
    }

    /**
     * Just need an empty implementation. Mocks don't work with multi-threaded
     * code (even though this code is no longer multi-thredaed Ive carried on
     * using this class rather then the mock)
     */
    private static class TestDirectoryMonitorCallback extends
        DirectoryMonitorCallbackImpl {


        public void beginChangeSet(File directory) {
            // do nothing
        }

        public void endChangeSet(File directory) {
            // do nothing
        }

        public void fileAdded(File directory, String filename) {
            // do nothing
        }

        public void fileRemoved(File directory, String filename) {
            // do nothing
        }

        public void fileUpdated(File directory, String filename) {
            // do nothing
        }
    }

}
