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

/**
 * A typesafe enumeration for combinators.
 */
public class CombinatorEnum {
    /**
     * A simple description of the type of combinator.
     */
    private String type;

    /**
     * Private constructor to prevent instantiation outside this class.
     */
    private CombinatorEnum(String type) {
        this.type = type;
    }

    /**
     * Descendant combinator.
     */
    public static final CombinatorEnum DESCENDANT =
            new CombinatorEnum("descendant");

    /**
     * Child combinator.
     */
    public static final CombinatorEnum CHILD =
            new CombinatorEnum("child");

    /**
     * Direct adjacent combinator.
     */
    public static final CombinatorEnum DIRECT_ADJACENT =
            new CombinatorEnum("direct-adjacent");

    /**
     * Indirect adjacent combinator.
     */
    public static final CombinatorEnum INDIRECT_ADJACENT =
            new CombinatorEnum("indirect-adjacent");

    // Javadoc inherited
    public String toString() {
        return type;
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o != null && o.getClass() == CombinatorEnum.class) {
            CombinatorEnum other = (CombinatorEnum) o;
            equal = (this.type == other.type);
        }
        return equal;
    }

    // Javadoc inherited
    public int hashCode() {
        int hash = type.hashCode();
        return hash;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/2	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
