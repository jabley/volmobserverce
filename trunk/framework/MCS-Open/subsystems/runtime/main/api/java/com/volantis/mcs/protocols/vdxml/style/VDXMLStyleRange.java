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
package com.volantis.mcs.protocols.vdxml.style;

/**
 * A type safe enumeration of the start and end points of a range that a VDXML 
 * style applies over.
 * <p>
 * Can be used with binary properties to calculate the related start and end 
 * attribute values.
 */ 
public class VDXMLStyleRange {

    /**
     * Start of the range.
     */ 
    public static final VDXMLStyleRange START =
            new VDXMLStyleRange("DE");
    
    /**
     * End of the range.
     */ 
    public static final VDXMLStyleRange STOP =
            new VDXMLStyleRange("FI");

    /**
     * String representation of the range value.
     */ 
    private final String range;

    /**
     * Private initialisation to prevent any more instances being created.
     */ 
    private VDXMLStyleRange(String range) {
        this.range = range;
    }

    /**
     * Return the attribute value that this range value represents.
     * 
     * @return the attribute value this range value represents.
     */ 
    public String getAttributeValue() {
        return range;
    }

    /**
     * Returns the inverse range value, i.e. if this value is start, return 
     * end, and if this value is end, return start.
     * 
     * @return the inverse range value.
     */ 
    public VDXMLStyleRange inverse() {
        if (this == VDXMLStyleRange.START) {
            return VDXMLStyleRange.STOP;
        } else if (this == VDXMLStyleRange.STOP) {
            return VDXMLStyleRange.START;
        } else {
            // Should never happen.
            throw new IllegalStateException();
        }
    }

    // Javadoc inherited.
    public String toString() {
        return range;
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

 08-Jun-04	4575/8	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 02-Jun-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
