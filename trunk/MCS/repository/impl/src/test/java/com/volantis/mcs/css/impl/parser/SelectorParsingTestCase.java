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

package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.themes.AttributeSelectorActionEnum;
import com.volantis.mcs.themes.AttributeSelectorMock;
import com.volantis.mcs.themes.ClassSelectorMock;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelectorMock;
import com.volantis.mcs.themes.IdSelectorMock;
import com.volantis.mcs.themes.NthChildSelectorMock;
import com.volantis.mcs.themes.PseudoClassSelectorMock;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.PseudoElementSelectorMock;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorMock;
import com.volantis.mcs.themes.SelectorSequenceMock;
import com.volantis.mcs.themes.TypeSelectorMock;
import com.volantis.mcs.themes.UniversalSelectorMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test that selectors are parsed properly.
 */
public class SelectorParsingTestCase
        extends ParsingSelectorMockTestCaseAbstract {

    private TypeSelectorMock typeSelectorMock;
    private SelectorSequenceMock selectorSequenceMock;
    private UniversalSelectorMock universalSelectorMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        typeSelectorMock = new TypeSelectorMock(
                "typeSelectorMock", expectations);

        selectorSequenceMock = new SelectorSequenceMock(
                "selectorSequenceMock", expectations);

        universalSelectorMock = new UniversalSelectorMock(
                "universalSelectorMock", expectations);
    }

    protected void doRoundTripTestSelector(String inputSelector,
                                           String expectedSelector)
            throws Exception {

        doRoundTripTest(inputSelector + " {color: green}",
                        expectedSelector + "{color:green}");

        doRoundTripTest("element " + inputSelector + " {color: green}",
                        "element " + expectedSelector + "{color:green}");
    }

    protected void doRoundTripTestSelector(String selector)
            throws Exception {

        doRoundTripTestSelector(selector, selector);
    }

    /**
     * Test that an type selector can be parsed.
     */
    public void testTypeSelector()
        throws Exception {

        expectSingleTypeSelector(null, "element");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("element");
    }

    private void expectSingleTypeSelector(
            final String namespace, final String type) {
        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createTypeSelector(namespace, type)
                .returns(typeSelectorMock);

        expectSingleSelector(typeSelectorMock);
    }

    private void expectSingleSelector(final SelectorMock selectorMock) {

        List selectors = new ArrayList();
        selectors.add(selectorMock);

        styleSheetFactoryMock.expects
                .createSelectorSequence(selectors)
                .returns(selectorSequenceMock);
    }

    /**
     * Test that an type selector with namespace can be parsed.
     */
    public void testTypeSelectorWithNamespace()
        throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectSingleTypeSelector("ns", "element");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("ns|element");
    }

    /**
     * Test that an type selector with any namespace can be parsed.
     */
    public void testTypeSelectorWithAnyNamespace()
        throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectSingleTypeSelector("*", "element");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("*|element");
    }

    /**
     * Test that an type selector with no namespace can be parsed.
     */
    public void testTypeSelectorWithNoNamespace()
        throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectSingleTypeSelector("", "element");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("|element");
    }

    /**
     * Test that an universal selector can be parsed.
     */
    public void testUniversalSelector()
        throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectSingleUniversalSelector(null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("*");
    }

    private void expectSingleUniversalSelector(final String namespace) {

        styleSheetFactoryMock.expects
                .createUniversalSelector(namespace)
                .returns(universalSelectorMock);

        expectSingleSelector(universalSelectorMock);
    }

    /**
     * Test that an universal selector with namespace can be parsed.
     */
    public void testUniversalSelectorWithNamespace()
        throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectSingleUniversalSelector("ns");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("ns|*");
    }

    /**
     * Test that an universal selector with namespace can be parsed.
     */
    public void testUniversalSelectorWithAnyNamespace()
        throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectSingleUniversalSelector("*");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("*|*");
    }

    /**
     * Test that an universal selector with no namespace can be parsed.
     */
    public void testUniversalSelectorWithNoNamespace()
        throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectSingleUniversalSelector("");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("|*");
    }

    /**
     * Test that an class selector can be parsed.
     */
    public void testClassSelector()
        throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ClassSelectorMock classSelectorMock =
                new ClassSelectorMock("classSelectorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createClassSelector("class")
                .returns(classSelectorMock);

        expectSingleSelector(classSelectorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence(".class");
    }

    /**
     * Test that an id selector can be parsed.
     */
    public void testIdSelector()
        throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final IdSelectorMock idSelectorMock =
                new IdSelectorMock("idSelectorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createIdSelector("id")
                .returns(idSelectorMock);

        expectSingleSelector(idSelectorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("#id");
    }

    /**
     * Test that an attribute exists selector can be parsed.
     */
    public void testAttributeExistsSelector()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.SET, null, "attr", null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[attr]");
    }

    private void expectSingleAttributeSelector(
            final AttributeSelectorActionEnum action, final String namespacePrefix,
            final String attributeName,
            final String value) {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final AttributeSelectorMock attributeSelectorMock =
                new AttributeSelectorMock("attributeSelectorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createAttributeSelector()
                .returns(attributeSelectorMock);

        expectSingleSelector(attributeSelectorMock);

        attributeSelectorMock.expects.setConstraint(action, value);
        attributeSelectorMock.expects.setNamespacePrefix(namespacePrefix);
        attributeSelectorMock.expects.setName(attributeName);
    }

    private void parseSingleSelectorSequence(final String css) {
        Selector selector = parseSelector(css);

        assertEquals("Selector must match", selectorSequenceMock, selector);
    }

    /**
     * Test that an attribute equals selector can be parsed.
     */
    public void testAttributeEqualsSelector()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.EQUALS, null, "attr", "xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[attr=\"xyz\"]");
    }

    /**
     * Test that an attribute contains word selector can be parsed.
     */
    public void testAttributeContainsWordSelector()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.CONTAINS_WORD, null, "attr", "xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[attr~=\"xyz\"]");
    }

    /**
     * Test that an attribute contains selector can be parsed.
     */
    public void testAttributeContainsSelector()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.CONTAINS, null, "attr", "xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[attr*=\"xyz\"]");
    }

    /**
     * Test that an attribute starts with selector can be parsed.
     */
    public void testAttributeStartsWithSelector()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.STARTS_WITH, null, "attr", "xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[attr^=\"xyz\"]");
    }

    /**
     * Test that an attribute ends with selector can be parsed.
     */
    public void testAttributeEndsWithSelector()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.ENDS_WITH, null, "attr", "xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[attr$=\"xyz\"]");
    }

    /**
     * Test that an attribute language match selector can be parsed.
     */
    public void testAttributeLanguageMatchSelector()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.LANGUAGE_MATCH, null, "attr", "xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[attr|=\"xyz\"]");
    }

    /**
     * Test that an attribute selector with a namespace can be parsed.
     */
    public void testAttributeSelectorWithNamespace()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.SET, "ns", "attr", null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[ns|attr]");
    }

    /**
     * Test that an attribute selector with any namespace can be parsed.
     */
    public void testAttributeSelectorWithAnyNamespace()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.SET, "*", "attr", null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[*|attr]");
    }

    /**
     * Test that an attribute selector with no namespace can be parsed.
     */
    public void testAttributeSelectorWithNoNamespace()
        throws Exception {

        expectSingleAttributeSelector(
                AttributeSelectorActionEnum.SET, "", "attr", null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("[|attr]");
    }

    /**
     * Test that a first-child pseudo class selector can be parsed.
     */
    public void testFirstChildPseudoClassSelector()
        throws Exception {

        doTestPseudoClass(PseudoClassTypeEnum.FIRST_CHILD, ":first-child");
    }

    private void doTestPseudoClass(
            final PseudoClassTypeEnum type, final String css) {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PseudoClassSelectorMock pseudoClassSelectorMock =
                new PseudoClassSelectorMock("pseudoClassSelectorMock",
                                            expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createPseudoClassSelector(type.getType())
                .returns(pseudoClassSelectorMock);

        pseudoClassSelectorMock.expects.getPseudoClassType().returns(type);

        expectSingleSelector(pseudoClassSelectorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence(css);
    }

    /**
     * Test that an nth-child pseudo class selector can be parsed.
     */
    public void testNthChildPseudoClassSelector()
        throws Exception {

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int a = 2 * i;
                int b = 2 * j;
                final StringBuffer argument = new StringBuffer();

                if (a != 0) {
                    argument.append(a).append("n");
                }
                if (b > 0) {
                    argument.append("+").append(b);
                } else if (b < 0) {
                    argument.append(b);
                }

                if (argument.length() > 0) {
                    doTestNthChildPseudoClassSelector(argument.toString());
                } else {
                    // assert that it fails
                    try {
                        parseSingleSelectorSequence(":nth-child(" +
                                argument + ")");
                        fail("Empty strings are not allowed as the " +
                                "nth-child argument");
                    } catch (IllegalStateException e) {
                        // do nothing - correct behaviour
                    }
                }
            }
        }
    }

    /**
     * Test that an nth-child pseudo class selector can be parsed.
     */
    public void doTestNthChildPseudoClassSelector(String argument)
        throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final NthChildSelectorMock nthChildSelectorMock =
                new NthChildSelectorMock("nthChildSelectorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects.createNthChildSelector(argument)
                .returns(nthChildSelectorMock);

        expectSingleSelector(nthChildSelectorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence(":nth-child(" + argument + ")");
    }

    /**
     * Test that an nth-child(odd) pseudo class selector can be parsed.
     */
    public void testNthChildOddPseudoClassSelector()
        throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final NthChildSelectorMock nthChildSelectorMock =
                new NthChildSelectorMock("nthChildSelectorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createNthChildSelector("odd")
                .returns(nthChildSelectorMock);

        expectSingleSelector(nthChildSelectorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence(":nth-child(odd)");
    }

    /**
     * Test that an nth-child(even) pseudo class selector can be parsed.
     */
    public void testNthChildEvenPseudoClassSelector()
        throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final NthChildSelectorMock nthChildSelectorMock =
                new NthChildSelectorMock("nthChildSelectorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createNthChildSelector("even")
                .returns(nthChildSelectorMock);

        expectSingleSelector(nthChildSelectorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence(":nth-child(even)");
    }

    /**
     * Test that an nth-child(invalid) pseudo class selector cannot be parsed.
     */
    public void testNthChildInvalidPseudoClassSelector()
        throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final NthChildSelectorMock nthChildSelectorMock =
                new NthChildSelectorMock("nthChildSelectorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createNthChildSelector("invalid")
                .returns(nthChildSelectorMock);

        expectSingleSelector(nthChildSelectorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence(":nth-child(invalid)");
    }

    /**
     * Test that a link pseudo class selector can be parsed.
     */
    public void testLinkPseudoClassSelector()
        throws Exception {

        doTestPseudoClass(PseudoClassTypeEnum.LINK, ":link");
    }

    /**
     * Test that a visited pseudo class selector can be parsed.
     */
    public void testVisitedPseudoClassSelector()
        throws Exception {

        doTestPseudoClass(PseudoClassTypeEnum.VISITED, ":visited");
    }

    /**
     * Test that a active pseudo class selector can be parsed.
     */
    public void testActivePseudoClassSelector()
        throws Exception {

        doTestPseudoClass(PseudoClassTypeEnum.ACTIVE, ":active");
    }

    /**
     * Test that a focus pseudo class selector can be parsed.
     */
    public void testFocusPseudoClassSelector()
        throws Exception {

        doTestPseudoClass(PseudoClassTypeEnum.FOCUS, ":focus");
    }

    /**
     * Test that a hover pseudo class selector can be parsed.
     */
    public void testHoverPseudoClassSelector()
        throws Exception {

        doTestPseudoClass(PseudoClassTypeEnum.HOVER, ":hover");
    }

    private void doTestPseudoElement(
            final PseudoElementTypeEnum type, final String css) {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PseudoElementSelectorMock pseudoElementSelectorMock =
                new PseudoElementSelectorMock("pseudoElementSelectorMock",
                                              expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createPseudoElementSelector(type.getType())
                .returns(pseudoElementSelectorMock);

        pseudoElementSelectorMock.expects.getPseudoElementType().returns(type);

        expectSingleSelector(pseudoElementSelectorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence(css);
    }


    /**
     * Test that a first-line pseudo element selector can be parsed.
     *
     * <p>This supports both the old and new syntax for pseudo elements,
     * i.e. ":" and "::" respectively.</p>
     */
    public void testFirstLinePseudoElementSelector()
        throws Exception {

        doTestPseudoElement(PseudoElementTypeEnum.FIRST_LINE, ":first-line");
        doTestPseudoElement(PseudoElementTypeEnum.FIRST_LINE, "::first-line");
    }

    /**
     * Test that a first-letter pseudo element selector can be parsed.
     *
     * <p>This supports both the old and new syntax for pseudo elements,
     * i.e. ":" and "::" respectively.</p>
     */
    public void testFirstLetterPseudoElementSelector()
        throws Exception {

        doTestPseudoElement(PseudoElementTypeEnum.FIRST_LETTER, ":first-letter");
        doTestPseudoElement(PseudoElementTypeEnum.FIRST_LETTER, "::first-letter");
    }

    /**
     * Test that a before pseudo element selector can be parsed.
     */
    public void testBeforePseudoElementSelector()
        throws Exception {

        doTestPseudoElement(PseudoElementTypeEnum.BEFORE, "::before");
    }

    /**
     * Test that an after pseudo element selector can be parsed.
     */
    public void testAfterPseudoElementSelector()
        throws Exception {

        doTestPseudoElement(PseudoElementTypeEnum.AFTER, "::after");
    }

    /**
     * Test that a marker pseudo element selector can be parsed.
     */
    public void testMarkerPseudoElementSelector()
        throws Exception {

        doTestPseudoElement(PseudoElementTypeEnum.MARKER, "::marker");
    }

    /**
     * Test that an mcs-shortcut pseudo element selector can be parsed.
     */
    public void testMCSShortcutPseudoElementSelector()
        throws Exception {

        doTestPseudoElement(PseudoElementTypeEnum.MCS_SHORTCUT, "::mcs-shortcut");
    }

    /**
     * Test that an mcs-shortcut after pseudo element selector can be parsed.
     */
    public void testMCSShortcutAfterPseudoElementSelector()
        throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PseudoElementSelectorMock mcsShortcutSelectorMock =
                new PseudoElementSelectorMock("mcsShortcutSelectorMock",
                                              expectations);

        final PseudoElementSelectorMock afterShortcutSelectorMock =
                new PseudoElementSelectorMock("afterShortcutSelectorMock",
                                              expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects.createPseudoElementSelector(
                PseudoElementTypeEnum.MCS_SHORTCUT.getType()).returns(
                        mcsShortcutSelectorMock);

        styleSheetFactoryMock.expects.createPseudoElementSelector(
                PseudoElementTypeEnum.AFTER.getType()).returns(
                        afterShortcutSelectorMock);

        List selectors = new ArrayList();
        selectors.add(mcsShortcutSelectorMock);
        selectors.add(afterShortcutSelectorMock);

        styleSheetFactoryMock.expects
                .createSelectorSequence(selectors)
                .returns(selectorSequenceMock);
        mcsShortcutSelectorMock.expects.getPseudoElementType().returns(
                PseudoElementTypeEnum.MCS_SHORTCUT);
        afterShortcutSelectorMock.expects.getPseudoElementType().returns(
                PseudoElementTypeEnum.AFTER);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        parseSingleSelectorSequence("::mcs-shortcut::after");
    }

    /**
     * Test that a selector group can be parsed.
     */
    public void testSelectorGroup()
        throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final TypeSelectorMock element1SelectorMock =
                new TypeSelectorMock("element1SelectorMock", expectations);

        final TypeSelectorMock element2SelectorMock =
                new TypeSelectorMock("element2SelectorMock", expectations);

        final SelectorSequenceMock subject1Mock =
                new SelectorSequenceMock("subject1Mock",
                                                expectations);

        final SelectorSequenceMock subject2Mock =
                new SelectorSequenceMock("subject2Mock",
                                                expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects
                .createTypeSelector(null, "element1")
                .returns(element1SelectorMock);

        List selector1List = new ArrayList();
        selector1List.add(element1SelectorMock);

        styleSheetFactoryMock.expects
                .createSelectorSequence(selector1List)
                .returns(subject1Mock);

        styleSheetFactoryMock.expects
                .createTypeSelector(null, "element2")
                .returns(element2SelectorMock);

        List selector2List = new ArrayList();
        selector2List.add(element2SelectorMock);

        styleSheetFactoryMock.expects
                .createSelectorSequence(selector2List)
                .returns(subject2Mock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List expectedSelectors = new ArrayList();
        expectedSelectors.add(subject1Mock);
        expectedSelectors.add(subject2Mock);

        List selectors = parseSelectorGroup("element1, element2");
        assertEquals("Selectors", expectedSelectors, selectors);
    }

    /**
     * Test that a descendant selector can be parsed.
     */
    public void testDescendantSelector()
        throws Exception {

        doTestCombined(CombinatorEnum.DESCENDANT, "parent", "child", "parent child");

    }

    private void doTestCombined(
            final CombinatorEnum combinator, final String contextType,
            final String subjectType, final String css) {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final TypeSelectorMock contextSelectorMock =
                new TypeSelectorMock("contextSelectorMock", expectations);

        final SelectorSequenceMock contextSequenceMock =
                new SelectorSequenceMock("contextSequenceMock",
                                                   expectations);

        final TypeSelectorMock subjectSelectorMock =
                new TypeSelectorMock("subjectSelectorMock", expectations);

        final SelectorSequenceMock subjectSequenceMock =
                new SelectorSequenceMock("subjectSequenceMock",
                                                expectations);

        final CombinedSelectorMock combinedSelectorMock =
                new CombinedSelectorMock("combinedSelectorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleSheetFactoryMock.expects.createTypeSelector(null, contextType)
                .returns(contextSelectorMock);

        List contextSelectorList = Arrays.asList(new Object[] {
            contextSelectorMock
        });

        styleSheetFactoryMock.expects
                .createSelectorSequence(contextSelectorList)
                .returns(contextSequenceMock);

        styleSheetFactoryMock.expects.createTypeSelector(null, subjectType)
                .returns(subjectSelectorMock);

        List selectorList = Arrays.asList(new Object[] {
            subjectSelectorMock
        });

        styleSheetFactoryMock.expects
                .createSelectorSequence(selectorList)
                .returns(subjectSequenceMock);

        styleSheetFactoryMock.expects.createCombinedSelector()
                .returns(combinedSelectorMock);

        combinedSelectorMock.expects.setCombinator(combinator);
        combinedSelectorMock.expects.setContext(contextSequenceMock);
        combinedSelectorMock.expects.setSubject(subjectSequenceMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Selector selector = parseSelector(css);

        assertEquals("Selector must match", combinedSelectorMock, selector);
    }

    /**
     * Test that a child selector can be parsed.
     */
    public void testChildSelector()
        throws Exception {

        doTestCombined(CombinatorEnum.CHILD,
                       "parent", "child",
                       "parent > child");
    }

    /**
     * Test that a direct adjacent selector can be parsed.
     */
    public void testDirectAdjacentSelector()
        throws Exception {

        doTestCombined(CombinatorEnum.DIRECT_ADJACENT,
                       "brother", "sister",
                       "brother + sister");
    }

    /**
     * Test that a child selector can be parsed.
     */
    public void testIndirectAdjacentSelector()
        throws Exception {

        doTestCombined(CombinatorEnum.INDIRECT_ADJACENT,
                       "cousin1", "cousin2",
                       "cousin1 ~ cousin2");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/2	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 28-Sep-05	9487/4	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
