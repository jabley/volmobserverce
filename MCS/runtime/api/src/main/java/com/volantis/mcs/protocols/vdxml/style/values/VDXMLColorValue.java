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
 * A typesafe enumeration of the valid VDXML color style values.
 * <p>
 * Used to represent the current state of color VDXML style properties such
 * as character color and background color.
 */ 
public class VDXMLColorValue extends VDXMLLinearValue {

    /**
     * Black (noir).
     */ 
    public static final VDXMLColorValue BLACK =
            new VDXMLColorValue("NO");
    
    /**
     * Red (rouge).
     */ 
    public static final VDXMLColorValue RED =
            new VDXMLColorValue("RO");
    
    /**
     * Lime (verde).
     */ 
    public static final VDXMLColorValue LIME =
            new VDXMLColorValue("VE");
    
    /**
     * Yellow (jaune)
     */ 
    public static final VDXMLColorValue YELLOW =
            new VDXMLColorValue("JA");
    
    /**
     * Blue (bleu).
     */ 
    public static final VDXMLColorValue BLUE =
            new VDXMLColorValue("BU");
    
    /**
     * Fuschia (magenta).
     */ 
    public static final VDXMLColorValue FUSCHIA =
            new VDXMLColorValue("MA");
    
    /**
     * Aqua (cyan).
     */ 
    public static final VDXMLColorValue AQUA =
            new VDXMLColorValue("CY");
    
    /**
     * White (blanc).
     */ 
    public static final VDXMLColorValue WHITE =
            new VDXMLColorValue("BC");

    /**
     * The VDXML attribute value that corresponds to this value. 
     */ 
    private final String attributeValue;

    /**
     * Private initialisation to prevent other classes creating instances.
     * 
     * @param attributeValue
     */ 
    private VDXMLColorValue(String attributeValue) {
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

 08-Jun-04	4575/8	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 28-May-04	4575/4	geoff	VBM:2004051807 Minitel VDXML protocol support (fix underline)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
