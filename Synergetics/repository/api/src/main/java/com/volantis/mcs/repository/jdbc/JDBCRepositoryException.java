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
 * $Header: /src/voyager/com/volantis/mcs/repository/jdbc/JDBCRepositoryException.java,v 1.12 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Apr-01    Paul            Created.
 * 26-Jun-01    Paul            VBM:2001051103 - Added an extra constructor.
 * 24-Oct-01    Paul            VBM:2001092608 - Added mapping from the SQL
 *                              state INTEGRITY_CONSTRAINT_VIOLATION_NUMBER
 *                              to OBJECT_ALREADY_EXISTS.
 * 13-Dec-01    Allan           VBM:2001112915 - Removed method 
 *                              getLocalizedSpecificMessage() - use parent 
 *                              version instead. If there is an integrity
 *                              contraint violation then set code to this
 *                              in initialize().
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 11-Feb-02    Paul            VBM:2001122105 - Fixed problem with retrieving
 *                              the bundle.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.jdbc;

import com.volantis.mcs.repository.RepositoryException;

import java.sql.SQLException;

/**
 * The JDBCRepositoryException class encapsulates SQL exceptions thrown when
 * there is an error in a JDBC repository operation.
 */

public class JDBCRepositoryException
  extends RepositoryException {

  private static String mark = "(c) Volantis Systems Ltd 2000.";

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message The error message associated with the exception. If
     *                localization is required this message must be
     *                pre-localized.
     */
    public JDBCRepositoryException(String message) {
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
    public JDBCRepositoryException(String message,
                 Exception exception) {
        super(message, exception);
    }

    public JDBCRepositoryException (SQLException sqle) {
        super(sqle);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 09-Oct-03	1524/1	philws	VBM:2003090101 Port of SQLSTATE handling from PROTEUS

 09-Oct-03	1522/1	philws	VBM:2003090101 Ensure that SQLSTATE codes are matched against class codes only

 ===========================================================================
*/
