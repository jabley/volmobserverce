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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-03    Allan           VBM:2003051303 - Created. A stub for
 *                              java.sql.DataSource.
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools.stubs;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

// javadoc inherited

public class DataSourceStub implements DataSource {

    // javadoc inherited
    public boolean isWrapperFor(Class iface) throws SQLException {
        return false;
    }

    // javadoc inherited
    public Object unwrap(Class iface) throws SQLException {
        return null;
    }

    // javadoc inherited
    public Connection getConnection() throws SQLException {
        return ConnectionStub.createConnectionStub(null);
    }

    // javadoc inherited    
    public Connection getConnection(String s, String s1) throws SQLException {
        return ConnectionStub.createConnectionStub(null);
    }

    // javadoc inherited
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    // javadoc inherited
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    // javadoc inherited
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
    }

    // javadoc inherited
    public void setLoginTimeout(int i) throws SQLException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jun-03	15/1	allan	VBM:2003060907 Move some more testtools to here from MCS

 ===========================================================================
*/
