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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;

import java.util.HashMap;
import java.util.Map;

/**
 * Typesafe enumerator for pseudo element types.
 */
public class PseudoElementTypeEnum {
    /**
     * A map associating pseudo element types with their type description.
     */
    private static final Map nameMappings = new HashMap();

    /**
     * An invalid pseudo element type.
     */
    public static final PseudoElementTypeEnum INVALID =
            new PseudoElementTypeEnum("");

    /**
     * The first-line pseudo element type.
     */
    public static final PseudoElementTypeEnum FIRST_LINE =
            new PseudoElementTypeEnum("first-line");

    /**
     * The first-letter pseudo element type.
     */
    public static final PseudoElementTypeEnum FIRST_LETTER =
            new PseudoElementTypeEnum("first-letter");

    /**
     * The mcs-shortcut pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_SHORTCUT =
            new PseudoElementTypeEnum("mcs-shortcut");

    /**
     * The after pseudo element type.
     */
    public static final PseudoElementTypeEnum AFTER =
            new PseudoElementTypeEnum("after");

    /**
     * The before pseudo element type.
     */
    public static final PseudoElementTypeEnum BEFORE =
            new PseudoElementTypeEnum("before");

    /**
     * The marker pseudo element type.
     */
    public static final PseudoElementTypeEnum MARKER =
            new PseudoElementTypeEnum("marker");

    /**
     * The mcs-next pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_NEXT =
            new PseudoElementTypeEnum("mcs-next");

    /**
     * The mcs-previous pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_PREVIOUS =
            new PseudoElementTypeEnum("mcs-previous");

    /**
     * The mcs-reset pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_RESET =
            new PseudoElementTypeEnum("mcs-reset");

    /**
     * The mcs-cancel pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_CANCEL =
            new PseudoElementTypeEnum("mcs-cancel");

    /**
     * The mcs-complete pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_COMPLETE =
            new PseudoElementTypeEnum("mcs-complete");

    /**
     * The mcs-label pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_LABEL =
            new PseudoElementTypeEnum("mcs-label");

    /**
     * The mcs-item pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_ITEM =
            new PseudoElementTypeEnum("mcs-item");

    /**
     * The mcs-between pseudo element type.
     */
    public static final PseudoElementTypeEnum MCS_BETWEEN =
            new PseudoElementTypeEnum("mcs-between");

    /**
     * The type of this pseudo element in string form.
     */
    private String type;

    /**
     * Create a new pseudo element type, and add it to the map.
     *
     * @param type The string representation of the type.
     */
    private PseudoElementTypeEnum(String type) {
        this.type = type;
        nameMappings.put(type, this);
    }

    /**
     * Retrieves a pseudo element type based on its string description. If
     * there is no match, the {@link #INVALID} type is returned.
     *
     * @param type The type to be retrieved
     * @return The corresponding {@link PseudoElementTypeEnum}, or {@#INVALID}
     *         if none is found.
     */
    public static PseudoElementTypeEnum getPseudoElementTypeEnum(String type) {
        PseudoElementTypeEnum result = (PseudoElementTypeEnum) nameMappings.get(type);
        if (result == null) {
            result = INVALID;
        }
        return result;
    }

    // Javadoc not required
    public String getType() {
        return type;
    }

    // Javdoc inherited
    public int hashCode() {
        int hash = type.hashCode();
        return hash;
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o!= null && o.getClass() == PseudoElementTypeEnum.class) {
            PseudoElementTypeEnum other = (PseudoElementTypeEnum) o;
            equal = (other.type.equals(type));
        }
        return equal;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Oct-05	9440/4	schaloner	VBM:2005070711 Added marker pseudo-element support

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
