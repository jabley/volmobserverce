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
package com.volantis.mcs.xml.validation.sax;

/**
 * A class that is used to manage the predicate allocations for a nodes
 * children
 */
public final class XPathChild {

    /**
     * The name of the child
     */
    private String name;

    /**
     * The namespace that this child belongs to.
     */
    private String namespaceURI;

    /**
     * Used to record the number of children that have this name and namespace
     * for a given parent node.
     */
    private int count;

    /**
     * Initializes a <code>XPathChild</code> instance with the give parameters
     * @param name the name of the child. Cannot be null
     * @param namespaceURI the URI of the namespace that the child belongs to.
     * Cannot be null
     * @throws IllegalArgumentException if the name of namespaceURI
     * arguments are null.
     */
    public XPathChild(String name,
                      String namespaceURI) {
        this.count = 1;
        setName(name);
        setNamespaceURI(namespaceURI);
    }

    // javadoc
    public boolean equals(Object other) {
        // Implemented to enable these child entries to be stored in lists that
        // can be searched.
        boolean result = false;

        if (other != null) {
            if (other == this) {
                result = true;
            } else if (other.getClass() == getClass()) {
                XPathChild otherChild = (XPathChild)other;

                // equal iff name & namespace are equal. Namesapce & name
                // properties cannot be null.
                result = name.equals(otherChild.name) &&
                         namespaceURI.equals(otherChild.namespaceURI);
            }
        }
        return result;
    }

    // javadoc inherited
    public int hashCode() {
        // name & namespaceURI are always non null.
        return namespaceURI.hashCode() + name.hashCode();
    }

    /**
     * Set the name of this child. If the name parameter is null
     * an <code>IllegalArgumentException</code> exception will be
     * thrown.
     * @param name the name of the child
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(
                "A non-null name must be provided"); //$NON-NLS-1$
        }
        this.name = name;
    }

    /**
     * Set the namespaceURI of this child. If the  parameter is null
     * an <code>IllegalArgumentException</code> exception will be
     * thrown.
     * @param namespaceURI the namespace of the child
     */
    public void setNamespaceURI(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException(
                "A non-null namespace must be provided"); //$NON-NLS-1$
        }
        this.namespaceURI = namespaceURI;
    }

    /**
     * Returns the number of children with this name in this namespace
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments the number of children with this name in this namespace
     */
    public void incrementCount() {
        this.count++;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 10-Dec-03	2057/4	doug	VBM:2003112803 Addressed several rework issues

 09-Dec-03	2057/2	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 ===========================================================================
*/
