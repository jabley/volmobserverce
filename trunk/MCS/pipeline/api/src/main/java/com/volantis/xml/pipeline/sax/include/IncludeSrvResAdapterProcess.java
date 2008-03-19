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
 * $Header:  $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-May-03    Sumit           VBM:2003030612 - Created. An AdapterProcess for
 *                              server includes connections.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.include;

import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A IncludeSrvResAdapterProcess is an AdapterProcess that includes server based
 * resources
 */
public class IncludeSrvResAdapterProcess
        extends AbstractAdapterProcess {

    /**
     * The Volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Identifier for the path attribute
     */
    private static final String PATH_ATTRIBUTE = "path";

    /**
     * The OperationProcess that will perform the actual include operation
     */
    private IncludeSrvResOperationProcess includeSrvRes;

    /**
     * Creates a new URLConnectorProcess
     */
    public IncludeSrvResAdapterProcess() {
        includeSrvRes = createOperationProcess();
        setDelegate(includeSrvRes);
    }

    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);
        includeSrvRes.setPipeline(pipeline);
    }

    public void processAttributes(Attributes atts) {
        includeSrvRes.setPath(atts.getValue(PATH_ATTRIBUTE));
    }

    public void stopProcess()
            throws SAXException {
        includeSrvRes.doIncludeServerResource();
        includeSrvRes.stopProcess();
    }

    /**
     * Sets the parameter information on the operation process from processed
     * markup after verifying that the local name is 'param' and the namespace
     * URI is the same as this processes namespace. All other events are
     * passed through unhindered.
     */
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attrs) throws SAXException {
        if ("param".equals(localName) &&
                ((namespaceURI == null) ||
                processNamespaceURI.equals(namespaceURI))) {
            includeSrvRes.setParameter(attrs.getValue("name"),
                                       attrs.getValue("value"));
        } else {
            super.startElement(namespaceURI, localName, qName, attrs);
        }
    }

    /**
     * Ensures that the parameter markup consumed by {@link #startElement} is
     * also consumed here.
     */
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        if (!("param".equals(localName) &&
                ((namespaceURI == null) ||
                processNamespaceURI.equals(namespaceURI)))) {
            super.endElement(namespaceURI, localName, qName);
        }
    }

    protected IncludeSrvResOperationProcess createOperationProcess() {
        return new IncludeSrvResOperationProcess();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 14-Aug-03	367/1	philws	VBM:2003081403 Ensure startElement/endElement event consumption is balanced

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 ===========================================================================
*/
