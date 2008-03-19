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

import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.Subject;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.styling.impl.compiler.MatcherBuilder;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherMock;
import com.volantis.styling.impl.engine.matchers.SimpleMatcherMock;
import com.volantis.styling.impl.engine.matchers.composites.CompositeStateFactoryMock;
import com.volantis.testtools.mock.expectations.Expectations;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.expectations.UnorderedExpectations;

import java.util.Arrays;

public class MatcherBuilderTestCase
        extends MatcherBuilderTestCaseAbstract {

    private MatcherMock defaultNamespaceMatcherMock;

    protected void setUp() throws Exception {
        super.setUp();

        defaultNamespaceMatcherMock = new SimpleMatcherMock(
                "defaultNamespaceMatcherMock", expectations);
    }

    /**
     * Test that a reasonably complex combined selector is process properly.
     *
     * <pre>
     *   a > b c
     * </pre>
     */
    public void testCombinedSelector() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final Matcher alocal = new SimpleMatcherMock("alocal", expectations);
        final Matcher asequence = new MatcherMock("asequence", expectations);

        final Matcher blocal = new SimpleMatcherMock("blocal", expectations);
        final Matcher bsequence = new MatcherMock("bsequence", expectations);

        final Matcher clocal = new SimpleMatcherMock("clocal", expectations);
        final Matcher csequence = new MatcherMock("csequence", expectations);

        final CompositeStateFactoryMock descendantFactoryMock =
                new CompositeStateFactoryMock("descendantFactoryMock",
                                              expectations);

        final CompositeStateFactoryMock childFactoryMock =
                new CompositeStateFactoryMock("childFactoryMock",
                                              expectations);

        final Matcher b_cMatcher = new MatcherMock(
                "b_cMatcher", expectations);

        final Matcher a_bcMatcher = new MatcherMock(
                "a_bcMatcher", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The different parts of the "b c" selector can be created in any
        // order but that must be before the composite matcher is created.
        final Expectations bcParts = new UnorderedExpectations() {
            public void add() {
                expectTypeSelectorWithDefaultNamespace("b", blocal, bsequence);

                expectTypeSelectorWithDefaultNamespace("c", clocal, csequence);

                factoryMock.expects.createDescendantStateFactory()
                        .returns(descendantFactoryMock);
            }
        };

        final Expectations bcComposite = new OrderedExpectations() {
            public void add() {

                add(bcParts);

                factoryMock.expects.createCompositeMatcher(bsequence, csequence,
                        descendantFactoryMock, matcherBuilderContextMock)
                        .returns(b_cMatcher);
            }
        };

        // The different parts of the "a > (b c)" selector can be created in
        // any order but that must be before the composite matcher is created.
        final Expectations a_bcParts = new UnorderedExpectations() {
            public void add() {

                add(bcComposite);

                expectTypeSelectorWithDefaultNamespace("a", alocal, asequence);

                factoryMock.expects.createChildStateFactory()
                        .returns(childFactoryMock);
            }
        };

        final Expectations a_bcComposite = new OrderedExpectations() {
            public void add() {

                add(a_bcParts);

                factoryMock.expects.createCompositeMatcher(
                        asequence, b_cMatcher, childFactoryMock,
                        matcherBuilderContextMock)
                        .returns(a_bcMatcher);
            }
        };

        expectations.add(a_bcComposite);

        specificityCalculatorMock.expects.addElementSelector();
        specificityCalculatorMock.expects.addElementSelector();
        specificityCalculatorMock.expects.addElementSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // CSS: b c
        CombinedSelector themeSelector;
        themeSelector = createCombinedSelector(
                createSelectorSequence("b"),
                CombinatorEnum.DESCENDANT,
                createSelectorSequence("c"));

        // CSS: a > b c
        themeSelector = createCombinedSelector(
                createSelectorSequence("a"),
                CombinatorEnum.CHILD,
                themeSelector);

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(themeSelector);

        assertSame("Constructed matcher not as expected",
                   a_bcMatcher, matcher);
    }

    private CombinedSelector createCombinedSelector(SelectorSequence context,
            CombinatorEnum combinator, Subject subject) {

        CombinedSelector selector = themeModelFactory.createCombinedSelector();
        selector.setContext(context);
        selector.setCombinator(combinator);
        selector.setSubject(subject);
        return selector;
    }

    private SelectorSequence createSelectorSequence(String type) {
        return createSelectorSequence(null, type);
    }

    private SelectorSequence createSelectorSequence(String prefix, String type) {
        TypeSelector typeSelector = themeModelFactory.createTypeSelector();
        typeSelector.setType(type);
        typeSelector.setNamespacePrefix(prefix);

        return createSelectorSequence(typeSelector);
    }


    /**
     * Test type selector without a namespace.
     */
    public void testTypeSelector() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MatcherMock typeMatcherMock = new SimpleMatcherMock(
                "typeMatcherMock", expectations);

        final MatcherMock matcherSequenceMock =
                new MatcherMock("matcherSequenceMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new UnorderedExpectations() {
            public void add() {
                expectTypeSelectorWithDefaultNamespace("a", typeMatcherMock,
                        matcherSequenceMock);
            }
        });


        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addElementSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // CSS: a
        SelectorSequence themeSequence = createSelectorSequence("a");

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(themeSequence);

        assertSame("Constructed matcher not as expected",
                   matcherSequenceMock, matcher);
    }

    private void expectTypeSelectorWithDefaultNamespace(
            final String localName, final Matcher typeMatcher,
            final Matcher matcherSequence) {

        factoryMock.expects.createLocalNameMatcher(localName)
                .returns(typeMatcher);
        factoryMock.expects.createDefaultNamespaceMatcher()
                .returns(defaultNamespaceMatcherMock);
        factoryMock.expects.createMatcherSequence(
                Arrays.asList(new Matcher[]{
                    defaultNamespaceMatcherMock,
                    typeMatcher,
                })).returns(matcherSequence);
    }

    /**
     * Test type selector without a namespace.
     */
    public void testTypeSelectorWithNamespace() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MatcherMock typeMatcherMock = new SimpleMatcherMock(
                "typeMatcherMock", expectations);

        final MatcherMock namespaceMatcherMock =
                new SimpleMatcherMock("namespaceMatcherMock", expectations);

        final MatcherMock matcherSequenceMock =
                new MatcherMock("matcherSequenceMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new UnorderedExpectations() {
            public void add() {
                factoryMock.expects.createLocalNameMatcher("a")
                        .returns(typeMatcherMock);
                factoryMock.expects.createNamespaceMatcher("cdm")
                        .returns(namespaceMatcherMock);
                factoryMock.expects.createMatcherSequence(
                        Arrays.asList(new Matcher[]{
                            namespaceMatcherMock,
                            typeMatcherMock,
                        })).returns(matcherSequenceMock);
            }
        });

        // Set the expectations on the specificity calculator.
        specificityCalculatorMock.expects.addElementSelector();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // CSS: a
        SelectorSequence themeSequence = createSelectorSequence("cdm", "a");

        MatcherBuilder builder = createMatcherBuilder();
        Matcher matcher = builder.getMatcher(themeSequence);

        assertSame("Constructed matcher not as expected",
                   matcherSequenceMock, matcher);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 05-Sep-05	9407/6	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/4	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
