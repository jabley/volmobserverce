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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/htmlversion3_2/HTML3_2TransTableTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests associated
 *                              class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.htmlversion3_2;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransTableTestAbstract;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueFactory;

import java.util.HashMap;

/**
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class HTML3_2TransTableTestCase extends TransTableTestAbstract {

    // Javadoc inherited.
    protected boolean checksCanPreserveClasses() {
        return false;
    }

    // Javadoc inherited.
    protected String[] getExpectedPreserveStyleAttributes() {
        return new String[]{"align", "bgcolor", "nowrap", "valign"};
    }

    // Javadoc inherited.
    public TransTable createTransTable(Element table, DOMProtocol protocol) {

        TransTable transTable = new HTML3_2TransTable(table, protocol);
        transTable.setFactory(new HTML3_2TransFactory(
                protocol.getProtocolConfiguration()));
        return transTable;
    }

    // Javadoc inherited.
    protected void setInnerTableCanOptimizeExpectations(
            HashMap expectedValues) {

        super.setInnerTableCanOptimizeExpectations(expectedValues);

        innerTable.expects.getAttributeValue(BORDER).
                returns((String) expectedValues.get(BORDER)).atLeast(1);
        innerTable.expects.getAttributeValue(CELLSPACING).
                returns((String) expectedValues.get(CELLSPACING)).any();
        innerTable.expects.getAttributeValue(CELLPADDING).
                returns((String) expectedValues.get(CELLPADDING)).any();
    }

    // Javadoc inherited.
    protected void setOuterTableCanOptimizeExpectations(
            HashMap expectedValues) {

        super.setOuterTableCanOptimizeExpectations(expectedValues);

        outerTable.expects.getAttributeValue(BORDER).
                returns((String) expectedValues.get(BORDER)).any();
        outerTable.expects.getAttributeValue(CELLSPACING).
                returns((String) expectedValues.get(CELLSPACING)).any();
        outerTable.expects.getAttributeValue(CELLPADDING).
                returns((String) expectedValues.get(CELLPADDING)).any();
    }

    /**
     * Verifies that tables can only be optimized away (when the optimization
     * level is LITTLE_IMPACT) if the borders of both tables are either null
     * or zero.
     */
    public void testCanOptimizeStyleWhenBorderIsZeroOrNull() {
        doTestCanOptimizeStyleWhenBorderIsZeroOrNull();
    }

    /**
     * Verifies that tables can only be optimized away (when the optimization
     * level is LITTLE_IMPACT) if the width of the inner table is null or 100%.
     */
    public void testCanOptimizeStyleWhenWidthIsHundredPercentOrNull()
            throws Exception {

        // ===================================================================
        //   Test when setting attribute values
        // ===================================================================
        checkCanOptimizeWithParticularAttributeValues(
                WIDTH, "100%", "10%", INNER);

        // ===================================================================
        //   Test when setting Styles values
        // ===================================================================
        StylePercentage optimizeValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 100);
        StylePercentage noOptValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 10);

        checkCanOptimizeWithParticularPropertyValues(StylePropertyDetails.WIDTH,
                optimizeValue, noOptValue, STYLES, INNER);
    }

    /**
     * Verifies that tables can only be optimized away if the cell padding is
     * set to the same value for both inner and outer tables.
     */
    public void testCanOptimizeStyleWithMatchingPadding() {

        checkCannotOptimizeStyleWithNonMatchingValues(CELLPADDING);        
        checkCanOptimizeStyleWithMatchingValues(CELLPADDING);

        checkCannotOptimizeStyleWithNonMatchingPropertyValues(
                StylePropertyDetails.PADDING_TOP);
        checkCannotOptimizeStyleWithNonMatchingPropertyValues(
                StylePropertyDetails.PADDING_BOTTOM);
        checkCannotOptimizeStyleWithNonMatchingPropertyValues(
                StylePropertyDetails.PADDING_LEFT);
        checkCannotOptimizeStyleWithNonMatchingPropertyValues(
                StylePropertyDetails.PADDING_RIGHT);

        checkCanOptimizeStyleWithMatchingPropertyValues(
                StylePropertyDetails.PADDING_TOP);
        checkCanOptimizeStyleWithMatchingPropertyValues(
                StylePropertyDetails.PADDING_BOTTOM);
        checkCanOptimizeStyleWithMatchingPropertyValues(
                StylePropertyDetails.PADDING_LEFT);
        checkCanOptimizeStyleWithMatchingPropertyValues(
                StylePropertyDetails.PADDING_RIGHT);
    }

    /**
     * Verifies that tables can only be optimized away if the cell spacing is
     * set to the same value for both inner and outer tables.
     */
    public void testCanOptimizeStyleWithMatchingCellSpacing() {

        checkCannotOptimizeStyleWithNonMatchingValues(CELLSPACING);
        checkCanOptimizeStyleWithMatchingValues(CELLSPACING);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 22-Jul-05	8859/4	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 ===========================================================================
*/
