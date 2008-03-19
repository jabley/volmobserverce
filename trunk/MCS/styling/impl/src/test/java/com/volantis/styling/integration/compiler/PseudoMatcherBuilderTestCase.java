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

import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoElementMock;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.impl.compiler.MatcherBuilder;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.SimpleMatcherMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PseudoMatcherBuilderTestCase
        extends MatcherBuilderTestCaseAbstract {

    public void testPseudoClassFirstChildSelector() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        Matcher firstChildMatcher = new SimpleMatcherMock(
                "firstChildMatcher", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pseudoEntityProviderMock.expects.getStatefulPseudoClass("first-child").returns(null);

        factoryMock.fuzzy.createNthChildMatcher(new Integer(0), new Integer(1))
                .returns(firstChildMatcher);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addPseudoClassSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PseudoClassSelector themeSelector =
                themeModelFactory.createPseudoClassSelector("first-child");
        SelectorSequence sequence = createSelectorSequence(themeSelector);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(sequence);

        assertSame("Constructed matcher not as expected",
                   firstChildMatcher, matcher);
    }

    public void testStatefulPseudoClassSelector() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        StatefulPseudoClass hover = StatefulPseudoClasses.HOVER;

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pseudoEntityProviderMock.expects.getStatefulPseudoClass("hover").returns(hover);

        factoryMock.expects.createUniversalMatcher().returns(universalMock);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addPseudoClassSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PseudoClassSelector themeSelector =
                themeModelFactory.createPseudoClassSelector("hover");
        SelectorSequence sequence = createSelectorSequence(themeSelector);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(sequence);

        assertSame("Constructed matcher not as expected",
                   universalMock, matcher);
        List entities = builder.getPseudoStyleEntities();
        assertEquals("List should contain one item", 1, entities.size());
        StatefulPseudoClassSet actual = (StatefulPseudoClassSet) entities.get(0);
        assertEquals("Pseudo class set not as expected", hover.getSet(), actual);
    }

    public void testStatefulPseudoClassSelectorMerged() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        StatefulPseudoClass hover = StatefulPseudoClasses.HOVER;
        StatefulPseudoClass visited = StatefulPseudoClasses.VISITED;
        StatefulPseudoClassSet hoverVisited = hover.getSet().add(visited);
//        StatefulPseudoClassMock hoverMock = new StatefulPseudoClassMock(
//                "hover", expectations);
//
//        StatefulPseudoClassMock visitedMock = new StatefulPseudoClassMock(
//                "visited", expectations);
//
//        StatefulPseudoClassMock hoverVisitedMock = new StatefulPseudoClassMock(
//                "hover:visited", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pseudoEntityProviderMock.expects.getStatefulPseudoClass("hover")
                .returns(hover);

        pseudoEntityProviderMock.expects.getStatefulPseudoClass("visited")
                .returns(visited);

        factoryMock.expects.createUniversalMatcher().returns(universalMock);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addPseudoClassSelector();
        specificityCalculatorMock.expects.addPseudoClassSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List selectors = new ArrayList();
        PseudoClassSelector themeSelector =
                themeModelFactory.createPseudoClassSelector("hover");
        selectors.add(themeSelector);
        themeSelector = themeModelFactory.createPseudoClassSelector("visited");
        selectors.add(themeSelector);
        SelectorSequence sequence = createSelectorSequence(selectors);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(sequence);

        assertSame("Constructed matcher not as expected",
                   universalMock, matcher);
        List entities = builder.getPseudoStyleEntities();
        assertEquals("List should contain one item", 1, entities.size());
        StatefulPseudoClassSet actual = (StatefulPseudoClassSet) entities.get(0);
        assertEquals("Pseudo class set not as expected", hoverVisited, actual);
    }

    public void testPseudoElementSelector() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        PseudoElement beforeMock = new PseudoElementMock(
                "beforeMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pseudoEntityProviderMock.expects.getPseudoElement("before")
                .returns(beforeMock);

        factoryMock.expects.createUniversalMatcher().returns(universalMock);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addPseudoElementSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PseudoElementSelector themeSelector =
                themeModelFactory.createPseudoElementSelector("before");
        SelectorSequence sequence = createSelectorSequence(themeSelector);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(sequence);

        assertSame("Constructed matcher not as expected",
                   universalMock, matcher);
        List entities = builder.getPseudoStyleEntities();
        assertEquals("List should contain one item", 1, entities.size());
        PseudoElement actual = (PseudoElement) entities.get(0);
        assertEquals("Pseudo class set not as expected", beforeMock, actual);
    }

    public void testMultiplePseudoElementSelector() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        PseudoElement afterMock = new PseudoElementMock(
                "afterMock", expectations);
        final PseudoElementMock mcsShortcutMock =
                new PseudoElementMock("mcsShortcutMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pseudoEntityProviderMock.expects.getPseudoElement("mcs-shortcut")
                .returns(mcsShortcutMock);
        pseudoEntityProviderMock.expects.getPseudoElement("after")
                .returns(afterMock);

        factoryMock.expects.createUniversalMatcher().returns(universalMock);

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addPseudoElementSelector();
        specificityCalculatorMock.expects.addPseudoElementSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PseudoElementSelector mcsShortcutSelector =
                themeModelFactory.createPseudoElementSelector("mcs-shortcut");
        PseudoElementSelector afterSelector =
                themeModelFactory.createPseudoElementSelector("after");
        SelectorSequence sequence = createSelectorSequence(
                Arrays.asList(
                        new Object[] {mcsShortcutSelector, afterSelector}));

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(sequence);

        assertSame("Constructed matcher not as expected",
                   universalMock, matcher);
        List entities = builder.getPseudoStyleEntities();

        List expectedEntities = Arrays.asList(new Object[] {
            mcsShortcutMock, afterMock
        });
        assertEquals("List contents incorrect", expectedEntities, entities);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 05-Sep-05	9407/7	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/5	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
