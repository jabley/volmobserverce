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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.widgets.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for Map Service
 * 
 * Test assumes Google Maps implementation of Maps service 
 */
public class MapServiceTestCase extends TestCaseAbstract {

    StringWriter out;
    HttpServletRequestMock reqMock;
    HttpServletResponseMock responseMock;
    
    MapService testee = new MapService();
    
    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        
        
        out = new StringWriter();
        reqMock = new HttpServletRequestMock("reqMock", expectations);
        responseMock = new HttpServletResponseMock("responseMock", expectations); 
        
        reqMock.expects.getMethod().returns("GET");
        responseMock.fuzzy.setContentType(mockFactory.expectsInstanceOf(String.class));
        responseMock.fuzzy.setHeader(
                mockFactory.expectsInstanceOf(String.class),
                mockFactory.expectsInstanceOf(String.class));
        responseMock.expects.getWriter().returns(new PrintWriter(out));
                
    }

    public void testPanLeftMap() throws Exception {
        Map params = new HashMap();
        params.put("x", new String[] {"5"});
        params.put("y", new String[] {"10"});
        params.put("z", new String[] {"13"});
        params.put("d", new String[] {"l"});
        params = Collections.unmodifiableMap(params);        
        doTest("pan", params, "{ zoom : 13, imgList : [{x : 4, y : 8},{x : 3, y : 9},{x : 3, y : 10}," +
                "{x : 3, y : 11},{x : 4, y : 12}]}");        
    }
    
    public void testPanRightMap() throws Exception {
        Map params = new HashMap();
        params.put("x", new String[] {"5"});
        params.put("y", new String[] {"10"});
        params.put("z", new String[] {"13"});
        params.put("d", new String[] {"r"});
        params = Collections.unmodifiableMap(params);        
        doTest("pan", params, "{ zoom : 13, imgList : [{x : 6, y : 8},{x : 7, y : 9},{x : 7, y : 10},{x : 7, y : 11}," +
                "{x : 6, y : 12}]}" );        
    }

    public void testPanUpMap() throws Exception {
        Map params = new HashMap();
        params.put("x", new String[] {"5"});
        params.put("y", new String[] {"10"});
        params.put("z", new String[] {"13"});
        params.put("d", new String[] {"u"});
        params = Collections.unmodifiableMap(params);        
        doTest("pan", params, "{ zoom : 13, imgList : [{x : 4, y : 8},{x : 5, y : 8},{x : 6, y : 8},{x : 3, y : 9}," +
                "{x : 7, y : 9}]}");        
    }

    public void testPanDownMap() throws Exception {
        Map params = new HashMap();
        params.put("x", new String[] {"5"});
        params.put("y", new String[] {"10"});
        params.put("z", new String[] {"13"});
        params.put("d", new String[] {"b"});
        params = Collections.unmodifiableMap(params);        
        doTest("pan", params,"{ zoom : 13, imgList : [{x : 3, y : 11},{x : 7, y : 11}," +
                "{x : 4, y : 12},{x : 5, y : 12},{x : 6, y : 12}]}" );        
    }

    public void testZoomInMap() throws Exception {
        Map params = new HashMap();
        params.put("x", new String[] {"5"});
        params.put("y", new String[] {"10"});
        params.put("z", new String[] {"13"});
        params.put("d", new String[] {"i"});
        params = Collections.unmodifiableMap(params);        
        doTest("zoom", params, "{ zoom : 12, imgList : [{x : 9, y : 19},{x : 10, y : 19},{x : 11, y : 19}," +
                "{x : 9, y : 20},{x : 10, y : 20},{x : 11, y : 20},{x : 9, y : 21},{x : 10, y : 21}," +
                "{x : 11, y : 21}]}" );        
    }

    public void testZoomOutMap() throws Exception {
        Map params = new HashMap();
        params.put("x", new String[] {"5"});
        params.put("y", new String[] {"10"});
        params.put("z", new String[] {"13"});
        params.put("d", new String[] {"o"});
        params = Collections.unmodifiableMap(params);        
        doTest("zoom", params, "{ zoom : 14, imgList : [{x : 1, y : 4},{x : 2, y : 4},{x : 3, y : 4},{x : 1, y : 5}," +
                "{x : 2, y : 5},{x : 3, y : 5},{x : 1, y : 6},{x : 2, y : 6},{x : 3, y : 6}]}"  );        
    }

    public void testPanLeftPhoto() throws Exception {
        Map params = new HashMap();
        params.put("t", new String[] {"tqrst"});
        params.put("z", new String[] {"4"});
        params.put("d", new String[] {"l"});
        params.put("offx", new String[] {"0"});
        params.put("offy", new String[] {"0"});
        params = Collections.unmodifiableMap(params);        
        doTest("pan", params, "{ zoom : 4, imgList : " +
        		"[{t: 'tqrtr', offx: 2, offy: 1},{t: 'tqrtr', offx: 1, offy: 2}," +
        		"{t: 'tqrts', offx: 1, offy: 0},{t: 'tqrts', offx: 1, offy: 1}," +
        		"{t: 'tqrts', offx: 2, offy: 2}]}");        
    }
    
    public void testPanRightPhoto() throws Exception {
        Map params = new HashMap();
        params.put("t", new String[] {"tqrst"});
        params.put("z", new String[] {"4"});
        params.put("d", new String[] {"r"});
        params.put("offx", new String[] {"0"});
        params.put("offy", new String[] {"0"});
        params = Collections.unmodifiableMap(params);        
        doTest("pan", params, "{ zoom : 4, imgList : [" +
        		"{t: 'tqrsq', offx: 1, offy: 1},{t: 'tqrsq', offx: 2, offy: 2}," +
        		"{t: 'tqrst', offx: 2, offy: 0},{t: 'tqrst', offx: 2, offy: 1}," +
        		"{t: 'tqrst', offx: 1, offy: 2}]"
        );        
    }

    public void testPanUpPhoto() throws Exception {
        Map params = new HashMap();
        params.put("t", new String[] {"tqrst"});
        params.put("z", new String[] {"4"});
        params.put("d", new String[] {"u"});
        params.put("offx", new String[] {"0"});
        params.put("offy", new String[] {"0"});
        params = Collections.unmodifiableMap(params);        
        doTest("pan", params, "{ zoom : 4, " +
        		"imgList : [" +
        		"{t: 'tqrtr', offx: 2, offy: 1},{t: 'tqrsq', offx: 0, offy: 1}," +
        		"{t: 'tqrsq', offx: 1, offy: 1}," +
        		"{t: 'tqrtr', offx: 1, offy: 2},{t: 'tqrsq', offx: 2, offy: 2}]}");        
    }

    public void testPanDownPhoto() throws Exception {
        Map params = new HashMap();
        params.put("t", new String[] {"tqrst"});
        params.put("z", new String[] {"4"});
        params.put("d", new String[] {"b"});
        params.put("offx", new String[] {"0"});
        params.put("offy", new String[] {"0"});
        params = Collections.unmodifiableMap(params);        
        doTest("pan", params, "{ zoom : 4, imgList : [" +
        		"{t: 'tqrts', offx: 1, offy: 1},{t: 'tqrst', offx: 2, offy: 1}," +
        		"{t: 'tqrts', offx: 2, offy: 2},{t: 'tqrst', offx: 0, offy: 2}," +
        		"{t: 'tqrst', offx: 1, offy: 2}]}"
        );        
    }

    public void testZoomInPhoto() throws Exception {
        Map params = new HashMap();
        params.put("t", new String[] {"tqrst"});
        params.put("z", new String[] {"4"});
        params.put("d", new String[] {"i"});
        params.put("offx", new String[] {"0"});
        params.put("offy", new String[] {"0"});
        params = Collections.unmodifiableMap(params);        
        doTest("zoom", params, "{ zoom : 5, imgList : [" +
        		"{t: 'tqrtrs', offx: 2, offy: 2},{t: 'tqrsqt', offx: 0, offy: 2},{t: 'tqrsqt', offx: 1, offy: 2},"+
                "{t: 'tqrtsr', offx: 2, offy: 0},{t: 'tqrstq', offx: 0, offy: 0},{t: 'tqrstq', offx: 1, offy: 0},"+
                "{t: 'tqrtsr', offx: 2, offy: 1},{t: 'tqrstq', offx: 0, offy: 1},{t: 'tqrstq', offx: 1, offy: 1}"
        );        
    }

    public void testZoomOutPhoto() throws Exception {
        Map params = new HashMap();
        params.put("t", new String[] {"tqrst"});
        params.put("z", new String[] {"4"});
        params.put("d", new String[] {"o"});
        params.put("offx", new String[] {"0"});
        params.put("offy", new String[] {"0"});
        params = Collections.unmodifiableMap(params);  
        
        
        doTest("zoom", params, "{ " +
        		"zoom : 3, imgList : [" +
        		"{t: 'tqrt', offx: 2, offy: 0},{t: 'tqrs', offx: 0, offy: 0},{t: 'tqrs', offx: 1, offy: 0}," +
        		"{t: 'tqrt', offx: 2, offy: 1},{t: 'tqrs', offx: 0, offy: 1},{t: 'tqrs', offx: 1, offy: 1}," +
        		"{t: 'tqrt', offx: 2, offy: 2},{t: 'tqrs', offx: 0, offy: 2},{t: 'tqrs', offx: 1, offy: 2}" +
        		"]}");        
    }
    
    protected void doTest(String operation, Map params, String expected) throws ServletException, IOException {
        reqMock.expects.getPathInfo().returns(operation);
        reqMock.expects.getParameterMap().returns(params).any();

        testee.service(reqMock, responseMock);

        String result = out.toString();
        assertTrue("Expected '" + expected +"' not found in response:\n" + result,
                result.indexOf(expected) > -1);
    }                    
}
