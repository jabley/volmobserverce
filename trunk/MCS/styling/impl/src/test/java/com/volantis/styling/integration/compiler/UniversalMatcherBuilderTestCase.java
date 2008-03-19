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

import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.UniversalSelector;
import com.volantis.styling.impl.compiler.MatcherBuilder;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherMock;
import com.volantis.styling.impl.engine.matchers.SimpleMatcherMock;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraintMock;

import java.util.ArrayList;
import java.util.List;

public class UniversalMatcherBuilderTestCase
        extends MatcherBuilderTestCaseAbstract {

    /**
     * Test that the universal selector in an otherwise empty sequence is
     * preserved.
     */
    public void testUniversalSelectorInOtherwiseEmptySequence() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        factoryMock.expects.createNamespaceMatcher("*").returns(null).any();

        factoryMock.expects.createUniversalMatcher().returns(universalMock);

        // Set the expectations on the specificity calculator.
        // Expectation is that no methods will be called.

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // CSS: *
        UniversalSelector themeUniversal =
                themeModelFactory.createUniversalSelector();
        themeUniversal.setNamespacePrefix("*");

        // CSS: *
        SelectorSequence themeSequence =
                createSelectorSequence(themeUniversal);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(themeSequence);
        assertSame("Universal selector on its own is preserved",
                   universalMock, matcher);
    }

    /**
     * Test that the universal selector with no namespace adds a default
     * namespace matched.
     */
    public void testUniversalSelectorInDefaultNamespace() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final SimpleMatcherMock defaultNamespaceMatcherMock =
                new SimpleMatcherMock("defaultNamespaceMatcherMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        factoryMock.expects.createDefaultNamespaceMatcher()
                .returns(defaultNamespaceMatcherMock).any();

        // Set the expectations on the specificity calculator.
        // Expectation is that no methods will be called.

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // CSS: *
        UniversalSelector themeUniversal =
                themeModelFactory.createUniversalSelector();

        // CSS: *
        SelectorSequence themeSequence =
                createSelectorSequence(themeUniversal);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(themeSequence);
        assertSame("Universal selector in default namespace adds a " +
                "namespace matcher",
                defaultNamespaceMatcherMock, matcher);
    }

    /**
     * Test that unnecessary universal selectors, i.e. those combined with
     * other selectors in a sequence are discarded.
     */
    public void testUnnecessaryUniversalSelectorsDiscarded() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        ValueConstraint listContains = new ValueConstraintMock(
                "listContains", expectations);

        Matcher attributeMatcher = new SimpleMatcherMock(
                "attributeMatcher", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        factoryMock.expects.createNamespaceMatcher("*").returns(null).any();

        factoryMock.expects.createListContainsConstraint("b")
                .returns(listContains);

        factoryMock.expects.createAttributeMatcher(null, "class", listContains)
                .returns(attributeMatcher);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addClassSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // CSS: *
        UniversalSelector themeUniversal =
                themeModelFactory.createUniversalSelector();
        themeUniversal.setNamespacePrefix("*");

        // CSS: .b
        ClassSelector themeClassSelector =
                themeModelFactory.createClassSelector("b");

        List selectors = new ArrayList();
        selectors.add(themeUniversal);
        selectors.add(themeClassSelector);

        // CSS: *.b
        SelectorSequence themeSequence = createSelectorSequence(selectors);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(themeSequence);
        assertSame("Universal selector combined with other selectors in a" +
                   "sequence has no impact on matching",
                   attributeMatcher, matcher);
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

 05-Sep-05	9407/7	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/5	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 13-Jun-05	8552/3	pabbott	VBM:2005051902 An Eclipse editor fix

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
