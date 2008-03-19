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
 * $Id: WebApplication.java,v 1.1 2002/11/08 12:29:47 sfound Exp $
 *
 * Copyright (c) 2001-2002, Russell Gold
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
import com.meterware.httpunit.HttpInternalErrorException;
import com.meterware.httpunit.HttpNotFoundException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This class represents the information recorded about a single web
 * application. It is usually extracted from web.xml.
 *
 * @author <a href="mailto:russgold@acm.org">Russell Gold</a>
 * @author <a href="balld@webslingerZ.com">Donald Ball</a>
 **/
class WebApplication {


    /**
     * Constructs a default application spec with no information.
     */
    WebApplication() {
        _contextPath = "";
        _realPath = null;
    }


    /**
     * Constructs an application spec from an XML document.
     */
    WebApplication( Document document ) throws MalformedURLException, SAXException {
        this( document, null, "" );
    }


    /**
     * Constructs an application spec from an XML document.
     */
    WebApplication( Document document, String contextPath ) throws MalformedURLException, SAXException {
        this( document, null, contextPath );
    }


    /**
     * Constructs an application spec from an XML document.
     */
    WebApplication( Document document, File file, String contextPath ) throws MalformedURLException, SAXException {
        _contextDir = file;
        _contextPath = contextPath == null ? "" : contextPath;
        registerServlets( document );
        extractSecurityConstraints( document );
        extractContextParameters( document );
        extractLoginConfiguration( document );
    }


    public void setRealPath( String path ) {
        _realPath = path;
    }
    
    public String getRealPath() {
        return _realPath;
    }
    
    private void extractSecurityConstraints( Document document ) throws SAXException {
        NodeList nl = document.getElementsByTagName( "security-constraint" );
        for (int i = 0; i < nl.getLength(); i++) {
            _securityConstraints.add( new SecurityConstraintImpl( (Element) nl.item( i ) ) );
        }
    }


    /**
     * Registers a servlet class to be run.
     **/
    void registerServlet( String resourceName, String servletClassName, Hashtable initParams ) {
        registerServlet( resourceName, new ServletConfiguration( servletClassName, initParams ) );
    }


    /**
     * Registers a servlet to be run.
     **/
    void registerServlet( String resourceName, ServletConfiguration servletConfiguration ) {
        // FIXME - shouldn't everything start with one or the other?
        if (!resourceName.startsWith( "/" ) && !resourceName.startsWith( "*" )) {
            resourceName = "/" + resourceName;
        }
        _servletMapping.put( resourceName, servletConfiguration );
    }


    ServletRequest getServletRequest( URL url ) {
        return _servletMapping.get( url );
    }


    /**
     * Returns true if this application uses Basic Authentication.
     */
    boolean usesBasicAuthentication() {
        return _useBasicAuthentication;
    }


    /**
     * Returns true if this application uses form-based authentication.
     */
    boolean usesFormAuthentication() {
        return _useFormAuthentication;
    }


    String getAuthenticationRealm() {
        return _authenticationRealm;
    }


    URL getLoginURL() {
        return _loginURL;
    }


    URL getErrorURL() {
        return _errorURL;
    }


    /**
     * Returns true if the specified path may only be accesses by an authorized user.
     * @param url the application-relative path of the URL
     */
    boolean requiresAuthorization( URL url ) {
        String result;
        String file = url.getFile();
        if (_contextPath.equals( "" )) {
            result = file;
        } else if (file.startsWith( _contextPath )) {
            result = file.substring( _contextPath.length() );
        } else {
            result = null;
        }
        return getControllingConstraint( result ) != NULL_SECURITY_CONSTRAINT;
    }


    /**
     * Returns true of the specified role may access the desired URL path.
     */
    boolean roleMayAccess( String roleName, URL url ) {
        String result;
        String file = url.getFile();
        if (_contextPath.equals( "" )) {
            result = file;
        } else if (file.startsWith( _contextPath )) {
            result = file.substring( _contextPath.length() );
        } else {
            result = null;
        }
        return getControllingConstraint( result ).hasRole( roleName );
    }


    private SecurityConstraint getControllingConstraint( String urlPath ) {
        for (Iterator i = _securityConstraints.iterator(); i.hasNext();) {
            SecurityConstraint sc = (SecurityConstraint) i.next();
            if (sc.controlsPath( urlPath )) return sc;
        }
        return NULL_SECURITY_CONSTRAINT;
    }


    File getResourceFile( String path ) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (_contextDir == null) {
            return new File( path );
        } else {
            return new File( _contextDir, path );
        }
    }


    Hashtable getContextParameters() {
        return _contextParameters;
    }


//------------------------------------------------ private members ---------------------------------------------


    private final static SecurityConstraint NULL_SECURITY_CONSTRAINT = new NullSecurityConstraint();

    private final ServletConfiguration SECURITY_CHECK_CONFIGURATION = new ServletConfiguration( SecurityCheckServlet.class.getName() );

    private final ServletMapping SECURITY_CHECK_MAPPING = new ServletMapping( SECURITY_CHECK_CONFIGURATION );

    /** A mapping of resource names to servlet class names. **/
    private ServletMap _servletMapping = new ServletMap();

    private ArrayList _securityConstraints = new ArrayList();

    private boolean _useBasicAuthentication;

    private boolean _useFormAuthentication;

    private String _authenticationRealm = "";

    private URL _loginURL;

    private URL _errorURL;

    private Hashtable _contextParameters = new Hashtable();

    private File _contextDir = null;

    private String _contextPath = null;
    
    private String _realPath = null;


    private void extractLoginConfiguration( Document document ) throws MalformedURLException, SAXException {
        NodeList nl = document.getElementsByTagName( "login-config" );
        if (nl.getLength() == 1) {
            final Element loginConfigElement = (Element) nl.item( 0 );
            String authenticationMethod = getChildNodeValue( loginConfigElement, "auth-method", "BASIC" );
            _authenticationRealm = getChildNodeValue( loginConfigElement, "realm-name", "" );
            if (authenticationMethod.equalsIgnoreCase( "BASIC" )) {
                _useBasicAuthentication = true;
                if (_authenticationRealm.length() == 0) throw new SAXException( "No realm specified for BASIC Authorization" );
            } else if (authenticationMethod.equalsIgnoreCase( "FORM" )) {
                _useFormAuthentication = true;
                if (_authenticationRealm.length() == 0) throw new SAXException( "No realm specified for FORM Authorization" );
                _loginURL = new URL( "http", "localhost", getChildNodeValue( loginConfigElement, "form-login-page" ) );
                _errorURL = new URL( "http", "localhost", getChildNodeValue( loginConfigElement, "form-error-page" ) );
            }
        }
    }


    private void registerServlets( Document document ) throws SAXException {
        Hashtable nameToClass = new Hashtable();
        NodeList nl = document.getElementsByTagName( "servlet" );
        for (int i = 0; i < nl.getLength(); i++) registerServletClass( nameToClass, (Element) nl.item( i ) );
        nl = document.getElementsByTagName( "servlet-mapping" );
        for (int i = 0; i < nl.getLength(); i++) registerServlet( nameToClass, (Element) nl.item( i ) );
    }


    private void registerServletClass( Dictionary mapping, Element servletElement ) throws SAXException {
        mapping.put( getChildNodeValue( servletElement, "servlet-name" ),
                     new ServletConfiguration( servletElement ) );
    }


    private void registerServlet( Dictionary mapping, Element servletElement ) throws SAXException {
        registerServlet( getChildNodeValue( servletElement, "url-pattern" ),
                         (ServletConfiguration) mapping.get( getChildNodeValue( servletElement, "servlet-name" ) ) );
    }


    private void extractContextParameters( Document document ) throws SAXException {
        NodeList nl = document.getElementsByTagName( "context-param" );
        for (int i = 0; i < nl.getLength(); i++) {
            Element param = (Element) nl.item( i );
            String name = getChildNodeValue( param, "param-name" );
            String value = getChildNodeValue( param, "param-value" );
            _contextParameters.put( name, value );
        }
    }


    private static String getChildNodeValue( Element root, String childNodeName ) throws SAXException {
        return getChildNodeValue( root, childNodeName, null );
    }


    private static String getChildNodeValue( Element root, String childNodeName, String defaultValue ) throws SAXException {
        NodeList nl = root.getElementsByTagName( childNodeName );
        if (nl.getLength() == 1) {
            return getTextValue( nl.item( 0 ) ).trim();
        } else if (defaultValue == null) {
            throw new SAXException( "Node <" + root.getNodeName() + "> has no child named <" + childNodeName + ">" );
        } else {
            return defaultValue;
        }
    }


    private static String getTextValue( Node node ) throws SAXException {
        Node textNode = node.getFirstChild();
        if (textNode == null) return "";
        if (textNode.getNodeType() != Node.TEXT_NODE) throw new SAXException( "No text value found for <" + node.getNodeName() + "> node" );
        return textNode.getNodeValue();
    }


    private static boolean patternMatches( String urlPattern, String urlPath ) {
        return urlPattern.equals( urlPath );
    }


//============================================= SecurityCheckServlet class =============================================


    static class SecurityCheckServlet extends HttpServlet {

        protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
            handleLogin( (ServletUnitHttpRequest) req, resp );
        }


        protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
            handleLogin( (ServletUnitHttpRequest) req, resp );
        }


        private void handleLogin( ServletUnitHttpRequest req, HttpServletResponse resp ) throws IOException {
            final String username = req.getParameter( "j_username" );
            final String password = req.getParameter( "j_password" );
            req.writeFormAuthentication( username, password );
            resp.sendRedirect( req.getOriginalURL().toExternalForm() );
        }

    }
//============================================= ServletConfiguration class =============================================


    class ServletConfiguration {

        ServletConfiguration( String className ) {
            _className = className;
        }


        ServletConfiguration( String className, Hashtable initParams ) {
            _className = className;
            if (initParams != null) _initParams = initParams;
        }


        ServletConfiguration( Element servletElement ) throws SAXException {
            this( getChildNodeValue( servletElement, "servlet-class" ) );
            final NodeList initParams = servletElement.getElementsByTagName( "init-param" );
            for (int i = initParams.getLength() - 1; i >= 0; i--) {
                _initParams.put( getChildNodeValue( (Element) initParams.item( i ), "param-name" ),
                                 getChildNodeValue( (Element) initParams.item( i ), "param-value" ) );
            }
        }


        synchronized Servlet getServlet() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException {
            if (_servlet == null) {
                Class servletClass = Class.forName( getClassName() );
                _servlet = (Servlet) servletClass.newInstance();
                _servlet.init( new ServletUnitServletConfig( _servlet, WebApplication.this, getInitParams() ) );
            }

            return _servlet;
        }


        String getClassName() {
            return _className;
        }


        Hashtable getInitParams() {
            return _initParams;
        }


        private Servlet _servlet;
        private String _className;
        private Hashtable _initParams = new Hashtable();
    }


//=================================== SecurityConstract interface and implementations ==================================


    interface SecurityConstraint {

        boolean controlsPath( String urlPath );


        boolean hasRole( String roleName );
    }


    static class NullSecurityConstraint implements SecurityConstraint {

        public boolean controlsPath( String urlPath ) {
            return false;
        }


        public boolean hasRole( String roleName ) {
            return true;
        }
    }


    static class SecurityConstraintImpl implements SecurityConstraint {

        SecurityConstraintImpl( Element root ) throws SAXException {
            final NodeList roleNames = root.getElementsByTagName( "role-name" );
            for (int i = 0; i < roleNames.getLength(); i++) _roles.add( getTextValue( (Element) roleNames.item( i ) ) );

            final NodeList resources = root.getElementsByTagName( "web-resource-collection" );
            for (int i = 0; i < resources.getLength(); i++) _resources.add( new WebResourceCollection( (Element) resources.item( i ) ) );
        }


        public boolean controlsPath( String urlPath ) {
            return getMatchingCollection( urlPath ) != null;
        }


        public boolean hasRole( String roleName ) {
            return _roles.contains( roleName );
        }


        private ArrayList _roles = new ArrayList();
        private ArrayList _resources = new ArrayList();


        public WebResourceCollection getMatchingCollection( String urlPath ) {
            for (Iterator i = _resources.iterator(); i.hasNext();) {
                WebResourceCollection wrc = (WebResourceCollection) i.next();
                if (wrc.controlsPath( urlPath )) return wrc;
            }
            return null;
        }


        class WebResourceCollection {

            WebResourceCollection( Element root ) throws SAXException {
                final NodeList urlPatterns = root.getElementsByTagName( "url-pattern" );
                for (int i = 0; i < urlPatterns.getLength(); i++) _urlPatterns.add( getTextValue( (Element) urlPatterns.item( i ) ) );
            }


            boolean controlsPath( String urlPath ) {
                for (Iterator i = _urlPatterns.iterator(); i.hasNext();) {
                    String pattern = (String) i.next();
                    if (patternMatches( pattern, urlPath )) return true;
                }
                return false;
            }


            private ArrayList _urlPatterns = new ArrayList();
        }
    }


    static class ServletRequestImpl implements ServletRequest {

        private URL            _url;
        private String         _servletName;
        private ServletMapping _mapping;


        ServletRequestImpl( URL url, String servletName, ServletMapping mapping ) {
            _url = url;
            _servletName = servletName;
            _mapping = mapping;
        }


        public Servlet getServlet() throws ServletException {
            if (getConfiguration() == null) throw new HttpNotFoundException( _url );

            try {
                return getConfiguration().getServlet();
            } catch (ClassNotFoundException e) {
                throw new HttpNotFoundException( _url, e );
            } catch (IllegalAccessException e) {
                throw new HttpInternalErrorException( _url, e );
            } catch (InstantiationException e) {
                throw new HttpInternalErrorException( _url, e );
            } catch (ClassCastException e) {
                throw new HttpInternalErrorException( _url, e );
            }
        }


        public String getServletPath() {
            return _mapping == null ? null : _mapping.getServletPath( _servletName );
        }


        public String getPathInfo() {
            return _mapping == null ? null : _mapping.getPathInfo( _servletName );
        }


        private ServletConfiguration getConfiguration() {
            return _mapping == null ? null : _mapping.getConfiguration();
        }
    }


    static class ServletMapping {

        private ServletConfiguration _configuration;


        ServletConfiguration getConfiguration() {
            return _configuration;
        }


        ServletMapping( ServletConfiguration configuration ) {
            _configuration = configuration;
        }


        String getServletPath( String servletName ) {
            return servletName;
        }


        String getPathInfo( String servletName ) {
            return null;
        }
    }


    static class PartialMatchServletMapping extends ServletMapping {

        private String _prefix;


        public PartialMatchServletMapping( ServletConfiguration configuration, String prefix ) {
            super( configuration );
            if (!prefix.endsWith( "/*" )) throw new IllegalArgumentException( prefix + " does not end with '/*'" );
            _prefix = prefix.substring( 0, prefix.length()-2 );
        }


        String getServletPath( String servletName ) {
            return _prefix;
        }


        String getPathInfo( String servletName ) {
            return servletName.length() > _prefix.length()
                    ? servletName.substring( _prefix.length() )
                    : null;
        }
    }


    /**
     * A utility class for mapping servlets to url patterns. This implements the
     * matching algorithm documented in section 10 of the JSDK-2.2 reference.
     */
    class ServletMap {

        private final Map _exactMatches = new HashMap();
        private final Map _extensions = new HashMap();
        private final Map _urlTree = new HashMap();

        void put( String mapping, ServletConfiguration servletConfiguration ) {
            if (mapping.startsWith( "*." )) {
                _extensions.put( mapping.substring( 2 ), new ServletMapping( servletConfiguration ) );
            } else if (!mapping.startsWith( "/" ) || !mapping.endsWith( "/*" )) {
                _exactMatches.put( mapping, new ServletMapping( servletConfiguration ) );
            } else {
                ParsedPath path = new ParsedPath( mapping );
                Map context = _urlTree;
                while (path.hasNext()) {
                    String part = path.next();
                    if (part.equals( "*" )) {
                        context.put( "*", new PartialMatchServletMapping( servletConfiguration, mapping ) );
                        return;
                    }
                    if (!context.containsKey( part )) {
                        context.put( part, new HashMap() );
                    }
                    context = (Map) context.get( part );
                }
            }
        }


        ServletRequest get( URL url ) {
            String file = url.getFile();
            if (!file.startsWith( _contextPath )) throw new HttpNotFoundException( url );

            String servletName = getServletName( file.substring( _contextPath.length() ) );

            if (servletName.endsWith( "j_security_check" )) {
                return new ServletRequestImpl( url, servletName, SECURITY_CHECK_MAPPING );
            } else {
                return new ServletRequestImpl( url, servletName, getMapping( servletName ) );
            }
        }


        private String getServletName( String urlFile ) {
            if (urlFile.indexOf( '?' ) < 0) {
                return urlFile;
            } else {
                return urlFile.substring( 0, urlFile.indexOf( '?' ) );
            }
        }


        private ServletMapping getMapping( String url ) {
            if (_exactMatches.containsKey( url )) return (ServletMapping) _exactMatches.get( url );

            Map context = getContextForLongestPathPrefix( url );
            if (context.containsKey( "*" )) return (ServletMapping) context.get( "*" );

            if (_extensions.containsKey( getExtension( url ))) return (ServletMapping) _extensions.get( getExtension( url ) );

            if (_urlTree.containsKey( "/" )) return (ServletMapping) _urlTree.get( "/" );

            final String prefix = "/servlet/";
            if (!url.startsWith( prefix )) return null;

            String className = url.substring( prefix.length() );
            try {
                Class.forName( className );
                return new ServletMapping( new ServletConfiguration( className ) );
            } catch (ClassNotFoundException e) {
                return null;
            }
        }


        private Map getContextForLongestPathPrefix( String url ) {
            Map context = _urlTree;

            ParsedPath path = new ParsedPath( url );
            while (path.hasNext()) {
                String part = path.next();
                if (!context.containsKey( part )) break;
                context = (Map) context.get( part );
            }
            return context;
        }


        private String getExtension( String url ) {
            int index = url.lastIndexOf( '.' );
            if (index == -1 || index >= url.length() - 1) {
                return "";
            } else {
                return url.substring( index + 1 );
            }
        }

    }

}


/**
 * A utility class for parsing URLs into paths
 *
 * @author <a href="balld@webslingerZ.com">Donald Ball</a>
 */
class ParsedPath {

    private final String path;
    private int position = 0;
    static final char seperator_char = '/';


    /**
     * Creates a new parsed path for the given path value
     *
     * @param path the path
     */
    ParsedPath( String path ) {
        if (path.charAt( 0 ) != seperator_char) {
            throw new IllegalArgumentException( "Illegal path '" + path + "', does not begin with " + seperator_char );
        }
        this.path = path;
    }


    /**
     * Returns true if there are more parts left, otherwise false
     */
    public final boolean hasNext() {
        return (position < path.length());
    }


    /**
     * Returns the next part in the path
     */
    public final String next() {
        int offset = position + 1;
        while (offset < path.length() && path.charAt( offset ) != seperator_char) {
            offset++;
        }
        String result = path.substring( position + 1, offset );
        position = offset;
        return result;
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
