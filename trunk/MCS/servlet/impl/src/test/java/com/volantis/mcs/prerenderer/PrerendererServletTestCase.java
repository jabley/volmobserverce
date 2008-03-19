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
package com.volantis.mcs.prerenderer;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mock.javax.servlet.ServletConfigMock;
import mock.javax.servlet.ServletContextMock;
import mock.javax.servlet.ServletHelper;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;
import mock.javax.servlet.http.HttpSessionMock;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.volantis.mcs.application.PrerendererApplicationMock;
import com.volantis.mcs.context.PrerendererPackageContextMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * The test case for PrerendererServlet
 */
public class PrerendererServletTestCase extends TestCaseAbstract {

    private ServletConfigMock servletConfigMock;

    private ServletContextMock servletContextMock;

    private HttpServletRequestMock httpServletRequestMock;

    private HttpServletResponseMock httpServletResponseMock;

    private HttpSessionMock httpSessionMock;

    private PrerendererPackageContextMock prerendererPackageContextMock;
    
    private PrerendererApplicationMock prerendererApplicationMock;
    
    private PrerendererServlet prerendererServlet;

    /**
     * @inheritDoc
     */
    public void setUp() throws Exception {
        /*
         * Create mocks
         */
        expectations =
            mockFactory.createUnorderedBuilder();

        servletConfigMock =
            new ServletConfigMock("servletConfigMock", expectations);

        servletContextMock =
            new ServletContextMock("servletContextMock", expectations);

        httpServletRequestMock =
            new HttpServletRequestMock("httpServletRequestMock",
                    expectations);
         
        httpSessionMock = 
            new HttpSessionMock("httpSessionMock", expectations);

        httpServletResponseMock =
            new HttpServletResponseMock("httpServletResponseMock",
                    expectations);
         
        prerendererApplicationMock = 
            new PrerendererApplicationMock("prerendererApplicationMock",
                    expectations);

        // Setup a usable servlet environment using mocks.
        ServletHelper.setExpectedResults(expectations,
                servletConfigMock,
                servletContextMock,
                PrerendererServlet.class);

        prerendererServlet = new PrerendererServlet();

        prerendererServlet.setPrerendererApplication(prerendererApplicationMock);
        
        prerendererServlet.skipApplicationRegistration();
        
        prerendererServlet.init(servletConfigMock);
    }

    /**
     * Tests POST servlet method. 
     */
    public void testPost() throws Exception {
        /*
         * Initialize data
         */
        
        // Create request string.
        String requestString = 
            "<pages base=\"http://www.google.com/gmail/\" prefix-path=\"/dev/gmail/\">" +
                "<page>login.xdime</page>" + 
                "<page>help/index.xdime</page>" + 
                "<page>welcome.xdime</page>" + 
            "</pages>";

        // Create request input stream from the request string.
        Reader requestReader = new BufferedReader(new StringReader(requestString)); 

        // Create expected parameters for PrerendererPackageContext
        List pageURIs = new ArrayList();
        pageURIs.add(new URI("login.xdime"));
        pageURIs.add(new URI("help/index.xdime"));
        pageURIs.add(new URI("welcome.xdime"));
        
        URI baseURI = new URI("http://www.google.com/gmail/");
        URI prefixPathURI = new URI("/dev/gmail/");

        // Create expected list of rewritten page URIs.
        List rewrittenPageURIs = new ArrayList();
        rewrittenPageURIs.add(new URI("index.html"));
        rewrittenPageURIs.add(new URI("1.html"));
        rewrittenPageURIs.add(new URI("2.html"));
        
        // Create expected response
        String expectedResponseString = 
            "<pages>" +
                "<page>index.html</page>" + 
                "<page>1.html</page>" + 
                "<page>2.html</page>" + 
            "</pages>";
        
        /*
         * Create mocks
         */
        prerendererPackageContextMock =
            new PrerendererPackageContextMock("prerendererPackageContextMock",
                    expectations, 
                    pageURIs, baseURI, prefixPathURI);

        /*
         * Setup expectations
         */
        prerendererApplicationMock.fuzzy
            .createPrerendererPackageContext(
                    mockFactory.expectsToStringOf(pageURIs.toString()),
                    mockFactory.expectsToStringOf(baseURI.toString()),
                    mockFactory.expectsToStringOf(prefixPathURI.toString()))
            .returns(prerendererPackageContextMock);
    
        httpSessionMock.expects
            .getAttribute(PrerendererServlet.PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME)
            .returns(null);
        
        httpSessionMock.expects
            .setAttribute(PrerendererServlet.PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME,
                    prerendererPackageContextMock);

        // Create a writer for the HttpServletResponse to use
        StringWriter responseStringWriter = new StringWriter();

        PrintWriter responsePrintWriter = new PrintWriter(responseStringWriter);
        
        httpServletRequestMock
                .expects
                .getReader()
                .returns(requestReader);

        httpServletRequestMock.expects
                 .getSession()
                 .returns(httpSessionMock);

        httpServletResponseMock.expects
                .getWriter()
                .returns(responsePrintWriter);
        
        prerendererPackageContextMock.expects
                .getRewrittenPageURIs()
                .returns(rewrittenPageURIs);

        prerendererServlet.doPost(httpServletRequestMock, httpServletResponseMock);

        assertXMLEquals(expectedResponseString, responseStringWriter.toString());
    }
    
    /**
     * Tests Get servlet method returning non-empty resources list.
     */
    public void testNonEmptyGet() throws Exception {
        /*
         * Initialize data
         */
        
        // Create parameters for PrerendererPackageContextMock constructor.
        List pageURIs = new ArrayList();
        URI baseURI = new URI("http://www.google.com/");
        URI prefixPathURI = new URI("/");

        // Create expected list of rewritten page URIs.
        // The TreeMap guarantees ordering of URIs, when retrieving.
        TreeMap incrementalRewrittenURIMap = new TreeMap();
        incrementalRewrittenURIMap.put(new URI("a"), new URI("aa"));
        incrementalRewrittenURIMap.put(new URI("b"), new URI("bb"));
        incrementalRewrittenURIMap.put(new URI("c"), new URI("cc"));
        
        // Create expected response
        String expectedResponseString = 
            "<resources>" +
                "<resource>" + 
                    "<remote>a</remote>" + 
                    "<local>aa</local>" + 
                "</resource>" + 
                "<resource>" + 
                    "<remote>b</remote>" + 
                    "<local>bb</local>" + 
                "</resource>" + 
                "<resource>" + 
                    "<remote>c</remote>" + 
                    "<local>cc</local>" + 
                "</resource>" + 
            "</resources>";
        
        /*
         * Create mocks
         */
        prerendererPackageContextMock =
            new PrerendererPackageContextMock("prerendererPackageContextMock",
                    expectations,
                    pageURIs, baseURI, prefixPathURI);

        /*
         * Setup expectations
         */
        httpSessionMock.expects
            .getAttribute(PrerendererServlet.PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME)
            .returns(prerendererPackageContextMock)
            .any();
        
        // Create a writer for the HttpServletResponse to use
        StringWriter responseStringWriter = new StringWriter();

        PrintWriter responsePrintWriter = new PrintWriter(responseStringWriter);
        
        httpServletRequestMock.expects
                .getSession()
                .returns(httpSessionMock);

        httpServletResponseMock.expects
                .getWriter()
                .returns(responsePrintWriter);
        
        prerendererPackageContextMock.expects
                .getIncrementalRewrittenURIMap()
                .returns(incrementalRewrittenURIMap);

        prerendererServlet.doGet(httpServletRequestMock, httpServletResponseMock);

        assertXMLEquals(expectedResponseString, responseStringWriter.toString());
    }

    /**
     * Tests Get servlet method returning empty resource list.
     */
    public void testEmptyGet() throws Exception {
        /*
         * Initialize data
         */
        
        // Create parameters for PrerendererPackageContextMock constructor.
        List pageURIs = new ArrayList();
        URI baseURI = new URI("http://www.google.com/");
        URI prefixPathURI = new URI("/");

        // Create expected list of rewritten page URIs.
        // The TreeMap guarantees ordering of URIs, when retrieving.
        Map incrementalRewrittenURIMap = new HashMap();
        
        // Create expected response
        String expectedResponseString = 
            "<resources/>";
        
        /*
         * Create mocks
         */
        prerendererPackageContextMock =
            new PrerendererPackageContextMock("prerendererPackageContextMock",
                    expectations, 
                    pageURIs, baseURI, prefixPathURI);

        /*
         * Setup expectations
         */
        httpSessionMock.expects
            .getAttribute(PrerendererServlet.PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME)
            .returns(prerendererPackageContextMock)
            .any();

        httpSessionMock.expects
            .removeAttribute(PrerendererServlet.PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME);
        
        httpSessionMock.expects.invalidate();
        
        // Create a writer for the HttpServletResponse to use
        StringWriter responseStringWriter = new StringWriter();

        PrintWriter responsePrintWriter = new PrintWriter(responseStringWriter);
        
        httpServletRequestMock.expects
                .getSession()
                .returns(httpSessionMock);

        httpServletResponseMock.expects
                .getWriter()
                .returns(responsePrintWriter);
        
        prerendererPackageContextMock.expects
                .getIncrementalRewrittenURIMap()
                .returns(incrementalRewrittenURIMap);

        prerendererServlet.doGet(httpServletRequestMock, httpServletResponseMock);

        assertXMLEquals(expectedResponseString, responseStringWriter.toString());
    }

    /**
     * Asserts semantical equality of two XML strings.
     * 
     * @param expectedXML The expected XMl string
     * @param actualXML The actual XML string
     * @throws Exception
     */
    private void assertXMLEquals(String expectedXML, String actualXML) throws Exception {
        SAXBuilder builder = new SAXBuilder();

        Document expectedDocument = builder.build(new StringReader(expectedXML));
        
        Document actualDocument = builder.build(new StringReader(actualXML));
        
        XMLOutputter outputter = new XMLOutputter();

        String expectedNormalizedXML = outputter.outputString(expectedDocument.getRootElement());
        
        String actualNormalizedXML = outputter.outputString(actualDocument.getRootElement());
        
        assertEquals(expectedNormalizedXML, actualNormalizedXML);
    }
}
