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

package com.volantis.xml.namespace;

/**
 * A mutable expanded name. It is not actually legal for a mutable expanded
 * name to have a null or empty local name value. null is enforced by the class
 * so if null is passed in as a parameter to setLocalName() or the parametered
 * constructor a NullPointerException will be thrown.
 * <br />
 * In the empty constructor however, the local name is initialised to the
 * empty string which should be changed by calling setLocalName(). Failure to
 * do this will lead to unpredictable results when using the class.
 *
 */
public class MutableExpandedName extends ExpandedName {

    /**
     * Create a mutable expanded name
     */
    public MutableExpandedName() {
        super("", "");
    }

    /**
     * Create a mutable expanded name
     * @param namespaceURI the namespace URI
     * @param localName the local name of the element
     */
    public MutableExpandedName(String namespaceURI, String localName) {
        super(namespaceURI, localName);
    }

    public ImmutableExpandedName getImmutableExpandedName() {
        return new ImmutableExpandedName(this);
    }

    /**
     * Create a mutable expanded name from an existing name
     * @param name the name to duplicate
     */
    public MutableExpandedName(ExpandedName name) {
        super(name.getNamespaceURI(), name.getLocalName());
    }

    /**
     * Set the Namespace URI property
     * @param namespaceURI the Namespace URI
     */
    public void setNamespaceURI(String namespaceURI) {
        if (namespaceURI == null) {
            throw new NullPointerException("Namespace URI cannot be null.");
        }
        this.namespaceURI = namespaceURI;
    }

    /**
     * Set the local name property
     * @param localName the local name of the element
     */
    public void setLocalName(String localName) {
        if (localName == null) {
            throw new NullPointerException("Local name cannot be null.");
        }
        this.localName = localName;
    }

    public void clear() {
        localName = null;
        namespaceURI = null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 25-Jul-03	242/2	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 ===========================================================================
*/
