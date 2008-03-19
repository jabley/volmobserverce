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

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The URL To URLC process can be used in a pipeline to convert URLs defined
 * in specific attributes in specific elements in specific namespaces to
 * URLCs, possibly renaming the attribute in the process.
 */
public class URLToURLCAdapterProcess extends AbstractAdapterProcess {
    /**
     * @link aggregation
     * @supplierRole operationProcess
     * @supplierCardinality 1
     */
    private URLToURLCOperationProcess operationProcess = null;

    /**
     * Creates a new <code>URLToURLCAdapterProcess</code> instance
     */
    public URLToURLCAdapterProcess() {
        operationProcess = createOperationProcess();
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);

        setDelegate(operationProcess);

        operationProcess.setPipeline(pipeline);
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        super.startProcess();

        operationProcess.startProcess();
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        operationProcess.stopProcess();

        super.stopProcess();
    }

    // javadoc inherited
    public void release() {
        operationProcess.release();

        setDelegate(null);

        super.release();
    }

    // javadoc inherited
    public void processAttributes(Attributes attributes) throws SAXException {
        String value;

        if ((value = attributes.getValue("server")) != null) {
            operationProcess.setServerURL(value);
        }
    }

    /**
     * Creates and returns a new operation process appropriate to this adapter
     * process.
     *
     * @return a new operation process
     */
    protected URLToURLCOperationProcess createOperationProcess() {
        return new URLToURLCOperationProcess();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 07-Aug-03	299/5	philws	VBM:2003080504 Remove the relativeWidth and maxFileSize attributes from the URL to URLC converter following architectural change

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored URLToURLCConverter process to use DynamicElementRules

 06-Aug-03	299/1	philws	VBM:2003080504 Pipeline work for the DSB convertImageURLToDMS process

 ===========================================================================
*/
