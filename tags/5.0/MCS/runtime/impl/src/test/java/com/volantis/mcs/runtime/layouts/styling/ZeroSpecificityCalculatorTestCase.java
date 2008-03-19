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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.compiler.SpecificityCalculatorMock;
import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.compiler.SpecificityMock;

/**
 * Test cases for {@link ZeroSpecificityCalculator}.
 */
public class ZeroSpecificityCalculatorTestCase
        extends TestCaseAbstract {

    public void test() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final SpecificityCalculatorMock specificityCalculatorMock =
                new SpecificityCalculatorMock("specificityCalculatorMock",
                                              expectations);

        final SpecificityMock specificityMock =
                new SpecificityMock("specificityMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                specificityCalculatorMock.expects.reset();

                specificityCalculatorMock.expects.getSpecificity()
                        .returns(specificityMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        SpecificityCalculator specificityCalculator =
                new ZeroSpecificityCalculator(specificityCalculatorMock);

        Specificity before = specificityCalculator.getSpecificity();
        assertSame("Specificity", specificityMock, before);

        specificityCalculator.addAttributeSelector();
        specificityCalculator.addClassSelector();
        specificityCalculator.addElementSelector();
        specificityCalculator.addIDSelector();
        specificityCalculator.addPseudoClassSelector();
        specificityCalculator.addPseudoElementSelector();

        Specificity after = specificityCalculator.getSpecificity();

        assertEquals("Specificity", after, before);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
