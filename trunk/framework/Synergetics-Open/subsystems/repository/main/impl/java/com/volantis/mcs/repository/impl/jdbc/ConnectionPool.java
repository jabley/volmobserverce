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
 * $Header: /src/voyager/com/volantis/mcs/repository/jdbc/ConnectionPool.java,v 1.21 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 10-Jul-01    Paul            VBM:2001050209 - Added a closed flag to the
 *                              LogicalConnection so that it's closed state
 *                              can be different to the physical connection's.
 * 09-Aug-01    Allan           VBM:2001080805 - Added keep connection alive
 *                              function to the background thread.
 * 09-Aug-01    Kula            VBM:2001071607 In Start method print statements
 *                              added to rectify the mssql connection problem
 * 10-Aug-01    Allan           VBM:2001080905 - Renamed connection-pole...
 *                              etc to connection-poll...
 * 14-Aug-01    Paul            VBM:2001081404 - Fixed minor problem when
 *                              compiling on J2SDK1.4.0.
 * 16-Aug-01    Paul            VBM:2001080805 - The keep alive mechanism
 *                              wasn't closing the connection that it got and
 *                              the PooledConnection wasn't cleaning up
 *                              properly so two references were obtained to
 *                              the same connection with dire consequences. The
 *                              keep alive code was also polling too
 *                              frequently.
 * 29-Aug-01    Allan           VBM:2001071607 -Modified SimplePooledConnection
 *                              constructor and resetPhysicalConnection()
 *                              to log a warning if the type map feature
 *                              appears not to be supported.
 * 29-Aug-01    Allan           VBM:2001082901 - Add a catch for
 *                              AbstractMethodError in the
 *                              SimplePooledConnection constructor and
 *                              in resetPhysicalConnection on calls to get and
 *                              set the typeMap.
 * 15-Oct-01    Doug            VBM:2001101101 - Modified the method
 *                              resetPhysicalConnection and the constructor for
 *                              SimplePooledConnection so that if exception is
 *                              caught a message is logged rather than a
 *                              Warning. The exception is expected and occurs
 *                              so frequently that logging as a Warning was
 *                              obscuring genuine warnings.
 * 21-Nov-01    Payal           VBM:2001111202 - Modified method
 *                              run to add REVISION_TABLE_NAME constant.
 * 27-Nov-01    Paul            VBM:2001112205 - Used ArrayList instead of
 *                              Vector because it is faster as it does not
 *                              synchronize its method calls. Added lots of
 *                              logging and made the fireConnectionClosed and
 *                              fireConnectionErrorOccurred reentrant with
 *                              the removeConnectionListener and
 *                              addConnectionListener.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.accessors.jdbc.JDBCAccessorHelper;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;

/**
 * The keepConnectionsAlive flag and the connectionPollInterval are used to
 * control the keep alive mechanism. This is required because if there is a
 * firewall between the application server and the database then the socket(s)
 * which the connections use can be closed down if they are unused for a
 * period of time. The keep alive mechanism attempts to prevent that by
 * periodically doing some work on a connection. It assumes that all
 * connections which are busy are actually doing some work and that all the
 * connections use the same port through the firewall so that using one
 * will keep all the others alive as well. If this is not the case then it
 * will either have to periodically do some work on all the free connections,
 * or test each connection to make sure that it still works before returning
 * it.
 *
 * The keep alive mechanism could either have used its own thread, or it could
 * have been added to the background thread and I chose to do the latter even
 * though the code was more complicated because it reduces the asynchronous
 * interactions which can be difficult to assess. If the disadvantages of this
 * approach outweight the advantages then this can be reconsidered.
 *
 * The way it works is to calculate the time when we need to poll a connection
 * and then makes sure that if we have to go to sleep we will always wake up
 * on time (within limitations of scheduling). When we wake up, or if we have
 * not been to sleep we check to see whether it is time to poll, if it is we
 * poll a connection if none of the connections are busy and at least one
 * connection is free. Polling a connection simply involves performing a query
 * using that connection. If an error occurs while performing the query then
 * the connection is assumed to be broken and so is discarded.
 *
 * TODO: If the logical connection is not closed then the pooled connection
 * will never be freed, even though the user of the logical connection no
 * longer has a reference to it. If the pooled connection uses a weak
 * reference to refer to the logical connection then if there are no strong
 * references to it it will be garbage collected and the reference cleared.
 * The background thread can check all the busy pooled connections to see
 * whether they still have a reference to the logical connection and if they
 * don't then they can be freed.
 */

class ConnectionPool
        implements DataSource,
        ConnectionPoolDataSource {
    /**
     * The logger to log to.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ConnectionPool.class);
    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(ConnectionPool.class);


    private DataSource dataSource;

    private List freeConnections;

    private List busyConnections;

    private List brokenConnections;

    private int maxConnections;

    private int maxFreeConnections;

    //private int optimalFreeConnections;
    private int minFreeConnections;

    private int initialConnections;

    private BackgroundThread backgroundThread;

    private volatile boolean terminateThread;

    private PooledConnectionListener listener;

    /**
     * Constant for the name of the revision table in the database.
     */
    private static final String REVISION_TABLE_NAME
            = JDBCAccessorHelper.TABLE_NAME_PREFIX + "REVISION";

    private boolean keepConnectionsAlive = false;

    private int connectionPollInterval = 1000 * 60 * 60; // 1 hour

    public ConnectionPool(DataSource dataSource) {
        this.dataSource = dataSource;

        freeConnections = new ArrayList();
        busyConnections = new ArrayList();
        brokenConnections = new ArrayList();
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void setMaxFreeConnections(int maxFreeConnections) {
        this.maxFreeConnections = maxFreeConnections;
    }

    /*
      public void setOptimalFreeConnections (int optimalFreeConnections) {
      this.optimalFreeConnections = optimalFreeConnections;
      }
    */

    public void setMinFreeConnections(int minFreeConnections) {
        this.minFreeConnections = minFreeConnections;
    }

    public void setInitialConnections(int initialConnections) {
        this.initialConnections = initialConnections;
    }

    public void setKeepConnectionsAlive(boolean keepConnectionsAlive) {
        this.keepConnectionsAlive = keepConnectionsAlive;
    }

    /**
     * Set connectionPollInterval.
     * @param connectionPollInterval value in seconds to set to
     * connectionPollInterval.
     */
    public void setConnectionPollInterval(int connectionPollInterval) {
        this.connectionPollInterval = connectionPollInterval * 1000;
    }

    public void start()
            throws RepositoryException {

        // Make sure that the values are reasonable.
        if (maxConnections < 1
                || maxFreeConnections < 0
                //|| optimalFreeConnections < 0
                || minFreeConnections < 0
                || initialConnections < 0
                || maxConnections < maxFreeConnections
                //||maxFreeConnections < optimalFreeConnections
                //||optimalFreeConnections < minFreeConnections
                || maxFreeConnections < initialConnections
                || initialConnections < minFreeConnections) {

            throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                    "jdbc-pool-invalid-configuration", new Integer[]{
                        new Integer(maxConnections),
                        new Integer(maxFreeConnections),
                        new Integer(initialConnections),
                        new Integer(minFreeConnections)
                    }));
        }

        listener = new PooledConnectionListener();

        try {
            for (int c = 0; c < initialConnections; c += 1) {
                PooledConnection pooledConnection = createPooledConnection();
                freeConnections.add(pooledConnection);
            }
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        }

        backgroundThread = new BackgroundThread();
		backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    public void terminate() {

        if (logger.isDebugEnabled()) {
            logger.debug("Closing all connections");
        }

        // If the thread has not been terminated then terminate it.
        if (!terminateThread) {
            terminateThread = true;
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to lock backgroundThread");
            }
            synchronized (backgroundThread) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Notifying background thread (terminated)");
                }
                backgroundThread.notify();
                backgroundThread = null;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to lock freeConnections");
        }
        synchronized (freeConnections) {

            int broken = brokenConnections.size();
            int free = freeConnections.size();
            int busy = busyConnections.size();

            if (logger.isDebugEnabled()) {
                logger.debug("Status free:" + free
                             + " busy: " + busy
                             + " broken: " + broken);

                logger.debug("Closing broken connections");
            }
            for (int c = broken - 1; c >= 0; c -= 1) {
                discardPooledConnection((PooledConnection)
                        brokenConnections.remove(c));
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Closing free connections");
            }
            for (int c = free - 1; c >= 0; c -= 1) {
                discardPooledConnection((PooledConnection)
                        freeConnections.remove(c));
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Closing busy connections");
            }
            for (int c = busy - 1; c >= 0; c -= 1) {
                discardPooledConnection((PooledConnection)
                        busyConnections.remove(c));
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Status free:" + freeConnections.size()
                             + " busy: " + busyConnections.size()
                             + " broken: " + brokenConnections.size());
            }
        }
    }

    // ---- DataSource/ConnectionPoolDataSource interface start ----

    public Connection getConnection()
            throws SQLException {

        PooledConnection pooledConnection = getPooledConnection();
        Connection connection = pooledConnection.getConnection();

        if (logger.isDebugEnabled()) {
            logger.debug("Got connection " + connection
                         + " from pooled connection " + pooledConnection);
        }

        return connection;
    }

    public Connection getConnection(String username, String password)
            throws SQLException {

        throw new UnsupportedOperationException();
    }

    public PooledConnection getPooledConnection(String user, String password)
            throws SQLException {

        throw new UnsupportedOperationException();
    }

    public PooledConnection getPooledConnection()
            throws SQLException {

        PooledConnection pooledConnection = null;
        boolean notifyThread = false;

        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to lock ConnectionPool");
        }
        synchronized (this) {
            if (logger.isDebugEnabled()) {
                logger.debug("Locked ConnectionPool");
            }
            while (pooledConnection == null && !terminateThread) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempting to lock freeConnections");
                }
                synchronized (freeConnections) {

                    int free = freeConnections.size();
                    if (free != 0) {
                        // Remove the last free connection and remember to
                        // notify the background thread so that it can allocate
                        // another one if necessary.

                        pooledConnection = getFreePooledConnection();

                        notifyThread = true;

                        if (logger.isDebugEnabled()) {
                            logger.debug("Got pooled connection "
                                         + pooledConnection);

                            logger.debug("Status free:" +
                                         freeConnections.size() +
                                         " busy: " + busyConnections.size() +
                                         " broken: " +
                                         brokenConnections.size());
                        }
                        // Break out of the loop.
                        break;
                    }

                    if (logger.isDebugEnabled()) {
                        logger.debug("Status free:" + freeConnections.size()
                                     + " busy: " + busyConnections.size()
                                     + " broken: "
                                     + brokenConnections.size());
                    }
                }

                // There are no free connections so go to sleep. There is no need
                // to wake the background thread because if the total number of
                // connections has not been exceeded it will have been woken up
                // when the last free connection was obtained.
                if (logger.isDebugEnabled()) {
                    logger.debug("Waiting for a pooled connection");
                }

                try {
                    wait();
                } catch (InterruptedException ie) {
                    // Reassert the interrupted state.
                    Thread.currentThread().interrupt();
                    return null;
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Woken up");
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("About to unlock ConnectionPool");
            }
        }

        if (notifyThread) {
            if (backgroundThread != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempting to lock backgroundThread");
                }
                synchronized (backgroundThread) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Notifying background thread (got)");
                    }
                    backgroundThread.notify();
                }
            }
        }

        return pooledConnection;
    }

    public void setLogWriter(PrintWriter out)
            throws SQLException {

        dataSource.setLogWriter(out);
    }

    public PrintWriter getLogWriter()
            throws SQLException {

        return dataSource.getLogWriter();
    }

    public void setLoginTimeout(int seconds)
            throws SQLException {

        dataSource.setLoginTimeout(seconds);
    }

    public int getLoginTimeout()
            throws SQLException {

        return dataSource.getLoginTimeout();
    }

    // ---- DataSource/ConnectionPoolDataSource interface end ----

    /**
     * This method must only be called while holding the freeConnections
     * monitor and when it is known that it is not empty. It removes a pooled
     * connection from the list, adds it to the list of busy connections and
     * returns. Currently it removes the last connection in the list but in
     * future there may be different configurable strategies of connection
     * use, e.g. most recently used (lifo), least recently used (fifo).
     *
     * @return A free pooled connection.
     */
    private PooledConnection getFreePooledConnection() {
        int free = freeConnections.size();

        PooledConnection pooledConnection
                = (PooledConnection) freeConnections.remove(free - 1);

        busyConnections.add(pooledConnection);

        return pooledConnection;
    }

    private PooledConnection createPooledConnection()
            throws SQLException {

        Connection physicalConnection = dataSource.getConnection();

        if (physicalConnection == null) {
            throw new SQLException("Could not get connection");
        }

        PooledConnection connection
                = new SimplePooledConnection(physicalConnection);

        if (logger.isDebugEnabled()) {
            logger.debug("Created " + connection
                         + " for " + physicalConnection);
        }

        connection.addConnectionEventListener(listener);

        return connection;
    }

    private void discardPooledConnection(PooledConnection connection) {
        if (logger.isDebugEnabled()) {
            logger.debug("Discarding pooled connection " + connection);
        }
        connection.removeConnectionEventListener(listener);
        try {
            connection.close();
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
        }
    }

    /**
     * A connection event listener that allows us to return connections to the
     * pool when they are closed.
     */
    private class PooledConnectionListener
            implements ConnectionEventListener {
        // javadoc inherited
        public void connectionClosed(ConnectionEvent e) {
            PooledConnection pooledConnection = (PooledConnection) e.getSource();

            boolean notify = false;

            if (logger.isDebugEnabled()) {
                logger.debug("Pooled connection " + pooledConnection +
                             " has been closed");

                // Protect the data structures.
                logger.debug("Attempting to lock freeConnections");
            }
            synchronized (freeConnections) {
                // Try and remove the connection from the busy list and add it
                // to the free list.
                if (busyConnections.remove(pooledConnection)) {
                    freeConnections.add(pooledConnection);

                    // Remember to notify one of those waiting for a new
                    // connection and the background thread.
                    notify = true;

                    if (logger.isDebugEnabled()) {
                        logger.debug("Status free:" + freeConnections.size()
                                     + " busy: " + busyConnections.size()
                                     + " broken: " + brokenConnections.size());
                    }
                }
            }

            if (notify) {
                // Notify one of those waiting for a free connection.
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempting to lock ConnectionPool");
                }
                synchronized (ConnectionPool.this) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Locked ConnectionPool");
                        logger.debug("Notifying thread waiting for a " +
                                     "connection (free)");
                    }
                    ConnectionPool.this.notify();
                    if (logger.isDebugEnabled()) {
                        logger.debug("About to unlock ConnectionPool");
                    }
                }

                // Notify the background thread.
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempting to lock backgroundThread");
                }
                synchronized (backgroundThread) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Notifying background thread (free)");
                    }
                    backgroundThread.notify();
                }
            }
        }

        // javadoc inherited
        public void connectionErrorOccurred(ConnectionEvent e) {
            SimplePooledConnection pooledConnection
                    = (SimplePooledConnection) e.getSource();

            boolean notifyThread = false;

            // Protect the data structures.
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to lock freeConnections");
            }
            synchronized (freeConnections) {
                // Try and remove the connection from whichever list it is in and
                // add it to the broken list.
                if (freeConnections.remove(pooledConnection)
                        || busyConnections.remove(pooledConnection)) {
                    brokenConnections.add(pooledConnection);

                    // Remember to notify the background thread.
                    notifyThread = true;

                    if (logger.isDebugEnabled()) {
                        logger.debug("Status free:" + freeConnections.size()
                                     + " busy: " + busyConnections.size()
                                     + " broken: " + brokenConnections.size());
                    }
                }
            }

            if (notifyThread) {
                // Notify the background thread.
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempting to lock backgroundThread");
                }
                synchronized (backgroundThread) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Notifying background thread (error)");
                    }
                    backgroundThread.notify();
                }
            }
        }
    }

    /**
     * A thread that handles connection keep alive timeout.
     */
    private class BackgroundThread
            extends Thread {

        private static final int INVALID = -1;

        private static final int SLEEP = 0;

        private static final int CLOSE = 1;

        private static final int OPEN = 2;

        // javadoc inherited
        public void run() {

            PooledConnection oldConnection = null;
            PooledConnection newConnection = null;

            long nextPollTime;
            long systemTime;

            // If we are keeping connections alive then we calculate the time
            // at which we next have to poll a connection by adding the poll
            // interval to the current time, otherwise we set it to the 'end
            // of time'.
            if (keepConnectionsAlive) {
                nextPollTime = System.currentTimeMillis() + connectionPollInterval;
                if (logger.isDebugEnabled()) {
                    logger.debug("Keeping connections alive:"
                                 + " poll interval is "
                                 + connectionPollInterval
                                 + " next poll time is " + nextPollTime);
                }
            } else {
                nextPollTime = Long.MAX_VALUE;
            }

            boolean notify;
            int action;
            while (true) {

                action = INVALID;
                notify = false;

                if (logger.isDebugEnabled()) {
                    logger.debug("Attempting to lock backgroundThread");
                }
                synchronized (this) {

                    if (terminateThread) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Terminating thread");
                        }
                        return;
                    }

                    // Protect the data structures.
                    if (logger.isDebugEnabled()) {
                        logger.debug("Attempting to lock freeConnections");
                    }
                    synchronized (freeConnections) {

                        // If we created a new connection last time around then
                        // add it to the free list.
                        if (newConnection != null) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Adding new connection");
                            }

                            freeConnections.add(newConnection);
                            newConnection = null;
                            notify = true;
                        }

                        int broken = brokenConnections.size();
                        int busy = busyConnections.size();
                        int free = freeConnections.size();
                        int total = busy + free;

                        if (logger.isDebugEnabled()) {
                            logger.debug("Status free:" + free
                                         + " busy: " + busy
                                         + " broken: " + broken);
                        }

                        // Remove all the broken connections.
                        for (int c = broken - 1; c >= 0; c -= 1) {
                            oldConnection = (PooledConnection) brokenConnections.remove(c);
                            if (logger.isDebugEnabled()) {
                                logger.debug("Discarding broken pooled connection "
                                             + oldConnection);
                            }
                            discardPooledConnection(oldConnection);
                        }

                        if (free > maxFreeConnections) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Above high water mark");
                            }

                            // Remove the last connection.
                            int last = free - 1;
                            oldConnection = (PooledConnection) freeConnections.remove(last);

                            // Remember to close the connection.
                            action = CLOSE;

                        } else if (total == maxConnections) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Maximum connections reached");
                            }

                            // Have reached the limit, there is nothing that
                            // can be done so go to sleep.
                            action = SLEEP;
                        } else if (free < minFreeConnections) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Below low water mark");
                            }

                            // Get another connection.
                            action = OPEN;
                        } else {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Nothing to do");
                            }
                            action = SLEEP;
                        }
                    }

                    // Only go to sleep if we do not have to notify waiting
                    // threads.
                    boolean dropThrough = true;
                    systemTime = -1;
                    if (!notify && action == SLEEP) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Sleeping with nothing to do");
                        }
                        dropThrough = false;
                        try {
                            if (keepConnectionsAlive) {
                                // Calculate how long we have before the next
                                // poll time, if the poll time has already
                                // passed then don't wait.
                                systemTime = System.currentTimeMillis();
                                long timeout = nextPollTime - systemTime;
                                if (timeout > 0) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug(
                                                "Keeping connections alive:" +
                                                " timeout is " + timeout);
                                    }
                                    wait(timeout);
                                    systemTime = -1;
                                }
                            } else {
                                wait();
                            }
                        } catch (InterruptedException ie) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Thread interrupted, exiting");
                            }
                            return;
                        }
                        if (logger.isDebugEnabled()) {
                            logger.debug("Woken");
                        }
                    }

                    // If we are trying to keep the connections alive then
                    // check to see whether it is time to poll the connection.
                    if (keepConnectionsAlive &&
                            ((systemTime = System.currentTimeMillis()) >=
                            nextPollTime)) {

                        if (logger.isDebugEnabled()) {
                            logger.debug("Keeping connections alive: poll " +
                                         "time " + systemTime);
                        }

                        PooledConnection pooledConnection = null;
                        if (logger.isDebugEnabled()) {
                            logger.debug("Attempting to lock freeConnections");
                        }
                        synchronized (freeConnections) {
                            // We can only poll the connection if there is a free
                            // connection and we only need to poll the connection
                            // if there are no busy connections.
                            if (freeConnections.size() > 0
                                    && busyConnections.size() == 0) {
                                pooledConnection = getFreePooledConnection();
                            } else {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Keeping connections alive:"
                                                 + " nothing to do,"
                                                 + " free connections "
                                                 + freeConnections.size()
                                                 + " busy connections "
                                                 + busyConnections.size());
                                }
                            }
                        }

                        // Do this outside the monitor.
                        if (pooledConnection != null) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(
                                        "Keeping connections alive: polling");
                            }

                            SimplePooledConnection.LogicalConnection
                                    sqlConnection = null;
                            boolean ok = false;
                            try {
                                // Get the SQL connection out of the pooled
                                // connection.
                                sqlConnection =
                                        (SimplePooledConnection.
                                        LogicalConnection)
                                        pooledConnection.getConnection();

                                Statement stmt = sqlConnection.
                                        createStatement();

                                String sql = "select revision from " +
                                        REVISION_TABLE_NAME;
                                if (logger.isDebugEnabled()) {
                                    logger.debug(sql);
                                }

                                ResultSet rs = stmt.executeQuery(sql);

                                stmt.close();
                                stmt = null;

                                // Close the connection which will add it back
                                // onto the free list.
                                sqlConnection.close();
                                sqlConnection = null;
                            } catch (SQLException sqle) {
                                logger.error("sql-exception", sqle);
                            } finally {
                                if (sqlConnection != null) {
                                    // An error occurred so assume that the
                                    // connection is broken and discard it.
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Discarding " +
                                                     pooledConnection +
                                                     " as it is broken");
                                        logger.debug("Attempting to lock " +
                                                     "freeConnections");
                                    }
                                    synchronized (freeConnections) {
                                        busyConnections.remove(
                                                pooledConnection);
                                    }
                                    discardPooledConnection(pooledConnection);
                                }
                            }

                            if (logger.isDebugEnabled()) {
                                logger.debug("Keeping connections alive: " +
                                             "done");
                            }
                        }

                        // Calculate the next time we need to poll the
                        // connection, by repeatedly adding the poll interval
                        // to the last time that we should have polled
                        // connections. This means that we never generate
                        // a poll time which is in the past and also we
                        // eliminate any jitter.
                        systemTime = System.currentTimeMillis();
                        do {
                            nextPollTime += connectionPollInterval;
                        } while (nextPollTime < systemTime);

                        if (logger.isDebugEnabled()) {
                            logger.debug("Keeping connections alive:" +
                                         " next poll time is " + nextPollTime);
                        }
                    }

                    // If we do not have anything we need to do while not
                    // holding the monitor on this thread then go back to the
                    // start of the loop, otherwise drop out.
                    if (!dropThrough) {
                        continue;
                    }
                }

                if (notify) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Attempting to lock ConnectionPool");
                    }
                    synchronized (ConnectionPool.this) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Locked ConnectionPool");
                            logger.debug("Notifying thread waiting for"
                                         + " a connection (new)");
                        }
                        ConnectionPool.this.notify();
                        if (logger.isDebugEnabled()) {
                            logger.debug("About to unlock ConnectionPool");
                        }
                    }
                }

                // Do these actions which may take quite a long time outside of
                // the monitor.
                switch (action) {
                case CLOSE:
                    {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Discarding excess pooled " +
                                         "connection " + oldConnection);
                        }
                        discardPooledConnection(oldConnection);
                        oldConnection = null;
                        break;
                    }

                case OPEN:
                    {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Getting a new pooled connection");
                        }
                        try {
                            newConnection = createPooledConnection();
                            if (logger.isDebugEnabled()) {
                                logger.debug("Created new pooled connection " +
                                             newConnection);
                            }
                        } catch (SQLException sqle) {
                            logger.error("sql-exception", sqle);
                        }
                        continue;
                    }

                default:
                    {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Unknown action " + action);
                        }
                    }
                }
            }
        }
    }
}

/**
 * A simple pooled connection implementation.
 */
class SimplePooledConnection
        implements PooledConnection {

    /**
     * The logger to log to.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SimplePooledConnection.class);

    /**
     * A flag which indicates whether we have already logged a warning about
     * the failure to get the type map.
     */
    private static boolean loggedTypeMapWarning;

    private List listeners;

    private Connection physicalConnection;

    private LogicalConnection logicalConnection;

    private boolean defaultAutoCommit;

    private String defaultCatalog;

    private boolean defaultReadOnly;

    private int defaultTransactionIsolation;

    private Map defaultTypeMap;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param physicalConnection
     *         the actual connection that is wrapped by this pooled connection
     * @throws SQLException if there is a problem obtaining the default state
     *                      of the connection
     */
    public SimplePooledConnection(Connection physicalConnection)
            throws SQLException {

        this.physicalConnection = physicalConnection;

        // Remember the default state of the connection.
        defaultAutoCommit = physicalConnection.getAutoCommit();
        defaultCatalog = physicalConnection.getCatalog();
        defaultReadOnly = physicalConnection.isReadOnly();
        defaultTransactionIsolation =
                physicalConnection.getTransactionIsolation();

        // The following method was added in JDBC 2.0 so it may fail if we are
        // using an older connection.
        try {
            defaultTypeMap = physicalConnection.getTypeMap();
        } catch (AbstractMethodError ame) {
            if (!loggedTypeMapWarning) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not get connection type map: "
                                 + "AbstractMethodError thrown");
                }
                loggedTypeMapWarning = true;
            }
        } catch (Exception e) {
            if (!loggedTypeMapWarning) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not get connection type map: "
                                 + "Exception thrown");
                }
                loggedTypeMapWarning = true;
            }
        }
    }

    // javadoc inherited
    public void addConnectionEventListener(ConnectionEventListener l) {
        if (listeners == null) {
            listeners = new ArrayList();
        }

        listeners.remove(l);
        listeners.add(l);
    }

    // javadoc inherited
    public void close()
            throws SQLException {

        if (logger.isDebugEnabled()) {
            logger.debug("Closing physical connection");
        }

        logicalConnection = null;
        physicalConnection.close();
        physicalConnection = null;
    }

    // javadoc inherited
    public Connection getConnection()
            throws SQLException {

        // Check to see whether the logical connection associated with this
        // pooled connection has been closed, if it hasn't then something has
        // gone wrong so mark it as closed and carry on. We cannot use the
        // normal close method to close it as this causes this pooled
        // connection to get added back in the pool so we call a special
        // version of close which does not fire connection closed
        // notifications.
        if (logicalConnection != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Discarding " + logicalConnection + " of " +
                             this);
            }
            logicalConnection.discard();
        }

        logicalConnection = new LogicalConnection(physicalConnection);

        return logicalConnection;
    }

    // javadoc inherited
    public void removeConnectionEventListener(ConnectionEventListener l) {
        if (listeners != null) {
            listeners.remove(l);
        }
    }

    /**
     * Reset the physical connection
     *
     * @exception SQLException if an error occurs
     */
    protected void resetPhysicalConnection()
            throws SQLException {

        // Discard the logical connection.
        logicalConnection = null;

        // If the physical connection has already been closed then throw
        // an exception.
        if (physicalConnection == null) {
            throw new SQLException("Physical connection has been closed");
        }

        if (!physicalConnection.getAutoCommit()) {
            physicalConnection.rollback();
        }

        physicalConnection.setAutoCommit(defaultAutoCommit);
        physicalConnection.setCatalog(defaultCatalog);
        physicalConnection.setReadOnly(defaultReadOnly);
        physicalConnection.setTransactionIsolation(defaultTransactionIsolation);

        // The following method was added in JDBC 2.0 so it may fail if we are
        // using an older connection.
        try {
            physicalConnection.setTypeMap(defaultTypeMap);
        } catch (AbstractMethodError ame) {
            if (!loggedTypeMapWarning) {
                logger.warn("connection-type-error");
                loggedTypeMapWarning = true;
            }
        } catch (Exception e) {
            if (!loggedTypeMapWarning) {
                logger.warn("connection-type-failure");
                loggedTypeMapWarning = true;
            }
        }
    }

    /**
     * Trigger a connection closed event.
     *
     * @param connection the connection for which the event should be
     *                   triggered.
     */
    protected void fireConnectionClosed(LogicalConnection connection) {

        if (connection != logicalConnection) {
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring closure of " + connection
                             + " as it is not " + logicalConnection);
            }
        }

        // Reset the physical connection, if something went wrong when
        // resetting then fireConnectionErrorOccurred instead.
        try {
            resetPhysicalConnection();
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            fireConnectionErrorOccurred(logicalConnection, sqle);
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Calling listeners to " + this);
        }
        if (!listeners.isEmpty()) {
            ConnectionEvent e = new ConnectionEvent(this);
            // Create a copy of the listeners list just in case the
            // connectionClosed method removes the listener.
            List list = new ArrayList(listeners);
            for (Iterator i = list.iterator(); i.hasNext();) {
                ((ConnectionEventListener) i.next()).connectionClosed(e);
            }
        }
    }

    /**
     * Trigger a connection error event.
     *
     * @param connection the connection for which the event should be
     *                   triggered.
     */
    protected void fireConnectionErrorOccurred(LogicalConnection connection,
                                               SQLException sqle) {

        if (connection != logicalConnection) {
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring error on " + connection
                             + " as it is not " + logicalConnection);
            }
        }

        if (listeners.isEmpty()) {
            ConnectionEvent e = new ConnectionEvent(this, sqle);
            // Create a copy of the listeners list just in case the
            // connectionClosed method removes the listener.
            List list = new ArrayList(listeners);
            for (Iterator i = list.iterator(); i.hasNext();) {
                ((ConnectionEventListener) i.next()).
                        connectionErrorOccurred(e);
            }
        }
    }

    /**
     * A logical connection that wraps a physical connection.
     */
    public class LogicalConnection
            implements Connection {

        private Connection physicalConnection;

        private boolean closed;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param physicalConnection the wrapped physical connection
         */
        public LogicalConnection(Connection physicalConnection) {
            this.physicalConnection = physicalConnection;
        }

        /**
         * Checks the connection's state and if it is closed throws an
         * exception.
         *
         * @throws SQLException if the connection is closed
         */
        protected void checkState()
                throws SQLException {
            if (closed) {
                throw new SQLException("Connection is closed");
            }
        }

        // javadoc inherited
        public void clearWarnings()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            physicalConnection.clearWarnings();
        }

        // javadoc inherited
        public void close()
                throws SQLException {

            if (logger.isDebugEnabled()) {
                logger.debug("Closing connection " + this);
            }

            // Make sure that the connection is in a suitable state.
            checkState();

            closed = true;
            fireConnectionClosed(this);
        }

        /**
         * This method simply marks this logical connection as being closed, it
         * does not fire connection closed events like close does. It is used
         * when the pooled connection wishes to discard its logical connection
         * without changing its own state.
         */
        private void discard() {
            closed = true;
        }

        // javadoc inherited
        public void commit()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            physicalConnection.commit();
        }

        // javadoc inherited
        public Statement createStatement()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.createStatement();
        }

        // javadoc inherited
        public Statement createStatement(int resultSetType,
                                         int resultSetConcurrency)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.createStatement(resultSetType,
                                                      resultSetConcurrency);
        }

        // javadoc inherited
        public boolean getAutoCommit()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.getAutoCommit();
        }

        // javadoc inherited
        public String getCatalog()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.getCatalog();
        }

        // javadoc inherited
        public DatabaseMetaData getMetaData()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.getMetaData();
        }

        // javadoc inherited
        public int getTransactionIsolation()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.getTransactionIsolation();
        }

        // javadoc inherited
        public Map getTypeMap()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.getTypeMap();
        }

        // javadoc inherited
        public SQLWarning getWarnings()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.getWarnings();
        }

        // javadoc inherited
        public boolean isClosed()
                throws SQLException {

            return closed;
        }

        // javadoc inherited
        public boolean isReadOnly()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.isReadOnly();
        }

        // javadoc inherited
        public String nativeSQL(String sql)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.nativeSQL(sql);
        }

        // javadoc inherited
        public CallableStatement prepareCall(String sql)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.prepareCall(sql);
        }

        // javadoc inherited
        public CallableStatement prepareCall(String sql, int resultSetType,
                                             int resultSetConcurrency)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.prepareCall(sql, resultSetType,
                                                  resultSetConcurrency);
        }

        // javadoc inherited
        public PreparedStatement prepareStatement(String sql)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.prepareStatement(sql);
        }

        // javadoc inherited
        public PreparedStatement prepareStatement(String sql,
                                                  int resultSetType,
                                                  int resultSetConcurrency)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            return physicalConnection.prepareStatement(sql, resultSetType,
                                                       resultSetConcurrency);
        }

        // javadoc inherited
        public void rollback()
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            physicalConnection.rollback();
        }

        // javadoc inherited
        public void setAutoCommit(boolean autoCommit)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            physicalConnection.setAutoCommit(autoCommit);
        }

        // javadoc inherited
        public void setCatalog(String catalog)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            physicalConnection.setCatalog(catalog);
        }

        // javadoc inherited
        public void setReadOnly(boolean readOnly)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            physicalConnection.setReadOnly(readOnly);
        }

        // javadoc inherited
        public void setTransactionIsolation(int level)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            physicalConnection.setTransactionIsolation(level);
        }

        // javadoc inherited
        public void setTypeMap(Map map)
                throws SQLException {

            // Make sure that the connection is in a suitable state.
            checkState();

            physicalConnection.setTypeMap(map);
        }

        //
        // The following methods were added to ensure that this class is
        // valid when loaded in a 1.4 VM, where they were added as part of
        // JDBC3.
        //
        // Since this connection pool is only used internally (by the
        // accessors), and we never call these methods (since we are still
        // compatible with JDBC1), we make them all throw
        // UnsupportedOperationException.
        //

        // javadoc inherited
        public void setHoldability(int holdability) throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public int getHoldability() throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public Savepoint setSavepoint() throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public Savepoint setSavepoint(String name) throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public void rollback(Savepoint savepoint) throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public Statement createStatement(int resultSetType,
                                         int resultSetConcurrency,
                                         int resultSetHoldability)
                throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public PreparedStatement prepareStatement(String sql,
                                                  int resultSetType,
                                                  int resultSetConcurrency,
                                                  int resultSetHoldability)
                throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public CallableStatement prepareCall(String sql,
                                             int resultSetType,
                                             int resultSetConcurrency,
                                             int resultSetHoldability)
                throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public PreparedStatement prepareStatement(String sql,
                                                  int autoGeneratedKeys)
                throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public PreparedStatement prepareStatement(String sql, int columnIndexes[])
                throws SQLException {
            throw new UnsupportedOperationException();
        }

        // javadoc inherited
        public PreparedStatement prepareStatement(String sql, String columnNames[])
                throws SQLException {
            throw new UnsupportedOperationException();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/
