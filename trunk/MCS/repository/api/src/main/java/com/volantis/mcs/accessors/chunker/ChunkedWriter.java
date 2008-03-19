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

import java.io.Writer;
import java.io.IOException;

/**
 * The ChunkedWriter implements a @link Writer that is able to take a stream of
 * output and write it into equal sized chunks.
 * <p>The main but by no means exclusive use of this is to chunk up XML output
 * for writing into a database</p>
 */
public class ChunkedWriter extends Writer {

    /**
     * The @link ChunkWriteHandler used to write the chunks from this
     * ChunkWriter.
     */
    private ChunkWriteHandler chunkWriteHandler;

    /**
     * The maximum allowable size of the chunk
     */
    private int chunkSize;

    /**
     * The buffer in which the chunk is constructed.
     */
    private char[] chunkBuffer;

    /**
     * The current offset in the chunk that we are writing to.
     */
    private int chunkOffset;

    /**
     * A flag to indicate if this Writer is closed.
     */
    private boolean closed;

    /**
     * Construct a new ChunkWriter with the given @link ChunkWriteHandler and
     * maximum chunk size.
     *
     * @param chunkWriteHandler The @link ChunkWriteHandler that will perform
     * the actual writing.
     * @param chunkSize         The maximum size of a chunk
     * @throws IOException If chunksize is less than 1.
     */
    public ChunkedWriter(ChunkWriteHandler chunkWriteHandler,
                         int chunkSize) throws IOException {
        if (chunkSize > 0) {
            this.chunkWriteHandler = chunkWriteHandler;
            this.chunkSize = chunkSize;
            chunkBuffer = new char[chunkSize];
            chunkOffset = 0;
            closed = false;
        } else {
            throw new IOException("Invalid chunksize.");
        }

    }

    //Javadoc Inherited
    public void close() throws IOException {
        flush();
        closed = true;
    }

    //Javadoc Inherited
    public void write(char cbuf[], int off, int len) throws IOException {
        if (!closed) {
            for (int i = off; i < (off + len); i++) {
                chunkBuffer[chunkOffset++] = cbuf[i];
                if (chunkOffset == chunkSize) {
                    chunkWriteHandler.writeChunk(new String(chunkBuffer));
                    chunkOffset = 0;
                }

            }
        } else {
            throw new IOException("write called after close.");
        }

    }

    //Javadoc Inherited
    public void flush() throws IOException {
        if (!closed) {
            if (chunkOffset != 0) {
                chunkWriteHandler.writeChunk(
                    new String(chunkBuffer, 0, chunkOffset));
                chunkOffset = 0;
            }
        } else {
            throw new IOException("flush called after close.");
        }

    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Oct-05	9938/1	ianw	VBM:2005101915 Fix up theme accessor issues

 21-Oct-05	9936/1	ianw	VBM:2005101915 Fix up theme accessor issues

 09-Jun-05	8346/3	ianw	VBM:2005051911 Fixed formating and expanded test case

 03-Jun-05	8346/1	ianw	VBM:2005051911 New JDBC Theme Accessor

 ===========================================================================
*/
