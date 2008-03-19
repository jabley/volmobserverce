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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common.odom;

import java.util.HashMap;

/**
 * A typesafe enumeration. The literals can be thought of as the ODOM's
 * equivalent to property names.
 */
public class ChangeQualifier {
    /**
     * The set of all literals. Keyed on the internal string version of the
     * enumeration, mapping to the ChangeQualifier equivalent.
     * <p/>
     * NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     *
     * @associates <{ChangeQualifier}>
     * @supplierRole entries
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    private static final HashMap entries = new HashMap();

    /**
     * Equivalent to not specifying a qualifier on registration of a listener.
     * This or a null value can be used to register interest in all
     * qualifiers.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ANY
     */
    public static final ChangeQualifier ANY =
        new ChangeQualifier("any"); //$NON-NLS-1$

    /**
     * Equivalent to not specifying a qualifier on generation of an event. This
     * or a null value can be used to raise an unqualified event.
     *
     * <p><strong>This should probably not be used.</strong></p>
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NONE
     */
    public static final ChangeQualifier NONE =
        new ChangeQualifier("none"); //$NON-NLS-1$

    /**
     * Hierarchy changes occur when elements or attributes have their parent
     * changed. This commonly occurs when elements or attributes are created or
     * deleted.
     * <p>Only instances of {@link ODOMHierarchyChangeEvent} can be qualified
     * with this value.</p>
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole HIERARCHY
     */
    public static final ChangeQualifier HIERARCHY =
        new ChangeQualifier("hierarchy"); //$NON-NLS-1$

    /**
     * Name changes occur when elements or attributes have their name string
     * changed.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NAME
     */
    public static final ChangeQualifier NAME =
        new ChangeQualifier("name"); //$NON-NLS-1$

    /**
     * Namespace changes occur when elements or attributes have their namespace
     * changed.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NAMESPACE
     */
    public static final ChangeQualifier NAMESPACE =
        new ChangeQualifier("namespace"); //$NON-NLS-1$

    /**
     * Attribute type changes occur when an attribute has its type changed.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ATTRIBUTE_TYPE
     */
    public static final ChangeQualifier ATTRIBUTE_TYPE =
        new ChangeQualifier("attribute type"); //$NON-NLS-1$

    /**
     * Attribute value changes occur when an attribute has its value changed.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole ATTRIBUTE_VALUE
     */
    public static final ChangeQualifier ATTRIBUTE_VALUE =
        new ChangeQualifier("attribute value"); //$NON-NLS-1$

    /**
     * Text changes occur when a text node has its textual content changed.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole TEXT
     */
    public static final ChangeQualifier TEXT =
        new ChangeQualifier("text"); //$NON-NLS-1$

    /**
     * The internal name for the enumeration literal.
     */
    private final String name;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the internal name for the new literal
     */
    private ChangeQualifier(String name) {
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
    public static ChangeQualifier literal(String name) {
        return (ChangeQualifier)entries.get(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2689/1	eduardo	VBM:2003112407 undo/redo manager for ODOM

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
