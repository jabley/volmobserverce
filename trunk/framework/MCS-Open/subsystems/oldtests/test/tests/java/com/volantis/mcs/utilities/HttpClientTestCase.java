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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/utilities/HttpClientTestCase.java,v 1.6 2003/02/27 11:47:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Nov-02    steve           VBM:2002040812 - Test case for HttpClient
 * 10-Dec-02    Steve           VBM:2002040812 - call shutdown method to kill server
 * 14-Jan-03    Byron           VBM:2003010910 - Modified tests so that each
 *                              server starts on a different port. Ensure setUp
 *                              waits long enough for server to be 'alive'.
 *                              Removed System.out's. Modified testTimeout to
 *                              check for 1 sec timeout.
 * 29-Jan-03    Byron           VBM:2003010910 - Modified testTimeout to
 *                              check that the timeout is > TIMEOUT.
 * 10-Feb-03    Steve           VBM:2002112504 - Because the 1 second timeout 
 *                              loop has been removed from NetworkClient, there
 *                              is no need to wait for 1 second after killing 
 *                              a server for it to die. Reduced the time to 1/4 
 *                              of a second  which should be plenty of time for 
 *                              the server thread to die.
 * 18-Feb-03    Geoff           VBM:2002102103 - Change the port number from
 *                              9000 to 9030 to avoid problem where it was
 *                              conflicting with DDMProxyServerTestCase when
 *                              tests run in a single VM.
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.utilities;

import com.volantis.mcs.utilities.HttpClient;
import com.volantis.testtools.server.HTMLServer;
import java.io.BufferedReader;
import java.util.Vector;
import junit.framework.*;

public class HttpClientTestCase extends TestCase
{
    
    private HTMLServer server;
    private Vector headers;
    private StringBuffer expectedHTML;
    // Change this at your own risk ... {Insert evil laugh here...}
    private static int port = 9030; 

    public HttpClientTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    /**
    * Set up a server to talk to.
    */
    public void setUp() {
        // Start the server on a new port for each test (faster than waiting
        // for the server to shutdown before another test may continue.
        server = new HTMLServer( port++ );
        server.start();

        headers = new Vector();
        headers.add( "header1: value1" );
        headers.add( "header2: value2" );
        headers.add( "header3: value3" );
        
        expectedHTML = new StringBuffer();
        expectedHTML.append( "<!DOCTYPE HTML PUBLIC " )
                    .append( "\"-//W3C//DTD HTML 4.0 Transitional//EN\">" )
                    .append( "<HTML><HEAD>  <TITLE>HTMLServer Results</TITLE>" )
                    .append( "</HEAD>" )
                    .append( "<BODY BGCOLOR=\"#FDF5E6\">" )
                    .append( "<H1 ALIGN=\"CENTER\">HTMLServer Results</H1>" )
                    .append( "Here are the request line and request headers " )
                    .append( "you sent<PRE>GET /volantis/wibble/file.html " )
                    .append( "HTTP/1.0header1: value1header2: value2" )
                    .append( "header3: value3</PRE></BODY></HTML>" );

        // Ensure that the server has enough time to start up and start
        // listening to a port before a tests attempts to connect to the port.
        try {
            int delay = 0;
            final int step = 25;
            while (!server.isActive() && (delay < 250)) {
                delay += step;
                Thread.currentThread().sleep(step);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Stop the server
    */
    public void tearDown() {
        server.shutdown();

    }

    /** Test creation of an HttpClient and call it by setting parameters */
    public void testSetByMethod() {
        try {
            HttpClient client = new HttpClient();
            client.setHost( "localhost" );
            client.setPort( server.getPort() );
            client.setUri( "/volantis/wibble/file.html" );
            client.setRequestHeaders( headers );
            client.connect();

            StringBuffer response = new StringBuffer();
            BufferedReader resp = client.getBufferedReader();
            String line;
            while( ( line = resp.readLine() ) != null ) {
                response.append( line );
            }
            assertEquals( expectedHTML.toString(), response.toString() );
        }
        catch( Exception e ) {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }
    
    public void testSetByParameters() {
        try {
            HttpClient client = new HttpClient( "localhost", server.getPort(),
                                "/volantis/wibble/file.html", headers );

            StringBuffer response = new StringBuffer();
            BufferedReader resp = client.getBufferedReader();
            String line;
            while( ( line = resp.readLine() ) != null ) {
                response.append( line );
            }
            assertEquals( expectedHTML.toString(), response.toString() );        
        }
        catch( Exception e ) {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }
    
    public void testSetByURL() {
        try {
            HttpClient client = new HttpClient( 
                    "http://localhost:" + server.getPort() +
                    "/volantis/wibble/file.html", headers );

            StringBuffer response = new StringBuffer();
            BufferedReader resp = client.getBufferedReader();
            String line;
            while( ( line = resp.readLine() ) != null ) {
                response.append( line );
            }
            assertEquals( expectedHTML.toString(), response.toString() );        
        }
        catch( Exception e ) {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }
    
    public void testTimeout() {
        try {

            final int MY_TIMEOUT = 100;

            HttpClient client = new HttpClient();
            client.setHost("localhost");
            client.setPort(server.getPort());
            client.setUri("/volantis/wibble/file.html");
            client.setRequestHeaders(headers);

            // Keep the server connection busy whilst connecting. This number
            // is should be greater than 2 seconds. Note that this delay is not
            // enforced because the server will shutdown within 1 second.
            server.setDelay(5000);
            client.connect(MY_TIMEOUT);

            // Shutdown the server and wait for 1/4 sec to guarentee it has
            // shut down properly.
            server.shutdown();
            Thread.currentThread().sleep(250);

            long now = System.currentTimeMillis();

            // Now attempt to read from the server. The request should timeout
            // with the specified timeout value.
            try {
                StringBuffer response = new StringBuffer();
                BufferedReader resp = client.getBufferedReader();
                String line;
                while ((line = resp.readLine()) != null) {
                    response.append(line);
                }
                assertEquals(expectedHTML.toString(), response.toString());
                fail("Should've timed out.");
            } catch (Exception e) {
                // We should have had an excpetion here, but ignore it.
            }
            // The timeout should be equal to the elapsed time (give or take
            // a few milliseconds).
            long delay = System.currentTimeMillis() - now;
            // the "10" is to allows a small region for variation
            assertTrue("Elapsed time must be greater than the timeout: " + delay,
                       delay + 10 >= MY_TIMEOUT);
//            System.out.println("Delay is " + delay);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
