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

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

public class CachingInputStream extends FilterInputStream {

    private ByteArrayOutputStream cache = new ByteArrayOutputStream();

    public CachingInputStream(InputStream in) {
        super(in);
    }

    public int read() throws IOException {
        int ch = super.read();
        if (ch > 0) {
            cache.write(ch);
        }
        return ch;
    }

    public int read(byte buf[], int off, int len) throws IOException {
        int result = super.read(buf, off, len);
        if (result > 0) {
            cache.write(buf, off, result);
        }
        return result;
    }

    public byte[] toByteArray() {
        return cache.toByteArray();
    }

    public InputStream getCacheInputStream() {
        return new ByteArrayInputStream(cache.toByteArray());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10756/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 ===========================================================================
*/
