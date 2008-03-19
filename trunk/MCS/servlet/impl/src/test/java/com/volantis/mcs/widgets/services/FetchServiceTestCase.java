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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.widgets.services;

import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;   

/**
 * The test case for FetchServlet
 */
public class FetchServiceTestCase extends TestCaseAbstract {

    StringWriter out;
    HttpServletRequestMock reqMock;
    HttpServletResponseMock responseMock;
    
    FetchService testee = new FetchService();
    
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
    
    /**
     * Tests Get servlet method returning pipeline xml with transformation.
     */
    public void testGetWithTransformation() throws Exception {
        
        Map params = new HashMap();
        params.put("src", new String[] {"http%3A//xml.weather.yahoo.com/forecastrss%3Fp%3DUKXX1727%26u%3Dc"});
        params.put("trans", new String[] {"file%3A/home/wbondyra/opt/apache-tomcat-5.5.20/webapps/volantis/projects/client-app/weather-presenter.xsl"});       
        params = Collections.unmodifiableMap(params);   
        
        // Create expected response        
        String expectedResponseString = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<pipeline:transform" + 
                        " xmlns:pipeline=\"http://www.volantis.com/xmlns/marlin-pipeline\""+
                        " xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\""+
                        " compile=\"true\" cache=\"true\">" + 
                    "<pipeline:transformation compilable=\"true\" href=\"file%3A/home/wbondyra/opt/apache-tomcat-5.5.20/webapps/volantis/projects/client-app/weather-presenter.xsl\"/>" + 
                    "<urid:fetch href=\"http%3A//xml.weather.yahoo.com/forecastrss%3Fp%3DUKXX1727%26u%3Dc\" parse=\"xml\"/>" + 
                "</pipeline:transform>"; 
                
        
        reqMock.expects.getParameterMap().returns(params).any();        
                
        testee.service(reqMock, responseMock);
        assertEquals(expectedResponseString, out.toString());
    }
    
    /**
     * Tests Get servlet method returning only urid:fetch element.
     */
    public void testGetOnlyFetch() throws Exception {
        
        Map params = new HashMap();
        params.put("src", new String[] {"http%3A//xml.weather.yahoo.com/forecastrss%3Fp%3DUKXX1727%26u%3Dc"});
        params = Collections.unmodifiableMap(params);   
        
        // Create expected response        
        String expectedResponseString = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
             "<urid:fetch xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\" href=\"http%3A//xml.weather.yahoo.com/forecastrss%3Fp%3DUKXX1727%26u%3Dc\"/>";                 
        
        reqMock.expects.getParameterMap().returns(params).any();        
                
        testee.service(reqMock, responseMock);
        assertEquals(expectedResponseString, out.toString());
    }
}
