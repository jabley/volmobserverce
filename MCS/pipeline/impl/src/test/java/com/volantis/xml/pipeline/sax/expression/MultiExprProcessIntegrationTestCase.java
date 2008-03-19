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
package com.volantis.xml.pipeline.sax.expression;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;

/**
 * Integration Test for the <code>ExpressionProcess</code> class.  All tests
 * are carried out using a pipeline capable of dealing with multiple expression
 * declarations.
 */
public class MultiExprProcessIntegrationTestCase
        extends PipelineTestAbstract {

    /**
     * Qualified class name for this class
     */
    private String name;

    /**
     * Creates an new ExpressionProcessIntegrationTestCase instance.
     */
    public MultiExprProcessIntegrationTestCase() {
        String className = getClass().getName();
        this.name = className.substring(className.lastIndexOf('.') + 1);
    }

    /**
     * Test a simple variable expression evaluation
     * @throws Exception if an error occurs
     */
    public void testSimpleVariableProcessing() throws Exception {

        doTest(new IntegrationTestHelper().getMultiExprPipelineFactory(),
               name + ".simpleVar_input.xml",
               name + ".simpleVar_expected.xml");
    }

    /**
     * Test variable reference expressions that are prefixed with a namespace
     * prefix.
     * @throws Exception if an error occurs
     */
    public void testQualifiedVariableProcessing() throws Exception {

        doTest(new IntegrationTestHelper().getMultiExprPipelineFactory(),
               name + ".qualifiedVar_input.xml",
               name + ".qualifiedVar_expected.xml");
    }

    /**
     * Test variable reference expression that belong in different scopes
     * @throws Exception if an error occurs.
     */
    public void testVariableScoping() throws Exception {

        doTest(new IntegrationTestHelper().getMultiExprPipelineFactory(),
               name + ".variableScope_input.xml",
               name + ".variableScope_expected.xml");
    }

    /**
     * Test variable reference expression that belong in different scopes
     * @throws Exception if an error occurs.
     */
    public void testRemovalOfAttributeWhenEmptySequenceExpression()
            throws Exception {

        doTest(new IntegrationTestHelper().getMultiExprPipelineFactory(),
               name + "EmptySequence.input.xml",
               name + "EmptySequence.expected.xml");
    }

    // javadoc inherited
    protected void registerExpressionFunctions(ExpressionContext context) {
        // add a function that returns an empty sequence when invoked
        new IntegrationTestHelper().registerEmptySequenceFunction(context);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Jun-05	8751/1	schaloner	VBM:2005060711 Added AttributeNormalizingContentHandler to the testtools jar file list

 15-Jun-05	8751/2	schaloner	VBM:2005060711 ExpressionProcess and PipelineExpressionHelper can now support multiple expression declaration markup

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Nov-03	438/1	doug	VBM:2003091803 Added parameter value processes

 29-Oct-03	435/2	doug	VBM:2003091902 Expressions that evaluate to an empty sequence are represented with null rather than empty string

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
