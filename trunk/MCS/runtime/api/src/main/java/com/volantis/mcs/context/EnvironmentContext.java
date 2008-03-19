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
 * $Header: /src/voyager/com/volantis/mcs/context/EnvironmentContext.java,v 1.13 2003/03/17 12:28:15 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Nov-01    Paul            VBM:2001112906 - Created.
 * 19-Dec-01    Paul            VBM:2001120506 - Added sendRedirect method.
 * 21-Dec-01    Paul            VBM:2001121702 - Added getSessionURLRewriter
 *                              and getSessionContext methods.
 * 18-Jan-02    Adrian          VBM:2001121003 - Added methods: getRealPath()
 *                              getTempDir() and getResourceAsString()
 *                              so that jsp specific code in old ChartTag
 *                              could be abstracted out of ChartElement.
 * 31-Jan-02    Paul            VBM:2001122105 - Added initialise method.
 * 25-Feb-02    Paul            VBM:2002022204 - Added getResponseWriter
 *                              method.
 * 04-Mar-02    Paul            VBM:2001101803 - Added getContextPathURL.
 * 08-Mar-02    Paul            VBM:2002030607 - Added setContentType,
 *                              getResponseOutputStream and getExtraPathInfo.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 01-Nov-02    Ian             VBM:2002091806 - Refactored session management
 *                              into EnvironmentContext.
 * 16-Dec-02    Phil W-S        VBM:2002121001 - Add method to allow the CSS
 *                              max-age cache header value to be set for use in
 *                              the response for CSS file requests. Method is
 *                              setCacheControlMaxAge.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported changes from Metis to
 *                              move session info here for MPS.
 * 14-Mar-03    Chris W         VBM:2003020607 - Fix JavaDoc in 
 *                              getSessionDevice()
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

import com.volantis.mcs.integration.URLRewriter;

import com.volantis.mcs.repository.RepositoryException;

import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.devices.InternalDevice;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.environment.EnvironmentInteractionTracker;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;


/**
 * This class provides methods which are specific to the environment that
 * mariner is running in, but which are not part of the public API. It is
 * really a work around a Java limitation which does not allow a class to
 * more finely control the classes / packages which can access individual
 * methods.
 *
 * @mock.generate
 */
public abstract class EnvironmentContext {

    // --------------------------------------------------------------------------
    // Session resources.
    // =================
    //   These resources are either initialised directly in initialiseSession, or
    //   are dependent on other session resources. Either way they are all
    //   released in releaseSession.
    // --------------------------------------------------------------------------

    /**
     * The <code>MarinerSessionContext</code> which is used in the current
     * session.
     */
    protected MarinerSessionContext sessionContext;

    /**
     * The <code>MarinerPageContext</code>.
     */
    protected MarinerPageContext marinerPageContext;

    /**
     * The <code>ExpressionContext</code> that encapsulates all the information
     * needed to evaluate an expression.
     */
    private ExpressionContext expressionContext;

    /**
     * The <code>PipelineContext</code> that encapsulates all xml pipeline
     * state. Will be null if pipeline processing is not being performed
     */
    private XMLPipelineContext pipelineContext;

    /**
     * Initialise the instance of this.
     */
    public abstract void initialise(MarinerPageContext context);

    /**
     * Initialise the response.
     * <p>
     * This method is only called by the top level page and should do any
     * initialisation of the response that is needed. This includes setting
     * the content type and clearing the buffer.
     * </p>
     */
    public abstract void initialiseResponse();

    /**
     * Initialise the session
     */
    public abstract void initialiseSession() throws RepositoryException;

    /**
     * Set the content type.
     * <p>
     * This method is only called by the top level page and should set
     * the content type.
     * </p>
     * @param mimeType The mime type to set.
     */
    public abstract void setContentType(String mimeType);

    /**
     * Get the writer to the response.
     * @return The <code>Writer</code> to use to write the response.
     * @throws MarinerContextException If there was a problem retrieving the
     * Writer.
     */
    public abstract Writer getResponseWriter()
            throws MarinerContextException;

    /**
     * Get the output stream to the response.
     * @return The <code>OutputStream</code> to use to write the response.
     * @throws MarinerContextException If there was a problem retrieving the
     * OutputStream.
     */
    public abstract OutputStream getResponseOutputStream()
            throws MarinerContextException;

    /**
     * Redirect the current request to a new url.
     * @param url The MarinerURL to which the request should be redirected, this
     * may be modified by this method.
     */
    public abstract void sendRedirect(MarinerURL url)
            throws IOException;

    /**
     * Get an instance of URLRewriter which should be used to encode session
     * information in the url if the browser does not support cookies.
     * <p>
     * It should only be given urls which are known to be within the same
     * session as the current request. Only the mapToExternalURL method should
     * be implemented for the moment. The other method must throw an
     * <code>UnsupportedOperationException</code>.
     * </p>
     * @return The <code>URLRewriter</code>.
     */
    public abstract URLRewriter getSessionURLRewriter();

    /**
     * Get the MarinerSessionContext to use in the current environment.
     * <p>
     * This will call {@link #createSessionContext} if necessary.
     * </p>
     * @return The <code>MarinerSessionContext</code>.
     */
    public abstract MarinerSessionContext getSessionContext()
            throws RepositoryException;

    /**
     * Get the retrieved MarinerSessionContext to use in the current environment.
     * @return The <code>MarinerSessionContext</code>.
     */
    public MarinerSessionContext getCurrentSessionContext() {
        return sessionContext;
    }

    /**
     * Get the absolute path to the current context on the application server
     * for creation of absolute URL information.
     * @param separator The path separator for the returned path
     * @return The current context path
     */
    public abstract String getRealPath(String separator);

    /**
     * Get the context path URL of the current application.
     * <p>
     * The context path of the current application is that part of the url
     * which the application server uses to determine what application to
     * run.
     * </p><p>
     * The returned url has a path which does end with /.
     * </p>
     * @return The current context path of the current application. This url is
     * read only.
     */
    public abstract MarinerURL getContextPathURL();

    /**
     * Get any extra path info associated with the current request.
     * <p>
     * Only part of the path in the request URL may have been used to identify
     * the servlet to run. The path which comes after that is the extra path
     * info.
     * </p>
     * @return The extra path info which is null if there is none and a string
     * which does not start with a / otherwise.
     */
    public abstract String getExtraPathInfo();

    /**
     * Get the path of the temporary directory for the current context.
     * @return The path of the temporary directory.
     */
    public abstract String getTempDir();

    /**
     * Get the name of a resource from the current context
     * @param resourceName The name of the requested resource
     * @return The resource string.
     */
    public abstract String getResourceAsString(String resourceName)
            throws MarinerContextException;

    /**
     * Get the device associated with this session.
     */
    public InternalDevice getSessionDevice() {
        return sessionContext.getDevice();
    }

    /**
     * Create a new MarinerSessionContext.
     * @return The newly created MarinerSessionContext.
     */
    abstract public MarinerSessionContext createSessionContext()
            throws RepositoryException;

    /**
     * Returns the ExpressionContext that contains all the information we need
     * to evaluate expressions.
     * @return ExpressionContext
     */
    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    /**
     * Sets the ExpressionContext that contains all the information needed to
     * evaluate expressions.
     * @param context The ExpressionContext.
     */
    public void setExpressionContext(ExpressionContext context) {
        expressionContext = context;
    }

    /**
     * Returns the <code>XMLPipelineContext</code> associated with the
     * current environment or null if pipeline processing is not being
     * preformed.
     * @return an XMLPipelineContext or null if pipeline processing is not
     * being performed
     */
    public XMLPipelineContext getPipelineContext() {
        return pipelineContext;
    }

    /**
     * Set the <code>XMLPipelineContext</code> that is associated with the
     * current environment.
     * @param pipelineContext the XMLPipelineContext to set.
     */
    public void setPipelineContext(XMLPipelineContext pipelineContext) {
        this.pipelineContext = pipelineContext;
    }

    /**
     * Initialises environment specific properties on the PipelineContext.
     * @param pipelineContext The pipeline context to initalise.
     */
    abstract public void initalisePipelineContextEnvironment(XMLPipelineContext pipelineContext);

    /**
     * Create an environment specific Root EnvironmentInteraction.
     *
     * @return The environment specific Root EnvironmentInteraction.
     */
    abstract public EnvironmentInteraction createRootEnvironmentInteraction();

    /**
     * Push the current EnvironmentInteraction
     * onto the stack of EnvironmentInteractions if required.
     * @param envTracker EnvironmentInteractionTracker
     */
    public void pushEnvironmentInteraction(EnvironmentInteractionTracker envTracker) {
        // Default do nothing
    }

    /**
     * Retrieves the named header from the specific Environment.
     * @param header The name of the header
     * @return The value of the header or null if not found
     */
    abstract public String getHeader(String header); 

    /**
     * Retrieves the named header from the specific Environment.
     * @param header The name of the header
     * @return An Enumeration of values of the header or null if not found
     */
    abstract public Enumeration getHeaders(String header);

    /**
     * Set the value of an attribute on the context
     * @param attributeName
     * @param attributeValue
     */
    public void setAttribute(String attributeName, Object attributeValue) {
        //default does nothing
    }

    /**
     * Returns the collected caching directives for the response;
     *
     * @return may return null if no cache-related information can be collected
     */
    public abstract ResponseCachingDirectives getCachingDirectives();

    /**
     * Applies the collected caching directives. The stored caching directives
     * become unmodifiable after applying them to the response.
     */
    public void applyCachingDirectives() {
        // do nothing
    }
    
    /**
     * Returns mime type from passed file name
     * If it is not implemented in inherited class null is returned 
     * 
     * @param name file name
     * @return mime type recognized from file extension or null if extension not
     * exists in web.xml
     */
    public String getMimeType(String name) {
    	return null;
    }
}
