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
package com.volantis.mcs.testtools.dom;

import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import org.jdom.Element;
import org.jdom.Document;

public class DOMValidatorStub implements DOMValidator {
    /**
     * Permits validation to be instigated on the given node. This must not be
     * null.
     */
    public void validate(Element node) {
    }

    public void terminateValidation() {        
    }

    // javadoc inherited
    public void declareSchemaLocation(String schemaLocation) {
    }

    // javadoc inherited
    public void declareNoNamespaceSchemaLocation(String schemaLocation) {
    }

    // javadoc inherited
    public void deriveSchemaLocationFrom(Document document) {
    }

    // javadoc inheritedXer
    public void enable(boolean enable) {
    }

    /**
     * Permits the current error reporter to be queried.
     */
    public ErrorReporter getErrorReporter() {
        return null;
    }

    /**
     * Permits the current error reporter to be reset.
     */
    public void setErrorReporter(ErrorReporter errorReporter) {
    }

    // javadoc inherited
    public void addSupplementaryValidator(
            String namespaceURI,
            String elementName,
            DOMSupplementaryValidator supplementaryValidator) {
    }

    // javadoc inherited
    public void removeSupplementaryValidator(
            String namespaceURI,
            String elementName,
            DOMSupplementaryValidator supplementaryValidator) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	7950/4	allan	VBM:2005041317 Some testcases for smart server

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Dec-03	2160/2	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 09-Dec-03	2057/3	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 01-Dec-03	2067/1	allan	VBM:2003111911 Rework design making ODOMEditorContext immutable.

 ===========================================================================
*/
