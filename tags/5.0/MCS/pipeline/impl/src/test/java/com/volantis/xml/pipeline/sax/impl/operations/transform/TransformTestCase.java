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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug           VBM:2002121803 - Created. Base class for
 *                             integration tests that wish to test the
 *                             transform process.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.impl.operations.transform;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;
import com.volantis.xml.pipeline.sax.operations.transform.DefaultTransformConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.synergetics.cache.GenericCache;


/**
 * Test case for the transform process
 */
public class TransformTestCase
        extends PipelineTestAbstract {

    /**
     * Factory for creating pipeline objects
     */
    protected XMLPipelineFactory pipelineFactory;

    /**
     * Creates a new TransformTestAbstract instance
     * @param name the name
     */
    public TransformTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    public void setUp() throws Exception {
        super.setUp();

        pipelineFactory = new IntegrationTestHelper().getPipelineFactory();
    }

    /**
     * Ensures that simple name value pair parameter elements are correctly
     * processed and used in the transform.
     */
    public void testSimpleParameters() throws Exception {
        doTest(pipelineFactory,
                "SimpleParameterTestCase.input.xml",
                "SimpleParameterTestCase.expected.xml");
    }

    /**
     * Ensures that complex infoset parameter elements are correctly
     * processed and used in the transform.
     */
    public void testComplexParameters() throws Exception {
        doTest(pipelineFactory,
                "ComplexParameterTestCase.input.xml",
                "ComplexParameterTestCase.expected.xml");
    }

    /**
     * Ensures that simple parameter elements and complex infoset parameter
     * elements are correctly processed and used in the transform in
     * combination.
     */
    public void testMixedParameters() throws Exception {
        doTest(pipelineFactory,
                "MixedParameterTestCase.input.xml",
                "MixedParameterTestCase.expected.xml");
    }

    /**
     * Ensures that namespace for complex infoset parameter
     * elements are correctly processed and used in the transform.
     */
    public void testNamespaceParameters() throws Exception {
        doTest(pipelineFactory,
                "NamespaceParameterTestCase.input.xml",
                "NamespaceParameterTestCase.expected.xml");
    }

    /**
     * Ensures that an xml fagment can be transformed
     * @throws Exception if an error occurs
     */
    public void testDocFragTransformation() throws Exception {
        doTest(pipelineFactory,
               "DocFragTransformTestCase.input.xml",
               "DocFragTransformTestCase.expected.xml");
    }

    /**
     * Ensures the transform process can handle inline stylesheet
     * declarations.
     * @throws Exception if an error occurs
     */
    public void testInlineTransformation() throws Exception {
        doTest(pipelineFactory,
               "InlineTransformationTestCase.input.xml",
               "InlineTransformationTestCase.expected.xml");
    }

    /**
     * Ensures the transform process can handle inline stylesheet that are
     * included via a fetch operation
     * @throws Exception if an error occurs
     */
    public void testInlineIncludeTransformation() throws Exception {
        doTest(pipelineFactory,
               "InlineInclusionTransformationTestCase.input.xml",
               "InlineInclusionTransformationTestCase.expected.xml");
    }

    /**
     * Ensures that nested transformations can be processed
     * @throws Exception if an error occurs
     */
    public void testNestedTransformations() throws Exception {
        doTest(pipelineFactory,
               "NestedTransformationsTestCase.input.xml",
               "NestedTransformationsTestCase.expected.xml");
    }

    /**
     * Ensures that multiple style sheets can be used when performing a
     * transformation
     * @throws Exception if an error occurs
     */
    public void testSimpleTransformations() throws Exception {
        doTest(pipelineFactory,
               "SimpleTransformationsTestCase.input.xml",
               "SimpleTransformationsTestCase.expected.xml");
    }

    /**
     * Ensures that a single style sheet can be used to perform
     * a transformation via the transformation element.
     * @throws Exception if an error occurs
     */
    public void testSimpleTransformation() throws Exception {
        doTest(pipelineFactory,
               "SimpleTransformationTestCase.input.xml",
               "SimpleTransformationTestCase.expected.xml");
    }

    /**
     * Ensures that a single style sheet can be used to perform
     * a transformation via the transform element.
     * @throws Exception if an error occurs
     */
    public void testSimpleTransform() throws Exception {
        doTest(pipelineFactory,
               "SimpleTransformTestCase.input.xml",
               "SimpleTransformTestCase.expected.xml");
    }

    /**
     * Ensures that a single style sheet can be used to perform
     * a transformation via the transform element. We specify that the transform
     * is compilable but there should be no difference between the results of
     * this test and that of testSimpleTransformation()
     * @throws Exception if an error occurs
     */
    public void testCompilableSimpleTransformation() throws Exception {
        XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        createPipelineConfiguration(),
                        createRootEnvironmentInteraction());

        DefaultTransformConfiguration config = (DefaultTransformConfiguration)
                pipelineContext.getPipelineConfiguration().
                retrieveConfiguration(TransformConfiguration.class);

        config.setTemplateCompilationRequired(true);

        doInContextTest(pipelineContext,
               "CompilableSimpleTransformationTestCase.input.xml",
               "SimpleTransformTestCase.expected.xml");
    }

    /**
     * Ensures that a single style sheet can be used to perform
     * a transformation via the transform element. We specify that the
     * transformation is compilable but there should be no difference between
     * the results of this test and that of testSimpleTransformation()
     * @throws Exception if an error occurs
     */
    public void testCompilableSimpleTransformation2() throws Exception {
        XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        createPipelineConfiguration(),
                        createRootEnvironmentInteraction());

        DefaultTransformConfiguration config = (DefaultTransformConfiguration)
                pipelineContext.getPipelineConfiguration().
                retrieveConfiguration(TransformConfiguration.class);

        config.setTemplateCompilationRequired(true);

        doInContextTest(pipelineContext,
               "CompilableSimpleTransformationTestCase.input.xml",
               "SimpleTransformTestCase.expected.xml");
    }

    /**
     * Ensures that nested transformations can be processed. The transform
     * element is compilable but one of its child transformation elements
     * overrides it to be not compilable.
     * @throws Exception if an error occurs
     */
    public void testCompilableNestedTransformations() throws Exception {
        XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        createPipelineConfiguration(),
                        createRootEnvironmentInteraction());

        DefaultTransformConfiguration config = (DefaultTransformConfiguration)
                pipelineContext.getPipelineConfiguration().
                retrieveConfiguration(TransformConfiguration.class);

        config.setTemplateCompilationRequired(true);

        doInContextTest(pipelineContext,
               "CompilableNestedTransformationsTestCase.input.xml",
               "NestedTransformationsTestCase.expected.xml");
    }

    /**
     *
     */
    public void testCache()  throws Exception {
        XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        createPipelineConfiguration(),
                        createRootEnvironmentInteraction());

        // extract the expression context
        ExpressionContext expresisonContext
                = pipelineContext.getExpressionContext();

        // allow subclasses to register functions that are specific to
        // their tests
        registerExpressionFunctions(expresisonContext);

        doInContextTest(pipelineContext,
               "CompilableNestedTransformationsTestCase.input.xml",
               "NestedTransformationsTestCase.expected.xml");

        DefaultTransformConfiguration config = (DefaultTransformConfiguration)
                pipeline.getPipelineContext().getPipelineConfiguration().
                retrieveConfiguration(TransformConfiguration.class);

        // Test the defaults
        assertFalse("No cache specified as default",
                config.isTemplateCacheRequired());
        assertNull("Should be no cache available", config.getTemplateCache());

        // Enabling caching
        config.setTemplateCacheRequired(true);
        assertNotNull("Cache should be created", config.getTemplateCache());

        // Re-run through the pipeline
        doInContextTest(pipelineContext,
                "CompilableNestedTransformationsTestCase.input.xml",
                "NestedTransformationsTestCase.expected.xml");

        // Check for something in the cache
        GenericCache cache = (GenericCache) config.getTemplateCache();
        assertFalse("Should be something in the cache", cache.isEmpty());

    }

    /**
     * Ensures that the transform process does not fail if there is no xsl
     * @throws Exception if an error occurs
     */
    public void testEmptyTransform() throws Exception {
        doTest(pipelineFactory,
               "EmptyTransformTestCase.input.xml",
               "EmptyTransformTestCase.expected.xml");
    }

    /**
     * Ensures that the transform process still executes the xsl even if there
     * is no content to transform
     */
    public void testNoContentTransform() throws Exception {
        doTest(pipelineFactory,
               "NoContentTransformTestCase.input.xml",
               "NoContentTransformTestCase.expected.xml");
    }

    /**
     * Test that transform template caching works in a multi threaded
     * environment.
     */
    public void testCacheMultiThreaded() throws Exception {
        final XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        createPipelineConfiguration(),
                        createRootEnvironmentInteraction());

        DefaultTransformConfiguration config = (DefaultTransformConfiguration)
                pipelineContext.getPipelineConfiguration().
                retrieveConfiguration(TransformConfiguration.class);

        config.setTemplateCacheRequired(true);
        config.setTemplateCompilationRequired(true);

        for (int i=0; i<50; i++) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        doInContextTest(pipelineContext,
                        "CompilableNestedTransformationsTestCase.input.xml",
                        "NestedTransformationsTestCase.expected.xml");
                    } catch (Exception e) {
                        fail(e.getLocalizedMessage());
                    }
                }
            };
            thread.run();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9663/1	doug	VBM:2005080416 Ensured transform operation can handle empty transforms

 28-Sep-05	9596/1	doug	VBM:2005080416 Ensured transform process correctly tries to perform a transform even if no content was received

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Apr-04	692/1	adrian	VBM:2004042603 Add parameter passing functionality to transform process

 30-Apr-04	686/2	adrian	VBM:2004042802 Add parameter support for transforms

 13-Apr-04	667/1	adrian	VBM:2004040713 Fixed transform template caching.

 13-Apr-04	665/1	adrian	VBM:2004040713 Fixed transform template caching.

 13-Apr-04	662/1	adrian	VBM:2004040713 Fixed transform template caching.

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 26-Jan-04	551/6	claire	VBM:2004012204 Caching clean-up

 26-Jan-04	551/4	claire	VBM:2004012204 Fixed and optimised caching code

 26-Jan-04	551/2	claire	VBM:2004012204 Implementing caching for transforms

 05-Aug-03	268/1	chrisw	VBM:2003072905 implemented compilable attribute on transform

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 24-Jun-03	109/1	philws	VBM:2003061913 Change pipeline:includeURI to urid:fetch and add new TLD for it

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 12-Jun-03	53/2	doug	VBM:2003050603 JSP ContentTag refactoring

 06-Jun-03	26/2	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
