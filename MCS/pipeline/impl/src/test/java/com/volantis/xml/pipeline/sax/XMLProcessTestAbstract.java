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
 * 12-May-03    Doug            VBM:2003030405 - Created. Abstract TestCase 
 *                              that should be used as the base class for
 *                              testing XMLProcess implelmentations.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

/**
 * Abstract TestCase that should be used as the base class for testing 
 * XMLProcess implelmentations.
 */ 
public abstract class XMLProcessTestAbstract extends TestCaseAbstract  {

    protected static final String PREFIX = "testPrefix";

    protected static final String URI = "testURI";

    protected static final String NAMESPACE_URI = "testNamespace";

    protected static final String LOCAL_NAME = "testLocalName";

    protected static final String Q_NAME = "testQName";

    protected static final Attributes ATTS = new AttributesImpl();

    protected static final char[] CH = new char[]{'a', 'b', 'c', 'd'};

    protected static final int START = 0;

    protected static final int LENGTH = 4;

    protected static final String TARGET = "testTarget";

    protected static final String DATA = "testData";

    protected static final String NAME = "testName";

    protected static final SAXParseException EXCEPTION = 
            new ExtendedSAXParseException("message", "publicID", "systemID", 0, 0);

    protected static final XMLProcess PROCESS = new XMLProcessImpl();

    protected static final Locator LOCATOR = new LocatorImpl();
    
    /**
     * Instance of a PipelineFactory 
     */ 
    protected XMLPipelineFactory factory;

    /**
     * Initialise.
     */
    protected XMLProcessTestAbstract() {
        factory = new TestPipelineFactory();
    }

    /**
     * Factory method for creating an XMLProcess that will be used as 
     * a next process for the process being tested
     * @return
     */ 
    protected XMLProcessTestable createNextProcess() {
        return new XMLProcessTestable();
    }
    
    /**
     * Factory method that creates a XMLPipelineContext
     * @return an XMLPipelineContext
     */ 
    protected XMLPipelineContext createPipelineContext() {
        return 
            factory.createPipelineContext(createPipelineConfiguration(),
                                          createRootEnvironmentInteraction());        
    }
    
    /**
     * Factory method for creating a root EnvironmentInteraction instance
     * This implementaion returns null. 
     * @return an EnvironmentInteraction instance
     */ 
    protected EnvironmentInteraction createRootEnvironmentInteraction() {
        return null;
    }
    
    /**
     * Factory method for creatin an XMLPipelineConfiguration
     * @return an XMLPipelineConfiguration instance.
     */                                                        
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        return factory.createPipelineConfiguration();    
    }
    
    protected abstract XMLProcess createTestableProcess() throws Exception;
    
    public abstract void testSetPipeline() throws Exception;

    public abstract void testRelease() throws Exception;

    public abstract void testStartProcess() throws Exception;

    public abstract void testStopProcess() throws Exception;

    
    public void testCharacters() throws Exception {
        testCharacters(createTestableProcess());
    }


    public void testEndDocument() throws Exception {
        testEndDocument(createTestableProcess());
    }

    public void testEndElement() throws Exception {
        testEndElement(createTestableProcess());        
    }

    public void testEndPrefixMapping() throws Exception {
        testEndPrefixMapping(createTestableProcess());
    }

    public void testIgnorableWhitespace() throws Exception {
        testIgnorableWhitespace(createTestableProcess());
    }

    public void testProcessingInstruction() throws Exception {
        testProcessingInstruction(createTestableProcess());        
    }

    public void testSetDocumentLocator() throws Exception {
        testSetDocumentLocator(createTestableProcess());
    }

    public void testSkippedEntity() throws Exception {
        testSkippedEntity(createTestableProcess());
    }

    public void testStartDocument() throws Exception {
        testStartDocument(createTestableProcess());
    }

    public void testStartElement() throws Exception {
        testStartElement(createTestableProcess());        
    }

    public void testStartPrefixMapping() throws Exception {
        testStartPrefixMapping(createTestableProcess());
    }

    public void testError() throws Exception {
        testError(createTestableProcess());
    }

    public void testFatalError() throws Exception {
        testFatalError(createTestableProcess());
    }

    public void testWarning() throws Exception {
        testWarning(createTestableProcess());
    }

    protected void testSetDocumentLocator(XMLProcess testable) {

        // ensure that a runtime exception occurs on invocation of
        // startDocument.
        XMLProcessTestable next = createNextProcess();
        testable.setNextProcess(next);

        try {
            testable.setDocumentLocator(LOCATOR);
            fail("setDocumentLocator() should throw UnsupportedOperationException");
        } catch (Throwable t) {
            // nothing to do
        }
        next.assertSetDocumentLocatorNotInvoked();
    }

    protected void testStartDocument(XMLProcess testable) throws Exception {

        // ensure that a runtime exception occurs on invocation of
        // startDocument.
        XMLProcessTestable next = createNextProcess();
        testable.setNextProcess(next);

        try {
            testable.startDocument();
            fail("StartDocument() should throw UnsupportedOperationException");
        } catch (Throwable t) {
            // nothing to do
        }
        next.assertStartDocumentNotInvoked();
    }

    protected void testEndDocument(XMLProcess testable) throws Exception {

        // ensure that a runtime exception occurs on invocation of
        // endDocument.
        XMLProcessTestable next = createNextProcess();
        testable.setNextProcess(next);

        try {
            testable.endDocument();
            fail("endDocument() should throw UnsupportedOperationException");
        } catch (Throwable t) {
            // nothing to do
        }
        next.assertEndDocumentNotInvoked();
    }

    protected void testStartPrefixMapping(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process
        testable.startPrefixMapping(PREFIX, URI);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        
        testable.startPrefixMapping(PREFIX, URI);
        
        next.assertStartPrefixMappingInvoked(PREFIX, URI);        
    }

    protected void testEndPrefixMapping(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process
        testable.endPrefixMapping(PREFIX);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        
        testable.endPrefixMapping(PREFIX);
        
        next.assertEndPrefixMappingInvoked(PREFIX);
    }

    protected void testStartElement(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process
        testable.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        
        testable.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
        
        next.assertStartElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
        
    }

    protected void testEndElement(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process
        testable.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        
        testable.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
        
        next.assertEndElementInvoked(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
    }

    protected void testCharacters(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process
        testable.characters(CH, START, LENGTH);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        
        testable.characters(CH, START, LENGTH);
            
        next.assertCharactersInvoked(CH, START, LENGTH);
    }

    protected void testIgnorableWhitespace(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process
        testable.ignorableWhitespace(CH, START, LENGTH);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        
        testable.ignorableWhitespace(CH, START, LENGTH);
            
        next.assertIgnorableWhitespaceInvoked(CH, START, LENGTH);
    }

    protected void testProcessingInstruction(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process
        testable.processingInstruction(TARGET, DATA);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        
        testable.processingInstruction(TARGET, DATA);
            
        next.assertProcessingInstructionInvoked(TARGET, DATA);        
    }

    protected void testSkippedEntity(XMLProcess testable) throws Exception {

        // ensure that a runtime exception occurs on invocation of
        // skippedEntity.
        XMLProcessTestable next = createNextProcess();
        testable.setNextProcess(next);

        try {
            testable.skippedEntity(NAME);
            fail("skippedEntity() should throw UnsupportedOperationException");
        } catch (Throwable t) {
            // nothing to do
        }
        next.assertSkippedEntityNotInvoked();
    }

    protected void testWarning(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process
        testable.warning(EXCEPTION);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        
        testable.warning(EXCEPTION);
            
        next.assertWarningInvoked(EXCEPTION);        
    }

    protected void testError(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no 
        // next process        
        testable.error(EXCEPTION);
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();                
        testable.setNextProcess(next);
        testable.error(EXCEPTION);
                    
        next.assertErrorInvoked(EXCEPTION);
    }

    protected void testFatalError(XMLProcess testable) throws Exception {

        // ensure that the error is re thrown if no error handler has been
        // provided
        try {
            testable.fatalError(EXCEPTION);
        } catch (SAXException e) {
            assertTrue("XMLProcessImpl should throw fatalError if no" +
                       " ErrorHandler is set", e == EXCEPTION);
        }
        
        // ensure that the event is passed to the next process
        XMLProcessTestable next = createNextProcess();
        try {
            testable.setNextProcess(next);        
            testable.fatalError(EXCEPTION);
        } catch (SAXException e) {
            assertTrue("XMLProcessImpl should throw fatalError if no" +
                       " ErrorHandler is set", e == EXCEPTION);
        }
            
        next.assertFatalErrorInvoked(EXCEPTION);
        
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

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 11-Jun-03	34/2	allan	VBM:2003022820 SQL Connector

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
