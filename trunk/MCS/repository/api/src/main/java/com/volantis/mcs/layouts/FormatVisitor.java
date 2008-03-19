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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatVisitor.java,v 1.9 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 * ----------------------------------------------------------------------------
 * Date         Who             Description
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Created
 * 09-Jul-01    Paul            VBM:2001062810 - Changed the visit methods
 *                              to return a boolean.
 * 29-Oct-01    Paul            VBM:2001102901 - Added an extra parameter to
 *                              all the visitor methods to allow
 *                              implementations of this interface to be more
 *                              easily shared.
 * 02-Nov-01    Paul            VBM:2001102403 - Added support for regions.
 * 24-Jan-02    Steve			VBM:2002011412 - New visit method for replicas
 * 13-Feb-02    Steve           VBM:2001101803 - New visit method for form fragments
 * 18-Oct-02    Chris W         VBM:2002110511 - New visit method for spatial
 *                              and temporal format iterators.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() methods.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

/**
 * This interface defines methods which are called when traversing a format
 * tree. It is completely type safe as there is a method for each type of
 * format in the tree so no casting is required.
 */
public interface FormatVisitor {

  /**
   * This method is called if while traversing the tree a ColumnIteratorPane
   * is found.
   * @param pane The ColumnIteratorPane which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (ColumnIteratorPane pane, Object object) 
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a DissectingPane
   * is found.
   * @param pane The DissectingPane which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (DissectingPane pane, Object object)
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a Form
   * is found.
   * @param form The Form which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Form form, Object object) 
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a Fragment
   * is found.
   * @param fragment The Fragment which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Fragment fragment, Object object) 
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a FormFragment
   * is found.
   * @param fragment The FormFragment which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (FormFragment fragment, Object object) 
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a Grid
   * is found.
   * @param grid The Grid which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Grid grid, Object object) 
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a Pane
   * is found.
   * @param pane The Pane which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Pane pane, Object object)
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a RowIteratorPane
   * is found.
   * @param pane The RowIteratorPane which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (RowIteratorPane pane, Object object) 
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a Segment
   * is found.
   * @param pane The Segment which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Segment segment, Object object)
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a SegmentGrid
   * is found.
   * @param grid The SegmentGrid which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (SegmentGrid grid, Object object) 
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a SpatialFormatIterator
   * is found.
   * @param spatial The SpatialFormatIterator which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (SpatialFormatIterator spatial, Object object)
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a TemporalFormatIterator
   * is found.
   * @param temporal The TemporalFormatIterator which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (TemporalFormatIterator temporal, Object object)
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a Region
   * is found.
   * @param region The Region which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Region region, Object object)
          throws FormatVisitorException;

  /**
   * This method is called if while traversing the tree a Replica
   * is found.
   * @param replica The Replica which is being visited.
   * @param object The object which was passed into the {@link Format#visit}
   * method.
   * @return True if you want to terminate the traversal and false otherwise.
   */
  public boolean visit (Replica replica, Object object)
          throws FormatVisitorException;
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

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
