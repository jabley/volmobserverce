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

/**
 * TestCase for the DocumentEventConsumer process
 */ 
public class DocumentEventConsumerTestCase extends XMLProcessImplTestCase {
    
    /**
     * Creates a new DocumentEventConsumerTestCase instance
     * @param name the name
     */ 
    public DocumentEventConsumerTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected XMLProcess createTestableProcess() {
        return new DocumentEventConsumer();
    }

    
    /**
     * Test that ensures that all startDocument() events are forwarded if
     * the process has not been started.
     * @throws Exception if an error occurs
     */ 
    public void noTestStartDocumentProcessNotStarted() throws Exception {
        // we need to ensure that if the process is not started then 
        // all startDocument events get forwarded
        XMLProcess testable = createTestableProcess();
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // defensive check
        next.assertStartDocumentNotInvoked();        
        // fire a startDocument() event
        testable.startDocument();
        // ensure the event was forwarded.
        next.assertStartDocumentInvoked();
        // reset the next process
        next.reset();
        // defensive check
        next.assertStartDocumentNotInvoked();        
        // fire a second startDocument() event
        testable.startDocument();
        // ensure the second event was forwarded.
        next.assertStartDocumentInvoked();   
    }
    
    /**
     * Test that ensures that only the first startDocument() event is 
     * forwarded if the process has been started.
     * @throws Exception if an error occurs
     */    
    public void noTestStartDocument() throws Exception {
        // we need to ensure that if the process is started then 
        // only the first startDocument() event is forwarded
        XMLProcess testable = createTestableProcess();
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // start the process
        testable.startProcess();
          
        // defensive check
        next.assertStartDocumentNotInvoked();        
        // fire a startDocument() event
        testable.startDocument();
        // ensure the event was forwarded.
        next.assertStartDocumentInvoked();
        // reset the next process
        next.reset();
        // defensive check
        next.assertStartDocumentNotInvoked();        
        // fire a second startDocument() event
        testable.startDocument();
        // ensure the second event was not forwarded.
        next.assertStartDocumentNotInvoked();        
    }

    /**
     * Test that ensures that all endDocument() events are forwarded if
     * the process has not been started.
     * @throws Exception if an error occurs
     */    
    public void noTestEndDocumentProcessNotStarted() throws Exception {
        // we need to ensure that if the process is not started then 
        // all endDocument events get forwarded
        XMLProcess testable = createTestableProcess();
        // need to initialize this process
        XMLPipelineFactory factory = new TestPipelineFactory();
        XMLPipelineContext context = createPipelineContext();              
        testable.setPipeline(factory.createPipeline(context));     
        XMLProcessTestable next = new XMLProcessTestable();
        
        testable.setNextProcess(next);
                                     
        // fire a couple of startDocument events
        testable.startDocument();
        testable.startDocument();
                                
        // defensive check
        next.assertEndDocumentNotInvoked();
            
        // fire an endDocument event
        testable.endDocument();            
        // ensure the event was forwarded.
        next.assertEndDocumentInvoked();
        // reset the next process
        next.reset();
        // defensive check
        next.assertEndDocumentNotInvoked();        
        // fire a second endDocument() event
        testable.endDocument();
        // ensure the second event was forwarded.
        next.assertEndDocumentInvoked();
        
        // defensive check
        next.reset();
        next.assertEndDocumentNotInvoked();
            
        XMLStreamingException xse = null;
        // fire a endDocument() event
        try {
            testable.endDocument();
        } catch (XMLStreamingException e) {
            // expected as no startDocument event was fired
            xse = e;
        }
        assertNotNull("XMLStreaming exception should be thrown if " +
                      "endDocumet() is received without preceeding " +
                      "startDocument", xse);
    }
    
    /**
     * Test that ensures that only the first endDocument() event is 
     * forwarded if the process has been started.
     * @throws Exception if an error occurs
     */        
    public void noTestEndDocument() throws Exception {
        // we need to ensure that if the process is started then 
        // only the first startDocument() event is forwarded
        XMLProcess testable = createTestableProcess();
        XMLProcessTestable next = new XMLProcessTestable();
        testable.setNextProcess(next);
        
        // start the process
        testable.startProcess();
        
        // fire a couple of startDocument events
        testable.startDocument();
        testable.startDocument();
        
        // defensive check
        next.assertEndDocumentNotInvoked();
        
        // fire an endDocument() event
        testable.endDocument();        
        // ensure the event was not forwarded.
        next.assertEndDocumentNotInvoked();        
        // reset the next process
        next.reset();        
        // defensive check
        next.assertEndDocumentNotInvoked();        
        // fire a second endDocument() event
        testable.endDocument();
        // ensure the second event was forwarded.
        next.assertEndDocumentInvoked();        
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 ===========================================================================
*/
