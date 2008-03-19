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
 * $Header: /src/voyager/com/volantis/mcs/layouts/RuntimeLayoutFactory.java,v 1.17 2002/12/09 15:48:39 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Sep-00    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Added Form format and renamed
 *                              GridFormat to Grid.
 * 27-Jul-01    Paul            VBM:2001072603 - Added comment.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout.
 * 02-Nov-01    Paul            VBM:2001102403 - Added support for regions.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 25-Jan-02    Steve			VBM:2002011412 - Creates a replica when requested
 * 13-Feb-02    Steve           VBM:2001101803 - Creates a FormFragment when
 *                              requested.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 28-Oct-02    Chris W         VBM:2002111105 - Creates SpatialFormatIterators
 *                              and TemporalFormatIterators when requested.                               
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 *
 *
 */
public class RuntimeLayoutFactory
  implements LayoutFactory {

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    RuntimeLayoutFactory.class);

  private Layout layout;
  private boolean isCanvas;

  public RuntimeLayoutFactory () {
  }

  public Layout createDeviceLayout (LayoutType type)
    throws LayoutException {

    if (type == null) {
      type = LayoutType.CANVAS;
    }

      if (type == LayoutType.CANVAS) {
          layout = new CanvasLayout();
      } else if (type == LayoutType.MONTAGE) {
          layout = new MontageLayout();
      } else {
          throw new IllegalArgumentException("Unknown type " + type);
      }

    isCanvas = type == LayoutType.CANVAS;

    return layout;
  }

  public Format createFormat (FormatType type, Format parent, int index)
    throws LayoutException {

    Format format = null;

    if (parent != null && parent.getNumChildren () == 0) {
        throw new LayoutException(EXCEPTION_LOCALIZER.format(
                "format-child-not-allowed", parent.getFormatType()));
    }

    if (isCanvas) {
        CanvasLayout canvasLayout = (CanvasLayout) layout;
      if (FormatType.GRID.equals (type)) {
        format = new Grid (canvasLayout);
      }
      else if (FormatType.PANE.equals (type)) {
        format = new Pane (canvasLayout);
      }
      else if (FormatType.ROW_ITERATOR_PANE.equals (type)) {
        format = new RowIteratorPane (canvasLayout);
      }
      else if (FormatType.COLUMN_ITERATOR_PANE.equals (type)) {
        format = new ColumnIteratorPane (canvasLayout);
      }
      else if (FormatType.DISSECTING_PANE.equals (type)) {
        format = new DissectingPane (canvasLayout);
      }
      else if (FormatType.FRAGMENT.equals (type)) {
        format = new Fragment (canvasLayout);
      }
      else if (FormatType.FORM.equals (type)) {
        format = new Form (canvasLayout);
      }
      else if (FormatType.FORM_FRAGMENT.equals (type)) {
        format = new FormFragment (canvasLayout);
      }
      else if (FormatType.REGION.equals (type)) {
        format = new Region (canvasLayout);
      }
      else if (FormatType.REPLICA.equals (type)) {
        format = new Replica (canvasLayout);
      }
      else if (FormatType.SPATIAL_FORMAT_ITERATOR.equals (type)) {
        format = new SpatialFormatIterator (canvasLayout);
      }
      else if (FormatType.TEMPORAL_FORMAT_ITERATOR.equals (type)) {
        format = new TemporalFormatIterator (canvasLayout);
      }


      // Temporary, if Layout is a canvas and we are reading some montage
      // formats then assume that it is a montage.
      else if (FormatType.SEGMENT_GRID.equals (type)) {
          throw new IllegalStateException("Not allowed segment grids inside canvas layouts");
//        isCanvas = false;
//        layout.setType (Layout.MONTAGE_TYPE);
//        format = new SegmentGrid (layout);
      }
      else if (FormatType.SEGMENT.equals (type)) {
          throw new IllegalStateException("Not allowed segments inside canvas layouts");
//        isCanvas = false;
//        layout.setType (Layout.MONTAGE_TYPE);
//        format = new Segment (layout);
      }
    }
    else {
        MontageLayout montageLayout = (MontageLayout) layout;
        if (FormatType.SEGMENT_GRID.equals (type)) {
        format = new SegmentGrid (montageLayout);
      }
      else if (FormatType.SEGMENT.equals (type)) {
        format = new Segment (montageLayout);
      }
    }

    if (format == null) {
      throw new LayoutException (EXCEPTION_LOCALIZER.format("format-unknown-type", new Object[]{layout.getType(), type}));
    }

    // Keep track of the total number of formats in the Layout.
    int formatInstance = layout.getFormatCount ();
    format.setInstance (formatInstance);
    layout.setFormatCount (formatInstance + 1);

    if (parent != null) {
      parent.setChildAt (format, index);
      format.setParent (parent);
    }

    return format;
  }

  public void formatAttributesHaveBeenSet (Format format)
    throws LayoutException {

    format.attributesHaveBeenSet ();
  }

  public void formatChildrenHaveBeenCreated (Format format)
    throws LayoutException {

    format.childrenHaveBeenCreated ();
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 ===========================================================================
*/
