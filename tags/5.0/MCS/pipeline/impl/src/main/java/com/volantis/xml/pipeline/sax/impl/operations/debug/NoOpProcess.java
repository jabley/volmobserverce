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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.impl.operations.debug;

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLProcess;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class provides an empty implementation of XMLProcess which does nothing.
 * All events sent to this class are silently consumed.
 */
public final class NoOpProcess implements XMLProcess {

    /**
     * Overridden to do nothing.
     */
    public void setNextProcess(XMLProcess next) {
    }

    /**
     * Overridden to do nothing.
     */
    public XMLProcess getNextProcess() {
        return null;
    }

    /**
     * Overridden to do nothing.
     */
    public void setPipeline(XMLPipeline pipeline) {
    }

    /**
     * Overridden to do nothing.
     */
    public XMLPipeline getPipeline() {
        return null;
    }

    /**
     * Overridden to do nothing.
     */
    public void startProcess() throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void stopProcess() throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void setDocumentLocator(Locator locator) {
    }

    /**
     * Overridden to do nothing.
     */
    public void startDocument()
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void endDocument()
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void endPrefixMapping(String prefix)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void endElement(String namespaceURI, String localName,
                           String qName)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void skippedEntity(String name)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void warning(SAXParseException exception)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void error(SAXParseException exception)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void fatalError(SAXParseException exception)
            throws SAXException {
    }

    /**
     * Overridden to do nothing.
     */
    public void release() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-May-05	8403/1	pcameron	VBM:2005052006 Added a no-op process serialiser to Pipeline for JSP tags

 ===========================================================================
*/
