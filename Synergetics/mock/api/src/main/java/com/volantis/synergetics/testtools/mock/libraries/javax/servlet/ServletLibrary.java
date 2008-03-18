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

package com.volantis.synergetics.testtools.mock.libraries.javax.servlet;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Triggers auto generation of classes within <code>javax.servlet</code> and
 * contained packages for which the source is not available.
 *
 * @mock.generate library="true"
 */
public class ServletLibrary {

    /**
     * @mock.generate interface="true"
     */
    public HttpSession httpSession;

    /**
     * @mock.generate interface="true"
     */
    public ServletConfig servletConfig;

    /**
     * @mock.generate interface="true"
     */
    public ServletContext servletContext;

    /**
     * @mock.generate interface="true"
     */
    public HttpServletRequest httpServletRequest;

    /**
     * @mock.generate interface="true"
     */
    public HttpServletResponse httpServletResponse;

    /**
     * @mock.generate interface="true"
     */
    public FilterChain filterChain;
    
    /**
     * @mock.generate interface="true"
     */
    public FilterConfig filterConfig;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
