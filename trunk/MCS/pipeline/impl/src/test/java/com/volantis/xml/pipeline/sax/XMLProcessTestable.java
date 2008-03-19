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
 * 12-May-03    Doug            VBM:2003030405 - Created. XMLProcess 
 *                              implementation to support testing
 * 27-May-03    Doug            VBM:2003030405 - Added a new constructor that
 *                              takes a String that is used as a process 
 *                              identifier. Modified the 
 *                              skippedEntity(String name) method so that the
 *                              skippedEntity event is forwarded with the 
 *                              process identifier appended to the name.                              
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import junit.framework.Assert;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * XMLProcess implementation to support testing
 */ 
public class XMLProcessTestable implements XMLProcess {

    /**
     * Flag used to record whether the setNextProcess method was invoked
     */
    boolean wasSetNextInvoked;

    /**
     * Flag used to record whether the getNextProcess method was invoked
     */
    boolean wasGetNextInvoked;
    
    /**
     * Flag used to record whether the setPipeline mehtod was invoked
     */
    boolean wasSetPipelineInvoked;

    /**
     * Flag used to record whether the release method was invoked
     */
    boolean wasReleaseInvoked;

    /**
     * Flag used to record whether the startProcess method was invoked
     */
    boolean wasStartProcessInvoked;

    /**
     * Flag used to record whether the stopProcess method was invoked
     */ 
    boolean wasStopProcessInvoked;

    /**
     * Flag used to record whether the stopDocumentLocator method was invoked
     */
    boolean wasSetDocumentLocatorInvoked;

    /**
     * Flag used to record whether the startDocument method was invoked
     */ 
    boolean wasStartDocumentInvoked;

    /**
     * Flag used to record whether the endDocument method was invoked
     */
    boolean wasEndDocumentInvoked;

    /**
     * Flag used to record whether the startPrefixMapping method was invoked
     */
    boolean wasStartPrefixMappingInvoked;

    /**
     * Flag used to record whether the endPrefixMapping method was invoked
     */
    boolean wasEndPrefixMappingInvoked;

    /**
     * Flag used to record whether the startElement method was invoked
     */
    boolean wasStartElementInvoked;

    /**
     * Flag used to record whether the endElement method was invoked
     */
    boolean wasEndElementInvoked;

    /**
     * Flag used to record whether the endElement method was invoked
     */
    boolean wasCharacterInvoked;

    /**
     * Flag used to record whether the characters method was invoked
     */
    boolean wasIgnorableWhiteSpaceInvoked;

    /**
     * Flag used to record whether the processingInstruction method was invoked
     */    
    boolean wasProcessingInstructionInvoked;

    /**
     * Flag used to record whether the skippedEntity method was invoked
     */
    boolean wasSkippedEntityInvoked;

    /**
     * Flag used to record whether the warning method was invoked
     */
    boolean wasWarningInvoked;

    /**
     * Flag used to record whether the error method was invoked
     */
    boolean wasErrorInvoked;

    /**
     * Flag used to record whether the fatalError method was invoked
     */
    boolean wasFatalErrorInvoked;
    
    /**
     * The XMLPipeline that this process has been added to.
     */ 
    XMLPipeline pipeline;
    
    /**
     * Locator that has been passed into this process
     */ 
    Locator locator;

    /**
     * prefix that has been passed into this process
     */
    String prefix;

    /**
     * uri that has been passed into this process
     */
    String uri;

    /**
     * namespace URI that has been passed into this process
     */
    String namespaceURI;

    /**
     * element localname that has been passed into this process
     */
    String localName;

    /**
     * qualified name that has been passed into this process
     */
    String qName;

    /**
     * Attributes that have been passed into this process
     */
    Attributes atts;

    /**
     * Characters that have been passed into this process
     */
    char[] ch;

    /**
     * start index that has been passed into this process
     */
    int start;

    /**
     * array length that has been passed into this process
     */
    int length;

    /**
     * target that has been passed into this process
     */
    String target;

    /**
     * data that has been passed into this process
     */
    String data;

    /**
     * name that has been passed into this process
     */
    String name;

    /**
     * error that has been passed into this process
     */
    SAXParseException error;
    
    /**
     * warning that has been passed into this process
     */
    SAXParseException warning;
    
    /**
     * fatal error that has been passed into this process
     */
    SAXParseException fatalError;

    /**
     * process that has been passed into this process
     */
    XMLProcess process;

    /**
     * process that has been passed into this process
     */    
    XMLProcess next;
    
    String processIndentifier; 
    
    public XMLProcessTestable() {        
    }
    
    /**
     * Creates a new XMLProcessTestable instance
     * @param processIdentifier a String that should be used as an identifier
     * for this process.  
     */ 
    public XMLProcessTestable(String processIdentifier) {
        this.processIndentifier = processIdentifier;        
    }
    
    /**
     * Ensure that the state of this testable process is in the "initail"
     * state. 
     */ 
    protected void assertState() {
        String msg = "XMLProcessTestable in illegal state for " +
                "performing test";
        Assert.assertTrue(msg, !wasSetNextInvoked);
        Assert.assertTrue(msg, !wasGetNextInvoked);
        Assert.assertTrue(msg, !wasSetPipelineInvoked);
        Assert.assertTrue(msg, !wasReleaseInvoked);
        Assert.assertTrue(msg, !wasStartProcessInvoked);
        Assert.assertTrue(msg, !wasStopProcessInvoked);
        Assert.assertTrue(msg, !wasSetDocumentLocatorInvoked);
        Assert.assertTrue(msg, !wasStartDocumentInvoked);
        Assert.assertTrue(msg, !wasEndDocumentInvoked);
        Assert.assertTrue(msg, !wasStartPrefixMappingInvoked);
        Assert.assertTrue(msg, !wasEndPrefixMappingInvoked);
        Assert.assertTrue(msg, !wasStartElementInvoked);
        Assert.assertTrue(msg, !wasEndElementInvoked);
        Assert.assertTrue(msg, !wasCharacterInvoked);
        Assert.assertTrue(msg, !wasIgnorableWhiteSpaceInvoked);
        Assert.assertTrue(msg, !wasProcessingInstructionInvoked);
        Assert.assertTrue(msg, !wasSkippedEntityInvoked);
        Assert.assertTrue(msg, !wasWarningInvoked);
        Assert.assertTrue(msg, !wasErrorInvoked);
        Assert.assertTrue(msg, !wasFatalErrorInvoked);

        Assert.assertNull(msg, pipeline);
        Assert.assertNull(msg, locator);
        Assert.assertNull(msg, prefix);
        Assert.assertNull(msg, uri);
        Assert.assertNull(msg, namespaceURI);
        Assert.assertNull(msg, localName);
        Assert.assertNull(msg, qName);
        Assert.assertNull(msg, atts);
        Assert.assertNull(msg, ch);
        Assert.assertEquals(msg, start, 0);
        Assert.assertEquals(msg, length, 0);
        Assert.assertNull(msg, target);
        Assert.assertNull(msg, data);
        Assert.assertNull(msg, name);
        Assert.assertNull(msg, error);
        Assert.assertNull(msg, fatalError);
        Assert.assertNull(msg, warning);
        Assert.assertNull(msg, process);
    }

    /**
     * Reset the state to the "initial" state
     */ 
    public void reset() {
        wasSetNextInvoked = false;
        wasGetNextInvoked = false;
        wasSetPipelineInvoked = false;
        wasReleaseInvoked = false;
        wasStartProcessInvoked = false;
        wasStopProcessInvoked = false;
        wasSetDocumentLocatorInvoked = false;
        wasStartDocumentInvoked = false;
        wasEndDocumentInvoked = false;
        wasStartPrefixMappingInvoked = false;
        wasEndPrefixMappingInvoked = false;
        wasStartElementInvoked = false;
        wasEndElementInvoked = false;
        wasCharacterInvoked = false;
        wasIgnorableWhiteSpaceInvoked = false;
        wasProcessingInstructionInvoked = false;
        wasSkippedEntityInvoked = false;
        wasWarningInvoked = false;
        wasErrorInvoked = false;
        wasFatalErrorInvoked = false;

        pipeline = null;
        locator = null;
        prefix = null;
        uri = null;
        namespaceURI = null;
        localName = null;
        qName = null;
        atts = null;
        ch = null;
        start = 0;
        length = 0;
        target = null;
        data = null;
        name = null;
        error = null;
        fatalError = null;
        warning = null;
        process = null;
    }

    // javadoc inherited
    public void setNextProcess(XMLProcess next) {
        wasSetNextInvoked = true;
        this.next = next;
    }

    /**
     * Performs an assert to ensure that the setNextProcess method 
     * was invoked
     */
    public void assertSetNextProcessInvoked() {
        Assert.assertTrue("setNextProcess() was not invoked", wasSetNextInvoked);
    }

    // javadoc inherited
    public XMLProcess getNextProcess() {
        wasGetNextInvoked = true;
        return next;
    }

    /**
     * Performs an assert to ensure that the getNextProcess method was invoked
     */ 
    public void assertGetNextProcessInvoked() {
        Assert.assertTrue("getNextProcess() was not invoked", wasGetNextInvoked);
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        wasSetPipelineInvoked = true;
        this.pipeline = pipeline;
    }

    // javadoc inherited
    public XMLPipeline getPipeline() {
        return pipeline;
    }

    /**
     * Performs an assert to ensure that the setPipeline() method was invoked
     * @param expectedPipeline the XMLPipeline that should have been set.
     */ 
    public void assertSetPipelineInvoked(XMLPipeline expectedPipeline) {
        Assert.assertTrue("initialze(XMLPipelineProcess) was not invoked",
                          wasSetPipelineInvoked);

        Assert.assertTrue("Unexpected XMLPipelineProcess passed to " +
                          "initialze(XMLPipelineProcess)",
                          pipeline == expectedPipeline);
    }

    /**
     * Performs an assert to ensure that the setPipeline() method was NOT
     * invoked
     */ 
    public void assertSetPipelineNotInvoked() {
        Assert.assertTrue("initialze(XMLPipelineContext) was invoked",
                          !wasSetPipelineInvoked);                               
    }
    
    // javadoc inherited
    public void release() {
        wasReleaseInvoked = true;        
    }

    /**
     * Performs an assert to ensure that the release method was invoked
     */
    public void assertReleaseInvoked() {
        Assert.assertTrue("release() was not invoked", wasReleaseInvoked);
    }

    /**
     * Performs an assert to ensure that the release method was NOT invoked
     */
    public void assertReleaseNotInvoked() {
        Assert.assertTrue("release() was invoked", !wasReleaseInvoked);
    }
    
    // javadoc inherited
    public void startProcess() throws SAXException {
        wasStartProcessInvoked = true;
    }

    /**
     * Performs an assert to ensure that the startProcess method was invoked
     */
    public void assertStartProcessInvoked() {
        Assert.assertTrue("startProcess() was not invoked",
                          wasStartProcessInvoked);
    }

    /**
     * Performs an assert to ensure that the startProcess method was 
     * NOT invoked
     */
    public void assertStartProcessNotInvoked() {
        Assert.assertTrue("startProcess() was invoked",
                          !wasStartProcessInvoked);
    }
    
    // javadoc inherited
    public void stopProcess() throws SAXException {
        wasStopProcessInvoked = true;
    }

    /**
     * Performs an assert to ensure that the stopProcess method was invoked
     */
    public void assertStopProcessInvoked() {
        Assert.assertTrue("stopProcess() was not invoked",
                          wasStopProcessInvoked);        
    }

    /**
     * Performs an assert to ensure that the stopProcess method was 
     * not invoked
     */
    public void assertStopProcessNotInvoked() {
        Assert.assertTrue("stopProcess() was invoked",
                          !wasStopProcessInvoked);
    }
    
    // javadoc inherited    
    public void setDocumentLocator(Locator locator) {
        wasSetDocumentLocatorInvoked = true;
        this.locator = locator;
        if(null != next) {
            next.setDocumentLocator(locator);
        }        
        
    }

    /**
     * Performs an assert to ensure that the setDocumentLocator method 
     * was invoked
     * @param locator The locator instance that should have been passed to
     * the setDocumentLocator method.
     */
    public void assertSetDocumentLocatorInvoked(Locator locator) {
        Assert.assertTrue("setDocumentLocator() was not invoked",
                          wasSetDocumentLocatorInvoked);

        Assert.assertTrue("Unexpected Locator passed to " +
                          "setDocumentLocator(Locator)",
                          this.locator == locator);
    }

    /**
     * Performs an assert to ensure that the setDocumentLocator method 
     * was not invoked
     */
    public void assertSetDocumentLocatorNotInvoked() {
        Assert.assertTrue("setDocumentLocator() was invoked",
                          !wasSetDocumentLocatorInvoked);
    }
    
    // javadoc inherited
    public void startDocument()
            throws SAXException {
        wasStartDocumentInvoked = true;
        if(null != next) {
            next.startDocument();
        }
    }

     /**
     * Performs an assert to ensure that the startDocument method was invoked
     */
    public void assertStartDocumentInvoked() {
        Assert.assertTrue("startDocument() was not invoked",
                          wasStartDocumentInvoked);
    }

    /**
     * Performs an assert to ensure that the startDocument method was 
     * NOT invoked
     */
    public void assertStartDocumentNotInvoked() {
        Assert.assertTrue("startDocument() was invoked",
                          !wasStartDocumentInvoked);
    }
    
    // javadoc inherited
    public void endDocument()
            throws SAXException {
        wasEndDocumentInvoked = true;
        if(null != next) {
            next.endDocument();
        }
    }

    /**
     * Performs an assert to ensure that the endDocument method was invoked
     */
    public void assertEndDocumentInvoked() {
        Assert.assertTrue("endDocument() was not invoked",
                          wasEndDocumentInvoked);
    }

    /**
     * Performs an assert to ensure that the endDocument method was 
     * NOT invoked
     */
    public void assertEndDocumentNotInvoked() {
        Assert.assertTrue("endDocument() was invoked",
                          !wasEndDocumentInvoked);
    }
    
    // javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        wasStartPrefixMappingInvoked = true;
        this.prefix = prefix;
        this.uri = uri;
        if(null != next) {
            next.startPrefixMapping(prefix, uri);
        }
    }

    /**
     * Performs an assert to ensure that the startPrefixMapping method 
     * was invoked with the correct arguments
     * @param prefix the prefix that should have been passed in
     * @param uri the uri that should have been passed in
     */
    public void assertStartPrefixMappingInvoked(String prefix,
                                                String uri) {
        Assert.assertTrue("startPrefixMapping() was not invoked",
                          wasStartPrefixMappingInvoked);

        Assert.assertTrue("Unexpected Prefix parameter passed to " +
                          "startPrefixMapping()",
                          this.prefix == prefix);

        Assert.assertTrue("Unexpected URI parameter passed to " +
                          "startPrefixMapping()",
                          this.uri == uri);
    }

    /**
     * Performs an assert to ensure that the startPrefixMapping method 
     * was NOT invoked
     */
    public void assertStartPrefixMappingNotInvoked() {
        Assert.assertTrue("startPrefixMapping() was invoked",
                          !wasStartPrefixMappingInvoked);
    }
    
    // javadoc inherited
    public void endPrefixMapping(String prefix)
            throws SAXException {
        wasEndPrefixMappingInvoked = true;
        this.prefix = prefix;
        if(null != next) {
            next.endPrefixMapping(prefix);
        }
    }

    /**
     * Performs an assert to ensure that the endPrefixMapping method 
     * was invoked with the correct arguments
     * @param prefix the prefix that should have been passed in
     */
    public void assertEndPrefixMappingInvoked(String prefix) {
        Assert.assertTrue("endPrefixMapping() was not invoked",
                          wasEndPrefixMappingInvoked);

        Assert.assertTrue("Unexpected Prefix parameter passed to " +
                          "endPrefixMapping()",
                          this.prefix == prefix);
    }

    /**
     * Performs an assert to ensure that the endPrefixMapping method 
     * was NOT invoked.
     */
    public void assertEndPrefixMappingNotInvoked() {
        Assert.assertTrue("endPrefixMapping() was invoked",
                          !wasEndPrefixMappingInvoked);
    }
    
    // javadoc inherited
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {
        wasStartElementInvoked = true;
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.qName = qName;
        this.atts = atts;
        if(null != next) {
            next.startElement(namespaceURI, localName, qName, atts);
        }
    }

    /**
     * Performs an assert to ensure that the startElement method 
     * was invoked with the correct arguments
     * @param namespaceURI the namespace URI that should have been passed in
     * @param localName the uri that should have been passed in
     * @param qName the qualified name that should have been passed in
     * @param atts the Attributes that should have been passed in
     */
    public void assertStartElementInvoked(String namespaceURI,
                                          String localName,
                                          String qName,
                                          Attributes atts) {
        Assert.assertTrue("startElement() was not invoked",
                          wasStartElementInvoked);

        Assert.assertTrue("Unexpected processNamespaceURI parameter passed to " +
                          "startElement()",
                          this.namespaceURI == namespaceURI);

        Assert.assertTrue("Unexpected processLocalName parameter passed to " +
                          "startElement()",
                          this.localName == localName);

        Assert.assertTrue("Unexpected qName parameter passed to " +
                          "startElement()",
                          this.qName == qName);

        if (this.atts != null && atts != null) {
            Assert.assertTrue("Unexpected atts parameter passed to " +
                          "startElement()",
                          this.atts.getLength() == atts.getLength());
        } else {
            if (atts == null) {
                Assert.fail("Null attributes passed to startElement.");
            } else if (this.atts == null) {
                Assert.fail("Expected null attributes to " +
                        "be passed to startElement");
            }

        }

    }

    /**
     * Performs an assert to ensure that the startElement method 
     * was NOT invoked.
     */
    public void assertStartElementNotInvoked() {
        Assert.assertTrue("startElement() was invoked",
                          !wasStartElementInvoked);
    }
    
    // javadoc inherited
    public void endElement(String namespaceURI, String localName,
                           String qName)
            throws SAXException {
        wasEndElementInvoked = true;
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.qName = qName;
        if(null != next) {
            next.endElement(namespaceURI, localName, qName);
        }
    }

    /**
     * Performs an assert to ensure that the endElement method 
     * was invoked with the correct arguments
     * @param namespaceURI the namespace URI that should have been passed in
     * @param localName the uri that should have been passed in
     * @param qName the qualified name that should have been passed in
     */
    public void assertEndElementInvoked(String namespaceURI,
                                        String localName,
                                        String qName) {
        Assert.assertTrue("endElement() was not invoked",
                          wasEndElementInvoked);

        Assert.assertTrue("Unexpected processNamespaceURI parameter passed to " +
                          "endElement()",
                          this.namespaceURI == namespaceURI);

        Assert.assertTrue("Unexpected processLocalName parameter passed to " +
                          "endElement()",
                          this.localName == localName);

        Assert.assertTrue("Unexpected qName parameter passed to " +
                          "endElement()",
                          this.qName == qName);
    }

    /**
     * Performs an assert to ensure that the endElement method 
     * was not invoked.
     */
    public void assertEndElementNotInvoked() {
        Assert.assertTrue("endElement() was invoked",
                          !wasEndElementInvoked);
    }
    
    // javadoc inherited
    public void characters(char ch[], int start, int length)
            throws SAXException {
        wasCharacterInvoked = true;
        this.ch = ch;
        this.start = start;
        this.length = length;
        if(null != next) {
            next.characters(ch, start, length);
        }
    }

    /**
     * Performs an assert to ensure that the characters method 
     * was invoked with the correct arguments
     * @param ch the characters that should have been passed in
     * @param start the start index that should have been passed in
     * @param length the length that should have been passed in
     */
    public void assertCharactersInvoked(char ch[], int start, int length) {

        Assert.assertTrue("characters() was not invoked",
                          wasCharacterInvoked);

        Assert.assertTrue("Unexpected ch parameter passed to " +
                          "characters()",
                          this.ch == ch);

        Assert.assertTrue("Unexpected start parameter passed to " +
                          "characters()",
                          this.start == start);

        Assert.assertTrue("Unexpected length parameter passed to " +
                          "characters()",
                          this.length == length);
    }

    /**
     * Performs an assert to ensure that the characters method 
     * was NOT invoked.
     */
    public void assertCharactersNotInvoked() {

            Assert.assertTrue("characters() was invoked",
                              !wasCharacterInvoked);
    }
        
    // javadoc inherited
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        wasIgnorableWhiteSpaceInvoked = true;
        this.ch = ch;
        this.start = start;
        this.length = length;
        if(null != next) {
            next.ignorableWhitespace(ch, start, length);
        }
    }

    /**
     * Performs an assert to ensure that the ignorableWhitespace method 
     * was invoked with the correct arguments
     * @param ch the characters that should have been passed in
     * @param start the start index that should have been passed in
     * @param length the length that should have been passed in
     */    
    public void assertIgnorableWhitespaceInvoked(char ch[],
                                                 int start,
                                                 int length) {

        Assert.assertTrue("ignorableWhitespace() was not invoked",
                          wasIgnorableWhiteSpaceInvoked);

        Assert.assertTrue("Unexpected ch parameter passed to " +
                          "ignorableWhitespace()",
                          this.ch == ch);

        Assert.assertTrue("Unexpected start parameter passed to " +
                          "ignorableWhitespace()",
                          this.start == start);

        Assert.assertTrue("Unexpected length parameter passed to " +
                          "ignorableWhitespace()",
                          this.length == length);
    }

    /**
     * Performs an assert to ensure that the ignorableWhitespace method 
     * was invoked with the correct arguments
     */
    public void assertIgnorableWhitespaceNotInvoked() {
        Assert.assertTrue("ignorableWhitespace() was invoked",
                          !wasIgnorableWhiteSpaceInvoked);
    }
    
    // javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
        wasProcessingInstructionInvoked = true;
        this.target = target;
        this.data = data;
        if(null != next) {
            next.processingInstruction(target, data);
        }        
    }

    /**
     * Performs an assert to ensure that the processingInstruction method 
     * was invoked with the correct arguments
     * @param target the target that should have been passed in
     * @param data the data that should have been passed in
     */ 
    public void assertProcessingInstructionInvoked(String target,
                                                   String data) {

        Assert.assertTrue("processingInstruction() was not invoked",
                          wasProcessingInstructionInvoked);

        Assert.assertTrue("Unexpected target parameter passed to " +
                          "processingInstruction()",
                          this.target == target);

        Assert.assertTrue("Unexpected data parameter passed to " +
                          "processingInstruction()",
                          this.data == data);

    }

    /**
     * Performs an assert to ensure that the processingInstruction method 
     * was NOT invoked.
     */
    public void assertProcessingInstructionNotInvoked() {
        Assert.assertTrue("processingInstruction() was invoked",
                          !wasProcessingInstructionInvoked);
    }
    
    // javadoc inherited
    public void skippedEntity(String name)
            throws SAXException {
        
         
        wasSkippedEntityInvoked = true;
        this.name = name;
        if(null != next) {
            if(processIndentifier != null) {
                next.skippedEntity(name + processIndentifier);                        
            } else {
                next.skippedEntity(name);                        
            }
        }
    }

    /**
     * Performs an assert to ensure that the skippedEntity method 
     * was invoked with the correct arguments
     * @param name the name that should have been passed in     
     */
    public void assertSkippedEntityInvoked(String name) {

        Assert.assertTrue("skippedEntity() was not invoked",
                          wasSkippedEntityInvoked);

        Assert.assertTrue("Unexpected name parameter passed to " +
                          "skippedEntity() " + name + ", " +
                          this.name,
                          this.name.equals(name));

    }
    
    /**
     * Performs an assert to ensure that the skippedEntity method 
     * was NOT invoked.    
     */
    public void assertSkippedEntityNotInvoked() {

            Assert.assertTrue("skippedEntity() was invoked",
                              !wasSkippedEntityInvoked);
    }

    // javadoc inherited
    public void warning(SAXParseException exception)
            throws SAXException {
        wasWarningInvoked = true;
        warning = exception;
        if(null != next) {
            next.warning(exception);
        }
    }

    /**
     * Performs an assert to ensure that the warning method 
     * was invoked with the correct arguments
     * @param exception the warning that should have been passed in
     */
    public void assertWarningInvoked(SAXParseException exception) {

        Assert.assertTrue("warning() was not invoked",
                          wasWarningInvoked);

        Assert.assertTrue("Unexpected exception parameter passed to " +
                          "warning()",
                          warning == exception);

    }

    /**
     * Performs an assert to ensure that the warning method 
     * was NOT invoked.
     */    
    public void assertWarningNotInvoked() {

        Assert.assertTrue("warning() was invoked",
                          !wasWarningInvoked);
    }
    
    // javadoc inherited
    public void error(SAXParseException exception)
            throws SAXException {
        wasErrorInvoked = true;
        error = exception;
        if (null != next) {
            next.error(exception);
        }
    }

    /**
     * Performs an assert to ensure that the error method 
     * was invoked with the correct arguments
     * @param exception the error that should have been passed in
     */
    public void assertErrorInvoked(SAXParseException exception) {

        Assert.assertTrue("error() was not invoked",
                          wasErrorInvoked);

        Assert.assertTrue("Unexpected exception parameter passed to " +
                          "error()",
                          error == exception);

    }

    /**
     * Performs an assert to ensure that the error method 
     * was NOT invoked.
     */
    public void assertErrorNotInvoked() {

        Assert.assertTrue("error() was invoked",
                          !wasErrorInvoked);
    }
    
    // javadoc inherited
    public void fatalError(SAXParseException exception)
            throws SAXException {
        wasFatalErrorInvoked = true;
        fatalError = exception;
        if(null != next) {
            next.fatalError(exception);
        } else {
            throw exception;
        }
    }

    /**
     * Performs an assert to ensure that the fatalError method 
     * was invoked with the correct arguments
     * @param exception the fatal error that should have been passed in
     */
    public void assertFatalErrorInvoked(SAXParseException exception) {

        Assert.assertTrue("fatalError() was not invoked",
                          wasFatalErrorInvoked);

        Assert.assertTrue("Unexpected exception parameter passed to " +
                          "fatalError()",
                          fatalError == exception);

    }

    /**
     * Performs an assert to ensure that the fatalError method 
     * was NOT invoked.
     */
    public void assertFatalErrorNotInvoked() {

            Assert.assertTrue("fatalError() was invoked",
                              !wasFatalErrorInvoked);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 05-Aug-03	290/1	doug	VBM:2003080412 Provided DynamicElementRule implementation for adding Adapters to a pipeline

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 ===========================================================================
*/
