/*
This file is part of Volantis Mobility Server.

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>.
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

import java.util.Map;

/**
 * A SAXParseException that is used within the pipeline.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class XMLPipelineException
        extends SAXParseException {

    /**
     * The error source id.
     */
    private String errorSourceID;

    /**
     * The error code URI.
     */
    private String errorCodeURI;

    /**
     * The error code name.
     */
    private String errorCodeName;

    /**
     * Additional error specific properties.
     */
    private Map errorProperties;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message the exception message
     * @param locator the locator
     */
    public XMLPipelineException(String message, Locator locator) {
        super(message, locator);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message   the exception message
     * @param locator   the locator
     * @param throwable the underlying throwable
     */
    public XMLPipelineException(String message, Locator locator,
                                Throwable throwable) {
        super(message, locator);

        initCause(throwable);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message      the exception message
     * @param publicId     the locator public ID
     * @param systemId     the locator system ID
     * @param lineNumber   the locator line number
     * @param columnNumber the locator column number
     */
    public XMLPipelineException(String message,
                                String publicId,
                                String systemId,
                                int lineNumber,
                                int columnNumber) {
        super(message, publicId, systemId, lineNumber, columnNumber);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message      the exception message
     * @param publicId     the locator public ID
     * @param systemId     the locator system ID
     * @param lineNumber   the locator line number
     * @param columnNumber the locator column number
     * @param throwable    the underlying throwable
     */
    public XMLPipelineException(String message,
                                String publicId,
                                String systemId,
                                int lineNumber,
                                int columnNumber,
                                Throwable throwable) {
        super(message, publicId, systemId,
                lineNumber, columnNumber);

        initCause(throwable);
    }

    /**
     * Initialise additional information about the error and its source.
     *
     * @param sourceID The value of the xml:id attribute of the operation that raised
     * this error, may be null.
     * @param codeURI The namespace URI for the error code, may not be null.
     * @param codeName The name of the error code, may not be null.
     * @param properties A set of additional properties provided by the operation.
     */
    public void initErrorInfo(String sourceID, String codeURI, String codeName,
                              Map properties) {

        errorSourceID = sourceID;
        errorCodeURI = codeURI;
        errorCodeName = codeName;
        errorProperties = properties;
    }

    /**
     * Get the xml:id of the operation that raised this error.
     *
     * @return The xml:id of the operation that raised this error, may be null.
     */
    public String getErrorSourceID() {
        return errorSourceID;
    }

    /**
     * Get the namespace URI for the error code.
     *
     * @return The namespace URI for the error code, may be null if and only if
     *         the {@link #getErrorCodeName()} is null.
     */
    public String getErrorCodeURI() {
        return errorCodeURI;
    }

    /**
     * Get the name of the error code.
     *
     * @return The name of the error code, may be null if and only if
     *         the {@link #getErrorCodeURI()} is null.
     */
    public String getErrorCodeName() {
        return errorCodeName;
    }

    /**
     * Get the set of additional properties provided by the raising operation.
     *
     * @return The set of additional properties provided by the raising
     *         operation, may be null.
     */
    public Map getErrorProperties() {
        return errorProperties;
    }
}
