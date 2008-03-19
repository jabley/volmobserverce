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
package com.volantis.mcs.protocols;

import com.volantis.styling.StylesBuilder;

/**
 * Test cases for TableFooter class.
 */
public class TableHeaderAttributesTestCase
        extends TableAttributesTestCaseAbstract {

    private TableHeaderAttributes attributes;

    protected void setUp() throws Exception {
        super.setUp();
        attributes = new TableHeaderAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
    }

    // Javadoc inherited.
    protected AbstractTableAttributes getAbstractTableAttributes() {
        return attributes;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9348/1	gkoch	VBM:2005081805 TableCellAttributes.noWrap property is stored in styles + inlined getters

 19-Aug-05	9245/1	gkoch	VBM:2005081006 vbm2005081006 storing property values in styles

 ===========================================================================
*/
