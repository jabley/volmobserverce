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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/io/TimeoutInputStreamTestCase.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 Test case for TimeoutInputStream
 *                              a stream that times out whenever a read is tried
 * 12-May-03    Steve           VBM:2003021815 Added java doc to test cases 
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.io;

import java.io.*;

import junit.framework.*;


public class TimeoutInputStreamTestCase extends TestCase {
    public TimeoutInputStreamTestCase(String name) {
        super( name );
    }
    
    /**
     * Check that stream dies instantly if no timeout is set
     * @throws Exception
     */
    public void testInstantFailure() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                              "test data".getBytes() );
        TimeoutInputStream in = new TimeoutInputStream( testIS );

        try {
            in.read();
            fail( "IOException expected." );
        }
        catch( IOException ioe ) {
            // Expected
        }
    }

    /**
     * Check that stream will wait and time out if parameter is supplied
     * @throws Exception
     */
    public void testDelayedFailure() throws Exception {
        ByteArrayInputStream testIS = new ByteArrayInputStream( 
                                              "test data".getBytes() );
        TimeoutInputStream in = new TimeoutInputStream( testIS, 5000 );

        long then = 0;
        long now = 0;
        
        try {
            then = System.currentTimeMillis();
            in.read();
            fail( "IOException expected." );
        }
        catch( IOException ioe ) {
            now = System.currentTimeMillis();
            if( now - then < 5000 ){
                fail( "Test failed in less than 5000 milliseconds." );
            }
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
