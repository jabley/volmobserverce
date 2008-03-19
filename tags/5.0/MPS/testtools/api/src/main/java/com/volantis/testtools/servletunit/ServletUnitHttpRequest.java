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
package com.volantis.testtools.servletunit;
/********************************************************************************************************************
* $Id: ServletUnitHttpRequest.java,v 1.1 2002/12/10 09:50:49 sumit Exp $
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
import com.meterware.httpunit.HttpUnitUtils;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebClient;
import com.meterware.httpunit.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.Dictionary;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletInputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * This class represents a servlet request created from a WebRequest.
 **/
class ServletUnitHttpRequest implements HttpServletRequest {

    private ServletInputStreamImpl _inputStream;
    private String                 _contentType;
    private Vector                 _headerNames;


    /**
     * Constructs a ServletUnitHttpRequest from a WebRequest object.
     **/
    ServletUnitHttpRequest( ServletRequest servletRequest, WebRequest request, 
                            ServletUnitContext context, Dictionary clientHeaders, 
                            byte[] messageBody ) throws MalformedURLException {
        _servletRequest = servletRequest;
        _request = request;
        _context = context;
        _headers = new WebClient.HeaderDictionary();
        _headers.addEntries( clientHeaders );
        _headers.addEntries( request.getHeaders() );
        _headerNames = new Vector();
        
        Enumeration en = clientHeaders.keys();
        while( en.hasMoreElements() ) {
            _headerNames.add( en.nextElement() );
        }
        
        en = request.getHeaders().keys();
        while( en.hasMoreElements() ) {
            _headerNames.add( en.nextElement() );
        }
        
        _messageBody = messageBody;
        _contentType = (String) _headers.get( "Content-Type" );
        if (context == null) throw new IllegalArgumentException( "Context must not be null" );

        String file = request.getURL().getFile();
        if (file.indexOf( '?' ) >= 0) loadParameters( file.substring( file.indexOf( '?' )+1 ) );
        if (_messageBody == null) return;
        if (_contentType == null || _contentType.indexOf( "x-www-form-urlencoded" ) >= 0 ) {
            loadParameters( new String( _messageBody ) );
        }
    }


//----------------------------------------- HttpServletRequest methods --------------------------


    /**
     * Returns the name of the authentication scheme used to protect the servlet, for example, "BASIC" or "SSL,"
     * or null if the servlet was not protected.
     **/
    public String getAuthType() {
        return null;
    }


    /**
     * Returns the query string that is contained in the request URL after the path.
     **/
    public String getQueryString() {
        return _request.getQueryString();
    }


    /**
     * Returns an array containing all of the Cookie objects the client sent with this request.
     * This method returns null if no cookies were sent.
     **/
    public Cookie[] getCookies() {
        if (_cookies.size() == 0) {
            return null;
        } else {
            Cookie[] result = new Cookie[ _cookies.size() ];
            _cookies.copyInto( result );
            return result;
        }
    }


    /**
     * Returns the value of the specified request header as an int. If the request does not have a header
     * of the specified name, this method returns -1. If the header cannot be converted to an integer,
     * this method throws a NumberFormatException.
     **/
    public int getIntHeader( String name ) {
        return -1;
    }


    /**
     * Returns the value of the specified request header as a long value that represents a Date object.
     * Use this method with headers that contain dates, such as If-Modified-Since.
     * <br>
     * The date is returned as the number of milliseconds since January 1, 1970 GMT. The header name is case insensitive.
     *
     * If the request did not have a header of the specified name, this method returns -1.
     * If the header can't be converted to a date, the method throws an IllegalArgumentException.
     **/
    public long getDateHeader( String name ) {
        return -1;
    }


    /**
     * Returns the value of the specified request header as a String. If the request did not include
     * a header of the specified name, this method returns null. The header name is case insensitive.
     * You can use this method with any request header.
     **/
    public String getHeader( String name ) {
        return (String) _headers.get( name );
    }


    /**
     * Returns an enumeration of all the header names this request contains. If the request has no headers,
     * this method returns an empty enumeration.
     *
     * Some servlet containers do not allow do not allow servlets to access headers using this method,
     * in which case this method returns null.
     **/
    public Enumeration getHeaderNames() {
        return _headerNames.elements();
    }


    /**
     * Returns the part of this request's URL that calls the servlet. This includes either the servlet name
     * or a path to the servlet, but does not include any extra path information or a query string.
     **/
    public String getServletPath() {
        return _servletRequest.getServletPath();
    }


    /**
     * Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT.
     **/
    public String getMethod() {
        return _request.getMethod();
    }


    /**
     * Returns any extra path information associated with the URL the client sent when it made this request.
     * The extra path information follows the servlet path but precedes the query string.
     * This method returns null if there was no extra path information.
     **/
    public String getPathInfo() {
        return _servletRequest.getPathInfo();
    }


    /**
     * Returns any extra path information after the servlet name but before the query string,
     * and translates it to a real path. If the URL does not have any extra path information, this method returns null.
     **/
    public String getPathTranslated() {
        HttpServlet servlet = null;
        String path = getPathInfo();
        if( path == null ) {
            return null;
        }
        try {
            servlet = (HttpServlet)_servletRequest.getServlet();
        } 
        catch( ServletException se ) {
            return null;
        }
        try {
            URL url = servlet.getServletContext().getResource( path );
            return url.getFile();
        } catch (MalformedURLException e) {
            return null;
        }
    }


    /**
     * Checks whether the requested session ID came in as a cookie.
     **/
    public boolean isRequestedSessionIdFromCookie() {
        return _sessionID != null;
    }


    /**
     * Returns the login of the user making this request, if the user has been authenticated,
     * or null if the user has not been authenticated.
     * Whether the user name is sent with each subsequent request depends on the browser and type of authentication.
     **/
    public String getRemoteUser() {
        return _userName;
    }


    /**
     * Returns the session ID specified by the client. This may not be the same as the ID of the actual session in use.
     * For example, if the request specified an old (expired) session ID and the server has started a new session,
     * this method gets a new session with a new ID. If the request did not specify a session ID, this method returns null.
     **/
    public String getRequestedSessionId() {
        return _sessionID;
    }


    /**
     * Returns the part of this request's URL from the protocol name up to the query string in the first line of the HTTP request.
     **/
    public String getRequestURI() {
        try {
            return _request.getURL().getPath();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Returns the current HttpSession associated with this request or, if there is no current session
     * and create is true, returns a new session.
     * <br>
     * If create is false and the request has no valid HttpSession, this method returns null.
     **/
    public HttpSession getSession( boolean create ) {
        if (_session == null && getRequestedSessionId() != null) {
            _session = _context.getSession( getRequestedSessionId() );
        }
        if (_session == null && create) {
            _session = _context.newSession();
        }
        return _session;
    }


    /**
     * Returns the current session associated with this request, or if the request does not have a session, creates one.
     **/
    public HttpSession getSession() {
        return getSession( true );
    }


    /**
     * Checks whether the requested session ID is still valid.
     **/
    public boolean isRequestedSessionIdValid() {
        return false;
    }


    /**
     * Checks whether the requested session ID came in as part of the request URL.
     **/
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }


    /**
     * @deprecated use #isRequestedSessionIdFromURL
     **/
    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }


//--------------------------------- ServletRequest methods ----------------------------------------------------


    /**
     * Returns the length, in bytes, of the content contained in the
     * request and sent by way of the input stream or -1 if the
     * length is not known.
     **/
    public int getContentLength() {
        return -1;
    }


    /**
     *
     * Returns the value of the named attribute as an <code>Object</code>.
     * This method allows the servlet engine to give the servlet
     * custom information about a request. This method returns
     * <code>null</code> if no attribute of the given name exists.
     **/
    public Object getAttribute( String name ) {
        return _attributes.get( name );
    }


    /**
     * Returns an <code>Enumeration</code> containing the
     * names of the attributes available to this request.
     * This method returns an empty <code>Enumeration</code>
     * if the request has no attributes available to it.
     **/
    public Enumeration getAttributeNames() {
        return _attributes.keys();
    }


    /**
     * Retrieves binary data from the body of the request as
     * a {@link ServletInputStream}, which
     * gives you the ability to read one line at a time.
     *
     * @return					a {@link ServletInputStream} object containing
     * 						the body of the request
     *
     * @exception IllegalStateException   	if the {@link #getReader} method
     * 						has already been called for this request
     *
     * @exception IOException    		if an input or output exception occurred
     *
     */
    public ServletInputStream getInputStream() throws IOException {
        if (_inputStream == null) {
            _inputStream = new ServletInputStreamImpl( _messageBody );
        }
        return _inputStream;
    }


    /**
     * Returns the name of the character encoding style used in this
     * request. This method returns <code>null</code> if the request
     * does not use character encoding.
     **/
    public String getCharacterEncoding() {
        return null;
    }


    /**
     *
     * Returns an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the parameters contained
     * in this request. If the request has
     * no parameters or if the input stream is empty, returns an
     * empty <code>Enumeration</code>. The input stream is empty
     * when all the data returned by {@link #getInputStream} has
     * been read.
     **/
    public Enumeration getParameterNames() {
        return _parameters.keys();
    }


    /**
     * Returns the MIME type of the content of the request, or
     * <code>null</code> if the type is not known. Same as the value
     * of the CGI variable CONTENT_TYPE.
     **/
    public String getContentType() {
        return _contentType;
    }


    /**
     * Returns the value of a request parameter as a <code>String</code>,
     * or <code>null</code> if the parameter does not exist. Request parameters
     * are extra information sent with the request.
     **/
    public String getParameter( String name ) {
        String[] parameters = getParameterValues( name );
        return parameters == null ? null : parameters[0];
    }


    /**
     * Returns an array of <code>String</code> objects containing
     * all of the values the
     * given request parameter has, or <code>null</code> if the
     * parameter does not exist. For example, in an HTTP servlet,
     * this method returns an array of <code>String</code> objects
     * containing the values of a query string or posted form.
     **/
    public String[] getParameterValues( String name ) {
        return (String[]) _parameters.get( name );
    }


    /**
     * Returns the name and version of the protocol the request uses
     * in the form <i>protocol/majorVersion.minorVersion</i>, for
     * example, HTTP/1.1.
     **/
    public String getProtocol() {
        return "HTTP/1.1";
    }


    /**
     * Returns the name of the scheme used to make this request,
     * for example,
     * <code>http</code>, <code>https</code>, or <code>ftp</code>.
     * Different schemes have different rules for constructing URLs,
     * as noted in RFC 1738.
     **/
    public String getScheme() {
        return "http";
    }


    /**
     * Returns the fully qualified name of the client that sent the
     * request.
     **/
    public String getRemoteHost() {
        return "localhost";
    }


    /**
     * Returns the host name of the server that received the request.
     **/
    public String getServerName() {
        return "localhost";
    }


    /**
     * Returns the port number on which this request was received.
     **/
    public int getServerPort() {
        return 0;
    }


    /**
     *
     * @deprecated 	As of Version 2.1 of the Java Servlet API,
     * 			use {@link javax.servlet.ServletContext#getRealPath} instead.
     *
     */
    public String getRealPath( String path ) {
        throwNotImplementedYet();
        return "";
    }


    /**
     * Returns the body of the request as a <code>BufferedReader</code>
     * that translates character set encodings.
     **/
    public BufferedReader getReader() throws IOException {
        throwNotImplementedYet();
        return null;
        
    }


    /**
     * Returns the Internet Protocol (IP) address of the client
     * that sent the request.
     **/
    public String getRemoteAddr() {
        return LOOPBACK_ADDRESS;
    }


    /**
     *
     * Stores an attribute in the context of this request.
     * Attributes are reset between requests.
     *
     * @exception IllegalStateException	if the specified attribute already has a value
     *
     **/
    public void setAttribute( String key, Object o ) {
        //if (_attributes.get( key ) != null) 
        //    throw new IllegalStateException( "Attribute '" + key + "' already has a value" );
        if( o == null ) {
            _attributes.remove( key );
        } else {
            _attributes.put( key, o );
        }
    }


//--------------------------------- methods added to ServletRequest in JSDK 2.2 ------------------------------------------------


    /**
     * Returns a boolean indicating whether this request was made using a secure channel, such as HTTPS.
     **/
    public boolean isSecure() {
        throw new RuntimeException( "isSecure not implemented" );
    }


    /**
     * Returns the preferred Locale that the client will accept content in, based on the Accept-Language header.
     * If the client request doesn't provide an Accept-Language header, this method returns the default locale for the server.
     **/
    public java.util.Locale getLocale() {
        throw new RuntimeException( "getLocale not implemented" );
    }


    /**
     * Returns an Enumeration of Locale objects indicating, in decreasing order starting with the preferred locale,
     * the locales that are acceptable to the client based on the Accept-Language header.
     * If the client request doesn't provide an Accept-Language header, this
     * method returns an Enumeration containing one Locale, the default locale for the server.
     **/
    public java.util.Enumeration getLocales() {
        throw new RuntimeException( "getLocales not implemented" );
    }


    /**
     * Removes an attribute from this request. This method is not generally needed
     * as attributes only persist as long as the request is being handled.
     **/
    public void removeAttribute( String name ) {
        _attributes.remove( name );
    }


    /**
     * Returns a RequestDispatcher object that acts as a wrapper for the resource located at the given path.
     * A RequestDispatcher object can be used to forward a request to the resource or to include the
     * resource in a response. The resource can be dynamic or static.
     *
     * The pathname specified may be relative, although it cannot extend outside the current servlet
     * context. If the path begins with a "/" it is interpreted as relative to the current context root.
     * This method returns null if the servlet container cannot return a RequestDispatcher.
     *
     * The difference between this method and ServletContext.getRequestDispatcher(java.lang.String)
     * is that this method can take a relative path.
     **/
    public RequestDispatcher getRequestDispatcher( String path ) {
        throw new RuntimeException( "getRequestDispatcher not implemented" );
    }



//--------------------------------- methods added to HttpServletRequest in JSDK 2.2 ------------------------------------------------


    /**
     * Returns a java.security.Principal object containing the name of the current authenticated user.
     * If the user has not been authenticated, the method returns null.
     **/
    public java.security.Principal getUserPrincipal() {
        return null;
    }


    /**
     * Returns a boolean indicating whether the authenticated user is included in the specified
     * logical "role". Roles and role membership can be defined using deployment descriptors.
     * If the user has not been authenticated, the method returns false.
     **/
    public boolean isUserInRole( String role ) {
        if (_roles == null) return false;
        for (int i = 0; i < _roles.length; i++) {
            if (role.equals( _roles[i] )) return true;
        }
        return false;
    }


    /**
     * Returns all the values of the specified request header as an Enumeration of String objects.
     **/
    public java.util.Enumeration getHeaders( String name ) {
        throw new RuntimeException( "getHeaders not implemented" );
    }


    /**
     * Returns the portion of the request URI that indicates the context of the request.
     * The context path always comes first in a request URI. The path starts with a "/" character
     * but does not end with a "/" character. For servlets in the default (root) context,
     * this method returns "".
     **/
    public java.lang.String getContextPath() {
        return _context.getContextPath();
    }


//--------------------------------------- methods added to ServletRequest in JSDK 2.3 ----------------------------

    /**
     * Returns a java.util.Map of the parameters of this request.
     * Request parameters are extra information sent with the request. For HTTP servlets, parameters are contained
     * in the query string or posted form data.
     *
     * @since 1.3
     **/
    public Map getParameterMap() {
        return _parameters;
    }


    /**
     * Overrides the name of the character encoding used in the body of this request.
     * This method must be called prior to reading request parameters or reading input using getReader().
     *
     * @since 1.3
     **/
    public void setCharacterEncoding( String s ) throws UnsupportedEncodingException {
        // XXX implement me!
    }


//--------------------------------------- methods added to HttpServletRequest in JSDK 2.3 ----------------------------


    /**
     * Reconstructs the URL the client used to make the request.
     * The returned URL contains a protocol, server name, port number, and server path, but
     * it does not include query string parameters.
     *
     * Because this method returns a StringBuffer, not a string, you can modify the URL easily, for example,
     * to append query parameters.
     *
     * This method is useful for creating redirect messages and for reporting errors.
     *
     * @since 1.3
     */
    public StringBuffer getRequestURL() {
        return null;
    }


//--------------------------------------------- package members ----------------------------------------------


    void addCookie( Cookie cookie ) {
        _cookies.addElement( cookie );
        if (cookie.getName().equalsIgnoreCase( ServletUnitHttpSession.SESSION_COOKIE_NAME )) {
            _sessionID = cookie.getValue();
        }
    }


    void writeFormAuthentication( String userName, String password ) {
        getServletSession().setUserInformation( userName, toArray( password ) );
    }


    private ServletUnitHttpSession getServletSession() {
        return (ServletUnitHttpSession) getSession();
    }


    void readFormAuthentication() {
        if (getSession( /* create */ false ) != null) {
            recordAuthenticationInfo( getServletSession().getUserName(), getServletSession().getRoles() );
        }
    }


    void readBasicAuthentication() {
        String authorizationHeader = (String) _headers.get( "Authorization" );

        if (authorizationHeader != null) {
            String userAndPassword = Base64.decode( authorizationHeader.substring( authorizationHeader.indexOf( ' ' ) + 1 ) );
            int colonPos = userAndPassword.indexOf( ':' );
            recordAuthenticationInfo( userAndPassword.substring( 0, colonPos ),
                                      toArray( userAndPassword.substring( colonPos+1 ) ) );
        }
    }

    private String[] toArray( String roleList ) {
        StringTokenizer st = new StringTokenizer( roleList, "," );
        String[] result = new String[ st.countTokens() ];
        for (int i = 0; i < result.length; i++) {
            result[i] = st.nextToken();
        }
        return result;
    }


    void setOriginalURL( URL originalURL ) {
        getServletSession().setOriginalURL( originalURL );
    }


    URL getOriginalURL() {
        return getServletSession().getOriginalURL();
    }


    void recordAuthenticationInfo( String userName, String[] roles ) {
        _userName = userName;
        _roles    = roles;
    }


    String[] getRoles() {
        return _roles == null ? NO_ROLES : _roles;
    }



//--------------------------------------------- private members ----------------------------------------------

    final static private String LOOPBACK_ADDRESS = "127.0.0.1";
    final static private String[] NO_ROLES = new String[0];


    private WebRequest                 _request;
    private ServletRequest             _servletRequest;
    private WebClient.HeaderDictionary _headers;
    private ServletUnitContext         _context;
    private ServletUnitHttpSession     _session;
    private Hashtable                  _attributes = new Hashtable();
    private Hashtable                  _parameters = new Hashtable();
    private Vector                     _cookies    = new Vector();
    private String                     _sessionID;
    private byte[]                     _messageBody;

    private String                     _userName;
    private String[]                   _roles;


    final static private int STATE_INITIAL     = 0;
    final static private int STATE_HAVE_NAME   = 1;
    final static private int STATE_HAVE_EQUALS = 2;
    final static private int STATE_HAVE_VALUE  = 3;


    /**
     * This method employs a state machine to parse a parameter query string.
     * The transition rules are as follows:
     *    State  \          text         '='           '&'
     *    initial:         have_name      -           initial
     *    have_name:          -         have_equals   initial
     *    have_equals:     have_value     -           initial
     *    have_value:         -         initial       initial
     * actions occur on the following transitions:
     *    initial -> have_name:   save token as name
     *    have_equals -> initial: record parameter with null value
     *    have_value  -> initial: record parameter with value
     **/
    private void loadParameters( String queryString ) {
        if (queryString.length() == 0) return;
        StringTokenizer st = new StringTokenizer( queryString, "&=", /* return tokens */ true );
        int state = STATE_INITIAL;
        String name  = null;
        String value = null;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals( "&" )) {
                state = STATE_INITIAL;
                if (name != null && value != null) addParameter( name, value );
                name  = value = null;
            } else if (token.equals( "=" )) {
                if (state == STATE_HAVE_NAME) {
                    state = STATE_HAVE_EQUALS;
                } else if (state == STATE_HAVE_VALUE) {
                    state = STATE_INITIAL;
                }
            } else if (state == STATE_INITIAL) {
                name = HttpUnitUtils.decode( token );
                value = "";
                state = STATE_HAVE_NAME;
            } else {
                value = HttpUnitUtils.decode( token );
                state = STATE_HAVE_VALUE;
            }
        }
        if (name != null && value != null) addParameter( name, value );
    }


    private void addParameter( String name, String encodedValue ) {
        String[] values = (String[]) _parameters.get( name );
        if (values == null) {
            _parameters.put( name, new String[] { encodedValue } );
        } else {
            _parameters.put( name, extendedArray( values, encodedValue ) );
        }
    }


    private String[] extendedArray( String[] baseArray, String newValue ) {
        String[] result = new String[ baseArray.length+1 ];
        System.arraycopy( baseArray, 0, result, 0, baseArray.length );
        result[ baseArray.length ] = newValue;
        return result;
    }


    private void throwNotImplementedYet() {
        throw new RuntimeException( "Not implemented yet" );
    }

    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }

    public String getLocalName() {
        throw new UnsupportedOperationException();
    }

    public String getLocalAddr() {
        throw new UnsupportedOperationException();
    }

    public int getLocalPort() {
        throw new UnsupportedOperationException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	829/3	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 ===========================================================================
*/
