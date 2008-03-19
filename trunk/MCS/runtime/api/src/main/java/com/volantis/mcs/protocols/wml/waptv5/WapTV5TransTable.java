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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/waptv5/WapTV5TransTable.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides WAPTV5
 *                              protocol-specific DOM table optimization
 *                              support.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml.waptv5;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransTableHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.styling.values.PropertyValues;

/**
 * Provides WAPTV5 protocol-specific DOM table optimization support.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class WapTV5TransTable extends TransTable {

    protected WapTV5TransTable(Element table, DOMProtocol protocol) {
        super(table, protocol);
    }

    protected void prefactorCols() {
        // Allow the superclass to perform the processing required
        super.prefactorCols();

        if (table != null) {
            // Manage the table's columns attribute to reflect the
            // column count expected after refactoring
            table.setAttribute("columns",
                               StringConvertor.valueOf(getCols()));
        }
    }

    protected String[] getPreserveStyleAttributes() {
        // class is handled in preserveStyle.
        // id, colspan, rowspan, width, height and tabindex are explicitly not
        // included since it is not meaningful to do so.
        String[] cellAttributes =
            {"align", "title", "hspace", "vspace", "hpad", "vpad", "linegap",
             "localsrc",
             "bgimage", "bgradius", "bgcolor", "bgfocused", "bgactivated",
             "borderwidth", "bordercolor", "borderfocused", "borderactivated",
             "mode", "update"};
        return cellAttributes;
    }

    protected boolean canOptimizeStyle(TransCell containingCell) {
        Element t = containingCell.getParent().getElement();
        Element c = containingCell.getElement();
        String rowspan = c.getAttributeValue("rowspan");
        String colspan = c.getAttributeValue("colspan");
        TransTableHelper h = TransTableHelper.getInstance();

        // Note that these tests are in "expense" order so common, simple
        // failures are detected before expending too much processing on
        // the more complex checks
        boolean canOptimize = (table.getAttributeValue("colwidths") == null) &&
            (t.getAttributeValue("colwidths") == null) &&
            (table.getAttributeValue("bgimage") == null) &&
            (t.getAttributeValue("bgimage") == null) &&
            (c.getAttributeValue("tabindex") == null) &&
            (c.getAttributeValue("id") == null) &&
            h.isZero(c.getAttributeValue("width")) &&
            h.isZero(c.getAttributeValue("height")) &&
            h.isZero(table.getAttributeValue("width")) &&
            h.isZero(table.getAttributeValue("height")) &&
            ((rowspan == null) || "1".equals(rowspan)) &&
            ((colspan == null) || "1".equals(colspan)) &&
            h.match(table, t, "rowgap") &&
            h.match(table, t, "colgap") &&
            h.match(table, t, "update") &&
            !h.tableContains(table, getFactory(), "onevent", false) &&
            !h.tableContains(t, getFactory(), "onevent", false) &&
            !h.tableHasId(table, getFactory(), false);

        if (canOptimize) {
            PropertyValues outerCellProps = null;
            PropertyValues tableProps = null;
            StyleValue outerCellWidth = null;
            StyleValue outerCellHeight = null;
            StyleValue tableWidth = null;
            StyleValue tableHeight = null;

            if (c.getStyles() != null) {
                outerCellProps = c.getStyles().getPropertyValues();
                outerCellWidth = outerCellProps.getComputedValue(
                        StylePropertyDetails.WIDTH);
                outerCellHeight = outerCellProps.getComputedValue(
                        StylePropertyDetails.HEIGHT);
            }
            if (table.getStyles() != null) {
                tableProps = table.getStyles().getPropertyValues();
                tableWidth = tableProps.getComputedValue(
                        StylePropertyDetails.WIDTH);
                tableHeight = tableProps.getComputedValue(
                        StylePropertyDetails.HEIGHT);
            }

            canOptimize = canOptimize &&
                    (tableProps == null ||
                    (tableWidth == null ||
                    h.isStyleValueZero(tableWidth)) &&
                    (tableHeight == null || h.isStyleValueZero(tableHeight))) &&
                    (outerCellProps == null ||
                    (outerCellWidth == null ||
                    h.isStyleValueZero(outerCellWidth)) &&
                    (outerCellHeight == null ||
                    h.isStyleValueZero(outerCellHeight)));
        }

        return canOptimize;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9223/2	emma	VBM:2005080403 Remove style class from within protocols and transformers

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	8859/4	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/
