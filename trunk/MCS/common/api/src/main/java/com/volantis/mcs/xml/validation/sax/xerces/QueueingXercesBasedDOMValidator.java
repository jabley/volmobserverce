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
package com.volantis.mcs.xml.validation.sax.xerces;

import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;
import org.jdom.Element;

/**
 * Implementation of XercesDOMBasedValidator that allows multiple validation
 * requests to be queued up. Rather than failing if a request for validation
 * is made while one is in progress, it queues up another validation (no matter
 * how many attempts to validate are made, only a single extra validation will
 * be carried out unless a new request is made during the queued validation).
 */
public class QueueingXercesBasedDOMValidator extends XercesBasedDOMValidator {
    private boolean validationQueued = false;

    /**
     * Initializes a <code>QueueingXercesBasedDOMValidator</code> instance with
     * the given arguments.
     *
     * @param errorReporter the ErrorReporter that will be used to report
     *                      any validation errors.
     * @throws org.xml.sax.SAXException if there is an error configuring the XML
     *                                  reader
     * @throws com.volantis.mcs.xml.validation.sax.ParserErrorException
     *                                  if initialisation of ParserErrors fails
     */
    public QueueingXercesBasedDOMValidator(ErrorReporter errorReporter) throws SAXException, ParserErrorException {
        super(errorReporter);
    }

    /**
     * An entity resolver may be provided that will handle local access to
     * specific entities via public or system IDs. In addition, the schema
     * location(s) should be specified to enable schema namespace URIs to be
     * mapped to schema system IDs which may, in turn, be handled via the
     * given entity resolver.
     * <p/>
     * <p>Neither value is mandatory since schema validation is optional and
     * DTD validation (also optional) may be performed using actual URLs.</p>
     * <p/>
     * <p>The schemaLocation is a string of the form:</p>
     * <p/>
     * <pre>
     * {&lt;schema namespace URI&gt; &lt;schema system ID&gt;}
     * </pre>
     *
     * @param entityResolver The EntityResolver to be used when parsing the
     *                       XML file
     * @param errorReporter  The ErrorReporter that will be used to report
     *                       any validation errors
     * @throws org.xml.sax.SAXException if there is an error configuring the XML
     *                                  reader
     * @throws com.volantis.mcs.xml.validation.sax.ParserErrorException
     *                                  if initialisation of ParserErrors fails
     * @throws IllegalArgumentException if the root element errorReporter are
     *                                  null
     */
    public QueueingXercesBasedDOMValidator(EntityResolver entityResolver, ErrorReporter errorReporter) throws SAXException, ParserErrorException {
        super(entityResolver, errorReporter);
    }

    // javadoc inherited
    public void validate(Element node) {
        if (isValidationInProgress()) {
            validationQueued = true;
        } else {
            do {
                validationQueued = false;
                super.validate(node);
            } while (validationQueued);
        }
    }
}
