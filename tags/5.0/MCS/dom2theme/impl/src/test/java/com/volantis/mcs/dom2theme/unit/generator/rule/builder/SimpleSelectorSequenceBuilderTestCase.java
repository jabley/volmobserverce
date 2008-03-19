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
package com.volantis.mcs.dom2theme.unit.generator.rule.builder;

import com.volantis.mcs.dom2theme.impl.generator.rule.builder.SimpleSelectorSequenceBuilder;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathMock;
import com.volantis.mcs.dom2theme.unit.model.PseudoStylePathAcceptMethodAction;
import com.volantis.mcs.themes.ClassSelectorMock;
import com.volantis.mcs.themes.PseudoClassSelectorMock;
import com.volantis.mcs.themes.PseudoElementSelectorMock;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.SelectorSequenceMock;
import com.volantis.mcs.themes.StyleSheetFactoryMock;
import com.volantis.mcs.themes.TypeSelectorMock;
import com.volantis.styling.PseudoElementMock;
import com.volantis.styling.PseudoStyleEntityVisitor;
import com.volantis.styling.StatefulPseudoClassIteratee;
import com.volantis.styling.StatefulPseudoClassMock;
import com.volantis.styling.StatefulPseudoClassSetMock;
import com.volantis.styling.unit.StatefulPseudoClassSetIterateMethodAction;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link SimpleSelectorSequenceBuilder}.
 */
public class SimpleSelectorSequenceBuilderTestCase
        extends TestCaseAbstract {

    /**
     * Test that we can build the simplest complete simple selector sequence.
     */
    public void test() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        PseudoStylePathMock pseudoStylePathMock = new PseudoStylePathMock(
                "path", expectations);

        final StatefulPseudoClassMock pseudoClassMock =
                new StatefulPseudoClassMock("pseudoClassMock",
                        expectations);

        final StatefulPseudoClassSetMock pseudoClassSetMock =
                new StatefulPseudoClassSetMock(
                        "pseudoClassSetMock", expectations);

        final PseudoElementMock pseudoElementMock = new PseudoElementMock(
                "pseudo element", expectations);

        final StyleSheetFactoryMock styleSheetFactoryMock =
                new StyleSheetFactoryMock("style sheet factory", expectations);

        final SelectorSequenceMock selectorSequenceMock =
                new SelectorSequenceMock("selector sequence",
                        expectations);

        final TypeSelectorMock typeSelectorMock = new TypeSelectorMock(
                "type selector", expectations);

        final ClassSelectorMock classSelectorMock = new ClassSelectorMock(
                "class selector", expectations);

        final PseudoClassSelectorMock pseudoClassSelectorMock =
                new PseudoClassSelectorMock("pseudo class selector",
                        expectations);

        final PseudoElementSelectorMock pseudoElementSelectorMock =
                new PseudoElementSelectorMock("pseudo element selector",
                        expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // stateful pseudo class representation does start with :
        pseudoClassMock.expects.getCSSRepresentation().returns(
                ":link").any();

        // pseudo element representation doesn't start with :
        pseudoElementMock.expects.getCSSRepresentation().returns(
                "first-letter").any();

        // Create the selector sequence.
        styleSheetFactoryMock.expects.createSelectorSequence().returns(
                selectorSequenceMock);

        // Create the type selector.
        styleSheetFactoryMock.expects.createTypeSelector().returns(
                typeSelectorMock);
        typeSelectorMock.expects.setType("type");
        selectorSequenceMock.expects.addSelector(typeSelectorMock);

        // Create the class selector.
        styleSheetFactoryMock.expects.createClassSelector("class").returns(
                classSelectorMock);
        selectorSequenceMock.expects.addSelector(classSelectorMock);

        // Create an action which fakes the mock path accept() method to visit
        // a pseudo class and element.
        MethodAction visitorAction = new PseudoStylePathAcceptMethodAction() {
            public void accept(PseudoStyleEntityVisitor visitor) {
                visitor.visit(pseudoClassSetMock);
                visitor.visit(pseudoElementMock);
            }
        };
        pseudoStylePathMock.fuzzy.accept(mockFactory.expectsInstanceOf(
                PseudoStyleEntityVisitor.class)).does(visitorAction);

        // Create the pseudo class selector.
        styleSheetFactoryMock.expects.createPseudoClassSelector("link").
                returns(pseudoClassSelectorMock);
        selectorSequenceMock.expects.addSelector(pseudoClassSelectorMock);

        // Create an action which fakes the mock stateful pseudo class iterate()
        // method and pretends to iterate over a single individual pseudo class.
        MethodAction iterateAction = new StatefulPseudoClassSetIterateMethodAction() {
            public void iterate(StatefulPseudoClassIteratee iteratee) {
                iteratee.next(pseudoClassMock);
            }
        };
        pseudoClassSetMock.fuzzy.iterate(mockFactory.expectsInstanceOf(
                StatefulPseudoClassIteratee.class)).does(iterateAction);

        // Create the pseudo element selector.
        styleSheetFactoryMock.expects.createPseudoElementSelector("first-letter").
                returns(pseudoElementSelectorMock);
        selectorSequenceMock.expects.addSelector(pseudoElementSelectorMock);
        // ==================================================================
        // Do the test.
        // ==================================================================

        SimpleSelectorSequenceBuilder builder =
                new SimpleSelectorSequenceBuilder(styleSheetFactoryMock);
        builder.setTypeSelector("type");
        builder.addClassSelector("class");
        builder.addPseudoSelectors(pseudoStylePathMock);
        SelectorSequence sequence = builder.getSequence();

        assertEquals("", selectorSequenceMock, sequence);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 01-Nov-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9409/1	geoff	VBM:2005083007 Move over to using the new themes model.

 18-Jul-05	8668/2	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
