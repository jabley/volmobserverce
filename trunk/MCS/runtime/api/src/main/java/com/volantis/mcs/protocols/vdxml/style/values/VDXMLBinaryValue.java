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
package com.volantis.mcs.protocols.vdxml.style.values;

import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleValue;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleValueVisitor;

/**
 * A typesafe enumeration of the valid VDXML binary style values.
 * <p>
 * Used to represent the current state of binary VDXML style properties such
 * as underline, reverse video and blinking.
 */ 
public class VDXMLBinaryValue implements VDXMLStyleValue {

    /**
     * The true value.
     */ 
    public static final VDXMLBinaryValue TRUE = new VDXMLBinaryValue("true");

    /**
     * The false value.
     */ 
    public static final VDXMLBinaryValue FALSE = new VDXMLBinaryValue("false");

    /**
     * The name of this value, for debugging only.
     */ 
    private final String name;

    /**
     * Private initialisation to prevent other classes creating instances.
     * 
     * @param value
     */ 
    private VDXMLBinaryValue(String value) {
        this.name = value;
    }

    /**
     * Returns the inverse value, i.e. if TRUE, returns FALSE, and if FALSE, 
     * returns TRUE.
     * 
     * @return the inverse binary value. 
     */ 
    public VDXMLBinaryValue inverse() {
        if (this == TRUE) {
            return FALSE;
        } else if (this == FALSE) {
            return TRUE;
        } else {
            throw new IllegalStateException();
        }
    }
    
    // Javadoc inherited.
    public void accept(VDXMLStyleValueVisitor visitor) {
        visitor.visit(this);
    }

    // Javadoc inherited.
    public String toString() {
        return name;
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

 08-Jun-04	4575/11	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 02-Jun-04	4575/7	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 28-May-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (working inline)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
