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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/io/BrokenInputStreamTestCase.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 Test case for BrokenInputStream
 *                              a stream that 'Breaks' after a set number of
 *                              bytes have been read.
 * 12-May-03    Steve           VBM:2003021815 Added java doc to test cases 
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.io;

import java.io.*;

import junit.framework.*;


public class BrokenInputStreamTestCase extends TestCase {
    public BrokenInputStreamTestCase( String name ) {
        super( name );
    }
    
    /**
     * Test that reading the correct amount of data from a broken
     * stream works without exceptions
     * @throws Exception
     */
    public void testFullRead() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                              "test data".getBytes() );
        BrokenInputStream bis = new BrokenInputStream( testIS, 11 );

        byte[] buf = new byte[ 16 ];
        assertEquals( 9, bis.read( buf ) );
        assertEquals( "test data", new String( buf, 0, 9 ) );
    }

    
    /**
     * Test that reading more bytes than allowed from a broken stream
     * results in an IO exception
     * @throws Exception
     */
    public void testNoRead() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                              "test data".getBytes() );
        BrokenInputStream bis = new BrokenInputStream( testIS, 0 );

        try {
            bis.read();
            fail( "Should have thrown IOException" );
        } catch( IOException ioex ) {
            // Expected
        }
    }

    /**
     * Test that reading up to the limit of a broken stream is OK but
     * a further read will fail.
     * @throws Exception
     */
    public void testPartialRead() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                              "test data".getBytes() );
        BrokenInputStream bis = new BrokenInputStream( testIS, 4 );
        byte[] buf4 = new byte[ 4 ];
        bis.read( buf4 );

        try {
            bis.read();
            fail( "Should have thrown IOException" );
        } catch( IOException ioex ) {
            // Expected
        }
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
