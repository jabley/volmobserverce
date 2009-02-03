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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.servlet;

import javax.servlet.ServletContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Default implementation of the <code>ServletEnvironmentFactory</code>
 * abstract class
 */
public class DefaultServletEnvironmentFactory
        extends ServletEnvironmentFactory {

    // javadoc inherited
    public ServletEnvironment createEnvironment(ServletContext context) {
        final ServletContext servletContext = context;
        // return a ServletEnvironment implementation
        return new ServletEnvironment() {
            public ServletContext getServletContext() {
                return servletContext;
            }
        };
    }

    // javadoc inherited
    public ServletEnvironmentInteraction
            createEnvironmentInteraction(ServletEnvironment servletEnvironment,
                                         Servlet servlet,
                                         ServletConfig servletConfig,
                                         ServletRequest servletRequest,
                                         ServletResponse servletResponse) {
        // return a ServletEnvironmentInteractionImpl instance
        return new ServletEnvironmentInteractionImpl(servletEnvironment,
                                                     servlet,
                                                     servletConfig,
                                                     servletRequest,
                                                     servletResponse);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	271/1	doug	VBM:2003073002 Implemented various environment fatories

 ===========================================================================
*/
