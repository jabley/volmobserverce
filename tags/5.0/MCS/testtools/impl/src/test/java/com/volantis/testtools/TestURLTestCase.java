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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/MarinerURLTestCase.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 Test case for MarinerURL protocol
 *                              handler.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools;

import com.volantis.testtools.io.ReplayInputStream;
import com.volantis.testtools.testurl.TestURLRegistry;
import java.io.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.net.*;
import java.net.URL;
import java.util.Date;
import junit.framework.*;

public class TestURLTestCase extends TestCase {

    public TestURLTestCase(String name) {
        super( name );
    }
    
    public void testURLConnection() throws Exception {
        InputStream dataIS = new ReplayInputStream( 
                      new ByteArrayInputStream( "My Test Data".getBytes()) );
        TestURLRegistry.register( "replay", dataIS);
        
        URL url = new URL( "testurl:replay" );
        InputStream in = url.openStream();

        assertSame( dataIS, in );
        
        byte[] dta = new byte[ 20 ];
        assertEquals( 12, in.read( dta ) );
        assertEquals( "My Test Data", new String( dta, 0, 12 ) );
        assertEquals( 12, in.read( dta ) );
        assertEquals( "My Test Data", new String( dta, 0, 12 ) );
    }
    
    public void testReadHTML() throws Exception {
        StringBuffer response = new StringBuffer();
        
        response.append( "HTTP/1.1 200 OK\n" )
                .append( "content-length: 44\n" )
                .append( "content-type: text/html\n" )
                .append( "content-encoding: MyEncoding\n" )
                .append( "expires: Mon, 05 Oct 1998 01:23:50 GMT\n" )
                .append( "date: Fri, 02 Oct 1998 13:27:15 GMT\n" )
                .append( "last-modified: Thu, 01 Oct 1998 18:00:32 GMT\n" )
                .append( "user-agent: My little browser.\n\n" )
                .append( "<html>\n<body>\n<p>Hello World</p>\n</body>\n</html>\n" );
        
        System.out.println( "length is " + response.length() );
        
        InputStream dataIS = new ReplayInputStream( 
                      new ByteArrayInputStream( response.toString().getBytes()) );
        TestURLRegistry.register( "replay", dataIS);
        
        URL url = new URL( "testurl:replay" );
        URLConnection conn = url.openConnection();
        assertEquals( "text/html", conn.getContentType() );
        assertEquals( 44, conn.getContentLength() );
        assertEquals( "MyEncoding", conn.getContentEncoding() );        
        assertEquals( Date.parse("Fri, 02 Oct 1998 13:27:15 GMT"), 
                      conn.getDate() );
        assertEquals( Date.parse("Mon, 05 Oct 1998 01:23:50 GMT"), 
                      conn.getExpiration() );
        assertEquals( Date.parse("Thu, 01 Oct 1998 18:00:32 GMT"), 
                      conn.getLastModified() );
        assertEquals( "My little browser.", conn.getHeaderField( "user-agent" ));
        
        BufferedReader rd = new BufferedReader( 
                                new InputStreamReader( conn.getInputStream() ) );
        
        assertEquals( "<html>", rd.readLine() );
        assertEquals( "<body>", rd.readLine() );
        assertEquals( "<p>Hello World</p>", rd.readLine() );
        assertEquals( "</body>", rd.readLine() );
        assertEquals( "</html>", rd.readLine() );
       
    }
        
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 06-Aug-03	921/6	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy - rework issue merge conflicts resolved

 06-Aug-03	956/3	geoff	VBM:2003080601 finally fix bodgy marinerurlregistry

 05-Aug-03	921/1	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 ===========================================================================
*/
