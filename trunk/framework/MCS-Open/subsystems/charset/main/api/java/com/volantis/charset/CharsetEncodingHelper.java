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
 * $Header: /src/voyager/com/volantis/mcs/protocols/CharsetEncodingWriter.java,v 1.1.2.1 2003/04/28 08:50:14 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-May-03    Mat             VBM:2003042907 - Created from code originally
 *                              in CharsetEncodingWriter.
 * ----------------------------------------------------------------------------
 */
package com.volantis.charset;

import java.io.IOException;
import java.io.Writer;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Helper class to provide a fast method to write an Integer to a writer.
 * @author mat
 *
 */
public class CharsetEncodingHelper {
	/**
	 * The copyright statement.
	 */
	private static final String mark = "(c) Volantis Systems Ltd 2003.";
        
    /**
     * Used for logging
     */
    private static LogDispatcher logger =
            LocalizationFactory.createLogger(CharsetEncodingHelper.class);
    
	/**
	 * The digits for numbers, indexed by multiple 
	 * of 10
	 */
	private final static char [] DigitTens = {
	'0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
	'1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
	'2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
	'3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
	'4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
	'5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
	'6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
	'7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
	'8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
	'9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
	} ; 
    
	/**
	 * All possible chars for representing a number as a String
	 */
	private final static char[] digits = {
	'0' , '1' , '2' , '3' , '4' , '5' ,
	'6' , '7' , '8' , '9' , 'a' , 'b' ,
	'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
	'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
	'o' , 'p' , 'q' , 'r' , 's' , 't' ,
	'u' , 'v' , 'w' , 'x' , 'y' , 'z'
	};
    
	/**
	 * A map of numbers to characters
	 */
	private final static char [] DigitOnes = { 
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	} ;
    
	/** 
	 * Number of characters in array to represent an integer
	 */
	private static final int INT_ARRAY_LENGTH = 12;
    
	/** Write an integer out to the writer in the fastest, most efficient
	* way.  This method creates a 12 character buffer and nothing else.
	* It is a direct copy of the Integer.toString() method in Suns JDK 1.4.1
	* minus any code for dealing with the sign, as we will always have positive
	* numbers.
	* Unless you understand how this works, it should not be changed.
	* @param i The integer to output
	* @param writer The wrapped writer to use.
	* @throws IOException An output error
	 */
	protected static void writeInteger(int i, Writer writer) throws IOException {
	    // The I here is the Sun guy who implemented the code - not me!!
	    //
	    // I use the "invariant division by multiplication" trick to
	    // accelerate Integer.toString.  In particular we want to
	    // avoid division by 10.
	    //
	    // The "trick" has roughly the same performance characterists
	    // as the "classic" Integer.toString code on a non-JIT VM.
	    // The trick avoids .rem and .div calls but has a longer code
	    // path and is thus dominated by dispatch overhead.  In the
	    // JIT case the dispatch overhead doesn't exist and the
	    // "trick" is considerably faster than the classic code.
	    //
	    // RE:  Division by Invariant Integers using Multiplication
	    //      T Gralund, P Montgomery
	    //      ACM PLDI 1994
	    //
	    int q;
	    int r;
	    int charPos = INT_ARRAY_LENGTH; 
	    char buf[] = null; 
	    
	    if(buf == null) {
	        buf = new char[charPos]; 
	    }
	
	    // Generate two digits per iteration
	    while ( i >= 65536 ) { 
	        q = i / 100 ; 
	        // really: r = i - (q * 100) ; 
	        r = i - ((q << 6) + (q << 5) + (q << 2)) ; 
	        i = q ; 
	        buf [--charPos] = DigitOnes[r]; 
	        buf [--charPos] = DigitTens[r]; 
	    }
	
	    // Fall thru to fast mode for smaller numbers
	    // assert(i <= 65536, i);
	    for (;;) { 
	        q = (i * 52429) >>> (16+3); 
	        r = i - ((q << 3) + (q << 1));		// r = i-(q*10) ...
	        buf [--charPos] = digits[r]; 
	        i = q ; 
	        if (i == 0) {
	            break ; 
	        }
	    }
	    
	    // Write the buffer.
	    if(logger.isDebugEnabled()) {
	        String buffer = new String(buf);
	        logger.debug("Buffer = " + buffer);
	    }
	    writer.write(buf, charPos, INT_ARRAY_LENGTH - charPos);
	}

    
        
    
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 15-Apr-04	3881/1	steve	VBM:2004032606 Allow assignment of MCS nature to imported projects

 ===========================================================================
*/
