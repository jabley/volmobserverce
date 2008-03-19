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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.operations.transform;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This TransformationState represents the "transforming state" that
 * is all content received is the content that is to be transformed.
 * All Content forwarded to the operation process for transforming.
 * This state is the final state that all other states are trying
 * to reach
 */
public class Transforming extends TransformationState {

    public Transforming(TransformAdapterProcess transformAdapterProcess) {
        super(transformAdapterProcess);
    }

    // javadoc inherited
    public void initialise(String namespaceURI, String localName,
                           String qName, Attributes atts)
            throws SAXException {
        // start the operation process
        operationProcess.beginTransform();
        operationProcess.startProcess();
    }

    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
        operationProcess.setDocumentLocator(locator);
    }

    // javadoc inherited
    public void startDocument()
            throws SAXException {
        operationProcess.startDocument();
    }

    // javadoc inherited
    public void endDocument()
            throws SAXException {
        operationProcess.endDocument();
    }

    // javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        operationProcess.startPrefixMapping(prefix, uri);
    }

    // javadoc inherited
    public void endPrefixMapping(String prefix)
            throws SAXException {
        operationProcess.endPrefixMapping(prefix);
    }

    // javadoc inherited
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {
        operationProcess.startElement(namespaceURI, localName, qName, atts);
    }

    // javadoc inherited
    public void endElement(String namespaceURI, String localName,
                           String qName)
            throws SAXException {
        operationProcess.endElement(namespaceURI, localName, qName);
    }

    // javadoc inherited
    public void characters(char ch[], int start, int length)
            throws SAXException {
        operationProcess.characters(ch, start, length);
    }

    // javadoc inherited
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        operationProcess.ignorableWhitespace(ch, start, length);
    }

    // javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
        operationProcess.processingInstruction(target, data);
    }

    // javadoc inherited
    public void skippedEntity(String name)
            throws SAXException {
        operationProcess.skippedEntity(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 28-Apr-04	683/3	adrian	VBM:2004042607 Added copyright statements

 27-Apr-04	683/1	adrian	VBM:2004042607 Refactored states out of transform adapter process

 ===========================================================================
*/
