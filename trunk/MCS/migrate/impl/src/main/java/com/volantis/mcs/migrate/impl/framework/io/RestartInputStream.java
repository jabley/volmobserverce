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
package com.volantis.mcs.migrate.impl.framework.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A filter input stream which buffers the stream it wraps and allows clients
 * to start reading the contained data from the start again at any point.
 * <p>
 * NOTE: This class does buffering, but not the standard readahead style
 * buffering that Java supports, so {@link #markSupported} returns false.
 * <p>
 * NOTE: The implementation of this class is the simplest possible rather than
 * the fastest. In future if performance is an issue this could be rewritten
 * to avoid reading the entire input at construction time.
 *
 * @mock.generate base="FilterInputStream"
 */
public class RestartInputStream extends FilterInputStream {

    /**
     * Buffer we store the content in.
     */
    private byte[] buffer;

    /**
     * Initialise.
     *
     * @param input the stream that this object buffers.
     *
     * @throws IOException
     */
    public RestartInputStream(InputStream input) throws IOException {

        super(null);

        // Read the whole input into the buffer up front.
        InputStreamByteArray isba = new InputStreamByteArray(input);
        buffer = isba.getByteArray();

        // And kick start the input.
        createNewStreamFromBuffer();
    }

    /**
     * Rewinds the input stream all the way back to the start again.
     */
    public void restart() {

        createNewStreamFromBuffer();
    }

    /**
     * Not supported, returns false.
     */
    public boolean markSupported() {

        return false;
    }

    /**
     * Not implemented, will thow an exception if called.
     */
    public synchronized void reset() throws IOException {

        throw new IllegalStateException();
    }

    /**
     * Not supported, does nothing.
     */
    public synchronized void mark(int readlimit) {
    }

    /**
     * Internal helper to create the parent class input stream from the buffer.
     */
    private void createNewStreamFromBuffer() {

        in = new ByteArrayInputStream(buffer);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
