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
 * $Header: /src/voyager/com/volantis/mcs/accessors/jdbc/SQLResultSetEnumeration.java,v 1.6 2002/03/18 12:41:12 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jan-01    Paul            Created.
 * 26-Jun-01    Paul            VBM:2001051103 - Logged SQLExceptions.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 25-Jan-02    ALlan           VBM:2001121703 - Removed obselete comment and
 *                              added some javadoc.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors.jdbc;

import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class hides the fiddly bits about iterating over SQL ResultSets.
 * The problem is that iteration over ResultSets is done differently than
 * iteration using Iterators.
 * e.g.
 * <CODE>
 *     while (resultSet.next ()) {
 *       String data = resultSet.getString (1);
 *     }
 * </CODE>
 * instead of
 * <CODE>
 *     while (iterator.hasNext ()) {
 *       String data = (String) iterator.next ();
 *     }
 * </CODE>
 *
 * A new method wad added to the ResultSet for JDBC 2.0 which would allow them
 * to be used similarly to Iterators.
 * e.g.
 * <CODE>
 *     while (!resultSet.isAfterLast ()) {
 *       resultSet.next ();
 *       String data = resultSet.getString (1);
 *     }
 * </CODE>
 * Unfortunately not all of the JDBC drivers that we use support JDBC 2.0 so
 * we cannot use that method.
 */
public abstract class SQLResultSetEnumeration
  implements RepositoryEnumeration {

  private static final String mark = "(c) Volantis Systems Ltd 2001.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(SQLResultSetEnumeration.class);

  /**
   * This flag is set after the hasNext method has called ResultSet.next ()
   * and cleared when the next method is called. It is used by hasNext to
   * determine whether or not it needs to call ResultSet.next () again.
   *
   * Note: This flag and the following flag are only needed because the
   * isAfterLast method is not part of JDBC 1.0 and therefore may not be
   * present on some of the drivers we use.
   */
  private boolean calledNext;

  /**
   * This flag is set by the hasNext method to the result of the call to
   * ResultSet.next () and is returned on all subsequent calls until next ()
   * is called again.
   */
  private boolean hasNext;

  protected ResultSet rs;
  protected int count;

  public SQLResultSetEnumeration (ResultSet rs) {
    this.rs = rs;
  }

  public boolean hasNext ()
    throws RepositoryException {

    try {
      // Can't use isAfterLast method as it may not be implemented in some
      // of the drivers that we use.
      //return !rs.isAfterLast ();

      if (!calledNext) {
        hasNext = rs.next ();
        calledNext = true;
      }

      return hasNext;
    }
    catch (SQLException sqle) {
      try { 
          rs.getStatement().close();
      }
      catch (SQLException sqle2) {
          logger.error("sql-exception", sqle2);
      }
      logger.error("sql-exception", sqle);;
      throw new JDBCRepositoryException (sqle);
    }
  }

  public Object next ()
    throws RepositoryException {

    // Call hasNext () just in case the caller didn't call it. It does nothing
    // if it has already been called.
    if (!hasNext ()) {
      return null;
    }

    // Next time hasNext is called it has to call ResultSet.next
    calledNext = false;

    count += 1;

    return getData ();
  }

  /** 
   * Use the data from the current row to get the required object.
   * @returns an object derived from the data in the current row
   * @throws RespositoryException if there was a problem accessing the
   * repository.
   */  
  protected abstract Object getData ()
    throws RepositoryException;

  public void close ()
    throws RepositoryException {

    try {
      rs.getStatement ().close ();
    }
    catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException (sqle);
    }
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
