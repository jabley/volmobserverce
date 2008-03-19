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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/xhtmlfull/XHTMLFullTransTable.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides XHTMLFull
 *                              protocol-specific DOM table optimization
 *                              support.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.xhtmlfull;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.html.XHTMLBasicTransTable;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.protocols.trans.TransTableHelper;

/**
 * Provides XHTMLFull protocol-specific DOM table optimization support.
 */
public class XHTMLFullTransTable extends XHTMLBasicTransTable {
    
    protected XHTMLFullTransTable(Element table, DOMProtocol protocol) {
        super(table, protocol);
    }

    protected String[] getPreserveStyleAttributes() {
        // The class attribute is handled by the superclass so is not
        // included here. id, colspan and rowspan are explicitly not
        // included since it is not meaningful to do so.
        String[] attributes =
                {"abbr", "align", "axis", "char", "charoff",
                 "dir", "headers", "lang", "onclick", "ondblclick",
                 "onkeydown", "onkeypress", "onkeyup", "onmousedown",
                 "onmousemove", "onmouseout", "onmouseover", "onmouseup",
                 "scope", "style", "title", "valign", "xml:lang"};

        return attributes;
    }

    protected boolean canOptimizeStyle(TransCell containingCell) {
        Element t = containingCell.getParent().getElement();
        Element c = containingCell.getElement();
        String width = table.getAttributeValue("width");
        String rowspan = c.getAttributeValue("rowspan");
        String colspan = c.getAttributeValue("colspan");
        TransTableHelper h = TransTableHelper.getInstance();

        // Note that these tests are in "expense" order so common, simple
        // failures are detected before expending too much processing on
        // the more complex checks
        boolean canOptimize = (table.getAttributeValue("summary") == null) &&
            (c.getAttributeValue("id") == null) &&
            (table.getAttributeValue("onclick") == null) &&
            (table.getAttributeValue("ondblclick") == null) &&
            (table.getAttributeValue("onkeydown") == null) &&
            (table.getAttributeValue("onkeypress") == null) &&
            (table.getAttributeValue("onkeyup") == null) &&
            (table.getAttributeValue("onmousedown") == null) &&
            (table.getAttributeValue("onmousemove") == null) &&
            (table.getAttributeValue("onmouseout") == null) &&
            (table.getAttributeValue("onmouseover") == null) &&
            (table.getAttributeValue("onmouseup") == null) &&
            (t.getAttributeValue("onclick") == null) &&
            (t.getAttributeValue("ondblclick") == null) &&
            (t.getAttributeValue("onkeydown") == null) &&
            (t.getAttributeValue("onkeypress") == null) &&
            (t.getAttributeValue("onkeyup") == null) &&
            (t.getAttributeValue("onmousedown") == null) &&
            (t.getAttributeValue("onmousemove") == null) &&
            (t.getAttributeValue("onmouseout") == null) &&
            (t.getAttributeValue("onmouseover") == null) &&
            (t.getAttributeValue("onmouseup") == null) &&
            ((rowspan == null) || "1".equals(rowspan)) &&
            ((colspan == null) || "1".equals(colspan)) &&
            h.isZero(table.getAttributeValue("border")) &&
            h.isZero(t.getAttributeValue("border")) &&
            h.match(table, t, "cellspacing") &&
            h.match(table, t, "cellpadding") &&
            h.match(table, t, "dir") &&
            h.match(table, t, "frame") &&
            h.match(table, t, "lang") &&
            h.match(table, t, "rules") &&
            h.match(table, t, "style") &&
            ((table.getAttributeValue("title") == null) ||
             h.match(table, t, "title")) &&
            ((width == null) ||
             "100%".equals(width)) &&
            !h.tableHasId(table, getFactory(), false);

//        if (canOptimize) {
//            MutablePropertyValues rowProps = null;
//            MutablePropertyValues tableProps = null;
//            StyleValue styleWidth = null;
//
//            if (t.getStyles() != null) {
//                rowProps = t.getStyles().getPropertyValues();
//            }
//            if (table.getStyles() != null) {
//                tableProps = table.getStyles().getPropertyValues();
//                styleWidth = tableProps.getComputedValue(
//                        StylePropertyDetails.WIDTH);
//            }
//
//            canOptimize = canOptimize &&
//                    (tableProps == null || (styleWidth == null ||
//                    styleWidth.equals(hundredPercent)) &&
//                    h.isBorderInsignificant(tableProps)) &&
//                    (rowProps == null || h.isBorderInsignificant(rowProps)) &&
//                    h.paddingMatches(tableProps, rowProps);
//        }

        return canOptimize && !areTableStylesSignificant(c, t, h);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 05-Aug-05	8859/6	emma	VBM:2005062006 Fixing merge conflicts

 22-Jul-05	8859/3	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 12-Jul-05	8990/2	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/
