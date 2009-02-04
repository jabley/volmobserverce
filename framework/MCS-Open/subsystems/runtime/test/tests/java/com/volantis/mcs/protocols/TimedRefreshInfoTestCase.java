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
 * (c) Copyright Volantis Systems Ltd. 2007.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.net.URISyntaxException;

public class TimedRefreshInfoTestCase extends TestCaseAbstract {

    public void testGetIntervalInSeconds() throws Exception {
        {
            TimedRefreshInfo tri = new TimedRefreshInfo(2.3f, "");
            assertEquals("should be rounded down", 2, tri.getIntervalInSeconds());
        }

        {
            TimedRefreshInfo tri = new TimedRefreshInfo(0.5f, "");
            assertEquals("should be rounded up", 1,
                         tri.getIntervalInSeconds());
        }

        try {
            TimedRefreshInfo tri = new TimedRefreshInfo(-0.5f, "");
            fail("should cause error");
        } catch (NumberFormatException nfe) {
            // success
        }
    }

    public void testGetIntervalInTenthsofSeconds() throws Exception {
        {
            TimedRefreshInfo tri = new TimedRefreshInfo(2.3f, "");
            assertEquals("should be rounded down", 23,
                         tri.getIntervalInTenthsOfSecond());
        }

        {
            TimedRefreshInfo tri = new TimedRefreshInfo(0.5f, "");
            assertEquals("should be rounded up", 5,
                         tri.getIntervalInTenthsOfSecond());
        }

        try {
            TimedRefreshInfo tri = new TimedRefreshInfo(-0.5f, "");
            fail("should cause error");
        } catch (NumberFormatException nfe) {
            // success
        }
    }

    public void testParse() throws Exception {

        {
            try {
                new TimedRefreshInfo("");
                fail("A NumberFormatException should have been thrown");
            }catch (NumberFormatException nfe) {
                // success
            } catch(ProtocolException e) {
                fail("A NumberFormatException should have been thrown");
            }
        }

        {
            TimedRefreshInfo tri = new TimedRefreshInfo("2.3");
            assertEquals("should be *10", 23,
                         tri.getIntervalInTenthsOfSecond());
        }

        try {

            TimedRefreshInfo tri = new TimedRefreshInfo("-2.7;");
            fail("should fail");
        } catch (NumberFormatException nfe) {
            // success
        }

        {
            TimedRefreshInfo tri = new TimedRefreshInfo("2.3;www.foo.bar");
            assertEquals("should be rounded down", 23,
                         tri.getIntervalInTenthsOfSecond());
        }

    }
    
    public void testAbsoluteURL() throws Exception {
        final MarinerPageContextMock pageContextMock
            = new MarinerPageContextMock("pageContextMock", expectations);
        
        String url = "http://www.bbc.co.uk";
        
        String requestPathURL = 
            "http://localhost:8080/volantis/test/test.xdime";
        
        pageContextMock.expects.getRequestURL(false)
                .returns(new MarinerURL(requestPathURL));
        
        TimedRefreshInfo tri = new TimedRefreshInfo(1.0f, url);
        String expected = "1;URL=" + url;
        
        String result = tri.getHTMLContent(pageContextMock);
        
        assertEquals(result, expected);
    }
    
    public void testRelativeURL() throws Exception {
        final MarinerPageContextMock pageContextMock
            = new MarinerPageContextMock("pageContextMock", expectations);
        
        String url = "../test2.xdime";
        
        String requestPathURL = 
            "http://localhost:8080/volantis/test/test.xdime";
        
        pageContextMock.expects.getRequestURL(false)
                .returns(new MarinerURL(requestPathURL));
        
        TimedRefreshInfo tri = new TimedRefreshInfo(1.0f, url);
        String expected = "1;URL=/volantis/test2.xdime";
        
        String result = tri.getHTMLContent(pageContextMock);
        
        assertEquals(result, expected);
    }
    
    public void testHostRelativeURL() throws Exception {
        final MarinerPageContextMock pageContextMock
            = new MarinerPageContextMock("pageContextMock", expectations);
        
        String url = "/volantis/test2.xdime";
        
        String requestPathURL = 
            "http://localhost:8080/volantis/test/dir/test.xdime";
        
        pageContextMock.expects.getRequestURL(false)
                .returns(new MarinerURL(requestPathURL));
        
        TimedRefreshInfo tri = new TimedRefreshInfo(1.0f, url);
        String expected = "1;URL=/volantis/test2.xdime";
        
        String result = tri.getHTMLContent(pageContextMock);
        
        assertEquals(result, expected);
    }

}
