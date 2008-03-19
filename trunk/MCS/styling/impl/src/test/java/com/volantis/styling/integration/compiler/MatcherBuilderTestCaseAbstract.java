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
import com.volantis.mcs.themes.ElementSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.styling.PseudoStyleEntitiesMock;
import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.compiler.SpecificityCalculatorMock;
import com.volantis.styling.impl.compiler.MatcherBuilder;
import com.volantis.styling.impl.compiler.MatcherBuilderConfiguration;
import com.volantis.styling.impl.compiler.MatcherBuilderImpl;
import com.volantis.styling.impl.compiler.MatcherFactoryMock;
import com.volantis.styling.impl.compiler.SpecificityCalculatorImpl;
import com.volantis.styling.impl.engine.matchers.MatcherBuilderContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherMock;
import com.volantis.styling.impl.engine.matchers.SimpleMatcherMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Iterator;
import java.util.List;

public abstract class MatcherBuilderTestCaseAbstract
        extends TestCaseAbstract {

    protected ExpectationBuilder expectations;
    protected MatcherFactoryMock factoryMock;
    protected PseudoStyleEntitiesMock pseudoEntityProviderMock;
    protected MatcherMock universalMock;
    protected SpecificityCalculatorMock specificityCalculatorMock;
    private Specificity specificity;
    protected ExpectationBuilder specificityExpectations;
    protected MatcherBuilderContextMock matcherBuilderContextMock;
    protected StyleSheetFactory themeModelFactory;

    protected void setupFactoryFixture() {

    }

    protected void setUp() throws Exception {
        super.setUp();

        // Create an expectation builder.
        expectations = mockFactory.createOrderedBuilder();

        // Create a mock factory.
        factoryMock = new MatcherFactoryMock("factory", expectations);

        // Create a mock universal matcher.
        universalMock = new SimpleMatcherMock("universal", expectations);

        // Create a mock pseudo entity provider.
        pseudoEntityProviderMock = new PseudoStyleEntitiesMock(
                "pseudoEntityProvider", expectations);

        matcherBuilderContextMock = new MatcherBuilderContextMock(
                "matcherBuilderContext", expectations);

        // Create a set of expectations for the specificity calculator as their
        // order is irrelevant, and there are no interactions between it and
        // the other mock objects being used.
        specificityExpectations = mockFactory.createUnorderedBuilder();

        // Create a mock specificity calculator.
        specificityCalculatorMock = new SpecificityCalculatorMock(
                "specificityCalculator", specificityExpectations);

        // Get the specificity to return.
        specificity = new SpecificityCalculatorImpl().getSpecificity();

        // Expect the calculator to be reset, and returns the
        // specificity. This should really make sure that these happen in the
        // correct order.
        specificityCalculatorMock.expects.reset();
        specificityCalculatorMock.expects.getSpecificity().returns(specificity);

        themeModelFactory = StyleSheetFactory.getDefaultInstance();
    }

    protected SelectorSequence createSelectorSequence(
            Selector selector) {

        SelectorSequence sequence =
                themeModelFactory.createSelectorSequence();

        sequence.addSelector(selector);
        return sequence;
    }

    protected SelectorSequence createSelectorSequence(List selectors) {

        SelectorSequence sequence =
                themeModelFactory.createSelectorSequence();

        for (Iterator i = selectors.iterator(); i.hasNext();) {
            Selector selector = (Selector) i.next();

            if (selector instanceof ElementSelector ||
                    selector instanceof PseudoElementSelector ||
                    selector instanceof AttributeSelector ||
                    selector instanceof ClassSelector ||
                    selector instanceof PseudoClassSelector) {
                sequence.addSelector(selector);
            } else {
                throw new IllegalArgumentException(
                        "Unknown selector type " + selector);
            }
        }

        return sequence;
    }

    protected MatcherBuilder createMatcherBuilder() {

        MatcherBuilderConfiguration configuration =
                new MatcherBuilderConfiguration();

        configuration.setFactory(factoryMock);
        configuration.setEntities(pseudoEntityProviderMock);
        configuration.setSpecificityCalculator(specificityCalculatorMock);
        configuration.setBuilderContext(matcherBuilderContextMock);

        return new MatcherBuilderImpl(configuration);
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

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/8	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/6	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 13-Jun-05	8552/3	pabbott	VBM:2005051902 An Eclipse editor fix

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
