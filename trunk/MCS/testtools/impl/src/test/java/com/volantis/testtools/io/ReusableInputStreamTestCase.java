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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/io/ReusableInputStreamTestCase.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 Test case for ReusableInputStream
 *                              a stream that supports mark and reset to restart 
 *                              the stream content.
 * 12-May-03    Steve           VBM:2003021815 Added java doc to test cases 
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.io;

import java.io.*;

import junit.framework.*;


public class ReusableInputStreamTestCase extends TestCase {
    public ReusableInputStreamTestCase(String name) {
        super( name );
    }

    /**
     * Test that reading the input stream once is OK
     * @throws Exception
     */
    public void testSingleRead() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                                    "test data".getBytes() );
        
        ReusableInputStream in = new ReusableInputStream( testIS );

        byte[] buf = new byte[ 16 ];
        assertEquals( 9, in.read( buf ) );
        assertEquals( "test data", new String( buf, 0, 9 ) );
    }
    
    /**
     * Test that reading the stream multiple times returns the same contents each time
     * @throws Exception
     */
    public void testMultipleReads() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                                    "test data".getBytes() );
        ReusableInputStream in = new ReusableInputStream( testIS );

        byte[] buf = new byte[ 5 ];
        assertEquals( 5, in.read( buf ) );
        assertEquals( "test ", new String( buf, 0, 5 ) );
        assertEquals( 4, in.read( buf ) );
        assertEquals( "data", new String( buf, 0, 4 ) );
        assertEquals( -1, in.read() );
        
        in.reset();
        assertEquals( 5, in.read( buf ) );
        assertEquals( "test ", new String( buf, 0, 5 ) );
        
        in.mark( 10 );
        assertEquals( 4, in.read( buf ) );
        assertEquals( "data", new String( buf, 0, 4 ) );

        in.reset();
        assertEquals( 4, in.read( buf ) );
        assertEquals( "data", new String( buf, 0, 4 ) );
        
        assertEquals( -1, in.read() );
        
        in.reset();
        assertEquals( 5, in.read( buf ) );
        assertEquals( "test ", new String( buf, 0, 5 ) );
        assertEquals( 4, in.read( buf ) );
        assertEquals( "data", new String( buf, 0, 4 ) );
        assertEquals( -1, in.read() );
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
