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
import com.volantis.shared.servlet.ServletEnvironment;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A class for creating HTTP servlet environment specific objects.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class HttpServletEnvironmentFactory {

    /**
     * Default HttpServletEnvironmentFactory instance
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    static DefaultHttpServletEnvironmentFactory defaultFactory =
            new DefaultHttpServletEnvironmentFactory();

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static HttpServletEnvironmentFactory getDefaultInstance() {
        return defaultFactory;
    }

    /**
     * Create a new HttpServletEnvironmentInteraction.
     *
     * @param servletEnvironment the servlet environment
     * @param servlet the servlet
     * @param servletConfig the servlet configuration
     * @param servletRequest the servlet request
     * @param servletResponse the servlet response
     * @return an environment interaction object for this servlet
     */
    public abstract
            HttpServletEnvironmentInteraction createEnvironmentInteraction(
            ServletEnvironment servletEnvironment,
            HttpServlet servlet,
            ServletConfig servletConfig,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse);

    /**
     * Create an empty HTTPMessageEntities for storing cookies
     * @return an empty HTTPMessageEntities instance
     */
    public abstract HTTPMessageEntities createCookies();

    /**
     * Create a HTTPMessageEntities and populate it with cookies from
     * a request
     * @param request the request
     * @return the populated HTTPMessageEntities instance
     */
    public abstract
            HTTPMessageEntities createCookies(HttpServletRequest request);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	8005/1	pduffin	VBM:2005050404 Moved dom to its own subsystem

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/9	allan	VBM:2003070709 Fixed merge conflicts

 04-Aug-03	294/6	allan	VBM:2003070709 Fixed merge conflicts

 04-Aug-03	294/3	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	271/2	doug	VBM:2003073002 Implemented various environment fatories

 31-Jul-03	271/2	doug	VBM:2003073002 Implemented various environment fatories

 31-Jul-03	217/5	allan	VBM:2003071702 Ensure correct array types created. Add our-commons-logging to build.

 31-Jul-03	271/2	doug	VBM:2003073002 Implemented various environment fatories

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to NetFactory.

 15-Jul-03	197/1	steve	VBM:2003071501 Cookie and environment implementation

 15-Jul-03	181/5	steve	VBM:2003070802 Made createEnvironmentInteraction static and corrected javadoc. All static methods hould be public.

 11-Jul-03	181/3	steve	VBM:2003070802 rework fixes

 11-Jul-03	181/1	steve	VBM:2003070802 Environment implementation

 ===========================================================================
*/
