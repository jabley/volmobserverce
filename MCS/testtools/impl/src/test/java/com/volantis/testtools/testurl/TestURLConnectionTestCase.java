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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/marinerurl/MarinerURLConnectionTestCase.java,v 1.2 2003/03/03 11:15:00 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 Test case for MarinerURLConnection
 * 03-Mar-03    Steve           VBM:2003021815 Changed unsupported test to catch
 *                              any exception for getContent() as it appears that
 *                              UnknownServiceException is not always thrown by 
 *                              every JVM.
 * 12-May-03    Steve           VBM:2003021815 Added java doc to test cases
 *                              Register handler with setProperty() 
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.testurl;

import java.io.*;

import java.net.*;

import junit.framework.*;

public class TestURLConnectionTestCase extends TestCase {

    static {
        // surefire class loading workaround see surefire vs
        // "java.protocol.handler.pkgs"
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {

            public URLStreamHandler createURLStreamHandler(String protocol) {
                if ("testurl".equals(protocol)) {
                    return new Handler();
                }
                return null;
            }
        });
    }
    
    public TestURLConnectionTestCase( String name ) {
        super( name );
    }

    /**
     * Test acquiring an input stream from a valid URL 
     * @throws Exception
     */
    public void testGetInputStream() throws Exception {
        InputStream is = new ByteArrayInputStream( "xyzzy".getBytes() );
        TestURLRegistry.register( "xyzzy", is );

        URLConnection uc = new TestURLConnection( new URL( "testurl:xyzzy" ) );
        assertNull( uc.getInputStream() );
        uc.connect();
        assertSame( is, uc.getInputStream() );
    }

    /**
     * Test that an error on connect URL can be created but does not error
     * if it is not accessed.
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        TestURLRegistry.register();
        URL url = new URL( "testurl:errorOnConnect" );
        new TestURLConnection( url ); // shouldn't throw.
    }

    /**
     * Test that an exception is thrown if an error URL is created and 
     * we try to access it.
     * @throws Exception
     */
    public void testErrorOnConnect() throws Exception {
        TestURLRegistry.register();
        URL url = new URL( "testurl:errorOnConnect" );

        try {
            url.openStream();
            fail( "Should have thrown exception" );
        } catch( IOException ioex ) {
            assertTrue( ioex.getMessage().indexOf( "Simulated" ) > -1 );
        }
    }        


    /**
     * Test that exceptions are thrown if we try to access a URL with no content.
     * @throws Exception
     */
    public void testUnsupportedFeatures() throws Exception {
        TestURLRegistry.register( "unsupp", 
                                  new ByteArrayInputStream( "".getBytes() ) );

        URL url = new URL( "testurl:unsupp" );
        URLConnection uc = url.openConnection();

        try {
            uc.getOutputStream();
            fail( "Should have thrown exception" );
        } catch( UnknownServiceException use ) {
        }

        try {
            uc.getContent();
            fail( "Should have thrown exception" );
        } catch( Exception e ) {
        }

        assertNull( uc.getHeaderField( 0 ) );
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
