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

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessTestable;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * TestCase for the {@link AbstractAddProcessRule} class
 * I would make this class abstract and test cases for any concrete 
 * implementations would extend this test case. However, I do not 
 * expect concrete AbstractAddAdapterRule to be tested (they will
 * be annonymous class implementations of 
 * {@link com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator}).
 *  With this in mind I have provided a "testable"   
 * inner class implmentation of the <code>AstractAddProcessRule</code>
 * class. 
 */
public abstract class AbstractAddProcessRuleTestAbstract extends TestCaseAbstract {

    /**
     * Instance of the class being tested
     */
    protected AbstractAddProcessRule rule;

    /**
     * Pipeline configuration
     */
    protected XMLPipelineConfiguration pipelineConfiguration;

    /**
     * DynamicProcess that will be used when invoking the rule
     */
    protected DynamicProcess dynamicProcess;

    /**
     * Creates a new <code>AbstractAddProcessRuleTestAbstract</code> instance
     * @param name the name of the test
     */
    public AbstractAddProcessRuleTestAbstract(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        // create the rule
        rule = createTestableRule();
        
        // get hold of the default pipeline factory
        XMLPipelineFactory factory = XMLPipelineFactory.getDefaultInstance();
        
        // factory a pipeline configuration
        pipelineConfiguration = factory.createPipelineConfiguration();
        
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
        rule = null;
        dynamicProcess = null;
    }

    /**
     * Tests the {@link AbstractAddProcessRule#startElement} method
     * @throws Exception if an error occurs
     */
    public void testStartElement() throws Exception {
        // create an expanded name        
        ExpandedName eName = new ImmutableExpandedName("testNamespace",
                                                       "testLocalName");

        Attributes atts = createStartElementAttributes();

        XMLPipeline pipeline = dynamicProcess.getPipeline();                       
            
        // invoke startElement on the rule
        Object returned = rule.startElement(dynamicProcess, eName, atts);

        // ask the rule to create a process
        XMLProcess process = rule.createProcess(dynamicProcess,
                                                eName,
                                                createStartElementAttributes());

        // ensure the process was added to the pipeline is the same type as
        // the one that the rule creates
        assertSame("Process was not added to the pipeline",
                   process.getClass(),
                   pipeline.getHeadProcess().getClass());

    }

    /**
     * Tests the {@link AbstractAddProcessRule#endElement} method 
     * @throws Exception if an error occurs
     */
    public void testEndElement() throws Exception {
        // retrieve the pipeline
        XMLPipeline pipeline = dynamicProcess.getPipeline();

        // create an expanded name
        ExpandedName eName = new ImmutableExpandedName("testNamespace",
                                                       "testLocalName");

        // ask the rule to create a process
        XMLProcess process = rule.createProcess(dynamicProcess,
                                                eName,
                                                createStartElementAttributes());

        // add a process to the head of the pipeline
        pipeline.addHeadProcess(process);

        // call end element on the rule
        rule.endElement(dynamicProcess, eName, process);
        
        // ensure that the process was removed from the pipeline
        assertNotSame("endElement did not remove the process from " +
                      "the pipeline",
                      process,
                      pipeline.getHeadProcess());

    }

    /**
     * Tests that the {@link AbstractAddProcessRule#endElement} method 
     * throws an exception if you attempt to remove a process that is
     * not at the head of the pipeline 
     * @throws Exception if an error occurs
     */
    public void testEndElementWithWrongProcess() throws Exception {
        // retrieve the pipeline
        XMLPipeline pipeline = dynamicProcess.getPipeline();
        // add the process to the head of the pipeline        
        pipeline.addHeadProcess(new XMLProcessTestable());
        
        // create an expanded name        
        ExpandedName eName = new ImmutableExpandedName("testNamespace",
                                                       "testLocalName");

        try {
            // call end element on the rule
            rule.endElement(dynamicProcess, eName, new XMLProcessTestable());
            fail("endElement did not throw an exception when attempting to " +
                 "remove a process that was not at the head of the pipeline");
        } catch (XMLPipelineException e) {
        }
    }

    /**
     * Factory method that creates the <code>Attriubtes</code> required
     * to test the {@link AbstractAddProcessRule#startElement} method
     * @return an Attributes instance
     */
    protected Attributes createStartElementAttributes() {
        return new AttributesImpl();
    }
    /**
     * Creates an instance of the class that is to be tested.
     * @return an AbstractAddProcessRule instance
     */
    protected abstract AbstractAddProcessRule createTestableRule();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 16-Oct-03	419/2	doug	VBM:2003101403 extracted base class from QueryOperationProcess

 13-Aug-03	335/1	doug	VBM:2003081003 Tidied up the AbstractAddProcessRule class

 ===========================================================================
*/
