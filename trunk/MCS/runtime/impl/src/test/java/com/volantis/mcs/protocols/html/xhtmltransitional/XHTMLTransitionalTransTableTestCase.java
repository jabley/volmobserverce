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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/xhtmltransitional/XHTMLTransitionalTransTableTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests associated
 *                              class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.xhtmltransitional;

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
public class XHTMLTransitionalTransTableTestCase
        extends TransTableTestAbstract {

    // Javadoc inherited.
    protected boolean checksCanPreserveClasses() {
        return true;
    }

    protected String[] getExpectedPreserveStyleAttributes() {
        return new String[]{
            "abbr", "align", "axis", "bgcolor", "char", "charoff", DIR, "headers",
            LANG, "nowrap", ONCLICK, ONDBLCLICK, ONKEYDOWN, ONKEYPRESS, ONKEYUP,
            ONMOUSEDOWN, ONMOUSEMOVE, ONMOUSEOUT, ONMOUSEOVER, ONMOUSEUP,
            "scope", STYLE, TITLE, "valign", "xml:lang"};
    }

    // Javadoc inherited.
    public TransTable createTransTable(Element table, DOMProtocol protocol) {

        TransTable transTable = new XHTMLTransitionalTransTable(table, protocol);
        transTable.setFactory(new XHTMLTransitionalTransFactory(
                protocol.getProtocolConfiguration()));
        return transTable;
    }

    // Javadoc inherited.
    protected void setInnerTableCanOptimizeExpectations(HashMap expectedValues) {

        super.setInnerTableCanOptimizeExpectations(expectedValues);

        innerTable.expects.getAttributeValue(SUMMARY).
                returns((String) expectedValues.get(SUMMARY)).optional();
        innerTable.expects.getAttributeValue(TITLE).
                returns((String) expectedValues.get(TITLE)).min(0).max(2);

        setXHTMLTableExpectations(expectedValues, innerTable);

        innerTable.expects.getName().returns("table").any();
//        innerTable.fuzzy.visitChildren(
//                mockFactory.expectsInstanceOf(TransTableHelper.class),
//                null).
//                returns(false).any();

        // todo This is not a very realistic test as it behaves like the table
        // todo element has no children but it does. This should iterator over
        // todo the innerRow, .... 
        innerTable.fuzzy
                .forEachChild(mockFactory.expectsInstanceOf(Object.class)).returns().any();
        innerTable.fuzzy.getAttributeValue(
                mockFactory.expectsInstanceOf(String.class)).
                returns(null).any();
    }

    // Javadoc inherited.
    protected void setOuterCellCanOptimizeExpectations(HashMap expectedValues) {

        super.setOuterCellCanOptimizeExpectations(expectedValues);

        outerCell.expects.getAttributeValue(HEIGHT).
                returns((String) expectedValues.get(HEIGHT)).optional();
        outerCell.expects.getAttributeValue(WIDTH).
                returns((String) expectedValues.get(WIDTH)).optional();
        outerCell.expects.getAttributeValue(ID).
                returns((String) expectedValues.get(ID)).optional();
    }

    // Javadoc inherited.
    protected void setOuterTableCanOptimizeExpectations(HashMap expectedValues) {

        super.setOuterTableCanOptimizeExpectations(expectedValues);

        outerTable.expects.getAttributeValue(TITLE).
                returns((String) expectedValues.get(TITLE)).optional();

        setXHTMLTableExpectations(expectedValues, outerTable);

        outerTable.expects.getName().returns("table").any();
//        outerTable.fuzzy.visitChildren(
//                mockFactory.expectsInstanceOf(TransTableHelper.class),
//                null).
//                returns(false).any();

        // todo This is not a very realistic test as it behaves like the table
        // todo element has no children but it does. This should iterator over
        // todo the outerRow, ....
        outerTable.fuzzy
                .forEachChild(mockFactory.expectsInstanceOf(Object.class)).returns().any();
        outerTable.fuzzy.getAttributeValue(
                mockFactory.expectsInstanceOf(String.class)).
                returns(null).any();
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
     * level is LITTLE_IMPACT) if the width of the cell containing the inner
     * table is null or zero.
     */
    public void testCanOptimizeStyleWhenCellWidthIsZeroOrNull()
            throws Exception {

        // ===================================================================
        //   Test when setting attribute values
        // ===================================================================
        checkCanOptimizeWithParticularAttributeValues(WIDTH, "0", "10%", CELL);

        // ===================================================================
        //   Test when setting Styles values
        // ===================================================================
        StylePercentage optimizeValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 0);
        StylePercentage noOptValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 10);

        checkCanOptimizeWithParticularPropertyValues(StylePropertyDetails.WIDTH,
                optimizeValue, noOptValue, STYLES, CELL);
    }

    /**
     * Verifies that tables can only be optimized away (when the optimization
     * level is LITTLE_IMPACT) if the height of the cell containing the inner
     * table is null or zero.
     */
    public void testCanOptimizeStyleWhenCellHeightIsZeroOrNull()
            throws Exception {

        // ===================================================================
        //   Test when setting attribute values
        // ===================================================================
        checkCanOptimizeWithParticularAttributeValues(HEIGHT, "0", "10%", CELL);

        // ===================================================================
        //   Test when setting Styles values
        // ===================================================================
        StylePercentage optimizeValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 0);
        StylePercentage noOptValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 10);

        checkCanOptimizeWithParticularPropertyValues(
                StylePropertyDetails.HEIGHT, optimizeValue, noOptValue,
                STYLES, CELL);
    }

    /**
     * Verifies that tables can only be optimized away (when the optimization
     * level is LITTLE_IMPACT) if the width of the inner table is null or 100%.
     */
    public void testCanOptimizeStyleWhenInnerTableWidthIsHundredPercentOrNull()
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
     * Verifies that tables can only be optimized away if the the following
     * values are either both null or matching for both inner and outer tables:
     * cellspacing, dir, frame, lang, rules, style and title.
     */
    public void testCanOptimizeStyleWithMatchingValues() {

        checkCannotOptimizeStyleWithNonMatchingValues(CELLSPACING);
        checkCanOptimizeStyleWithMatchingValues(CELLSPACING);

        checkCannotOptimizeStyleWithNonMatchingValues(DIR);
        checkCanOptimizeStyleWithMatchingValues(DIR);

        checkCannotOptimizeStyleWithNonMatchingValues(FRAME);
        checkCanOptimizeStyleWithMatchingValues(FRAME);

        checkCannotOptimizeStyleWithNonMatchingValues(LANG);
        checkCanOptimizeStyleWithMatchingValues(LANG);

        checkCannotOptimizeStyleWithNonMatchingValues(RULES);
        checkCanOptimizeStyleWithMatchingValues(RULES);

        checkCannotOptimizeStyleWithNonMatchingValues(STYLE);
        checkCanOptimizeStyleWithMatchingValues(STYLE);

        checkCannotOptimizeStyleWithNonMatchingValues(TITLE);
        checkCanOptimizeStyleWithMatchingValues(TITLE);
    }

    /**
     * Verifies that tables cannot be optimized away (when the optimization
     * level is LITTLE_IMPACT) if certain key attributes are set at all.
     */
    public void testCannotOptimizeStyleIfKeyAttributesNonNull() {

        for (int i = 0; i < EVENT_ATTS.length; i++) {
            checkCanOptimizeStyleIfKeyAttributesNonNull(
                    EVENT_ATTS[i], INNER);
            checkCanOptimizeStyleIfKeyAttributesNonNull(
                    EVENT_ATTS[i], OUTER);
        }

        checkCanOptimizeStyleIfKeyAttributesNonNull(BORDER, INNER);
        checkCanOptimizeStyleIfKeyAttributesNonNull(BORDER, OUTER);
        checkCanOptimizeStyleIfKeyAttributesNonNull(SUMMARY, INNER);
        checkCanOptimizeStyleIfKeyAttributesNonNull(ID, CELL);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 22-Jul-05	8859/4	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
