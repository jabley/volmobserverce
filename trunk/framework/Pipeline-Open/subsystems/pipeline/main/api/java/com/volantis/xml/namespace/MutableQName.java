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
 * Represents a mutable XML qualified name.
 *
 */
public class MutableQName extends QName {

    /**
     * Create a mutable QName
     */
    public MutableQName() {
        super("", "");
    }

    /**
     * Create a mutable QName from a combine localname and prefix
     * @param longName the name in the form "prefix:localname"
     * @return a QName
     */
    public MutableQName(String longName) {
        super(longName);
    }

    /**
     * Create a mutable QName
     * @param prefix the qualified name prefix
     * @param localName the local name of the qualified name
     */
    public MutableQName(String prefix, String localName) {
        super(prefix, localName);
    }

    /**
     * Create a mutable QName from an existing name
     * @param prefix the qualified name prefix
     * @param localName the local name of the qualified name
     */
    public MutableQName(QName name) {
        super(name.getPrefix(), name.getLocalName());
    }

    /**
     * Set the prefix for this XML name
     * @param prefix the name prefix
     */
    public void setPrefix(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Prefix cannot be null.");
        }
        this.prefix = prefix;
    }

    /**
     * Set the local name for this XML element
     * @param name the local name
     */
    public void setLocalName(String name) {
        if (name == null) {
            throw new NullPointerException("Local name cannot be null.");
        }
        localName = name;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 25-Jul-03	242/3	steve	VBM:2003072310 Added single parameter constructor for QName

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 ===========================================================================
*/
