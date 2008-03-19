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
 * $Header: /src/voyager/com/volantis/mcs/repository/jdbc/PostgreSQLRepository.java,v 1.11 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 10-Aug-01    Paul            VBM:2001071607 - Cleaned up.
 * 05-Oct-01    Paul            VBM:2001092801 - Updated createJDBCRepository
 *                              method as getPort now returns an int not a
 *                              String.
 * 15-Oct-01    Paul            VBM:2001101202 - Removed
 *                              createRepositoryAccessorManager method.
 * 07-Nov-01    Mat             VBM:2001110701 - Add ability to get the
 *                              datasource from the application server
 * 08-Nov-01    Paul            VBM:2001110701 - Modified to use the supplied
 *                              DataSource, if any, in preference to creating
 *                              our own, also removed all references to
 *                              Volantis and AppServerInterfaceManager.
 * 20-Dec-01    Mat             VBM:2001122001 - Changed to use ceil instead
 *                              of ceiling
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;

/**
 * A PostgreSQL specialization of JDBCRepository.
 */
class PostgreSQLRepository
        extends JDBCRepositoryImpl {

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    protected PostgreSQLRepository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);

        sqlKeyTable.put("ceiling", "ceil");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jun-03	316/4	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
