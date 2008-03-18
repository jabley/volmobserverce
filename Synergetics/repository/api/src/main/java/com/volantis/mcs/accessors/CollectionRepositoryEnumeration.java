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
 * $Header: /src/voyager/com/volantis/mcs/accessors/CollectionRepositoryEnumeration.java,v 1.2 2002/03/18 12:41:12 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Oct-01    Paul            VBM:2001092608 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors;

import com.volantis.mcs.repository.RepositoryEnumeration;

import java.util.Collection;
import java.util.Iterator;

/**
 * This class adapts a Collection into a RepositoryEnumeration.
 */
public class CollectionRepositoryEnumeration
  implements RepositoryEnumeration {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2001.";

  /**
   * The iterator over the collection.
   */
  private Iterator iterator;

  /**
   * Create a new <code>CollectionRepositoryEnumeration</code>.
   * @param collection The collection of objects to wrap.
   */
  public CollectionRepositoryEnumeration (Collection collection) {
    iterator = collection.iterator ();
  }

  // Javadoc inherited from super class.
  public boolean hasNext () {
    return iterator.hasNext ();
  }
  
  // Javadoc inherited from super class.
  public Object next () {
    return iterator.next ();
  }

  // Javadoc inherited from super class.
  public void close () {
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * end:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
