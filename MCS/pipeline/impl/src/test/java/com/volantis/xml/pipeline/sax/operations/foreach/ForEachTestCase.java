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

package com.volantis.xml.pipeline.sax.operations.foreach;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;

/**
 * Test {@link ForEachRule}.
 */
public class ForEachTestCase
        extends PipelineTestAbstract {

    /**
     * Register a function to create sequences.
     *
     * @param expressionContext The context into which functions should be
     *                          registered.
     */
    protected void registerExpressionFunctions(
            ExpressionContext expressionContext) {
        super.registerExpressionFunctions(expressionContext);

        expressionContext.registerFunction(
                new ImmutableExpandedName("", "sequence"),
                new SequenceBuilderFunction());
    }

    /**
     * Override the configuration creation method to register the
     * {@link ForEachRule} in the pipeline configuration.
     *
     * @return The newly created pipeline configuration.
     */
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        XMLPipelineConfiguration config = super.createPipelineConfiguration();

        DynamicProcessConfiguration dynamicConfig =
                (DynamicProcessConfiguration)
                config.retrieveConfiguration(DynamicProcessConfiguration.class);

        NamespaceRuleSet namespaceRules =
                dynamicConfig.getNamespaceRules(
                        Namespace.PIPELINE.getURI(),
                        true);
        namespaceRules.addRule("for-each", new ForEachRule());
        return config;
    }

    /**
     * Test that an empty sequence works.
     *
     * @throws Exception
     */
    public void testEmptySequence() throws Exception {
        doTest(new TestPipelineFactory(),
                "empty.input.xml", "empty.expected.xml");
    }

    /**
     * Test that a single item works.
     *
     * @throws Exception
     */
    public void testSingleItem() throws Exception {
        doTest(new TestPipelineFactory(),
                "single.input.xml", "single.expected.xml");
    }

    /**
     * Test that multiple items work.
     *
     * @throws Exception
     */
    public void testMultipleItems() throws Exception {
        doTest(new TestPipelineFactory(),
                "multiple.input.xml", "multiple.expected.xml");
    }

    /**
     * Test that nested for-each work.
     *
     * @throws Exception
     */
    public void testNestedItems() throws Exception {
        doTest(new TestPipelineFactory(),
                "nested.input.xml", "nested.expected.xml");
    }

    /**
     * Ensure that nested for-each works when the nested for-each has an empty
     * sequence and therefore invokes flow control.
     *
     * @throws Exception
     */
    public void testNestedEmptyItems() throws Exception {
        doTest(new TestPipelineFactory(),
                "nested-empty.input.xml", "nested-empty.expected.xml");
    }

    /**
     * Test that nested for-each that use shadowed variables work.
     *
     * @throws Exception
     */
    public void testShadowedVariables() throws Exception {
        doTest(new TestPipelineFactory(),
                "shadowed.input.xml", "shadowed.expected.xml");
    }
}
