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

import java.io.FilterReader;
import java.io.Reader;
import java.io.IOException;
import java.io.CharArrayWriter;
import java.io.CharArrayReader;

public class CachingReader extends FilterReader {

    private CharArrayWriter cache = new CharArrayWriter();

    public CachingReader(Reader in) {
        super(in);
    }

    public int read() throws IOException {
        int ch = super.read();
        if (ch > 0) {
            cache.write(ch);
        }
        return ch;
    }

    public int read(char cbuf[], int off, int len) throws IOException {
        int result = super.read(cbuf, off, len);
        if (result > 0) {
            cache.write(cbuf, off, result);
        }
        return result;
    }

    public char[] toCharArray() {
        return cache.toCharArray();
    }

    public Reader getCacheReader() {
        return new CharArrayReader(cache.toCharArray());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10756/4	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/2	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 ===========================================================================
*/
