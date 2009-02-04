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
 * $Header: /src/voyager/com/volantis/mcs/utilities/ReusableStringWriter.java,v 1.1 2002/04/27 18:26:15 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-02    Allan           VBM:2002013101 - Created. A class that has a
 *                              similar functionality to that of StringWriter
 *                              except that it's internal buffer is a
 *                              ReusableStringBuffer instead of a StringBuffer.
 * 28-Mar-02    Allan           VBM:2002022007 - Added to Europa. 
 * 25-Apr-04    Allan           VBM:2002042404 - Moved to utilities package.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.utilities;

import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;

import java.io.IOException;
import java.io.Writer;

/**
 * Like a StringWriter but designed with re-use in mind. 
 */
public final class ReusableStringWriter extends Writer {

    /**
     * Volantis copyright.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Flag indicating whether the stream has been closed.
     */
    private boolean isClosed = false;

    /**
     * The ReusableStringBuffer to write to.
     */
    private ReusableStringBuffer buffer;

    /**
     * Create a new string writer, using the default initial string-buffer
     * size.
     */
    public ReusableStringWriter() {
	buffer = new ReusableStringBuffer();
    }

    /**
     * Create a new string writer, using the given buffer.
     * @param buffer the ReusableStringBuffer to hold the current buffer value.
     */
    public ReusableStringWriter(ReusableStringBuffer buffer) {
	this.buffer = buffer;
    }

    /**
     * Write a single character.
     */
    public void write(int c) {
	buffer.append((char) c);
    }

    /**
     * Write a portion of an array of characters.
     *
     * @param  cbuf  Array of characters
     * @param  off   Offset from which to start writing characters
     * @param  len   Number of characters to write
     */
    public void write(char cbuf[], int off, int len) {
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        buffer.append(cbuf, off, len);
    }

    /**
     * Write a string.
     */
    public void write(String str) {
	buffer.append(str);
    }

    /**
     * Write a ReusableStringBuffer.
     * @param buffer the ReusableStringBuffer to write
     */
    public void write(ReusableStringBuffer buffer) {
	buffer.append(buffer);
    }

    /**
     * Write a portion of a string.
     *
     * @param  str  String to be written
     * @param  off  Offset from which to start writing characters
     * @param  len  Number of characters to write
     */
    public void write(String str, int off, int len)  {
	buffer.append(str.substring(off, off + len));
    }

    /**
     * Return the buffer's current value as a string.
     */
    public String toString() {
	return buffer.toString();
    }

    /**
     * Return the reusable string buffer itself.
     *
     * @return ReusableStringBuffer holding the current buffer value.
     */
    public ReusableStringBuffer getBuffer() {
	return buffer;
    }

    /**
     * Set the reusable string buffer itself.
     *
     * @param buffer the ReusableStringBuffer to hold the current buffer value.
     */
    public void setBuffer(ReusableStringBuffer buffer) {
	this.buffer = buffer;
    }

    /**
     * Return the reusable character array belonging to string buffer itself.
     *
     * @return the char array belonging to the ReusableStringBuffer 
     * holding the current buffer value.
     */
    public char [] getChars() {
	return buffer.getChars();
    }

    /**
     * Flush the stream.
     */
    public void flush() { 
    }

    /**
     * Close the stream.  This method does not release the buffer, since its
     * contents might still be required.
     */
    public void close() throws IOException { 
	isClosed = true;
        buffer.setLength(0);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
