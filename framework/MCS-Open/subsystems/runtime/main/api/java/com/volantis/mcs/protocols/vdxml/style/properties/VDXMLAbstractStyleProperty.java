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
package com.volantis.mcs.protocols.vdxml.style.properties;

import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleProperty;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleValue;

/**
 * Parent class for the various VDXML style properties.
 * <p>
 * Note that VDXML style properties are immutable.
 */ 
public class VDXMLAbstractStyleProperty implements VDXMLStyleProperty {

    /**
     * The name of the style property.
     * 
     * todo: should this be an enumerated value?
     */ 
    private String name;

    /**
     * The value of the style property.
     */ 
    private VDXMLStyleValue value;

    /**
     * Initialise this property with the name and value provided.
     * 
     * @param name the name of the property.
     * @param value the value of the property.
     */ 
    protected void initialise(String name, VDXMLStyleValue value) {
        this.name = name;
        this.value = value;
    }
    
    // Javadoc inherited.
    public String getName() {
        return name;
    }
    
    // Javadoc inherited.
    public VDXMLStyleValue getValue() {
        return value;
    }

    // Javadoc inherited.
    public String toString() {
        return "[" + name + "=" + value + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 08-Jun-04	4575/7	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 03-Jun-04	4575/5	geoff	VBM:2004051807 Minitel VDXML protocol support (checkin before fragmentation)

 28-May-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (working inline)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
