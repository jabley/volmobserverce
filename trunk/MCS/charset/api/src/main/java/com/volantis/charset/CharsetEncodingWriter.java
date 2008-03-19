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
 * 31-Mar-03    Mat             VBM:2003033108 - Created to wrap a writer and
 *                              encode the data according to the character set
 *                              being used.
 * 02-May-03    Mat             VBM:2003033108 - Moved to charset package and
 *                              removed dependencies on any mcs package.
 * 23-May-03    Mat             VBM:2003042907 - Refactored writeInteger() out
 *                              to CharsetEncodingHelper.
 * ----------------------------------------------------------------------------
 */

package com.volantis.charset;

import java.io.IOException;
import java.io.Writer;


/**
 * Class that wraps a writer, encoding all characters according to the charset
 * encoding, before writing them to the real writer.
 */
public class CharsetEncodingWriter extends Writer {

    /**
     * The wrapped writer.
     */
    private Writer writer;
    
    /**
     * The charset encoding class to use.
     */
    private Encoding encoding;
    
    /** Creates a new instance of CharsetEncodingWriter
        * @param writer The writer to wrap
        * @param encoding The encoding class to use.
     */
    public CharsetEncodingWriter(Writer writer, Encoding encoding) {
        this.writer = writer;
        this.encoding = encoding;
    }

    /**
     * Close the stream, flushing it first.  Once a stream has been closed,
     * further write() or flush() invocations will cause an IOException to be
     * thrown.  Closing a previously-closed stream, however, has no effect.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void close() throws IOException {
        writer.close();
    }

    /**
     * Flush the stream.  If the stream has saved any characters from the
     * various write() methods in a buffer, write them immediately to their
     * intended destination.  Then, if that destination is another character or
     * byte stream, flush it.  Thus one flush() invocation will flush all the
     * buffers in a chain of Writers and OutputStreams.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void flush() throws IOException {
        writer.flush();
    }

    /**
     * Write a portion of an array of characters.
     *
     * @param cbuf Array of characters
     * @param off  Offset from which to start writing characters
     * @param len  Number of characters to write
     * @throws IOException If an I/O error occurs
     */
    public void write(char[] cbuf, int off, int len) throws IOException {
        int writeStart = off;
        int writeLength = 0;
        
        // Walk through the character array.  If we find an unrepresentable
        // character, write out the array up to that character, then write 
        // out the unrepresentable character.
        
        for(int i = off; i < len; i++) {
            char c = cbuf[i];
            CharacterRepresentable rep = encoding.checkCharacter(c);
            if(rep.isRepresentable()) {
                writeLength++;
            } else {
                flush( cbuf,writeStart,writeLength,c);
                writeStart = i + 1;
                writeLength = 0;
            }
        }
        // Now mop up any final characters.
        if(writeLength > 0) {
            writer.write(cbuf, writeStart, writeLength);
        }
    }

    /**
     * Output the current buffer to the writer and escape the next character
     * @param cbuf the buffer of characters to write from
     * @param start the first character to write
     * @param length the number of characters to write
     * @param c the character to escape 
     * @throws IOException
     */
    private void flush( char[] cbuf, int start, int length, int c ) 
            throws IOException {
        if(length > 0) {
            writer.write(cbuf, start, length);
        }
        writer.write('&');
        writer.write('#');
        CharsetEncodingHelper.writeInteger(c, writer);
        writer.write(';');
            
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 19-Jan-04	2653/9	steve	VBM:2004011304 Remove visibility of constants

 19-Jan-04	2653/5	steve	VBM:2004011304 Merge from proteus

 19-Jan-04	2576/5	steve	VBM:2004011304 Remove visibility of constants

 19-Jan-04	2576/3	steve	VBM:2004011304 VBM Review tidy ups

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 ===========================================================================
*/
