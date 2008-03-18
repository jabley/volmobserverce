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
 * $Header: /src/voyager/com/volantis/mcs/repository/Repository.java,v 1.10 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Apr-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 09-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 05-Apr-02    Mat             VBM:2002022009 - Added multipleRepositories
 *                              flag.
 * 29-May-02    Paul            VBM:2002050301 - Removed multipleRepositories
 *                              methods as they are no longer needed.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

/**
 * The Repository interface defines the set of operations that can be
 * performed against any Volantis repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate
 */
public interface Repository {

    /**
     * Create a connection to the repository. If the repository is a
     * database, for example, a database connection is created. If the repository
     * is file based, the appropriate files are opened. By default, connections
     * with support for operation sets DISABLED by default. This is equivalent to
     * autocommit being enabled by default for JDBC databases.
     *
     * @return The RepositoryConnection.
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public RepositoryConnection connect()
            throws RepositoryException;

    /**
     * Disconnect a connection to the repository. If the
     * repository is a database, for example, the database connection is closed.
     * If the repository is file based, the appropriate files are closed.
     *
     * @param connection The RepositoryConnection to close.
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void disconnect(RepositoryConnection connection)
            throws RepositoryException;

    /**
     * Terminate all connections to repository. If the
     * repository is a database, for example, the database connections are
     * closed. If the repository is file based, the appropriate files are closed.
     * After calling this all other methods will fail.
     *
     * @throws RepositoryException An exception caused during access to the
     *                             repository
     */
    public void terminate()
            throws RepositoryException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
