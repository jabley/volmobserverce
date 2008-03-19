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
 * $Header: /src/voyager/com/volantis/mcs/repository/RepositoryConnection.java,v 1.12 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Apr-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 10-Jul-01    Paul            VBM:2001050209 - Cleaned up.
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 28-Jan-02    Payal           VBM:2002012305 - Modified beingOperationSet(),
 *                              endOperationSet() and abortOperationSet() to
 *                              return a boolean value.
 * 29-Jan-02    Payal           VBM:2002012305 - Added javadoc return statement
 *                              to beingOperationSet(), endOperationSet() and
 *                              abortOperationSet() .Added javadoc in the class
 *                              description javadoc to show an example of how
 *                              to use the operation set code.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 02-Apr-02    Mat             VBM:2002022009 - Added cacheObject() and
 *                              cacheRetrievedObject() plus setter methods.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

/**
 * The RepositoryConnection interface defines the set of operations that can
 * be performed against any Volantis repository connection.
 * <pre>
 *  boolean abortOperationSet = false;
 *  try {
 *  abortOperationSet = connection.beginOperationSet ();
 *  <code which operates on the data base>
 *  abortOperationSet = (!connection.endOperationSet ());
 *  }
 *  <catch block>
 *  } finally {
 * 	if (abortOperationSet) {
 *
 *  try {
 *    connection.abortOperationSet ();
 *  } catch (RepositoryException re) {
 *    logger.error("localized-message", re.getLocalizedMessage(), re);
 *  }
 * }
 * </pre>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate
 */
public interface RepositoryConnection {

    /**
     * Mark the start of a set of repository operations.
     * Repository operations are often grouped, especially where updates are
     * concerned. Frequently, it is important that a group of updates are
     * applied together or not at all. Repositories implemented in databases
     * support the grouping of operations in this way to improve the integrity
     * of the stored data.
     *
     * startOperationSet() marks the beginning of a group of operations that
     * must be processed together in this way.
     *
     * Where a repository implementation does not support grouping of operations,
     * this method has no effect.
     *
     * @return true if successful else throws an exception.
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public boolean beginOperationSet()
            throws RepositoryException;

    /**
     * Mark the successful end of a set of repository operations.
     * Repository operations are often grouped, especially where updates are
     * concerned. Frequently, it is important that a group of updates are
     * applied together or not at all. Repositories implemented in databases
     * support the grouping of operations in this way to improve the integrity
     * of the stored data.
     *
     * endOperationSet() marks the end of a group of operations that must be
     * processed together in this way. It marks the previous operations as
     * being successful and confirms the changes.
     *
     * Where a repository implementation does not support grouping of operations,
     * this method has no effect.
     *
     * @return true if successful else throws an exception.
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public boolean endOperationSet()
            throws RepositoryException;

    /**
     * Abort a set of repository operations.
     * Repository operations are often grouped, especially where updates are
     * concerned. Frequently, it is important that a group of updates are
     * applied together or not at all. Repositories implemented in databases
     * support the grouping of operations in this way to improve the integrity
     * of the stored data.
     *
     * abortOperationSet() marks the end of a group of operations that must be
     * processed together in this way. It marks the previous operations as
     * being unsuccessful and backs out the changes.
     *
     * Where a repository implementation does not support grouping of operations,
     * this method has no effect.
     *
     * @return true if successful else throws an exception.
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public boolean abortOperationSet()
            throws RepositoryException;

    /**
     * Report whether or not this connection supports operation sets.
     *
     * @return True if it does and false if it doesn't.
     */
    public boolean supportsOperationSets();

    /**
     * Disconnect from the repository
     *
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void disconnect()
            throws RepositoryException;

    /**
     * Report whether or not this connection is currently connected to
     * the repository.
     *
     * @return Indicates whether or not the connection is connected to the
     *         repository
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public boolean isConnected()
            throws RepositoryException;

    /**
     * Get the repository to which this connection relates.
     *
     * @return The repository with which this connection is associated
     */
    public Repository getRepository();

    /**
     * Get the under lying connection for this connection.  If there are no more
     * underlying connections, returns this connection.
     *
     * @return The underlying connection, or this
     *         connection if there are no more
     *         underlying connections
     * @throws RepositoryException A problem with the repository
     * @deprecated Do not use.
     */
    public RepositoryConnection getUnderLyingConnection()
            throws RepositoryException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/2	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	2789/1	tony	VBM:2004012601 update localisation services

 05-Feb-04	2694/4	mat	VBM:2004011917 Rework for finding repositories

 26-Jan-04	2694/2	mat	VBM:2004011917 Improve the way repository connections are located

 ===========================================================================
*/
