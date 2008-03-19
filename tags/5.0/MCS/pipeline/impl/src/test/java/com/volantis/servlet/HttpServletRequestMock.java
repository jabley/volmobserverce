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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 16-May-2003  Sumit       VBM:2003030612 - Mock for use in Servlet/JSP 
 *                          test cases
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;


/**
 * A dummy servlet response for use in Servlet/ JSP test cases
 */
public class HttpServletRequestMock extends HttpServletRequestStub {


    private RequestDispatcher requestDispatcher;

    /**
     * A hashmap to contain attributes
     */
    private HashMap attributes= new HashMap();
    
    /**
     * A hashtable to contain headers - An enumeration is needed so the use of a 
     * hashtable is prefered instead of a hashmap. 
     */
    private static Hashtable headers = new Hashtable();

    // Initialise the essential header variables
    static {
        headers.put("Mariner-Application","mcs");
    }
    
    /**
     * Hashtable containing the parameters - An enumeration is needed so the use of a 
     * hashtable is prefered instead of a hashmap.
     */
    
    private Hashtable parameters = new Hashtable();
    
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    public String getHeader(String key) {
        return (String)headers.get(key);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    public Enumeration getHeaderNames() {
        
        return headers.keys();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
     */
    public Enumeration getHeaders(String arg0) {
        
        return headers.keys();
    }

    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    public String getParameter(String key) {
        return (String) parameters.get(key);
    }
    
    public void setParameter(String key, String value) {
        parameters.put(key, value);
    }
    

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    public Enumeration getParameterNames() {
        return parameters.keys();
    }
    
    /**
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String arg0) {
        String[] rVal = new String[1];
        rVal[0] = getParameter(arg0);
        return rVal[0]==null?null:rVal;
        
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    public HttpSession getSession() {
        return new HttpSessionMock();
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.servlet.HttpServletRequestStub#getSession(boolean)
     */
    public HttpSession getSession(boolean arg0) {
        return new HttpSessionMock();
    }

    /**
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String arg0) {
        return requestDispatcher;
    }
    
    public void setRequestDispatcher(RequestDispatcher disp) {
        requestDispatcher = disp;
    }
    
    


    

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 25-Jun-03	102/1	sumit	VBM:2003061906 request:getParameter XPath function support

 ===========================================================================
*/
