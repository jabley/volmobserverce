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
package com.volantis.mcs.eclipse.ab.editors.xml.schema;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents the definition of an element within the {@link SchemaDefinition}.
 *
 * <p>The Namespace for the element is assumed to be that of the entire
 * schema definition.</p>
 */
public class ElementDefinition {
    /**
     * This element's name.
     */
    private String name;

    /**
     * The names of the sub-elements permitted.
     *
     * @supplierRole subElements
     * @supplierCardinality 0..*
     * @associates String
     */
    private List subElements;

    /**
     * The attributes permitted.
     *
     * @supplierRole attributes
     * @supplierCardinality 0..*
     * @associates <{AttributeDefinition}>
     */
    private List attributes;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the name of the element
     */
    public ElementDefinition(String name) {
        this.name = name;
        this.subElements = new ArrayList();
        this.attributes = new ArrayList();
    }

    /**
     * Returns a list of sub-element names where the sub-elements' names match
     * the given match string (i.e. their names start with this string).
     * Returns all available sub-element names if match is null or empty. This
     * list may be empty but will not be null.
     *
     * @param match the string with which sub-element names must start
     * @return a list of matching sub-elements
     */
    public List getSubElementNames(String match) {
        List result = new ArrayList(subElements.size());

        if ((match == null) || "".equals(match)) { //$NON-NLS-1$
            result.addAll(subElements);
        } else {
            for (int i = 0;
                 i < subElements.size();
                 i++) {
                String name = (String)subElements.get(i);

                if (name.startsWith(match)) {
                    result.add(name);
                }
            }
        }

        return result;
    }

    /**
     * Returns true if the element has any sub-elements at all.
     *
     * @return true if the element has one or more sub-elements
     */
    public boolean hasSubElements() {
        return subElements.size() != 0;
    }

    /**
     * Returns a list of attribute definitions where the attributes' names
     * match the given match string (i.e. their names start with this string).
     * Returns all available attribute definitions if match is null or empty.
     * This list may be empty but will not be null.
     *
     * @param match the string with which attribute names must start
     * @return a list of matching attribute definitions
     */
    public List getAttributeDefinitions(String match) {
        List result = new ArrayList(attributes.size());

        if ((match == null) || "".equals(match)) { //$NON-NLS-1$
            result.addAll(attributes);
        } else {
            for (int i = 0;
                 i < attributes.size();
                 i++) {
                AttributeDefinition attribute =
                    (AttributeDefinition)attributes.get(i);

                if (attribute.getName().startsWith(match)) {
                    result.add(attribute);
                }
            }
        }

        return result;
    }

    /**
     * Returns true if the element has any attributes at all.
     *
     * @return true if the element has one or more attributes
     */
    public boolean hasAttributes() {
        return attributes.size() != 0;
    }

    /**
     * Used, within this package, to add a sub-element to the permitted
     * sub-elements list.
     *
     * @param name the name of the sub-element to be added
     */
    void addSubElement(String name) {
        subElements.add(name);
    }
    
    /**
     * Used, within this package, to add an attribute to the permitted
     * attributes list.
     *
     * @param name the name of the attribute to be added
     * @param use  the attribute usage from the schema definition
     */
    void addAttribute(String name, String use) {
        boolean required = false;

        if (use != null) {
            required = use.equals("required"); //$NON-NLS-1$
        }

        attributes.add(new AttributeDefinition(name, required));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 07-Jan-04	2433/1	philws	VBM:2004010702 Fix content assist choice refinement and optimize generated markup

 04-Jan-04	2309/1	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
