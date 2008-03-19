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
 * The <code>AbsoluteToRelativeURLAdapterProcess</code> implementation.
 *
 * This class creates the operation process and forwards the stop/startProcess,
 * startElement events to the operation process.
 *
 * Any attributes that match 'baseURL' are stored in the operation process.
 */
public class AbsoluteToRelativeURLAdapterProcess
        extends AbstractAdapterProcess {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The operationProcess for this adapter process.
     */
    private AbsoluteToRelativeURLOperationProcess operationProcess;

    /**
     * Default constructor that creates the operationProcess process.
     */
    public AbsoluteToRelativeURLAdapterProcess() {
        operationProcess = new AbsoluteToRelativeURLOperationProcess();
        setDelegate(operationProcess);
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {

        super.setPipeline(pipeline);
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

    // Javadoc inherited
    public void processAttributes(Attributes attributes) throws SAXException {

        // Example:
        // -------
        // <convertAbsoluteToRelativeURL baseURL="http://www.site.com/">
        //      <includeURI href="http://www.site.com/index.html"/>
        // </convertAbsoluteToRelativeURL>
        String value;
        if ((value = attributes.getValue("baseURL")) != null) {
            operationProcess.setBaseURL(value);
        }

        if ((value = attributes.getValue("substitutionPath")) != null) {
            operationProcess.setSubstitutionPath(value);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Mar-04	656/1	adrian	VBM:2004031602 Updated pipeline with DSB enhancement requests

 23-Mar-04	624/1	adrian	VBM:2004031904 Updated AbsoluteToRelativeURL process

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 ===========================================================================
*/
