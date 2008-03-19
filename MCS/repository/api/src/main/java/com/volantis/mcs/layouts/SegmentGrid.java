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
 * $Header: /src/voyager/com/volantis/mcs/layouts/SegmentGrid.java,v 1.21 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Jan-01    Allan           Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright,
 *                              added visit method and renamed GridFormat to
 *                              Grid.
 * 09-Jul-01    Paul            VBM:2001062810 - Changed the visit method to
 *                              return a boolean.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width and
 *                              height unit format attributes.
 * 26-Jul-01    Paul            VBM:2001071707 - Modified to make it compatible
 *                              with some changes to the SegmentGridAttributes
 *                              class.
 * 30-Jul-01    Paul            VBM:2001071609 - Removed some unnecessary code.
 * 10-Aug-01    Paul            VBM:2001072505 - Change the default setting
 *                              for the height units attribute in row to
 *                              percent.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                               to string.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.model.validation.ValidationContext;

/**
 * A format that provides a segment grid (e.g. frameset)
 *
 * @mock.generate base="AbstractGrid"
 */
public class SegmentGrid
        extends AbstractGrid
        implements BorderColourAttribute, FrameBorderAttribute {

    private static String[] userAttributes = new String[]{
        FormatConstants.FRAME_BORDER_ATTRIBUTE,
        FormatConstants.FRAME_SPACING_ATTRIBUTE,
        FormatConstants.BORDER_COLOUR_ATTRIBUTE,
        FormatConstants.BORDER_WIDTH_ATTRIBUTE
    };

    private static String[] defaultAttributes = new String[]{
        FormatConstants.FRAME_BORDER_ATTRIBUTE,
        FormatConstants.FRAME_SPACING_ATTRIBUTE,
        FormatConstants.BORDER_WIDTH_ATTRIBUTE
    };

    private static String[] persistentAttributes = userAttributes;

    public SegmentGrid(MontageLayout montageLayout) {
        super(montageLayout);
    }

    public FormatType getFormatType() {
        return FormatType.SEGMENT_GRID;
    }

    public String[] getUserAttributes() {
        return userAttributes;
    }

    public String[] getDefaultAttributes() {
        return defaultAttributes;
    }

    public String[] getPersistentAttributes() {
        return persistentAttributes;
    }

    public String getBorderColour() {
        return (String) getAttribute(FormatConstants.BORDER_COLOUR_ATTRIBUTE);
    }

    public void setBorderColour(String borderColour) {
        setAttribute(FormatConstants.BORDER_COLOUR_ATTRIBUTE,
                     borderColour);
    }

    public String getFrameBorder() {
        return (String) getAttribute(FormatConstants.FRAME_BORDER_ATTRIBUTE);
    }

    public void setFrameBorder(String frameBorder) {
        setAttribute(FormatConstants.FRAME_BORDER_ATTRIBUTE,
                     frameBorder);
    }

    public String getFrameSpacing() {
        return (String) getAttribute(FormatConstants.FRAME_SPACING_ATTRIBUTE);
    }

    public void setFrameSpacing(String frameSpacing) {
        setAttribute(FormatConstants.FRAME_SPACING_ATTRIBUTE,
                     frameSpacing);
    }

    /**
     * Override this method to change the default height unit to percent.
     */
    protected Row createRow() {
        Row row = super.createRow();
        row.setAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE,
                         FormatConstants.HEIGHT_UNITS_VALUE_PERCENT);
        return row;
    }

    // Javadoc inherited from super class.
    public boolean visit(FormatVisitor visitor, Object object)
            throws FormatVisitorException {
        return visitor.visit(this, object);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        // todo: later: add validation for segments and segment grids
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9652/1	gkoch	VBM:2005092204 completely custom marshalling/unmarshalling of layoutFormat

 29-Sep-05	9590/3	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
