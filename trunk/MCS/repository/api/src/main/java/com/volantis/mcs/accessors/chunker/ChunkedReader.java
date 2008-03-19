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


package com.volantis.mcs.accessors.chunker;

import com.volantis.shared.throwable.ExtendedIOException;

import java.io.Reader;
import java.io.IOException;

/**
 * The ChunkedReader implements a @link Reader that is able to take a stream of
 * chunked data and reconstitute it.
 * <p>The main but by no means exclusive use of this is to read chunked up XML
 * from a database</p>
 */
public class ChunkedReader extends Reader {

    /**
     * A flag to indicate if this Reader is closed.
     */
    private boolean closed;
    /**
     * The @link ChunkReadHandler used to read the chunks for this
     * ChunkReader.
     */
    private ChunkReadHandler chunkReadHandler;

    /**
     * The buffer into wich the chunk is read.
     */
    private char[] chunkBuffer;

    /**
     * The number of available characters in the buffer.
     */
    private int available;

    /**
     * The current location in the buffer from where we are reading
     */
    private int chunkOffset;

    /**
     * Indicator for the end of the input stream.
     */
    private boolean endOfStream;

    /**
     * A cache of the first chunk that we read in the constructor.
     * <p>
     * This is required so that we can figure out if we are empty before
     * we are used for reading.
     */
    private String cachedChunk;

    /**
     * Construct a new ChunkReader with the given @link ChunkReadHandler and
     * maximum chunk size.
     *
     * @param chunkReadHandler The @link ChunkReadHandler that will perform the
     * actual reading.
     */
    public ChunkedReader(ChunkReadHandler chunkReadHandler)
            throws ExtendedIOException {
        this.chunkReadHandler = chunkReadHandler;
        closed = false;
        endOfStream = false;

        // Read in the first chunk so we can see if the reader is empty before
        // we start using it for real. This avoids having to do an additional
        // JDBC query to figure out if there are any chunks actually present
        // before we query for them.
        cachedChunk = readChunk();
        if (cachedChunk == null) {
            endOfStream = true;
        }
    }

    /**
     * Returns true if the reader is at the end of the stream.
     *
     * @return true if the reader is at the end of the stream.
     */
    public boolean isEndOfStream() {
        return endOfStream;
    }

    //Javadoc inherited
    public void close() throws IOException {
        closed = true;
    }

    //Javadoc inherited
    public int read(char cbuf[], int off, int len) throws IOException {
        int count = 0;
        if (!closed) {
            if (!endOfStream) {

                while ((count < len) && !endOfStream) {
                    if (available == 0) {
                        String chunk = readChunk();
                        if (chunk != null) {
                            chunkBuffer = chunk.toCharArray();
                            available = chunkBuffer.length;
                            chunkOffset = 0;
                        } else {
                            endOfStream = true;
                            break;
                        }
                    }
                    cbuf[off + count] = chunkBuffer[chunkOffset];
                    available--;
                    chunkOffset++;
                    count++;
                }

            } else {
                count = -1;
            }
        } else {
            throw new IOException("read called after close.");
        }
        return count;
    }

    /**
     * Read the next available chunk.
     * <p>
     * This will return the cached chunk if it is available.
     *
     * @return the next chunk as a String.
     * @throws ExtendedIOException
     */
    private String readChunk() throws ExtendedIOException {
        String chunk = null;
        if (cachedChunk != null) {
            chunk = cachedChunk;
            cachedChunk = null;
        } else {
            chunk = chunkReadHandler.readChunk();
        }
        return chunk;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 27-Oct-05	9965/4	ianw	VBM:2005101811 interim commit

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 09-Jun-05	8346/3	ianw	VBM:2005051911 Fixed formating and expanded test case

 03-Jun-05	8346/1	ianw	VBM:2005051911 New JDBC Theme Accessor

 ===========================================================================
*/
