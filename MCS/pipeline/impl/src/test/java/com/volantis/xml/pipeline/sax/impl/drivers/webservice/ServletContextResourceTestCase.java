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

package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import mock.javax.servlet.ServletContextMock;

import org.xml.sax.InputSource;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.volantis.xml.pipeline.sax.drivers.webservice.ServletContextResource;

public class ServletContextResourceTestCase
        extends WSDLResourceTestAbstract {

    public void testProvideInputSource() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ServletContextMock servletContextMock =
                new ServletContextMock("servletContextMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getProperty(ServletContext.class)
                .returns(servletContextMock).any();

        servletContextMock.expects.getResource("/fred.jsp")
                .returns(ServletContextResourceTestCase.class
                .getResource("ServletContextResourceTestCaseContent.txt"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        ServletContextResource resource =
                new ServletContextResource("/fred.jsp");
        InputSource source = resource.provideInputSource(contextMock);

        InputStream is = source.getByteStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String input = null;
        StringBuffer actualResult = new StringBuffer();
        while ((input = br.readLine()) != null) {
            actualResult.append(input);
        }

        assertEquals("Some text in a resource file", actualResult.toString());
    }
}
