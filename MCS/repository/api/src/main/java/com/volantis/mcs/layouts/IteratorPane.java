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
 * $Header: /src/voyager/com/volantis/mcs/layouts/IteratorPane.java,v 1.18 2002/03/18 12:41:16 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history and
 *                              sorted out the copyright.
 * 09-Jul-01    Paul            VBM:2001062810 - Made getContentBuffer public.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

/**
 * A Pane that can hold multiple content elements. In contrast with a
 * normal Pane, an IteratorPane holds its content as a set of elements.
 * The parent format can control the output of these elements individually
 * via the writeElement method, of can write the entire content in one
 * go with writeOutput() as usual. IteratorPanes allow support where the
 * exact number of output elements is not known prior to run time but where
 * formatting is required on each element.
 *
 * @mock.generate base="Pane"
 */
public abstract class IteratorPane
  extends Pane {

  /**
   * Create a new iterator pane
   * @param canvasLayout The Layout to which this pane belongs
   */
  public IteratorPane (CanvasLayout canvasLayout) {
    super (canvasLayout);
  }

    // NOTE: no explicit validation required as this has exactly the same
    // fields as the parent class Pane.

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
