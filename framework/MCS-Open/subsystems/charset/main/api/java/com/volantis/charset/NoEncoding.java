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
 * $Header: /src/voyager/com/volantis/charset/NoEncoding.java,v 1.2 2003/04/28 15:24:05 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Mat             VBM:2003033107 - Created to support character
 *                              encoding.
 * 22-Apr-03    Mat             VBM:2003033107 - Updated class javadoc
 * 22-May-03    Mat             VBM:2003042907 - Added MIBEnum to constructor.
 * ----------------------------------------------------------------------------
 */
package com.volantis.charset;

/**
 * A simple class for character sets that require no encoding.  It always
 * returns true for isRepresentable().  The reason for it's existence
 * is to improve performance by eliminating the bit set look up that 
 * would be required if BitSetEncoding was used.
 *
 * @author mat
 */
public class NoEncoding extends Encoding {

    /**
     * The copyright statement.
     */
     private static final String mark = "(c) Volantis Systems Ltd 2003.";
    
    /**
     * Creates a new instance of NoEncoding.
     * 
     * @param charsetName The Java name of a charset.
     * @param MIBEnum The MIBenum value for this charset name.
     */
    public NoEncoding(String charsetName, int MIBEnum) {
        super(charsetName, MIBEnum);
    }

    // Javadoc inherited
    public CharacterRepresentable checkCharacter(int c) {
        return CharacterRepresentable.Representable;
    }

    protected String debugName() {
        return "NoEncoding";
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

 19-Jan-04	2653/7	steve	VBM:2004011304 Remove visibility of constants

 19-Jan-04	2653/1	steve	VBM:2004011304 Merge from proteus

 16-Jan-04	2576/3	steve	VBM:2004011304 Support multibyte character sets

 16-Jan-04	2576/3	steve	VBM:2004011304 Support multibyte character sets

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
