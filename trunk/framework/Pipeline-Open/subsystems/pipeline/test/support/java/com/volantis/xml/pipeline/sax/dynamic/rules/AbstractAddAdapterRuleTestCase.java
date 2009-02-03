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
package com.volantis.xml.pipeline.sax.dynamic.rules;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.adapter.AdapterProcess;

import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.MockAdapterProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.shared.environment.EnvironmentInteraction;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Test case for the {@link AbstractAddAdapterRule} class. Normaly
 * I would make this class abstract and test cases for any concrete 
 * implementations would extend this test case. However, I do not 
 * expect concrete AbstractAddAdapterRule to be tested (they will
 * be annonymous class of various 
 * {@link com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator} 
 * implementations). With this in mind I have provided a "testable"   
 * inner class implmentation of the <code>AstractAddAdapterRule</code>
 * class. 
 */ 
public class AbstractAddAdapterRuleTestCase extends TestCaseAbstract {

    /**
     * An AdapterProcess that will be added to the rule
     */ 
    private MockAdapterProcess adapterProcess;
    
    /**
     * Instance of the class being tested
     */ 
    private AbstractAddAdapterRule rule;
    
    /**
     * DynamicProcess that will be used when invoking the rule
      */    
    private DynamicProcess dynamicProcess;
    
    /**
     * Creates a new <code>AbstractAddAdapterRuleTestCase</code> instance
     * @param name the name of the test case
     */ 
    public AbstractAddAdapterRuleTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        
        // create the adapter process that will be added by the rule
        adapterProcess = new MockAdapterProcess();
        
        // create the rule
        rule = new AddAdapterRule();
        
        // get hold of the default pipeline factory
        XMLPipelineFactory factory = XMLPipelineFactory.getDefaultInstance();
        
        // factory a pipeline configuration
        XMLPipelineConfiguration pipelineConfiguration =
            factory.createPipelineConfiguration();
        
        // factor a DynamicProcessConfiguration
        DynamicProcessConfiguration dynamicConfig = 
                factory.createDynamicProcessConfiguration();
        
        // register the DynamicProcessConfiguration with the pipeline
        // configuration
        pipelineConfiguration.storeConfiguration(
                DynamicProcessConfiguration.class,
                dynamicConfig);
        
        // create a XMLPipelineContext
        EnvironmentInteraction interaction = null;
        XMLPipelineContext context = factory.createPipelineContext(
                    pipelineConfiguration,
                    interaction);

        // create a pipeline
        XMLPipeline pipeline = factory.createDynamicPipeline(context);
        
        // create a dynamic process
        dynamicProcess = factory.createDynamicProcess(
                new SimpleDynamicProcessConfiguration());
                
        // finally, register the pipeline with the dynamic process
        dynamicProcess.setPipeline(pipeline);        
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        adapterProcess = null;
        rule = null;
        dynamicProcess = null;
    }

    /**
     * Tests the {@link AbstractAddAdapterRule#startElement} method
     * @throws Exception if an error occurs
     */ 
    public void testStartElement() throws Exception {
        // create an expanded name        
        ExpandedName eName = new ImmutableExpandedName("testNamespace", 
                                                       "testLocalName");
        Attributes atts = new AttributesImpl();        
        
        XMLPipeline pipeline = dynamicProcess.getPipeline();        
        
        NamespacePrefixTracker tracker = 
                pipeline.getPipelineContext().getNamespacePrefixTracker();
        
        // register the prefix with the tracker
        tracker.startPrefixMapping("p", "testNamespace");
            
        // invoke startElement on the rule
        Object returned = rule.startElement(dynamicProcess, eName, atts);     
        
        // ensure the process was added to the pipeline
        assertSame("Adapter Process was not added to the pipeline",
                   adapterProcess, pipeline.getHeadProcess());
        
        // ensure the object return was the addapter process
        assertSame("startElement should return the adapter process that was " +
                   "added to the pipeline", adapterProcess, returned);
        
        // ensure the adapters processes setElementDetails method was invoked
        adapterProcess.assertSetElementDetailsInvoked("testNamespace",
                                                      "testLocalName",
                                                      "p:testLocalName");
        
        // ensure the processAttributes method was invoked ok
        adapterProcess.assertProcessAttributesInvoked(atts);                        
    }
    
    /**
     * Tests the {@link AbstractAddAdapterRule#endElement} method 
     * @throws Exception if an error occurs
     */ 
    public void testEndElement() throws Exception {
        // retrieve the pipeline
        XMLPipeline pipeline = dynamicProcess.getPipeline();
        // add a process to the head of the pipeline        
        pipeline.addHeadProcess(adapterProcess);
            
        ExpandedName eName = new ImmutableExpandedName("testNamespace", 
                                                       "testLocalName");
    
        // call end element on the rule
        rule.endElement(dynamicProcess, eName, adapterProcess);
        
        // ensure that the process was removed from the pipeline
        assertNotSame("endElement did not remove the adapter process from " +
                      "the pipeline",
                      adapterProcess,
                      pipeline.getHeadProcess());
                
    }
    
    /**
     * Tests that the {@link AbstractAddAdapterRule#endElement} method 
     * throws an exception if you attempt to remove a process that is
     * not at the head of the pipeline 
     * @throws Exception if an error occurs
     */     
    public void testEndElementWithWrongProcess() throws Exception {
        // retrieve the pipeline
        XMLPipeline pipeline = dynamicProcess.getPipeline();
        // add the process to the head of the pipeline        
        pipeline.addHeadProcess(adapterProcess);        
        
        // create an expanded name        
        ExpandedName eName = new ImmutableExpandedName("testNamespace", 
                                                       "testLocalName");
        
        try {
            // call end element on the rule
            rule.endElement(dynamicProcess, eName, new MockAdapterProcess());
            fail("endElement did not throw an exception when attempting to " +
                 "remove a process that was not at the head of the pipeline");
        } catch (XMLPipelineException e) {            
        }                
    }
    
    /**
     * Factory an concrete <code>AbstractAddAdapterRule</code> instance
     * @return a AbstractAddAdapterRule instance
     */ 
    protected AbstractAddAdapterRule createTestableRule() {
        return new AddAdapterRule();
    }
    
    /**
     * Inner Concrete implementation of the 
     * <code>AbstractAddAdapterRule</code>
     */ 
    private class AddAdapterRule extends AbstractAddAdapterRule {

        // javadoc inherited
        public AdapterProcess createAdapterProcess(DynamicProcess dynamicProcess) {                
            return adapterProcess;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 05-Aug-03	290/1	doug	VBM:2003080412 Provided DynamicElementRule implementation for adding Adapters to a pipeline

 ===========================================================================
*/
