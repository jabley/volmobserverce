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

package com.volantis.styling.integration.compiler;

import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.constraints.Constraint;
import com.volantis.mcs.themes.constraints.ContainsWord;
import com.volantis.mcs.themes.constraints.Equals;
import com.volantis.mcs.themes.constraints.MatchesLanguage;
import com.volantis.mcs.themes.constraints.Set;
import com.volantis.mcs.themes.constraints.ConstraintFactory;
import com.volantis.styling.impl.compiler.MatcherBuilder;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.SimpleMatcherMock;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraintMock;

public class AttributeMatcherBuilderTestCase
        extends MatcherBuilderTestCaseAbstract {

    private static final ConstraintFactory CONSTRAINT_FACTORY =
        ConstraintFactory.getDefaultInstance();

    private ValueConstraint constraintMock;
    private Matcher attributeMatcherMock;

    /**
     * Test that an {@link ContainsWord} is handled correctly.
     */
    public void testAttributeSelectorIncludes() {
        doAttributeSelectorTest(CONSTRAINT_FACTORY.createContainsWord());
    }

    /**
     * Test that an {@link MatchesLanguage} is handled correctly.
     */
    public void testAttributeSelectorDashMatch() {
        doAttributeSelectorTest(CONSTRAINT_FACTORY.createMatchesLanguage());
    }

    /**
     * Test that an {@link Equals} is handled correctly.
     */
    public void testAttributeSelectorEquals() {
        doAttributeSelectorTest(CONSTRAINT_FACTORY.createEquals());
    }

    /**
     * Test that an {@link Set} is handled correctly.
     */
    public void testAttributeSelectorSet() {
        doAttributeSelectorTest(CONSTRAINT_FACTORY.createSet());
    }

    private void doAttributeSelectorTest(Constraint constraint) {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        setupAttributeSelectorFixture();

        // =====================================================================
        //   Set Expectations
        // =====================================================================
        factoryMock.expects.createConstraint(constraint).returns(constraintMock);

        factoryMock.expects.createAttributeMatcher(null, "xyz", constraintMock)
                .returns(attributeMatcherMock);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addAttributeSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        AttributeSelector themeSelector = createAttributeSelector(constraint);
        SelectorSequence sequence = createSelectorSequence(themeSelector);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(sequence);

        assertSame("Constructed matcher not as expected",
                   attributeMatcherMock, matcher);
    }

    private AttributeSelector createAttributeSelector(
            Constraint constraint) {

        AttributeSelector themeSelector =
                themeModelFactory.createAttributeSelector();

        themeSelector.setName("xyz");
        themeSelector.setConstraint(constraint);
        return themeSelector;
    }

    private void setupAttributeSelectorFixture() {

        constraintMock = new ValueConstraintMock("constraint", expectations);

        attributeMatcherMock = new SimpleMatcherMock(
                "attributeMatcher", expectations);
    }

    /**
     * Test that an ID selector is processed correctly.
     */
    public void testIDSelector() {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        setupAttributeSelectorFixture();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        factoryMock.expects.createEqualsConstraint("abc").returns(constraintMock);

        factoryMock.expects.createAttributeMatcher(null, "id", constraintMock).returns(attributeMatcherMock);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addIDSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        IdSelector themeSelector = themeModelFactory.createIdSelector("abc");
        SelectorSequence sequence = createSelectorSequence(themeSelector);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(sequence);

        assertSame("Constructed matcher not as expected",
                   attributeMatcherMock, matcher);
    }

    /**
     * Test that a class selector is processed correctly.
     */
    public void testClassSelector() {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        setupAttributeSelectorFixture();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        factoryMock.expects.createListContainsConstraint("abc").returns(constraintMock);

        factoryMock.expects.createAttributeMatcher(null, "class", constraintMock).returns(attributeMatcherMock);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addClassSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ClassSelector themeSelector =
                themeModelFactory.createClassSelector("abc");
        SelectorSequence sequence = createSelectorSequence(themeSelector);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(sequence);

        assertSame("Constructed matcher not as expected",
                   attributeMatcherMock, matcher);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/8	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/6	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
