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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.sgml;

/**
 * Enumeration of the different models supported in SGML.
 *
 * <p>This is not an exhaustive list of models, only those ones that affect
 * the output of the document are present.</p>
 */
public class ElementModel {

    /**
     * The element contains unprocessed character data so special characters
     * for not have to be encoded.
     */
    public static final ElementModel CDATA = new ElementModel("CDATA");

    /**
     * The element is empty so may not need an end tag.
     */
    public static final ElementModel EMPTY = new ElementModel("EMPTY");

    /**
     * The element has some unknown content.
     *
     * <p>This is the default for those elements for which no more information
     * is available. It is assumed that elements with this require both start
     * and end tags and if it contains character data it is #PCDATA.</p>
     */
    public static final ElementModel UNKNOWN = new ElementModel("UNKNOWN");

    /**
     * The name of the enumeration.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name.
     */
    private ElementModel(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
