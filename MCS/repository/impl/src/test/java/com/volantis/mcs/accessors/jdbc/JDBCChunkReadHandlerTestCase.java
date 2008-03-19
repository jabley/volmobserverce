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
import mock.java.sql.ResultSetMock;

import java.sql.ResultSet;


public class JDBCChunkReadHandlerTestCase extends TestCaseAbstract {

    private static final int CHUNK_INDEX = 2;

    public void testJDBCChunkReadHandler()
            throws Exception {

        // Test readChunk
        //
        // We expect to receive 4 chunks, "12", "34", "56" and "7"
        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();
        ResultSetMock resultSetMock = new ResultSetMock("ResultSet", expectations);

        resultSetMock.expects.next().returns(true);
        resultSetMock.expects.getString(CHUNK_INDEX).returns("12");
        resultSetMock.expects.next().returns(true);
        resultSetMock.expects.getString(CHUNK_INDEX).returns("34");
        resultSetMock.expects.next().returns(true);
        resultSetMock.expects.getString(CHUNK_INDEX).returns("56");
        resultSetMock.expects.next().returns(true);
        resultSetMock.expects.getString(CHUNK_INDEX).returns("7");
        resultSetMock.expects.next().returns(false);

        JDBCChunkReadHandler jdbcChunkReadHandler = new JDBCChunkReadHandler((ResultSet)resultSetMock,CHUNK_INDEX);

        assertEquals("Chunk 0 doesn't match expected", "12", jdbcChunkReadHandler.readChunk());
        assertEquals("Chunk 1 doesn't match expected", "34", jdbcChunkReadHandler.readChunk());
        assertEquals("Chunk 2 doesn't match expected", "56", jdbcChunkReadHandler.readChunk());
        assertEquals("Chunk 3 doesn't match expected", "7", jdbcChunkReadHandler.readChunk());
        assertEquals("Chunk 4 doesn't match expected", null, jdbcChunkReadHandler.readChunk());
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
