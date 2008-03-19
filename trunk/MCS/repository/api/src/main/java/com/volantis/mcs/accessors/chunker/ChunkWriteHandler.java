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

import java.io.IOException;

/**
 * This interface describes the ChunkWriteHandler.
 * <p>The implementation of ChunkWriteHandler is responsible for writing the
 * chunked data from the
 * @link ChunkedWriter. to the storage medium.</p>
 * @mock.generate
 *
 **/
public interface ChunkWriteHandler {

    /**
     * Handle the writing of the chunked data to the storage medium.
     *
     *
     * @param chunk The contents of this chunk.
     * @throws IOException If an error occurs whilst writing the chunk
     */
    public void writeChunk(String chunk) throws IOException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Jun-05	8346/3	ianw	VBM:2005051911 Fixed formating and expanded test case

 03-Jun-05	8346/1	ianw	VBM:2005051911 New JDBC Theme Accessor

 ===========================================================================
*/
