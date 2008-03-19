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
package com.volantis.mcs.xml.validation;

import org.jdom.Element;
import org.jdom.Document;


/**
 * Validation on a given document (sub-)tree can be instigated on an
 * implementation of this interface. This validation will include schema, DTD
 * and supplementary validation as appropriate.
 */
public interface DOMValidator extends DOMSupplementaryValidationProvider {

    /**
     * Permits validation to be instigated on the given node. This must not be
     * null.
     */
    void validate(Element node);

    /**
     * Allow validation in progress to be terminated.
     */
    void terminateValidation(); 

    /**
     * Allows clients to declare the location of one or more external XSD
     * files that should be used to perform validation.
     * @param schemaLocation a string that declares one or more xsd schema
     * files and the namespaces that they belong to. For example the string
     *
     * "http://foo.com /first.xsd http://foo.com /second.xsd
     *
     * indicates that the schemas /first.xsd and /second.xsd should be used
     * to perform validation and that these schemas belong to the
     * http://foo.com namespace. Must not be null.
     */
    void declareSchemaLocation(String schemaLocation);

    /**
     * Allows clients to declare the location of <p><strong>ONE</strong></p>
     * external XSD file that should be used to perform validation. This
     * schema file does not belong to any namespace.
     * @param schemaLocation a string that declares one xsd schema file.
     * Must not be null.
     */
    void declareNoNamespaceSchemaLocation(String schemaLocation);

    /**
     * A convenience method that will walk the document looking for the
     * <p><strong>FIRST</strong></p> schema declaration. This will then
     * be used as the schema to validate against.
     * @param document the document to search
     */
    void deriveSchemaLocationFrom(Document document);

    /**
     * Enables or disables this validator depending on the value of the
     * enable parameter
     * @param enable enables the validator if its value is true. Otherwise
     * it disables the validator
     */
    void enable(boolean enable);

    /**
     * Permits the current error reporter to be queried.
     */
    ErrorReporter getErrorReporter();

    /**
     * Permits the current error reporter to be reset.
     */
    void setErrorReporter(ErrorReporter errorReporter);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Dec-03	2160/2	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 09-Dec-03	2057/4	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 28-Nov-03	2055/1	doug	VBM:2003112802 Added ODOM validation interfaces

 ===========================================================================
*/
