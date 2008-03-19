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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-03-03     Doug            VBM:2003030405 - Created. Class that is used to
 *                              signal that a Streaming Error occured in an
 *                              XMLPipeline.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import org.xml.sax.Locator;

/**
 * Exception class used to indicate that a Streaming Error occurred whilst
 * an XMLProcess had started generating events but before it had finished.
 *
 * <p>Recovery from a Streaming Error requires that there is a process in the
 * pipeline after the process which reported the error. This process must
 * intercept all events it is given until it has received the complete
 * infoset. If no Streaming Errors occurred then this process can generate
 * events for the stored infoset to the rest of the pipeline. If a Streaming
 * Error does occur then it can discard the infoset. This guarantees that the
 * rest of the pipeline only processes a well formed infoset.</p>
 *
 * @deprecated The distinction between a {@link XMLStreamingException} and an
 * {@link XMLProcessingException} is irrelevant in terms of error recovery so
 * {@link XMLPipelineException} should be used instead.
 */
public class XMLStreamingException extends XMLProcessingException {

    /**
     * Construct a new XMLStreamingException instance
     * @param message The error or warning message, or null to use the
     * message from the embedded exception.
     * @param locator The locator object for the error or warning
     * (may be null).
     */
    public XMLStreamingException(String message, Locator locator) {
        super(message, locator);
    }

    /**
     * Construct a new XMLStreamingException instance
     * @param message The error or warning message, or null to use the
     * message from the embedded exception.
     * @param locator The locator object for the error or warning
     * (may be null).
     * @param e Another exception to embed in this one.
     */
    public XMLStreamingException(String message, Locator locator,
                                 Exception e) {
        super(message, locator, e);
    }

    /**
     * Construct a new XMLStreamingException instance
     * @param message The error or warning message.
     * @param publicId The public identifer of the entity that generated the
     * error or warning.
     * @param systemId The system identifer of the entity that generated the
     * error or warning.
     * @param lineNumber The line number of the end of the text that caused
     * the error or warning.
     * @param columnNumber The column number of the end of the text that cause
     * the error or warning.
     */
    public XMLStreamingException(String message,
                                 String publicId,
                                 String systemId,
                                 int lineNumber,
                                 int columnNumber) {
        super(message, publicId, systemId, lineNumber, columnNumber);
    }

    /**
     * Construct a new XMLStreamingException instance
     * @param message The error or warning message, or null to use the
     * message from the embedded exception.
     * @param publicId The public identifer of the entity that generated the
     * error or warning.
     * @param systemId The system identifer of the entity that generated the
     * error or warning.
     * @param lineNumber The line number of the end of the text that caused
     * the error or warning.
     * @param columnNumber The column number of the end of the text that cause
     * the error or warning.
     * @param e Another exception to embed in this one.
     */
    public XMLStreamingException(String message,
                                 String publicId,
                                 String systemId,
                                 int lineNumber,
                                 int columnNumber,
                                 Exception e) {
        super(message, publicId, systemId, lineNumber, columnNumber, e);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	337/1	doug	VBM:2003081004 Implemented XMLPipelineException class

 16-Jun-03	78/1	philws	VBM:2003061205 Add JSP test cases and debug some issues

 ===========================================================================
*/
