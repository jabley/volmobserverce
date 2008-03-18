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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Callback interface used by JdbcTemplate's query methods. Implementations of
 * this interface perform the actual work of extracting results, but don't need
 * to worry about exception handling. SQLExceptions will be caught and handled
 * correctly by the JdbcTemplate class.
 * <p>
 * NOTE: this is simpler than the RowCallbackHandler/ResultReader that Spring
 * uses. Probably it would be good to extend this to be more similar to Spring's
 * way of doing things, as that is more capable.
 */
public interface RowProcessor {

    /**
     * Implementations must implement this method to process each row of data
     * in the ResultSet. This method should not call next() on the ResultSet,
     * but extract the current values.
     *
     * @param rs  the ResultSet to process.
     * @return the object that this row was processed into.
     * @throws SQLException if a SQLException is encountered getting column
     *      values (that is, there's no need to catch SQLException).
     */
    Object processRow(ResultSet rs) throws SQLException;
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
