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

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.Reader;

public class ChunkReaderTestCase extends TestCaseAbstract {

    public void testChunkReaderNoData()
            throws Exception {

        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        ChunkReadHandlerMock chunkReadHandlerMock =
                new ChunkReadHandlerMock("chunkReadHandler", expectations);

        char[] buffer;
        Reader chunkReader;

        // Test read with no data
        //
        // We expect readChunk to be called until we return null
        buffer = new char[10];

        chunkReadHandlerMock.expects.readChunk().returns(null);

        chunkReader = new ChunkedReader(chunkReadHandlerMock);
        int length = chunkReader.read(buffer);
        assertEquals(length, -1);

        // Test read again at end of stream
        int c = chunkReader.read();
        assertEquals(c, -1);


    }

    public void testChunkReaderReadEvenData()
            throws Exception {

        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        ChunkReadHandlerMock chunkReadHandlerMock =
                new ChunkReadHandlerMock("chunkReadHandler", expectations);

        // Test read with odd chunk
        //
        // We expect readChunk to be called until we return null
        char[] buffer = new char[10];

        chunkReadHandlerMock =
                new ChunkReadHandlerMock("chunkReadHandler", expectations);

        chunkReadHandlerMock.expects.readChunk().returns("12");
        chunkReadHandlerMock.expects.readChunk().returns("34");
        chunkReadHandlerMock.expects.readChunk().returns("56");
        chunkReadHandlerMock.expects.readChunk().returns("78");
        chunkReadHandlerMock.expects.readChunk().returns(null);

        Reader chunkReader = new ChunkedReader(chunkReadHandlerMock);

        int length = chunkReader.read(buffer);
        String result = new String(buffer, 0, length);
        assertEquals(length, 8);
        assertEquals(result, "12345678");

        // Test read at end  of stream
        //
        // We no not expect readChunk to be called and we expect a return of -1
        int c = chunkReader.read();
        assertEquals(c, -1);


    }

    public void testChunkReaderReadOddData()
            throws Exception {

        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        ChunkReadHandlerMock chunkReadHandlerMock =
                new ChunkReadHandlerMock("chunkReadHandler", expectations);

        // Test read with odd chunk
        //
        // We expect readChunk to be called until we return null
        char[] buffer = new char[10];

        chunkReadHandlerMock =
                new ChunkReadHandlerMock("chunkReadHandler", expectations);

        chunkReadHandlerMock.expects.readChunk().returns("12");
        chunkReadHandlerMock.expects.readChunk().returns("34");
        chunkReadHandlerMock.expects.readChunk().returns("56");
        chunkReadHandlerMock.expects.readChunk().returns("7");
        chunkReadHandlerMock.expects.readChunk().returns(null);

        Reader chunkReader = new ChunkedReader(chunkReadHandlerMock);

        int length = chunkReader.read(buffer);
        String result = new String(buffer, 0, length);
        assertEquals(length, 7);
        assertEquals(result, "1234567");

        // Test read at end  of stream
        //
        // We no not expect readChunk to be called and we expect a return of -1
        int c = chunkReader.read();
        assertEquals(c, -1);


    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 09-Jun-05	8346/5	ianw	VBM:2005051911 Fixed formating and expanded test case

 09-Jun-05	8346/3	ianw	VBM:2005051911 New JDBC Theme Accessor

 03-Jun-05	8346/1	ianw	VBM:2005051911 New JDBC Theme Accessor

 ===========================================================================
*/
