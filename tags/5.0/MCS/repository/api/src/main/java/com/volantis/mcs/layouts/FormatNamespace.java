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
package com.volantis.mcs.layouts;

/**
 * A typesafe enumeration of the different format namespaces available within
 * MCS. 
 * <p>
 * Format namespaces group together similar types of formats. For example, 
 * all the subtypes of pane are grouped together into a single namespace.
 * <p>
 * For this reason, the runtime should probably favour the use of this class 
 * over {@link FormatType}. Format Type is more suitable for identifying all 
 * the leaf nodes of the format tree, regardless of their similarity - which 
 * is more applicable in the accessors and the policy manager.  
 */ 
public class FormatNamespace {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The namespace for all panes (normal, column and row iterator and 
     * dissecting).
     */ 
    public static final FormatNamespace PANE =
            new FormatNamespace("Pane", 0);

    public static final FormatNamespace FORM =
            new FormatNamespace("Form", 1);

    public static final FormatNamespace FORM_FRAGMENT =
            new FormatNamespace("FormFragment", 2);

    public static final FormatNamespace FRAGMENT =
            new FormatNamespace("Fragment", 3);

    public static final FormatNamespace GRID =
            new FormatNamespace("Grid", 4);

    public static final FormatNamespace REGION =
            new FormatNamespace("Region", 5);

    public static final FormatNamespace REPLICA =
            new FormatNamespace("Replica", 6);

    public static final FormatNamespace SEGMENT =
            new FormatNamespace("Segment", 7);

    public static final FormatNamespace SEGMENT_GRID =
            new FormatNamespace("SegmentGrid", 8);

    public static final FormatNamespace SPATIAL_FORMAT_ITERATOR =
            new FormatNamespace("SpatialFormatIterator", 9);

    public static final FormatNamespace TEMPORAL_FORMAT_ITERATOR =
            new FormatNamespace("TemporalFormatIterator", 10);
    
    public static final FormatNamespace CONTAINER =
            new FormatNamespace("Container", 11);

    public static final int FORMAT_NAMESPACE_COUNT = 12;
    
    /**
     * The name of this namespace.
     */ 
    private final String name;

    /**
     * The index of this namespace within {@link FormatScope}.
     */ 
    private final int index;

    /**
     * Private constructor to prevent others creating instances.
     */ 
    private FormatNamespace(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * @see #name
     */ 
    public String getName() {
        return name;
    }

    /**
     * @see #index
     */ 
    public int getIndex() {
        return index;
    }

    // Javadoc inherited.
    public String toString() {
        return name + ":" + index;
    }

    // javadoc inherited
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final FormatNamespace namespace = (FormatNamespace) other;
        return index == namespace.index && name.equals(namespace.name);
    }

    // javadoc inherited
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + index;
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Jul-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (review comments)

 21-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 ===========================================================================
*/
