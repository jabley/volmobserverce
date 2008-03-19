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
package com.volantis.synergetics.testtools.servletunit;
/********************************************************************************************************************
* $Id: ServletRunner.java,v 1.1 2002/11/08 12:29:47 sfound Exp $
*
* Copyright (c) 2000-2002, Russell Gold
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
* documentation files (the "Software"), to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
* to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions
* of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
* THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*******************************************************************************************************************/
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.http.Cookie;
import javax.xml.parsers.DocumentBuilder;

import com.meterware.httpunit.HttpUnitUtils;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;


/**
 * This class acts as a test environment for servlets.
 **/
public class ServletRunner {

    /**
     * Entity resolver that resolves the web-app_2_3.dtd, so it doesn't need to
     * go to the web and fetch it from there.
     */
    private static final EntityResolver WEB_XML_RESOLVER =
        new WebXmlEntityResolver();

    /**
     * Default constructor, which defines no servlets.
     */
    public ServletRunner() {
        _application = new WebApplication();
        completeInitialization( null );
    }

    /**
     * Constructor which expects the full path to the web.xml for the 
     * application.
     *
     * @param webXMLFileSpec the full path to the web.xml file
     */
    public ServletRunner( String webXMLFileSpec ) throws IOException, SAXException {
        final DocumentBuilder documentBuilder = HttpUnitUtils.newParser();
        documentBuilder.setEntityResolver(WEB_XML_RESOLVER);
        _application = new WebApplication( documentBuilder.parse( webXMLFileSpec ) );
        completeInitialization( null );
    }


    /**
     * Constructor which expects the full path to the web.xml for the 
     * application and a context path under which to mount it.
     *
     * @param webXMLFileSpec the full path to the web.xml file
     * @param contextPath the context path
     */
    public ServletRunner( String webXMLFileSpec, String contextPath ) throws IOException, SAXException {
        File webXMLFile = new File( webXMLFileSpec );
        final DocumentBuilder documentBuilder = HttpUnitUtils.newParser();
        documentBuilder.setEntityResolver(WEB_XML_RESOLVER);
        _application = new WebApplication( documentBuilder.parse( webXMLFileSpec ), webXMLFile.getParentFile().getParentFile(), contextPath );
        completeInitialization( contextPath );
    }


    /**
     * Constructor which expects an input stream containing the web.xml for the application.
     **/
    public ServletRunner( InputStream webXML ) throws IOException, SAXException {
        this( webXML, null );
    }

    /**
     * Constructor which expects an input stream containing the web.xml for the application.
     **/
    public ServletRunner( InputStream webXML, String contextPath ) throws IOException, SAXException {
        final DocumentBuilder documentBuilder = HttpUnitUtils.newParser();
        documentBuilder.setEntityResolver(WEB_XML_RESOLVER);
        _application = new WebApplication( documentBuilder.parse( new InputSource( webXML ) ), contextPath );
        completeInitialization( contextPath );
    }


    public void setRealPath( String path ) {
        _application.setRealPath( path );
    }

    public String getRealPath() {
        return _application.getRealPath();
    }

    /**
     * Registers a servlet class to be run.
     **/
    public void registerServlet( String resourceName, String servletClassName ) {
        _application.registerServlet( resourceName, servletClassName, null );
    }


    private void completeInitialization( String contextPath ) {
        _context = new ServletUnitContext( contextPath );
        _application.registerServlet( "*.jsp", _jspServletDescriptor.getClassName(), _jspServletDescriptor.getInitializationParameters( null, null ) );
    }


    /**
     * Registers a servlet class to be run, specifying initialization parameters.
     **/
    public void registerServlet( String resourceName, String servletClassName, Hashtable initParameters ) {
        _application.registerServlet( resourceName, servletClassName, initParameters );
    }


    /**
     * Returns the response from the specified servlet.
     * @exception SAXException thrown if there is an error parsing the response
     **/
    public WebResponse getResponse( WebRequest request ) throws MalformedURLException, IOException, SAXException {
        return getClient().getResponse( request );
    }


    /**
     * Returns the response from the specified servlet using GET.
     * @exception SAXException thrown if there is an error parsing the response
     **/
    public WebResponse getResponse( String url ) throws MalformedURLException, IOException, SAXException {
        return getClient().getResponse( url );
    }


    /**
     * Creates and returns a new web client that communicates with this servlet runner.
     **/
    public ServletUnitClient newClient() {
        return ServletUnitClient.newClient( _factory );
    }


    public static class JasperJSPServletDescriptor implements JSPServletDescriptor {

        public String getClassName() {
            return "org.apache.jasper.servlet.JspServlet";
        }


        public Hashtable getInitializationParameters( String classPath, String workingDirectory ) {
            Hashtable params = new Hashtable();
            if (classPath != null) params.put( "classpath", classPath );
            if (workingDirectory != null) params.put( "scratchdir", workingDirectory );
            return params;
        }
    }


    public final static JSPServletDescriptor JASPER_DESCRIPTOR = new JasperJSPServletDescriptor();


//-------------------------------------------- package methods ---------------------------------------------------------


    ServletUnitContext getContext() {
        return _context;
    }


    WebApplication getApplication() {
        return _application;
    }


//---------------------------- private members ------------------------------------

    private static JSPServletDescriptor _jspServletDescriptor = JASPER_DESCRIPTOR;

    private WebApplication     _application;

    private ServletUnitClient  _client;

    private ServletUnitContext _context;

    private InvocationContextFactory _factory = new InvocationContextFactory() {
        public InvocationContext newInvocation( ServletUnitClient client, WebRequest request, Cookie[] clientCookies, Dictionary clientHeaders, byte[] messageBody ) throws IOException, MalformedURLException {
            return new InvocationContextImpl( client, ServletRunner.this, request, clientCookies, clientHeaders, messageBody );
        }
    };


    private ServletUnitClient getClient() {
        if (_client == null) _client = newClient();
        return _client;
    }

    private static final class WebXmlEntityResolver implements EntityResolver {
        public InputSource resolveEntity(final String publicId,
                                         final String systemId) {

            InputSource is = null;
            if ("http://java.sun.com/dtd/web-app_2_3.dtd".equals(systemId)) {
                is = new InputSource(getClass().getResourceAsStream(
                    "res/web-app_2_3.dtd"));
            }
            return is;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
