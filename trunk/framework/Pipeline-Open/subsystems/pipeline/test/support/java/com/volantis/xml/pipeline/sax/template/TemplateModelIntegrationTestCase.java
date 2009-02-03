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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.template;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.dependency.TestDependencyFunctions;
import com.volantis.xml.pipeline.sax.impl.dependency.DependencyTestRuleConfigurator;
import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.SAXParseException;

/**
 * Comprehensive integration tests for the template model. Split into two
 * types of test:
 * <ol>
 *   <li>positive testing (where the processing should succeed)</li>
 *   <li>negative testing (where the processing should fail with a known
 *       exception</li>
 * </ol>
 */
public class TemplateModelIntegrationTestCase extends PipelineTestAbstract {
    /**
     * The name of this class, without the package prefix. Used to (prefix)
     * name the input and expected XML files.
     */
    protected String name;

    /**
     * Initialize the new instance using the given parameters.
     */
    public TemplateModelIntegrationTestCase() {

        String className = getClass().getName();

        this.name = className.substring(className.lastIndexOf('.') + 1);
    }

    protected void configureDynamicProcess(
            DynamicProcessConfiguration configuration) {
        super.configureDynamicProcess(configuration);

        DependencyTestRuleConfigurator.getDefaultInstance()
                .configure(configuration);
    }

    protected void registerExpressionFunctions(ExpressionContext context) {
        super.registerExpressionFunctions(context);

        TestDependencyFunctions.FUNCTION_TABLE.registerFunctions(context);
    }

    /**
     * Test the template model with the definition in-line. Note that this
     * test covers:
     * <ul>
     *   <li>immediate mode bindings</li>
     *   <li>deferred mode bindings</li>
     *   <li>simple nested markup bindings with pipeline markup</li>
     *   <li>complex nested markup bindings with pipeline and other markup</li>
     *   <li>value attribute bindings</li>
     *   <li>overridden default attribute parameters</li>
     *   <li>bound required simple parameters</li>
     *   <li>bound required complex parameters</li>
     *   <li>unbound optional complex parameters</li>
     *   <li>simple parameters bound to simple bindings</li>
     *   <li>complex parameters bound to complex bindings</li>
     *   <li>complex parameters bound to simple bindings</li>
     *   <li>repeated mode parameter defaults</li>
     *   <li>use of value</li>
     * </ul>
     */
    public void testInlineDefinition() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".inline_input.xml",
                   name + ".inline_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test the template model with the definition referenced via href. This
     * covers exactly the same points as {@link #testInlineDefinition}.
     */
    public void testIncludeDefinition() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".include_input.xml",
                   name + ".include_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that immediate mode bindings are processed once, immediately,
     * and not again.
     */
    public void testImmediateBinding() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".immediate_input.xml",
                   name + ".immediate_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that repeated mode bindings are processed each time they are used.
     */
    public void testRepeatedBinding() throws Exception {
        try {
                doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".repeated_input.xml",
                   name + ".repeated_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that a mismatch between complexity is detected if a repeated value
     * produces simple content the first time it is processed and complex the
     * next.
     */
    public void testRepeatedErrorBinding() throws Exception {
        try {
                doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".repeated_error_input.xml",
                   name + ".repeated_error_expected.xml");
            fail("Did not detect complexity mismatch");
        } catch(XMLPipelineException e) {
            assertEquals("The 'count' parameter was declared to be simple " +
                    "but complex markup was produced during evaluation",
                    e.getMessage());
        }
    }

    /**
     * Test that deferred mode bindings are processed once, on first use,
     * and not again.
     */
    public void testDeferredBinding() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".deferred_input.xml",
                   name + ".deferred_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that ref bindings actually refer to the underlying binding and
     * that they behave according to the underlying binding's evaluation mode.
     */
    public void testRefBinding() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".ref_binding_input.xml",
                   name + ".ref_binding_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that ref bindings actually refer to the underlying binding and
     * that they behave according to the underlying binding's evaluation mode
     * where the underlying binding itself is a ref to another binding.
     */
    public void testDoubleRefBinding() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".doubleref_binding_input.xml",
                   name + ".doubleref_binding_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that default values for parameters are handled correctly for
     * all the various ways defaults can be supplied.
     */
    public void testDefaultParameters() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".default_params_input.xml",
                   name + ".default_params_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that expression variables can be used to insert parameter values
     * into markup attributes.
     */
    public void testExpressionVariables() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".exp_var_attrs_input.xml",
                   name + ".exp_var_attrs_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that bindings can find the values from their parent templates.
     * This was originally reported as a bug in VBM:2004022507.
     */
    public void testValueBinding() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".value_binding_input.xml",
                   name + ".value_binding_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Tests that two calls to the same template does not cause variable
     * redeclaration exception as reported in VBM:2007032027.
     */
    public void testMultipleCallsHref() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".multiple_calls_inp_href.xml",
                   name + ".multiple_calls_exp_href.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                " (" + e.getLineNumber() + ", " + e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Tests that executing two templates using the same local variable does not
     * cause variable redeclaration exception. 
     */
    public void testMultipleCallsInline() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".multiple_calls_inp_inline.xml",
                   name + ".multiple_calls_exp_inline.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                " (" + e.getLineNumber() + ", " + e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Tests if nested templates can have variables with the same name and the
     * right variable is used when executing expressions.
     */
    public void testMultipleCallsNested() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".multiple_calls_nested_inp.xml",
                   name + ".multiple_calls_nested_exp.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                " (" + e.getLineNumber() + ", " + e.getColumnNumber() + ")", e);
        }
    }

    // NOTE: all the DefaultDuplicate tests below came from Paul.
    
    /**
     * Tests that a template that defines the default value of one parameter 
     * in terms of the value for the other parameter works, where the template
     * apply has no bindings and uses href= rather than inline content. 
     * <p>
     * Note, the order of parameters in the definition is important as a 
     * default value can only reference parameters.
     */
    public void testDefaultDuplicateNoneHref() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".defdup_none_input_href.xml",
                   name + ".defdup_none_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }
    
    /**
     * Tests that a template that defines the default value of one parameter 
     * in terms of the value for the other parameter works, where the template
     * apply has no bindings and uses inline content rather than href=. 
     * <p>
     * Note, the order of parameters in the definition is important as a 
     * default value can only reference parameters.
     */
    public void testDefaultDuplicateNoneInline() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".defdup_none_input_inline.xml",
                   name + ".defdup_none_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Tests that a template that defines the default value of one parameter 
     * in terms of the value for the other parameter works, where the template
     * apply has a binding for the first parameter and uses href= rather than 
     * inline content. 
     * <p>
     * Note, the order of parameters in the definition is important as a 
     * default value can only reference parameters.
     */
    public void testDefaultDuplicateFirstHref() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".defdup_first_input_href.xml",
                   name + ".defdup_first_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }
    
    /**
     * Tests that a template that defines the default value of one parameter 
     * in terms of the value for the other parameter works, where the template
     * apply has a binding for the first parameter and uses inline content 
     * rather than href=. 
     * <p>
     * Note, the order of parameters in the definition is important as a 
     * default value can only reference parameters.
     */
    public void testDefaultDuplicateFirstInline() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".defdup_first_input_inline.xml",
                   name + ".defdup_first_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Tests that a template that defines the default value of one parameter 
     * in terms of the value for the other parameter works, where the template
     * apply has a binding for the second parameter and uses href= rather than 
     * inline content. 
     * <p>
     * Note, the order of parameters in the definition is important as a 
     * default value can only reference parameters.
     */
    public void testDefaultDuplicateSecondHref() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".defdup_second_input_href.xml",
                   name + ".defdup_second_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }
    
    /**
     * Tests that a template that defines the default value of one parameter 
     * in terms of the value for the other parameter works, where the template
     * apply has a binding for the second parameter and uses inline content 
     * rather than href=. 
     * <p>
     * Note, the order of parameters in the definition is important as a 
     * default value can only reference parameters.
     */
    public void testDefaultDuplicateSecondInline() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".defdup_second_input_inline.xml",
                   name + ".defdup_second_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Tests that a template that defines the default value of one parameter 
     * in terms of the value for the other parameter works, where the template
     * apply has a binding for both parameters and uses href= rather than 
     * inline content. 
     * <p>
     * Note, the order of parameters in the definition is important as a 
     * default value can only reference parameters.
     */
    public void testDefaultDuplicateBothHref() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".defdup_both_input_href.xml",
                   name + ".defdup_both_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }
    
    /**
     * Tests that a template that defines the default value of one parameter 
     * in terms of the value for the other parameter works, where the template
     * apply has a binding for both parameters and uses inline content rather 
     * than href=. 
     * <p>
     * Note, the order of parameters in the definition is important as a 
     * default value can only reference parameters.
     */
    public void testDefaultDuplicateBothInline() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".defdup_both_input_inline.xml",
                   name + ".defdup_both_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }

    /**
     * Test that bindings that don't map to parameters cause a fatal error.
     */
    public void testUndeclaredParameter() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".undeclared_param.xml",
                   name + ".undeclared_param.xml");

            fail("Should have had a streaming exception for the " +
                 "undeclared parameter");
        } catch (XMLPipelineException e) {
            // Correct behaviour
            System.out.println("Undeclared parameter exception: " + e);
        }
    }

    /**
     * Test that bindings that have no name cause a fatal error.
     */
    public void testUnnamedBinding() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".unnamed_binding.xml",
                   name + ".unnamed_binding.xml");

            fail("Should have had a processing exception for the " +
                 "unnamed binding");
        } catch (XMLPipelineException e) {
            // Correct behaviour
            System.out.println("Unnamed binding exception: " + e);
        }
    }

    /**
     * Test that parameters that have no name cause a fatal error.
     */
    public void testUnnamedParameter() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".unnamed_param.xml",
                   name + ".unnamed_param.xml");

            fail("Should have had a processing exception for the " +
                 "unnamed parameter");
        } catch (XMLPipelineException e) {
            // Correct behaviour
            System.out.println("Unnamed parameter exception: " +e);
        }
    }

    /**
     * Test that a fatal error is raised when a required parameter has no
     * binding.
     */
    public void testMissingRequiredParameter() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".missing_req_param.xml",
                   name + ".missing_req_param.xml");

            fail("Should have had a processing exception for the " +
                 "missing required parameter");
        } catch (XMLPipelineException e) {
            // Correct behaviour
            System.out.println("Missing required parameter exception: " + e);
        }
    }

    /**
     * Test that a fatal error is raised when a parameter is declared without
     * a containing template apply.
     */
    public void testMissingApply() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".missing_apply.xml",
                   name + ".missing_apply.xml");

            fail("Should have had a processing exception for the " +
                 "missing apply (no parameter block)");
        } catch (XMLPipelineException e) {
            // Correct behaviour
            System.out.println("Missing apply exception: " + e);
        }
    }

    /**
     * Test that a mismatch for binding complexity vs binding value causes
     * a fatal error when the binding has immediate mode.
     */
    public void testBadImmediateBindingComplexity() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".bad_immediate.xml",
                   name + ".bad_immediate.xml");

            fail("Should have had a streaming exception for the " +
                 "bad type of content for an immediate binding");
        } catch (XMLPipelineException e) {
            // Correct behaviour
            System.out.println("Bad immediate binding exception: " + e);
        }
    }

    /**
     * Test that a mismatch for binding complexity vs binding value causes
     * a fatal error when the binding has deferred mode.
     */
    public void testBadDeferredBindingComplexity() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".bad_deferred.xml",
                   name + ".bad_deferred.xml");

            fail("Should have had a streaming exception for the " +
                 "bad type of content for an deferred binding");
        } catch (XMLPipelineException e) {
            // Correct behaviour
            System.out.println("Bad deferred binding exception: " + e);
        }
    }

    /**
     * Test that a mismatch for binding value vs parameter complexity causes
     * a fatal error.
     */
    public void testBadParameterComplexity() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".bad_param.xml",
                   name + ".bad_param.xml");

            fail("Should have had a processing exception for the " +
                 "bad type of binding content for a simple parameter");
        } catch (XMLPipelineException e) {
            // Correct behaviour
            System.out.println("Bad binding vs parameter exception: " + e);
        }
    }

    /**
     * Test the template model with the definition referenced via href. This
     * covers exactly the same points as {@link #testInlineDefinition}.
     */
    public void testDependency() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + ".dependency_input.xml",
                   name + ".dependency_expected.xml");
        } catch (SAXParseException e) {
            throw new ExtendedSAXException(e.getMessage() +
                                            " (" + e.getLineNumber() + ", " +
                                            e.getColumnNumber() + ")", e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 03-Mar-04	582/6	geoff	VBM:2004022507 Template binding cannot access outer parameters

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 16-Jun-03	78/1	philws	VBM:2003061205 Add JSP test cases and debug some issues

 13-Jun-03	68/1	allan	VBM:2003022821 Fix testcase problems with IntegrationTestHelper and HypersonicManager

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 10-Jun-03	13/3	philws	VBM:2003030610 Integrate with Template Model Expression facilities

 06-Jun-03	26/2	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
