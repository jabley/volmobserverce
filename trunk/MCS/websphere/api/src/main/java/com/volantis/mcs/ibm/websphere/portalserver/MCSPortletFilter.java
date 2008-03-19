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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.apache.jetspeed.portlet.Client;
import org.apache.jetspeed.portlet.PortletException;
import org.apache.jetspeed.portlet.PortletRequest;
import org.apache.jetspeed.portlet.PortletResponse;

import our.w3c.tidy.Tidy;

import com.ibm.wps.pe.om.definition.ContentType;
import com.ibm.wps.pe.om.definition.PortletDefinition;
import com.ibm.wps.pe.om.definition.ServletDefinition;
import com.ibm.wps.pe.pc.legacy.cmpf.ClientWrapper;
import com.ibm.wps.pe.pc.legacy.cmpf.PortletFilterAdapter;
import com.ibm.wps.pe.pc.legacy.cmpf.PortletFilterChain;
import com.ibm.wps.pe.pc.legacy.cmpf.PortletFilterConfig;
import com.ibm.wps.pe.pc.legacy.cmpf.PortletRequestWrapper;
import com.ibm.wps.pe.pc.legacy.cmpf.PortletResponseWrapper;
import com.ibm.wps.pe.pc.legacy.core.PortletUtils;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;


/**
 * Implementation of a filter for portlets which surrounds the portlets
 * with markup from the configuration file.
 *
 * The filter is to allow non-MAML portlets to be rendered on a MAML portal.
 * In order to enable this filter, the correct filtername (from
 * PortletFilterService.properties) must be added to the portlet configuration
 * using the FilterChain attribute.
 *
 * The configuration lists preamble markup which is output before the portlet
 * markup and postamble markup which is output after the portlet.
 * <p>
 * The format of the configuration file is as follows:
 *
 * <pre>
 * filtername3=MCSPortletFilter
 * MCSPortletFilter.classname=com.volantis.mcs.ibm.websphere.portalserver.MCSPortletFilter
 * MCSPortletFilter.inputMarkup= xhtml
 * MCSPortletFilter.transcodeMarkup.1 = xhtml->xdime
 * MCSPortletFilter.MCS.preamble.1 = <usePipeline>
 * MCSPortletFilter.MCS.preamble.2 = <pipeline:transform href="xslt filename">
 * MCSPortletFilter.MCS.preamble.3 = <content>
 * MCSPortletFilter.MCS.postamble.1 = </content>
 * MCSPortletFilter.MCS.postamble.2 = </pipeline:transform>
 * MCSPortletFilter.MCS.postamble.3 = </usePipeline>
 * </pre>
 *
 * The inputMarkup is used to tell the portlet what markup to generate.
 *
 * If the from markup is provided by the portlet and the to markup is supported
 * by the client, the transcode will be performed.
 * Note that, although this class does not use the transcodeMarkup parameter,
 * it is needed by the Websphere Portlet container and is used to pick which 
 * portlets are viewable.  It must not, therefore, be removed from the 
 * configuration.
 * </p>
 *
 *
 * @author  mat
 */
public class MCSPortletFilter extends PortletFilterAdapter {

    /**
     * The copyright statement.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MCSPortletFilter.class);

    /**
     * Constant for the xdime markup
     */
    private static final String XDIME_MARKUP = "xdime";

    /**
     * The JTidy object.
     */
    private static Tidy tidy;

    /**
     * Initialise JTidy.
     */
    static {
        tidy = new Tidy();
        tidy.setQuoteNbsp(false);
        tidy.setDocType("omit");
        tidy.setNumEntities(true);
        tidy.setShowWarnings(false);
        tidy.setQuiet(true);
        tidy.setWraplen(0);
        tidy.setXHTML(true);
    }

    /**
     * The nane of the filter
     */
    private String filterName;

    /**
     * The markup to output before the portlet markup
     */
    private StringBuffer preamble = new StringBuffer();

     /**
     * The markup to output after the portlet markup
     */
    private StringBuffer postamble = new StringBuffer();


    /**
     * The markup that the portlet should provide
     */
    private String inputMarkup = null;

    /** Creates a new instance of MCSPortletFilter */
    public MCSPortletFilter() {
    }

    /**
     * Initialise this filter
     *
     * @param filterConfig The filterConfig
     * @throws PortletException A problem in the servlet
     *
     */
    public void init(PortletFilterConfig filterConfig)
        throws PortletException {
        super.init(filterConfig);
        this.filterName = filterConfig.getFilterName();

        inputMarkup = filterConfig.getInitParameter("inputMarkup");
        if (logger.isDebugEnabled()) {
            logger.debug(filterName + ": inputMarkup: - " + inputMarkup);
        }

        // Check that the inputMarkup has been specified
        if(inputMarkup == null) {
            throw new PortletException("No inputMarkup specified for " +
                                       filterName);
        }

        // Read in the preamble from the configuration
        String value;

        for (int i = 1;
            (value = filterConfig.getInitParameter("MCS.preamble." + i))
                != null;
            i++) {
            preamble.append(value);
            preamble.append('\n');
        }



        // and the postamble
        for (int i = 1;
            (value = filterConfig.getInitParameter("MCS.postamble." + i))
                != null;
            i++) {
            postamble.append(value);
            postamble.append('\n');
        }

        if(logger.isDebugEnabled()) {
            logger.debug(filterName + ": Preamble: -\n" + preamble.toString());
            logger.debug(filterName + ": Postamble: -\n" +
                         postamble.toString());
            logger.debug(filterName + ": initialised");
        }
    }

    // Javadoc inherited
    public void doService(
        PortletRequest request,
        PortletResponse response,
        PortletFilterChain chain)
        throws PortletException, IOException {

        if(logger.isDebugEnabled()) {
            logger.debug("MCSPortletFilter doService");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Checking portlet "
                         + request.getPortletSettings().getTitle(
                                     request.getLocale(),
                                     request.getClient()));
        }

        // this is the markup that the client is requesting
        String clientMarkup = request.getClient().getMarkupName();
        if(logger.isDebugEnabled()) {
            logger.debug("Clientmarkup is " + clientMarkup);
        }

        // check to see if we need to transcode
        if (!XDIME_MARKUP.equalsIgnoreCase(clientMarkup)) {
            // The client has not requeted XDIME markup
            if (logger.isDebugEnabled()) {
                logger.debug("Not transcoding as client has not requested " +
                             "XDIME markup");
            }
            // Process the portlet
            chain.doFilter(request, response);
        } else if (!portletProvides(request, inputMarkup)) {
            // cannot transcode as the portlet does not support the input
            // markup
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot transcode to xdime as the portlet does " +
                             "not provide the input markup type: " +
                             inputMarkup);
            }
        } else if (XDIME_MARKUP.equalsIgnoreCase(inputMarkup)) {
            // the portlet already provides XDIME so we do not need to
            // transcode
            if (logger.isDebugEnabled()) {
                logger.debug("Portlet already provideds XDIME so no need to " +
                             "transcode");
            }
            // Process the portlet
            chain.doFilter(request, response);            
        } else {
            // we can transcode
            if (logger.isDebugEnabled()) {
                logger.debug("Transcoding from " + inputMarkup + " to " +
                             XDIME_MARKUP);
            }
            PrintWriter writer = response.getWriter();

            // Create a response to hold the markup generated by the portlet
            if (logger.isDebugEnabled()) {
                logger.debug("Portlet Encoding is :" + 
                    response.getCharacterEncoding());
            }

            MCSBufferedPortletResponse mcsResponse = 
                new MCSBufferedPortletResponse(response);

            MCSPortletRequest mcsRequest =
                        createMCSPortletRequest(request);

            // Process the portlet
            chain.doFilter(mcsRequest, mcsResponse);

            // Now write the preamble to the real response
            writer.write(preamble.toString());
            // And then write the contents of our buffered response
            // If the portlet is HTML, we pass it through JTidy
            if (inputMarkup.equalsIgnoreCase("HTML")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Tidying HTML");
                }
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ByteArrayInputStream bais = new
                    ByteArrayInputStream(
                        mcsResponse.toString().getBytes(
                            response.getCharacterEncoding())
                        );
                tidy.parse(bais,
                           output);
                writer.write(output.toString());
            } else {
                writer.write(mcsResponse.toString());
            }
            // and then the postamble
            writer.write(postamble.toString());
        }
    }

    /**
     * Check whether the portlet provides the given markup
     *
     * @param request The portlet request
     * @param markup The markup
     * @return True if the markup is provided by the portlet
     */
    protected boolean portletProvides(PortletRequest request, String markup)
    {
        for(Iterator iterator =
            ((ServletDefinition) getPortletDefinitionFromRequest(request).
            getServletDefinition()).getContentTypeSet().iterator();
            iterator.hasNext();)
        {
            ContentType contentType = (ContentType)iterator.next();
            if(contentType.getContentType().equalsIgnoreCase(markup)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the portlets definition from the request
     *
     * @param request The portlet request
     * @return The PortletDefinition
     */
    protected PortletDefinition getPortletDefinitionFromRequest(
                PortletRequest request)
    {
        return (PortletDefinition)PortletUtils.getInternalRequest(request).
                getPortletWindow().getPortletEntity().getPortletDefinition();
    }




   /**
   * The data written to this response will be saved in a buffer
   *
   */
    private class MCSBufferedPortletResponse extends PortletResponseWrapper {
               
        /**
         * our internal writer
         */
        private StringWriter stringWriter = null;
        /**
         * a buffered output writer
         */
        private PrintWriter writer = null;
        

        /**
         * create a new wrapper around the response
         * which buffers the response from the portlet
         *
         * @param resp   the original response
         */
        public MCSBufferedPortletResponse(
            PortletResponse resp) {
            super(resp);
            
            stringWriter = new StringWriter();
            writer = new PrintWriter(stringWriter);
        }
        

        /**
         * overload response functionality with buffered implementation
         *
         * @return a buffered writer
         * @exception IOException
         */
        public PrintWriter getWriter() throws IOException {
            return writer;
        }

        /**
         * return the output of the portlet as String
         *
         * @return a String holding the output of the portlet
         */
        public String toString() {
            writer.flush();
            return stringWriter.toString();
        }

    }

    /**
     * A client wrapper which wraps a client and overrides the getMarkupName
     * so that the markup generated by the client can be specified.
     *
     * @see ClientWrapper
     */
    private class InputMarkupClient
        extends ClientWrapper {

        /** The markup name */
        private String markupName = null;

        /**
         * creates an new wrapper around the original client,
         * which change the clients markup to the given markup.
         *
         * @param client the original client which will be wrapped
         * @param markupName The markup for this client
         */
        public InputMarkupClient(Client client, String markupName) {
            super(client);
            this.markupName = markupName;
        }

        /**
         * Returns the name of the markup that this client supports.
         *
         * @return   the markup name
         */
        public String getMarkupName() {
            return markupName;
        }
    }

    /**
     * A new PortletRequest which wraps the original one and returns a modified
     * client and a provider.
     *
     * @see PortletRequestWrapper
     */
    private class MCSPortletRequest
        extends PortletRequestWrapper {

        /**
         * the new client
         */
        private ClientWrapper inputMarkupClient = null;

        /**
         *  creates a new wrapper around the original request
         *
         * @param req the original request
         * @param client the wrapped client
         */
        public MCSPortletRequest(
            PortletRequest req,
            ClientWrapper client) {
            super(req);
            inputMarkupClient = client;
        }

        /**
         * return the modified client
         *
         * @return the client of type org.apache.jetspeed.portlet.Client
         */
        protected ClientWrapper getClientWrapper() {
            return inputMarkupClient;
        }
    }

    // Javadoc inherited
    public void doTitle(
        PortletRequest request,
        PortletResponse response,
        PortletFilterChain filterChain)
        throws PortletException, IOException {

        MCSPortletRequest mcsRequest =
            createMCSPortletRequest(request);
        super.doTitle(mcsRequest, response, filterChain);
    }

    /**
     * Create an MCSBufferedPortletRequest from the PortletRequest
     *
     * @param request The portlet request
     * @return The MCSBufferedPortletRequest
     */
    private MCSPortletRequest createMCSPortletRequest(
        PortletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("Markup required from portlet " + inputMarkup);
        }
        return new MCSPortletRequest(
            request,
            new InputMarkupClient(request.getClient(), inputMarkup));
    }

    // Javadoc inherited
    public void doActionEvent(
        PortletRequest request,
        PortletResponse response,
        PortletFilterChain filterChain)
        throws PortletException, IOException {
            MCSPortletRequest mcsRequest =
                createMCSPortletRequest(request);
        super.doActionEvent(mcsRequest, response, filterChain);
    }

    // Javadoc inherited
    public void doBeginPage(
        PortletRequest request,
        PortletResponse response,
        PortletFilterChain filterChain)
        throws PortletException, IOException {
            MCSPortletRequest mcsRequest =
                createMCSPortletRequest(request);
        super.doBeginPage(mcsRequest, response, filterChain);
    }

    // Javadoc inherited
    public void doEndPage(
        PortletRequest request,
        PortletResponse response,
        PortletFilterChain filterChain)
        throws PortletException, IOException {
            MCSPortletRequest mcsRequest =
                createMCSPortletRequest(request);
        super.doEndPage(mcsRequest, response, filterChain);
    }

    // Javadoc inherited
    public void doLogin(
        PortletRequest request,
        PortletResponse response,
        PortletFilterChain filterChain)
        throws PortletException, IOException {
            MCSPortletRequest mcsRequest =
                            createMCSPortletRequest(request);
        super.doLogin(mcsRequest, response, filterChain);
    }

    // Javadoc inherited
    public void doMessageEvent(
        PortletRequest request,
        PortletResponse response,
        PortletFilterChain filterChain)
        throws PortletException, IOException {
            MCSPortletRequest mcsRequest =
                createMCSPortletRequest(request);
        super.doMessageEvent(mcsRequest, response, filterChain);
    }

    // Javadoc inherited
    public void doWindowEvent(
        PortletRequest request,
        PortletResponse response,
        PortletFilterChain filterChain)
        throws PortletException, IOException {
            MCSPortletRequest mcsRequest =
                createMCSPortletRequest(request);
        super.doWindowEvent(mcsRequest, response, filterChain);
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8389/1	emma	VBM:2005051808 Merge from 330 - Portlets accessed via MCSPortletFilter now render even when no transcoding is required

 20-May-05	8375/1	emma	VBM:2005051808 Bug fix - Portlets accessed via MCSPortletFilter now render even when no transcoding is required

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 17-May-04	4444/1	ianw	VBM:2004051210 Made mcsResponse local to doService method

 12-May-04	4323/8	ianw	VBM:2004043004 Removed character encoding from MCSPortletFilter

 11-May-04	4141/4	ianw	VBM:2004043004 Removed encoding issues from Portlet Filter

 24-Feb-04	3188/1	mat	VBM:2004022402 Change maml to xdime

 19-Feb-04	3073/2	mat	VBM:2004012716 Change Javadoc and ignoreCase on HTML match

 17-Feb-04	2980/2	doug	VBM:2004012716 Removed need for the transcodeMarkup config parameter

 13-Feb-04	2990/1	doug	VBM:2004021201 Changed logging to use a log4j logger

 15-Dec-03	2191/1	mat	VBM:2003121002 Override methods that take the PortletRequest

 02-Dec-03	2039/4	mat	VBM:2003112606 Change the inner classes to be private

 02-Dec-03	2039/2	mat	VBM:2003112606 Set the portlet markup using the inputMarkup config parameter

 24-Oct-03	1439/5	mat	VBM:2003091805 Added jtidy for XHTML portlets

 15-Oct-03	1439/3	mat	VBM:2003091805 Rework changes

 13-Oct-03	1439/1	mat	VBM:2003091805 Added WebSphere PortletFilter

 ===========================================================================
*/
