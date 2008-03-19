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
package com.volantis.shared.content;

import com.volantis.shared.io.CachingInputStream;
import com.volantis.shared.io.CachingReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class CachingContentInput implements ContentInput {

    private ContentInput cached;

    private CachingInputStream cachingInputStream;

    private CachingReader cachingReader;

    public CachingContentInput(ContentInput cached) {
        this.cached = cached;
    }

    public ContentStyle getContentStyle() {
        return cached.getContentStyle();
    }

    public InputStream getInputStream() throws IOException {
        if (cachingInputStream == null) {
            cachingInputStream = new CachingInputStream(cached.getInputStream());
        }
        return cachingInputStream;
    }

    public Reader getReader() throws IOException {
        if (cachingReader == null) {
            cachingReader = new CachingReader(cached.getReader());
        }
        return cachingReader;
    }

    public ContentInput getCachedContent() {
        return new ContentInput() {
            public ContentStyle getContentStyle() {
                return cached.getContentStyle();
            }

            public InputStream getInputStream() {
                return cachingInputStream.getCacheInputStream();
            }

            public Reader getReader() {
                return cachingReader.getCacheReader();
            }
        };
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
