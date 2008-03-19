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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.styling.unit.compiler;

import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.impl.compiler.InlineStylesSpecificityCalculator;
import com.volantis.styling.impl.compiler.SpecificityCalculatorImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for the InlineStylesSpecificityCalculator
 */
public class InlineStylesSpecificityCalculatorTestCase
        extends TestCaseAbstract {

    /**
     * The calculator to test against
     */
    private InlineStylesSpecificityCalculator calculator;

    //setup the tests
    protected void setUp() throws Exception {
        super.setUp();

        calculator = new InlineStylesSpecificityCalculator();
    }

    /**
     * Test the specificity value is equal initially and after a reset
     */
    public void testInitialSetupAndReset() {
        Specificity initial = calculator.getSpecificity();

        calculator.addAttributeSelector();
        calculator.reset();

        Specificity after = calculator.getSpecificity();

        assertEquals("The initial specificity should be equal to the " +
                "reset specificity", initial, after);
    }

    /**
     * Test the specificity values returned from the
     * InlineStyleSpecificityCalculator always exceed a standard Specificity
     * Calculator
     */
    public void testInlineCalculatorExceedsStandardCalculator() {
        SpecificityCalculator standardCalculator =
                new SpecificityCalculatorImpl();

        checkCalculatorTakePresidence(standardCalculator);

        calculator.addAttributeSelector();
        standardCalculator.addAttributeSelector();

        checkCalculatorTakePresidence(standardCalculator);

        calculator.addClassSelector();
        standardCalculator.addClassSelector();

        checkCalculatorTakePresidence(standardCalculator);

        calculator.addElementSelector();
        standardCalculator.addElementSelector();

        checkCalculatorTakePresidence(standardCalculator);

        calculator.addIDSelector();
        standardCalculator.addIDSelector();

        checkCalculatorTakePresidence(standardCalculator);

        calculator.addPseudoClassSelector();
        standardCalculator.addPseudoClassSelector();

        checkCalculatorTakePresidence(standardCalculator);

        calculator.addPseudoElementSelector();
        standardCalculator.addPseudoElementSelector();

        checkCalculatorTakePresidence(standardCalculator);

        calculator.reset();
        standardCalculator.reset();

        checkCalculatorTakePresidence(standardCalculator);

    }

    private void checkCalculatorTakePresidence(
            SpecificityCalculator standardCalculator) {
        assertTrue("Calculator should take presidence",
            (calculator.getSpecificity().compareTo(
                standardCalculator.getSpecificity()) > 0));
    }

}
