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
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 04-Jun-01    Paul            VBM:2001051103 - Added a work connection which
 *                              is used to do administration work for locks
 *                              which cannot / must not be done on the clients
 *                              connection. Also added generic lock / unlock
 *                              methods which can be used for any objects.
 * 26-Jun-01    Paul            VBM:2001051103 - Added a check to make sure
 *                              that an object was locked before trying to
 *                              unlock it.
 * 16-Jul-01    Paul            VBM:2001070508 - Cleaned up and added a check
 *                              to make sure that an object wasn't already
 *                              locked before we locked it.
 * 09-Aug-01    Kula            MSSQL Support added
 * 09-Aug-01    Allan           VBM:2001080805 - Added support for new
 *                              jdbc config properties:
 *                              keep-connections-alive, and
 *                              connection-pole-interval.
 * 10-Aug-01    Allan           VBM:2001080905 - Renamed connection-pole...
 *                              etc to connection-poll...
 * 10-Aug-01    Doug            VBM:2001080809 Added support for DB2. Modified
 *                              selectUniqueValues to use the
 *                              getVendorSpecificSQLKeyWord.
 * 10-Aug-01    Paul            VBM:2001071607 - Changed the default key words,
 *                              allowed getVendorSpecificSQLKeyWord to return
 *                              null to indicate no support for a keyword and
 *                              fixed a minor problem with selectUniqueValues.
 * 29-Aug-01    Allan           VBM:2001082902 - Modify all SQL statements
 *                              that refered to the KEY column to refer to
 *                              LOCKKEY instead.
 * 05-Oct-01    Paul            VBM:2001092801 - Added a list of supported
 *                              vendors and changed the getPort method to
 *                              return an int instead of a String.
 * 15-Oct-01    Paul            VBM:2001101202 - Added
 *                              createRepositoryAccessorManager method.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock method.
 * 17-Oct-01    Paul            VBM:2001101701 - Modify the
 *                              createRepositoryAccessorManager method to
 *                              return the singleton instance of
 *                              JDBCRepositoryAccessorManager rather than
 *                              create a new one every time.
 * 07-Nov-01    Mat             VBM:2001110701 Add ability to use an
 *                              application servers connection pool instead
 *                              of ours.
 * 08-Nov-01    Paul            VBM:2001110701 - Added property to allow caller
 *                              to optionally specify a DataSource to use
 *                              in preference to creating our own, also
 *                              removed all references to Volantis and
 *                              AppServerInterfaceManager.
 * 21-Nov-01    Payal           VBM:2001111202 - Modified method
 *                              lock() to add TABLE_NAME constant.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 28-Jan-02    Payal           VBM:2002012305 - Modified lock () ,
 *                              Added  boolean flag called abortOperationSet
 *                              and set to the result of beginOperationSet and
 *                              endOperationSet. The boolean flag is checked
 *                              in finally block and if true the
 *                              abortOperationSet() is called.
 * 29-Jan-02    Payal           VBM:2002012305 - Modified lock ()  to set
 *                              boolean flag to ! the result of
 *                              endOperationSet.
 * 29-Jan-02    Payal           VBM:2002012305 - Modified lock () moved
 *                              lockConnection.beginOperationSet statement
 *                              before the lockStmt.executeQuery.
 * 30-Jan-02    Allan           VBM:2002013006 - Modified openConnection() so
 *                              as not to create a new underlying connection.
 *                              Added getDataSource().
 * 11-Feb-02    Paul            VBM:2001122105 - Fixed minor problem in a
 *                              comment.
 * 13-Feb-02    Allan           VBM:2002020502 - Removed obselete code comment.
 * 25-Feb-02    Payal           VBM:2002022008 - Modified createJDBCRepository()
 *                              to get JDBCRepositoryConnectionImpl to check
 *                              valid login details are entered for an database
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 31-Oct-02    Adrian          VBM:2002103004 - Added new member boolean
 *                              anonymous and getter/setter methods.  Added
 *                              static final String ANONYMOUS_PROPERTY as key
 *                              to retrieve repository "anonymous" property
 *                              from map of properties.  Updated method
 *                              createJDBCRepository to set value of anonymous.
 *                              Updated createConnectionPool to throw an
 *                              Exception if anonymous is true as Mariner
 *                              ConnectionPool does not support anonymous login
 * 08-Nov-02    Steve           VBM:2002071604  -  Added Hypersonic SQL ID's
 * 19-Dec-02    Allan           VBM:2002120402 - Added todo comment regarding
 *                              2002071604 change in getPassword() and javadoc.
 * 07-Jan-02    Allan           VBM:2003010711 - Removed HyperSonic SQL from
 *                              the vendors array.
 * 04-Feb-03    Ian             VBM:2003020413 - Added MS MSSQL driver.
 * 18-Feb-03    Ian             VBM:2003021405 - Implemented VBM 2003020413 for
 *                              Mimas.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 12-May-03    Allan           VBM:2003051303 - Handling of connections
 *                              moved to here from AbstractRepository involving
 *                              overriding  and terminate() and adding
 *                              addConnection() and removeConnection().
 * 19-May-03    Allan           VBM:2003051303 - Removed conflict in javadoc.
 * 23-May-03    Phil W-S        VBM:2003052301 - Wrap logger.debug() statements
 *                              in condition.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.accessors.jdbc.JDBCAccessorHelper;
import com.volantis.mcs.accessors.jdbc.StringEnumeration;
import com.volantis.mcs.accessors.jdbc.UniqueStringEnumeration;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.AbstractRepository;
import com.volantis.mcs.repository.impl.jdbc.lock.JDBCLockManagerImpl;
import com.volantis.mcs.repository.jdbc.AlternateNames;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.mcs.repository.lock.LockManager;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A repository accessed by the java JDBC interface.
 */
public class JDBCRepositoryImpl
        extends AbstractRepository
        implements InternalJDBCRepository {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(JDBCRepositoryImpl.class);

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    JDBCRepositoryImpl.class);

    /**
     * A collection of database connections to use.
     */
    final Collection connections = new ArrayList(4);

    /**
     * Used to obtain a connection to the database.
     */
    private final DataSource dataSource;

    /**
     * The username to be used when attempting to open a connection to
     * the database.
     */
    private final String username;

    /**
     * The password to use when attempting to open a connection to the
     * database.
     */
    private final String password;

    /**
     * Flag to determine if anonymous connection is permitted for this repository
     */
    private final boolean anonymous;

    /**
     * Flag to determine whether connections may be released early.
     */
    private final boolean allowImmediateConnectionRelease;

    /**
     * True if the repository should use short names, false otherwise.
     */
    private final boolean useShortNames;

    /**
     * Used to store vendor specific SQL keywords
     */
    protected final Map sqlKeyTable = new HashMap();

    /**
     * The default chunk size
     */
    private int chunkSize = 1024;

    /**
     * Initialises an instance with the supplied parameters.
     *
     * @param configuration The configuration, data must be copied out of it
     *                      as it is mutable.
     */
    protected JDBCRepositoryImpl(InternalJDBCRepositoryConfiguration configuration) {
        sqlKeyTable.put("distinct", "distinct");
        sqlKeyTable.put("length", "length");
        sqlKeyTable.put("ceiling", "ceiling");

        this.dataSource = configuration.getDataSource();
        this.anonymous = configuration.isAnonymous();
        this.username = configuration.getUsername();
        this.password = configuration.getPassword();
        this.allowImmediateConnectionRelease =
                configuration.isReleaseConnectionsImmediately();

        this.useShortNames = configuration.isShortNames();
    }

    // Javadoc inherited.
    public String getAppropriateName(AlternateNames alternateNames) {
        if (useShortNames) {
            return alternateNames.getShortName();
        } else {
            return alternateNames.getNormalName();
        }
    }

    // ---- Overidden AbstractRepository methods.


    /**
     * Add a RepositoryConnection to the list of connections
     *
     * @param connection
     * @throws com.volantis.mcs.repository.RepositoryException
     *          If the repository is terminating.
     */
    protected void addConnection(RepositoryConnection connection)
            throws RepositoryException {
        synchronized (connections) {
            checkStatus();

            // Add the new connection to the list of connections.
            connections.add(connection);
            if (logger.isDebugEnabled()) {
                logger.debug("Added connection " + connection);
            }
        }
    }

    /**
     * Add a RepositoryConnection to the list of connections
     *
     * @param connection
     * @throws com.volantis.mcs.repository.RepositoryException
     *          If the repository is terminating.
     */
    protected void removeConnection(RepositoryConnection connection)
            throws RepositoryException {
        synchronized (connections) {

            // Remove the connection from the list of connections.
            connections.remove(connection);
            if (logger.isDebugEnabled()) {
                logger.debug("Removed connection " + connection);
            }
        }
    }

    /**
     * Terminate all the connections in the connectios collection.
     *
     * @throws com.volantis.mcs.repository.RepositoryException
     *
     */
    private void terminateAllConnections()
            throws RepositoryException {

        // Mark this repository as being in the process of terminating, do it
        // while holding the connections lock so that we don't set it while
        // another method is in the process of adding to the list.

        synchronized (connections) {
            // Check to make sure that this repository is in the right state to
            // terminate, if it is not then an exception is thrown.
            checkStatus();

            terminating = true;
        }

        // At this point none of the other methods will be accessing the
        // list of connections as those that need to all check the state of
        // the terminating and terminated flags first before they do. This
        // means that we do not have to protect the list while we iterate
        // through it.

        boolean exception = false;

        if (logger.isDebugEnabled()) {
            logger.debug("Terminating all connections");
        }

        // Copy the connections collections so that closeConnection() can
        // change it without causeing a ConcurrentModificationException.
        RepositoryConnection connectionsArray [] = new RepositoryConnection[0];
        connectionsArray = (RepositoryConnection[])
                connections.toArray(connectionsArray);
        for (int i = 0; i < connectionsArray.length; i++) {
            RepositoryConnection connection = connectionsArray[i];

            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Disconnecting " + connection);
                }
                closeConnection(connection);
                if (logger.isDebugEnabled()) {
                    logger.debug("Disconnected " + connection);
                }
            } catch (RepositoryException re) {
                re.printStackTrace();
                logger.error("repository-exception", re);
                exception = true;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Terminated all connections");
        }

        synchronized (connections) {
            terminated = true;
        }

        if (exception) {
            throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                    "repository-termination-error"));
        }
    }

    /**
     * Terminate this Repository. Override AbstractRepository.terminate() to
     * call terminateAllConnections() and remove the workConnection if there
     * is one.
     *
     * @throws com.volantis.mcs.repository.RepositoryException
     *
     */
    public void terminate()
            throws RepositoryException {

        RepositoryException exception = null;

        try {
            terminateAllConnections();
        } catch (RepositoryException re) {
            exception = re;
        }

        // Close all connections in the Connection Pool if there is one.
        if (dataSource instanceof ConnectionPool) {
            ConnectionPool connectionPool = (ConnectionPool) dataSource;

            connectionPool.terminate();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Disconnected all connections");
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Create and return a new RepositoryConnection
     *
     * @return a RepositoryConnection Object.
     */
    protected RepositoryConnection openConnection()
            throws RepositoryException {

        RepositoryConnection connection =
                new JDBCRepositoryConnectionImpl(this, anonymous, username, password,
                        allowImmediateConnectionRelease);

        return connection;
    }

    protected void closeConnection(RepositoryConnection connection)
            throws RepositoryException {

        // Cast the RepositoryConnection to a JDBCRepositoryConnectionImpl.
        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        // Ask it to close the connection.
        jdbcConnection.closeConnection();
    }

    // ---- End of overidden AbstractRepository methods.

    /**
     * Return an enumeration of the unique values in the column.
     */
    public
    RepositoryEnumeration selectUniqueValues(
            JDBCRepositoryConnection connection,
            String columnName,
            String tableName,
            String projectName)
            throws RepositoryException {

        return selectUniqueValues(connection, columnName, tableName, columnName,
                projectName);
    }

    /**
     * Return an enumeration of the unique values in the column.
     * <p/>
     * Different databases support this in different ways.
     * Most have a key word that you can use to return unique rows.
     * Vendor specific keywords can be retrieved via the method
     * getVendorSpecificSQLKeyWord. If a vendor specific keyword
     * exists for unique we will use that. If not then we shall filter
     * out duplicates from the result set ourselves.
     */
    protected
    RepositoryEnumeration selectUniqueValues(
            RepositoryConnection connection,
            String columnName,
            String tableName,
            String orderBy,
            String projectName)
            throws RepositoryException {

        // Cast the repository connection to a JDBC Connection in
        // order to get the java.sql.Connection out.
        Connection sqlConnection = ((JDBCRepositoryConnection)
                connection).getConnection();
        try {
            Statement stmt = sqlConnection.createStatement();
            String uniqueKeyword = getVendorSpecificSQLKeyWord("distinct");
            String sql
                    = "select " +
                    ((uniqueKeyword == null) ? "" : uniqueKeyword + " ")
                    + columnName + " from " + tableName
                    + " where PROJECT = "
                    + JDBCAccessorHelper.quoteValue(projectName)
                    + " order by " + orderBy;

            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }

            ResultSet rs = stmt.executeQuery(sql);

            if (uniqueKeyword == null) {
                // vendor does not support unique selection so filter results
                return new UniqueStringEnumeration(rs);
            }
            return new StringEnumeration(rs);
        } catch (SQLException sqle) {
            throw new JDBCRepositoryException(sqle);
        }
    }

    /**
     * Get the DataSource for this JDBCRepository.
     *
     * @return the DataSource associated with this JDBCRepository
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    public String getVendorSpecificSQLKeyWord(String keyword) {
        if (sqlKeyTable.containsKey(keyword)) {
            return (String) sqlKeyTable.get(keyword);
        }

        //return key;
        return null;
    }

    public LockManager getLockManager(String projectName) {
        return new JDBCLockManagerImpl(this, projectName);
    }


    //Javadoc inherited.
    public int getChunkSize() {
        return chunkSize;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10168/1	ianw	VBM:2005102504 Fixup table definitions

 10-Nov-05	10170/1	ianw	VBM:2005102504 Fixup DB column names

 20-May-05	8365/2	rgreenall	VBM:2005051614 Added Javadoc

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 22-Apr-05	7785/1	emma	VBM:2005041207 Merged from 3.3.0 - bug fix - now retrieves unique values from a JDBCRepository for a particular project (rather than just for the default project)

 22-Apr-05	7783/2	emma	VBM:2005041207 Merged from 3.2.3 - bug fix - now retrieves unique values from a JDBCRepository for a particular project (rather than just for the default project)

 23-Feb-05	7101/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	7091/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	6905/2	allan	VBM:2005020703 Added support for Sybase

 12-Jan-05	6627/1	geoff	VBM:2005011001 Support Cloudscape 10/Apache Derby as a repository RDBMS (take 2)

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 06-Oct-04	5710/3	geoff	VBM:2004052005 Short column name support

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 30-Sep-04	4511/6	tom	VBM:2004052005 Added short column support for new table columns and cache accessors

 27-May-04	4511/3	tom	VBM:2004052005 Added support for short column names

 06-Aug-04	5123/2	matthew	VBM:2004080401 Add MySQL Support

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 17-May-04	3649/1	mat	VBM:2004031910 Add short tablename support

 08-Apr-04	3653/1	mat	VBM:2004031910 Change accessors to support resolving the tablename from the repository

 04-May-04	4023/2	ianw	VBM:2004032302 Added support for short length tables

 30-Apr-04	4111/1	ianw	VBM:2004042908 Added new Public API to get a local JDBC Repository

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 16-Mar-04	2867/12	ianw	VBM:2004012603 Fixed some rework issues with datasource rationalisation

 09-Mar-04	2867/8	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 11-Mar-04	3376/3	adrian	VBM:2004030908 Rework to fix javadoc duplication

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 25-Feb-04	3136/1	philws	VBM:2004021908 Remove accessor manager singletons and make MCSDeviceRepositoryProvider and its test case use the runtime device accessor correctly

 19-Feb-04	2789/7	tony	VBM:2004012601 refactored localised logging to synergetics

 16-Feb-04	2789/5	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Feb-04	3007/3	doug	VBM:2004021103 Ensured the JDBCRepository#unlock method does not throw an IllegaArgumentException

 06-Feb-04	2883/3	claire	VBM:2004020516 Refactoring project code

 06-Feb-04	2883/1	claire	VBM:2004020516 Including project when selecting values

 30-Jan-04	2807/3	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 04-Jan-04	2360/2	andy	VBM:2003121710 added PROJECT column to all tables

 09-Oct-03	1524/1	philws	VBM:2003090101 Port of SQLSTATE handling from PROTEUS

 09-Oct-03	1522/1	philws	VBM:2003090101 Ensure that SQLSTATE codes are matched against class codes only

 27-Jun-03	586/1	byron	VBM:2003062704 Username and password are not used if no connection pooling in mariner-config.xml

 24-Jun-03	497/1	byron	VBM:2003062302 Issues with Database configuring and sql connector

 20-Jun-03	480/1	byron	VBM:2003062006 Fixed ClassCastException

 12-Jun-03	316/3	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
