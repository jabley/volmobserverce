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
 * $Header: /src/voyager/com/volantis/mcs/accessors/jdbc/JDBCAccessorHelper.java,v 1.37 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- ----------------------------------------------
 * 11-Apr-01    Allan           Created.
 * 04-Jun-01    Paul            VBM:2001051103 - Move selectUniqueValues method
 *                              into the JDBCRepository so it can be overridden
 *                              by database specific implementations.
 * 06-Jun-01    Paul            VBM:2001051103 - Removed duplicated logging.
 * 26-Jun-01    Paul            VBM:2001051103 - Added methods to rename
 *                              a single object, rename multiple objects,
 *                              remove multiple objects, moved some duplicated
 *                              code into a common method, generalised some
 *                              of methods to use Map instead of Hashtable.
 * 29-Jun-01    Paul            VBM:2001062906 - Fix problem with rename.
 * 28-Aug-01    Doug            VBM:2001082806 - Removed single quotes from
 *                              the revision number in method makeInsertString
 *                              as revision number is now a NUMBER datatype in
 *                              database.
 * 10-Oct-01    Allan           VBM:2001100106 - Added a new param to
 *                              makeInsertString() that specifies if the
 *                              revision field should be inserted.
 * 15-Oct-01    Paul            VBM:2001101202 - Removed unused import and
 *                              unused code.
 * 23-Oct-01    Allan           VBM:2001100106 - Added methods for updating
 *                              based on properties: makeUpdateString() and
 *                              update(). Made removeObjects() use
 *                              deleteByName() since that is all it is doing.
 *                              Added a deprecated comment to removeObjects().
 * 24-Oct-01    Paul            VBM:2001092608 - Used the
 *                              getPropertyDescriptors method in AccessorHelper
 *                              instead of doing the introspection directly as
 *                              that removes the 'identity' property which
 *                              is picked up from RepositoryObject and would
 *                              break all the accessors.
 * 17-Nov-01    Allan           VBM:2001102504 - Fixed bug in
 *                              createBeanFromKey() where if the bean did not
 *                              exist in the repository a runtime exception
 *                              was thrown instead of returning null.
 * 21-Nov-01    Payal           VM:2001111202 - Modified deleteByName(),
 *                              createBeansFromKey(),
 *                              enumerateObjects(),moveAsset(),
 *                              renameObjects(),moveAsset()
 *                              to append VM to the front of the table
 *                              names it generates from class names
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 11-Jan-02    Paul            VBM:2002010403 - Moved REVISION_FIELD from
 *                              AccessorHelper.
 * 27-Jan-02    Allan           VBM:2002012801 - Added updateByName().
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 05-Apr-02    Mat             VBM:2002022009 - Added
 *                              getCorrectRepositoryConnection(), also changed
 *                              any methods getting a JDBCRepositoryConnectionImpl
 *                              to call the new method.
 * 30-Apr-02    Mat             VBM:2002040814 - Removed
 *                              getCorrectRepositoryConnection(), to reflect
 *                              the new runtime repository.
 * 03-Jun-02    Allan           VBM:2002060302 - Modified makeUpdateString() to
 *                              use the source for the set part and change
 *                              update(conn, object, object) to use the source
 *                              for the table name.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors.jdbc;

import com.volantis.mcs.accessors.AccessorHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Contains general methods for use by JDBC Accessor classes.
 */
public class JDBCAccessorHelper
  extends AccessorHelper {

    /** Holds prefix for table names  */
  public static final String TABLE_NAME_PREFIX = "VM";

  /**
   * Insert a String into prepared statement. This method will insert
   * a String into a prepared statement, or insert null on null or zero
   * length String.
   *
   * @param pstmt Prepared statement
   * @param index Column index
   * @param value String to insert
   */
  public static void setStringValue(PreparedStatement pstmt, int index,
          String value) throws SQLException {

      if (value == null || value.length() == 0) {
          pstmt.setNull(index, java.sql.Types.VARCHAR);
      } else {
          pstmt.setString(index, value);
      }

  }

  /**
   * Quote a string for use in an SQL statement (i.e. surround a string
   * with ''). This method works with strings that contain ' characters.
   * <p>
   * NOTE: this translates empty strings to nulls as part of support for
   * Sybase (apparently). Using this method for writing fields that are
   * mandatory in the lpdm schema means that the matching
   * {@link #unquoteMandatory} is required to be used on read otherwise we
   * get JiBX/schema errors.
   *
   * @param s String to quote
   * @return quoted version of s
   */
  public static String quoteValue(String s) {
    if (s == null || s.length()==0) {
      return null;
    }


    StringBuffer filtered = new StringBuffer(s.length ());
    char c;

    filtered.append ("'");
    for(int i = 0; i < s.length(); i++) {
      c = s.charAt (i);
      if(c == '\'') {
        filtered.append("''");
      } else {
        filtered.append (c);
      }
    }
    filtered.append("'");

    return filtered.toString();
  }

    /**
     * "Unquote" a string returned from a SQL statement for use with a
     * mandatory field.
     * <p>
     * This implements the inverse of {@link #quoteValue} and
     * {@link #setStringValue} above, i.e. if a null String is read it
     * translates it back into an enpty string.
     * <p>
     * This is required for objects which have fields which are mandatory, for
     * example text assets have a value which is mandatory according to the
     * lpdm schema but can be empty.
     *
     * @param s
     * @return
     */
    public static String unquoteMandatory(String s) {
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10449/1	geoff	VBM:2005110803 MCS35: Export issue with textAsset contradicts import & GUI and throws exception

 24-May-05	7890/2	pduffin	VBM:2005042705 Committing extensive restructuring changes

 28-Apr-05	7908/1	pduffin	VBM:2005042712 Removing Revision object, added UNKNOWN_REVISION constant to JDBCAccessorHelper. This will be removed when revisions are removed from the database tables

 27-Apr-05	7896/1	pduffin	VBM:2005042709 Removing PolicyPreference and all related classes

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 23-Feb-05	7101/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	7091/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	6905/2	allan	VBM:2005020703 Added support for Sybase

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 30-Sep-04	4511/7	tom	VBM:2004052005 Added short column support for new table columns and cache accessors

 04-Jun-04	4511/5	tom	VBM:2004052005 added support for short column names

 17-May-04	3649/1	mat	VBM:2004031910 Add short tablename support

 19-Feb-04	2789/10	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/8	tony	VBM:2004012601 update localisation services

 16-Feb-04	2789/6	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 12-Feb-04	3009/1	mat	VBM:2004021210 fix up identities in Export

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 04-Jan-04	2360/1	andy	VBM:2003121710 added PROJECT column to all tables

 ===========================================================================
*/
