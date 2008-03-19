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
 * $Header: /src/voyager/com/volantis/mcs/utilities/SimpleAttributeContainer.java,v 1.11 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Nov-00    Paul            Created.
 * 27-Jul-01    Paul            VBM:2001072603 - Cleaned up.
 * 17-Oct-01    Paul            VBM:2001101701 - Changed to extend a Hashtable
 *                              rather than encapsulate one.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * A <code>SimpleAttributeContainer</code> is simply a <code>Hashtable</code>
 * wrapped with an <code>AttributeContainer</code> interface. It does not
 * record whether or not the value of the attribute has changed or not so
 * <code>isAttributeDirty</code> always returns false.
 * @see #isAttributeDirty(java.lang.String)
 *
 * @mock.generate 
 */
public class SimpleAttributeContainer
  extends Hashtable
  implements AttributeContainer {

  //private Map attributes = new Hashtable ();

  /**
   * Construct a <code>SimpleAttributeContainer</code> with no contents.
   */
  public SimpleAttributeContainer () {
  }

  // --------------------------------------------------------------------------
  // Implementation of AttributeContainer interface.
  // --------------------------------------------------------------------------

  public void setAttribute (String name, Object value) {
    if (value == null) {
      //attributes.remove (name);
      remove (name);
    }
    else {
      //attributes.put (name, value);
      put (name, value);
    }
  }

  public Object getAttribute (String name) {
    //return attributes.get (name);
    return get (name);
  }

  public Iterator attributeNames () {
    //Iterator unsafe = attributes.keySet ().iterator ();
    Iterator unsafe = keySet ().iterator ();

    // Return an iterator which does not allow removals.
    return (unsafe);
  }

  /**
   * Because a <code>SimpleAttributeContainer</code> is a simple wrapper around
   * a hash table it does not know remember whether an attribute has changed so
   * it always returns false.
   * @return false
   */
  public boolean isAttributeDirty (String name) {
    return false;
  }

  // --------------------------------------------------------------------------
  // End of implementation of AttributeContainer interface.
  // --------------------------------------------------------------------------
}

/*
  class SafeIterator
  implements Iterator {

  private Iterator unsafe;

  public SafeIterator (Iterator unsafe) {
  this.unsafe = unsafe;
  }

  public boolean hasNext () {
  return unsafe.hasNext ();
  }

  public Object next () {
  return unsafe.next ();
  }

  public void remove () {
  String message = "Iterator cannot remove attribute from AttributeContainer";
  throw new UnsupportedOperationException (message);
  }
  }
*/

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

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 29-Apr-05	7946/2	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
