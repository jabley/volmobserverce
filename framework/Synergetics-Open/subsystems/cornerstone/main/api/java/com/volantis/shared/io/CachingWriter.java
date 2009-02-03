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

import java.io.Writer;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.CharArrayWriter;
import java.io.Reader;
import java.io.CharArrayReader;

/**
 * A writer that saves a cache of what was written.
 */
public class CachingWriter extends FilterWriter {

    /**
     * The cache of what was written.
     */
    private CharArrayWriter cache = new CharArrayWriter();

    public CachingWriter(Writer out) {
        super(out);
    }

    public void write(int c) throws IOException {
        super.write(c);
        cache.write(c);
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        super.write(cbuf, off, len);
        cache.write(cbuf, off, len);
    }

    public void write(String str, int off, int len) throws IOException {
        super.write(str, off, len);
        cache.write(str, off, len);
    }

    /**
     * Return the content of the cache as a character array.
     *
     * @return the content of the cache as a character array.
     */
    public char[] toCharArray() {
        return cache.toCharArray();
    }

    /**
     * Return a reader to the cache content.
     *
     * @return a reader to the cache content.
     */
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
