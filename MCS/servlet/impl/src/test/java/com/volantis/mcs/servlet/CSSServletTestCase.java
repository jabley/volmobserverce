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


import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.css.WritableCSSEntityMock;
import com.volantis.mcs.cache.css.CSSCacheEntry;
import com.volantis.mcs.cache.Cache;
import com.volantis.mcs.cache.CacheIdentity;
import com.volantis.mcs.application.ApplicationInternals;
import mock.javax.servlet.ServletConfigMock;
import mock.javax.servlet.ServletContextMock;
import mock.javax.servlet.ServletHelper;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;

import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;


public class CSSServletTestCase extends TestCaseAbstract {


     private ExpectationBuilder expectations;

     private Volantis volantis;

     private MarinerServletApplicationMock marinerApplicationMock;

     private ServletConfigMock servletConfigMock;

     private ServletContextMock servletContextMock;


     private HttpServletRequestMock httpServletRequestMock;

     private HttpServletResponseMock httpServletResponseMock;


    public void setUp() {
        //
         // SETUP
         //
         expectations =
                 mockFactory.createUnorderedBuilder();

         //
         // We need to create a Volantis bean to initialise the CSS cache. If no
         // configuration is supplied the cache defaults to a long flush cycle
         // (10 minutes). So unless the test case is running on an Abacus then
         // it should not be time critical.
         //
         volantis = new Volantis();

         marinerApplicationMock =
                 new MarinerServletApplicationMock("marinerApplication",
                         expectations);

         servletConfigMock =
                 new ServletConfigMock("servletConfigMock", expectations);

         servletContextMock =
                 new ServletContextMock("servletContextMock", expectations);


         httpServletRequestMock =
                 new HttpServletRequestMock("httpServletRequestMock",
                         expectations);

         httpServletResponseMock =
                 new HttpServletResponseMock("httpServletResponseMock",
                         expectations);
    }

    /**
     * This integration test, tests that an entry placed into the cache via
     * the Volantis bean can be retrieved using the returned key from the
     * CSSServlet and that the servlet writes the content out with the correct
     * content type.
     */
    public void testCSSServletWithValidKey() throws Exception {


        //
        // SETUP
        //
        WritableCSSEntityMock writableCSSEntityMock =
                new WritableCSSEntityMock("writableCSSEntityMock",
                        expectations);

        //
        // We need to initialise the cache and get the one and hopefully only
        // instance of it.
        //
        Cache cache = volantis.getCSSCache();

        //
        // Store our WritableCSSEntityMock in the cache and get the identity
        // that can be used to retrieve the entry.
        //
        CacheIdentity identity = cache.store(
                new CSSCacheEntry(writableCSSEntityMock));


        //
        // EXPECTATIONS
        //

        //
        // Setup a usable servlet environment using mocks.
        //
        ServletHelper.setExpectedResults(expectations,
                servletConfigMock,
                servletContextMock,
                CSSServlet.class);


        //
        // Create a writer for the HttpServletResponse to use
        //

        StringWriter stringWriter = new StringWriter();

        PrintWriter printWriter = new PrintWriter(stringWriter);

        //
        // We don't need a fully initialised Volantis bean so we can
        // return a MarinerServletApplicationMock.
        //
        servletContextMock
                .expects
                .getAttribute("marinerApplication")
                .returns(marinerApplicationMock);

        //
        // We need to register our Version of the Volantis bean against our
        // MarinerServketApplicationMock as we cannot overide the
        // getVolantisBean() method which is package protected.
        //

        ApplicationInternals.setVolantisBean(marinerApplicationMock, volantis);
//        marinerApplicationMock
//                .expects
//                .getVolantisBean()
//                .returns(volantis).any();

        //
        // For piece of mind we will get our WritableCSSEntityMock to actually
        // write something to its supplied writer. That way we know for sure
        // that the servlet works end to end.
        //
        writableCSSEntityMock.expects.write(printWriter).does(new MethodAction() {
            public Object perform(MethodActionEvent event) throws Throwable {
                Writer pw = (Writer) event.getArgument(Writer.class);
                pw.write("foo");
                return null;
            }
        });

        httpServletRequestMock
                .expects
                .getParameter("key")
                .returns(identity.getBase64KeyAsString());

        httpServletResponseMock
                .expects
                .getWriter()
                .returns(printWriter);

        httpServletResponseMock
                .expects
                .setContentType("text/css");

        httpServletResponseMock
                .fuzzy
                .addHeader(mockFactory.expectsAny(), mockFactory.expectsAny())
                .returns().any();

        httpServletResponseMock
                .fuzzy
                .setDateHeader(mockFactory.expectsAny(), mockFactory.expectsAny())
                .returns().any();
        
        CSSServlet cssServlet = new CSSServlet();


        cssServlet.init(servletConfigMock);

        cssServlet.doGet(httpServletRequestMock, httpServletResponseMock);

        assertEquals("foo", stringWriter.toString());
    }

    /**
     * This integration test, tests that a 404 response is returned from the
     * servlet if a cache key is not found.
     */
    public void testCSSServletWithMissingKey() throws Exception {


        //
        // SETUP
        //

        WritableCSSEntityMock writableCSSEntityMock =
                new WritableCSSEntityMock("writableCSSEntityMock",
                        expectations);

        //
        // We need to initialise the cache and get the one and hopefully only
        // instance of it.
        //
        Cache cache = volantis.getCSSCache();

        //
        // Store our WritableCSSEntityMock in the cache and get the identity
        // that can be used to retrieve the entry.
        //
        CacheIdentity identity = cache.store(
                new CSSCacheEntry(writableCSSEntityMock));
        //
        // Now remove all entries so we get a cache miss.
        //
        cache.clear();

        //
        // EXPECTATIONS
        //

        //
        // Setup a usable servlet environment using mocks.
        //
        ServletHelper.setExpectedResults(expectations,
                servletConfigMock,
                servletContextMock,
                CSSServlet.class);

        //
        // We don't need a fully initialised Volantis bean so we can
        // return a MarinerServletApplicationMock.
        //
        servletContextMock
                .expects
                .getAttribute("marinerApplication")
                .returns(marinerApplicationMock);

        //
        // We need to register our Version of the Volantis bean against our
        // MarinerServketApplicationMock as we cannot overide the
        // getVolantisBean() method which is package protected.
        //

        ApplicationInternals.setVolantisBean(marinerApplicationMock, volantis);
//        marinerApplicationMock
//                .expects
//                .getVolantisBean()
//                .returns(volantis).any();


        httpServletRequestMock
                .expects
                .getParameter("key")
                .returns(identity.getBase64KeyAsString());


        httpServletResponseMock.expects.sendError(404);


        CSSServlet cssServlet = new CSSServlet();


        cssServlet.init(servletConfigMock);

        cssServlet.doGet(httpServletRequestMock, httpServletResponseMock);

    }

    /**
     * This integration test, tests that an exception is thrown if the format
     * of the key used to retrieve a cache identity is invalid.
     */
    public void testCSSServletWithInvalidKey() throws Exception {



        //
        // EXPECTATIONS
        //

        //
        // Setup a usable servlet environment using mocks.
        //
        ServletHelper.setExpectedResults(expectations,
                servletConfigMock,
                servletContextMock,
                CSSServlet.class);


        //
        // Create a writer for the HttpServletResponse to use
        //

        StringWriter stringWriter = new StringWriter();

        PrintWriter printWriter = new PrintWriter(stringWriter);

        //
        // We don't need a fully initialised Volantis bean so we can
        // return a MarinerServletApplicationMock.
        //
        servletContextMock
                .expects
                .getAttribute("marinerApplication")
                .returns(marinerApplicationMock);

        //
        // We need to register our Version of the Volantis bean against our
        // MarinerServketApplicationMock as we cannot overide the
        // getVolantisBean() method which is package protected.
        //

        ApplicationInternals.setVolantisBean(marinerApplicationMock, volantis);
//        marinerApplicationMock
//                .expects
//                .getVolantisBean()
//                .returns(volantis).any();


        httpServletRequestMock
                .expects
                .getParameter("key")
                .returns("rubbish");

        CSSServlet cssServlet = new CSSServlet();


        cssServlet.init(servletConfigMock);

        boolean exceptionCaught = false;
        try {
            cssServlet.doGet(httpServletRequestMock, httpServletResponseMock);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),
                    "base64Key is not a valid encoding of createTime" +
                    ", expiresTime and sequenceNo");
            exceptionCaught = true;
        }
        assertEquals(exceptionCaught, true);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
