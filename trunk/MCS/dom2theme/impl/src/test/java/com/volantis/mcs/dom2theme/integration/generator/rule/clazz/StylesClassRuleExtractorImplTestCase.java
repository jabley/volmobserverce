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
package com.volantis.mcs.dom2theme.integration.generator.rule.clazz;

import com.volantis.mcs.dom2theme.impl.generator.rule.builder.RuleBuilderFactoryMock;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.RuleBuilderMock;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.SimpleSelectorSequenceBuilderMock;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.StylesClassRuleExtractorImpl;
import com.volantis.mcs.dom2theme.impl.model.OutputStylesMock;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathIteratee;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathMock;
import com.volantis.mcs.dom2theme.unit.model.OutputStylesIterateMethodAction;
import com.volantis.mcs.themes.RuleMock;
import com.volantis.mcs.themes.StyleSheetMock;
import com.volantis.mcs.themes.MutableStylePropertiesMock;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class StylesClassRuleExtractorImplTestCase extends TestCaseAbstract {

    public void test() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        RuleBuilderFactoryMock ruleBuilderFactoryMock = new RuleBuilderFactoryMock(
                "rule builder factory", expectations);

        RuleBuilderMock ruleBuilderMock = new RuleBuilderMock(
                "rule builder", expectations);

        SimpleSelectorSequenceBuilderMock sequenceBuilderMock =
                new SimpleSelectorSequenceBuilderMock("sequence builder",
                        expectations);

        StyleSheetMock styleSheetMock = new StyleSheetMock("style sheet",
                expectations);

        OutputStylesMock outputStylesMock = new OutputStylesMock(
                "output styles", expectations);

        final PseudoStylePathMock pseudoStylePathMock = new PseudoStylePathMock(
                "path", expectations);

        MutableStylePropertiesMock propertiesMock =
                new MutableStylePropertiesMock("properties", expectations);

        RuleMock ruleMock = new RuleMock("rule", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // Create an action which fakes the iteration.
        MethodAction action = new OutputStylesIterateMethodAction() {
            public void iterate(PseudoStylePathIteratee iteratee) {
                iteratee.next(pseudoStylePathMock);
            }
        };
        outputStylesMock.fuzzy.iterate(mockFactory.expectsInstanceOf(
                PseudoStylePathIteratee.class)).does(action);

        // Create a rule with the provided class selector and pseudo selectors
        // from the iteratee.

        ruleBuilderFactoryMock.expects.createRuleBuilder().returns(
                ruleBuilderMock);

        ruleBuilderFactoryMock.expects.createSequenceBuilder().returns(
                sequenceBuilderMock);

        sequenceBuilderMock.expects.addClassSelector("classname");

        sequenceBuilderMock.expects.addPseudoSelectors(pseudoStylePathMock);

        ruleBuilderMock.expects.addSequence(sequenceBuilderMock);

        // Copy the properties for the pseudo selectors from the styles.

        outputStylesMock.expects.getPathProperties(pseudoStylePathMock).
                returns(propertiesMock);

        ruleBuilderMock.expects.setProperties(propertiesMock);

        // Extract the rule from the builder and add it to the stylesheet.

        ruleBuilderMock.expects.getRule().returns(ruleMock);

        styleSheetMock.expects.addRule(ruleMock);

        // ==================================================================
        // Do the test.
        // ==================================================================

        StylesClassRuleExtractorImpl stylesClassRuleExtractor =
                new StylesClassRuleExtractorImpl(ruleBuilderFactoryMock,
                        styleSheetMock);

        stylesClassRuleExtractor.extractClassRules(outputStylesMock,
                "classname");

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 18-Jul-05	8668/2	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
