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

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The adapter process for the request element.
 */
public class RequestAdapterProcess extends AbstractAdapterProcess {

    public RequestAdapterProcess() {
        RequestOperationProcess operationProcess =
                new RequestOperationProcess();
        setDelegate(operationProcess);
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline process) {
        super.setPipeline(process);
        getDelegate().setPipeline(process);
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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 20-Jun-03	98/8	allan	VBM:2003022822 Updates for ContextInternals

 20-Jun-03	98/6	allan	VBM:2003022822 Add new classes to make them available to others

 19-Jun-03	98/4	allan	VBM:2003022822 WS Connector renamed to WS Driver

 18-Jun-03	98/1	allan	VBM:2003022822 Promote prelimary classes so that other can access them

 ===========================================================================
*/
