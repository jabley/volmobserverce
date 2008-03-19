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
package com.volantis.mcs.dom2theme.integration.generator.rule.builder;

import com.volantis.mcs.dom2theme.impl.generator.rule.builder.RuleBuilder;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.SimpleSelectorSequenceBuilderMock;
import com.volantis.mcs.themes.MutableStylePropertiesMock;
import com.volantis.mcs.themes.SelectorSequenceMock;
import com.volantis.mcs.themes.Rule;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link RuleBuilder}.
 */
public class RuleBuilderTestCase extends TestCaseAbstract {

    /**
     * Do the simplest possible complete simple test, i.e. that we can build a
     * rule containing a single selector sequence and a properties.
     */
    public void test() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        SimpleSelectorSequenceBuilderMock sequenceBuilderMock
                = new SimpleSelectorSequenceBuilderMock(
                        "sequence builder", expectations);

        SelectorSequenceMock sequenceMock =
                new SelectorSequenceMock("sequence", expectations);

        MutableStylePropertiesMock stylePropertiesMock =
                new MutableStylePropertiesMock("properties", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================


        sequenceBuilderMock.expects.getSequence().returns(sequenceMock);

        // ==================================================================
        // Do the test.
        // ==================================================================

        RuleBuilder ruleBuilder = new RuleBuilder();
        ruleBuilder.addSequence(sequenceBuilderMock);
        ruleBuilder.setProperties(stylePropertiesMock);
        Rule rule = ruleBuilder.getRule();

        assertEquals("", rule.getSelectors().size(), 1);

        assertEquals("", rule.getSelectors().get(0), sequenceMock);

        assertEquals("", rule.getProperties(), stylePropertiesMock);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 31-Aug-05	9409/1	geoff	VBM:2005083007 Move over to using the new themes model.

 18-Jul-05	8668/2	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
