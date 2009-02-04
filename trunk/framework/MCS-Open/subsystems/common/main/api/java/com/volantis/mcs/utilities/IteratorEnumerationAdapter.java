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
 * $Header: /src/voyager/com/volantis/mcs/utilities/IteratorEnumerationAdapter.java,v 1.2 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to wrap an Iterator
 *                              in an Enumeration for MarinerURL.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * This class adapts an Iterator so that it looks like an Enumeration.
 */
public class IteratorEnumerationAdapter
  implements Enumeration {

  /**
   * The Iterator which is being adapted to look like an Enumeration.
   */
  private Iterator iterator;

  /**
   * Create a new <code>IteratorEnumerationAdapter</code>.
   * @param iterator The Iterator to adapt.
   */
  public IteratorEnumerationAdapter (Iterator iterator) {
    this.iterator = iterator;
  }

  // Javadoc inherited from super class.
  public boolean hasMoreElements () {
    return iterator.hasNext ();
  }

  // Javadoc inherited from super class.
  public Object nextElement () {
    return iterator.next ();
  }
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
