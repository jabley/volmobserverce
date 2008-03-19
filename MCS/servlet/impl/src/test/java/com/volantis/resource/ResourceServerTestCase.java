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
package com.volantis.resource;

import com.volantis.mcs.runtime.ExternalPathToInternalURLMapperMock;
import com.volantis.mcs.servlet.ByteArrayServletOutputStream;
import com.volantis.mcs.servlet.ServletResourceMapperMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.javax.servlet.ServletConfigMock;
import mock.javax.servlet.ServletContextMock;
import mock.javax.servlet.ServletHelper;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;

/**
 * Test Case for ResourceServer
 */
public class ResourceServerTestCase extends TestCaseAbstract {

    private ResourceServer server;

    /**
     * mock servlet context object
     */
    private ServletContextMock servletContextMock;

    /**
     * mock request object
     */
    private HttpServletRequestMock httpServletRequestMock;

    /**
     * mock responce object
     */
    private HttpServletResponseMock httpServletResponseMock;

    private ServletResourceMapperMock resourceMapperMock;
    private ExternalPathToInternalURLMapperMock urlMapperMock;

    /**
     * set up method
     */
    public void setUp() throws Exception {

        super.setUp();

        final ServletConfigMock servletConfigMock =
            new ServletConfigMock("servletConfigMock", expectations);

        servletContextMock =
                new ServletContextMock("servletContextMock", expectations);


        httpServletRequestMock =
                new HttpServletRequestMock("httpServletRequestMock",
                        expectations);

        httpServletResponseMock =
                new HttpServletResponseMock("httpServletResponseMock",
                        expectations);

        //
        // Setup a usable servlet environment using mocks.
        //
        ServletHelper.setExpectedResults(expectations,
                servletConfigMock,
                servletContextMock,
                ResourceServer.class);

        urlMapperMock = new ExternalPathToInternalURLMapperMock(
                "urlMapperMock", expectations);

        resourceMapperMock = new ServletResourceMapperMock(
                "resourceMapperMock", expectations);

        server = new ResourceServer();
        server.init(servletConfigMock, urlMapperMock, resourceMapperMock);
    }

    /**
     * Test that if a resource which can be remapped (remapping means resolving
     * any references within the resource that are relative to absolute
     * references) is requested from the resource server, then it correctly
     * remaps it and returns the modified resource file.
     *
     * @throws ServletException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testValidRemappableObject() throws ServletException,
            IOException, ParserConfigurationException, SAXException {

        ByteArrayServletOutputStream os = new ByteArrayServletOutputStream();

        String expected ="<TEST/>";

        doTestRetrievePolicy(os, "image.mimg");

        String result = os.toString("UTF-8");

        // @todo later uncomment this - couldn't find XMLUnit on committal...
        assertXMLEquals("Image asset received did not match that expected",
                expected, result.trim());
    }

    private void doTestRetrievePolicy(
            ServletOutputStream os, final String projectRelativePath)
            throws ServletException, IOException {

        // Set the expectations for the request.

        // http://localServer:8080/volantis/resource/" +
        httpServletRequestMock.expects.getScheme().returns("http").any();
        httpServletRequestMock.expects.getServerName().returns("localServer").any();
        httpServletRequestMock.expects.getServerPort().returns(8080).any();

        httpServletRequestMock.expects.getServletPath().returns("").any();

        httpServletRequestMock.expects.getContextPath()
                .returns("/volantis/").any();

        httpServletRequestMock.expects.getPathInfo().returns(
                "resource/" +
                projectRelativePath).atLeast(1);

        httpServletRequestMock.expects.getHeader("x-mcs-project-config")
                .returns(null);

        // Create test objects.

        URL projectFile = getClassRelativeResourceURL("mcs-project.xml");
        String projectFileAsString = projectFile.toExternalForm();
        String projectDirectory = projectFileAsString.substring(0,
                projectFileAsString.lastIndexOf('/') + 1);

        URL imageFile = new URL(projectFile,
                "relative/" + projectRelativePath);

        resourceMapperMock.expects
                .getLocalURL("/resource/" + projectRelativePath)
                .returns(imageFile.toExternalForm());

        urlMapperMock.expects
                .mapInternalURLToExternalPath(projectDirectory)
                .returns("/resource/");

        httpServletResponseMock.expects.setHeader("x-mcs-project-config",
                "http://localServer:8080/volantis/resource/");

        if (os != null) {
            httpServletResponseMock.expects.setContentType("text/xml");
            httpServletResponseMock.expects.getOutputStream().returns(os);
        }

        server.doGet(httpServletRequestMock, httpServletResponseMock);
    }

    /**
     * Test that the resource server returns the correct error if the requested
     * resource is not avaliable.
     *
     * @throws ServletException if there was a problem running the test
     * @throws IOException if there was a problem running the test
     */
    public void testInvalidRemappableObject() throws Exception {

        httpServletResponseMock.expects.sendError(404);

        doTestRetrievePolicy(null, "image2.mimg");
    }

    /**
     * Test that the content types are handled properly.
     */
    public void testDetermineContentType() {

        String contentType =
                server.determineContentType("resources/test/image.mimg");
        assertEquals("text/xml", contentType);

        contentType = server.determineContentType("resources/test/test.xdime");
        assertEquals("x-application/vnd.xdime+xml" , contentType);

        servletContextMock.expects.getMimeType("resources/test/image.unknown")
                .returns("image/unknown");

        contentType =
                server.determineContentType("resources/test/image.unknown");
        assertEquals("image/unknown", contentType);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Nov-05	9990/1	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 ===========================================================================
*/
