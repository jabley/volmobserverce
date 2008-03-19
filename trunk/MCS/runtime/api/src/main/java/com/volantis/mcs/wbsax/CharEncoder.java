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
 * 15-May-03    Geoff           VBM:2003042904 - Created; provides a character 
 *                              encoding service for WBSAX.
 * 20-May-03    Geoff           VBM:2003052102 - Added char[] overload.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Provides a character encoding service for WBSAX.
 * <p>
 * Note for WBSAX to be generally useful, we require a matching ByteDecoder
 * class, but since we do not currently have a use for it, I have not created
 * it yet.

 * @todo I think the use of checked exceptions here may be wrong. The way we 
 * use wbsax means that we should never encounter an encoding problem, unless 
 * there is a bug. This means that, according to Josh Bloch, we should not
 * throw checked exceptions. I think perhaps we ought to wrap the checked
 * UnsupportedEncodingException in a RuntimeException, this would certainly
 * remove a lot of try / catch blocks that don't add much value.
 */ 
public class CharEncoder {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(CharEncoder.class);

    /**
     * The underlying buffer that we output to.
     */ 
    private ByteArrayOutputStream baos;
    
    /**
     * Writer that encodes characters into bytes.
     */ 
    private Writer encodingWriter;
    
    /**
     * Construct an instance of this class, for the charset provided.
     * 
     * @param charset the name of the character set to encode as.
     * @throws WBSAXException if there was a problem during creation.
     */ 
    public CharEncoder(String charset) 
            throws WBSAXException {
        this.baos = new ByteArrayOutputStream();
        try {
            this.encodingWriter = 
                    new OutputStreamWriter(baos, charset);
        } catch (UnsupportedEncodingException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format("encoding-not-supported"), e);
        }
    }

    /**
     * Encode a string into a byte array of the character set passed in the 
     * constructor. Will include a null terminator.
     * 
     * @param string the string to encode.
     * @return the string, translated into a array of bytes of the appropriate
     *      character set (including the termination character).
     * @throws WBSAXException if there was a problem during the encoding.
     */ 
    public byte[] encode(final String string) throws WBSAXException {
        return writeVirtualString(new VirtualString() {
            public void writeTo(Writer writer) throws IOException {
                writer.write(string);
            }
        }, true);
    }

    /**
     * Encode a char array into a byte array of the character set passed in 
     * the constructor. Will include a null terminator.
     * 
     * @param chars the char array to encode.
     * @return the char array, translated into a array of bytes of the 
     *      appropriate character set (including the termination character).
     * @throws WBSAXException if there was a problem during the encoding.
     */ 
    public byte[] encode(final char[] chars) throws WBSAXException {
        return writeVirtualString(new VirtualString() {
            public void writeTo(Writer writer) throws IOException {
                writer.write(chars);
            }
        }, true);
    }

    /**
     * Encode a char into a byte array of the character set passed in 
     * the constructor. Will not include a null terminator.
     * 
     * @param c the char to encode.
     * @return the char array, translated into a array of bytes of the 
     *      appropriate character set (including the termination character).
     * @throws WBSAXException if there was a problem during the encoding.
     */ 
    public byte[] encode(final char c) throws WBSAXException {
        return writeVirtualString(new VirtualString() {
            public void writeTo(Writer writer) throws IOException {
                writer.write(c);
            }
        }, false);
    }

    /**
     * A "virtual string" which may be written to a writer.
     * <p>
     * A convenience interface to allow us to define anonynous inner classes
     * to simulate a closure.
     */ 
    private interface VirtualString {
        void writeTo(Writer writer) throws IOException;
    }
    
    /**
     * Encode a "virtual string" provided into a byte array.
     *  
     * @param virtualString the virtual string to encode.
     * @return the byte array that resulted.
     * @throws WBSAXException if there was a problem during the encoding.
     */ 
    private byte[] writeVirtualString(VirtualString virtualString, 
            boolean includeNull) throws WBSAXException {
        byte[] bytes;
        baos.reset();
        try {
            // Write out the string.
            virtualString.writeTo(encodingWriter);
            if (includeNull) {
                // Write out a terminator (Unicode null)
                encodingWriter.write(0x00);
            }
            // Flush the stream.
            // IBM VM definitely needs this, think Sun may not, but lets do 
            // it for now anyway.
            encodingWriter.flush();
        } catch (IOException e) {
            // Should never happen?
            // MCSWS0003X="Exception during character encoding"
            throw new WBSAXException(
                        exceptionLocalizer.format("character-encoding-error"),
                        e);
        }
        bytes = baos.toByteArray();
        return bytes;
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

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
