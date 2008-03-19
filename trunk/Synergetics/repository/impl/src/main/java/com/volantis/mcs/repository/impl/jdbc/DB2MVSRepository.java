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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.AlternateNames;

/**
 * A DB2 specialization of JDBCRepository.
 */
class DB2MVSRepository
        extends JDBCRepositoryImpl {

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    protected DB2MVSRepository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);
    }

    // Javadoc inherited.
    public String getAppropriateName(AlternateNames alternateNames) {
        // DB2 7.1 on MVS only supports 18 character table names
        // So hardcode the use of short schema names.
        return alternateNames.getShortName();
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

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 04-Jun-04	4511/10	tom	VBM:2004052005 added support for short column names

 04-May-04	4023/1	ianw	VBM:2004032302 Added support for short length tables

 22-Mar-04	3486/1	ianw	VBM:2004031909 Added support for 18 character table names

 ===========================================================================
*/
