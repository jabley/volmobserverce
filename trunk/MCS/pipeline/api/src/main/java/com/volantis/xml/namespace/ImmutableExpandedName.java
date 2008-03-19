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
 * An expanded name that is not allowed to change
 */
public class ImmutableExpandedName extends ExpandedName {

    /**
     * Create an Immutable expanded name
     * @param namespaceURI the namespace URI
     * @param localName the local name of the element
     */
    public ImmutableExpandedName(String namespaceURI, String localName) {
        super(namespaceURI, localName);
    }

    public ImmutableExpandedName getImmutableExpandedName() {
        return this;
    }

    /**
     * Create an Immutable expanded name from an existing name
     * @param name the expanded name to copy
     */
    public ImmutableExpandedName(ExpandedName name) {
        super(name.getNamespaceURI(), name.getLocalName());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 ===========================================================================
*/
