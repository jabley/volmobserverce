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
package com.volantis.mcs.management.tracking;

import java.util.HashMap;

/**
 * A typesafe enumeration of canvas types.
 * @volantis-api-include-in PublicAPI
 */
public class CanvasType {

    /**
     * The set of all literals. Keyed on the internal string version of the
     * enumeration, mapping to the CanvasType equivalent.
     * <p/>
     * NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     *
     */
    private static final HashMap entries = new HashMap();

    /**
     * Canvas of type Montage.
     */
    public static final CanvasType MONTAGE = new CanvasType("montage");

    /**
     * Canvas of type Main.
     */
    public static final CanvasType MAIN = new CanvasType("main");

    /**
     * Canvas of type Gear.
     */
    public static final CanvasType GEAR = new CanvasType("gear");

    /**
     * Canvas of type PORTAL.
     */
    public static final CanvasType PORTAL = new CanvasType("portal");

    /**
     * Canvas of type Portlet.
     */
    public static final CanvasType PORTLET = new CanvasType("portlet");

    /**
     * Canvas of type Inclusion.
     */
    public static final CanvasType INCLUSION = new CanvasType("inclusion");

    /**
     * Canvas of type Message.
     */
    public static final CanvasType MESSAGE = new CanvasType("message");


    /**
     * The internal name for the enumeration literal.
     */
    private final String name;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the internal name for the new literal
     */
    private CanvasType(String name) {
        this.name = name;
        entries.put(name, this);
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param name the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static CanvasType literal(String name) {
        return (CanvasType) entries.get(name);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Oct-04	5921/1	tom	VBM:2004101102 added public API documentation for canvas tracking

 22-Oct-04	5910/1	tom	VBM:2004101102 Added Public API documentation for canvas tracking

 21-Jun-04	4702/1	matthew	VBM:2004061402 rework PageTracking

 11-Jun-04	4689/1	matthew	VBM:2004060706 add basic PageTracking facilities

 ===========================================================================
*/
