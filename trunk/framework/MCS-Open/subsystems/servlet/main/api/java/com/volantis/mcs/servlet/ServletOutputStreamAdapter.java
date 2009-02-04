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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import javax.servlet.ServletOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * A simple adapter that can wrap an arbitrary OutputStream as a
 * ServletOutputStream.
 */
public class ServletOutputStreamAdapter extends ServletOutputStream {
    /**
     * The output stream wrapped by this adapter.
     */
    private OutputStream wrapped;

    /**
     * Create a ServletOutputStreamAdapter wrapping a specified output
     * stream.
     *
     * @param toWrap The output stream to wrap
     */
    public ServletOutputStreamAdapter(OutputStream toWrap) {
        wrapped = toWrap;
    }

    // Javadoc inherited
    public void write(int b) throws IOException {
        wrapped.write((byte) (b & 0xff));
    }

    // Javadoc inherited
    public void write(byte b[]) throws IOException {
        wrapped.write(b);
    }

    // Javadoc inherited
    public void write(byte b[], int off, int len) throws IOException {
        wrapped.write(b, off, len);
    }

    // Javadoc inherited
    public void flush() throws IOException {
        wrapped.flush();
    }

    // Javadoc inherited
    public void close() throws IOException {
        wrapped.close();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	6786/1	adrianj	VBM:2005012506 Rendered page cache rework

 ===========================================================================
*/
