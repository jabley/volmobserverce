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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * This output stream provides the byte array output stream features required
 * by the {@link MCSResponseWrapper} while also providing all servlet output
 * stream facilities.
 */
public class ByteArrayServletOutputStream extends ServletOutputStream {
    /**
     * The byte array output stream that this output stream delegates to. May
     * be null
     */
    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Initializes the new instance.
     */
    public ByteArrayServletOutputStream() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    // javadoc inherited
    public void write(byte b[]) throws IOException {
        byteArrayOutputStream.write(b);
    }

    // javadoc inherited
    public void write(byte b[], int off, int len) throws IOException {
        byteArrayOutputStream.write(b, off, len);
    }

    // javadoc inherited
    public void write(int b) throws IOException {
        byteArrayOutputStream.write(b);
    }

    /**
     * Creates a newly allocated byte array. Its size is the current size of
     * this output stream and the valid contents of the buffer have been copied
     * into it. Will return null if this output stream is not delegating to a
     * byte array output stream.
     *
     * @return the current contents of this output stream, as a byte array.
     * @see java.io.ByteArrayOutputStream#toByteArray()
     */
    public synchronized byte[] toByteArray() {
        byte[] result = byteArrayOutputStream.toByteArray();

        return result;
    }

    /**
     * Converts the buffer's contents into a string, translating bytes into
     * characters according to the specified character encoding. Will return
     * null if this output stream is not delegating to a byte array output
     * stream.
     *
     * @param enc a character-encoding name.
     * @return String translated from the buffer's contents.
     * @throws UnsupportedEncodingException If the named encoding is not
     *                                      supported.
     * @see java.io.ByteArrayOutputStream#toString(String)
     */
    public String toString(String enc) throws UnsupportedEncodingException {
        String result = byteArrayOutputStream.toString(enc);

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Feb-05	6806/1	philws	VBM:2005012610 Introduce out-of-the-box native XDIME handling

 ===========================================================================
*/
