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

import com.volantis.xml.pipeline.sax.TestPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLWrappingProcessTestAbstract;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.MessageAdapterProcess;


public class MessageAdapterProcessTestCase
        extends XMLWrappingProcessTestAbstract {

    protected XMLProcess createTestableProcess() {
        MessageAdapterProcess process = new MessageAdapterProcess();
        XMLPipelineContext context = createPipelineContext();
        XMLPipelineProcess pipeline = new XMLPipelineProcessImpl(context);
        process.setElementDetails("namespace", "message", "wsd");

        process.setPipeline(pipeline);

        return process;
    }


    /**
     * Construct a new WSDLOperationAdapterProcessTestCase
     * @param name The name of this testcase.
     */
    public MessageAdapterProcessTestCase(String name) {
        super(name);
    }

    /**
     * This test replaces testStartElement(), testCharacters() and
     * testEndElement() because they all need to use the same
     * MessageAdapterProcess instance.
     * @throws Exception
     */
    public void testStartCharactersEndElement() throws Exception {
        XMLProcess process = createTestableProcess();

        // Don't call super.test.... methods because they expect
        // MessageAdapterProcess to forward the next adapter process which
        // it correctly does not.
        process.startProcess();
        process.startElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME, ATTS);
        process.characters(CH, START, LENGTH);
        process.endElement(NAMESPACE_URI, LOCAL_NAME, Q_NAME);
        process.stopProcess();
    }

    /**
     * Override to do nothig.
     * @see #testStartCharactersEndElement
     * @throws Exception
     */
    public void testStartElement() throws Exception {
    }

    /**
     * Override to do nothig.
     * @see #testStartCharactersEndElement
     * @throws Exception
     */
    public void testEndElement() throws Exception {
    }

    /**
     * Override to do nothig.
     * @see #testStartCharactersEndElement
     * @throws Exception
     */
    public void testCharacters() throws Exception {
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 01-Jul-03	165/1	allan	VBM:2003070101 Fix bug in MessageOperationProcess.startElement()

 ===========================================================================
*/
