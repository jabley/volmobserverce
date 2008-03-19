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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/waptv5/WapTV5TransTableTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
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
package com.volantis.mcs.protocols.wml.waptv5;

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
public class WapTV5TransTableTestCase extends TransTableTestAbstract {

    // Javadoc inherited.
    protected boolean checksCanPreserveClasses() {
        return true;
    }

    // Javadoc inherited.
    protected String[] getExpectedPreserveStyleAttributes() {
        return new String[]{
            "align", "title", "hspace", "vspace", "hpad", "vpad", "linegap",
            "localsrc", "bgimage", "bgradius", "bgcolor", "bgfocused",
            "bgactivated", "borderwidth", "bordercolor", "borderfocused",
            "borderactivated", "mode", "update"};
    }

    // Javadoc inherited.
    public TransTable createTransTable(Element table, DOMProtocol protocol) {

        TransTable transTable = new WapTV5TransTable(table, protocol);
        transTable.setFactory(new WapTV5TransFactory(
                protocol.getProtocolConfiguration()));
        return transTable;
    }

    // Javadoc inherited.
    protected void setInnerTableCanOptimizeExpectations(HashMap expectedValues) {

        super.setInnerTableCanOptimizeExpectations(expectedValues);

        innerTable.expects.getAttributeValue(HEIGHT).
                returns((String) expectedValues.get(HEIGHT)).min(0).max(1);
        innerTable.expects.getAttributeValue(COLWIDTHS).
                returns((String) expectedValues.get(COLWIDTHS)).min(0).max(1);
        innerTable.expects.getAttributeValue(BGIMAGE).
                returns((String) expectedValues.get(BGIMAGE)).min(0).max(1);
        innerTable.expects.getAttributeValue(ROWGAP).
                returns((String) expectedValues.get(ROWGAP)).min(0).max(1);
        innerTable.expects.getAttributeValue(COLGAP).
                returns((String) expectedValues.get(COLGAP)).min(0).max(1);
        innerTable.expects.getAttributeValue(UPDATE).
                returns((String) expectedValues.get(UPDATE)).min(0).max(1);

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

        outerCell.expects.getAttributeValue(WIDTH).
                returns((String) expectedValues.get(WIDTH)).min(0).max(1);
        outerCell.expects.getAttributeValue(HEIGHT).
                returns((String) expectedValues.get(HEIGHT)).min(0).max(1);
        outerCell.expects.getAttributeValue(TABINDEX).
                returns((String) expectedValues.get(TABINDEX)).min(0).max(1);
        outerCell.expects.getAttributeValue(ID).
                returns((String) expectedValues.get(ID)).min(0).max(1);
    }

    // Javadoc inherited.
    protected void setOuterTableCanOptimizeExpectations(HashMap expectedValues) {

        super.setOuterTableCanOptimizeExpectations(expectedValues);

        outerTable.expects.getAttributeValue(COLWIDTHS).
                returns((String) expectedValues.get(COLWIDTHS)).min(0).max(1);
        outerTable.expects.getAttributeValue(BGIMAGE).
                returns((String) expectedValues.get(BGIMAGE)).min(0).max(1);
        outerTable.expects.getAttributeValue(ROWGAP).
                returns((String) expectedValues.get(ROWGAP)).min(0).max(1);
        outerTable.expects.getAttributeValue(COLGAP).
                returns((String) expectedValues.get(COLGAP)).min(0).max(1);
        outerTable.expects.getAttributeValue(UPDATE).
                returns((String) expectedValues.get(UPDATE)).min(0).max(1);

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
     * @todo later implement test
     */
    public void notestPrefactorCols() throws Exception {
    }

    /**
     * Verifies that tables can only be optimized away (when the optimization
     * level is LITTLE_IMPACT) if the widths of the inner table and cell are
     * null or zero.
     */
    public void testCanOptimizeStyleWhenWidthIsZeroOrNull() throws Exception {

        // ===================================================================
        //   Test when setting attribute values
        // ===================================================================
        checkCanOptimizeWithParticularAttributeValues(WIDTH, "0", "10%", INNER);
        checkCanOptimizeWithParticularAttributeValues(WIDTH, "0", "10%", CELL);

        // ===================================================================
        //   Test when setting Styles values
        // ===================================================================
        StylePercentage optimizeValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 0);
        StylePercentage noOptValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 10);

        checkCanOptimizeWithParticularPropertyValues(StylePropertyDetails.WIDTH,
                optimizeValue, noOptValue, STYLES, INNER);
        checkCanOptimizeWithParticularPropertyValues(StylePropertyDetails.WIDTH,
                optimizeValue, noOptValue, STYLES, CELL);
    }

    /**
     * Verifies that tables can only be optimized away (when the optimization
     * level is LITTLE_IMPACT) if the heights of the inner table and cell are
     * null or zero.
     */
    public void testCanOptimizeStyleWhenHeightIsZeroOrNull() throws Exception {

        // ===================================================================
        //   Test when setting attribute values
        // ===================================================================
        checkCanOptimizeWithParticularAttributeValues(
                HEIGHT, "0", "10%", INNER);
        checkCanOptimizeWithParticularAttributeValues(
                HEIGHT, "0", "10%", CELL);

        // ===================================================================
        //   Test when setting Styles values
        // ===================================================================
        StylePercentage optimizeValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 0);
        StylePercentage noOptValue =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 10);

        checkCanOptimizeWithParticularPropertyValues(
                StylePropertyDetails.HEIGHT, optimizeValue, noOptValue,
                STYLES, INNER);
        checkCanOptimizeWithParticularPropertyValues(
                StylePropertyDetails.HEIGHT, optimizeValue, noOptValue,
                STYLES, CELL);
    }

    /**
     * Verifies that tables can only be optimized away if the the following
     * values are either both null or matching for both inner and outer tables:
     * rowgap, colgap and update.
     */
    public void testCanOptimizeStyleWithMatchingValues() {

        checkCannotOptimizeStyleWithNonMatchingValues(ROWGAP);
        checkCanOptimizeStyleWithMatchingValues(ROWGAP);

        checkCannotOptimizeStyleWithNonMatchingValues(COLGAP);
        checkCanOptimizeStyleWithMatchingValues(COLGAP);

        checkCannotOptimizeStyleWithNonMatchingValues(UPDATE);
        checkCanOptimizeStyleWithMatchingValues(UPDATE);
    }

    /**
     * Verifies that tables cannot be optimized away (when the optimization
     * level is LITTLE_IMPACT) if certain key attributes are set at all.
     */
    public void testCannotOptimizeStyleIfKeyAttributesNonNull() {

        checkCanOptimizeStyleIfKeyAttributesNonNull("colwidths", INNER);
        checkCanOptimizeStyleIfKeyAttributesNonNull("colwidths", OUTER);
        checkCanOptimizeStyleIfKeyAttributesNonNull("bgimage", INNER);
        checkCanOptimizeStyleIfKeyAttributesNonNull("bgimage", OUTER);
        checkCanOptimizeStyleIfKeyAttributesNonNull("tabindex", CELL);
        checkCanOptimizeStyleIfKeyAttributesNonNull("id", CELL);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 22-Jul-05	8859/4	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 ===========================================================================
*/
