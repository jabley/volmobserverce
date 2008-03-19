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
package com.volantis.mcs.wbsax;

import com.volantis.synergetics.ArrayUtils;

/**
 * The WBSAX representation of a character.
 * <p>
 * This class is used to allow fine grained access to the contents of a 
 * {@link WBSAXString}, and in particular to understand exactly how characters
 * map onto individual bytes. 
 */ 
public class WBSAXCharacter {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private char c;
    
    private byte[] bytes;

    WBSAXCharacter(char c, byte[] bytes) {
        this.c = c;
        this.bytes = bytes;
    }

    /**
     * Return the char for this WBSAX character,
     * 
     * @return the character
     */ 
    public char getChar() {
        return c;
    }

    /**
     * Return the bytes for this WBSAX character.
     * 
     * @return the bytes as an array
     */ 
    public byte[] getBytes() {
        return bytes;
    }
    
    public String toString() {
        return "[" + c + ":" + ArrayUtils.toString(bytes) + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	8925/1	allan	VBM:2005062308 Move ArrayUtils to Synergetics

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
