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
* $Id: ServletUnitClient.java,v 1.1 2002/11/08 12:29:47 sfound Exp $
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
import com.meterware.httpunit.*;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

import java.net.MalformedURLException;

import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;

import javax.servlet.http.Cookie;

import org.xml.sax.SAXException;

/**
 * A client for use with the servlet runner class, allowing the testing of servlets
 * without an actual servlet container. Testing can be done in one of two ways.
 * End-to-end testing works much like the HttpUnit package, except that only servlets
 * actually registered with the ServletRunner will be invoked.  It is also possible
 * to test servlets 'from the inside' by creating a ServletInvocationContext and then
 * calling any servlet methods which may be desired.  Even in this latter mode, end-to-end
 * testing is supported, but requires a call to this class's getResponse method to update
 * its cookies and frames.
 **/
public class ServletUnitClient extends WebClient {


    /**
     * Creates and returns a new servlet unit client instance.
     **/
    public static ServletUnitClient newClient( InvocationContextFactory factory ) {
        return new ServletUnitClient( factory );
    }


    /**
     * Creates and returns a new invocation context from a GET request.
     **/
    public InvocationContext newInvocation( String requestString ) throws IOException, MalformedURLException {
        return newInvocation( new GetMethodWebRequest( requestString ) );
    }


    /**
     * Creates and returns a new invocation context to test calling of servlet methods.
     **/
    public InvocationContext newInvocation( WebRequest request ) throws IOException, MalformedURLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeMessageBody( request, baos );
        return _invocationContextFactory.newInvocation( this, request, getCookies(), this.getHeaderFields(), baos.toByteArray() );
    }


    /**
     * Updates this client and returns the response which would be displayed by the
     * user agent. Note that this will typically be the same as that returned by the
     * servlet invocation unless that invocation results in a redirect request.
     **/
    public WebResponse getResponse( InvocationContext invocation ) throws MalformedURLException,IOException,SAXException {
        updateClient( invocation.getServletResponse() );
        return getFrameContents( invocation.getTarget() );
    }


//-------------------------------- WebClient methods --------------------------------------


    /**
     * Creates a web response object which represents the response to the specified web request.
     **/
    protected WebResponse newResponse( WebRequest request ) throws MalformedURLException,IOException {

        try {
            InvocationContext invocation = newInvocation( request );
            invocation.getServlet().service( invocation.getRequest(), invocation.getResponse() );
            return invocation.getServletResponse();
        } catch (ServletException e) {
            throw new HttpInternalErrorException( request.getURL(), e );
        }

    }


//-------------------------- private members -----------------------------------


    private InvocationContextFactory _invocationContextFactory;

    final private static Cookie[] NO_COOKIES = new Cookie[0];


//--------------------------------- package methods ---------------------------------------


    private ServletUnitClient( InvocationContextFactory factory ) {
        _invocationContextFactory = factory;
    }


    private Cookie[] getCookies() {
        String cookieHeader = (String) getHeaderFields().get( "Cookie" );
        if (cookieHeader == null) return NO_COOKIES;
        Vector cookies = new Vector();

        StringTokenizer st = new StringTokenizer( cookieHeader, "=;" );
        while (st.hasMoreTokens()) {
            String name = st.nextToken();
            if (st.hasMoreTokens()) {
                String value = st.nextToken();
                cookies.addElement( new Cookie( name, value ) );
            }
        }
        Cookie[] results = new Cookie[ cookies.size() ];
        cookies.copyInto( results );
        return results;
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
