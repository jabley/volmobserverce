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

import com.volantis.mcs.protocols.vdxml.style.values.VDXMLBinaryValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLColorValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLFontSizeValue;

/**
 * Visitor for VDXML style values.
 */ 
public interface VDXMLStyleValueVisitor {

    /**
     * Visit a binary value.
     * 
     * @param value the value to visit.
     */ 
    void visit(VDXMLBinaryValue value);
    
    /**
     * Vist a color value.
     * 
     * @param value the value to visit.
     */ 
    void visit(VDXMLColorValue value);
    
    /**
     * Visit a font size value.
     * 
     * @param value the value to visit.
     */ 
    void visit(VDXMLFontSizeValue value);
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 08-Jun-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 ===========================================================================
*/
