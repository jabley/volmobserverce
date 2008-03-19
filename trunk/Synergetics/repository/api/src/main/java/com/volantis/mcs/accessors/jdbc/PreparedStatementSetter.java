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
package com.volantis.mcs.accessors.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Callback interface used by the JdbcTemplate class.
 * <p>
 * This interface sets values on a PreparedStatement provided by the
 * JdbcTemplate class. Implementations are responsible for setting any
 * necessary parameters. SQL with placeholders will already have been supplied.
 * <p>
 *  It's easier to use this interface than PreparedStatementCreator, as the
 * JdbcTemplate will create the prepared statement.
 * <p>
 * Implementations do not need to concern themselves with SQLExceptions that
 * may be thrown from operations they attempt. The JdbcTemplate class will
 * catch and handle SQLExceptions appropriately.
 */
public interface PreparedStatementSetter {

    /**
     * Set values on the given PreparedStatement.
     *
     * @param ps PreparedStatement we'll invoke setter methods on.
     * @throws SQLException there is no need to catch SQLExceptions that may be
     * thrown in the implementation of this method. The JdbcTemplate class will
     * handle them.
     */
    void setValues(PreparedStatement ps) throws SQLException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 ===========================================================================
*/
