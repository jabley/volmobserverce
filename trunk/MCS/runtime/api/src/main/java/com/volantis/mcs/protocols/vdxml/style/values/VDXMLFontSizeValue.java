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

import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleValueVisitor;

/**
 * A typesafe enumeration of the valid VDXML font size style values.
 * <p>
 * Used to represent the current state of the font size VDXML style property.
 */ 
public class VDXMLFontSizeValue extends VDXMLLinearValue {

    /**
     * Normal width, normal height.
     */ 
    public static final VDXMLFontSizeValue NORMAL =
            new VDXMLFontSizeValue("TN");
    
    /**
     * Double width, normal height.
     */ 
    public static final VDXMLFontSizeValue DOUBLE_WIDTH =
            new VDXMLFontSizeValue("DL");
    
    /**
     * Normal width, double height.
     */ 
    public static final VDXMLFontSizeValue DOUBLE_HEIGHT =
            new VDXMLFontSizeValue("DH");
    
    /**
     * Double width, double height.
     */ 
    public static final VDXMLFontSizeValue DOUBLE_HEIGHT_AND_WIDTH =
            new VDXMLFontSizeValue("DT");

    /**
     * The VDXML attribute value that corresponds to this value. 
     */ 
    private final String attributeValue;

    /**
     * Private initialisation to prevent other classes creating instances.
     * 
     * @param attributeValue
     */ 
    private VDXMLFontSizeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    // Javadoc inherited.
    public String getAttributeValue() {
        return attributeValue;
    }
    
    // Javadoc inherited.
    public void accept(VDXMLStyleValueVisitor visitor) {
        visitor.visit(this);
    }
    
    // Javadoc inherited.
    public String toString() {
        return attributeValue;
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

 08-Jun-04	4575/5	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
