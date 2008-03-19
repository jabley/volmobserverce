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

import java.io.IOException;
import java.io.Writer;



public class ChunkWriterTestCase extends TestCaseAbstract {

    public void testChunkWriter()
            throws Exception {

        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        ChunkWriteHandlerMock chunkWriteHandlerMock = new ChunkWriteHandlerMock("chunkWriteHandler",expectations);


        // Test write
        //
        // We expect writeChunk to be called with 4 chunks, "12", "34", "56" and "7"
        chunkWriteHandlerMock.expects.writeChunk("12");
        chunkWriteHandlerMock.expects.writeChunk("34");
        chunkWriteHandlerMock.expects.writeChunk("56");
        chunkWriteHandlerMock.expects.writeChunk("7");

        Writer chunkWriter = new ChunkedWriter(chunkWriteHandlerMock, 2);

        chunkWriter.write("1234567");
        chunkWriter.close();

        // Test negative chunk size
        //
        // We should fail with an IOException
        String message = null;
        try {
            chunkWriter = new ChunkedWriter(null,-1);
        } catch(IOException ioe) {
            message = ioe.getMessage();

        }
        assertEquals(message,"Invalid chunksize.");

        // Test zero chunk size
        //
        // We should fail with an IOException
        message = null;
        try {
            chunkWriter = new ChunkedWriter(null,0);
        } catch(IOException ioe) {
            message = ioe.getMessage();

        }
        assertEquals(message,"Invalid chunksize.");

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
