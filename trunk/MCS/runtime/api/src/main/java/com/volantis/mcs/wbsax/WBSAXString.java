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
 * 18-May-03    Geoff           VBM:2003042904 - Created; represents an inline 
 *                              string in a WBSAX event stream.
 * 20-May-03    Geoff           VBM:2003052102 - rename InlineString to 
 *                              WBSAXString, pass Codec in constructor rather
 *                              than StringTable.
 * 20-May-03    Geoff           VBM:2003052102 - Added getChars() method and 
 *                              modified other get() methods to convert from
 *                              char[].
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * 30-May-03    Mat             VBM:2003042906 - Changed to throw 
 *                              RuntimeWrappingException in accept
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents a string in a WBSAX event stream.
 * <p>
 * {@link WBSAXString} is used to implement all the various types of strings 
 * present in WBSAX. It is used for:
 * <ul>
 *   <li>{@link GlobalToken#STR_I} attribute and content inline strings.
 *   <li>{@link GlobalToken#STR_T} attribute and content string references.
 *   <li>{@link GlobalToken#LITERAL} literal element and attribute names.
 *   <li>{@link GlobalToken#EXT_I_0}, etc extension inline strings.
 *   <li>{@link GlobalToken#EXT_T_0}, etc extension string references.
 * </ul>
 * All instances of this class are created via an instance of 
 * {@link StringFactory}. Instances used for string references are created and
 * accessed indirectly via their own {@link StringReferenceFactory}.
 * <p>
 * {@link WBSAXString} has three internal forms:
 * <ul>
 *   <li>byte [] - an array of bytes 
 *   <li>char [] - an array of characters
 *   <li>String - a Java string
 * </ul>
 * None of these forms is canonical. This class allows its clients to 
 * retrieve the contents of the string in the form that it requires. 
 * It will do any conversion necessary using the {@link Codec} previously 
 * defined for this WBSAX event stream.
 * <p>
 * Note that the byte[] form of the string includes a "null terminator" byte
 * whilst the char[] and String versions do not. This is a convenience which 
 * is possible because WBSAX defines that all string data is null terminated.
 * 
 * @todo it would be useful to have an interface for the getString and getBytes 
 * methods so that there are standard ways to get the content from the various 
 * WBSAX objects. For example, we could define a cost() method which took a 
 * BinaryContent interface which would work for both strings and multi byte 
 * integers. This is complified, however, by the fact that some binary content
 * is available directly, and other (string) binary content may need to be
 * translated from text content, which means you can get an exception. See the
 * to do in CharEncoder for more details.
 */ 
public class WBSAXString implements WBSAXValueVisitor.Acceptor {

    public interface InternalIterator {
        void next(WBSAXCharacter character);
    }
    
    /**
     * A cached reference to the codec in use for this WBSAX event stream.
     * <p>
     * This will be used to convert string data from the original form to
     * the requested form if necessary.
     */ 
    private Codec codec;
    
    /**
     * The string value as an array of bytes, including the null terminator.
     * <p>
     * This may be null if this instance was created with another type and 
     * conversion has not yet been performed.
     */ 
    private byte[] bytes;
    
    /**
     * The string value as an array of chars.
     * <p>
     * This may be null if this instance was created with another type and 
     * conversion has not yet been performed.
     */ 
    private char[] chars;
    
    /**
     * The string value as a String.
     * <p>
     * This may be null if this instance was created with another type and 
     * conversion has not yet been performed.
     */ 
    private String string;

    /**
     * The string value as individually encoded wbsax "characters".
     * <p>
     * This may be null if {@link #forEachCharacter} has not been called. 
     */ 
    private WBSAXCharacter[] characters;
    
    /**
     * Create an instance of this class, using the string table and String
     * value provided.
     * 
     * @param codec the codec to use for any conversions.
     * @param string the value to use for this string.
     */ 
    public WBSAXString(Codec codec, String string) {
        this.codec = codec;
        this.string = string;
    }
    
    /**
     * Create an instance of this class, using the codec and char [] 
     * value provided (this must not include the null terminator).
     * 
     * @param codec the codec to use for any conversions.
     * @param string the value to use for this string.
     */ 
    public WBSAXString(Codec codec, char[] string) {
        this.codec = codec;
        this.chars = string;
    }

    /**
     * Create an instance of this class, using the codec and byte [] 
     * value provided (this must include the null terminator).
     * 
     * @param codec the codec to use for any conversions.
     * @param string the value to use for this string.
     */ 
    public WBSAXString(Codec codec, byte[] string) {
        this.codec = codec;
        this.bytes = string;
    }
    
    /**
     * Returns the value of this string as a byte array, including the null 
     * terminator.
     * <p>
     * If this string was not constructed with a byte array, this value will 
     * be derived by converting it from the type supplied.
     * <p>
     * If you just wish to see how each character turns into bytes, use 
     * {@link #forEachCharacter} instead.
     * <p>
     * Note that unlike {@link #forEachCharacter}, this method includes the
     * null terminator.
     * 
     * @return the value of this string as a byte array.
     * @throws WBSAXException if there was a problem during encoding.
     */ 
    public byte[] getBytes() throws WBSAXException {
        if (bytes == null) {
            if (chars != null) {
                // char[] to byte[]
                bytes = codec.getEncoder().encode(chars);
            } else if (string != null) {
                // String to byte[]
                bytes = codec.getEncoder().encode(string);
            } else {
                // No values at all!
                throw new IllegalStateException();
            }
        }
        return bytes;
    }

    /**
     * Returns the value of this string as a char array.
     * <p>
     * If this string was not constructed with a char array, this value will 
     * be derived by converting it from the type supplied.
     * 
     * @return the value of this string as a byte array.
     * @throws WBSAXException if there was a problem during encoding.
     */ 
    public char[] getChars() throws WBSAXException {
        if (chars == null) {
            if (string != null) {
                // String to char[]
                chars = string.toCharArray();
            } else if (bytes != null) {
                // byte[] to char[]
                throw new UnsupportedOperationException(
                        "binary input not implemented");
                // chars = codec.getDecoder().decode(bytes); 
            } else {
                // No values at all!
                throw new IllegalStateException();
            }
        }
        return chars;
    }
    
    /**
     * Returns the value of this string as a String.
     * <p>
     * If this string was not constructed with a String, this value
     * will be derived by converting it from the type supplied.
     * 
     * @return the value of this string as String.
     * @throws WBSAXException if there was a problem during decoding.
     */ 
    public String getString() throws WBSAXException {
        if (string == null) {
            if (chars != null) {
                // char[] to String
                string = new String(chars);
            } else if (bytes != null) {
                // byte[] to String
                throw new UnsupportedOperationException(
                        "binary input not implemented");
                // string = codec.getDecoder().decode(bytes);
            } else {
                // No values at all!
                throw new IllegalStateException();
            }
        }
        return string;
    }

    /**
     * Convert the characters in the string to bytes individually, informing 
     * the encoding listener what was done for each character.
     * <p>
     * This is useful for clients who need have a detailed understanding
     * of string content, i.e. how each character maps to each byte.
     * <p>
     * If you just wish to get all bytes in this string as a block, use 
     * {@link #getBytes} instead. 
     * <p>
     * Note that unlike {@link #getBytes}, this method ignores the null 
     * terminator, since it is "driven" from the text representation of the
     * string which does not include one. 
     * 
     * @param iterator is informed how each character was encoded.
     * @throws WBSAXException if there was an encoding or decoding problem.
     * 
     * @todo implement character caching/factory/flyweight for performance.
     */ 
    public void forEachCharacter(InternalIterator iterator) 
            throws WBSAXException {
        if (characters == null) {
            // We haven't been here before, so iterate over each char, 
            // creating the matching wbsax "character", and ensure we save
            // them so that if this is called again we can skip the creation
            // part.
            
            // Grab the chars. This will involve converting them from bytes
            // if that was what we were provided with. This seems strange given
            // we then convert them back to bytes, but it is unavoidable.
            char[] chars = getChars();
            // Loop over the chars, converting them one at a time and saving
            // the converted length of each as it's cost. Seems slow but I 
            // can't think of a better way to do it.
            int charLength = chars.length;
            characters = new WBSAXCharacter[charLength];
            byte[] theCharBytes;
            for (int i = 0; i < charLength; i++) {
                char theChar = chars[i];
                // Create the wbsax "character"
                theCharBytes = codec.getEncoder().encode(theChar);
                WBSAXCharacter character = new WBSAXCharacter(theChar, 
                        theCharBytes);
                // Save it for next time.
                characters[i] = character;
                // And tell the iterator.
                iterator.next(character);
            }
        } else {
            // We already created the characters, so just iterate over em.
            
            for (int i= 0; i < characters.length; i++) {
                iterator.next(characters[i]);
            }
        }
    }
    
    // Inherit javadoc.
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        visitor.visitString(this);
    }

    // Inherit javadoc.
    public String toString() {
        try {
            return getString();
        } catch (WBSAXException e) {
            throw new RuntimeException(e);
        }
    }
    
    // Inherit javadoc.
    public boolean equals(Object obj) {
        /* The Codec is the same for all WBSAXStrings within the same document
         * so we won't worry about it here as we don't anticipate comparing
         * WBSAXStrings between different documents.
         */
        if (this == obj)  {
            return true;
        }
        
        if (obj instanceof WBSAXString)  {
            WBSAXString wbsaxString = (WBSAXString)obj;
            try {
                /* @todo At the moment this object does not have a canonical
                 * form and so the conversion from byte form to string form
                 * will throw an UnsupportedOperationException
                 */
                return getString().equals(wbsaxString.getString());
            } catch (WBSAXException e) {                
                throw new RuntimeException(e);
            }            
        } else {
            return false;
        }
    }

    // Inherit javadoc.
    public int hashCode() {
        try  {
            return getString().hashCode();
        } catch (WBSAXException e)  {
            throw new RuntimeException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 24-Jun-03	365/2	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 13-Jun-03	372/2	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
