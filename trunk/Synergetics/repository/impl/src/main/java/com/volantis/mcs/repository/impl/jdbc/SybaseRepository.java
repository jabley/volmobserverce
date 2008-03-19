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
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;

/**
 * A Sybase specialization of JDBCRepository.
 */
class SybaseRepository
        extends JDBCRepositoryImpl {

// Sybase doesn't seem to support long columns even if you set the correct connection
// parameters so lets set the chunkSize down to something that works.
private int chunkSize = 255;
    
    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    public SybaseRepository(InternalJDBCRepositoryConfiguration configuration) {
        super(configuration);

        sqlKeyTable.put("length", "len");
    }

        //Javadoc inherited
   public int getChunkSize() {
       return chunkSize;
   }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 23-Mar-05	7081/1	rgreenall	VBM:2005022301 setString method no longer called directly on a JDBC PreparedStatement.

 23-Feb-05	7091/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	6905/1	allan	VBM:2005020703 Added support for Sybase

 ===========================================================================
*/
