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
package com.volantis.mcs.protocols;

/**
 * Typesafe enumeration for the pane rendering type which indicate how a
 * pane should be rendered.
 */
public class PaneRendering {
    /**
     * The enumeration value if we should use a table.
     */
    public static final PaneRendering USE_TABLE =
            new PaneRendering("USE_TABLE");

    /**
     * The enumeration value if we should use an enclosing table's cell.
     */
    public static final PaneRendering USE_ENCLOSING_TABLE_CELL =
            new PaneRendering("USE_ENCLOSING_TABLE_CELL");

    /**
     * The enumeration value if we should do nothing.
     */
    public static final PaneRendering DO_NOTHING =
            new PaneRendering("DO_NOTHING");

    /**
     * The enumeration value if we should use a new enclosing element such
     * as the 'div' element.
     */
    public static final PaneRendering CREATE_ENCLOSING_ELEMENT =
            new PaneRendering("CREATE_ENCLOSING_ELEMENT");

    /**
     * The enumeration type (used to identify the enumeration type).
     */
    private final String type;

    /**
     * Private constructor so types are limited to those defined internally.
     */
    private PaneRendering(String name) {
        type = name;
    }

    // javadoc inherited.
    public String toString() {
        return type;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Nov-04	5871/4	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - moved PaneRendering

 03-Nov-04	5871/1	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 ===========================================================================
*/
