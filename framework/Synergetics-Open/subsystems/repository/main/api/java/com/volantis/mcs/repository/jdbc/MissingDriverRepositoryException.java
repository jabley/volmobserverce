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
package com.volantis.mcs.repository.jdbc;

import java.sql.SQLException;

/**
 * An exception indicating a failure to locate the database driver.
 */
public class MissingDriverRepositoryException extends JDBCRepositoryException {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message The error message associated with the exception. If
     *                localization is required this message must be
     *                pre-localized.
     */
    public MissingDriverRepositoryException(String message) {
        super(message);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message   The error message associated with the exception. If
     *                  localization is required this message must be
     *                  pre-localized.
     * @param exception The associated exception
     */
    public MissingDriverRepositoryException(String message, Exception exception) {
        super(message, exception);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param sqle The SQL exception causing this error condition
     */
    public MissingDriverRepositoryException(SQLException sqle) {
        super(sqle);
    }
}
