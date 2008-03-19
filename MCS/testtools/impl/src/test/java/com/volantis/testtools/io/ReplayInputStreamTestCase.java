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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/io/ReplayInputStreamTestCase.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 Test case for ReplayInputStream
 *                              a stream that wraps when output is completed.
 * 12-May-03    Steve           VBM:2003021815 Added java doc to test cases 
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.io;

import java.io.*;

import junit.framework.*;


public class ReplayInputStreamTestCase extends TestCase {
    public ReplayInputStreamTestCase(String name) {
        super( name );
    }

    /**
     * Test that reading the input stream once is OK
     * @throws Exception
     */
    public void testSingleRead() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                                    "test data".getBytes() );
        ReplayInputStream in = new ReplayInputStream( testIS );

        byte[] buf = new byte[ 16 ];
        assertEquals( 9, in.read( buf ) );
        assertEquals( "test data", new String( buf, 0, 9 ) );
    }
    
    /**
     * Test that reading the stream 5 times returns the same contents each time
     * @throws Exception
     */
    public void testMultipleReads() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                                    "test data".getBytes() );
        ReplayInputStream in = new ReplayInputStream( testIS );

        byte[] buf = new byte[ 16 ];
        assertEquals( 9, in.read( buf ) );
        assertEquals( "test data", new String( buf, 0, 9 ) );
        assertEquals( 9, in.read( buf ) );
        assertEquals( "test data", new String( buf, 0, 9 ) );
        assertEquals( 9, in.read( buf ) );
        assertEquals( "test data", new String( buf, 0, 9 ) );
        assertEquals( 9, in.read( buf ) );
        assertEquals( "test data", new String( buf, 0, 9 ) );
        assertEquals( 9, in.read( buf ) );
        assertEquals( "test data", new String( buf, 0, 9 ) );
    }
    

    /**
     * Check that we can muck about with reads, marks and resets and always
     * get the right result.
     * @throws Exception
     */
    public void testPartialRead() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                              "0123456789".getBytes() );
        ReplayInputStream in = new ReplayInputStream( testIS );

        byte[] buf = new byte[ 4 ];
        
        // Read the first four bytes
        assertEquals( 4, in.read( buf ) );
        assertEquals( "0123", new String( buf, 0, 4 ) );
        
        // Mark the current position and read the next 4 bytes
        in.mark( 10 );
        assertEquals( 4, in.read( buf ) );
        assertEquals( "4567", new String( buf, 0, 4 ) );
        
        // Reset to the marked position and read the same 4 bytes
        in.reset();
        assertEquals( 4, in.read( buf ) );
        assertEquals( "4567", new String( buf, 0, 4 ) );
        
        // Read the last 2 bytes
        assertEquals( 2, in.read( buf ) );
        assertEquals( "89", new String( buf, 0, 2 ) );

        // Read the 10 again to make sure it is still wrapping.
        assertEquals( 4, in.read( buf ) );
        assertEquals( "0123", new String( buf, 0, 4 ) );
        assertEquals( 4, in.read( buf ) );
        assertEquals( "4567", new String( buf, 0, 4 ) );
        assertEquals( 2, in.read( buf ) );
        assertEquals( "89", new String( buf, 0, 2 ) );
    }
        
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
