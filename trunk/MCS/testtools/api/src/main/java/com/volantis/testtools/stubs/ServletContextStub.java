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
 * $Header: /src/voyager/com/volantis/testtools/stubs/ServletContextStub.java,v 1.3 2003/02/18 17:17:14 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Oct-02    Allan           VBM:2002100208 - Created. A stub implementation
 *                              of a ServletContext.
 * 11-Oct-02    Allan           VBM:2002101101 - Added realPath protected
 *                              member and updated getRealPath to use this.
 * 18-Feb-03    Byron           VBM:2003020610 - Added setRealPath method.
 * 28-Apr-03    Sumit           VBM:2003041502 - Added attributes HashMap
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.stubs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * A stub implementation of a ServletContext. None of the methods do anything
 * except return a value where required. This class can be used to create
 * ServletContext objects for test purposes - for example to call initialize()
 * in Volantis.java. This class can also be extended to provide specific
 * behaviour in specific methods without the need to implement every method in
 * the ServletContext interface.
 * <p>
 * Note: With the addition of the setRealPath method, this class may develop
 * into a Mock object. For now it may remain as a stub.
 */
public class ServletContextStub implements ServletContext {

    /**
     * A realPath property that allows us to influence the result of the
     * getRealPath() method. If you want to set this then use its setter.
     */
    protected String realPath;

    /**
     * Allows us to influence result of getInitParameter method. If you want
     * to set this then use its setter.
     */
    protected String mcsConfigFile;

    /**
     * A hashmap to contain attributes set by the setAttribute method
     */

    protected HashMap attributes = new HashMap();
    /**
     * Returns <code>this</code>.
     * @param s Ignored.
     */
    public ServletContext getContext(String s) {
        return this;
    }

    /**
     * @return -1
     */
    public int getMajorVersion() {
        return -1;
    }

    /**
     * @return -1
     */
    public int getMinorVersion() {
        return -1;
    }

    /**
     * @return s
     */
    public String getMimeType(String s) {
        return s;
    }

    /**
     * @return An empty Set.
     */
    public Set getResourcePaths(String s) {
        return new TreeSet();
    }

    /**
     * @return A URL that looks like: http://localhost:8080/<s>
     */
    public URL getResource(String s) throws MalformedURLException {
        return new URL("http", "localhost", 8080, s);
    }

    /**
     * Assumes the String is a file name and provides the InputStream for
     * that file if it exists.
     *
     * @return The InputStream for the specified fileName or null if the
     * file is not found.
     */
    public InputStream getResourceAsStream(String fileName) {
        File file = new File(fileName);
        if(file.exists()) {
            try {
                return new FileInputStream(file);
            } 
            catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }

    /**
     * @return null
     */
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    /**
     * @return null
     */
    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }
    
    /**
     * @return null
     * @deprecated Method getServlet is deprecated
     */
    public Servlet getServlet(String s) throws ServletException {
        return null;
    }

    /**
     * @return null
     * @deprecated Method getServlets is deprecated
     */
    public Enumeration getServlets() {
        return null;
    }

    /**
     * @return null
     * @deprecated Method getServletNames is deprecated
     */
    public Enumeration getServletNames() {
        return null;
    }

    /**
     * Does nothing.
     */
    public void log(String s) {
    }

    /**
     * Does nothing.
     * @deprecated Method log is deprecated
     */
    public void log(Exception exception, String s) {
    }

    /**
     * Does nothing.
     */
    public void log(String s, Throwable throwable) {
    }

    /**
     * @return The realPath property or s if realPath is null.
     */
    public String getRealPath(String s) {
        return realPath == null ? s : realPath;
    }

    /**
     * Set the realPath property.
     * @param s The realPath.
     */
    public void setRealPath(String s) {
        realPath = s;
    }

    /**
     * @return ""
     */
    public String getServerInfo() {
        return "";
    }

    /**
     * @return s
     */
    public String getInitParameter(String s) {
        String initParam = s;
        if (s.equals("config.file")) {
            initParam = mcsConfigFile;
        } 
        return initParam;
    }

    /**
     * @return null
     */
    public Enumeration getInitParameterNames() {
        return null;
    }

    /**
     * @return an object stored in the attributes hashmap with key of String key
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * @return null
     */
    public Enumeration getAttributeNames() {
        return null;
    }

    /**
     * Set an attribute (value) in the internal HashMap with key key 
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * Remove an attribute with key key
     */
    public void removeAttribute(String s) {
        attributes.remove(s);
    }

    /**
     * @return ""
     */
    public String getServletContextName() {
        return "";
    }

    public void setMCSConfigFile(String filename) {
        this.mcsConfigFile = filename;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Mar-05	6842/3	emma	VBM:2005020302 Make all file/resource references in config files relative to that file

 11-Mar-05	6842/1	emma	VBM:2005020302 Making file references in config files relative to those files

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
