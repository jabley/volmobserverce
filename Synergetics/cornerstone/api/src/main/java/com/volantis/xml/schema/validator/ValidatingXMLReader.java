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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.schema.validator;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import java.io.IOException;

/**
 * An {@link XMLReader} that performs validation during parsing, collates the
 * errors, and warnings and reports them all at the end.
 */
public class ValidatingXMLReader
        implements XMLReader {

    /**
     * The underlying reader.
     */
    private final XMLReader delegate;

    /**
     * Allows this to add its own error handler as well as the one that may
     * be specified by used or this.
     */
    private final TeeErrorHandler teeErrorHandler;

    /**
     * Allows this to add its own entity resolver as well as the one that may
     * be specified by used or this.
     */
    private final TeeEntityResolver teeEntityResolver;

    /**
     * Error handler that collates the errors and warnings.
     */
    private final CollatingErrorHandler collatingErrorHandler;

    /**
     * Initialise.
     *
     * @param delegate             The underlying reader.
     * @param entityResolver       The entity resolver used for schemata.
     * @param failOnWarningsAndLog True if warnings cause it to fail and
     *                             all messages are logged, false if warnings
     *                             are not errors and messages are not logged.
     */
    public ValidatingXMLReader(
            XMLReader delegate, EntityResolver entityResolver,
            boolean failOnWarningsAndLog) {
        this.delegate = delegate;

        // Create a collating error handler, or a simple error throwing one
        // based on the flag.
        ErrorHandler handler;
        if (failOnWarningsAndLog) {
            collatingErrorHandler = new CollatingErrorHandler();
            handler = collatingErrorHandler;
        } else {
            collatingErrorHandler = null;
            handler = new ErrorHandler() {
                public void warning(SAXParseException e) {
                }

                public void error(SAXParseException e) throws SAXException {
                    throw e;
                }

                public void fatalError(SAXParseException e)
                        throws SAXException {
                    throw e;
                }
            };
        }

        // Create the error handler, the second part is used for this as it
        // ensures that the user's error handler is invoked first minimising
        // the impact of the validation on the behaviour.
        teeErrorHandler = new TeeErrorHandler();
        teeErrorHandler.setSecond(handler);
        delegate.setErrorHandler(teeErrorHandler);

        // Ditto for entity resolver.
        teeEntityResolver = new TeeEntityResolver();
        teeEntityResolver.setSecond(entityResolver);
        delegate.setEntityResolver(teeEntityResolver);
    }

    // Javadoc inherited.
    public void parse(String systemId)
            throws IOException, SAXException {

        delegate.parse(systemId);

        if (collatingErrorHandler != null) {
            collatingErrorHandler.checkForErrors();
        }
    }

    // Javadoc inherited.
    public void parse(InputSource input)
            throws IOException, SAXException {

        delegate.parse(input);

        if (collatingErrorHandler != null) {
            collatingErrorHandler.checkForErrors();
        }
    }

    // Javadoc inherited.
    public boolean getFeature(String name)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        return delegate.getFeature(name);
    }

    // Javadoc inherited.
    public void setFeature(String name, boolean value)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        delegate.setFeature(name, value);
    }

    // Javadoc inherited.
    public ContentHandler getContentHandler() {
        return delegate.getContentHandler();
    }

    // Javadoc inherited.
    public void setContentHandler(ContentHandler handler) {
        delegate.setContentHandler(handler);
    }

    // Javadoc inherited.
    public DTDHandler getDTDHandler() {
        return delegate.getDTDHandler();
    }

    // Javadoc inherited.
    public void setDTDHandler(DTDHandler handler) {
        delegate.setDTDHandler(handler);
    }

    // Javadoc inherited.
    public EntityResolver getEntityResolver() {
        return teeEntityResolver.getFirst();
    }

    // Javadoc inherited.
    public void setEntityResolver(EntityResolver resolver) {
        teeEntityResolver.setFirst(resolver);
    }

    // Javadoc inherited.
    public ErrorHandler getErrorHandler() {
        return teeErrorHandler.getFirst();
    }

    // Javadoc inherited.
    public void setErrorHandler(ErrorHandler handler) {
        teeErrorHandler.setFirst(handler);
    }

    // Javadoc inherited.
    public Object getProperty(String name)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        return delegate.getProperty(name);
    }

    // Javadoc inherited.
    public void setProperty(String name, Object value)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        delegate.setProperty(name, value);
    }
}
