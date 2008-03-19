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

package com.volantis.xml.schema;

/**
 * Contains definitions of the standard W3C namespaces and schemata.
 */
public class W3CSchemata {

    /**
     * The W3C XML Schema Instance namespace.
     */
    public static final String XSI_NAMESPACE =
            "http://www.w3.org/2001/XMLSchema-instance";

    /**
     * The public ID for the xhtml-lat1.ent
     */
    public static final String XHTML_LAT1_ENT_PUBLIC_ID =
        "-//W3C//ENTITIES Latin 1 for XHTML//EN";

    /**
     * The public ID for the xhtml-special.ent
     */
    public static final String XHTML_SPECIAL_ENT_PUBLIC_ID =
        "-//W3C//ENTITIES Special for XHTML//EN";

    /**
     * The public ID for the xhtml-symbol.ent
     */
    public static final String XHTML_SYMBOL_ENT_PUBLIC_ID =
        "-//W3C//ENTITIES Symbols for XHTML//EN";

    /**
     * The mariner path to the special entity file
     */
    public static final String XHTML_SYMBOL_PATH =
        "com/volantis/mcs/dtd/xhtml-symbol.ent";

    // XHTML Latin 1 Entities
    public static final SchemaDefinition XHTML_LAT1_ENT =
            new SchemaDefinition(
                    null,
                    "http://www.w3.org/TR/xhtml1/DTD/xhtml-lat1.ent",
                    "com/volantis/mcs/dtd/xhtml-lat1.ent");

    // XHTML Symbol Entities
    public static final SchemaDefinition XHTML_SYMBOL_ENT =
            new SchemaDefinition(
                    null,
                    "http://www.w3.org/TR/xhtml1/DTD/xhtml-symbol.ent",
                    "com/volantis/mcs/dtd/xhtml-symbol.ent");


    // XHTML Special Entities
    public static final SchemaDefinition XHTML_SPECIAL_ENT =
            new SchemaDefinition(
                    null,
                    "http://www.w3.org/TR/xhtml1/DTD/xhtml-special.ent",
                    "com/volantis/mcs/dtd/xhtml-special.ent");

    // All XHTML Entities
    public static final Schemata XHTML_ENTITIES;

    static {
        Schemata schemata;

        schemata = new Schemata();
        schemata.addSchema(XHTML_LAT1_ENT);
        schemata.addSchema(XHTML_SYMBOL_ENT);
        schemata.addSchema(XHTML_SPECIAL_ENT);
        XHTML_ENTITIES = schemata;
    }
}
