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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.devices.validation;

import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.xpath.XPath;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;

import java.util.Iterator;

/**
 * Supplementary validator for validating that a type of element has
 * content.
 */
public class ContentPresentElementValidator
        implements DOMSupplementaryValidator {

    /**
     * The elementName of the kind of elements validated by this validator.
     */
    private final String elementName;

    /**
     * The elementNamespace of the kind of elements validated by this validator.
     */
    private final Namespace elementNamespace;

    /**
     * Construct a new ContentPresentElementValidator.
     * @param elementName the name of the element to be validated
     * @param elementNamespace the namespace of the element to be validated
     * @throws IllegalArgumentException if either elementName or
     * elementNamesapce are null.
     */
    public ContentPresentElementValidator(String elementName,
                                          Namespace elementNamespace) {
        if (elementName == null) {
            throw new IllegalArgumentException("Cannot be null: elementName");
        }
        if (elementNamespace == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "elementNamespace");
        }
        this.elementName = elementName;
        this.elementNamespace = elementNamespace;
    }

    /**
     * Validate that there are no whitespace or empty content items in the
     * given element.
     * @param element the Element to validate
     * @param errorReporter the ErrorReport to report validation errors
     */
    public void validate(Element element,
                         ErrorReporter errorReporter) {

        if (element == null) {
            throw new IllegalArgumentException("Cannot be null: element");
        }

        if (!element.getName().equals(elementName)) {
            throw new IllegalArgumentException("Expected element named \"" +
                    elementName +
                    "\" but got element named \"" + element.getName() + "\"");
        }

        if (!element.getNamespace().getURI().equals(elementNamespace.getURI())) {
            throw new IllegalArgumentException("Expected element namespace \"" +
                    elementNamespace.getURI() +
                    "\" but got element namespace \"" +
                    element.getNamespace().getURI() + "\"");
        }

        // Validate that the element has content that is non-whitespace
        Iterator contents = element.getContent().iterator();
        if (!contents.hasNext()) {
            ErrorDetails details = new ErrorDetails(element, new XPath(element),
                    null, FaultTypes.WHITESPACE, null, null);
            errorReporter.reportError(details);
        } else {
            do {
                Object content = contents.next();
                if (content == null || ((Text) content).getText().trim().
                        length() == 0) {
                    ErrorDetails details = new ErrorDetails(element, new XPath(element),
                            null, FaultTypes.WHITESPACE, null, null);
                    errorReporter.reportError(details);
                }
            } while (contents.hasNext());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 ===========================================================================
*/
