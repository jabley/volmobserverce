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
 * $Header: /src/voyager/com/volantis/mcs/internal/InternalEnvironmentContext.java,v 1.4 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 14-Mar-03    Chris W         VBM:2003020607 - Fixed JavaDoc
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.internal;


import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerSessionContext;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.servlet.ServletEnvironment;
import com.volantis.shared.servlet.ServletEnvironmentFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Enumeration;

/**
 * This is the InternalEnvironmentContext that represents a non
 * servlet call to Mariner, typically used to run the MamlSAXParser in
 * an API environment.
 */
public class InternalEnvironmentContext extends EnvironmentContext {

    
    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(InternalEnvironmentContext.class);
    
    private static final String SESSION_CONTEXT_ATTRIBUTE_NAME =
            MarinerSessionContext.class.getName();

    /**
     * The request context associated with this internal request
     */
    private MarinerInternalRequestContext requestContext;
    
    /**
     * The Volantis bean associated with this internal request
     */

    private MarinerServletApplication marinerServletApplication;

    /**
     * The Volantis bean associated with this internal request
     */
    private Volantis volantisBean;

    /**
     * The instance of URLRewriter to use to encode the session id into the
     * URL if necessary.
     */
    private static URLRewriter sessionURLRewriter
        = new InternalSessionURLRewriter ();
    
    /** Creates a new instance of InternalEnvironmentContext */
    protected InternalEnvironmentContext() {
    }
    
    //javadoc inheritted
    public InternalEnvironmentContext(MarinerInternalRequestContext 
            requestContext, MarinerSessionContext sessionContext) {
        this.sessionContext = sessionContext;
        this.requestContext = requestContext;
    }
    
    //javadoc inheritted
    public void initialise(MarinerPageContext marinerPageContext) {
        this.marinerPageContext = marinerPageContext;
    }
    
    //javadoc inheritted
    public void initialiseResponse() {
        VolantisProtocol protocol = marinerPageContext.getProtocol();
        setContentType(protocol.mimeType());
    }
    
    //javadoc inheritted
    public void setContentType(String mimeType) {
        InternalResponse response = requestContext.getResponse();
        if(logger.isDebugEnabled()){
            logger.debug("Setting content type to " + mimeType);
        }
        response.setContentType(mimeType);
    }

    // javadoc inherited
    public void setCacheControlMaxAge(int maxAge) {
        InternalResponse response = requestContext.getResponse();

        if (logger.isDebugEnabled()) {
            logger.debug("Setting Cache-Control max-age to " + maxAge);
        }

        response.setCacheControlMaxAge(String.valueOf(maxAge));
    }

    //javadoc inherited
    public MarinerURL getContextPathURL() {
        return volantisBean.getBaseURL();
    }
    
    
    //javadoc inherited
    public String getExtraPathInfo() {
        return "";
    }

    //javadoc inheritted
    public String getRealPath(String separator) {
        
        return marinerServletApplication.getServletContext().getRealPath(separator);
    }
    
    //javadoc inheritted
    public String getResourceAsString(String resourceName) throws MarinerContextException {
        try {
            return marinerServletApplication.getServletContext().
            getResource(resourceName).toString();
        } catch (MalformedURLException mue) {
            throw new MarinerContextException(mue);
        }
    }
    
    /** Get the output stream to the response.
     * @return The <code>OutputStream</code> to use to write the response.
     * @throws MarinerContextException If there was a problem retrieving the
     * OutputStream.
     *
     */
    public OutputStream getResponseOutputStream() throws MarinerContextException {
        
        try {
            InternalResponse response = requestContext.getResponse();
            return response.getOutputStream();
        } catch (IOException e) {
            throw new MarinerContextException(e);
        }
    }
    
    /** Get the writer to the response.
     * @return The <code>Writer</code> to use to write the response.
     * @throws MarinerContextException If there was a problem retrieving the
     * Writer.
     *
     */
    public Writer getResponseWriter() throws MarinerContextException {

        try {
            InternalResponse response = requestContext.getResponse();
            return response.getWriter();
        } catch (IOException e) {
            throw new MarinerContextException(e);
        }
        
    }
    

    //javadoc inherited
    public URLRewriter getSessionURLRewriter() {
        return sessionURLRewriter;
    }
    
    /** Get the path of the temporary directory for the current context.
     * @return The path of the temporary directory.
     *
     */
    public String getTempDir() {
        return marinerServletApplication.getServletContext().
                getAttribute("javax.servlet.context.tempdir").toString();
    }
        
    
    /** Redirect the current request to a new url.
     * @param url The MarinerURL to which the request should be redirected, this
     * may be modified by this method.
     *
     */
    public void sendRedirect(MarinerURL url) throws IOException {
    }
        
    // Javadoc inherited from super class.
    public MarinerSessionContext getSessionContext() 
        throws RepositoryException {

        if (sessionContext == null) {
          sessionContext = createSessionContext ();
          if(logger.isDebugEnabled()){
              logger.debug ("Created " + sessionContext);
          }
        } else {
          if(logger.isDebugEnabled()){
              logger.debug ("Found " + sessionContext);
          }
        }
        return sessionContext;
    }
    
    //javadoc inherited
    public MarinerSessionContext createSessionContext ()
        throws RepositoryException {
            
        // Create a new MarinerSessionContext.
        MarinerSessionContext sessionContext
            = new MarinerSessionContext();

        return sessionContext;    
    }
    
    //javadoc inherited
    public void initialiseSession() 
        throws RepositoryException {

        marinerServletApplication = MarinerServletApplication.getInstance();
        // Get the Volantis bean.
        volantisBean = Volantis.getInstance ();
        if (volantisBean == null) {
            throw new IllegalStateException
                ("Volantis bean has not been initialised");
        }
        sessionContext = getSessionContext ();
        System.err.println("SessionContext = "+sessionContext);
    }
    
    /**
     * Set the {@link MarinerSessionContext} to null.
     */ 
    public void deleteSession() {
        sessionContext.release();
        sessionContext = null;
    }


    /**
     * @todo Not sure what to do here
     */
    public EnvironmentInteraction createRootEnvironmentInteraction() {
        EnvironmentInteraction rootInteraction = null;
        ServletEnvironmentFactory servletEnvironmentFactory = null;
        ServletEnvironment servletEnvironment = null;
                                                                                                                                                                                                    
        servletEnvironmentFactory =
                ServletEnvironmentFactory.getDefaultInstance();
                                                                                                                                                                                                    
                                                                                                                                                                                                    
        servletEnvironment = servletEnvironmentFactory.createEnvironment(
                null);
                                                                                                                                                                                                    
        // create the root interaction
        rootInteraction =
                servletEnvironmentFactory.createEnvironmentInteraction(
                        servletEnvironment,
                        null,
                        null,
                        null,
                        null);
                                                                                                                                                                                                    
        return rootInteraction;
    }

    /**
     * @todo Not sure what to do here
     */
    public void initalisePipelineContextEnvironment(XMLPipelineContext pipelineContext) {

    }

    //Javadoc inherited
    public String getHeader(String header) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call to getHeader in a non-servlet environment");
        }
        return null;
    }

    //Javadoc inherited
    public Enumeration getHeaders(String header) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call to getHeaders in a non-servlet environment");
        }
        return null;
    }

    // javadoc inherited
    public ResponseCachingDirectives getCachingDirectives() {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 27-Apr-05	7896/1	pduffin	VBM:2005042709 Removing PolicyPreference and all related classes

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/6	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 ===========================================================================
*/
