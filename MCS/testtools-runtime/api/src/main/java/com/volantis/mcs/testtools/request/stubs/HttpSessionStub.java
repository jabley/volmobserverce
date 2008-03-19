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
 * 02-May-2003  Sumit       VBM:2003041502 - Stub for use in Servlet/JSP 
 *                          test cases
 * 19-May-2003  Sumit       VBM:2003041502 - Refactored to make this a stub 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.testtools.request.stubs;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * TODO Add class description
 */
public class HttpSessionStub  implements HttpSession {

    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
     */
    public Object getAttribute(String key) {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getCreationTime()
     */
    public long getCreationTime() {
        
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getId()
     */
    public String getId() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getLastAccessedTime()
     */
    public long getLastAccessedTime() {
        
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
     */
    public int getMaxInactiveInterval() {
        
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getSessionContext()
     */
    public HttpSessionContext getSessionContext() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
     */
    public Object getValue(String arg0) {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getValueNames()
     */
    public String[] getValueNames() {
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#invalidate()
     */
    public void invalidate() {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#isNew()
     */
    public boolean isNew() {
        
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
     */
    public void putValue(String arg0, Object arg1) {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String arg0) {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
     */
    public void removeValue(String arg0) {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String arg0, Object arg1) {
        
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
     */
    public void setMaxInactiveInterval(int arg0) {
        
        
    }
    
    public ServletContext getServletContext() {
                
        return null;
    }
    

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	7950/1	allan	VBM:2005041317 Some testcases for smart server

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4737/2	allan	VBM:2004062202 Restrict volantis initialization.

 ===========================================================================
*/
