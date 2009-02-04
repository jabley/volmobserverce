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
 * $Header: /src/voyager/com/volantis/mcs/servlet/ServletInternals.java,v 1.6 2002/11/26 12:23:12 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-02    Paul            VBM:2001122105 - Created to allow access to
 *                              package private methods from other mariner
 *                              packages.
 * 25-Feb-02    Paul            VBM:2002022204 - Removed responseWriter
 *                              parameter from initialise method.
 * 18-Mar-02    Ian             VBM:2002031203 - Changed log4j Category from
 *                              class to string.
 * 13-Nov-02    Paul            VBM:2002091806 - Changed throws declaration to
 *                              throw MarinerContextException instead of
 *                              RepositoryException.
 * 22-Nov-02    Paul            VBM:2002091806 - Replaced references to
 *                              ServletConfig with ServletContext.
 * 26-Nov-02    Paul            VBM:2002091806 - Undid some of the work above
 *                              in order to maintain backwards compatability.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provides methods which allow other packages in mariner to
 * access server package methods. It is not part of the public API but has
 * to be a public class to allow other mariner packages to use its methods.
 */
public class ServletInternals {

  /**
   * Initialise the <code>MarinerServletRequestContext</code>.
   * @param servletConfig The <code>ServletConfig</code>
   * @param servletContext The <code>ServletContext</code>
   * @param request The <code>ServletRequest</code>.
   * @param response The <code>ServletResponse</code>.
   * @param environmentContext The <code>EnvironmentContext</code>.
   * @throws IOException If there was a problem clearing the output.
   * @throws MarinerContextException If there was a problem initializing
   * the context.
   */
  public static
    void initialise (MarinerServletRequestContext context,
                     ServletConfig servletConfig,
                     ServletContext servletContext,
                     ServletRequest request,
                     ServletResponse response,
                     EnvironmentContext environmentContext)
    throws IOException,
           MarinerContextException {

    context.initialise (servletConfig, servletContext, 
            (HttpServletRequest)request, (HttpServletResponse) response,
                        environmentContext);
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
