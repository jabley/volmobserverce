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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasicTransTable.java,v 1.5 2003/01/09 15:14:38 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 24-Dec-02    Phil W-S        VBM:2002122402 - Update preserveStyle to handle
 *                              the original cell's style class.
 * 09-Jan-03    Phil W-S        VBM:2003010902 - Updated constructor signature
 *                              and make preserveStyle call the superclass.
 * 09-Jan-03    Phil W-S        VBM:2003010906 - Ensure that style classes are
 *                              only merged if multiple style classes are
 *                              supported within the class attribute. Affects
 *                              preserveStyle. Extend the latter to also handle
 *                              table sections, if applicable.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransTableHelper;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;

/**
 * The trans table for the XHTMLBasic unabridged transformer algorithm.
 */
public class XHTMLBasicTransTable extends TransTable {
    
    /**
     * Initializes the instance with the given parameters.
     */
    public XHTMLBasicTransTable(Element table, DOMProtocol protocol) {
        super(table, protocol);
    }

    protected boolean areTableStylesSignificant(
            Element cell, Element row, TransTableHelper helper) {

        // todo fix the tests so that they set up the styles properly so we can removed these fracking null tests.
        final Styles tableStyles = table.getStyles();
        PropertyValues tableProps;
        if (tableStyles == null) {
            tableProps = null;
        } else {
            tableProps = tableStyles.getPropertyValues();
        }

        final Styles rowStyles = row.getStyles();
        PropertyValues rowProps;
        if (rowStyles == null) {
            rowProps = null;
        } else {
        rowProps = rowStyles.getPropertyValues();
        }

        final Styles cellStyles = cell.getStyles();
        PropertyValues cellProps;
        if (cellStyles == null) {
            cellProps = null;
        } else {
            cellProps = cellStyles.getPropertyValues();
        }

        // todo fix the tests that rely on this check.
        if (tableProps != null) {
            if (helper.isTableWidthSignificant(tableProps) ||
                helper.isBorderSignificant(tableProps)) {
                return true;
            }
        }

        if (rowProps != null) {
            if (helper.isBorderSignificant(rowProps)) {
                return true;
            }
        }

        // todo should this check padding of table and row, or table and cell.
        if (!helper.paddingMatches(tableProps, rowProps)) {
            return true;
        }

        if (cellProps != null) {
            if (helper.isCellWidthSignificant(cellProps) ||
                helper.isCellHeightSignificant(cellProps)) {
                return true;
            }
        }

        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 13-May-05	8233/1	emma	VBM:2005042706 Merge from 3.3.0 - multiple style classes generated when device didn't support it

 13-May-05	8192/1	emma	VBM:2005042706 Merge from 3.2.3 - multiple style classes generated when device didn't support it

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/
