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

package com.volantis.styling.engine;

/**
 * Minimal interface required by styling for accessing attributes associated
 * with an element being styled.
 *
 * @mock.generate 
 */
public interface Attributes {

    /**
     * Get the value of the attribute.
     *
     * @param namespace The namespace of the attribute, null if the attribute
     * does not belong in a namespace, i.e. it belongs to the element.
     * @param localName The local name of the attribute, may not be null.
     *
     * @return The value of the attribute, or null if the attribute does not
     * exist.
     */
    String getAttributeValue(String namespace, String localName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
