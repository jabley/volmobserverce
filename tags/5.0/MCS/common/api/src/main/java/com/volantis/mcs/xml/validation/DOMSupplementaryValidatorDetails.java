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
package com.volantis.mcs.xml.validation;

/**
 * Class for holding the information associated with a
 * DOMSupplementaryValidator.
 */
public class DOMSupplementaryValidatorDetails {
    /**
     * The namespace uri of the supplementary validator.
     */
    public final String namespaceURI;

    /**
     * The name of the element that is validated by the supplementary
     * validator.
     */
    public final String elementName;

    /**
     * The supplementary validator.
     */
    public final DOMSupplementaryValidator supplementaryValidator;

    /**
     * Construct a new SupplementaryValidationDetails
     * @param namespaceURI the namespaceURI
     * @param elementName the elementName
     * @param supplementaryValidator the supplementaryValidator
     */
    public DOMSupplementaryValidatorDetails(String namespaceURI,
                                            String elementName,
                                            DOMSupplementaryValidator supplementaryValidator) {
        this.namespaceURI = namespaceURI;
        this.elementName = elementName;
        this.supplementaryValidator = supplementaryValidator;
    }

    // javadoc inherited
    public boolean equals(Object o) {
        boolean equals = o.getClass().
                equals(DOMSupplementaryValidatorDetails.class);
        if (equals) {
            DOMSupplementaryValidatorDetails details =
                    (DOMSupplementaryValidatorDetails) o;
            equals = namespaceURI.equals(details.namespaceURI) &&
                    elementName.equals(details.elementName) &&
                    supplementaryValidator.equals(details.
                    supplementaryValidator);
        }

        return equals;
    }

    // javadoc inherited
    public int hashCode() {
        return namespaceURI.hashCode() +
                elementName.hashCode() +
                supplementaryValidator.hashCode();
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

 05-May-04	4115/1	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 ===========================================================================
*/
