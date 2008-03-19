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
 * $Header: /src/voyager/com/volantis/charset/BitSetEncoding.java,v 1.2 2003/04/28 15:24:05 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Mat             VBM:2003033107 - Created to support character
 *                              encoding.
 * 22-Apr-03    Mat             VBM:2003033107 - Updated some javadoc and 
 *                              changed the constructor to use the 
 *                              encoding(charset) constructor that has been 
 *                              added to Encoding.
 * 22-May-03    Mat             VBM:2003042907 - Added MIBEnum to constructor.
 * ----------------------------------------------------------------------------
 */
package com.volantis.charset;


import java.io.UnsupportedEncodingException;

import java.util.BitSet;

/**
 * Class which sets up a BitSet to enable a quick lookup as to whether a
 * character can be represented using the encoding.
 * 
 * @author mat
 * @todo creating bitset encodings would be faster for single byte charsets 
 *      if the configuration declared how many bytes per char each charset was 
 *      and then we only checked the known 256 byte values for them.
 */
public class BitSetEncoding extends Encoding {

    /**
     * The copyright statement.
     */
     private static final String mark = "(c) Volantis Systems Ltd 2003.";
    
    /**
     * Every character has a corresponding bit in the BitSet which is set to
     * true of the character can be represented.
     */
    private BitSet supportedEncodings = null;

    /**
     * Creates a new instance of BitSetEncoding.
     * 
     * @param charsetName The Java name of a charset.
     * @param MIBEnum The MIBenum value for this charset name.
     */
    public BitSetEncoding(String charsetName, int MIBEnum) 
            throws UnsupportedEncodingException {
        super(charsetName, MIBEnum);
        supportedEncodings = buildEncodings(charsetName);
    }
  
    /**
     * Generates a bitset we can use to quickly determine if a given Unicode
     * character has a representation in the given character encoding.
     * 
     * @param charsetName The encoding to use
     * @return The bitset
     * @throws UnsupportedEncodingException if the charset is not supported.
     */
    private BitSet buildEncodings(String charsetName) 
            throws UnsupportedEncodingException {
        BitSet bitSet = new BitSet(ARRAY_SIZE);
        for (int i = 0; i < ARRAY_SIZE; ++i) {
            Character c = new Character((char) i);
            String oldStr = c.toString();
            // convert from unicode to new encoding and back.
            // If it's the same, it's representable.
            byte[] oldBytes = oldStr.getBytes(charsetName);
            String newStr = new String(oldBytes,charsetName);
            if (newStr.equals(oldStr)) {        
                bitSet.set(i);
                if ( oldBytes.length > 1 ) {
                    int clearIdx = oldBytes[0];
                    if( clearIdx < 0 ) {
                        clearIdx += 256;
                    }
                    bitSet.clear(clearIdx);
                }
            } 
        }
        return bitSet;
    }

    // Javadoc inherited
    public CharacterRepresentable checkCharacter(int c) {
        CharacterRepresentable representable = 
                               CharacterRepresentable.NotRepresentable;
        if (supportedEncodings.get(c)) {
            representable = CharacterRepresentable.Representable;
        }
        return representable;
    }

    protected String debugName() {
        return "BitSetEncoding";
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 19-Jan-04	2653/5	steve	VBM:2004011304 Merge from proteus

 19-Jan-04	2576/3	steve	VBM:2004011304 VBM Review tidy ups

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/2	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
