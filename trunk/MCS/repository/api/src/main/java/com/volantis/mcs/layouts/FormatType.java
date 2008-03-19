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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatType.java,v 1.2 2003/03/11 12:42:56 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Dec-02    Allan           VBM:2002120615 - A FormatType.
 * 11-Mar-03    Allan           VBM:2003010303 - Removed the FormatType for
 *                              DeviceLayoutFormat.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This is a type safe enumeration for Format types. As well as the enueration,
 * this class provides access to the type name and FormatScope index of a
 * particular FormatType.
 *
 * @mock.generate
 */
public class FormatType {
    /**
     * A typesafe enumeration for types of format structure.
     */
    public static final class Structure {
        /**
         * Indicates that a format is a leaf (i.e. it doesn't contain any
         * other formats).
         */
        public static final Structure LEAF =
            new Structure("leaf");

        /**
         * Indicates that a format has a single child format.
         */
        public static final Structure SIMPLE_CONTAINER =
            new Structure("simple container");

        /**
         * Indicates that a format is grid-based allowing
         * <dfn>m</dfn>x<dfn>n</dfn> child formats to be defined.
         */
        public static final Structure GRID =
            new Structure("grid");

        /**
         * For debug purposes only
         */
        private String name;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param name the internal name for debug purposes
         */
        private Structure(String name) {
            this.name = name;
        }

        // javadoc unnecessary
        public String toString() {
            return name;
        }
    }

    /**
     * peterca
     */
    private static HashMap entries = new HashMap();

    /**
     * The name of this type of Format.
     */
    private String typeName;

    /**
     * The namespace of this FormatType.
     */
    private FormatNamespace namespace;

    /**
     * The class of the Format associated with the FormatType
     */
    private Class formatClass;

    /**
     * The name of the element within an XML repository that represents the
     * format type. This value <strong>must</strong> match the element naming
     * in the schema.
     */
    private String elementName;

    /**
     * Identifies the format's structure type.
     */
    private Structure structure;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param typeName    The name of the type of Format
     * @param namespace   The namespace of Format of this type
     * @param formatClass The actual Format class that this literal represents
     * @param elementName The name of the element used in an XML repository to
     *                    represent this format type
     * @param structure   Indicates the type of structure that the format has
     */
    FormatType(String typeName, FormatNamespace namespace, Class formatClass,
               String elementName, Structure structure) {

        if ((typeName == null) ||
            (elementName == null) ||
            (structure == null)) {
            throw new IllegalArgumentException(
                "Must have non-null typeName, elementName and structure");
        }

        this.typeName = typeName;
        this.namespace = namespace;
        this.formatClass = formatClass;
        this.elementName = elementName;
        this.structure = structure;

        entries.put(elementName, this);
    }

    /**
     * @return the namespace of this format type.
     */ 
    public FormatNamespace getNamespace() {
        return namespace;
    }

    /**
     * peterca
     * @param elementName
     */
    public static FormatType getFormatTypeForElementName(String elementName) {
        return (FormatType) entries.get(elementName);
    }

    /**
     * @return The name of the type of Format associated with this FormatType.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @return The Class assoicated with this FormatType
     */
    public Class getFormatClass() {
        return formatClass;
    }

    // javadoc unnecessary
    public String getElementName() {
        return elementName;
    }

    // javadoc unnecessary
    public Structure getStructure() {
        return structure;
    }

    /**
     * @return The toString() for FormatType showing the name and index
     * values.
     */
    public String toString() {
        return "name: " + getTypeName() +
                ", namespace: " + getNamespace();
    }

    /**
     * One of the format types
     */
    public static final FormatType COLUMN_ITERATOR_PANE =
            new FormatType("ColumnIteratorPane",
                           FormatNamespace.PANE,
                           ColumnIteratorPane.class,
                           "columnIteratorPaneFormat",
                           Structure.LEAF);

    /**
     * One of the format types
     */
    public static final FormatType DISSECTING_PANE =
            new FormatType("DissectingPane",
                           FormatNamespace.PANE,
                           DissectingPane.class,
                           "dissectingPaneFormat",
                           Structure.LEAF);

    /**
     * One of the format types
     */
    public static final FormatType FORM =
            new FormatType("Form",
                           FormatNamespace.FORM,
                           Form.class,
                           "formFormat",
                           Structure.SIMPLE_CONTAINER);

    /**
     * One of the format types
     */
    public static final FormatType FORM_FRAGMENT =
            new FormatType("FormFragment",
                           FormatNamespace.FORM_FRAGMENT,
                           FormFragment.class,
                           "formFragmentFormat",
                           Structure.SIMPLE_CONTAINER);

    /**
     * One of the format types
     */
    public static final FormatType FRAGMENT =
            new FormatType("Fragment",
                           FormatNamespace.FRAGMENT,
                           Fragment.class,
                           "fragmentFormat",
                           Structure.SIMPLE_CONTAINER);

    /**
     * One of the format types
     */
    public static final FormatType GRID =
            new FormatType("Grid",
                           FormatNamespace.GRID,
                           Grid.class,
                           "gridFormat",
                           Structure.GRID);

    /**
     * One of the format types
     */
    public static final FormatType PANE =
            new FormatType("Pane",
                           FormatNamespace.PANE,
                           Pane.class,
                           "paneFormat",
                           Structure.LEAF);

    /**
     * One of the format types
     */
    public static final FormatType REGION =
            new FormatType("Region",
                           FormatNamespace.REGION,
                           Region.class,
                           "regionFormat",
                           Structure.LEAF);

    /**
     * One of the format types
     */
    public static final FormatType REPLICA =
            new FormatType("Replica",
                           FormatNamespace.REPLICA,
                           Replica.class,
                           "replicaFormat",
                           Structure.LEAF);

    /**
     * One of the format types
     */
    public static final FormatType ROW_ITERATOR_PANE =
            new FormatType("RowIteratorPane",
                           FormatNamespace.PANE,
                           RowIteratorPane.class,
                           "rowIteratorPaneFormat",
                           Structure.LEAF);

    /**
     * One of the format types
     */
    public static final FormatType SEGMENT =
            new FormatType("Segment",
                           FormatNamespace.SEGMENT,
                           Segment.class,
                           "segmentFormat",
                           Structure.LEAF);

    /**
     * One of the format types
     */
    public static final FormatType SEGMENT_GRID =
            new FormatType("SegmentGrid",
                           FormatNamespace.SEGMENT_GRID,
                           SegmentGrid.class,
                           "segmentGridFormat",
                           Structure.GRID);

    /**
     * One of the format types
     */
    public static final FormatType SPATIAL_FORMAT_ITERATOR =
            new FormatType("SpatialFormatIterator",
                           FormatNamespace.SPATIAL_FORMAT_ITERATOR,
                           SpatialFormatIterator.class,
                           "spatialFormatIterator",
                           Structure.SIMPLE_CONTAINER);

    /**
     * One of the format types
     */
    public static final FormatType TEMPORAL_FORMAT_ITERATOR =
            new FormatType("TemporalFormatIterator",
                           FormatNamespace.TEMPORAL_FORMAT_ITERATOR,
                           TemporalFormatIterator.class,
                           "temporalFormatIterator",
                           Structure.SIMPLE_CONTAINER);

    /**
     * A special format type that acts as a placeholder for other formats.
     */
    public static final FormatType EMPTY =
        new FormatType("Empty",
                       FormatNamespace.PANE, // todo: wrong? may be a bug
                       null,
                       "emptyFormat",
                       Structure.LEAF);

    /**
     * Intentionally package protected, this identifies the number of "real"
     * format types that can be iterated across.
     */
    static final int FORMAT_TYPE_COUNT = 14;

    /**
     * The format types that can be iterated over
     */
    private static ArrayList formatTypes = new ArrayList(FORMAT_TYPE_COUNT);

    static {
        // Initialize the array of iteratable format types
        formatTypes.add(COLUMN_ITERATOR_PANE);
        formatTypes.add(DISSECTING_PANE);
        formatTypes.add(FORM);
        formatTypes.add(FORM_FRAGMENT);
        formatTypes.add(FRAGMENT);
        formatTypes.add(GRID);
        formatTypes.add(PANE);
        formatTypes.add(REGION);
        formatTypes.add(REPLICA);
        formatTypes.add(ROW_ITERATOR_PANE);
        formatTypes.add(SEGMENT);
        formatTypes.add(SEGMENT_GRID);
        formatTypes.add(SPATIAL_FORMAT_ITERATOR);
        formatTypes.add(TEMPORAL_FORMAT_ITERATOR);

        // The following check can be replaced with assert in jdk1.4.
        if (formatTypes.size() != FORMAT_TYPE_COUNT) {
            throw new RuntimeException("Wrong number of FormatTypes in " +
                                       "list (" + formatTypes.size() +
                                       " but should be " + FORMAT_TYPE_COUNT);
        }
    }

    /**
     * Provide an iteration over all the available FormatTypes
     *
     * @return An Iterator over FormatTypes
     */
    public static final Iterator iterator() {
        // Override ArrayList Iterator so that Objects cannot be removed.
        return new Iterator() {
            /**
             * The underlying iterator to which this iterator delegates
             */
            Iterator iterator = formatTypes.iterator();

            // javadoc inherited
            public boolean hasNext() {
                return iterator.hasNext();
            }

            // javadoc inherited
            public Object next() {
                return iterator.next();
            }

            // javadoc inherited
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // javadoc inherited
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final FormatType type = (FormatType) other;
        return
            (elementName == null ? type.elementName == null :
                elementName.equals(type.elementName)) &&
            (formatClass == null ? type.formatClass == null :
                formatClass.equals(type.formatClass)) &&
            (namespace == null ? type.namespace == null :
                namespace.equals(type.namespace)) &&
            (structure == null ? type.structure == null :
                structure.equals(type.structure)) &&
            (typeName == null ? type.typeName == null : 
                typeName.equals(type.typeName));

    }

    // javadoc inherited
    public int hashCode() {
        int result = (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        result = 31 * result + (formatClass != null ? formatClass.hashCode() : 0);
        result = 31 * result + (elementName != null ? elementName.hashCode() : 0);
        result = 31 * result + (structure != null ? structure.hashCode() : 0);
        return result;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 23-Jan-04	2720/2	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 22-Jan-04	2549/2	pcameron	VBM:2004011201 Committed for integration into layout editor

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 13-Jan-04	2534/2	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
