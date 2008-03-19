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

package com.volantis.shared.jsp;

import com.volantis.shared.servlet.ServletEnvironment;

import javax.servlet.jsp.PageContext;

/**
 * A class for creating JSP environment specific objects.
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
public abstract class JspEnvironmentFactory {

    /**
     * Default JspEnvironmentFactory instance
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    static JspEnvironmentFactory defaultEnvironmentFactory =
            new DefaultJspEnvironmentFactory();

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static JspEnvironmentFactory getDefaultInstance() {
        return defaultEnvironmentFactory;
    }

    /**
     * Create an environment interaction object for a JSP.
     * @param servletEnvironment The servlet environment within which the JSP
     * is operating, this can be a ServletEnvironment or any of the derived
     * classes that are suitable for use by a JSP.
     * @param pageContext The page context for the JSP.
     * @return A new JspEnvironmentInteraction.
     */
    public abstract JspEnvironmentInteraction createEnvironmentInteraction(
            ServletEnvironment servletEnvironment,
            PageContext pageContext);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	271/1	doug	VBM:2003073002 Implemented various environment fatories

 15-Jul-03	197/1	steve	VBM:2003071501 Cookie and environment implementation

 11-Jul-03	181/1	steve	VBM:2003070802 Environment implementation

 ===========================================================================
*/
