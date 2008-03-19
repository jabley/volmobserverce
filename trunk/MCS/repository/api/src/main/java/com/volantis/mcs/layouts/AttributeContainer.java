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
 * $Header: /src/voyager/com/volantis/mcs/utilities/AttributeContainer.java,v 1.5 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Nov-00    Paul            Created.
 * 27-Jul-01    Paul            VBM:2001072603 - Cleaned up.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import java.util.Iterator;

/**
 * This interface defines the methods that should be implemented by those
 * classes which have attributes; (key, value) pairs which need to be modified
 * through a graphical user interface.
 */
public interface AttributeContainer {

  /**
   * Set the value of the attribute.
   * @param name The name of the attribute
   * @param value The new value of the attribute
   */
  public void setAttribute (String name, Object value);

  /**
   * Retrieve the value of the attribute.
   * @param name The name of the attribute
   * @return The current value of the attribute
   */
  public Object getAttribute (String name);

  /**
   * Return an iterator over the attribute names.
   */
  public Iterator attributeNames ();

  /**
   * Check whether the attribute has changed or not.
   * @param name The name of the attribute
   * @return true if the container knows that the attribute has changed
   * and false otherwise
   */
  public boolean isAttributeDirty (String name);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/2	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
