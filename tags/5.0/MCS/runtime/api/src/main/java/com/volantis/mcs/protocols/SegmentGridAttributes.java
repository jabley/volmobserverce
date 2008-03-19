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
 * $Header: /src/voyager/com/volantis/mcs/protocols/SegmentGridAttributes.java,v 1.8 2002/03/18 12:41:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width and
 *                              height unit format attributes.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 14-Sep-01    Kula            VBM:2001091011 - the tagname is set to 
 *                              "montage" in the initialise method. In themes
 *                              montage tag is used to set the overscan 
 *                              attributes. The overscan attribute is 
 *                              applicable to body and frameset tags.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.FormatConstants;

/**
 * @mock.generate base="MCSAttributes"
 */
public class SegmentGridAttributes
        extends MCSAttributes {

    public static final String HEIGHT_UNITS_VALUE_PERCENT
            = FormatConstants.HEIGHT_UNITS_VALUE_PERCENT;
    public static final String HEIGHT_UNITS_VALUE_PIXELS
            = FormatConstants.HEIGHT_UNITS_VALUE_PIXELS;

    public static final String WIDTH_UNITS_VALUE_PERCENT
            = FormatConstants.WIDTH_UNITS_VALUE_PERCENT;
    public static final String WIDTH_UNITS_VALUE_PIXELS
            = FormatConstants.WIDTH_UNITS_VALUE_PIXELS;

    private String borderColor;
    private int borderWidth;
    private String[] columnWidthUnits;
    private int[] columnWidths;
    private boolean frameBorder;
    private int frameSpacing;
    private String[] rowHeightUnits;
    private int[] rowHeights;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public SegmentGridAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {
        setTagName("montage");
        borderColor = null;
        borderWidth = 0;
        columnWidthUnits = null;
        columnWidths = null;
        frameBorder = false;
        frameSpacing = 0;
        rowHeightUnits = null;
        rowHeights = null;
    }

    /**
     * Set the borderColor property.
     *
     * @param borderColor The new value of the borderColor property.
     */
    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Get the value of the borderColor property.
     *
     * @return The value of the borderColor property.
     */
    public String getBorderColor() {
        return borderColor;
    }

    /**
     * Set the borderWidth property.
     *
     * @param borderWidth The new value of the borderWidth property.
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * Get the value of the borderWidth property.
     *
     * @return The value of the borderWidth property.
     */
    public int getBorderWidth() {
        return borderWidth;
    }

    /**
     * Set the columnWidthUnits property.
     *
     * @param columnWidthUnits The new value of the columnWidthUnits property.
     */
    public void setColumnWidthUnits(String[] columnWidthUnits) {
        this.columnWidthUnits = columnWidthUnits;
    }

    /**
     * Get the value of the columnWidthUnits property.
     *
     * @return The value of the columnWidthUnits property.
     */
    public String[] getColumnWidthUnits() {
        return columnWidthUnits;
    }

    /**
     * Set the columnWidths property.
     *
     * @param columnWidths The new value of the columnWidths property.
     */
    public void setColumnWidths(int[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    /**
     * Get the value of the columnWidths property.
     *
     * @return The value of the columnWidths property.
     */
    public int[] getColumnWidths() {
        return columnWidths;
    }

    /**
     * Set the frameBorder property.
     *
     * @param frameBorder The new value of the frameBorder property.
     */
    public void setFrameBorder(boolean frameBorder) {
        this.frameBorder = frameBorder;
    }

    /**
     * Get the value of the frameBorder property.
     *
     * @return The value of the frameBorder property.
     */
    public boolean isFrameBorder() {
        return frameBorder;
    }

    /**
     * Set the frameSpacing property.
     *
     * @param frameSpacing The new value of the frameSpacing property.
     */
    public void setFrameSpacing(int frameSpacing) {
        this.frameSpacing = frameSpacing;
    }

    /**
     * Get the value of the frameSpacing property.
     *
     * @return The value of the frameSpacing property.
     */
    public int getFrameSpacing() {
        return frameSpacing;
    }

    /**
     * Set the rowHeightUnits property.
     *
     * @param rowHeightUnits The new value of the rowHeightUnits property.
     */
    public void setRowHeightUnits(String[] rowHeightUnits) {
        this.rowHeightUnits = rowHeightUnits;
    }

    /**
     * Get the value of the rowHeightUnits property.
     *
     * @return The value of the rowHeightUnits property.
     */
    public String[] getRowHeightUnits() {
        return rowHeightUnits;
    }

    /**
     * Set the rowHeights property.
     *
     * @param rowHeights The new value of the rowHeights property.
     */
    public void setRowHeights(int[] rowHeights) {
        this.rowHeights = rowHeights;
    }

    /**
     * Get the value of the rowHeights property.
     *
     * @return The value of the rowHeights property.
     */
    public int[] getRowHeights() {
        return rowHeights;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
