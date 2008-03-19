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
 * 30-May-2003  Sumit       VBM:2003030612 - JSP testcases can use this class
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * A default extension of the PageContext class for use in JSP tag 
 * test cases
 */
public class JSPPageContextStub extends PageContext {

    private ServletRequest servletRequest;
    private ServletContext servletContext;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#findAttribute(java.lang.String)
     */
    public Object findAttribute(String arg0) {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#forward(java.lang.String)
     */
    public void forward(String arg0) throws ServletException, IOException {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String arg0) {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getAttribute(java.lang.String, int)
     */
    public Object getAttribute(String arg0, int arg1) {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getAttributeNamesInScope(int)
     */
    public Enumeration getAttributeNamesInScope(int arg0) {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getAttributesScope(java.lang.String)
     */
    public int getAttributesScope(String arg0) {
        
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getException()
     */
    public Exception getException() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getOut()
     */
    public JspWriter getOut() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getPage()
     */
    public Object getPage() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getRequest()
     */
    public ServletRequest getRequest() {
        
        return servletRequest;
    }

    public void setRequest(ServletRequest request) {
            
            servletRequest = request;
    }
    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getResponse()
     */
    public ServletResponse getResponse() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getServletConfig()
     */
    public ServletConfig getServletConfig() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getServletContext()
     */
    public ServletContext getServletContext() {
        return servletContext;
    }
    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getServletContext()
     */
    public void setServletContext(ServletContext context) {
        servletContext = context;
    }
        
    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getSession()
     */
    public HttpSession getSession() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Exception)
     */
    public void handlePageException(Exception arg0) throws ServletException, IOException {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#include(java.lang.String)
     */
    public void include(String arg0) throws ServletException, IOException {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#initialize(javax.servlet.Servlet, javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.String, boolean, int, boolean)
     */
    public void initialize(Servlet arg0, ServletRequest arg1, ServletResponse arg2, String arg3, boolean arg4, int arg5, boolean arg6) throws IOException, IllegalStateException, IllegalArgumentException {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#release()
     */
    public void release() {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String arg0) {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#removeAttribute(java.lang.String, int)
     */
    public void removeAttribute(String arg0, int arg1) {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String arg0, Object arg1) {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#setAttribute(java.lang.String, java.lang.Object, int)
     */
    public void setAttribute(String arg0, Object arg1, int arg2) {
        
        
    }

    /**
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Throwable)
     */
    public void handlePageException(Throwable arg0)
        throws ServletException, IOException {

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
