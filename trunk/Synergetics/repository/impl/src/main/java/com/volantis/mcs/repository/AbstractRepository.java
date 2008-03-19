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
 * 04-Jun-01    Paul            VBM:2001051103 - When terminating call low
 *                              level closeConnection method rather than
 *                              disconnect as that causes a concurrent
 *                              modification exception on the collection of
 *                              connections.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 26-Sep-01    Allan           VBM:2001091104 - Javadoc
 * 17-Oct-01    Paul            VBM:2001101701 - Removed throws declaration
 *                              from getRepositoryAccessorManager and
 *                              createRepositoryAccessorManager as they are
 *                              never thrown.
 * 27-Nov-01    Paul            VBM:2001112205 - Restructured to make sure that
 *                              the openConnection and closeConnection methods
 *                              were not called while holding any locks as this
 *                              caused a dead lock with the ConnectionPool
 *                              used by the JDBC sub class.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 05-Apr-02    Mat             VBM:2002022009 - Added multipleRepositories
 *                              flag.
 * 29-May-02    Paul            VBM:2002050301 - Removed multipleRepositories
 *                              flag as it is no longer needed.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 12-May-03    Allan           VBM:2003051303 - Modified connect(),
 *                              disconnect() and terminate() to not do anything
 *                              with the now moved connections list.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The AbstractRepository class encapsulates differences between different
 * repository implementations. Specific repositories inherit from this class
 * and implement particular repository technology. For example, one such
 * repository is based on an Oracle 8 RDBMS.
 *
 * @mock.generate
 */
public abstract class AbstractRepository
  implements Repository {

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractRepository.class);

  /**
   * The repository version number. This is established during the connect()
   * operation, when the repository is opened. The repository must hold its own
   * version number internally.
   */
  private String version;

  /**
   * True if the repository is currently in the processing of terminating
   * all the connections.
   * <p>
   * This flag is protected by the connections lock
   * and must only be accessed while holding that lock.
   * </p>
   */
  protected boolean terminating;

  /**
   * True if the repository has terminated all the connections.
   * <p>
   * This flag is protected by the connections lock
   * and must only be accessed while holding that lock.
   * </p>
   */
  protected boolean terminated;

  /**
   * Check the state of the terminating and terminated flags.
   * <p>
   * This method should only be called while holding the connections lock.
   * </p>
   */
  protected void checkStatus ()
    throws RepositoryException {

    // Check to see whether this Repository is in the process of terminating,
    // or has been terminated.
    if (terminating) {
        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "repository-terminating"));
    } else if (terminated) {
        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "repository-terminated"));
    }
  }

  public RepositoryConnection connect ()
    throws RepositoryException {
        checkStatus();
        return openConnection();
  }

  public void disconnect (RepositoryConnection connection)
    throws RepositoryException {
        checkStatus();
        closeConnection(connection);
  }

  public void terminate ()
    throws RepositoryException {
        checkStatus();
        terminated = true;
    }

  /**
   * Open a new connection to the repository. This method is for use only
   * within Mariner's repository support. Applications must use the connect
   * method.
   * <p>
   * This method is called without holding any locks as it could be a source
   * of dead locks if this method waits while holding another lock. Great
   * care should be taken when writing this method.
   * </p>
   * @return a connection to the repository
   * @throws RepositoryException an exception caused during access to the
   * repository
   */
  protected abstract RepositoryConnection openConnection ()
    throws RepositoryException;

  /**
   * Close a connection to the repository. This method is for use only within
   * Mariner's repository support. Applications must use the disconnect method.
   * <p>
   * This method is called without holding any locks as it could be a source
   * of dead locks if this method waits while holding another lock. Great
   * care should be taken when writing this method.
   * </p>
   * @param connection the repository connection to be closed
   * @exception RepositoryException an exception caused during access to the
   * repository.
   */
  protected abstract void closeConnection (RepositoryConnection connection)
    throws RepositoryException;

  protected void setVersion (String version) {
    this.version = version;
  }

  String getVersion () {
    return version;
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
