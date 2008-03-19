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
package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessImplTestCase;
import com.volantis.xml.pipeline.sax.XMLProcessTestable;
import com.volantis.xml.pipeline.sax.impl.flow.SimpleFlowControlManager;
import com.volantis.xml.pipeline.sax.impl.flow.SimpleFlowControlManager;
import com.volantis.xml.pipeline.sax.impl.dynamic.FlowControlProcess;
import junitx.util.PrivateAccessor;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.Set;

/**
 * Test case for the {@link com.volantis.xml.pipeline.sax.impl.dynamic.FlowControlProcess} class
 */
public class FlowControlProcessTestCase extends XMLProcessImplTestCase {

    /**
     * Creates a new <code>FlowControlProcessTestCase</code> instance
     * @param name the name of the test
     */
    public FlowControlProcessTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected XMLProcess createTestableProcess() {
        FlowControlProcess process = new FlowControlProcess();
        initializeProcess(process);
        return process;
    }

    // javadoc inherited
    public void testCharactersInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.characters(CH, START, LENGTH);
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertCharactersNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#endDocument} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testEndDocumentInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.endDocument();
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertEndDocumentNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#endElement} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testEndElementInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
        
        // endElement event should be forwarded
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
        
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#endPrefixMapping} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testEndPrefixMappingInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.endPrefixMapping(PREFIX);
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertEndPrefixMappingNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#ignorableWhitespace} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testIgnorableWhitespaceInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.ignorableWhitespace(CH, START, LENGTH);
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertIgnorableWhitespaceNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#processingInstruction} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testProcessingInstructionInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.processingInstruction(TARGET, DATA);
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertProcessingInstructionNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#setDocumentLocator} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testSetDocumentLocatorInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.setDocumentLocator(LOCATOR);
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertSetDocumentLocatorNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#skippedEntity} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testSkippedEntityInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.skippedEntity(NAME);
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertSkippedEntityNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#startDocument} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testStartDocumentInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.startDocument();
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertStartDocumentNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#startElement} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testStartElementInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
        
        // in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertStartElementNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#startPrefixMapping} events are not forwarded
     * @throws Exception if an error occures
     */
    public void testStartPrefixMappingInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.startPrefixMapping(PREFIX, URI);
        
        // as in flow control mode ensure event was not forwarded to  
        // the next process
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertStartPrefixMappingNotInvoked();
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#error} events are forwarded
     * @throws Exception if an error occures
     */
    public void testErrorInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.error(EXCEPTION);
        
        // even in flow control mode errors should be forwarded
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertErrorInvoked(EXCEPTION);
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#fatalError} events are forwarded
     * @throws Exception if an error occures
     */
    public void testFatalErrorInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        try {
            testable.fatalError(EXCEPTION);
            fail("fatalError did not forward the error to the next process");
        } catch(SAXParseException e) {            
        }        
    }

    /**
     * Test to Ensure that when in flow control mode 
     * {@link XMLProcess#warning} events are forwarded
     * @throws Exception if an error occures
     */
    public void testWarningInFlowControlMode() throws Exception {
        // create a FlowControlProcess that is in flow control mode and
        // has a next process set
        XMLProcess testable = createTestableProcess();
        prepareProcessForFlowControlTest(testable);
        
        // invoke the method being tested
        testable.warning(EXCEPTION);
        
        // even in flow control mode warnings should be forwarded
        XMLProcessTestable next = 
                (XMLProcessTestable) testable.getNextProcess();
        next.assertWarningInvoked(EXCEPTION);
    }

    public void testStartProcess() throws Exception {
        // create an instance of the class that is being tested
        FlowControlProcess process =
                (FlowControlProcess) createTestableProcess();
        
        // set a next process
        XMLProcessTestable next = createNextProcess();
        process.setNextProcess(next);

        XMLPipelineContext context = process.getPipelineContext();
        SimpleFlowControlManager manager =
                (SimpleFlowControlManager) context.getFlowControlManager();

        Set flowControllers = (Set) PrivateAccessor.getField(manager,
                                                             "flowControllers");
                
        // invoke the method being tested
        process.startProcess();
        
        // ensure the process registered itself with the flow control manager
        assertTrue("FlowControlProcess was not registered with the flow " +
                   "control manager", flowControllers.contains(process));
        
        // ensure start was not invoked on the next process
        next.assertStartProcessNotInvoked();
    }

    public void testStopProcess() throws Exception {
        // create an instance of the class that is being tested
        FlowControlProcess process =
                (FlowControlProcess) createTestableProcess();
        
        // set a next process
        XMLProcessTestable next = createNextProcess();
        process.setNextProcess(next);

        XMLPipelineContext context = process.getPipelineContext();
        SimpleFlowControlManager manager =
                (SimpleFlowControlManager) context.getFlowControlManager();

        Set flowControllers = (Set) PrivateAccessor.getField(manager,
                                                             "flowControllers");
        
        // add the process to the set
        flowControllers.add(process);
        
        // need to ensure the process has been started
        process.startProcess();
        
        // invoke the method being tested
        process.stopProcess();
        
        // ensure the process registered itself with the flow control manager
        assertFalse("FlowControlProcess was not unregistered with the flow " +
                    "control manager", flowControllers.contains(process));
        
        // ensure start was not invoked on the next process
        next.assertStopProcessNotInvoked();
    }

    /**
     * Helper method that starts the process passed in chains it to a
     * a next process and starts flow control  
     *
     * @param process the process that is to be started     
     * @throws SAXException if an error occurs
     */ 
    protected void prepareProcessForFlowControlTest(
            XMLProcess process) throws SAXException {

        // start the flow control process
        process.startProcess();
        
        // get hold of the pipeline context
        XMLPipelineContext context = 
                process.getPipeline().getPipelineContext();
        
        // start flow control
        context.getFlowControlManager().exitNestingLevels(1);        
        
        // set a next process
        XMLProcessTestable next = createNextProcess();
        process.setNextProcess(next);        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 ===========================================================================
*/
