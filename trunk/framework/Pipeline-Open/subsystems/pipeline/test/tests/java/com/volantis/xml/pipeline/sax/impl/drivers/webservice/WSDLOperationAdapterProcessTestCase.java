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

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLWrappingProcessTestAbstract;
import com.volantis.xml.pipeline.testtools.mock.MockAttributes;

public class WSDLOperationAdapterProcessTestCase
        extends XMLWrappingProcessTestAbstract {

    protected XMLProcess createTestableProcess() {
        WSDLOperationAdapterProcess process = new WSDLOperationAdapterProcess();  
        XMLPipelineProcess pipeline = 
                new XMLPipelineProcessImpl(createPipelineContext());
        process.setPipeline(pipeline);
        return process;
    }

    /**
     * Construct a new WSDLOperationAdapterProcessTestCase
     * @param name The name of this testcase.
     */
    public WSDLOperationAdapterProcessTestCase(String name) {
        super(name);
    }

    /**
     * Test the processAttributes() method that should create an Operation and
     * set is as a property in the pipeline context.
     * @throws Exception
     */
    public void testProcessAttributes() throws Exception {
        WSDLOperationAdapterProcess process =
                (WSDLOperationAdapterProcess) createTestableProcess();

        String theWsdl = "the theWsdl";
        String thePortType = "the portType";
        String theOperationName = "the operation name";

        MockAttributes attrs = new MockAttributes();
        attrs.setValue("wsdl", theWsdl);
        attrs.setValue("portType", thePortType);
        attrs.setValue("operation", theOperationName);

        process.processAttributes(attrs);

        XMLPipelineContext context = process.getPipelineContext();
        Operation operation = (Operation) context.getProperty(Operation.class);

        assertNotNull(operation);

        assertEquals(theWsdl, operation.retrieveWSDLURI());
        assertEquals(thePortType, operation.getPortType());
        assertEquals(theOperationName, operation.getOperationName());
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
