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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the complete schema definition (available elements and their
 * attributes). This provides various useful methods that can be used to
 * implement content assistance and content completion.
 *
 * <p>Note that this only supports a single namespace.</p>
 */
public class SchemaDefinition {
    /**
     * The set of elements permitted in the entire schema.
     *
     * @supplierRole elements
     * @supplierCardinality 0..*
     * @associates <{ElementDefinition}>
     */
    private Map elements = new HashMap();

    /**
     * The namespace URI for the schema.
     */
    private String namespaceURI;

    /**
     * Initializes the new instance using the given parameters.
     */
    public SchemaDefinition() {
    }

    // javadoc unnecessary
    public String getNamespaceURI() {
        return namespaceURI;
    }

    public ElementDefinition getElementDefinition(String elementName) {
        return (ElementDefinition)elements.get(elementName);
    }
    
    /**
     * Used, in this package, to define the namespace URI for the schema.
     *
     * @param namespaceURI the namespace URI for the schema
     */
    void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    /**
     * Used, in this package, to add a new element definition. The definition
     * is returned so that its configuration can be updated.
     *
     * @param name the name for the new element definition
     * @return the element definition created
     */
    ElementDefinition addElement(String name) {
        ElementDefinition result = new ElementDefinition(name);

        elements.put(name, result);

        return result;
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

 04-Jan-04	2309/1	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
