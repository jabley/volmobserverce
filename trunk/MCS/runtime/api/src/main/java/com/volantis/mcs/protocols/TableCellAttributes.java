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
 * $Header: /src/voyager/com/volantis/mcs/protocols/TableCellAttributes.java,v 1.5 2002/08/06 23:12:25 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 11-Oct-01    Allan           VBM:2001090401 - Renamed to TableCellAttributes
 *                              so as to include TableDataCellAttributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 06-Aug-02    Sumit           VBM:2002080509 - Added support for WML onevent 
 *                              handlers
 * 06-Aug-02    Paul            VBM:2002080509 - Removed on... attributes as
 *                              these are all part of EventAttributes.
 * 20-May-03    Byron           VBM:2003051903 - Add tabindex property with
 *                              getter and setter.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.BackgroundColorKeywords;
import com.volantis.mcs.themes.properties.HeightKeywords;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.mcs.themes.properties.WidthKeywords;

public class TableCellAttributes
        extends AbstractTableAttributes {

    private String colSpan;
    private String rowSpan;
    private Object tabindex;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public TableCellAttributes() {
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

        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class. Though both th and td use these attributes
        setTagName("td");

        colSpan = null;
        rowSpan = null;
        tabindex = null;
    }

    /**
     * Set the synthesized value of the bgColor property.
     *
     * @param bgColor the background colour.
     */
    public void setBgColor(final String bgColor) {
        setComputedValue(bgColor,
                BackgroundColorKeywords.getDefaultInstance(),
                StylePropertyDetails.BACKGROUND_COLOR);
    }

    /**
     * Set the colSpan property.
     *
     * @param colSpan The new value of the colSpan property.
     */
    public void setColSpan(final String colSpan) {
        this.colSpan = colSpan;
    }

    /**
     * Get the synthesized value of the colSpan property.
     *
     * @return The value of the colSpan property.
     */
    public String getColSpan() {
        return colSpan;
    }

    /**
     * Set the synthesized value of the height property.
     *
     * @param height The new value of the height property.
     */
    public void setHeight(final String height) {
        setComputedValue(height,
                HeightKeywords.getDefaultInstance(),
                StylePropertyDetails.HEIGHT);
    }

    /**
     * Set the synthesized value of the noWrap property.
     *
     * @param noWrap The new value of the noWrap property.
     */
    public void setNoWrap(String noWrap) {
        setComputedValue(noWrap,
                WhiteSpaceKeywords.getDefaultInstance(),
                StylePropertyDetails.WHITE_SPACE);
    }

    /**
     * Set the rowSpan property.
     *
     * @param rowSpan The new value of the rowSpan property.
     */
    public void setRowSpan(String rowSpan) {
        this.rowSpan = rowSpan;
    }

    /**
     * Get the value of the rowSpan property.
     *
     * @return The value of the rowSpan property.
     */
    public String getRowSpan() {
        return rowSpan;
    }

    /**
     * Set the synthesized value of the vertical align property.
     *
     * @param vAlign The new value of the vAlign property.
     */
    public void setVAlign(final String vAlign) {
        setComputedValue(vAlign,
                VerticalAlignKeywords.getDefaultInstance(),
                StylePropertyDetails.VERTICAL_ALIGN);
    }

    /**
     * Set the synthesized value of the width property.
     *
     * @param width The new value of the width property.
     */
    public void setWidth(final String width) {
        setComputedValue(width,
                WidthKeywords.getDefaultInstance(),
                StylePropertyDetails.WIDTH);
    }

    /**
     * Set the value of the tabindex property.
     *
     * @param tabindex The new value of the tabindex property.
     */
    public void setTabindex(Object tabindex) {
        this.tabindex = tabindex;
    }

    /**
     * Get the value of the tabindex property.
     *
     * @return The value of the tabindex property.
     */
    public Object getTabindex() {
        return tabindex;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Aug-05	9348/5	gkoch	VBM:2005081805 TableCellAttributes.noWrap property is stored in styles + inlined getters

 19-Aug-05	9245/1	gkoch	VBM:2005081006 vbm2005081006 storing property values in styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
