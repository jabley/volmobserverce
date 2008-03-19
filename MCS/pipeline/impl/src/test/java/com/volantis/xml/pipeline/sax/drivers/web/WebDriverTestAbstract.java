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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug           VBM:2002121803 - Created. Base class for
 *                             integration tests that wish to test the
 *                             transform process.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HttpServerMock;
import com.volantis.shared.net.http.WaitTransaction;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.cookies.CookieIdentity;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.headers.HeaderImpl;
import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.shared.net.http.parameters.RequestParameterImpl;
import com.volantis.synergetics.url.URLPrefixRewriteManager;
import com.volantis.synergetics.url.URLPrefixRewriteOperation;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.NamespaceContentWriter;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.conditioners.ContentConditioner;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.convert.URLRewriteProcessConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.conditioners.XMLResponseConditioner;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test case for the web driver process.
 */
public abstract class WebDriverTestAbstract
        extends PipelineTestAbstract {

    /**
     * Factory for creating pipeline objects.
     */
    protected XMLPipelineFactory pipelineFactory;

    private List servers;

    private int inputServerPort;
    private int outputServerPort;

    protected HttpServerMock serverMock;

    /*
     * (non-javadoc)
     * Deliberately comment this out so that the logging is not used for
     * this test (and subsequent test that get run in a single VM).
     * Given that PipelineTestAbstract does not extend TestCaseAbstract (and it
     * can't), it is easier to manually switch on debugging when required
     * by un-commenting this code:
     */
    static {
    }

    // javadoc inherited
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        Category.getRoot().setLevel(Level.DEBUG);
        pipelineFactory = new IntegrationTestHelper().getPipelineFactory();

        servers = new ArrayList();

        serverMock = createServerMock();
        inputServerPort = serverMock.getServerPort();
        outputServerPort = inputServerPort;
    }

    private HttpServerMock createServerMock() throws IOException {
        HttpServerMock serverMock = new HttpServerMock();
        servers.add(serverMock);
        return serverMock;
    }

    // javadoc inherited from superclass
    protected void tearDown() throws Exception {
        Category.getRoot().removeAllAppenders();
        for (int i = 0; i < servers.size(); i++) {
            HttpServerMock httpServerMock = (HttpServerMock) servers.get(i);
            httpServerMock.close();
        }
        inputServerPort = -1;
        outputServerPort = -1;
    }

    /**
     * Override this method in order to preserve the namespace markup in the
     * generated output.
     *
     * @param  writer the writer.
     * @return        a new DefaultHandler that includes the namespace values
     *                in the output.
     */
    protected ContentHandler createContentHandler(Writer writer) {

        return new NamespaceContentWriter(writer) {

            protected void writeAttributes(Attributes attrs)
                    throws SAXException {
                if (attrs != null) {
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String aName = attrs.getLocalName(i);
                        if ("".equals(aName) || (attrs.getQName(i) != null)) {
                            aName = attrs.getQName(i);
                        }
                        write(" " + aName + "=\"" + attrs.getValue(i) + "\"");
                    }
                }
            }
        };
    }

    /**
     * Test a very simple get request
     *
     * @throws Exception if an error occurs.
     */
    public void testSimpleGet() throws Exception {
            String[] responseContent = new String[]{
                "HTTP/1.0 200 OK",
                "Date: Fri, 31 Dec 1999 23:59:59 GMT",
                "Content-Type: text/plain",
                "",
                "<p>A Get</p>"
            };

        serverMock.addTransaction(null, responseContent);

            doTest(pipelineFactory,
                   "SimpleGetTestCase.input.xml",
                   "SimpleGetTestCase.expected.xml");
    }

    /**
     * Test a get request that times out.
     *
     * @throws Exception if an error occurs.
     */
    public void testTimeoutGet() throws Exception {
        serverMock.addTransaction(new WaitTransaction(2000));

        try {
            doTest(pipelineFactory,
                    "TimeoutGetTestCase.input.xml",
                    "TimeoutGetTestCase.expected.xml");
            fail("Did not time out");
        } catch (XMLPipelineException e) {
            HTTPException cause = (HTTPException) e.getCause();
            assertEquals("Request to '" +
                    serverMock.getURLAsString("/mantis/Mantis_Login.jsp") +
                    "' timed out after 1000ms",
                    cause.getMessage());
        }
    }
    
    /**
     * Test a very simple post request
     *
     * @throws Exception if an error occurs.
     */
    public void testSimplePost() throws Exception {
            String[] responseContent = new String[]{
                "HTTP/1.0 200 OK",
                "Date: Fri, 31 Dec 1999 23:59:59 GMT",
                "Content-Type: text/plain",
                "",
                "<p>A Post</p>"
            };

        serverMock.addTransaction(null, responseContent);

            doTest(pipelineFactory,
                   "SimplePostTestCase.input.xml",
                   "SimplePostTestCase.expected.xml");
    }

    /**
     * Tests that parameters can be defined, copied and renamed via inline
     * markup.
     *
     * @throws Exception if an error occurs
     */
    public void testInlineParameters() throws Exception {

        final String[] responseContent = new String[]{
                "HTTP/1.0 200 OK",
                "Date: Fri, 31 Dec 1999 23:59:59 GMT",
                "Content-Type: text/plain",
                "",
                "<p>Inline Parameter Test</p>"
            };

        final String[] expectedRequest = new String[]{
            "GET /mantis/Mantis_Login.jsp?simple1=value&" +
                "simple2=value&simple3=overrideValue&" +
                "simple4=overrideValue&multi=value1&multi=value2&" +
                "name=fred&renamedRequest=jane HTTP/1.1",
            "User-Agent: Jakarta Commons-HttpClient/2.0.2",
            getHostHeader(inputServerPort),
        };

        serverMock.addTransaction(expectedRequest, responseContent);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            WebDriverConfiguration webdConf = retrieveWebdConfiguration(config);
            webdConf.setResponseContainsPipelineMarkup(true);
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            // register the parameters that this test requires with the
            // WebDriverRequest
            WebDriverRequest request = accessor.getRequest(context);
            HTTPMessageEntities parameters =
                    HTTPFactory.getDefaultInstance().
                    createHTTPMessageEntities();
            RequestParameter parameter1 = new RequestParameterImpl("name");
            parameter1.setValue("fred");
            RequestParameter parameter2 = new RequestParameterImpl("request2");
            parameter2.setValue("jane");
            parameters.add(parameter1);
            parameters.add(parameter2);
            request.setRequestParameters(parameters);

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "InlineParameterTestCase.input.xml",
                            "InlineParameterTestCase.expected.xml");

        }

    protected Reader createPipelineInputFilter(Reader reader)
            throws IOException {

        return createRedirectingReader(reader);

    }

    private Reader createRedirectingReader(Reader reader) throws IOException {
        StringBuffer buffer = new StringBuffer();
        int c;
        while ((c = reader.read()) != -1) {
            buffer.append((char) c);
        }

        String inputServerPortAsString = ":" + inputServerPort + "/";
        String outputServerPortAsString = ":" + outputServerPort + "/";

        String inputPortToken = ":8080/";
        System.out.println("Replacing " + inputPortToken + " with " +
                inputServerPortAsString);
        String outputPortToken = ":9090/";
        System.out.println("Replacing "+ outputPortToken + " with " +
                outputServerPortAsString);

        String s = buffer.toString();
        s = s.replaceFirst(inputPortToken, inputServerPortAsString);
        s = s.replaceFirst(outputPortToken, outputServerPortAsString);
        return new StringReader(s);
    }

    protected Reader createExpectedFilter(Reader reader) throws IOException {
        return createRedirectingReader(reader);
    }

    /**
     * Tests that headers can be defined, copied and renamed via inline
     * markup.
     *
     * @throws Exception if an error occurs
     */
    public void testInlineHeaders() throws Exception {

            String[] responseContent = new String[]{
                "HTTP/1.0 200 OK",
                "Date: Fri, 31 Dec 1999 23:59:59 GMT",
                "Content-Type: text/plain",
                "",
                "<p>Inline Header Test</p>"
            };

        final String[] expectedRequest = new String[]{
            "GET /mantis/Mantis_Login.jsp HTTP/1.1",
            "simple1: value",
            "simple2: value",
            "simple3: overrideValue",
            "simple4: overrideValue",
            "multi: value1",
            "multi: value2",
            "name: fred",
            "renamedRequest: jane",
            "User-Agent: Jakarta Commons-HttpClient/2.0.2",
            getHostHeader(inputServerPort),
        };

        serverMock.addTransaction(expectedRequest, responseContent);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            WebDriverConfiguration webdConf = retrieveWebdConfiguration(config);
            webdConf.setResponseContainsPipelineMarkup(true);
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();
            // register the parameters that this test requires with the
            // WebDriverRequest
            WebDriverRequest request = accessor.getRequest(context);

            HTTPMessageEntities headers =
                    HTTPFactory.getDefaultInstance().
                    createHTTPMessageEntities();
            Header header1 = new HeaderImpl("name");
            header1.setValue("fred");
            Header header2 = new HeaderImpl("request2");
            header2.setValue("jane");
            headers.add(header1);
            headers.add(header2);
            request.setHeaders(headers);

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "InlineHeaderTestCase.input.xml",
                            "InlineHeaderTestCase.expected.xml");
                }

    private String getHostHeader(final int serverPort) {
        return "Host: localhost:" + serverPort;
        }

    /**
     * Check that HTML Conditioning occurs for the text/html mime type. By
     * default a JTidy conditioner is allways provided for text/html.
     *
     * @throws Exception if an error occurs
     */
    public void testHTMLConditioning() throws Exception {

        String[] expectedRequest = new String[]{
            "GET /mantis/HTMLConditioning.jsp HTTP/1.1",
            "User-Agent: Jakarta Commons-HttpClient/2.0.2",
            getHostHeader(inputServerPort),
        };

            String[] responseContent = new String[]{
                "HTTP/1.0 200 OK",
                "Date: Fri, 31 Dec 1999 23:59:59 GMT",
                "Content-Type: text/html",
                "",
                "<html><p>HTML Conditioning</p></html>"
            };

        serverMock.addTransaction(expectedRequest, responseContent);

            doTest(pipelineFactory,
                   "HTMLConditioningTestCase.input.xml",
                   "HTMLConditioningTestCase.expected.xml");
    }

    /**
     * Provide an alternative conditioner for html.
     *
     * @throws Exception if an error occurs
     */
    public void testHTMLConditioningCanBeConfigured() throws Exception {
        String[] expectedRequest = new String[]{
            "GET /mantis/Mantis_Login.jsp HTTP/1.1",
            "User-Agent: Jakarta Commons-HttpClient/2.0.2",
            getHostHeader(inputServerPort)
        };

            String[] responseContent = new String[]{
                "HTTP/1.0 200 OK",
                "Date: Fri, 31 Dec 1999 23:59:59 GMT",
                "Content-Type: text/html",
                "",
                "<html><p>HTML Conditioning</p></html>"
            };

        serverMock.addTransaction(expectedRequest, responseContent);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            WebDriverConfiguration webdConf = retrieveWebdConfiguration(config);
        webdConf.addWebDriverConditionerFactory("text/html",
                    new WebDriverConditionerFactory() {
                    public ContentConditioner createConditioner(XMLFilter filter) {
                            XMLFilterImpl xmlFilter = new XMLFilterImpl();
                            return new XMLResponseConditioner(xmlFilter);
                        }
                    });

            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            doInContextTest(context,
                            "HTMLConditioningCanBeConfiguredTestCase.input.xml",
                            "HTMLConditioningCanBeConfiguredTestCase.expected.xml");
    }

    /**
     * Test a very simple post request
     *
     * @throws Exception if an error occurs.
     */
    public void testResponsePopulation() throws Exception {
        try {
            String[] responseContent = new String[]{
                "HTTP/1.0 200 OK",
                "Date: Fri, 31 Dec 1999 23:59:59 GMT",
                "Content-Type: text/plain",
                "Age: 1",
                "Title: Response Test",
                "Title: Response Test 2",
                "Set-Cookie: BROWSER=Netscape; domain=cnn.com; path=/",
                "Set-Cookie: DEVICE=PC; domain=volantis.com; path=/; secure",
                "Set-Cookie: DEVICE=PALM; domain=volantis.com; path=/",
                "",
                "<p>A Response Test</p>"
            };

            serverMock.addTransaction(null, responseContent);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            XMLPipelineContext context =
                    pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "SimpleResponsePopulationTestCase.input.xml",
                            "SimpleResponsePopulationTestCase.expected.xml");
            WebDriverResponse response = accessor.getResponse(context, "91");
            // check the header count
            assertEntityCount(response.getHeaders(), 5);
            // check the date header
            assertSingleHeader(response,
                               "Date",
                               "Fri, 31 Dec 1999 23:59:59 GMT");
            // check the content type header
            assertSingleHeader(response,
                               "Content-Type",
                               "text/plain");
            // check the Title headers
            String[] headers = new String[]{"Response Test",
                                            "Response Test 2"};

            assertMultipleHeader(response,
                                 "Title",
                                 new ArrayList(Arrays.asList(headers)));

            // check the cookies

            // check that we have the correct number of cookies
            assertEntityCount(response.getCookies(), 3);
            // check the browser cookie
            Cookie browser = HTTPFactory.getDefaultInstance().createCookie(
                    "BROWSER",
                    "cnn.com",
                    "/");
            browser.setValue("Netscape");
            assertSingleCookie(response,
                               browser);

            Cookie device1 = HTTPFactory.getDefaultInstance().createCookie(
                    "DEVICE",
                    "volantis.com",
                    "/");
            device1.setValue("PC");
            device1.setSecure(true);

            Cookie device2 = HTTPFactory.getDefaultInstance().createCookie(
                    "DEVICE",
                    "volantis.com",
                    "/");
            device2.setValue("PALM");
            device2.setSecure(false);
            List expectedCookies = new ArrayList();
            expectedCookies.add(device1);
            expectedCookies.add(device2);
            // check the device cookie
            assertMultipleCookies(response,
                                  "DEVICE",
                                  expectedCookies);

            // check the responses status code
            assertResponseStatusCode(response, 200);
            // check the responses content type
            assertResponseContentType(response, "text/plain");
            // check the http version
            assertResponseHTTPVersion(response, HTTPVersion.HTTP_1_0);

        } finally {
        }
    }

    /**
     * Test to ensure that an exception is thrown when the maximum number
     * of redirects is exceeded.
     *
     * @throws Exception
     */
    public void testMaximumNumberOfRedirectsExceeded() throws Exception {
        //arbitrary number larger then MAX_REDIRECTS
        final int maxRedirects = 30;

        HttpServerMock[] redirectServers = new HttpServerMock[maxRedirects + 1];
        redirectServers[0] = serverMock;

        String path = "/mantis/Mantis_Login.jsp";
        for (int i = 1; i < maxRedirects + 1; i++) {

            HttpServerMock redirectServerMock = createServerMock();
            redirectServers[i] = redirectServerMock;
            int redirectServerPort = redirectServerMock.getServerPort();

            HttpServerMock redirectingServerMock = redirectServers[i - 1];
            int redirectingServerPort = redirectingServerMock.getServerPort();
            String[] expectedRequest = new String[]{
                "GET " + path + " HTTP/1.1",
                "User-Agent: Jakarta Commons-HttpClient/2.0.2",
                getHostHeader(redirectingServerPort),
            };

                String[] redirectContent1 = new String[]{
                    "HTTP/1.0 301 OK",
                    "Content-Type: text/plain",
                    "Content-Length: 99",
                "Location: http://localhost:" + redirectServerPort +
                    "/welcome.jsp",
                    "Set-Cookie: BROWSER=Netscape; domain=localhost; path=/",
                    "",
                    "<p>We will be redirected so should never see this</p>"
                };

            redirectingServerMock.addTransaction(expectedRequest,
                    redirectContent1);

            path = "/welcome.jsp";
            }

            XMLPipelineConfiguration config = createPipelineConfiguration();
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            try {

                doInContextTest(context,
                                "SimpleMaxRedirectTestCase.input.xml",
                                "SimpleMaxRedirectTestCase.expected.xml");
                fail("An exception should have been thrown to indicate that the" +
                     "maximum allowed number of redirects has been exceeded");
            } catch (XMLPipelineException pe) {
            Exception exception = (Exception) pe.getCause();
            if (exception instanceof HTTPException) {
                HTTPException he = (HTTPException) exception;
                assertEquals("Maximum number of redirects (30) has been exceeded.",
                        he.getMessage());
            } else {
                throw exception;
            }
            }
        }

    /**
     * Test to ensure that infinate cycles are detected and a HTTPException
     * thrown.
     *
     * @throws Exception
     */
    public void testCyclicalRedirect() throws Exception {

        HttpServerMock redirectServerMock = createServerMock();

        int redirectServerPort = redirectServerMock.getServerPort();
        String[] redirectContent1 = new String[]{
            "HTTP/1.0 301 OK",
            "Content-Type: text/plain",
            "Content-Length: 99",
            "Location: http://localhost:" + redirectServerPort +
                "/welcome.jsp",
            "Set-Cookie: BROWSER=Netscape; domain=localhost; path=/",
            "",
            "<p>We will be redirected so should never see this</p>"
        };

        // Redirect from the server to the redirect server.
        serverMock.addTransaction(null, redirectContent1);

        String[] redirectContent2 = new String[]{
            "HTTP/1.0 301 OK",
            "Content-Type: text/plain",
            "Content-Length: 99",
            "Location: http://localhost:" + inputServerPort + "/welcome.jsp",
            "Set-Cookie: BROWSER=Netscape; domain=localhost; path=/",
            "",
            "<p>We will be redirected so should never see this</p>"
        };

        // Redirect back from the redirect server to the original server.
        redirectServerMock.addTransaction(null, redirectContent2);

        // Redirect from the original server back to the redirect server.
        serverMock.addTransaction(null, redirectContent1);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            try {
                doInContextTest(context,
                                "SimpleRedirectTestCase.input.xml",
                                "SimpleRedirectTestCase.expected.xml");
                fail("A cyclical redirect exception should have been thrown");
            } catch (XMLPipelineException pe) {
            Exception exception = (Exception) pe.getCause();
            if (exception instanceof HTTPException) {
                HTTPException he = (HTTPException) exception;
                assertEquals("Cyclical redirect has been detected.",
                        he.getMessage());
            } else {
                throw exception;
            }
        }
    }


    /**
     * Tests that the status codes 301, 302 and 307 result in a redirect being
     * followed. This also tests URL remaping.
     *
     * @throws Exception if an error occurs
     */
    public void testSimpleRedirect() throws Exception {
        // redirect status codes are 301, 302 and 307

        HttpServerMock redirectServerMock1 = createServerMock();
        HttpServerMock redirectServerMock2 = createServerMock();
        HttpServerMock redirectServerMock3 = createServerMock();

        String[] redirectContent1 = new String[]{
            "HTTP/1.0 301 OK",
            "Content-Type: text/plain",
            "Content-Length: 99",
            "Age: 1",
            "Location: " + redirectServerMock1.getURLAsString("/welcome.jsp"),
            "Set-Cookie: BROWSER=Netscape; domain=localhost; path=/",
            "",
            "<p>We will be redirected so should never see this</p>"
        };
        serverMock.addTransaction(null, redirectContent1);

        String[] redirectContent2 = new String[]{
            "HTTP/1.0 302 OK",
            "Content-Type: text/wml",
            "Content-Length: 88",
            "Age: 1",
            "Location: " + redirectServerMock2.getURLAsString("/hello.jsp"),
            "Set-Cookie: DEVICE=PC; domain=localhost; path=/; secure",
            "",
            "<p>Again we will be redirected so should never see this</p>"
        };
        redirectServerMock1.addTransaction(null, redirectContent2);

        String[] redirectContent3 = new String[]{
            "HTTP/1.0 307 OK",
            "Content-Type: text/xml",
            "Content-Length: 77",
            "Age: 1",
            "Location: " + redirectServerMock3.getURLAsString("/my.jsp"),
            "Set-Cookie: DEVICE=PALM; domain=localhost; path=/",
            "",
            "<p>Again we will be redirected so should never see this</p>"
        };
        redirectServerMock2.addTransaction(null, redirectContent3);

        String[] finalContent = new String[]{
            "HTTP/1.0 200 OK",
            "Content-Type: text/html",
            "Content-Length: 543",
            "Age: 1",
            "Set-Cookie: DEVICE=NOKIA; domain=localhost; path=/",
            "",
            "<html>",
            "<p>Should see this</p>",
            "<a href=\"fred.jsp\"/>",
            "</html>"
        };
        redirectServerMock3.addTransaction(null, finalContent);

        outputServerPort = redirectServerMock3.getServerPort();

            XMLPipelineConfiguration config = createPipelineConfiguration();
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "SimpleRedirectTestCase.input.xml",
                            "SimpleRedirectTestCase.expected.xml");

            // check the response
            WebDriverResponse response = accessor.getResponse(context, "5");

            // check the status code
            assertResponseStatusCode(response, 200);

            // check the content type
            assertResponseContentType(response, "text/html");

            // check that we have two headers
            assertEntityCount(response.getHeaders(), 3);

            // check the content length header
            assertSingleHeader(response,
                               "Content-Length",
                               "543");

            // check the content type header
            assertSingleHeader(response,
                               "Content-Type",
                               "text/html");

            // check that we have the expected number of cookies
            assertEntityCount(response.getCookies(), 4);

            // check that we have the correct cookies
        Cookie browser = HTTPFactory.getDefaultInstance().createCookie("BROWSER",
                    "localhost",
                    "/");
            browser.setValue("Netscape");
            assertSingleCookie(response,
                               browser);

            assertSingleCookie(response, browser);

        Cookie device1 = HTTPFactory.getDefaultInstance().createCookie("DEVICE",
                    "localhost",
                    "/");
            device1.setValue("PC");
            device1.setSecure(true);

        Cookie device2 = HTTPFactory.getDefaultInstance().createCookie("DEVICE",
                    "localhost",
                    "/");
            device2.setValue("PALM");
            device2.setSecure(false);

        Cookie device3 = HTTPFactory.getDefaultInstance().createCookie("DEVICE",
                    "localhost",
                    "/");
            device3.setValue("NOKIA");
            device3.setSecure(false);

            List expectedCookies = new ArrayList();
            expectedCookies.add(device1);
            expectedCookies.add(device2);
            expectedCookies.add(device3);

            assertMultipleCookies(response, "DEVICE", expectedCookies);
    }

    /**
     * Tests that you can switch of the folling or redirects via in line markup
     *
     * @throws Exception if an error occurs
     */
    public void testFollowRedirectSwitchedOffInline() throws Exception {

        HttpServerMock redirectServerMock = createServerMock();

        int redirectServerPort = redirectServerMock.getServerPort();
        String[] redirectContent1 = new String[]{
            "HTTP/1.0 301 OK",
            "Content-Type: text/plain",
            "Content-Length: 46",
            "Age: 1",
            "Location: http://localhost:" + redirectServerPort +
                "/welcome.jsp",
            "",
            "<p>Ignoring redirects so should see this</p>"
        };

        serverMock.addTransaction(null, redirectContent1);

        String[] redirectContent2 = new String[]{
            "HTTP/1.0 302 OK",
            "Content-Type: text/wml",
            "Content-Length: 88",
            "Age: 1",
            "Set-Cookie: DEVICE=PC; domain=localhost; path=/; secure",
            "",
            "<p>As we are not following redirects we should not see this</p>"
        };
        redirectServerMock.addTransaction(null, redirectContent2);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "InlineRedirectConfigurationTestCase.input.xml",
                            "InlineRedirectConfigurationTestCase.expected.xml");

            WebDriverResponse response = accessor.getResponse(context, "5");

            // check the status code
            assertResponseStatusCode(response, 301);

            // check header count
            assertEntityCount(response.getHeaders(), 4);

            // check the content length header
            assertSingleHeader(response,
                               "Content-Length",
                               "46");

            // check the content type header
            assertSingleHeader(response,
                               "Content-Type",
                               "text/plain");

            // check the location header
            assertSingleHeader(response,
                               "Location",
                "http://localhost:" + redirectServerPort + "/welcome.jsp");

            // check cookie count
            assertEntityCount(response.getCookies(), 0);
    }


    /**
     * Tests that remapping of URLs occurs when there is no redirection.
     */
    public void testRemapNoRedirects() throws Exception {
        String[] redirectContent1 = new String[]{
            "HTTP/1.0 302 OK",
            "Content-Type: text/plain",
            "Age: 1",
            "Content-Length: 46",
            "Location: http://www.volantis.com/dsb/xpv/welcome.jsp",
            "",
            "<p>Ignoring redirects so should see this</p>"
        };

        serverMock.addTransaction(null, redirectContent1);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            WebDriverConfiguration webdConf = retrieveWebdConfiguration(config);
            webdConf.setResponseContainsPipelineMarkup(true);

            // Switch on remapping
            webdConf.setRemapRedirects(true);

            // Create and configure the URL rewriter.
            URLRewriteProcessConfiguration configuration =
                    new URLRewriteProcessConfiguration();
            URLPrefixRewriteManager rewriter =
                    configuration.getURLPrefixRewriteManager();

            // This rule simulates mapping a DSB service called xpv to
            // the DSB service anotherService.
            rewriter.addRewritableURLPrefix("http://www.volantis.com/dsb/xpv",
                    "http://www.volantis.com/dsb/anotherService",
                    URLPrefixRewriteOperation.REPLACE_PREFIX);
            webdConf.setRedirectRewriteManager(rewriter);

            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                    "RemapNoRedirectsConfigurationTestCase.input.xml",
                    "RemapNoRedirectsConfigurationTestCase.expected.xml");

            WebDriverResponse response = accessor.getResponse(context, "5");

            // check the status code
            assertResponseStatusCode(response, 302);

            // check header count
            assertEntityCount(response.getHeaders(), 4);

            // check the content length header
            assertSingleHeader(response,
                    "Content-Length",
                    "46");

            // check the content type header
            assertSingleHeader(response,
                    "Content-Type",
                    "text/plain");

            // Check that the location header is remapped from the xpv DSB
            // service to the anotherService.
            assertSingleHeader(response,
                    "Location",
                    "http://www.volantis.com/dsb/anotherService/welcome.jsp");

            // check cookie count
            assertEntityCount(response.getCookies(), 0);
    }

    /**
     * Tests that you can configure the driver to process pipeline markup in
     * the response
     *
     * @throws Exception if an error occurs
     */
    public void testProcessingOfPipelineMarkupInResponse() throws Exception {
        // Cannot set a content length header here (unless accurate) as it
        // upsets the Jigsaw implementation which expects it to match the true
        // szie of the content and checks this during processing.
        String[] response = new String[]{
            "HTTP/1.0 200 OK",
            "Content-Type: text/plain",
            "",
            "<p xmlns:pipeline=\"http://www.volantis.com/xmlns/marlin-integration\">",
            "<pipeline:variableScope>",
            "<pipeline:variableDeclaration name=\"myVar\" value=\"hello\"/>",
            "<variableReference reference=\"%{$myVar}\"/>",
            "<notVariableReference value=\"\\%{value}\"/>",
            "</pipeline:variableScope>",
            "</p>"
        };

        serverMock.addTransaction(null, response);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            WebDriverConfiguration webdConf = retrieveWebdConfiguration(config);
            webdConf.setResponseContainsPipelineMarkup(true);
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "ProcessingOfPipelineMarkupTestCase.input.xml",
                            "ProcessingOfPipelineMarkupTestCase.expected.xml");
    }

    /**
     * Test that errored content is ignored and saved to the WebDriverResponse
     *
     * @throws Exception if an error occurs
     */
    public void testIgnoreErroredContent() throws Exception {
        // Cannot set a content length header here (unless accurate) as it
        // upsets the Jigsaw implementation which expects it to match the true
        // szie of the content and checks this during processing.
        String[] response = new String[]{
            "HTTP/1.0 404 OK",
            "Content-Type: text/plain",
            "",
            "This should be ignored"
        };

        serverMock.addTransaction(null, response);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "IgnoreErroredContentTestCase.input.xml",
                            "IgnoreErroredContentTestCase.expected.xml");

            WebDriverResponse resp = accessor.getResponse(context, "5");

            InputStream is = resp.getIgnoredContent();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String content = null;
            do {
                content = reader.readLine();
                if (content != null) {
                    buffer.append(content);
                }
            } while (content != null);
            reader.close();
            assertEquals("Ignored content was not saved",
                         "This should be ignored",
                         buffer.toString());
    }

    /**
     * Test that errored content is ignored and is not saved when configured
     * to do so.
     *
     * @throws Exception if an error occurs
     */
    public void testIgnoreErroredContentWhenDisabled() throws Exception {
        // Cannot set a content length header here (unless accurate) as it
        // upsets the Jigsaw implementation which expects it to match the true
        // szie of the content and checks this during processing.
        String[] response = new String[]{
            "HTTP/1.0 404 OK",
            "Content-Type: text/plain",
            "",
            "<p>This should not be ignored</p>"
        };

        serverMock.addTransaction(null, response);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "IgnoreErroredContentWhenDisabledTestCase.input.xml",
                            "IgnoreErroredContentWhenDisabledTestCase.expected.xml");

            WebDriverResponse resp = accessor.getResponse(context, "5");

            InputStream is = resp.getIgnoredContent();
            assertNull("Ignored content should not be saved", is);
    }

    /**
     * Test that you can cofigure the driver to ignore content of a given mime
     * type.
     *
     * @throws Exception if an error occurs
     */
    public void testIgnoreContentForMimeType() throws Exception {
        // Cannot set a content length header here (unless accurate) as it
        // upsets the Jigsaw implementation which expects it to match the true
        // szie of the content and checks this during processing.
        String[] response = new String[]{
            "HTTP/1.0 200 OK",
            "Content-Type: text/plain",
            "",
            "<p>This should be saved to the response</p>"
        };

        serverMock.addTransaction(null, response);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            // need to enable ignore mime type feature if we want the ignored
            // content to be saved to the response
            retrieveWebdConfiguration(config).setIgnoreContentEnabled(true);
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "IgnoreContentForMimeTypeTestCase.input.xml",
                            "IgnoreContentForMimeTypeTestCase.expected.xml");

            WebDriverResponse resp = accessor.getResponse(context, "5");

            InputStream is = resp.getIgnoredContent();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String content = null;
            do {
                content = reader.readLine();
                if (content != null) {
                    buffer.append(content);
                }
            } while (content != null);
            reader.close();
            assertEquals("Ignored content was not saved",
                         "<p>This should be saved to the response</p>",
                         buffer.toString());
    }

    /**
     * Test that you can cofigure the driver to ignore content of a given mime
     * type and not save the ignored to the response if configured as such.
     *
     * @throws Exception if an error occurs
     */
    public void testIgnoreContentForMimeTypeIsNotSaved() throws Exception {
        String[] response = new String[]{
            "HTTP/1.0 200 OK",
            "Content-Type: text/plain",
            "Content-Length: 90",
            "",
            "<p>This should be saved to the response</p>"
        };

        serverMock.addTransaction(null, response);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                            createRootEnvironmentInteraction());

            WebDriverAccessor accessor = createWebDriverAccessor();

            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "IgnoreContentForMimeTypeTestCase.input.xml",
                            "IgnoreContentForMimeTypeTestCase.expected.xml");

            WebDriverResponse resp = accessor.getResponse(context, "5");
            // as we have not configured the WebDriverConfiguration to save
            // away ignored content we are looking for a null IntputStream from
            // the responses getIgnoredContent() method
            InputStream is = resp.getIgnoredContent();
            assertNull("Ignored content should not be saved", is);
    }

    /**
     * Ensures that a {@link HTTPResponsePreprocessor} is invoked if registered.
     *
     * @throws Exception if an error occurs.
     */
    public void testHTTPHeaderPreprocessing() throws Exception {

            String[] responseContent = new String[]{
                "HTTP/1.0 200 OK",
                "Date: Fri, 31 Dec 1999 23:59:59 GMT",
                "Content-Type: text/plain",
                "",
                "<p>A Get</p>"
            };

        serverMock.addTransaction(null, responseContent);

            XMLPipelineConfiguration config = createPipelineConfiguration();
            WebDriverConfiguration webdConf = retrieveWebdConfiguration(config);

            webdConf.setResponseContainsPipelineMarkup(true);
            XMLPipelineContext context =
                pipelineFactory.createPipelineContext(config,
                                    createRootEnvironmentInteraction());
            context.setProperty(HTTPResponsePreprocessor.class,
                                new HTTPResponsePreprocessor() {
                    public void preprocessResponse(HTTPMessageEntities headers,
                                                       HTTPMessageEntities cookies,
                            int statusCode) {
                            // create a content type header in order to perform a
                            // lookup
                            Header contentType =
                                    HTTPFactory.getDefaultInstance()
                                    .createHeader("Content-Type");

                            // retrieve all the contentType headers
                        Header[] headerArray = (Header[]) headers.retrieve(contentType.getIdentity());

                            // change all contentType headers
                            for (int i = 0; i < headerArray.length; i++) {
                                contentType = headerArray[i];
                                contentType.setValue("text/xml");
                            }
                        }
                    }, false);
            WebDriverAccessor accessor = createWebDriverAccessor();
            context.setProperty(WebDriverAccessor.class, accessor, false);

            doInContextTest(context,
                            "SimpleGetTestCase.input.xml",
                            "SimpleGetTestCase.expected.xml");


            WebDriverResponse response = accessor.getResponse(context,  "id");
            assertEquals("Unexpected Content type ",
                         "text/xml",
                         response.getContentType());
    }

    /**
     * Checks tha the given <code>HTTPMessageEntities</code> contains the
     * expected number of entities
     *
     * @param entities the <code>HTTPMessageEntities</code> to check
     * @param expectedCount the expected count
     */
    private void assertEntityCount(
            HTTPMessageEntities entities,
                                   int expectedCount) {
        if (expectedCount == 0) {
            assertNull("Should be 0 entities", entities);
        } else {
            assertEquals("Should be " + expectedCount + " entities",
                         expectedCount,
                         entities.size());
        }
    }

    /**
     * Assert that the response contains the expected header
     *
     * @param response the response
     * @param expectedName the name of the expected header
     * @param expectedValue the value of the expected header
     */
    private void assertSingleHeader(
            WebDriverResponse response,
                                    String expectedName,
                                    String expectedValue) {

        HTTPMessageEntities headers = response.getHeaders();
        Header expected =
                HTTPFactory.getDefaultInstance().createHeader(expectedName);

        Header[] headerArray = (Header[]) headers.retrieve(
                expected.getIdentity());

        assertNotNull("Should be a " + expectedName + " header", headerArray);
        assertEquals("Should only be a single " + expectedName + " header",
                     1,
                     headerArray.length);
        assertEquals("Wrong value for header : " + expectedName,
                     expectedValue,
                     headerArray[0].getValue());
    }

    /**
     * Check that the response contains the expected multi header.
     *
     * @param response the response
     * @param expectedName the name of the header
     * @param expectedValues A list of the expected values
     */
    private void assertMultipleHeader(
            WebDriverResponse response,
                                      String expectedName,
                                      List expectedValues) {

        HTTPMessageEntities headers = response.getHeaders();
        int expectedCount = expectedValues.size();

        Header expected = HTTPFactory.getDefaultInstance().createHeader(
                expectedName);
        Header[] headerArray = (Header[]) headers.retrieve(
                expected.getIdentity());

        assertNotNull("Should be " + expectedName + " headers", headerArray);
        assertEquals("Should only be " + expectedCount + " " + expectedName +
                     " headers",
                     expectedCount,
                     headerArray.length);

        for (int i = 0; i < expectedCount; i++) {
            assertTrue("Response did not contain expected header : " +
                       expectedName,
                       expectedValues.contains(headerArray[i].getValue()));
            expectedValues.remove(headerArray[i].getValue());
        }

    }

    /**
     * Assert that the response contains the expected cookie
     *
     * @param response the response
     * @param expected the expected cookie
     */
    private void assertSingleCookie(
            WebDriverResponse response,
                                    Cookie expected) {

        HTTPMessageEntities cookies = response.getCookies();
        Cookie[] cookieArray = (Cookie[]) cookies.retrieve(
                expected.getIdentity());

        assertNotNull("Should be a " + expected.getName() + " cookie",
                      cookieArray);
        assertEquals("Should only be a single " + expected.getName() +
                     " cookie",
                     1,
                     cookieArray.length);
        assertEquals("Wrong value for cookie : " + expected.getName(),
                     expected,
                     cookieArray[0]);
    }

    /**
     * Check that the response contains the expected multi cookies
     *
     * @param response the response
     * @param expectedName the name of the cookie
     * @param expectedCookies A list of expected cookies
     */
    private void assertMultipleCookies(
            WebDriverResponse response,
                                       String expectedName,
                                       List expectedCookies) {

        HTTPMessageEntities cookies = response.getCookies();
        // the multiple cookies should have the same identity
        Cookie cookie = (Cookie) expectedCookies.get(0);
        CookieIdentity expectedIdentity = (CookieIdentity) cookie.getIdentity();

        int expectedCount = expectedCookies.size();
        Cookie[] cookieArray = (Cookie[]) cookies.retrieve(expectedIdentity);

        assertNotNull("Should be " + expectedName + " cookies", cookieArray);
        assertEquals("Should only be " + expectedCount + " " + expectedName +
                     " cookies",
                     expectedCount,
                     cookieArray.length);

        for (int i = 0; i < expectedCount; i++) {
            Cookie actual = cookieArray[i];
            // check to see if the cookie exists in the expected list.
            assertTrue("Response did not contain the expected cookie " +
                       expectedName, expectedCookies.contains(actual));
            // remove the expected from the list
            expectedCookies.remove(actual);
        }

    }

    /**
     * Checks that the given response has the expected content type
     *
     * @param response the response to check
     * @param expectedType the expected content type.
     */
    private void assertResponseContentType(
            WebDriverResponse response,
                                           String expectedType) {
        assertEquals("Incorrect content type",
                     expectedType,
                     response.getContentType());

    }

    /**
     * Checks that the given response has the expected status code
     *
     * @param response the response to check
     * @param expectedSatusCode the expected status code.
     */
    private void assertResponseStatusCode(
            WebDriverResponse response,
                                          int expectedSatusCode) {

        assertEquals("Incorrect status code",
                     expectedSatusCode,
                     response.getStatusCode());
    }

    /**
     * Check that the given response contains the expected HTTPVersion.
     *
     * @param response the response to check.
     * @param version the expected http version.
     */
    private void assertResponseHTTPVersion(
            WebDriverResponse response,
                                           HTTPVersion version) {

        assertEquals("Incorrect HTTP version", version,
                     response.getHTTPVersion());
    }

    /**
     * Retrieves the WebDriverConfiguraton from the given
     * XMLPipelineConfiguration
     *
     * @param conf the XMLPipelineConfiguration
     * @return a WebDriverConfiguraton instance
     */
    private WebDriverConfiguration retrieveWebdConfiguration(
                XMLPipelineConfiguration conf) {
        return (WebDriverConfiguration) conf.retrieveConfiguration(
                WebDriverConfiguration.class);
    }

    /**
     * Creates a <code>WebDriverAccessor</code> for use in a test case
     *
     * @return a <code>WebDriverAccessor</code> instance
     */
    private WebDriverAccessor createWebDriverAccessor() {
        // create a WebDriverAccessor
        return new WebDriverAccessor() {

            /**
             * A request
             */
            private WebDriverRequest request =
                    WebDriverFactory.getDefaultInstance().createRequest();

            /**
             * Will store the responses
             */
            private Map responses = new HashMap();

            // javadoc inherited
            public WebDriverRequest getRequest(
                    XMLPipelineContext pipelineContext) {
                return request;
            }

            // javadoc inherited
            public WebDriverResponse getResponse(
                    XMLPipelineContext pipelineContext,
                    String id) {
                WebDriverResponse response =
                        (WebDriverResponse) responses.get(id);
                if (response == null) {
                    response = WebDriverFactory.getDefaultInstance().
                            createResponse();
                    responses.put(id, response);
                }
                return response;
            }
        };
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/2	matthew	VBM:2005092809 Allow proxy configuration via system properties

 20-Jun-05	8751/1	schaloner	VBM:2005060711 [Refactor - method signature] public DefaultHandler createDefaultHandler changed to public ContentHandler createContentHandler in PipelineTestAbstract

 24-Mar-05	7501/1	matthew	VBM:2005031708 Make CachingPluggableHTTPManager use request parameters a part of the cache key and refactor AbstractPluggableHTTPManager

 23-Mar-05	7443/3	matthew	VBM:2005031708 allow urls with parameters to be cached

 18-Mar-05	7450/2	pcameron	VBM:2005030904 DSB's Propagate to browser now remaps URLs

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Sep-04	872/1	matthew	VBM:2004083107 add JSessionID proxy handling

 08-Sep-04	854/1	matthew	VBM:2004083107 allow httpProcessor to obtain pre and post request processors from Pipline context

 08-Sep-04	869/1	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	865/2	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	858/2	doug	VBM:2004090610 Added preprocessing of response capability

 27-Aug-04	842/1	matthew	VBM:2004082417 stop urls being rewritten when a redirect does NOT occur

 27-Aug-04	838/1	matthew	VBM:2004082417 stop urls being rewritten when a redirect does NOT occur

 27-Jul-04	798/2	adrianj	VBM:2004072206 Changes to allow cookie propagation

 20-Jul-04	789/2	matthew	VBM:2004071602 Add HTTPVersion to WebDriverResponse

 20-Jul-04	781/8	adrianj	VBM:2004071302 Moved FakeSocketFactory and SocketImplWrapper from Pipeline to Synergetics

 19-Jul-04	781/6	adrianj	VBM:2004071302 Moved FakeSocketFactory and SocketImplWrapper from Pipeline to Synergetics

 16-Jul-04	767/4	claire	VBM:2004070101 Provide Jigsaw implemention of PluggableHTTPManager

 12-Jul-04	751/9	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 16-Jul-04	781/1	adrianj	VBM:2004071302 Moved FakeSocketFactory and SocketImplWrapper from Pipeline to Synergetics

 09-Jul-04	769/4	doug	VBM:2004070502 Improved integration tests for the Web Driver

 04-Aug-03	217/1	allan	VBM:2003071702 Filter nested anchors. Fixed merge conflicts.

 31-Jul-03	238/8	byron	VBM:2003072309 Create the adapter process for parent task v4

 30-Jul-03	238/5	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit v3

 30-Jul-03	238/3	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit v2

 30-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit

 ===========================================================================
*/
