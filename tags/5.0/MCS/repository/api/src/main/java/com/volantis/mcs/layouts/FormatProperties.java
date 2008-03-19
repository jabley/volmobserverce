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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatProperties.java,v 1.23 2003/01/03 16:33:18 philws Exp $
 * ----------------------------------------------------------------------------
 * Copyright (c) 2000 by Volantis Systems Ltd. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-00    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 09-Jul-01    Paul            VBM:2001062810 - Cleaned up.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 10-Aug-01    Paul            VBM:2001072505 - Added support for height unit
 *                              format attributes.
 * 01-Oct-01    Doug            VBM:2001092501 - Added default value for 
 *                              the BACKGROUND_COMPONENT_TYPE_ATTRIBUTE drop
 *                              down list.
 * 01-Nov-01    Pether          VBM:2001102602 - Added default value for
 *                              the WIDTH_ATTRIBUTE.
 * 04-Mar-02    Allan           VBM:2002030102 - Removed default value settings
 *                              for borderWidth, horizontalAlign and vertAlign.
 *                              Note: this class should not exist since the
 *                              properties it contains are the defaults for
 *                              a format - they should be in Format and 
 *                              subclasses where necessary.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 26-Apr-02    Paul            VBM:2002042205 - Added default for border
 *                              width back in, the problem that 2002030102
 *                              was fixing has been fixed in a different way.
 * 09-May-02    Payal           VBM:2002041803 - Modified FormatProperties ()
 *                              added  default value for HEIGHT_ATTRIBUTE.
 * 17-May-02    Payal           VBM:2002041803 - Modified FormatProperties ()
 *                              removed default value for HEIGHT_ATTRIBUTE.
 * 08-Nov-02    Byron           VBM:2002110515 - Used DEFAULT_CELL_SPACING
 * 03-Jan-03    Phil W-S        VBM:2002122404 - Add the OPTIMIZATION_LEVEL
 *                              attribute default.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;



public class FormatProperties
  extends SimpleAttributeContainer {

  /**
   * Creates a new <code>FormatProperties</code> instance.
   *
   */
  public FormatProperties () {
    setAttribute (FormatConstants.BORDER_WIDTH_ATTRIBUTE,
                  FormatConstants.DEFAULT_BORDER_WIDTH);
    setAttribute (FormatConstants.CELL_PADDING_ATTRIBUTE,
                  FormatConstants.DEFAULT_CELL_PADDING);
    setAttribute (FormatConstants.CELL_SPACING_ATTRIBUTE,
                  FormatConstants.DEFAULT_CELL_SPACING);
    setAttribute (FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE, "0");
    setAttribute (FormatConstants.FRAME_BORDER_ATTRIBUTE, "false");
    setAttribute (FormatConstants.FRAME_SPACING_ATTRIBUTE, "0");
    setAttribute (FormatConstants.HEIGHT_UNITS_ATTRIBUTE,
                  FormatConstants.HEIGHT_UNITS_VALUE_PERCENT);
    setAttribute (FormatConstants.MARGIN_HEIGHT_ATTRIBUTE, "0");
    setAttribute (FormatConstants.MARGIN_WIDTH_ATTRIBUTE, "0");
    setAttribute (FormatConstants.NEXT_SHARD_LINK_TEXT_ATTRIBUTE, "Next");
    setAttribute (FormatConstants.PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE, "Previous");
    setAttribute (FormatConstants.RESIZE_ATTRIBUTE, "true");
    setAttribute (FormatConstants.SCROLLING_ATTRIBUTE, "Auto");
    setAttribute (FormatConstants.WIDTH_ATTRIBUTE, "100");
    setAttribute (FormatConstants.WIDTH_UNITS_ATTRIBUTE,
                  FormatConstants.WIDTH_UNITS_VALUE_PERCENT);
    setAttribute (FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
                  FormatConstants.BACKGROUND_COMPONENT_TYPE_IMAGE);
    setAttribute(FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE,
                 FormatConstants.DEFAULT_OPTIMIZATION_LEVEL);
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

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
