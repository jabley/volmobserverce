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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.testtools.stubs.sax;

import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLProcessStub extends ContentHandlerStub implements XMLProcess {
    /**
     * Method to allow XMLProcess objects to be chained together.
     * Sets the next XMLProcess in the pipeline, this is the process
     * that will receive as its input the output of this XMLProcess 
     * @param next the next XMLProcess in the pipeline.
     */
    public void setNextProcess(XMLProcess next) {
    }

    /**
     * Get the next XMLProcess in the pipeline.
     * @return the next XMLProcess in the pipeline or null if there is 
     * no next process.
     */
    public XMLProcess getNextProcess() {
        return null;
    }

    /**
     * Called just before the process is added to the pipeline and should 
     * initialise any resources which are not dependent on information from 
     * the markup.
     * @param pipeline the current XMLPipelineContext.
     */
    public void setPipeline(XMLPipeline pipeline) {
    }

    // javadoc inherited
    public XMLPipeline getPipeline() {
        return null;
    }

    /**
     * Called just after the process is removed from the pipeline, either 
     * because the process has finished, or an error has occurred. This 
     * must release any resources which are owned by the process and should 
     * be written so it will work no matter what state the process is in.
     */
    public void release() {
    }

    /**
     * Called just after the process has been added to the pipeline. 
     * Simple processes may use this as a trigger to perform the operation.
     * @throws SAXException if error occurs. Implementations should use
     * the ErrorHandler interface to report Errors if possible. A SAXException
     * should only be thrown as a last resort
     */
    public void startProcess() throws SAXException {
    }

    /**
     * Called just before the process is removed from the pipeline.
     * Simple processes may use this as a trigger to perform the operation
     * @throws SAXException if error occurs. Implementations should use
     * the ErrorHandler interface to report Errors if possible. A SAXException
     * should only be thrown as a last resort
     */
    public void stopProcess() throws SAXException {
    }

    public void warning(SAXParseException e) throws SAXException {
    }

    public void error(SAXParseException e) throws SAXException {
    }

    public void fatalError(SAXParseException e) throws SAXException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 ===========================================================================
*/
