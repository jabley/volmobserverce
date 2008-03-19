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

package com.volantis.mcs.themes;

/**
 * An enumeration of the possible priorities of a style value.
 */
public class Priority
    implements Comparable {

    /**
     * The normal priority.
     */
    public static final Priority NORMAL = new Priority("NORMAL", 0, "");

    /**
     * The important priority.
     */
    public static final Priority IMPORTANT = new Priority("IMPORTANT", 100,
            " !important");

    /**
     * Debug name.
     */
    private final String name;

    /**
     * Numerical priority, the higher the number the higher the priority.
     */
    private final int priority;

    /**
     * The cost (in characters) of representing this priority according to
     * the standard.
     */
    private final int standardCost;

    /**
     * The standard CSS representation of the priority.
     */
    private String standardCSS;

    /**
     * Initialise.
     *
     * @param name The name of the object, used for debugging purposes.
     * @param priority The priority of the object, used for defining order.
     * @param standardCSS
     */
    Priority(String name, int priority, String standardCSS) {
        this.name = name;
        this.priority = priority;
        this.standardCost = standardCSS.length();
        this.standardCSS = standardCSS;
    }

    /**
     * Check to see if this priority is greater than the specified priority.
     *
     * @param other The priority to compare against.
     * @return True if it is greater, false otherwise.
     */
    public boolean isGreaterThan(Priority other) {
        return compareTo(other) > 0;
    }

    // Javadoc inherited.
    public int compareTo(Object o) {
        Priority other = (Priority) o;

        return priority - other.priority;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }

    public String getStandardCSS() {
        return standardCSS;
    }

    public int getStandardCost() {
        return standardCost;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
