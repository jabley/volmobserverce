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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-May-03    Geoff           VBM:2003042904 - Created; represents an 
 *                              element in a WBSAX event stream.
 * 19-May-03    Geoff           VBM:2003042904 - Fixed toString(). 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents an element name in a WBSAX event stream. 
 */ 
public class ElementNameCode extends SingleByteInteger {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The name of the element.
     */ 
    private String name;
    
    /**
     * Construct an instance of this class, using the token and name provided.
     *  
     * @param token the token value for this element.
     * @param name the name of the element.
     */ 
    ElementNameCode(int token, String name) {
        super(token);
        this.name = name;
    }

    /**
     * Returns the name of the element.
     * 
     * @return the name of the element.
     */ 
    public String getName() {
        return name;
    }
    
    // Inherit javadoc.
    public String toString() {
        return "[ElementNameCode:name=" + name + "," + super.toString() + "]";
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
