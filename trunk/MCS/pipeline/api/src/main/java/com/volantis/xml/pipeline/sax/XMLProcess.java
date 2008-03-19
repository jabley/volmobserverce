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
 *
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-03-03     Doug            VBM:2003030405 - Created. Interface for an
 *                              XMLProcess that resides in an XML XMLPipeline.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * An XMLProcess is a component that accepts SAX events as an input,
 * performing some operation or process on this input in order to generate
 * SAX events as an output. XMLProcess are linked together to create an
 * XML XMLPipeline.
 * <p>Users should not implement this interface directly, rather they should
 * use the {@link XMLProcessImpl} as it is likely that additional methods
 * will be added to this interface in future as required. e.g. it may be
 * necessary in future to extends some of the other SAX events interfaces such
 * as {@link org.xml.sax.EntityResolver} and possibly even some of the optional
 * ones such as {@link org.xml.sax.ext.DeclHandler} and
 * {@link org.xml.sax.ext.LexicalHandler}.</p>
 * <h2>Warning</h2>
 * <p>Processes must not rely on the qName attribute of the
 * {@link ContentHandler#startElement} and {@link ContentHandler#endElement}
 * being available. The pipeline requires namespace support and so the
 * namespaceURI and localName attributes are always set but the qName may not
 * be.</p>
 * <h2>Unsupported Methods</h2>
 * <p>The following methods from {@link ContentHandler} are not used within the
 * pipeline. It is a fatal error if any of them methods are invoked.</p>
 * <ul>
 *     <li>{@link #setDocumentLocator}</li>
 *     <li>{@link #startDocument}</li>
 *     <li>{@link #endDocument}</li>
 *     <li>{@link #skippedEntity}</li>
 * </ul>
 * <p>In addition processes must not rely on receiving the following events as
 * they may eventually join the above list of unsupported methods.</p>
 * <ul>
 *   <li>{@link ContentHandler#startPrefixMapping}</li>
 *   <li>{@link ContentHandler#endPrefixMapping}</li>
 * </ul>
 * @see <a href="package-summary.html#contextual-information">Contextual Information</a>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public interface XMLProcess
        extends ContentHandler, ErrorHandler, ResourceOwner {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Method to allow XMLProcess objects to be chained together.
     * Sets the next XMLProcess in the pipeline, this is the process
     * that will receive as its input the output of this XMLProcess
     * @param next the next XMLProcess in the pipeline.
     */
    public void setNextProcess(XMLProcess next);

    /**
     * Get the next XMLProcess in the pipeline.
     * @return the next XMLProcess in the pipeline or null if there is
     * no next process.
     */
    public XMLProcess getNextProcess();

    /**
     * Set the pipeline to which this process belongs.
     * <p>This method must only be called by the XMLPipeline just before it
     * adds the process into the pipeline.</p>
     * @param pipeline The XMLPipeline that this process belongs to.
     */
    public void setPipeline(XMLPipeline pipeline);

    /**
     * Get the pipeline to which this process belongs.
     *
     * @return The pipeline to which this process belongs, will be null if the
     * process is not in a pipeline.
     */
    public XMLPipeline getPipeline();

    /**
     * Called just after the process has been added to the pipeline.
     * Simple processes may use this as a trigger to perform the operation.
     * @throws SAXException if error occurs. Implementations should use
     * the ErrorHandler interface to report Errors if possible. A SAXException
     * should only be thrown as a last resort
     */
    public void startProcess() throws SAXException;

    /**
     * Called just before the process is removed from the pipeline.
     * Simple processes may use this as a trigger to perform the operation
     * @throws SAXException if error occurs. Implementations should use
     * the ErrorHandler interface to report Errors if possible. A SAXException
     * should only be thrown as a last resort
     */
    public void stopProcess() throws SAXException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jan-04	529/1	adrian	VBM:2004011904 Pipeline API updates in preparation for fully integrating ContextUpdating/Annotating processes

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 ===========================================================================
*/
