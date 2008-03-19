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

package com.volantis.xml.pipeline.sax;

import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.Locator;

/**
 * A SAXParseException that is used within the pipeline.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class XMLPipelineException
        extends ExtendedSAXParseException {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

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
     * @param message the exception message
     * @param locator the locator
     * @param throwable the underlying throwable
     */
    public XMLPipelineException(String message, Locator locator,
                                Throwable throwable) {
        super(message, locator, throwable);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message the exception message
     * @param publicId the locator public ID
     * @param systemId the locator system ID
     * @param lineNumber the locator line number
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
     * @param message the exception message
     * @param publicId the locator public ID
     * @param systemId the locator system ID
     * @param lineNumber the locator line number
     * @param columnNumber the locator column number
     * @param throwable the underlying throwable
     */
    public XMLPipelineException(String message,
                                String publicId,
                                String systemId,
                                int lineNumber,
                                int columnNumber,
                                Throwable throwable) {
        super(message, publicId, systemId,
              lineNumber, columnNumber, throwable);
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

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
