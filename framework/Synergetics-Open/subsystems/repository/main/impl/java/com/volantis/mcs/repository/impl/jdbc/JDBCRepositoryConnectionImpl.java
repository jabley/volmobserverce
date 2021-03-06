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
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Apr-01    Paul            Created.
 * 04-Jun-01    Paul            VBM:2001051103 - Added methods to lock and
 *                              unlock objects and also to select unique values
 *                              from a column. All these methods defer to the
 *                              repository.
 * 26-Jun-01    Paul            VBM:2001051103 - Sorted out some throws
 *                              declarations and added a check for nested
 *                              operation sets.
 * 16-Jul-01    Paul            VBM:2001070508 - Cleaned up.
 * 10-Aug-01    Paul            VBM:2001071607 - Added helper method which
 *                              calls getVendorSpecificSQLKeyWord in the
 *                              repository class.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock method.
 * 27-Nov-01    Paul            VBM:2001112205 - Added more logging.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 28-Jan-02    Payal           VBM:2002012305 - Modified beingOperationSet(),
 *                              endOperationSet() and abortOperationSet() to
 *                              return a boolean true if successful,otherwise
 *                              false.
 * 29-Jan-02    Payal           VBM:2002012305 -  Modified beingOperationSet(),
 *                              endOperationSet() and abortOperationSet() to
 *                              comment finally.
 * 30-Jan-02    Allan           VBM:2002013006 - Modified getConnection() to
 *                              get the underlying connection if it is not
 *                              set. Added new constructor that takes
 *                              repository, username and password so that
 *                              getConnection() can create a new connection.
 *                              Modified all methods that use connection
 *                              directly to either check for null or ensure
 *                              the connection exists first.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 02-Apr-02	Mat             VBM:2002022009 - Added
 *                              setCacheRetrievedObjects() to constructor
 * 01-May-02    Jason           VBM:2002050102 - Added debug message to
 *                              getConnection, to help debug problems.
 * 24-Jul-02    Byron           VBM:2002052706 - Modified beginOperationSet()
 *                              to modify the isolation level depending on
 *                              the flag set in the Oracle8Repository
 * 31-Oct-02    Adrian          VBM:2002103004 - Added new boolean parameter
 *                              anonymous to constructor.  This is used to set
 *                              the value of the new member boolean anonymous.
 *                              The field is used in getConnection() to
 *                              determine if the connection is retrieved from
 *                              the DataSource anonymously or using the
 *                              username and password.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 12-May-03    Allan           VBM:2003051303 - acquireResources() modified to
 *                              call repository.addConnection(). Override
 *                              disconnect() to call closeConnection().
 *                              Modify closeConnection() to call
 *                              removeConnection().
 *                              a real connection. NOTE: acquireResources
 *                              will most likely need to move back into
 *                              getConnection().
 * 23-May-03    Phil W-S        VBM:2003052301 - Wrap logger.debug() statements
 *                              in condition.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.mcs.repository.Repository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import com.volantis.mcs.repository.impl.LocalRepositoryConnectionImpl;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 */
public class JDBCRepositoryConnectionImpl
  extends LocalRepositoryConnectionImpl
 implements JDBCRepositoryConnection, LocalRepositoryConnection {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(JDBCRepositoryConnectionImpl.class);
    
    /**
     * Used for localizing exceptions
     */ 
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = 
            LocalizationFactory.createExceptionLocalizer(
                    JDBCRepositoryConnectionImpl.class);

  private Connection connection;
  private String userName;
  private String password;

  /**
   * boolean flag to determine if a connection can be made anonymously to the
   * underlying repository.
   */
  private boolean anonymous;

    /**
     * boolean flag to determine if a connection can be released via the
     * method {@link #releaseConnection}
     */
    private boolean allowRelease = false;

  /**
   * Construct a new JDBCRepositoryException without an underlying database
   * connection.
   * @param repository the Repository associated with this connection
   * @param anonymous flag to determine if a connection can be made anonymously
   * @param userName the user name with which to later establish the
   * underlying connection
   * @param password  the password with which to later establish the
   * underlying connection
   */
  public JDBCRepositoryConnectionImpl (Repository repository, boolean anonymous,
                                   String userName, String password,
                                   boolean allowRelease) {
    super (repository);
    this.anonymous = anonymous;
    this.userName = userName;
    this.password = password;
    this.allowRelease = allowRelease;
  }

  /**
   * Construct a new JDBCRepositoryException with an underlying database
   * connection.
   * @param repository the Repository associated with this connection
   * @param connection the underlying database connection associated with
   * this connection.
   */
  public JDBCRepositoryConnectionImpl (Repository repository,
                                   Connection connection) {
    super (repository);
    this.connection = connection;
  }

  /**
   * Get the underlying jdbc Connection for this JDBCConnection object. If
   * the connection is not already established then this method will handle
   * that.
   * @return the Connection object of this connection
   * @throws RepositoryException if there is a problem accessing the
   * repository
   */
  public Connection getConnection () throws RepositoryException {
      if (connection == null) {
          try {
              if (logger.isDebugEnabled()) {
                  logger.debug("Getting database connection");
              }
              JDBCRepositoryImpl repository = (JDBCRepositoryImpl) getRepository();
              repository.addConnection(this); // Do this first.
              DataSource dataSource = repository.getDataSource();
              if (anonymous) {
                  connection = dataSource.getConnection();
              } else {
                  connection = dataSource.getConnection(userName, password);
              }
          } catch (SQLException sqle) {
              logger.error("sql-exception", sqle);
              throw new JDBCRepositoryException(sqle);
          }
          if (logger.isDebugEnabled()) {
              logger.debug("Got database connection " + connection);
          }
      }

      return connection;
  }

  /**
   * Close the real connection if there is one.
   * @throws RepositoryException if there was a problem closing the real
   * connection.
   */
  public void releaseConnection() throws RepositoryException {
      if (allowRelease) {
          closeConnection();
          connection = null;
      }
  }


    /**
     * Disconnect this connection if there is a real connection.
     * @throws RepositoryException
     */
    public void disconnect()
            throws RepositoryException {
        closeConnection();
    }

  /**
   * Close this RepositoryConnection and the underlying jdbc Connection if
   * it exists.
   * @throws RepositoryException if there is a problem accessing the
   * repository
   */
  public void closeConnection ()
    throws RepositoryException {

    // Close the underlying connection.
    try {
      if(connection!=null) {
        connection.close ();
          ((JDBCRepositoryImpl) getRepository()).removeConnection(this);
          if (logger.isDebugEnabled()) {
              logger.debug("Closed " + connection);
          }
      }
    }
    catch (SQLException sqle) {
      throw new JDBCRepositoryException (sqle);
    }
  }

  // ---- RepositoryConnection interface start ----

  public boolean beginOperationSet ()
    throws RepositoryException {
    try {
      getConnection(); // ensure we have a connection
      if (!connection.getAutoCommit ()) {
        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "repository-nested-operation-set"));
      }
      connection.setAutoCommit (false);

      int isolationLevel = Connection.TRANSACTION_SERIALIZABLE;

      // This is a workaround for oracle 8i problem with long transactions.
      // For imports, permit the isolation level to be modified to a lower
      // level to prevent the exception: 'ORA-08177: can't serialize access for
      // this transaction'. The default isolation level should be SERIALIZABLE.
        if (getRepository() instanceof Oracle8Repository) {
            isolationLevel = Connection.TRANSACTION_READ_COMMITTED;
        }
        
      connection.setTransactionIsolation (isolationLevel);

      return true;
    }
    catch (SQLException sqle) {
      throw new JDBCRepositoryException (sqle);
    }
    // finally {
    // return false;
    // }
  }

  public boolean endOperationSet ()
    throws RepositoryException {

    try {
      getConnection(); // ensure we have a connection
      if (!connection.isClosed() ) {
          connection.commit ();
          connection.setAutoCommit (true);
      }

      return true;
    }
    catch (SQLException sqle) {
      throw new JDBCRepositoryException (sqle);
    }
  }

  public boolean abortOperationSet ()
    throws RepositoryException {

    try {
      getConnection(); // ensure we have a connection
      if (!connection.isClosed() ) {
          connection.rollback ();
          connection.setAutoCommit(true);
      }
      return true;
    }
    catch (SQLException sqle) {
      throw new JDBCRepositoryException (sqle);
    }
  }

  public boolean supportsOperationSets () {
    return true;
  }

  // javadoc inherited
  public boolean isConnected ()
    throws RepositoryException {

    try {
      if(connection==null) {
        return false;
      } else {
        return !connection.isClosed ();
      }
    }
    catch (SQLException sqle) {
      throw new JDBCRepositoryException (sqle);
    }
  }

  // ---- RepositoryConnection interface end ----

  public InternalJDBCRepository getJDBCRepository () {
    return (InternalJDBCRepository) getRepository ();
  }

    public RepositoryEnumeration selectUniqueValues (String columnName,
                                                   String tableName,
                                                   String projectName)
    throws RepositoryException {

    return getJDBCRepository ().selectUniqueValues (
            this, columnName, tableName, projectName);
  }

  public String getVendorSpecificSQLKeyWord (String keyword) {
    return getJDBCRepository ().getVendorSpecificSQLKeyWord (keyword);
  }

  public RepositoryConnection getUnderLyingConnection() throws RepositoryException {
      return this;
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 22-Apr-05	7785/1	emma	VBM:2005041207 Merged from 3.3.0 - bug fix - now retrieves unique values from a JDBCRepository for a particular project (rather than just for the default project)

 22-Apr-05	7783/1	emma	VBM:2005041207 Merged from 3.2.3 - bug fix - now retrieves unique values from a JDBCRepository for a particular project (rather than just for the default project)

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 19-Feb-04	2789/10	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/8	tony	VBM:2004012601 localisation services update

 18-Feb-04	2789/5	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 18-Feb-04	3090/1	ianw	VBM:2004021716 Added extra debugging and removed error masking for IBM projects problem

 11-Feb-04	2761/2	mat	VBM:2004011910 Add Project repository

 05-Feb-04	2694/9	mat	VBM:2004011917 Rework for finding repositories

 05-Feb-04	2851/1	mat	VBM:2004020404 Amended code generators to fix the import/export

 03-Feb-04	2767/1	claire	VBM:2004012701 Adding project handling code

 30-Jan-04	2807/2	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 ===========================================================================
*/
