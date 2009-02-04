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
 * $Header: /src/voyager/com/volantis/mcs/servlet/AbstractMarinerServlet.java,v 1.1 2002/11/06 16:51:03 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Mar-02    Mat             VBM:2002090207 - Created to hold common servlet
 *                              methods.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author  mat
 */
public abstract class AbstractMarinerServlet extends HttpServlet {/**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AbstractMarinerServlet.class);
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);   
    }
    
    /** Destroys the servlet.
     */
    public void destroy()
    {
        
    }
    /**
     * Get the mariner request context, or create a new one
     * @param request  The HttpServletRequest passed to the servlet
     * @param response The HttpServletResponse passed to the servlet
     * @return The MarinerRequestContext for this servlet
     */
    protected MarinerServletRequestContext getMarinerRequestContext( 
                   HttpServletRequest request, HttpServletResponse response )
    {
        MarinerServletRequestContext context = (MarinerServletRequestContext)
            MarinerServletRequestContext.getCurrent( request );
        if( context != null )
        {
            return context;
        }
        
        try {
            context = new MarinerServletRequestContext(
                        getServletConfig(), request, response );
        }
        catch( MarinerContextException mce )
        {
            logger.warn("mariner-context-exception", mce);
            context = null;
        }
        catch( IOException ioe )
        {
            logger.warn("unexpected-ioexception", ioe);
            context = null;
        }
        return context;
    }

    /**
     * Get the initialized Volantis bean.
     * @return the initialized Volantis.
     */
    protected synchronized Volantis getVolantisBean() {
        try {
            MarinerApplication application =
                    MarinerServletApplication.getInstance(getServletContext());
            return ApplicationInternals.getVolantisBean(application);
        } catch (ServletException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-May-05	7963/1	pduffin	VBM:2005042906 Removed DDM components, e.g. ApplicationProperties, URLMappers, DDMProxy, etc

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 24-Jun-04	4737/1	allan	VBM:2004062202 Restrict volantis initialization.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
