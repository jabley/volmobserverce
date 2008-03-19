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
 * $Header: /src/voyager/com/volantis/mcs/repository/jdbc/Oracle8Repository.java,v 1.15 2002/07/24 16:54:38 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 04-Jun-01    Paul            VBM:2001051103 - Override the
 *                              selectUniqueValues method to use the Oracle
 *                              "select unique" mechanism.
 * 09-Aug-01    Kula            VBM:2001071607 - MSSQL Support added
 * 10-Aug-01    Paul            VBM:2001071607 - Changed the default keywords
 *                              to use Oracle specific ones and removed the
 *                              selectUniqueValues method.
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
 * 24-Jul-02    Byron           VBM:2002052706 - Added attribute readCommitted
 *                              and methods setIsolationLevelReadCommitted() and
 *                              isIsolationLevelReadCommitted(). Changed class
 *                              to public access (from package access)
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;

/**
 * An Oracle specialization of JDBCRepository.
 */
class Oracle8Repository
        extends JDBCRepositoryImpl {

    /**
     * Default isolation level for Oracle 8 is not READ_COMMITTED
     */
    private boolean readCommitted = false;

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    public Oracle8Repository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);

        sqlKeyTable.put("ceiling", "ceil");
        sqlKeyTable.put("distinct", "unique");
    }

    /**
     * Set the isolation level to be read committed or false
     *
     * @param value if true isolation level will be READ_COMMITTED otherwise
     *              it will be the default value.
     */
    public void setIsolationLevelReadCommitted(boolean value) {
        readCommitted = value;
    }

    /**
     * Get the isolation level
     *
     * @return true if the isolation level will be READ_COMMITTED otherwise false
     */
    public boolean isIsolationLevelReadCommitted() {
        return readCommitted;
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

 04-Jun-04	4511/3	tom	VBM:2004052005 added support for short column names

 17-May-04	3649/1	mat	VBM:2004031910 Add short tablename support

 08-Apr-04	3653/3	mat	VBM:2004031910 Change accessors to support resolving the tablename from the repository

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 12-Jun-03	316/2	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
