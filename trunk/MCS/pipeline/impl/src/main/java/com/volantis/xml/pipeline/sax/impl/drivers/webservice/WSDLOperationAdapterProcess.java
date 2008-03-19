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
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The adapter process for wsdl-operation elements.
 */
public class WSDLOperationAdapterProcess extends AbstractAdapterProcess {

    /**
     * Create a WSDLOperation from the attributes and set this as the
     * operation in the RequestOperationProcess.
     */
    // rest of javadoc inherited.
    public void processAttributes(Attributes attributes) throws SAXException {
        String wsdlURI = attributes.getValue("wsdl");
        String portType = attributes.getValue("portType");
        String operationName = attributes.getValue("operation");

        WSDLOperation operation = new WSDLOperation(wsdlURI,
                                                    portType,
                                                    operationName);

        XMLPipelineContext context = getPipelineContext();

        context.setProperty(Operation.class, operation, false);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 ===========================================================================
*/
