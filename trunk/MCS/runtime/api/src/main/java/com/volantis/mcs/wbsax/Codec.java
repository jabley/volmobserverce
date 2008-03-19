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
 * 20-May-03    Geoff           VBM:2003052102 - Created; the central 
 *                              management point for char/byte codec. 
 * 22-May-03    Mat             VBM:2003042907 - Added constructor that 
 *                              accepts a CharsetCode.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * The central management point for char/byte codec of a WBSAX event stream.
 * <p>
 * Generally the input side will provide a charset name which drives the 
 * char/byte codec behaviour of WBSAX. If the input side provides all strings, 
 * then it may leave the charset name as null, in which case the output side 
 * may nominate the charset it wishes the strings to be converted to. 
 */ 
public class Codec {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Represents the character set to be used for codec.
     */ 
    private CharsetCode charset;
    
    /**
     * The encoder to use (based on the charset code's charset name).
     */ 
    private CharEncoder encoder;
    
    //private ByteDecoder decoder;

    /**
     *  Constructor for a Codec
     *  @param charset  The CharsetCode
     */
    public Codec(CharsetCode charset) {
        setCharset(charset);
    }
    
    /**
     * Default constructor for a Codec
     * 
     */
    public Codec() {
    }
    
    /**
     * Returns the code for charset used for byte encoding.
     * <p>
     * This may be null if the input side provided only strings and the output 
     * side has not provided one.
     * 
     * @return the name of the charset used to encode bytes, or null.
     */ 
    public CharsetCode getCharset() {
        return charset;
    }

    /**
     * Sets the code for the charset to be used for byte encoding. Must be 
     * called by the input side if providing byte content, may be called by 
     * the output side if not called by the input side.
     * 
     * @param charset the code for the charset to use for byte encoding.
     */ 
    public void setCharset(CharsetCode charset) {
        if (this.charset != null) {
            throw new IllegalStateException(
                    "Cannot set charset more than once");
        }
        this.charset = charset;
    }

    /**
     * Returns the encoder to use for converting Strings to byte arrays.
     * 
     * @return the encoder to use for converting Strings to byte arrays.
     * @throws WBSAXException if there was a conversion problem.
     * @throws IllegalStateException if no charset name has been provided.
     */ 
    CharEncoder getEncoder() throws WBSAXException {
        if (encoder == null) {
            if (charset != null) {
                encoder = new CharEncoder(charset.getCharsetName());
            } else {
                throw new IllegalStateException(
                        "Attempt to encode with no character set");
            }
        }
        return encoder;
    }

//    // this is kept as an indication of what will need to be added to 
//    // support "parsing".    
//    public ByteDecoder getDecoder() {
//        if (decoder == null) {
//            if (charsetName != null) {
//                decoder = new ByteDecoder(charset.getCharsetName());
//            } else {
//                throw new IllegalStateException(
//                        "Attempt to decode with no character set");
//            }
//        } 
//        return decoder;
//    }
    
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
