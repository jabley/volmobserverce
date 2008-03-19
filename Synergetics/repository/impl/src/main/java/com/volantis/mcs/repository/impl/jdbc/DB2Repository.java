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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Aug-01    Doug            VBM:2001080809 - Created file to add support
 *                              for DB2
 * 10-Aug-01    Paul            VBM:2001071607 - Removed selectUniqueValues
 *                              and removed setting of vendor specific keywords
 *                              as DB2 supports the defaults.
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
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;

/**
 * A DB2 specialization of JDBCRepository.
 *
 * @todo remove as this is no longer needed.
 */
class DB2Repository
        extends JDBCRepositoryImpl {

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    protected DB2Repository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);
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

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 09-Sep-03	1377/1	philws	VBM:2003082803 Fix DB2 JDBC driver URL generation

 23-Jun-03	440/1	chrisw	VBM:2003061802 perl script to convert our sql92 scripts to db2 format

 12-Jun-03	316/2	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
