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


package com.volantis.mcs.accessors.jdbc;

import com.volantis.mcs.accessors.chunker.ChunkReadHandler;
import com.volantis.shared.throwable.ExtendedIOException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the reading of chunked data from a JDBC @link ResultSet for a @link ChunkedReader.
 */
public class JDBCChunkReadHandler implements ChunkReadHandler {

    /**
     * The @link REsultSet containing the chunked data
     */
    private ResultSet rs;

    /**
     * The column containing the chunked data
     */
    private int chunkColumnIndex;

    /**
     * Construct a new JDBCChunkReadHandler with the given @link ResultSet
     * @param rs The @link ResultSet containing the chunked data
     * @param chunkColumnIndex The column number containing the chunked data
     */
    public JDBCChunkReadHandler(ResultSet rs, int chunkColumnIndex) {
        this.rs = rs;
        this.chunkColumnIndex = chunkColumnIndex;

    }

    //Javadoc Inherited
    public String readChunk() throws ExtendedIOException {
        String chunk = null;
            try {
                if (rs.next()) {
                    chunk = rs.getString(chunkColumnIndex);
                }
            } catch (SQLException e) {
                throw new ExtendedIOException(e);
            }
        return chunk;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Jun-05	8346/1	ianw	VBM:2005051911 New JDBC Theme Accessor

 ===========================================================================
*/
