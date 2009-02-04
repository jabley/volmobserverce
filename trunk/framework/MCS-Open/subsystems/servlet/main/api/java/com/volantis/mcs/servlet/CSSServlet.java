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

package com.volantis.mcs.servlet;

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.cache.Cache;
import com.volantis.mcs.cache.css.CSSCacheEntry;
import com.volantis.mcs.cache.css.CSSCacheIdentity;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This servlet retrieve a @link WritableCSSEntity from the @link CSSCache and
 * renders it.
 */
public class CSSServlet
        extends HttpServlet {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(CSSServlet.class);
    /**
     * A reference to the MarinerServletApplication.
     */
    private MarinerServletApplication application;

    /**
     * The CSS Cache.
     */
    private Cache cssCache;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        ServletContext context = getServletContext();

        application = MarinerServletApplication.getInstance(context);

        cssCache =
                ApplicationInternals
                .getVolantisBean(application)
                .getCSSCache();

    }

    // Javadoc inherited from super class.
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    // Javadoc inherited from super class.
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Look up the cached CSS and return it to the client.
     *
     * @param request  The request.
     * @param response The response.
     * @throws IOException      If there was a problem writing the style sheet.
     * @throws ServletException If there was any other problem.
     */
    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {



        String base64Key = request.getParameter("key");
        if (logger.isDebugEnabled()) {
            logger.debug("Retrieving key " + base64Key)  ;
        }
        if (base64Key != null) {
            CSSCacheIdentity identity = new CSSCacheIdentity(base64Key);
            if (logger.isDebugEnabled()) {
                logger.debug("Key createTime is  " + identity.getCreateTime());
                logger.debug("Key sequenceNo is  " + identity.getSequenceNo());
            }
            CSSCacheEntry entity = (CSSCacheEntry) cssCache.retrieve(identity);
            if (entity != null) {
                response.setContentType("text/css");

                // Set caching HTTP headers.
                long nowTime = System.currentTimeMillis();
                long createTime = identity.getCreateTime();
                long expiresTime = identity.getExpiresTime();
                
                // Calculate max-age, avoiding negative values.
                long maxAgeSeconds = Math.max ((expiresTime - nowTime) / 1000, 0);
                    
                // Set HTTP 1.1 headers
                response.addHeader("Cache-Control", "max-age=" + maxAgeSeconds);
                
                // Set HTTP 1.0 headers
                response.setDateHeader("Date", createTime);
                response.setDateHeader("Expires", expiresTime);
                
                // Write HTTP contents
                PrintWriter out = response.getWriter();

                entity.getCacheObjectAsWritableCSSEntity().write(out);
                
                out.close();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            throw new ServletException("Incorrect URL Format");
        }
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Nov-05	10208/1	ianw	VBM:2005110410 Partial backout of performance fix as not needed

 08-Nov-05	10210/3	ianw	VBM:2005110410 Improve performance issues with CSS cache

 08-Nov-05	10210/1	ianw	VBM:2005110410 Improve performance issues with CSS cache

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
