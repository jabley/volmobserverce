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


package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.accessors.chunker.ChunkWriteHandler;
import com.volantis.mcs.accessors.jdbc.JDBCAccessorHelper;
import com.volantis.shared.throwable.ExtendedIOException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;


public class JDBCChunkWriteHandler implements ChunkWriteHandler {

    /**
     * The logging object to use in this class for localised logging services.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(JDBCChunkWriteHandler.class);

    private int rows;

    /**
     * The sequence number of the chunk being writed
     */
    private int chunkNumber;

    /**
     * The index of the CHUNK_ID column
     */
    private int chunkIdColumnIndex;

    /**
     * The index of the CHUNK_ID column
     */
    private int chunkColumnIndex;

    /**
     * The @link PreparedStatement containing the SQL to insert a chunk row into the database
     */
    private PreparedStatement pstmt;


   /**
    * Construct a new JDBCChunkWriteHandler, passing in the @link PreparedStatement that will be used to write out a
    * chunk.
    *
    * @param pstmt The @link PreparedStatement used to write out the chunk.
    * @param chunkIdColumnIndex The index to the CHUNK_ID column of the @link PreparedStatement.
    * @param chunkColumnIndex The index to the CHUNK column of the @link PreparedStatement.
    */
    public JDBCChunkWriteHandler(PreparedStatement pstmt, int chunkIdColumnIndex, int chunkColumnIndex ) {
        rows = 0;
        this.pstmt = pstmt;
        this.chunkIdColumnIndex = chunkIdColumnIndex;
        this.chunkColumnIndex = chunkColumnIndex;
        chunkNumber = 0;
    }

    //Javadoc Inherited
    public void writeChunk(String chunk) throws IOException {

        try {
            pstmt.setInt(chunkIdColumnIndex,chunkNumber);
            chunkNumber ++;
            JDBCAccessorHelper.setStringValue(pstmt, chunkColumnIndex, chunk);
            rows = rows + pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ExtendedIOException(e);
        }

    }

    public int getRows() {
        return rows;
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
