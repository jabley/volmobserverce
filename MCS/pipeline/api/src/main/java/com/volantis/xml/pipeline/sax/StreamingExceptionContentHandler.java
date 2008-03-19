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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-May-03    Phil W-S        VBM:2003030610 - Created. ContentHandler that
 *                              generates XMLStreamingExceptions which are used
 *                              to raise warning, error or fatalError on an
 *                              error handler (or throws the exception when a
 *                              fatalError is required without an error
 *                              handler). setDocumentLocator, whitespace
 *                              characters and ignoredWhitespace events are
 *                              quietly consumed.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The SAX event methods (except {@link #setDocumentLocator}) invoke the given
 * error handler (if available) to raise fatal errors, errors or warnings
 * consisting of an {@link XMLStreamingException} (depending on the actual
 * event in question). A pipeline context is required in order to allow access
 * to the current document locator.
 * <p/>
 * Note that {@link #setDocumentLocator},{@link #startDocument},
 * {@link #endDocument} whitespace {@link #characters} and
 * {@link #ignorableWhitespace} events are quietly consumed rather than raising
 * an exception against the error handler.
 */
public class StreamingExceptionContentHandler implements ContentHandler {
    /**
     * The pipeline context from which the document locator can be obtained.
     *
     * @supplierRole context
     * @supplierCardinality 1
     */
    protected XMLPipelineContext context;

    /**
     * The optional error handler used to handle the fatal errors, errors
     * and warnings raised.
     *
     * @supplierRole handler
     * @supplierCardinality 0..1
     */
    protected ErrorHandler handler;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param context the pipeline context from which the document locator
     *                may be retrieved. Must be provided.
     * @param handler the error handler to which errors should be reported.
     *                May be null.
     * @throws NullPointerException if a null value is given for context
     */
    public StreamingExceptionContentHandler(XMLPipelineContext context,
                                            ErrorHandler handler) {
        if (context == null) {
            throw new NullPointerException(
                    "A pipeline context must be provided");
        } else {
            this.context = context;
            this.handler = handler;
        }
    }

    /**
     * Reports an XMLStreamingException for the given type of SAX event as a
     * fatal error using the error handler, if available. Processing of the
     * exception is performed by {@link #error}.
     *
     * @param type the SAX event type for which the error is to be reported
     * @throws SAXException if the error handler can't handle the exception or
     *                      there is no error handler
     */
    protected void reportFatalError(String type) throws SAXException {
        XMLStreamingException e = new XMLStreamingException(
                "Unexpected " + type + " event received (not permitted)",
                context.getCurrentLocator());

        fatalError(e);
    }

    /**
     * Reports an XMLStreamingException for the given type of SAX event as an
     * error using the error handler, if available. Processing of the exception
     * is performed by {@link #error}.
     *
     * @param type the SAX event type for which the error is to be reported
     * @throws SAXException if the error handler can't handle the exception or
     *                      there is no error handler
     */
    protected void reportError(String type) throws SAXException {
        XMLStreamingException e = new XMLStreamingException(
                "Unexpected " + type + " event received (not anticipated)",
                context.getCurrentLocator());

        error(e);
    }

    /**
     * Reports an XMLStreamingException for the given type of SAX event as a
     * warning using the error handler, if available. Processing of the
     * exception is performed by {@link #error}.
     *
     * @param type the SAX event type for which the warning is to be reported
     * @throws SAXException if the error handler can't handle the exception or
     *                      there is no error handler
     */
    protected void reportWarning(String type) throws SAXException {
        XMLStreamingException e = new XMLStreamingException(
                "Unexpected " + type + " event received (not anticipated)",
                context.getCurrentLocator());

        warning(e);
    }

    /**
     * Simplifies the raising of a fatal error exception.
     *
     * @param e the exception to be raised
     * @throws SAXException if a handler isn't available to process the error
     *                      or the handler chooses to throw the exception
     */
    protected void fatalError(SAXParseException e) throws SAXException {
        if (handler != null) {
            handler.fatalError(e);
        } else {
            throw e;
        }
    }

    /**
     * Simplifies the raising of an error exception. Does nothing if no error
     * handler is available.
     *
     * @param e the exception to be raised
     * @throws SAXException if the handler chooses to throw the exception
     */
    protected void error(SAXParseException e) throws SAXException {
        if (handler != null) {
            handler.error(e);
        }
    }

    /**
     * Simplifies the raising of a warning exception. Does nothing if no error
     * handler is available.
     *
     * @param e the exception to be raised
     * @throws SAXException if the handler chooses to throw the exception
     */
    protected void warning(SAXParseException e) throws SAXException {
        if (handler != null) {
            handler.warning(e);
        }
    }

    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
    }

    // javadoc inherited
    public void startDocument() throws SAXException {
    }

    // javadoc inherited
    public void endDocument() throws SAXException {
    }

    /**
     * Reports a warning.
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        reportWarning("startPrefixMapping (" + prefix + ", " + uri + ")");
    }

    /**
     * Reports a warning.
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        reportWarning("endPrefixMapping");
    }

    /**
     * Reports a fatal error.
     */
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attrs) throws SAXException {
        reportFatalError("startElement");
    }

    /**
     * Reports a fatal error.
     */
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        reportFatalError("endElement");
    }

    /**
     * Reports a fatal error if non-whitespace characters are found. All
     * whitespace is quietly ignored.
     */
    public void characters(char[] chars,
                           int start,
                           int length) throws SAXException {
        boolean whitespace = true;

        for (int i = 0;
             (i < length) &&
                (whitespace = Character.isWhitespace(chars[start + i]));
             i++) {
            // Intentionally blank
        }

        if (!whitespace) {
            reportFatalError("characters (" +
                             new String(chars, start, length) + ")");
        }
    }

    /**
     * Quietly ignored.
     */
    public void ignorableWhitespace(char[] chars,
                                    int start,
                                    int length) throws SAXException {
    }

    /**
     * Reports a warning.
     */
    public void processingInstruction(String target,
                                      String data) throws SAXException {
        reportWarning("processingInstruction");
    }

    /**
     * Reports a warning.
     */
    public void skippedEntity(String name) throws SAXException {
        reportWarning("skippedEntity");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 ===========================================================================
*/
