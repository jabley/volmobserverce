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
package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.servlet.ServletEnvironmentFactory;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.impl.dynamic.FlowControlProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.FlowControlProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.XMLPipelineContextImpl;
import org.xml.sax.XMLReader;

/**
 * Test Case for the {@link XMLPipelineFactory} class.
 */ 
public class DefaultPipelineFactoryTestCase 
        extends XMLPipelineFactoryTestCase {
    
    /**
     * Instance of the class being tested
     */ 
    protected XMLPipelineFactory factory;
    
    /**
     * Creates a new DefaultPipelineFactoryTestCase instance.
     * @param name the name of the test.
     */ 
    public DefaultPipelineFactoryTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        factory = createTestable();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        factory = null;
    }
    
    /**
     * Factory method for creating an instance of the class being tested.
     * @return an XMLPipelineFactory instance.
     */ 
    protected XMLPipelineFactory createTestable() {
        return XMLPipelineFactory.getDefaultInstance();
    }

    /**
     * Factory method that creates an <code>XMLPipeline</code> instance.
     * @return an XMLPipeline instance.
     */ 
    protected XMLPipeline createPipeline() {
        XMLPipelineFactory pipelineFactory = createTestable();
        return pipelineFactory.createPipeline(createContext());                
    }
    
    /**
     * Factory method that creates an <code>XMLPipelineContext</code> instance.
     * @return an XMLPipeline instance.
     */
    protected XMLPipelineContext createContext() {
        // create a factory
        XMLPipelineFactory pipelineFactory = createTestable();
        // create a config
        XMLPipelineConfiguration config = 
                pipelineFactory.createPipelineConfiguration();
        // get hold of an ExpressionFactory
        ExpressionFactory expressionFactory =
                pipelineFactory.getExpressionFactory();
        
        // create an expression context
        ExpressionContext expressionContext = 
                expressionFactory .createExpressionContext(null, null);        
        
        // return a context 
        return factory.createPipelineContext(config, expressionContext);
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#createPipelineContext} method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreatePipelineContext() throws Exception {
        // create a config
        XMLPipelineConfiguration config = 
                factory.createPipelineConfiguration();
        
        // create an environment interaction
        EnvironmentInteraction interaction = 
                ServletEnvironmentFactory.getDefaultInstance()
                    .createEnvironmentInteraction(null, null, null, null,null);
        
        // factor a context
        XMLPipelineContext context = 
                factory.createPipelineContext(config, interaction);
        
        // ensure the context is of the correct type
        assertEquals("createPipelineContext should return an instance " + 
                     "of XMLPipelineContextImpl",
                     XMLPipelineContextImpl.class,
                     context.getClass());
        
        // ensure the context references the correct config
        assertEquals("createPipelineContext should reference the " +
                     "XMLPipelineConfiguration provided via the " +
                     "createPipelineContext method",
                     config, context.getPipelineConfiguration());
        
        // ensure the context references the correct root environment 
        // interactions                 
        assertEquals("createPipelineContext should reference the " +
                     "Root environment interaction provided via the " +
                     "createPipelineContext method",
                     interaction, 
                     context.getEnvironmentInteractionTracker().
                        getRootEnvironmentInteraction());        
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#createDynamicPipeline} method.
     * @throws Exception if an error occurs.
     */ 
    public void testSecondCreatePipelineContext() throws Exception {
        // create a config
        XMLPipelineConfiguration config = 
                factory.createPipelineConfiguration();
        
        ExpressionContext expressionContext = 
                factory.getExpressionFactory().createExpressionContext(
                        null, null);        
        
        // factor a context
        XMLPipelineContext context = 
                factory.createPipelineContext(config, expressionContext);
        
        // ensure the context is of the correct type
        assertEquals("createPipelineContext should return an instance " + 
                     "of XMLPipelineContextImpl",
                     XMLPipelineContextImpl.class, 
                     context.getClass());
        
        // ensure the context references the correct config
        assertEquals("createPipelineContext should reference the " +
                     "XMLPipelineConfiguration provided via the " +
                     "createPipelineContext method",
                     config, context.getPipelineConfiguration());
        
        // ensure the context references the correct ExpressionContext
        assertEquals("createPipelineContext should reference the " +
                     "ExpressionContext provided via the " +
                     "createPipelineContext method",
                     expressionContext, context.getExpressionContext());                
    }

    /**
     * Tests the {@link XMLPipelineFactory#createContextUpdatingProcess}
     * method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreateContextUpdatingProcess() throws Exception {
        XMLProcess contextProcess = factory.createContextUpdatingProcess();
        assertEquals("createContextUpdatingProcess should factor a" +
                     " ContextManagerProcess intance",
                     ContextManagerProcess.class,
                     contextProcess.getClass());
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#createPipelineFilter} method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreatePipelineFilter() throws Exception {
        XMLPipeline pipeline = createPipeline();
        XMLPipelineFilter filter = factory.createPipelineFilter(pipeline);
        // Ensure the correct type was factored
        assertTrue("createPipleineFilter should factor an " +
                     "XMLPipelineFilterAdapter instance",
                       filter instanceof XMLPipelineFilterAdapter);
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#createPipelineReader} method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreatePipelineReader() throws Exception {
        XMLPipeline pipeline = createPipeline();
        XMLPipelineReader reader = factory.createPipelineReader(pipeline);
        // Ensure the correct type was factored
        assertTrue("createPipleineReader should factor an " +
                     "XMLPipelineFilterAdapter instance", 
                     reader instanceof XMLPipelineFilterAdapter);
        assertTrue("The returned XMLPipelineFilterAdapter should have an " +
                "XMLReader set as its parent.",
                ((XMLPipelineFilter)reader).getParent() instanceof XMLReader);
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#getRuleConfigurator} method.
     * @throws Exception if an error occurs.
     */ 
    public void testGetRuleConfigurator() throws Exception {
        DynamicRuleConfigurator configurator = factory.getRuleConfigurator();
        // ensure the DynamicRuleConfigurator not null
        assertNotNull("getRuleConfigurator returned null", configurator);
    }

    /**
     * Tests the {@link XMLPipelineFactory#createFlowControlProcess}
     * method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreateFlowControlProcess() throws Exception {
        XMLProcess flowProcess = factory.createFlowControlProcess();
        assertEquals("createFlowControlProcess should return a " +
                     "FlowControlProcess instance",
                    FlowControlProcess.class,
                    flowProcess.getClass());
    }
    
    /**
     * Tests the 
     * {@link XMLPipelineFactory#createDynamicProcessConfiguration} method
     * @throws Exception if an error occurs
     */ 
    public void testCreateDynamicProcessConfiguration() throws Exception {
        DynamicProcessConfiguration config = 
                factory.createDynamicProcessConfiguration();
        // ensure a class of the right type was factored
        assertEquals("createDynamicProcessConfiguration should factor a " +
                     "SimpleDynamicProcessConfiguration instance", 
                     SimpleDynamicProcessConfiguration.class,
                     config.getClass());
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#createDynamicProcess} method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreateDynamicProcess() throws Exception {
        
        // create an empty pipeline configuration
        XMLPipelineConfiguration configuration = 
                factory.createPipelineConfiguration();
        
        // store a DynamicProcessConfiguration in the pipeline configuratin
        configuration.storeConfiguration(
                DynamicProcessConfiguration.class,
                factory.createDynamicProcessConfiguration());
        
        // factor a DynamicProcess
        DynamicProcess process = factory.createDynamicProcess(configuration);
        
        // ensure the object factored is of the correct type
        assertEquals("createDynamicProcess should factor a " +
                     "SimpleDynamicProcess instance", 
                     SimpleDynamicProcess.class, 
                     process.getClass());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createDynamicProcess} method.
     * @throws Exception if an error occurs.
     */ 
    public void testSecondCreateDynamicProcess() throws Exception {
         // factor a DynamicProcess
        DynamicProcess process = 
                factory.createDynamicProcess(
                        factory.createDynamicProcessConfiguration());
        
        // ensure the object factored is of the correct type
        assertEquals("createDynamicProcess should factor a " +
                     "SimpleDynamicProcess instance", 
                     SimpleDynamicProcess.class, 
                     process.getClass());       
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#createPipelineConfiguration}
     * method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreatePipelineConfiguration() throws Exception {
        DynamicProcessConfiguration config = 
                factory.createDynamicProcessConfiguration();
        
        // ensure an object of the correct type was factored
        assertEquals("createPipelineConfiguration should factor a " +
                     "SimpleDynamicProcessConfiguration instance", 
                     SimpleDynamicProcessConfiguration.class,
                     config.getClass());
    }
    
    
    /**
     * Tests the {@link XMLPipelineFactory#createPipeline} method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreatePipeline() throws Exception {
        // create a pipeline context
        XMLPipelineContext context = createContext();
        // create an XMLPipeline
        XMLPipeline pipeline = factory.createPipeline(context);
        // ensure that the object factored is of the correct type
        assertEquals("createPipeline should factor a XMLPipelineProcessImpl " +
                     "instance", 
                     XMLPipelineProcessImpl.class, 
                     pipeline.getClass());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createDynamicPipeline} method.
     * @throws Exception if an error occurs.
     */ 
    public void testCreateDynamicPipeline() throws Exception {
        // create a context
        XMLPipelineContext context = createContext();
        // retrieve the pipeline configuratin        
        XMLPipelineConfiguration config = context.getPipelineConfiguration();
        // store a Dynamic Process configuration in the pipeline config
        config.storeConfiguration(DynamicProcessConfiguration.class,
                                  factory.createDynamicProcessConfiguration());
        // factor a dynamic pipeline                
        XMLPipeline pipeline = factory.createDynamicPipeline(context);
        // ensure the correct object was factored
        assertEquals("createDynamicPipeline should factor a " +
                     "SimpleDynamicProcess instance",
                     SimpleDynamicProcess.class,
                     pipeline.getClass());

        assertEquals("Pipeline process should be the pipeline",
                     pipeline,
                     pipeline.getPipelineProcess());

        assertNull("Head process must not be set", pipeline.getHeadProcess());
    }

    /**
     * Tests the {@link XMLPipelineFactory#getNamespaceFactory} method.
     * @throws Exception if an error occurs.
     */ 
    public void testGetNamespaceFactory() throws Exception {
        // retrieve the namespace factory
        NamespaceFactory namespaceFactory = factory.getNamespaceFactory();
        // ensure the namespace factory is of the correct type
        assertEquals("getNamespaceFactory should return the default " +
                     "NamespaceFactory instance", 
                     NamespaceFactory.getDefaultInstance(), 
                     namespaceFactory);
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#getExpressionFactory} method.
     * @throws Exception if an error occurs.
     */ 
    public void testGetExpressionFactory() throws Exception {
        // retrieve the expression factory
        ExpressionFactory expressionFactory = 
                factory.getExpressionFactory();
        // ensure the expression factory is of the correct type
        assertEquals("getExpressionFactory should return the default " +
                     "ExpressionFactory instance", 
                     ExpressionFactory.getDefaultInstance(), 
                     expressionFactory);        
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8751/1	schaloner	VBM:2005060711 Added a getter method for the AttributeNormalizingContentHandler in PipelineTestAbstract

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 20-Jan-04	529/1	adrian	VBM:2004011904 Pipeline API updates in preparation for fully integrating ContextUpdating/Annotating processes

 11-Aug-03	275/2	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/2	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 ===========================================================================
*/
