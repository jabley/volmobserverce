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
package com.volantis.shared.servlet.http;

import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.shared.servlet.ServletEnvironment;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Default implementation of the <code>HttpServletEnvironmentFactory</code>
 * abstract class
 */
public class DefaultHttpServletEnvironmentFactory
        extends HttpServletEnvironmentFactory {

    // javadoc inherited
    public HttpServletEnvironmentInteraction createEnvironmentInteraction(
            ServletEnvironment servletEnvironment,
            HttpServlet servlet,
            ServletConfig servletConfig,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        // return a new HttpServletEnvironmentInteractionImpl instance
        return new HttpServletEnvironmentInteractionImpl(servletEnvironment,
                                                         servlet,
                                                         servletConfig,
                                                         servletRequest,
                                                         servletResponse);
    }

    // javadoc inherited
    public HTTPMessageEntities createCookies() {
        return new SimpleHTTPMessageEntities();
    }

    // javadoc inherited
    public HTTPMessageEntities createCookies(HttpServletRequest request) {
        HTTPMessageEntities cookies = createCookies();
        javax.servlet.http.Cookie requestCookies[] = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < requestCookies.length; i++) {
                cookies.add(new HTTPServletCookie(requestCookies[i]));
            }
        }
        return cookies;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	217/3	allan	VBM:2003071702 Rename and re-write HttpCookie

 31-Jul-03	217/1	allan	VBM:2003071702 Ensure correct array types created. Add our-commons-logging to build.

 31-Jul-03	271/1	doug	VBM:2003073002 Implemented various environment fatories

 ===========================================================================
*/
