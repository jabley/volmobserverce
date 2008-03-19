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

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.repository.impl.jdbc.JDBCChunkWriteHandler;
import mock.java.sql.PreparedStatementMock;

import java.sql.PreparedStatement;


public class JDBCChunkWriteHandlerTestCase extends TestCaseAbstract {

    public void testJDBCChunkWriteHandler()
            throws Exception {

        final int CHUNK_COLUMN_ID_INDEX = 3;
        final int CHUNK_COLUMN_INDEX = 4;

        // Test writeChunk
        //
        // We expect to write 4 chunks, "12", "34", "56" and "7" which are chunk 0,1,2,3 and 4 respectively.
        // 4 Rows should be written to the database.
        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();
        PreparedStatementMock preparedStatementMock = new PreparedStatementMock("PreparedStatement", expectations);


        preparedStatementMock.expects.setInt(CHUNK_COLUMN_ID_INDEX, 0);
        preparedStatementMock.expects.setString(CHUNK_COLUMN_INDEX, "12");
        preparedStatementMock.expects.executeUpdate().returns(1);
        preparedStatementMock.expects.setInt(CHUNK_COLUMN_ID_INDEX, 1);
        preparedStatementMock.expects.setString(CHUNK_COLUMN_INDEX, "34");
        preparedStatementMock.expects.executeUpdate().returns(1);
        preparedStatementMock.expects.setInt(CHUNK_COLUMN_ID_INDEX, 2);
        preparedStatementMock.expects.setString(CHUNK_COLUMN_INDEX, "56");
        preparedStatementMock.expects.executeUpdate().returns(1);
        preparedStatementMock.expects.setInt(CHUNK_COLUMN_ID_INDEX, 3);
        preparedStatementMock.expects.setString(CHUNK_COLUMN_INDEX, "7");
        preparedStatementMock.expects.executeUpdate().returns(1);

        JDBCChunkWriteHandler jdbcChunkWriteHandler =
                new JDBCChunkWriteHandler((PreparedStatement) preparedStatementMock, CHUNK_COLUMN_ID_INDEX, CHUNK_COLUMN_INDEX);

        jdbcChunkWriteHandler.writeChunk("12");
        jdbcChunkWriteHandler.writeChunk("34");
        jdbcChunkWriteHandler.writeChunk("56");
        jdbcChunkWriteHandler.writeChunk("7");
        int result = jdbcChunkWriteHandler.getRows();
        assertEquals(4, result);

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
