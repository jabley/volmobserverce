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
 * $Header: /src/voyager/com/volantis/mcs/repository/RepositoryEnumeration.java,v 1.6 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Apr-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

/**
 * This interface should be implemented by those classes which represent a
 * set of results from a database query. It is very similar to the standard
 * java.util.Enumeration and java.util.Iterator class except that the methods 
 * can all throw RepositoryExceptions and it has an extra method, close, which
 * must be called when the results have been read.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public interface RepositoryEnumeration {

  /**
   * Volantis copyright.
   */ 
  public static String mark = "(c) Volantis Systems Ltd 2001.";


  /**
   * Test for more objects in this <code>RepositoryEnumeration</code>.
   * @return true if there are more objects in this 
   * <code>RepositoryEnumeration</code>; false otherwise.
   * @throws RepositoryException an exception caused during access to the 
   * repository
   */
  public boolean hasNext ()
    throws RepositoryException;

  /**
   * Retrieve the next object in this <code>RepositoryEnumeration</code>.
   * @return the next object in this <code>RepositoryEnumeration</code>.
   * @throws RepositoryException an exception caused during access to the 
   * repository
   */
  public Object next ()
    throws RepositoryException;

  /**
   * Close this <code>RepositoryEnumeration</code> and clean up associated 
   * repository resources that are no longer required.
   * 
   * @throws RepositoryException an exception caused during access to the 
   * repository
   */
  public void close ()
    throws RepositoryException;
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
