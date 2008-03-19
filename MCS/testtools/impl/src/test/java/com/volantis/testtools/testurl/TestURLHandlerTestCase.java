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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/marinerurl/HandlerTestCase.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 Test case for Handler
 * 12-May-03    Steve           VBM:2003021815 Added java doc to test cases 
 * 12-May-03    Steve           VBM:2003021815 Set package property 
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.testurl;

import java.io.*;
import java.net.*;
import junit.framework.*;

public class TestURLHandlerTestCase extends TestCase {
    
    public TestURLHandlerTestCase( String name ) {
        super( name );
    }
    
    /**
     * Check the creation of a URL
     * @throws Exception
     */
    public void testURLConstruction() throws Exception {
        TestURLRegistry.register();
        
        URL u = new URL( "testurl:foobar" );
        assertEquals( "testurl", u.getProtocol() );
        assertEquals( "foobar", u.getFile() );

        URL emptyURL = new URL( "testurl:" );
        assertEquals( "testurl", emptyURL.getProtocol() );
        assertEquals( "", emptyURL.getFile() );
    }

    /**
     * Test connecting to a valid URL
     * @throws Exception
     */
    public void testURLConnection() throws Exception {
        InputStream is = new ByteArrayInputStream( "".getBytes() );
        TestURLRegistry.register( "testconn", is );

        URL u = new URL( "testurl:testconn" );
        InputStream openedIS = u.openStream();
        assertSame( is, u.openStream() );
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 06-Aug-03	956/4	geoff	VBM:2003080601 finally fix bodgy marinerurlregistry

 ===========================================================================
*/
