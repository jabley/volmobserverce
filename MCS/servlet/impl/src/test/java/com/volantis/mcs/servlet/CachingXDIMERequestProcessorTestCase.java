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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.mcs.context.CacheScopeConstant;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.runtime.configuration.RenderedPageCacheConfiguration;
import com.volantis.mcs.service.ServiceDefinition;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.system.SystemClock;
import mock.javax.servlet.ServletContextMock;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Testcase for the CachingXDIMERequestProcessor
 */
public class CachingXDIMERequestProcessorTestCase extends TestCaseAbstract {
    /**
     * cache scope value that would be specified in the mcs-config file
     */
    private String configCacheScope;

    /**
     * cache scope value that is specified by the page
     */
    private String pageCacheScope;

    /**
     * servlet context being for the tests
     */
    private ServletContextMock servletContext;

    /**
     * object used in the test to act as the pageCacheConfiguration created
     * from the values in the mcs-config file
     */
    private RenderedPageCacheConfiguration pageCacheConfiguration =
            new RenderedPageCacheConfiguration();

    /**
     * the request object being used be the tests
     */
    private HttpServletRequest request;

    /**
     * the cache scope which would normally be specified by mcs
     */
    private CacheScopeConstant mcsCacheScope;

    /**
     * page representation
     */
    private String xdimeContentString;
    private ResponseCachingDirectives cachingDirectives;
    private XDIMERequestProcessorHelperStub requestProcessorHelperStub;

    /**
     * initialse the content of the xdimeContent string to the value of a
     * test page. the cacheScope of the page is set to the value of the
     * pageCacheScope if the pageCacheScope has been specified
     */
    private void initxdimeContentString() {
        if (pageCacheScope == null) {
            xdimeContentString = "<canvas\n" +
                "    xmlns=\"http://www.volantis.com/xmlns/marlin-cdm\"\n" +
                "    layoutName=\"/welcome.mlyt\"\n" +
                "    theme=\"/welcome.mthm\"\n" +
                "    pageTitle=\"Welcome to XDIME XML\">\n" +
                "    <pane name=\"logo\">\n" +
                "        <p>Some Text</p>" +
                "    </pane>" +
                "    </canvas>";
        } else {
            xdimeContentString = "<canvas\n" +
            "    xmlns=\"http://www.volantis.com/xmlns/marlin-cdm\"\n" +
            "    layoutName=\"/welcome.mlyt\"\n" +
            "    theme=\"/welcome.mthm\"\n" +
            "    pageTitle=\"Welcome to XDIME XML\"\n" +
            "    cacheScope=\""+ pageCacheScope +"\">\n" +
            "    <pane name=\"logo\">\n" +
            "        <p>Some Text</p>" +
            "    </pane>" +
            "    </canvas>";
        }
    }

    /**
     * create a CachingXDIMERequestProcessor which will be used for the
     * testing.
     *
     * The processor will have overridden methods and requestProcessorHelper
     * will be specified, instead of using the default, to prevent the whole
     * of mcs being invoked.
     *
     * @return
     * @throws ServletException
     */
    private CachingXDIMERequestProcessor createRequestProcessor(String mode)
            throws ServletException {

        List mimeTypes = new ArrayList();
        mimeTypes.add("x-application/vnd.xdime+xml");
        mimeTypes.add("x-application/vnd.volantis.xdime+xml");

        MarinerServletRequestContext requestContext =
                createServletRequestContext();

        requestProcessorHelperStub = new XDIMERequestProcessorHelperStub(
                requestContext, xdimeContentString, mode);

        setupPageCacheConfiguration();

        CachingXDIMERequestProcessor requestProcessor =
                new CachingXDIMERequestProcessor(servletContext,
                                mimeTypes, pageCacheConfiguration, null,
                                requestProcessorHelperStub) {
                    protected void initVolantis() {
                        //no-op
                    }
                    protected void initApplication(
                            ServletContext servletContext) {
                        //no-op
                    }
                    protected boolean willGenerateBinaryOutput(
                            MarinerServletRequestContext context) {
                        return false;
                    }
                };
        return requestProcessor;
    }

    /**
     * create a servlet request context to be used for the test. methods will
     * be overridden instead of mocking mcs
     * @return
     */
    private MarinerServletRequestContext createServletRequestContext() {
        MarinerServletRequestContext marinerServletRequestContext =
                new MarinerServletRequestContext() {
                    public HttpServletRequest getHttpRequest() {
                        return request;
                    }

                    public String getDeviceName () {
                        return "PC";
                    }

                    public HttpServletResponse getHttpResponse() {
                        return createServletResponse(true);
                    }

                    public ServletContext getServletContext() {
                        return servletContext;
                    }

                    public ServletResponse getResponse() {
                        return createServletResponse(false);
                    }
                };

        addEnvironmentContext(marinerServletRequestContext);
        return marinerServletRequestContext;
    }

    private void addEnvironmentContext(
            final MarinerRequestContext marinerRequestContext) {
        // set the environment context
        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        cachingDirectives =
            new ResponseCachingDirectives(SystemClock.getDefaultInstance());
        cachingDirectives.enable();
        assertTrue(cachingDirectives.isEnabled());
        environmentContextMock.expects.getCachingDirectives().returns(
            cachingDirectives).any();
        ContextInternals.setEnvironmentContext(marinerRequestContext,
            environmentContextMock);
    }

    /**
     * create a caching response wrapper to be used by the test
     * @return
     */
    private CachingResponseWrapper createCachingResponse(boolean isNone) {
        HttpServletResponseMock servletResponse =
                new HttpServletResponseMock("servletResponse", expectations);

        if (isNone) {
            servletResponse.expects.setContentType("TEXT").any();
            servletResponse.expects.getWriter().returns(
                new PrintWriter(new ByteArrayOutputStream()));
        }
        servletResponse.expects.getCharacterEncoding().returns("ISO-8859-1");

        return new CachingResponseWrapper(servletResponse);
    }

    /**
     * set up the pageCacheConfiguration
     */
    private void setupPageCacheConfiguration() {
        pageCacheConfiguration.setDefaultScope(configCacheScope);
        pageCacheConfiguration.setMaxEntries(new Integer(200));
        pageCacheConfiguration.setStrategy("least-used");
        pageCacheConfiguration.setTimeout(new Integer(-1));
    }

    /**
     * create the servlet request for the test
     * @return
     */
    private HttpServletRequest createServletRequest(boolean isInitialRequest,
                                                    boolean isNone) {

        HttpServletRequestMock servletRequest =
                    new HttpServletRequestMock("servletRequest", expectations);

        servletRequest.expects.getAttribute(ServiceDefinition.class.getName()).
                returns(null);                        

        if (!isNone) {
            servletRequest.expects.getRequestURI().
                        returns("/volantis/welcome/welcome.xdime");

            servletRequest.expects.getParameter("vfrag").returns(null);

            if (isInitialRequest) {
                servletRequest.expects.getAttribute("cacheScope").
                        returns(mcsCacheScope);
            }
        }

        return servletRequest;
    }

    /**
     * create the servlet response which will be used for the test
     * @param useWriter - indicates if a printwriter is required or an
     * output stream.
     * @return
     */
    private HttpServletResponse createServletResponse(boolean useWriter) {
        HttpServletResponseMock servletResponse =
                new HttpServletResponseMock("servletResponse", expectations);

        servletResponse.expects.setContentType("TEXT").any();
        if (useWriter) {
            servletResponse.expects.getWriter().returns(
                new PrintWriter(new ByteArrayOutputStream()));
        } else {
            servletResponse.expects.getOutputStream().
                    returns(new ServletOutputStream() {
                        public void write(int b) throws IOException {

                        }
                    });
        }

        return servletResponse;
    }

    /**
     * do the tests by processing the same request twice
     * @param mode
     * @throws IOException
     * @throws ServletException
     */
    private void doTest(String mode) throws IOException, ServletException {
        //create the context
        servletContext =
                new ServletContextMock("servletContext", expectations);

        boolean isNone = mode.equals("none");

        /**
         * send the first request
         */
        CachingXDIMERequestProcessor requestProcessor =
                firstRequest(isNone, mode);

        /**
         * send the second request
         */
        secondRequest(isNone, requestProcessor);
    }

    /**
     * Run the second request
     * @param none - specifies if the cache scope is none so that the correct
     * expectations are used
     * @param requestProcessor - the processor used for the first request
     * @throws IOException
     * @throws ServletException
     */
    private void secondRequest(boolean none,
                               CachingXDIMERequestProcessor requestProcessor)
            throws IOException, ServletException {
        CachingResponseWrapper cachingResponse2 =
                createCachingResponse(none);
        request = createServletRequest(false, none);
        // first request releases the MarinerServletRequestContext, so the
        // request processor helper gets a fresh one to return on
        // createServletRequestContext
        requestProcessorHelperStub.setServletRequestContext(
            createServletRequestContext());
        requestProcessor.processXDIME(servletContext, request,
            cachingResponse2.getResponse(), cachingResponse2,
            cachingResponse2.getCharacterEncoding(), true);
        assertFalse(cachingDirectives.isEnabled());
    }

    /**
     * Run the first request where no processor has been created previously
     * @param none - specifies if the cache scope is none so that the correct
     * expectations are used
     * @param mode - the caching mode being used
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private CachingXDIMERequestProcessor firstRequest(boolean none,
                                                      String mode)
            throws ServletException, IOException {
        return firstRequest(none, mode, null);
    }

    /**
     * Run the first request where a processor has been created previously.
     * This method is used because when no page caching is allowed the second
     * request should use the same expectations as the first request but should
     * share the same request processor. If no request processor is specified
     * one will be created
     * @param none - specifies if the cache scope is none so that the correct
     * expectations are used
     * @param mode - the caching mode being used
     * @param requestProcessor
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private CachingXDIMERequestProcessor firstRequest(boolean none,
                              String mode,
                              CachingXDIMERequestProcessor requestProcessor)
            throws ServletException, IOException {

        request = createServletRequest(true, none);

        if (requestProcessor == null) {
            requestProcessor =
                createRequestProcessor(mode);
        }

        CachingResponseWrapper cachingResponse =
                createCachingResponse(none);
        requestProcessor.processXDIME(servletContext, request,
            cachingResponse.getResponse(), cachingResponse,
            cachingResponse.getCharacterEncoding(), false);
        assertTrue(cachingDirectives.isEnabled());
        return requestProcessor;
    }

    /**
     * test the page caching when the cache scope is configured as optimistic
     *
     * @throws ServletException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public void testOptimistic() throws ServletException, IOException,
            IllegalAccessException, InstantiationException,
            ClassNotFoundException {
        /**
         * define the configuration params
         */
        configCacheScope = "optimistic";
        pageCacheScope = "optimistic";
        mcsCacheScope = CacheScopeConstant.CAN_CACHE_PAGE;

        //init the requested page
        initxdimeContentString();

        doTest("optimistic");

    }

    /**
     * test the page caching when the cache scope is configured as safe
     *
     * @throws ServletException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public void testSafe() throws ServletException, IOException,
            IllegalAccessException, InstantiationException,
            ClassNotFoundException {
        /**
         * define the configuration params
         */
        configCacheScope = "safe";
        pageCacheScope = "safe";
        mcsCacheScope = CacheScopeConstant.CAN_CACHE_PAGE;

        //init the requested page
        initxdimeContentString();

        doTest("safe");
    }

    /**
     * test the page caching when no scope has been specified by the page but
     * has been set to optimistic by the mcs-config
     * @throws IOException
     * @throws ServletException
     */
    public void testOptimisticUsingNoPageCacheScope() throws IOException,
            ServletException {
        /**
         * define the configuration params
         */
        configCacheScope = "optimistic";
        mcsCacheScope = CacheScopeConstant.CAN_CACHE_PAGE;

        //init the requested page
        initxdimeContentString();

        doTest("optimistic");
    }

    /**
     * test the page caching when no scope has been specified by the page but
     * has been set to safe by the mcs-config
     * @throws IOException
     * @throws ServletException
     */
    public void testSafeUsingNoPageCacheScope() throws IOException,
            ServletException {
        /**
         * define the configuration params
         */
        configCacheScope = "safe";
        mcsCacheScope = CacheScopeConstant.CAN_CACHE_PAGE;

        //init the requested page
        initxdimeContentString();

        doTest("safe");
    }

    /**
     * test that the caching scope specified in the page supersedes the value
     * specified in the config file
     * @throws IOException
     * @throws ServletException
     */
    public void testCacheScopePresedence() throws IOException,
            ServletException {
        /**
         * define the configuration params
         */
        pageCacheScope = "optimistic";
        configCacheScope = "none";
        mcsCacheScope = CacheScopeConstant.CAN_CACHE_PAGE;

        //init the requested page
        initxdimeContentString();

        doTest("optimistic");
    }

    /**
     * test the page caching is not used when the scope specified none
     * @throws IOException
     * @throws ServletException
     */
    public void testNone() throws IOException,
            ServletException {
        /**
         * define the configuration params
         */
        pageCacheScope = "none";
        configCacheScope = "none";
        mcsCacheScope = CacheScopeConstant.CAN_CACHE_PAGE;

        //init the requested page
        initxdimeContentString();

        doTest("none");
    }

    /**
     * test the page caching when the scope specifies optimistic but mcs
     * does not allow the caching. The expected result of this is that the
     * first request appears to run twice.
     * @throws IOException
     * @throws ServletException
     */
    public void testOptimisticWithMCSNotCaching() throws IOException,
            ServletException {
        /**
         * define the configuration params
         */
        pageCacheScope = "optimistic";
        configCacheScope = "optimistic";
        mcsCacheScope = CacheScopeConstant.CAN_NOT_CACHE_PAGE;

        //init the requested page
        initxdimeContentString();

        servletContext =
                new ServletContextMock("servletContext", expectations);

        /**
         * send the first request
         */
        CachingXDIMERequestProcessor requestProcessor =
                firstRequest(false, "optimistic", null);

        // first request releases the MarinerServletRequestContext, so the
        // request processor helper gets a fresh one to return on
        // createServletRequestContext
        requestProcessorHelperStub.setServletRequestContext(
            createServletRequestContext());

        firstRequest(false, "optimistic", requestProcessor);
    }
}
