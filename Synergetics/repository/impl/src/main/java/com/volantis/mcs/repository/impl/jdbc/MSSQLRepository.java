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
 * $Header: /src/voyager/com/volantis/mcs/repository/jdbc/MSSQLRepository.java,v 1.9 2003/02/18 11:25:13 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Aug-01    Paul            VBM:2001071607 - Added this header, cleaned up
 *                              the code a bit, removed setting of the
 *                              distinct keyword as it was the same as the
 *                              default and removed getVendorSpecificSQLKeyWord
 *                              method.
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
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 04-Feb-03    Ian             VBM:2003020413 - Added MS MSSQL driver.
 * 18-Feb-03    Ian             VBM:2003021405 - Implemented VBM 2003020413 for
 *                              Mimas.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;

/**
 * A Microsoft SQL specialization of JDBCRepository.
 */
class MSSQLRepository
        extends JDBCRepositoryImpl {

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    protected MSSQLRepository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);

        sqlKeyTable.put("length", "len");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jun-03	316/2	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
