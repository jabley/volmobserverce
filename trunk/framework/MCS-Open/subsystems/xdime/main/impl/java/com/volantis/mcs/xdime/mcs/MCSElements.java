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

package com.volantis.mcs.xdime.mcs;

import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.Namespace;
import com.volantis.mcs.xdime.XDIMESchemata;

/**
 * Contains enumeration of all element types in the XDIME 2 MCS namespace.
 */
public class MCSElements {

    /**
     * The namespace containing all these elements.
     */
    public static final Namespace NAMESPACE =
            new Namespace(XDIMESchemata.XDIME2_MCS_NAMESPACE);

    /**
     * Get the element type for the specified local name in the current
     * namespace.
     *
     * @param localName The local name of the element.
     * @return The element type.
     */
    private static ElementType getElement(String localName) {
        return NAMESPACE.addElement(localName);
    }

    public static final ElementType UNIT = getElement("unit");

    /**
     * The 'handler' element for XDIME 2 events.
     * <p>
     * NOTE: The handler element is currently included in the XDIME 2 MCS
     * namespace, but it appears that in future it will be in the XML Events
     * namespace (according to the current XHTML WD).
     */
    public static final ElementType HANDLER = getElement("handler");

    /**
     * The 'script' element for XDIME 2 scritps.
     *
     * <p>NOTE: The script element is currently included in the XDIME 2 MCS
     * namespace.</p>
     */
    public static final ElementType SCRIPT = getElement("script");

    /**
     * The 'br' element for XDIME 2 line break.
     *
     * <p>NOTE: The br element is currently included in the XDIME 2 MCS
     * namespace.</p>
     */
    public static final ElementType BR = getElement("br");
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
