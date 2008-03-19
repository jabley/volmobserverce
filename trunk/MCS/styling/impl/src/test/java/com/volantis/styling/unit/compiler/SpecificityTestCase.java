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

package com.volantis.styling.unit.compiler;

import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.impl.compiler.SpecificityCalculatorImpl;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.compiler.Specificity;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class SpecificityTestCase
        extends TestCaseAbstract {

    private static final int MAX_SELECTORS_PER_TYPE = 99;

    private SpecificityCalculator calculator;

    protected void setUp() throws Exception {
        super.setUp();

        calculator = new SpecificityCalculatorImpl();
    }

    public void testResetWorks() {

        Specificity expected = calculator.getSpecificity();
        calculator.addAttributeSelector();
        calculator.reset();
        Specificity actual = calculator.getSpecificity();

        assertEquals("Reset should clear specificity", expected, actual);
    }

    /**
     * Test that all the add methods actually increase the specificity.
     */
    public void testAddIncreasesSpecificity() {

        // Get the empty specificity.
        Specificity EMPTY = calculator.getSpecificity();
        Specificity after;

        calculator.reset();
        calculator.addAttributeSelector();
        after = calculator.getSpecificity();
        assertTrue("addAttributeSelector should increase specificity.",
                   after.compareTo(EMPTY) > 0);

        calculator.reset();
        calculator.addClassSelector();
        after = calculator.getSpecificity();
        assertTrue("addClassSelector should increase specificity.",
                   after.compareTo(EMPTY) > 0);

        calculator.reset();
        calculator.addElementSelector();
        after = calculator.getSpecificity();
        assertTrue("addElementSelector should increase specificity.",
                   after.compareTo(EMPTY) > 0);

        calculator.reset();
        calculator.addIDSelector();
        after = calculator.getSpecificity();
        assertTrue("addIDSelector should increase specificity.",
                   after.compareTo(EMPTY) > 0);

        calculator.reset();
        calculator.addPseudoClassSelector();
        after = calculator.getSpecificity();
        assertTrue("addPseudoClassSelector should increase specificity.",
                   after.compareTo(EMPTY) > 0);

        calculator.reset();
        calculator.addPseudoElementSelector();
        after = calculator.getSpecificity();
        assertTrue("addPseudoElementSelector should increase specificity.",
                   after.compareTo(EMPTY) > 0);
    }

    /**
     * Test that selectors have the same effect on specificity as others in
     * their group.
     */
    public void testSameEffect() {

        // Make sure that attribute, class and pseudo class selectors have
        // the same specificity.
        calculator.reset();
        calculator.addAttributeSelector();
        Specificity attributeSpecificity = calculator.getSpecificity();

        calculator.reset();
        calculator.addClassSelector();
        Specificity classSpecificity = calculator.getSpecificity();

        calculator.reset();
        calculator.addPseudoClassSelector();
        Specificity pseudoClassSpecificity = calculator.getSpecificity();

        assertEquals("Attribute and class selectors have the same specificity",
                     attributeSpecificity, classSpecificity);
        assertEquals("Class and pseudo class selectors have the same specificity",
                     classSpecificity, pseudoClassSpecificity);

        // Make sure that element and pseudo element selectors have the same
        // specificity.
        calculator.reset();
        calculator.addElementSelector();
        Specificity elementSpecificity = calculator.getSpecificity();

        calculator.reset();
        calculator.addPseudoElementSelector();
        Specificity pseudoElementSpecificity = calculator.getSpecificity();

        assertEquals("Element and pseudo element selectors have the same specificity",
                     elementSpecificity, pseudoElementSpecificity);
    }

    /**
     * Test that the different groups of selectors have the correct relative
     * specificities.
     */
    public void testRelativeSpecificities() {

        // Calculate the specificity of a single class.
        calculator.reset();
        calculator.addClassSelector();
        Specificity singleClassSpecificity = calculator.getSpecificity();

        // Calculate the specificity of a single id.
        calculator.reset();
        calculator.addIDSelector();
        Specificity singleIDSpecificity = calculator.getSpecificity();

        // Calculate the specificity of a large number of element selectors.
        calculator.reset();
        for (int i = 0; i < MAX_SELECTORS_PER_TYPE; i += 1) {
            calculator.addElementSelector();
        }
        Specificity manyElementSpecificity = calculator.getSpecificity();

        // Calculate the specificity of a large number of class selectors.
        calculator.reset();
        for (int i = 0; i < MAX_SELECTORS_PER_TYPE; i += 1) {
            calculator.addClassSelector();
        }
        Specificity manyClassSpecificity = calculator.getSpecificity();

        assertTrue("Class selector (" + singleClassSpecificity +
                   ") should have higher specificity than " +
                   "many element selectors (" + manyElementSpecificity + ")",
                   singleClassSpecificity.compareTo(manyElementSpecificity) > 0);

        assertTrue("ID selector (" + singleIDSpecificity +
                   ") has higher specificity than many class selectors (" +
                   manyClassSpecificity + ")",
                   singleIDSpecificity.compareTo(manyClassSpecificity) > 0);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
