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
 * $Header: /src/voyager/com/volantis/mcs/layouts/RowIteratorPane.java,v 1.45 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history and
 *                              added visit method.
 * 09-Jul-01    Paul            VBM:2001062810 - Sorted out the background
 *                              image retrieval and changed the visit method
 *                              to return a boolean.
 * 23-Jul-01    Paul            VBM:2001070507 - Renamed setBorder method in
 *                              PaneAttributes to setBorderWidth so that it
 *                              more closely matches its use.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 26-Jul-01    Paul            VBM:2001072301 - Initialise the pane attribute
 *                              in PaneAttributes.
 * 30-Jul-01    Paul            VBM:2001071609 - Trimmed content buffers and
 *                              removed some unnecessary code.
 * 02-Aug-01    Allan           VBM:2001072604 - Replaced openComment() -
 *                              comment - closeComment() with
 *                              doComment(comment).
 * 31-Aug-01    Allan           VBM:2001083121 - Set width and height on
 *                              attributes in writeOutput().
 * 01-Oct-01    Doug            VBM:2001092501 - now use the Format.java
 *                              method getBestBackgroundImage to calculate the
 *                              background image url.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

/** A Pane that can hold multiple content elements. In contrast with a
 * normal Pane, an RowIteratorPane holds its content as a set of elements.
 * These are written out as the rows of a table. RowIteratorPanes allow support
 * where the exact number of output elements is not known prior to run time but
 * where row formatting is required on each element.
 *
 * @mock.generate base="IteratorPane"
 */
public class RowIteratorPane
  extends IteratorPane {

  /**
   * Create a new iterator pane
   * @param canvasLayout The Layout to which this pane belongs
   */
  public RowIteratorPane (CanvasLayout canvasLayout) {
    super (canvasLayout);
  }

  public FormatType getFormatType () {
    return FormatType.ROW_ITERATOR_PANE;
  }

  // Javadoc inherited from super class.
  public boolean visit (FormatVisitor visitor, Object object)
          throws FormatVisitorException {
    return visitor.visit (this, object);
  }

    // NOTE: no explicit validation required as this has exactly the same
    // fields as the grandparent class Pane.

}

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
