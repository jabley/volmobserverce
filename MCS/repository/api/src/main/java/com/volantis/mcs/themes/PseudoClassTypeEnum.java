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
 * Typesafe enumerator for pseudo class types.
 */
public class PseudoClassTypeEnum {
    /**
     * A map associating pseudo element types with their type description.
     */
    private static final Map nameMappings = new HashMap();

    /**
     * An invalid pseudo class type.
     */
    public static final PseudoClassTypeEnum INVALID = new PseudoClassTypeEnum("", false);

    /**
     * The first-child pseudo class type.
     */
    public static final PseudoClassTypeEnum FIRST_CHILD = new PseudoClassTypeEnum("first-child", true);

    /**
     * The nth-child pseudo class type.
     */
    public static final PseudoClassTypeEnum NTH_CHILD = new PseudoClassTypeEnum("nth-child", true);

    /**
     * The link pseudo class type.
     */
    public static final PseudoClassTypeEnum LINK = new PseudoClassTypeEnum("link", false);

    /**
     * The visited pseudo class type.
     */
    public static final PseudoClassTypeEnum VISITED = new PseudoClassTypeEnum("visited", false);

    /**
     * The active pseudo class type.
     */
    public static final PseudoClassTypeEnum ACTIVE = new PseudoClassTypeEnum("active", false);

    /**
     * The focus pseudo class type.
     */
    public static final PseudoClassTypeEnum FOCUS = new PseudoClassTypeEnum("focus", false);

    /**
     * The hover pseudo class type.
     */
    public static final PseudoClassTypeEnum HOVER = new PseudoClassTypeEnum("hover", false);

    /**
     * The mcs-concealed pseudo class type.
     */
    public static final PseudoClassTypeEnum MCS_CONCEALED = new PseudoClassTypeEnum("mcs-concealed", false);

    /**
     * The mcs-unfolded pseudo class type.
     */
    public static final PseudoClassTypeEnum MCS_UNFOLDED = new PseudoClassTypeEnum("mcs-unfolded", false);

    /**
     * The mcs-invalid pseudo class type.
     */
    public static final PseudoClassTypeEnum MCS_INVALID = new PseudoClassTypeEnum("mcs-invalid", false);

    /**
     * The mcs-normal pseudo class type.
     */
    public static final PseudoClassTypeEnum MCS_NORMAL = new PseudoClassTypeEnum("mcs-normal", false);

    /**
     * The mcs-busy pseudo class type.
     */
    public static final PseudoClassTypeEnum MCS_BUSY = new PseudoClassTypeEnum("mcs-busy", false);

    /**
     * The mcs-failed pseudo class type.
     */
    public static final PseudoClassTypeEnum MCS_FAILED = new PseudoClassTypeEnum("mcs-failed", false);

    /**
     * The mcs-suspended pseudo class type.
     */
    public static final PseudoClassTypeEnum MCS_SUSPENDED = new PseudoClassTypeEnum("mcs-suspended", false);

    /**
     * The mcs-disabled pseudo class type.
     */
    public static final PseudoClassTypeEnum MCS_DISABLED = new PseudoClassTypeEnum("mcs-disabled", false);
    
    /**
     * The type of this pseudo class in string form.
     */
    private String type;

    /**
     * Indicates whether the pseudo class is structural (true) or stateful
     * (false)
     */
    private boolean structural;

    /**
     * Create a new pseudo class type, and add it to the map.
     *
     * @param type The string representation of the type.
     */
    private PseudoClassTypeEnum(String type, boolean structural) {
        this.type = type;
        this.structural = structural;
        nameMappings.put(type, this);
    }

    /**
     * Retrieves a pseudo class type based on its string description. If
     * there is no match, the {@link #INVALID} type is returned.
     *
     * @param type The type to be retrieved
     * @return The corresponding {@link PseudoClassTypeEnum}, or {@#INVALID}
     *         if none is found.
     */
    public static PseudoClassTypeEnum getPseudoClassTypeEnum(String type) {
        PseudoClassTypeEnum result = (PseudoClassTypeEnum) nameMappings.get(type);
        if (result == null) {
            result = INVALID;
        }
        return result;
    }

    // Javadoc not required
    public String getType() {
        return type;
    }

    // Javadoc not required
    public boolean isStructural() {
        return structural;
    }

    // Javdoc inherited
    public int hashCode() {
        int hash = type.hashCode();
        if (structural) {
            hash *= 17;
        }
        return hash;
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o!= null && o.getClass() == PseudoClassTypeEnum.class) {
            PseudoClassTypeEnum other = (PseudoClassTypeEnum) o;
            equal = (other.structural == structural) &&
                    (other.type.equals(type));
        }
        return equal;
    }

    public String toString() {
        return "[PseudoClassTypeEnum - " + type + "]";
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10504/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Nov-05	10484/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
