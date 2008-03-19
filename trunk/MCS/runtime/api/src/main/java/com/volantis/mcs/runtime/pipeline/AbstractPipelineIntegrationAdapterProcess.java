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
package com.volantis.mcs.runtime.pipeline;

import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;

/**
 * An abstract base class for pipeline integration adapter processes.
 */
public class AbstractPipelineIntegrationAdapterProcess
        extends AbstractAdapterProcess {
    /**
     * Nesting depth of markup.
     */
    protected int depth;

    // javadoc inherited
    public void processAttributes(Attributes attributes) throws SAXException {
        // No attributes for this process
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline xmlPipeline) {
        super.setPipeline(xmlPipeline);

        depth = 0;
    }

    // javadoc inherited
    public void release() {
        depth = 0;

        super.release();
    }

    // Javadoc Inherited
    public void startDocument() throws SAXException {
        XMLProcess process = getConsumerProcess();
        if (process != null) {
            process.startDocument();
        } else {
            super.startDocument();
        }
    }

    // Javadoc Inherited
    public void endDocument() throws SAXException {
        XMLProcess process = getConsumerProcess();
        if (process != null) {
            process.endDocument();
        } else {
            super.endDocument();
        }
    }

    // Javadoc Inherited
    public void endPrefixMapping(String arg0) throws SAXException {
        XMLProcess process = getConsumerProcess();
        if (process != null) {
            process.endPrefixMapping(arg0);
        } else {
            super.endPrefixMapping(arg0);
        }
    }

    // Javadoc Inherited
    public void setDocumentLocator(Locator arg0) {
        XMLProcess process = getConsumerProcess();
        if (process != null) {
            process.setDocumentLocator(arg0);
        } else {
            super.setDocumentLocator(arg0);
        }
    }

    // Javadoc Inherited
    public void skippedEntity(String arg0) throws SAXException {
        XMLProcess process = getConsumerProcess();
        if (process != null) {
            process.skippedEntity(arg0);
        } else {
            super.skippedEntity(arg0);
        }
    }

    // Javadoc Inherited
    public void startPrefixMapping(String arg0, String arg1)
            throws SAXException {
        XMLProcess process = getConsumerProcess();
        if (process != null) {
            process.startPrefixMapping(arg0, arg1);
        } else {
            super.startPrefixMapping(arg0, arg1);
        }
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        depth++;

        super.startElement(namespaceURI, localName, qName, atts);
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        super.endElement(namespaceURI, localName, qName);

        depth--;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Dec-04	6541/1	philws	VBM:2004122101 Fix pipeline integration issues and tidy up parts of the build

 ===========================================================================
*/
