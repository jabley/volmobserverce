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

package com.volantis.mcs.ibm.websphere.portalserver;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.ibm.wps.engine.RunData;
import com.ibm.wps.engine.portalfilter.PortalFilter;
import com.ibm.wps.engine.portalfilter.PortalFilterChain;
import com.ibm.wps.engine.portalfilter.PortalFilterConfig;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.servlet.MCSFilter;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

/**
 * Class that can be plugged into the IBM WebSphere Portal Server servlet chain.
 * It takes the contents of the response, which will contain Marlin markup
 * and parses it into the correct device specific markup.
 * 
 * @author mat
 */
public class MCSPortalFilter implements PortalFilter {
    
    /**
     * The copyright statement.
     */
     private static String mark =
         "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MCSPortalFilter.class);

    /**
     * The exception localizer used by this class.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(MCSFilter.class);

    /**
     * Holds configuration information for the filter.
     */
    private PortalFilterConfig filterConfig;


    /**
     * Create a new MCSPortalFilter
     */
    public MCSPortalFilter() {
    }

    // javadoc inherited
    public void init(PortalFilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * This method will be called during processing of the filter chain.
     * We will be passed a response containing the Marlin markup for the page.
     * This needs to be parsed by Mariner into the correct markup for the 
     * device.
     * If Nariner has not been started, this method will initialise it.
     * 
     * An XMLPipelineContext will be created, with the base URI set to the
     * value of 
     * 
     * <pre>filterConfig.getServletContext().getResource("/")</pre>
     * 
     * This will normally point to the location of the WPS web application.
     * 
     * @param request The request
     * @param response The response
     * @param portalFilterChain The chain for the portal
     * @throws ServletException Servlet problem
     * @throws IOException An IO problem
     * 
     */
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         PortalFilterChain portalFilterChain)
        throws ServletException, IOException {
            
        // Get the MarinerServletApplication.  This will initialise
        // MCS if it hasn't already been done.
        MarinerServletApplication application = MarinerServletApplication.
            getInstance(filterConfig.getServletContext());

        // The response is a HTTP response.
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // The request is a HTTP request.
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Get the character set that WPS has selected for the response.
        String characterSet = RunData.from(request).getCharSet();
        if (logger.isDebugEnabled()) {
            logger.debug("WPS selected character set is " + characterSet);
        }

        // Create a wrapper for the response
        MCSPortalResponseWrapper responseWrapper =
            new MCSPortalResponseWrapper(httpResponse, characterSet);

        // Create a wrapper for the request
        // This is only required for MCS as we need it to fool MCS into using
        // the charset set by portal and not the one set by the device.
        MCSPortalRequestWrapper requestWrapper =
            new MCSPortalRequestWrapper(httpRequest, characterSet);                

        // Process the response down the chain
        portalFilterChain.doFilter(request, responseWrapper);
        
        responseWrapper.flushBuffer();
        
        // Check the status on the response
        int responseStatus = responseWrapper.getStatus();
        if (responseStatus >= 300 && responseStatus < 400) {
                
                if(logger.isDebugEnabled()) {
                    logger.debug(
                    "Received redirect HTTP response header.  Returning without action.");
                }
                
            return;
        }
        
        if(logger.isDebugEnabled()) {
            logger.debug("Contents received from portal: "
                         + responseWrapper.getContentsAsString());
        }



        try {
            
            MarinerServletRequestContext servletRequestContext =
                new MarinerServletRequestContext(
                    filterConfig.getServletContext(),
                    requestWrapper,
                    response);
            
            // Parse the contents of the response as Marlin markup.
            // The processed page will be written to the response, so
            // we don't need to do anything further.
            XMLReader xmlReader = MarlinSAXHelper.getXMLReader(servletRequestContext, null);

            // todo: The following baseURI initialisation code should be
            // unnecessary if the system id is properly set on the InputSource.
            // However, this needs to be tested properly before changing it as
            // there were some problems with the pipeline.
            EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(servletRequestContext);

            // The getXMLReader() call above will set up the pipelineContext in
            // the environmentContext.  See MarlinSAXHelper.setPipelineContext()
             XMLPipelineContext pipelineContext = environmentContext.getPipelineContext();
             // set the Base URI in the pipeline's context
             try {
                 URL baseURI = filterConfig.getServletContext().getResource("/");
                 String baseURIAsString = baseURI.toExternalForm();
                 if (logger.isDebugEnabled()) {
                     logger.debug("Setting Base URI " + baseURIAsString);
                 }
                 pipelineContext.pushBaseURI(baseURIAsString);
             } catch (MalformedURLException e) {
                 throw new ServletException(e);
             }

            // Treat the response wrapper as an InputSource for the XML parser.
            Reader reader = responseWrapper.getReader();
            InputSource inputSource = new InputSource(reader);
            //inputSource.setSystemId(baseURIAsString);

            // Parse the response and process it as XDIME.
            xmlReader.parse(inputSource);
                
            servletRequestContext.release();
       } catch (MarinerContextException e) {
            logger.error("portal-filter-exception",
                    new Object[]{responseWrapper.getContentsAsString()});
            logger.error("mariner-context-exception", e);
            throw new ServletException(
                    exceptionLocalizer.format("mariner-context-exception"),
                    e);
        } catch (SAXException se) {
            logger.error("portal-filter-exception",
                    new Object[]{responseWrapper.getContentsAsString()});
            logger.error("sax-exception-caught", se);
            Exception cause = se.getException();
            // Check the root cause of the SAXException, as it may not be
            // logged correctly.
            if (cause != null) {
                logger.error("root-cause", cause);
                throw new ServletException(
                        exceptionLocalizer.format("root-cause"),
                        cause);
            }
            throw new ServletException(
                    exceptionLocalizer.format("sax-exception-caught"),
                    se);
        }
    }

    /**
     * Destroy this filter
     * 
     */
    public void destroy() {
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Jan-05	6760/1	tom	VBM:2005011709 Fix branding identity handling and add WebSphere portal filter errors to include output of received content

 13-Jan-05	6655/4	pcameron	VBM:2005011103 Fixes to servlet filter

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Sep-04	5622/1	ianw	VBM:2004052202 ported forward IBM charset fixes

 23-Sep-04	5620/1	ianw	VBM:2004052202 ported forward IBM charset fixes

 25-May-04	4537/1	ianw	VBM:2004052202 Set MCS accept-charset to be specified by WPS

 21-Apr-04	3988/1	pduffin	VBM:2004042107 Attempted fix for Japanese character problem in MCSPortalFilter

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Oct-03	1439/3	mat	VBM:2003091805 Rework changes

 13-Oct-03	1439/1	mat	VBM:2003091805 Added WebSphere PortletFilter

 17-Sep-03	1360/8	mat	VBM:2003090502 Integrate with Websphere portalserver.

 17-Sep-03	1360/6	mat	VBM:2003090502 Integrate with Websphere portalserver.

 17-Sep-03	1360/4	mat	VBM:2003090502 Integrate with WebSphere Portal

 15-Sep-03	1360/1	mat	VBM:2003090502 Integrate with Websphere portalserver.

 ===========================================================================
*/
