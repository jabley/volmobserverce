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

package com.volantis.xml.pipeline.sax.convert;

import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The ElementCase process can be used in a pipeline to convert any element
 * within the <convertElementCase> tags to uppercase or lowercase (depending
 * on the parameter of the tag.
 */
public class ElementCaseAdapterProcess extends AbstractAdapterProcess {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    private ElementCaseOperationProcess operationProcess;

    public ElementCaseAdapterProcess() {
        operationProcess = new ElementCaseOperationProcess();
        setDelegate(operationProcess);
    }

    public void processAttributes(Attributes attributes) throws SAXException {
        String value;
        if ((value = attributes.getValue("mode")) != null) {
            operationProcess.setMode(value);
        }
    }

    // javadoc inherited
    public void startProcess() throws SAXException {

        super.startProcess();
        operationProcess.stopProcess();
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {

        operationProcess.stopProcess();
        super.stopProcess();
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {

        super.setPipeline(pipeline);

        setDelegate(operationProcess);
        operationProcess.setPipeline(pipeline);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 12-Aug-03	323/1	byron	VBM:2003080802 Provide ConvertElementCase pipeline process

 ===========================================================================
*/
