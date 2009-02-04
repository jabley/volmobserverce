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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/htmlversion3_2/HTML3_2TransTable.java,v 1.3 2003/01/16 11:29:17 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides HTML 3.2
 *                              protocol-specific DOM table optimization
 *                              support.
 * 15-Jan-03    Phil W-S        VBM:2002110402 - Rework: remove bgcolor match
 *                              rule.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.htmlversion3_2;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransTableHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.WidthKeywords;
import com.volantis.styling.values.PropertyValues;

/**
 * Provides HTML 3.2 protocol-specific DOM table optimization support.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class HTML3_2TransTable extends TransTable {

    public HTML3_2TransTable(Element table, DOMProtocol protocol) {
        super(table, protocol);
    }

    protected String[] getPreserveStyleAttributes() {
        // The class attribute is handled by the superclass so is not
        // included here. colspan, height, rowspan and width are explicitly not
        // included since it is not meaningful to do so. bgcolor is not
        // in the DTD but is commonly supported by browsers so is included
        // here
        String[] attributes =
            {"align", "bgcolor", "nowrap", "valign"};

        return attributes;
    }

    protected boolean canOptimizeStyle(TransCell containingCell) {
        Element t = containingCell.getParent().getElement();
        Element c = containingCell.getElement();
        String width = table.getAttributeValue("width");
        String rowspan = c.getAttributeValue("rowspan");
        String colspan = c.getAttributeValue("colspan");
        TransTableHelper h = TransTableHelper.getInstance();

        boolean canOptimize =
                h.isZero(table.getAttributeValue("border")) &&
                h.isZero(t.getAttributeValue("border")) &&
                ((rowspan == null) || "1".equals(rowspan)) &&
                ((colspan == null) || "1".equals(colspan)) &&
                h.match(table, t, "cellspacing") &&
                h.match(table, t, "cellpadding") &&
                ((width == null) || "100%".equals(width));

        if (canOptimize) {
            PropertyValues outerProps = null;
            PropertyValues innerProps = null;
            StyleValue styleWidth = null;
            if (t.getStyles() != null) {
                outerProps = t.getStyles().getPropertyValues();
            }
            if (table.getStyles() != null) {
                innerProps = table.getStyles().getPropertyValues();
                styleWidth = innerProps.getComputedValue(
                        StylePropertyDetails.WIDTH);
            }
            
            canOptimize = canOptimize &&
                    (innerProps == null || (styleWidth == null ||
                    styleWidth.equals(hundredPercent) ||
                    styleWidth == WidthKeywords.AUTO) &&
                    h.isBorderInsignificant(innerProps)) &&
                    (outerProps == null || h.isBorderInsignificant(outerProps)) &&
                    h.paddingMatches(innerProps, outerProps);
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

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 22-Jul-05	8859/3	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/
