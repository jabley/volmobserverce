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
package com.volantis.mcs.xml.xpath;

/**
 * Typesafe enumerator representing the types of DOM object known to XPath.
 */
public class DOMType {
    /**
     * No 1. The Larch - err Attribute.
     */
    public static final DOMType ATTRIBUTE_TYPE = new DOMType();

    /**
     * The Element.
     */
    public static final DOMType ELEMENT_TYPE = new DOMType();

    /**
     * The Text.
     */
    public static final DOMType TEXT_TYPE = new DOMType();

    /**
     * The private constructor.
     */
    private DOMType() {
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

 11-Jun-04	4691/1	allan	VBM:2004060202 Allow setFocus(XPath) to work with Elements

 ===========================================================================
*/
