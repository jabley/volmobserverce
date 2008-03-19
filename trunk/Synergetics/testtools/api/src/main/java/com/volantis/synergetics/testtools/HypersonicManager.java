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
 * $Header: /src/voyager/com/volantis/testtools/HypersonicManager.java,v 1.1 2003/03/07 10:21:46 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Geoff           VBM:2003010904 - Created; manages setup and 
 *                              teardown for Hypersonic Databases. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools;

import com.volantis.synergetics.UndeclaredThrowableException;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Manages setup and teardown for Hypersonic Databases.
 *
 * @todo there still a quite a few classes that could use this; do a usage
 * search on {@link #BOGUS_HARDCODED_SOURCE} to see what I mean.
 * @todo Hypersonic supports In Memory Databases, which would be worth using to
 * avoid dependencies if possible.
 *
 * Note: Hypersonic lauches background threads, which can be problematic for
 * single VM testing if not closed properly.
 */
public class HypersonicManager {

    /**
     * The hardcoded source that Hypersonic uses for in-memory databases. <p>
     * Using an in memory database has the following advantages: <ul> <li>It
     * appears to be much faster - the mysterious pauses you get from using the
     * file based database disappear. <li>It doesn't appear to leave background
     * threads lying around. </ul> Using an in memory database has the
     * following disadvantages: <ul> <li>It appears that the content of the
     * database disappears when the connection is closed. Thus, the user must
     * be able to set up the database and perform the test all in a single
     * connection. </ul> NOTE: over time it may be useful to use this as the
     * default rather than the file based default source, given all the
     * problems that seemingly has.
     */
    public static String IN_MEMORY_SOURCE = ".";

    /**
     * The default file source to use for file based databases. <p> NOTE: using
     * this source will more than likely leave background threads lying around
     * after your test has run, even in you use {@link HypersonicManager#useCleanupWith}.
     * Try and use the in-memory version if possible!
     *
     * @deprecated leaves background threads around - use in-memory if
     *             possible,
     */
    public static String DEFAULT_FILE_SOURCE = "hypersonic-db";

    /**
     * A hardcoded source for those old test cases which still do their clean
     * up manually / not at all rather than use this class. <p> I've got a
     * feeling that they depend on each other as well, so I'm making their
     * source separate from the classes that use this properly to avoid
     * stepping on their toes. <p> They end up leaving a copy of the files
     * around after the test suite has finished, which is very bogus. <p> They
     * should all be refactored to use this class properly.
     *
     * @deprecated Use this class properly instead of using this!
     */
    public static String BOGUS_HARDCODED_SOURCE = "bogus-hypersonic-db";

    /**
     * The (default) driver class for Hypersonic. It's unlikely to have any
     * other :-).
     */
    public static String DEFAULT_DRIVER_CLASS = "org.hsqldb.jdbcDriver";

    // The username and password should probably be normal accessors like
    // source, but the dodgy old testcases depend on these now so it better
    // wait till they all disappear.
    
    /**
     * A useful default user for Hypersonic; has admin priviledges.
     */
    public static String DEFAULT_USERNAME = "sa";

    /**
     * Password for the default user.
     */
    public static String DEFAULT_PASSWORD = "";

    /**
     * The JDBC "source" we will use for the basename of the database files to
     * clean up.
     */
    private String source;

    // The various files that Hypersonic may create.
    private File data;

    private File properties;

    private File script;

    /**
     * Create an instance of this class with a default, on disk source.
     *
     * @deprecated leaves background threads around - use in-memory if
     *             possible,
     */
    public HypersonicManager() {

        this(DEFAULT_FILE_SOURCE);
    }

    /**
     * Create an instance of this class with an explicit source.
     *
     * @param source JDBC source to check.
     */
    public HypersonicManager(String source) {
        try {
            Class.forName(DEFAULT_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        }

        this.source = source;
        this.data = new File(source + ".data");
        this.properties = new File(source + ".properties");
        this.script = new File(source + ".script");
    }

    /**
     * Returns the source used.
     *
     * @return the source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the JDBC url used.
     *
     * @return the JDBC url.
     */
    public String getUrl() {
        return "jdbc:hsqldb:" + source;
    }

    /**
     * Run some test code provided by the executor, and then clean up the
     * Hypersonic database files left over afterwards. <p> Uses the source to
     * decide what database to clean up, since in Hypersonic the source usually
     * contains the root file name of the files created.
     *
     * @param executor code to run which will create a hypersonic database.
     * @throws java.lang.Exception if there was a problem.
     */
    public void useCleanupWith(Executor executor) throws Exception {
        // An alternative impl would be just to clean out the tables but 
        // presumably this would be faster? Hmmm. We'd need to create things
        // in a temp directory rather than the current directory in this case.
        
        // Ensure we are in a known state before we execute.
        cleanupFiles();
        try {
            // Do the work which uses Hypersonic.
            executor.execute();
        } finally {
            // Attempt to clean up after ourselves.
            
            // Shutdown the database. 
            // Just cleaning up the files does not have the same effect.
            Class.forName(DEFAULT_DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(getUrl(),
                                                                DEFAULT_USERNAME,
                                                                DEFAULT_PASSWORD);
            Statement statement = connection.createStatement();
            statement.execute("shutdown");
            statement.close();
            try {
                connection.close();
            } catch (Exception e) {
                // ignore
            }

            // Cleanup any files created
            cleanupFiles();
        }
    }

    /**
     * Cleans up Hypersonic database files, if any exist.
     *
     * @throws java.io.IOException if there was a problem.
     */
    public void cleanupFiles() throws IOException {
        delete(data);
        delete(properties);
        delete(script);
    }

    /**
     * Delete an individual file, if it exists.
     *
     * @param file to delete.
     * @throws java.io.IOException if there was a problem.
     */
    private void delete(File file) throws IOException {
        if (file != null && file.exists()) {
            if (!file.delete()) {
                throw new IOException("Unable to delete Hypersonic " +
                                      "database for '" + source + "', file:" + file);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Aug-04	306/5	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 11-Jun-03	18/1	allan	VBM:2003022820 SQL Connector

 10-Jun-03	15/1	allan	VBM:2003060907 Move some more testtools to here from MCS

 ===========================================================================
*/
