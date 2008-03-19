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
 
package com.volantis.charset;

/**
 * An instance of this class is returned by the BitSetEncoding class to denote
 * ther validity of a character code depending on the character set used by
 * the encoding class.
 * Any character code can be <b>Representable</b> meaning that it can be
 * displayed as is. <b>Not Representable</b> meaning that the given code is not
 * valid in the character set at all.
 */
public class CharacterRepresentable {

    private static int REPRESENTABLE = 1;
    private static int NOTREPRESENTABLE = 2;

    /**
     * Object denoting a representable character code 
     */
    public final static CharacterRepresentable Representable = 
                             new CharacterRepresentable(REPRESENTABLE);
    /**
     * Object denoting a character code that is not representable
     * in a character set
     */
    public final static CharacterRepresentable NotRepresentable = 
                             new CharacterRepresentable(NOTREPRESENTABLE);

    /**
     * The state value of this representation
     */                           
    private int state = NOTREPRESENTABLE;
    
    /**
     * Create the state. 
     * This constructor is private so only the representations above can exist.
     */
    private CharacterRepresentable( int status ) {
        state = status;
    }
    
    /**
     * Denotes that a character code is representable in its own right within
     * a character set.
     * @return true if the character is representable, false if it is not
     */
    public boolean isRepresentable() {
        return (state==REPRESENTABLE);
    }

    /**
     * Denotes that a character code is not valid for a given character set.
     * @return true if the character is not valid in this set, false if it is.
     */
    public boolean notRepresentable() {
        return (state==NOTREPRESENTABLE);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Jan-04	2653/2	steve	VBM:2004011304 Remove visibility of constants

 16-Jan-04	2576/3	steve	VBM:2004011304 Support multibyte character sets

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 ===========================================================================
*/
