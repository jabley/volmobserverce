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

package com.volantis.mcs.xml.schema.model;

/**
 * Encapsulates the namespace and local name for an element.
 */
public final class ElementType
        implements Content {

    /**
     * The namespace of the element.
     */
    private final String namespaceURI;

    /**
     * The local name of the element.
     */
    private final String localName;
    private final String qName;

    /**
     * Initialise.
     *
     * @param namespaceURI The namespace of the element.
     * @param localName    The local name of the element.
     */
    public ElementType(String namespaceURI, String localName) {
        this(namespaceURI, localName, "{" + namespaceURI + "}" + localName);
    }

    /**
     * Initialise.
     *
     * @param namespaceURI The namespace of the element.
     * @param localName    The local name of the element.
     */
    public ElementType(String namespaceURI, String localName, String qName) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI cannot be null");
        }
        if (localName == null) {
            throw new IllegalArgumentException("localName cannot be null");
        }
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.qName = qName;                      
    }

    /**
     * Get the namespace of the element.
     *
     * @return The namespace of the element.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Get the local name of the element.
     *
     * @return The local name of the element.
     */
    public String getLocalName() {
        return localName;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof ElementType)) {
            return false;
        }

        ElementType other = (ElementType) obj;

        return namespaceURI.equals(other.getNamespaceURI())
                && localName.equals(other.getLocalName());
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = namespaceURI.hashCode();
        result += result * 37 + localName.hashCode();
        return result;
    }

    // Javadoc inherited.
    public String toString() {
        return qName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
