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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatVisitorStateAdapter.java,v 1.4 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 * ----------------------------------------------------------------------------
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Mar-02    Adrian          VBM:2002020101 - Created to visit Fragment and
 *                              Segment to find and either set or unset the
 *                              DeviceLayout defaultFragment / defaultSegment
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 02-Apr-02    Adrian          VBM:2002020101 - removed redundant condition
 *                              statements in method visit (Segment segment...
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

/**
 * A subclass of FormatVisitor which checks if a <code>Fragment</code> or
 * <code>Segment</code> is the <code>Layout</code> default.  It unsets
 * the default value if the <code>Fragment</code> or <code>Segment</code> is 
 * being removed from the <code>Layout</code>.  It reset the default
 * value if the <code>Fragment</code> or <code>Segment</code> is being re-added
 * to the <code>Layout</code>.
 */
public class FormatVisitorStateAdapter extends FormatVisitorAdapter {

  /**
   * This method is called if while traversing the tree a Fragment
   * is found.  Dependent on the value of object, an attempt is made to either
   * unset or reset the value of the <code>Layout</code> default
   * fragment.
   * @param fragment The Fragment which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Fragment fragment, Object object)
          throws FormatVisitorException {
    if (!(object instanceof Boolean)) {
      throw new IllegalArgumentException("Object should be of type Boolean");
    }

    boolean unset = ((Boolean)object).booleanValue();
    if (unset) {
      if (fragment.checkUnsetDefaultFragment()) {
	return true;
      }
    } else {
      if (fragment.checkResetDefaultFragment()) {
	return true;
      }
    }
    return fragment.visitChildren (this, object);
  }

  /**
   * This method is called if while traversing the tree a Segment
   * is found.  Dependent on the value of object, an attempt is made to either
   * unset or reset the value of the <code>Layout</code> default
   * segment.
   * @param segment The segment which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Segment segment, Object object) {
    if (!(object instanceof Boolean)) {
      throw new IllegalArgumentException("Object should be of type Boolean");
    }

    boolean unset = ((Boolean)object).booleanValue();

    if (unset) {
      return segment.checkUnsetDefaultSegment();
    }
    return segment.checkResetDefaultSegment();
  }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
