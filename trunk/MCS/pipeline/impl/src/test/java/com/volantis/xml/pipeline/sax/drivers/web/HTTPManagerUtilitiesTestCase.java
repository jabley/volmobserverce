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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import junitx.util.PrivateAccessor;

public class HTTPManagerUtilitiesTestCase extends TestCaseAbstract {


    /**
     * Test the addRequestCookiesToRequest method with cookies from the
     * context only.
     */
    public void testAddRequestCookiesToHttpMethodNoWebRequest()
            throws Throwable {
        HTTPFactory factory = HTTPFactory.getDefaultInstance();
        String name1 = "name1";
        String name2 = "name2";
        String value1a = "value1a";
        String value1b = "value1b";
        String value2 = "value2";

        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
        WebRequestCookie cookie1 = new WebRequestCookie();
        cookie1.setName(name1);
        cookie1.setValue(value1a);
        entities.add(cookie1);

        WebRequestCookie cookie2 = new WebRequestCookie();
        cookie2.setName(name1);
        cookie2.setValue(value1b);
        entities.add(cookie2);

        WebRequestCookie cookie3 = new WebRequestCookie();
        cookie3.setName(name2);
        cookie3.setValue(value2);
        entities.add(cookie3);


        AbstractPluggableHTTPManagerTestAbstract.MockXMLPipelineContext
                context = new AbstractPluggableHTTPManagerTestAbstract.MockXMLPipelineContext();
        context.setProperty(WebRequestCookie.class, entities, false);

        HTTPMessageEntities cookieJar =
                HTTPManagerUtilities.retrieveRequestCookies(null, context);


        assertEquals("There should be 3 cookies", 3, cookieJar.size());

        assertEquals("Checking cookie 1",
                     cookie1,
                     cookieJar.retrieve(cookie1.getIdentity())[0]);
        assertEquals("Checking cookie 2",
                     cookie2,
                     cookieJar.retrieve(cookie2.getIdentity())[1]);
        assertEquals("Checking cookie 3",
                     cookie3,
                     cookieJar.retrieve(cookie3.getIdentity())[0]);
    }



    /**
     * Test the addRequestCookiesToRequest method with a WebDriverRequest
     * that contains cookies that are referenced from the markup but where the
     * markup cookies do not overlap with the WebDriverRequest cookies.
     */
    public void testAddRequestCookiesToHttpMethodWithWebRequest()
            throws Throwable {
        HTTPFactory factory = HTTPFactory.getDefaultInstance();
        String name1 = "name1";
        String name2 = "name2";
        String value1 = "value1";
        String value2 = "value2";

        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
        WebRequestCookie cookie1 = new WebRequestCookie();
        cookie1.setName(name1);
        cookie1.setFrom(name1);
        entities.add(cookie1);

        WebRequestCookie cookie2 = new WebRequestCookie();
        cookie2.setName(name2);
        cookie2.setFrom(name2);
        entities.add(cookie2);

        AbstractPluggableHTTPManagerTestAbstract.MockXMLPipelineContext context =
                new AbstractPluggableHTTPManagerTestAbstract.MockXMLPipelineContext();
        context.setProperty(WebRequestCookie.class, entities, false);

        WebDriverRequest request = new WebDriverRequestImpl();
     //   WebDriverAccessor accessor = createWebDriverAccessor(request, null);

    //    context.setProperty(WebDriverAccessor.class, accessor, false);

        HTTPMessageEntities requestEntities =
                factory.createHTTPMessageEntities();
        request.setCookies(requestEntities);

        WebRequestCookie cookie3 = new WebRequestCookie();
        cookie3.setName(name1);
        cookie3.setValue(value1);
        requestEntities.add(cookie3);

        WebRequestCookie cookie4 = new WebRequestCookie();
        cookie4.setName(name2);
        cookie4.setValue(value2);
        requestEntities.add(cookie4);

        HTTPMessageEntities cookieJar = HTTPManagerUtilities.
                retrieveRequestCookies(request, context);


        assertEquals("There should be 2 cookies", 2, cookieJar.size());

        assertEquals("Checking cookie 1",
                     cookie3,
                     cookieJar.retrieve(cookie3.getIdentity())[0]);
        assertEquals("Checking cookie 2",
                     cookie4,
                     cookieJar.retrieve(cookie4.getIdentity())[0]);
    }

    /**
     * Test the addRequestHeadersToRequest method with headers from the
     * context only.
     */
    public void testAddRequestHeadersToHttpMethodNoWebRequest()
            throws Throwable {
        HTTPFactory factory = HTTPFactory.getDefaultInstance();
        String name1 = "name1";
        String name2 = "name2";
        String value1a = "value1a";
        String value1b = "value1b";
        String value2 = "value2";

        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
        WebRequestHeader header1 = new WebRequestHeader();
        header1.setName(name1);
        header1.setValue(value1a);
        entities.add(header1);

        WebRequestHeader header2 = new WebRequestHeader();
        header2.setName(name1);
        header2.setValue(value1b);
        entities.add(header2);

        WebRequestHeader header3 = new WebRequestHeader();
        header3.setName(name2);
        header3.setValue(value2);
        entities.add(header3);

        AbstractPluggableHTTPManagerTestAbstract.MockXMLPipelineContext context =
                new AbstractPluggableHTTPManagerTestAbstract.MockXMLPipelineContext();
        context.setProperty(WebRequestHeader.class, entities, false);

        HTTPMessageEntities headers =
                HTTPManagerUtilities.retrieveRequestHeaders(null, context);

        assertEquals("There should be 3 headers", 3, headers.size());
        assertEquals("checking header 1",
                     header1,
                     headers.retrieve(header1.getIdentity())[0]);
        assertEquals("checking header 2",
                     header2,
                     headers.retrieve(header2.getIdentity())[1]);
        assertEquals("checking header 3",
                     header3,
                     headers.retrieve(header3.getIdentity())[0]);
    }

    /**
     * Test the addRequestHeadersToRequest method with a WebDriverRequest
     * that contains headers that are referenced from the markup but where the
     * markup headers do not overlap with the WebDriverRequest headers.
     */
    public void testAddRequestHeadersToHttpMethodWithWebRequest()
            throws Throwable {
        HTTPFactory factory = HTTPFactory.getDefaultInstance();
        String name1 = "name1";
        String name2 = "name2";
        String value1a = "value1a";
        String value1b = "value1b";
        String value2 = "value2";

        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
        WebRequestHeader header1 = new WebRequestHeader();
        header1.setName(name1);
        header1.setFrom(name1);
        entities.add(header1);

        WebRequestHeader header2 = new WebRequestHeader();
        header2.setName(name1);
        header2.setFrom(name1);
        entities.add(header2);

        WebRequestHeader header3 = new WebRequestHeader();
        header3.setName(name2);
        header3.setFrom(name2);
        entities.add(header3);

 //       PluggableHTTPManager manager = createTestablePluggableHTTPManager();
        AbstractPluggableHTTPManagerTestAbstract.MockXMLPipelineContext context =
                new AbstractPluggableHTTPManagerTestAbstract.MockXMLPipelineContext();
        context.setProperty(WebRequestHeader.class, entities, false);
  //      WebDriverConfiguration config = createTestWebDriverConfig();

        WebDriverRequest request = new WebDriverRequestImpl();
  //      WebDriverAccessor accessor = createWebDriverAccessor(request, null);

   //     context.setProperty(WebDriverAccessor.class, accessor, false);
  //      manager.initialize(config);

        HTTPMessageEntities requestEntities =
                factory.createHTTPMessageEntities();
        request.setHeaders(requestEntities);

        WebRequestHeader header4 = new WebRequestHeader();
        header4.setName(name1);
        header4.setValue(value1a);
        requestEntities.add(header4);


        WebRequestHeader header5 = new WebRequestHeader();
        header5.setName(name1);
        header5.setValue(value1b);
        requestEntities.add(header5);

        WebRequestHeader header6 = new WebRequestHeader();
        header6.setName(name2);
        header6.setValue(value2);
        requestEntities.add(header6);

        HTTPMessageEntities headers =
                HTTPManagerUtilities.retrieveRequestHeaders(request, context);

        assertEquals("There should be 3 headers", 3, headers.size());
        assertEquals("checking header 1",
                     header4,
                     headers.retrieve(header4.getIdentity())[0]);
        assertEquals("checking header 2",
                     header5,
                     headers.retrieve(header5.getIdentity())[1]);
        assertEquals("checking header 3",
                     header6,
                     headers.retrieve(header6.getIdentity())[0]);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Mar-05	7443/1	matthew	VBM:2005031708 refactor AbstractPluggableHTTPManager

 ===========================================================================
*/
