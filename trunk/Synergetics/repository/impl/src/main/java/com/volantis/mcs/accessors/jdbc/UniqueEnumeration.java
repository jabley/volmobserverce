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
 * $Header: /src/voyager/com/volantis/mcs/accessors/jdbc/UniqueEnumeration.java,v 1.5 2002/03/18 12:41:12 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 26-Jun-01    Paul            VBM:2001051103 - Logged SQLExceptions.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
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

public abstract class UniqueEnumeration
  implements RepositoryEnumeration {

  private static final String mark = "(c) Volantis Systems Ltd 2001.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(UniqueEnumeration.class);

  private boolean gotLast;
  private Object lastValue;

  private boolean gotNext;
  private Object nextValue;

  protected ResultSet rs;
  protected int count;

  public UniqueEnumeration (ResultSet rs) {
    this.rs = rs;
  }

  public boolean hasNext ()
    throws RepositoryException {

    try {
      if (gotNext) {
        return gotNext;
      }

      do {
        if (!rs.next ()) {
          return false;
        }

        nextValue = getData ();

        // If this is not the first value then make sure that the next value
        // to return is not the same as the last value that we returned.
      } while (gotLast
               && (lastValue == null
                   ? nextValue == null
                   : lastValue.equals (nextValue)));

      gotNext = true;

      gotLast = true;
      lastValue = nextValue;

      return true;
    }
    catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
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
    gotNext = false;

    count += 1;

    return getData ();
  }

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
