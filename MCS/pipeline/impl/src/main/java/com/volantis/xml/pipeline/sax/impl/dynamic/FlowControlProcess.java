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
package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.flow.FlowController;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

/**
 * An <code>XMLProcess</code> that implements the <code>FlowController</code>
 * interface so that the flow of SAX events through this process can be
 * controlled
 */
public class FlowControlProcess
        extends XMLProcessImpl
        implements FlowController {

    /**
     * The flow control manager that is managing this FlowController
     */
    private FlowControlManager flowControlManager;

    /**
     * Flag that indicates wether this process is performing flow control
     */
    private boolean inFlowControlMode;

    /**
     * Creates a new <code>FlowControlProcess</code> instance
     */
    public FlowControlProcess() {
        this.flowControlManager = null;
        this.inFlowControlMode = false;
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        // register this process with the flow control manager
        flowControlManager = getPipelineContext().getFlowControlManager();
        flowControlManager.addFlowController(this);
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        // unregister this process with the flow control manager
        flowControlManager.removeFlowController(this);
    }

    // javadoc inherited
    public void release() {
        super.release();
        this.inFlowControlMode = false;
    }

    // javadoc inherited
    public void beginFlowControl() {
        this.inFlowControlMode = true;
    }

    // javadoc inherited
    public void endFlowControl() {
        this.inFlowControlMode = false;
    }

    // Javadoc inherited
    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        if (!inFlowControlMode) {
            super.characters(ch, start, length);
        }
    }

    // Javadoc inherited
    public void endDocument() throws SAXException {
        if (!inFlowControlMode) {
            super.endDocument();
        }
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        if (!inFlowControlMode ||
                flowControlManager.handleEndElementEvent()) {
            super.endElement(namespaceURI, localName, qName);
        }
    }

    // Javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        if (!inFlowControlMode ||
                flowControlManager.forwardEndPrefixMappingEvent()) {
            super.endPrefixMapping(prefix);
        }
    }

    // Javadoc inherited
    public void ignorableWhitespace(char ch[],
                                    int start,
                                    int length) throws SAXException {
        if (!inFlowControlMode) {
            super.ignorableWhitespace(ch, start, length);
        }
    }

    // Javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
        if (!inFlowControlMode) {
            super.processingInstruction(target, data);
        }
    }

    // Javadoc inherited
    public void setDocumentLocator(Locator locator) {
        if (!inFlowControlMode) {
            super.setDocumentLocator(locator);
        }
    }

    // Javadoc inherited
    public void skippedEntity(String name) throws SAXException {
        if (!inFlowControlMode) {
            super.skippedEntity(name);
        }
    }

    // Javadoc inherited
    public void startDocument() throws SAXException {
        if (!inFlowControlMode) {
            super.startDocument();
        }
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        if (!inFlowControlMode) {
            super.startElement(namespaceURI, localName, qName, atts);
        } else {
            flowControlManager.handleStartElementEvent();
        }
    }

    // Javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        if (!inFlowControlMode) {
            super.startPrefixMapping(prefix, uri);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 ===========================================================================
*/
