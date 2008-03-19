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
package com.volantis.mcs.protocols.wml;

/**
 * Utility class to define constants required for rendering of access keys.
 */
public class AccesskeyConstants {

    /**
     * Dummy element which wraps the element containing a numeric shortcut menu
     * which must be modified whilst it is present in the MCS DOM.
     * <p/>
     * Note that this element is not present in the WBDOM as as it has a more
     * flexible structure - it is translated into a more appropriate form for
     * the WBDOM in {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     *
     * @todo this name should have single quotes around it to be consistent
     * with the other special element names. The problem with this is
     * that it makes testing it rather impractical since then it is not
     * a valid element name from an XML perspective, and thus you cannot
     * create test documents from an XML string. The real fix here is
     * to create infrastructure to allow us to have both runtime and
     * testing implementations of the various special names so that we
     * can test easily. Have a look at HTML3_2UnabridgedTransformerTestCase
     * for an example of the problems we have to go through at the moment
     * to test special elements with invalid names.
     */
    public static final String ACCESSKEY_ANNOTATION_ELEMENT =
            "ACCESSKEY-ANNOTATION";

    /**
     * Dummy value to represent the accesskey which will be calculated by
     * the {@link AccesskeyWBSAXFilter} later, as a char.
     */
    public static final char DUMMY_ACCESSKEY_VALUE_CHAR = 'x';

    /**
     * Dummy value to represent the accesskey which will be calculated by
     * the {@link AccesskeyWBSAXFilter} later, as a single character string.
     */
    public static final String DUMMY_ACCESSKEY_VALUE_STRING = new String(
            new char[]{DUMMY_ACCESSKEY_VALUE_CHAR});

    /**
     * Private constructor to prevent instantiation.
     */
    private AccesskeyConstants() {
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 02-Mar-05	7120/3	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 ===========================================================================
*/
