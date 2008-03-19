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

/**
 * This interface describes the ChunkReadHandler.
 * <p>The implementation of ChunkReadHandler is responsible for reconstituting
 * chunked data and passing it on to a
 * @link ChunkedReader.</p>
 * @mock.generate
 */
public interface ChunkReadHandler {
    /**
     * Read a chunk as a string.
     *
     * @return The chunk as a string.
     * @throws ExtendedIOException If an error occurs whilst reading the
     * chunked data.
     */
    public String readChunk() throws ExtendedIOException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 09-Jun-05	8346/3	ianw	VBM:2005051911 Fixed formating and expanded test case

 03-Jun-05	8346/1	ianw	VBM:2005051911 New JDBC Theme Accessor

 ===========================================================================
*/
