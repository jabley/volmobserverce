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
package com.volantis.shared.io;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class CachingOutputStream extends FilterOutputStream {

    /**
     * The cache of what was written.
     */
    private ByteArrayOutputStream cache = new ByteArrayOutputStream();

    public CachingOutputStream(OutputStream out) {
        super(out);
    }

    public void write(int c) throws IOException {
        out.write(c);
        cache.write(c);
    }

    public void write(byte cbuf[], int off, int len) throws IOException {
        out.write(cbuf, off, len);
        cache.write(cbuf, off, len);
    }

    /**
     * Return the content of the cache as a character array.
     *
     * @return the content of the cache as a character array.
     */
    public byte[] toByteArray() {
        return cache.toByteArray();
    }

    /**
     * Return a reader to the cache content.
     *
     * @return a reader to the cache content.
     */
    public InputStream getCacheInputStream() {
        return new ByteArrayInputStream(cache.toByteArray());
    }

    public void close() throws IOException {
        out.close();
        cache.close();
    }

    public void flush() throws IOException {
        out.flush();
        cache.flush();
    }
}
