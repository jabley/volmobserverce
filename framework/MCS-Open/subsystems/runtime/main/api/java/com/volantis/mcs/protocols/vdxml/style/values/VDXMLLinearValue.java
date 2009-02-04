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

/**
 * Parent class for all those style values which are not binary. I have chosen
 * to call this "linear" in contrast as these values simply apply at the point
 * they are set and stay like that until overriden with another value.
 * <p>
 * Linear values all implement a simple {@link #getAttributeValue} method in 
 * order to return their attribute value. Unlike booleans, it is impossible to 
 * calculate the ending value of the range that it applies over using just the 
 * value and range value. Thus, linear values are calculated up front for the 
 * start and end of a range and during rendering we just get the pre-calculated
 * value from this method.
 */ 
public abstract class VDXMLLinearValue implements VDXMLStyleValue {

    /**
     * Return the pre-calculated attribute value that this value represents.
     * 
     * @return the attribute value this value represents.
     */ 
    public abstract String getAttributeValue();
    
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
