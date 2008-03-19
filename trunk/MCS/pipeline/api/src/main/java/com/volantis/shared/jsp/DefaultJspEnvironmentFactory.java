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
 * A default implementation of the <code>JspEnvironmentFactory</code> class
 */
public class DefaultJspEnvironmentFactory extends JspEnvironmentFactory {

    // javadoc inherited
    public JspEnvironmentInteraction createEnvironmentInteraction(
            ServletEnvironment servletEnvironment,
            PageContext pageContext) {
        // return a new JspEnvironmentInteractionImpl instance
        return new JspEnvironmentInteractionImpl(servletEnvironment,
                                                 pageContext);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	271/2	doug	VBM:2003073002 Implemented various environment fatories

 ===========================================================================
*/
