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
package com.volantis.xml.pipeline.sax.drivers.web;

import java.util.ArrayList;
import java.util.List;

import junitx.util.PrivateAccessor;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.commons.httpclient.HttpMethod;

/**
 * Unit test for the {@link HTTPClientPluggableHTTPManager#createGetMethod} method
 */
public class HTTPClientPluggableHTTPManagerGetMethodTestCase extends TestCaseAbstract {


    /**
     * Tests that the creating of the get method works fine with various parameter values.
     * @throws Throwable
     */
    public void testExpectedUsage() throws Throwable {
        List requestParameters = new ArrayList();
        HttpMethod httpMethod = null;
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param1","value1a",null));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param1","value1b",null));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param2","value2","query"));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param3","value3","query"));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param4","value4","default"));

        String url = "http://www.website.com:8080/srt/bap/test/get-asset/540.0/383.0/-/-/1609077505/test.jpeg";

        HTTPClientPluggableHTTPManager manager = new HTTPClientPluggableHTTPManager();

        String method = "createGetMethod";
        Class paramTypes [] = {String.class,List.class};
        Object args [] = {url,requestParameters};

        httpMethod = (HttpMethod) PrivateAccessor.invoke(manager,
                                                     method,
                                                     paramTypes,
                                                     args);

        assertNotNull(httpMethod);
        assertEquals("Query string should equal:", "param1=value1a&param1=value1b&param2=value2&param3=value3&param4=value4", httpMethod.getQueryString());

    }

    /**
     * Tests that the creating of the get method fails when parameter target is invalid.
     * @throws Throwable
     */
    public void testBodyTargetParameterFails() throws Throwable {
        List requestParameters = new ArrayList();
        HttpMethod httpMethod = null;
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param1","value1a",null));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param1","value1b",null));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param2","value2","query"));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param3","value3","query"));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param4","value4","default"));
        requestParameters.add(HTTPWebDriverHelper.createWebRequestParameter("param5","value5","body"));

        String url = "http://www.website.com:8080/srt/bap/test/get-asset/540.0/383.0/-/-/1609077505/test.jpeg";

        HTTPClientPluggableHTTPManager manager = new HTTPClientPluggableHTTPManager();

        String method = "createGetMethod";
        Class paramTypes [] = {String.class,List.class};
        Object args [] = {url,requestParameters};

        try {
                httpMethod = (HttpMethod) PrivateAccessor.invoke(manager,
                                method,
                                paramTypes,
                                args);
                fail("Must throw an exception becasue of the invalid target value on GET method");
        } catch (HTTPException e) {

        }

    }

}
