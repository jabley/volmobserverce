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
package com.volantis.mcs.dom2theme.unit.generator.rule.clazz;

import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.ClassRuleExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.StylesClassRuleExtractorFactoryMock;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.StylesClassRuleExtractorMock;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementListMock;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementMock;
import com.volantis.mcs.dom2theme.impl.model.OutputStylesMock;
import com.volantis.mcs.dom2theme.unit.model.OutputStyledElementsIterateMethodAction;
import com.volantis.mcs.themes.StyleSheetMock;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link ClassRuleExtractor}.
 */
public class ClassRuleExtractorTestCase extends TestCaseAbstract {

    private StylesClassRuleExtractorFactoryMock stylesClassRuleExtractorFactoryMock;

    private StylesClassRuleExtractorMock stylesClassRuleExtractorMock;

    private OutputStyledElementListMock outputElementsMock;

    private StyleSheetMock styleSheetMock;

    private ElementMock elementMock1;

    private OutputStyledElementMock outputElementMock1;

    private OutputStylesMock outputStylesMock1;

    private ElementMock elementMock2;

    private OutputStyledElementMock outputElementMock2;

    private OutputStylesMock outputStylesMock2;

    protected void setUp() throws Exception {

        super.setUp();

        // ==================================================================
        // Create mocks.
        // ==================================================================

        stylesClassRuleExtractorFactoryMock =
                new StylesClassRuleExtractorFactoryMock("extractor factory",
                        expectations);

        stylesClassRuleExtractorMock =
                new StylesClassRuleExtractorMock("extractor", expectations);

        outputElementsMock = new OutputStyledElementListMock("elements", expectations);

        styleSheetMock = new StyleSheetMock("style sheet", expectations);

        elementMock1 = new ElementMock("element", expectations);

        elementMock2 = new ElementMock("element 2", expectations);

        outputElementMock1 = new OutputStyledElementMock(
                "output element", expectations, elementMock1, null);

        outputElementMock2 = new OutputStyledElementMock(
                "output element 2", expectations, elementMock2, null);

        outputStylesMock1 = new OutputStylesMock(
                "output styles 1", expectations);

        outputStylesMock2 = new OutputStylesMock(
                "output styles 2", expectations);

        // The output styles are not empty.
        outputStylesMock1.expects.isEmpty().returns(false).any();
        outputStylesMock2.expects.isEmpty().returns(false).any();
    }

    public void testTwoElementsSameStyles() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // Debug logging calls getName so allow that to be called as required.

        outputElementMock1.expects.getName().returns("output element 1").any();

        outputElementMock2.expects.getName().returns("output element 2").any();

        // Create an action which fakes the iteration of two elements.
        MethodAction action = new OutputStyledElementsIterateMethodAction() {
            public void iterate(OutputStyledElementIteratee iteratee) {
                assertEquals(IterationAction.CONTINUE,
                        iteratee.next(outputElementMock1));
                assertEquals(IterationAction.CONTINUE,
                        iteratee.next(outputElementMock2));
            }
        };

        stylesClassRuleExtractorFactoryMock.expects.create(
                styleSheetMock).returns(stylesClassRuleExtractorMock);

        outputElementsMock.fuzzy.iterate(mockFactory.expectsInstanceOf(
                OutputStyledElementIteratee.class)).does(action);

        // Create new class for c1.

        outputElementMock1.expects.getStyles().returns(outputStylesMock1);

        stylesClassRuleExtractorMock.expects.extractClassRules(
                outputStylesMock1, "c1");

        outputElementMock1.expects.setClass("c1");

        outputElementMock1.expects.clearStyles();

        // Re-use class c1.

        outputElementMock2.expects.getStyles().returns(outputStylesMock1);

        outputElementMock2.expects.setClass("c1");

        outputElementMock2.expects.clearStyles();

        // ==================================================================
        // Do the test.
        // ==================================================================

        ClassRuleExtractor extractor = new ClassRuleExtractor(
                stylesClassRuleExtractorFactoryMock);

        extractor.extractRules(outputElementsMock, styleSheetMock);
    }

    public void testTwoElementsDifferentStyles() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // Debug logging calls getName so allow that to be called as required.

        outputElementMock1.expects.getName().returns("output element 1").any();

        outputElementMock2.expects.getName().returns("output element 2").any();

        // Create an action which fakes the iteration of two elements.
        MethodAction action = new OutputStyledElementsIterateMethodAction() {
            public void iterate(OutputStyledElementIteratee iteratee) {
                assertEquals(IterationAction.CONTINUE,
                        iteratee.next(outputElementMock1));
                assertEquals(IterationAction.CONTINUE,
                    iteratee.next(outputElementMock2));
            }
        };

        stylesClassRuleExtractorFactoryMock.expects.create(
                styleSheetMock).returns(stylesClassRuleExtractorMock);

        outputElementsMock.fuzzy.iterate(mockFactory.expectsInstanceOf(
                OutputStyledElementIteratee.class)).does(action);

        // Create a new class for c1.

        outputElementMock1.expects.getStyles().returns(outputStylesMock1);

        stylesClassRuleExtractorMock.expects.extractClassRules(
                outputStylesMock1, "c1");

        outputElementMock1.expects.setClass("c1");

        outputElementMock1.expects.clearStyles();

        // Create a new class for c2.

        outputElementMock2.expects.getStyles().returns(outputStylesMock2);

        stylesClassRuleExtractorMock.expects.extractClassRules(
                outputStylesMock2, "c2");

        outputElementMock2.expects.setClass("c2");

        outputElementMock2.expects.clearStyles();

        // ==================================================================
        // Do the test.
        // ==================================================================

        ClassRuleExtractor extractor = new ClassRuleExtractor(
                stylesClassRuleExtractorFactoryMock);

        extractor.extractRules(outputElementsMock, styleSheetMock);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 19-Jul-05	8668/5	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/3	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
