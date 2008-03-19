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
 * 18-May-03    Geoff           VBM:2003042904 - Created; an abstract base 
 *                              class for string factories. 
 * 20-May-03    Geoff           VBM:2003052102 - Add codec to constructor, 
 *                              rename InlineString to WBSAXString.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * A factory for creating {@link WBSAXString}s.
 * <p>
 * Currently this allows us to create strings from the various forms
 * of string data (byte[], char[], String). 
 * <p>
 * In the future we will probably wish to implement something like the 
 * Flyweight pattern to prevent excess garbage creation.
 * <p>
 * It might be nice to split this in half, one for logical and one for 
 * physical access. That is, one suitable for clients creating references from
 * high level data such as a DOM, and one suitable for clients creating
 * references from low level data such as a WBXML byte stream. This would make
 * it easier for the client to understand which methods were suitable given
 * the use they were making of the factory.
 */ 
public class StringFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    protected Codec codec;
    
    /**
     * Construct an instance of this class.
     * 
     * @param codec the codec to use for creating strings.
     */ 
    public StringFactory(Codec codec) {
        this.codec = codec;
    }

    /**
     * Return the codec that this instance is using for character conversion.
     * 
     * @return the Codec
     */ 
    public Codec getCodec() {
        return codec;
    }
    
    //
    // Logical and Physical
    // 
    
    /**
     * Create an inline string from an array of bytes.
     * 
     * @param bytes the byte content of the string, must include the 
     *      termination character.
     * @return a WBSAXString created from the byte array
     */ 
    public WBSAXString create(byte[] bytes) {
        return createFromBytes(bytes);
    }

    /**
     * Convenience method to create an inline string.
     *  
     * @param bytes the byte content of the string, must include the 
     *      termination character.
     * @return the inline string created.
     */ 
    protected final WBSAXString createFromBytes(byte[] bytes) {
        return new WBSAXString(codec, bytes);
    }

    //
    // Logical Only.
    //
    
    /**
     * Create a WBSAX string from a String.
     *  
     * @param string the character content of the string.
     * @return the inline string created.
     */ 
    public WBSAXString create(String string) {
        return new WBSAXString(codec, string);
    }

    /**
     * Create a WBSAX string from a portion of a String.
     *  
     * @param string the character content of the string.
     * @param offset the offset of the string data within the string. 
     * @param length the length of the string data within the string. 
     * @return the inline string created.
     */ 
    public WBSAXString create(String string, int offset, int length) {
        char[] cbuf = new char[length];
        string.getChars(offset, (offset + length), cbuf, 0);
        return create(cbuf);
    }

    /**
     * Create a WBSAX string from a char array.
     *  
     * @param chars the character content of the string.
     * @return the inline string created.
     */ 
    public WBSAXString create(char[] chars) {
        return new WBSAXString(codec, chars);
    }

    /**
     * Create a WBSAX string from a portion of a char array.
     *  
     * @param chars the character content of the string.
     * @param offset the offset of the string data within chars. 
     * @param length the length of the string data within chars. 
     * @return the inline string created.
     */ 
    public WBSAXString create(char[] chars, int offset, int length) {
        char[] buffer = new char[length];
        System.arraycopy(chars, offset, buffer, 0, length);
        return new WBSAXString(codec, buffer);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 14-Jul-03	790/2	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/2	geoff	VBM:2003070404 clean up WBSAX

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
