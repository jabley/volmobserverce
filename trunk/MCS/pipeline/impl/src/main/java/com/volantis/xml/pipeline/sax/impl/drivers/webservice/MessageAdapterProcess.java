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
package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The adapter process for the message element.
 */
public class MessageAdapterProcess extends AbstractAdapterProcess {
    /**
     * Construct a new MessageAdapterProcess.
     */
    public MessageAdapterProcess() {
        MessageOperationProcess operationProcess =
                new MessageOperationProcess();
        setDelegate(operationProcess);
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline process) {
        super.setPipeline(process);
        MessageOperationProcess operationProcess =
                (MessageOperationProcess)getDelegate();
        operationProcess.setPipeline(process);
        operationProcess.setProcessNamespaceURI(processNamespaceURI);
    }

    // javadoc inherited
    public void processAttributes(Attributes attributes) throws SAXException {
        // There are no attributes to process.
    }

    // javadoc inherited from interface
    public void startProcess() throws SAXException {
        getDelegate().startProcess();
    }

    // javadoc inherited from interface
    public void stopProcess() throws SAXException {
        getDelegate().stopProcess();
    }

    /**
     * Override startElement() to forward the event to the delegate operation
     * process.
     */
    // rest of javadoc inherited
    public void startElement(String namespace, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
        getDelegate().startElement(namespace, localName, qName, attributes);
    }

    /**
     * Override endElement() to forward the event to the delegate operation
     * process.
     */
    // rest of javadoc inherited
    public void endElement(String namespace, String localName,
                           String qName)
            throws SAXException {
        getDelegate().endElement(namespace, localName, qName);
    }

    /**
     * Override characters() to forward the event to the delegate operation
     * process.
     */
    // rest of javadoc inherited
    public void characters(char[] buffer, int start, int offset)
            throws SAXException {
        getDelegate().characters(buffer, start, offset);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 ===========================================================================
*/
