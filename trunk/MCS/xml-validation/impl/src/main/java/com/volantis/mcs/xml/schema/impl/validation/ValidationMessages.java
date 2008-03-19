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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xml.schema.impl.validation;

public class ValidationMessages {

    /**
     * Element ''{0}'' is not valid inside element ''{1}''
     * <p/>
     * 0 - child element
     * 1 - containing element
     */
    public static final String INVALID_ELEMENT =
            "validation-error-element-not-valid-in-context";

    // todo: enable this check and add to Messages.properties
//    /**
//     * Character data is not valid inside element ''{0}''
//     * <p/>
//     * 0 - containing element
//     */
//    public static final String INVALID_PCDATA = "validation-error-pcdata-not-valid-in-context";

    /**
     * Invalid content inside element ''{0}'', expected ''{1}'', found ''{2}''
     * <p/>
     * 0 - containing element
     * 1 - expected content
     * 2 - actual content
     */
    public static final String INVALID_CONTENT = "validation-error-invalid-content";

    /**
     * Invalid content, element ''{0}'' is excluded inside element ''{1}''
     * <p/>
     * 0 - excluded element
     * 1 - excluding element
     */
    public static final String EXCLUDED_CONTENT = "validation-error-excluded-content";

    /**
     * Mandatory content missing within element ''{0}'', expected ''{1}''
     * <p/>
     * 0 - containing element
     * 1 - expected content
     */
    public static final String MISSING_CONTENT = "validation-error-missing-content";

    /**
     * Invalid content, character data is outside document root.
     */
    public static final String PCDATA_OUTSIDE_DOCUMENT_ROOT = "validation-error-pcdata-outside-document-root";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
