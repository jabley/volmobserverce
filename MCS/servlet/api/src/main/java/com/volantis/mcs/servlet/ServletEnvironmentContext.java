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
 * $Header: /src/voyager/com/volantis/mcs/servlet/ServletEnvironmentContext.java,v 1.17 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Dec-01    Paul            VBM:2001121702 - Created.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Jan-02    Adrian          VBM:2001121003 - Added methods: getRealPath()
 *                              getTempDir() and getResourceAsString()
 *                              so that jsp specific code in old ChartTag
 *                              could be abstracted out of ChartElement
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to allow these
 *                              instances to be created before initialising
 *                              the MarinerServletRequestContext.
 * 21-Feb-02    Allan           VBM:2002022007 - Changed logger.info() to
 *                              logger.debug() in initialiseResponse().
 * 25-Feb-02    Paul            VBM:2002022204 - Added implementation of
 *                              getResponseWriter method.
 * 04-Mar-02    Paul            VBM:2001101803 - Implemented getContextPathURL.
 * 08-Mar-02    Paul            VBM:2002030607 - Implemented the methods
 *                              setContentType, getResponseOutputStream and
 *                              getExtraPathInfo.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02    Allan           VBM:2002032801 - Modified getContextPathURL()
 *                              to set contextPath to "/" if it is null or has
 *                              0 length.
 * 01-Nov-02    Ian             VBM:2002091806 - Moved session management here
 *                              from MarinerPageContext.
 * 11-Nov-02    Mat             VBM:2002103010 - Removed unnecessary import
 *                              of ApplicationContextFactory.
 * 16-Dec-02    Phil W-S        VBM:2002121001 - Add new overriding method
 *                              setCacheControlMaxAge.
 * 17-Dec-02    Allan           VBM:2002121711 - Modified setContentType() to
 *                              use the contentTypeCharSet property of the
 *                              MarinerRequestContext if it is set.
 * 20-Jan-03    Allan           VBM:2002121901 - Modified setContentType() to
 *                              use getCharacterEncoding() instead of
 *                              getContentTypeCharSet().
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 19-Feb-03    Phil W-S        VBM:2003021707 - Add compositeContentType with
 *                              WLS6.1 charset fix and update setContentType to
 *                              call this method.
 * 20-Feb-03    Phil W-S        VBM:2003021707 - Rework to correct behaviour to
 *                              match that identified by decompiling the
 *                              (broken) WLS class that handles the charset
 *                              definition on ContentType.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

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
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.number.LongHelper;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.servlet.ServletEnvironment;
import com.volantis.shared.servlet.ServletEnvironmentFactory;
import com.volantis.shared.time.Time;
import com.volantis.shared.time.Period;
import com.volantis.shared.system.SystemClock;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This class implements servlet specific functionality which is used
 * internally in the core. It is not part of the public API.
 */
public class ServletEnvironmentContext extends EnvironmentContext {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ServletEnvironmentContext.class);

    /**
     * The instance of URLRewriter to use to encode the session id into the
     * URL if necessary.
     */
    private static final URLRewriter SESSION_URL_REWRITER =
        new ServletSessionURLRewriter ();

    /**
     * The key to use in the HttpSession to store and retrieve the
     * MarinerSessionContext object.
     */
    private static final String SESSION_CONTEXT_ATTRIBUTE_NAME =
        MarinerServletSessionContext.class.getName ();

    /**
     * Set of header names for the value of the Vary response header when
     * caching is enabled.
     */
    public static final Set VARY_HEADER_NAMES;

    /**
     * The value of the Vary response header when network edge caching of
     * responses is enabled.
     */
    private static final String VARY_HEADER_VALUE;

    static {
        VARY_HEADER_NAMES = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        VARY_HEADER_NAMES.add("User-Agent");
        VARY_HEADER_NAMES.add("User_Agent");
        VARY_HEADER_NAMES.add("Profile");
        VARY_HEADER_NAMES.add("X-WAP-Profile");
        VARY_HEADER_NAMES.add("UA-Pixels");
        VARY_HEADER_NAMES.add("UA-Color");
        VARY_HEADER_NAMES.add("UA-CPU");
        VARY_HEADER_NAMES.add("UA-OS");
        VARY_HEADER_NAMES.add("UA-Voice");
        VARY_HEADER_NAMES.add("Accept");

        final StringBuffer buffer = new StringBuffer();
        for (Iterator iter = VARY_HEADER_NAMES.iterator(); iter.hasNext(); ) {
            final String headerName = (String) iter.next();
            buffer.append(headerName);
            if (iter.hasNext()) {
                buffer.append(",");
            }
        }
        VARY_HEADER_VALUE = buffer.toString();
    }

  /**
   * The <code>MarinerServletRequestContext</code>.
   */
  private MarinerServletRequestContext requestContext;

  /**
   * The context path URL.
   */
  private MarinerURL contextPathURL;

  /**
   * The extra path info.
   */
  private String extraPathInfo;


  protected Volantis volantisBean;
    private ResponseCachingDirectives cachingDirectives;

    /**
     * Create a new <code>ServletEnvironmentContext</code>.
     * @param requestContext The <code>MarinerServletRequestContext</code>.
     */
    public ServletEnvironmentContext (
        MarinerServletRequestContext requestContext) {

      this.requestContext = requestContext;
    }

  /**
   * Initialise this <code>ServletEnvironmentContext</code>.
   * @param marinerPageContext The <code>MarinerPageContext</code>.
   */
  public void initialise (MarinerPageContext marinerPageContext) {
    this.marinerPageContext = marinerPageContext;

  }

    /**
     * This method sets the content type.
     */
    public void initialiseResponse () {
        VolantisProtocol protocol = marinerPageContext.getProtocol ();
        setContentType (protocol.mimeType ());
    }

    /**
     * This method sets the content type.
     */
    public void setContentType(String mimeType) {
        ServletResponse response = requestContext.getResponse();

        String charSet = requestContext.getCharacterEncoding();
        String contentType = compositeContentType(mimeType, charSet);

        if (logger.isDebugEnabled()) {
            logger.debug("Setting content type to " +
                         contentType);
        }

        response.setContentType(contentType);
    }

    /**
     * Generates the content type from the given MIME type and character
     * encoding.
     *
     * @param mimeType the MIME type for the response
     * @param charEncoding the character encoding for the response
     * @return the composite content type
     */
    protected String compositeContentType(String mimeType,
                                          String charEncoding) {
        String result = mimeType;

        if (charEncoding != null) {
            // Add the character set to the content type
            StringBuffer contentType = new StringBuffer(mimeType.length() +
                                                        charEncoding.length() +
                                                        9);
            contentType.append(mimeType).
                append(";charset=").append(charEncoding);

            result = contentType.toString();
        }

        return result;
    }

    /**
     * This method returns the Writer from the response .
     */
    public Writer getResponseWriter ()
      throws MarinerContextException {

      try {

        ServletResponse response = requestContext.getResponse ();
        return response.getWriter ();
      }
      catch (IOException e) {
        throw new MarinerContextException (e);
      }
    }

  /**
   * This method returns the OutputStream from the response.
   */
  public OutputStream getResponseOutputStream ()
    throws MarinerContextException {

    try {
      ServletResponse response = requestContext.getResponse ();
      return response.getOutputStream ();
    }
    catch (IOException e) {
      throw new MarinerContextException (e);
    }
  }

  // Javadoc inherited from super class.
  public void sendRedirect (MarinerURL url)
    throws IOException {

    HttpServletResponse response = requestContext.getHttpResponse ();

    String path = url.getPath ();
    path = response.encodeRedirectURL (path);
    url.setPath (path);

    response.sendRedirect (url.getExternalForm ());
  }

  // Javadoc inherited from super class.
  public URLRewriter getSessionURLRewriter () {
    return SESSION_URL_REWRITER;
  }

  /**
   * Get the MarinerSessionContext from the HttpSession.
   * @param session The HttpSession which may contain the
   * MarinerSessionContext.
   * @return The MarinerSessionContext, or null.
   */
  protected MarinerSessionContext getSessionContext (HttpSession session) {
    return (MarinerSessionContext) session.getAttribute
      (SESSION_CONTEXT_ATTRIBUTE_NAME);
  }

  /**
   * Set the MarinerSessionContext in the HttpSession.
   * @param session The HttpSession to which the MarinerSessionContext should
   * be added.
   * @param sessionContext The MarinerSessionContext to store.
   */
  protected void setSessionContext (HttpSession session,
                                    MarinerSessionContext sessionContext) {
    session.setAttribute (SESSION_CONTEXT_ATTRIBUTE_NAME, sessionContext);
  }

    // Javadoc inherited from super class.
    public MarinerSessionContext getSessionContext()
            throws RepositoryException {

        if (sessionContext != null) {
            return sessionContext;
        }
        // Get the http servlet request.
        HttpServletRequest httpRequest = requestContext.getHttpRequest();

        // Get the session
        HttpSession session = httpRequest.getSession(true);
        synchronized (session) {
            // Get the MarinerSessionContext from the session, if we could not
            // find one then we need to create one.
            sessionContext = getSessionContext(session);
            if (sessionContext == null) {
                // Create the MarinerSessionContext.
                sessionContext = createSessionContext();

                setSessionContext(session, sessionContext);

                if (logger.isDebugEnabled()) {
                    logger.debug("Created " + sessionContext);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found " + sessionContext);
                }
            }
        }

        return sessionContext;
    }

    // Javadoc inherited from super class.
  public String getRealPath (String separator) {
    return requestContext.getServletContext().getRealPath(separator);
  }

  // Javadoc inherited from super class.
  public MarinerURL getContextPathURL () {

    if (contextPathURL == null) {
      // Get the servlet request.
      HttpServletRequest request = requestContext.getHttpRequest ();

      // Get the context path from the servlet request.
      String contextPath = request.getContextPath ();

      // J2EE compliant application servers are supposed to return the context
      // path without the / but some do not. It is much more useful if it
      // does have a / so we make sure that it does here.
      if(contextPath==null || contextPath.length()==0) {
        contextPath = "/";
      } else if (contextPath.charAt (contextPath.length () - 1) != '/') {
        contextPath = contextPath + '/';
      }
      contextPathURL = new MarinerURL (contextPath);
      contextPathURL.makeReadOnly ();
    }

    return contextPathURL;
  }

  // Javadoc inherited from super class.
  public String getExtraPathInfo () {

    if (extraPathInfo == null) {
      // Get the servlet request.
      HttpServletRequest request = requestContext.getHttpRequest ();

      extraPathInfo = request.getPathInfo ();
      if (extraPathInfo.charAt (0) == '/') {
        extraPathInfo = extraPathInfo.substring (1);
      }
    }

    return extraPathInfo;
  }

  // Javadoc inherited from super class.
  public String getTempDir() {
    return requestContext.getServletContext().
      getAttribute("javax.servlet.context.tempdir").toString();
  }

  // Javadoc inherited from super class.
  public String getResourceAsString(String resourceName)
  throws MarinerContextException {
    try {
      return requestContext.getServletContext().
	getResource(resourceName).toString();
    }
    catch (MalformedURLException mue) {
      throw new MarinerContextException(mue);
    }
  }

    /**
   * Initialise the session context dependent properties of this object.
   * <p>
   * If the session is not already available then it needs to be created.
   * </p><p>
   * This code needs to be moved into the Jsp/ServletMarinerRequestContext as
   * it is servlet / JSP specific.
   * </p>
   */
  public void initialiseSession ()
    throws RepositoryException {


//    // Get the Volantis bean.
//    volantisBean = com.volantis.mcs.runtime.Volantis.getInstance ();
//    if (volantisBean == null) {
//        throw new IllegalStateException
//            ("Volantis bean has not been initialised");
//    }
    /*
    if (enclosingRequestContext == null) {
      sessionContext = environmentContext.getSessionContext ();
    } else {
      sessionContext = enclosingRequestContext.getSessionContext ();
    }
    */
    sessionContext = getSessionContext ();
  }

  /**
   * Release any resources which were allocated by the initialiseSession
   * method.
   * <p>
   * This method has to free the resources in an order determined by the
   * dependencies between the different resources. A resource must only
   * be released once all resources which depend on it have also been
   * released.
   * </p>
   */
  protected void releaseSession () {
    sessionContext = null;
  }

    /**
     * Create a new MarinerSessionContext.
     * @return The newly created MarinerSessionContext.
     */
    public MarinerSessionContext createSessionContext ()
        throws RepositoryException {

      // Create a new ServletMarinerSessionContext.
      return new MarinerServletSessionContext();
    }

    //javadoc inherited
    public void initalisePipelineContextEnvironment(XMLPipelineContext pipelineContext) {

        // These properties are generally useful for JSP and Servlet
        // environment specific processes (e.g. the include server resource
        // process)
        pipelineContext.setProperty(ServletRequest.class,
                                   requestContext.getRequest(),
                                    false);
        pipelineContext.setProperty(ServletResponse.class,
                                    requestContext.getResponse(),
                                    false);
    }

    public EnvironmentInteraction createRootEnvironmentInteraction () {
                                                                                                                                                       
        final ServletEnvironmentFactory servletEnvironmentFactory =
                ServletEnvironmentFactory.getDefaultInstance();
                                                                                        
        ServletContext servletContext =
                requestContext.getServletContext();
                                                                                        
        final ServletEnvironment servletEnvironment =
            servletEnvironmentFactory.createEnvironment(servletContext);
                                                                                        
                                                                                        
        // create the root interaction
        final EnvironmentInteraction rootInteraction =
                servletEnvironmentFactory.createEnvironmentInteraction(
                        servletEnvironment,
                        // TODO where can I get the Servlet from
                        null,
                        // TODO can't use a deprecated method
                        requestContext.getServletConfig(),
                        requestContext.getRequest(),
                        requestContext.getResponse());
                                                                                        
        return rootInteraction;
    }


    //Javadoc inherited
    public String getHeader(String header) {
        HttpServletRequest httpRequest =
            requestContext.getHttpRequest();
        return httpRequest.getHeader(header);
    }

    //Javadoc inherited
    public Enumeration getHeaders(String header) {
        HttpServletRequest httpRequest =
            requestContext.getHttpRequest();
        return httpRequest.getHeaders(header);
    }

    //javadoc inherited
    public void setAttribute(String attributeName, Object attributeValue) {
        HttpServletRequest httpRequest =
            requestContext.getHttpRequest();

        httpRequest.setAttribute(attributeName, attributeValue);
    }

    //javadoc inherited
    public ResponseCachingDirectives getCachingDirectives() {
        if (cachingDirectives == null) {
            cachingDirectives = new ResponseCachingDirectives(
                SystemClock.getDefaultInstance());
        }
        return cachingDirectives;
    }

    // javadoc inherited
    public void applyCachingDirectives() {
        getCachingDirectives().close();
        addCachingHeaders();
    }

    /**
     * Adds cache-related headers to the response based on the values stored in
     * the caching directives.
     *
     * @see #getCachingDirectives()
     */
    private void addCachingHeaders() {
        final ResponseCachingDirectives cachingDirectives =
            getCachingDirectives();
        final HttpServletResponse response =
            (HttpServletResponse) requestContext.getResponse();
        if (cachingDirectives != null && cachingDirectives.isEnabled() &&
            cachingDirectives.getExpires() != null) {
            // if enabled, set Expires, Cache-Control/max-age and Vary response
            // headers
            final Time expires = cachingDirectives.getExpires();
            if (expires != Time.NEVER) {
                response.addDateHeader("Expires", expires.inMillis());
                // compute max-age value
                final Period timeToLive = cachingDirectives.getTimeToLive();
                long maxAgeInSeconds = 0;
                if (timeToLive != null) {
                    maxAgeInSeconds =
                        LongHelper.asInt(timeToLive.inMillis() / 1000);
                    if (maxAgeInSeconds < 0) {
                        maxAgeInSeconds = 0;
                    }
                }
                response.addHeader("Cache-Control", "max-age=" + maxAgeInSeconds);
            } else {
                // from the spec:
                // To mark a response as "never expires," an origin server sends
                // an Expires date approximately one year from the time the
                // response is sent. HTTP/1.1 servers SHOULD NOT send Expires
                // dates more than one year in the future.
                final SystemClock clock = cachingDirectives.getClock();
                final long oneYearInSeconds = 365 * 24 * 60 * 60;
                response.addDateHeader("Expires",
                    clock.getCurrentTime().inMillis() + oneYearInSeconds * 1000);
                response.addHeader("Cache-Control", "max-age=" + oneYearInSeconds);
            }
            response.addHeader("Vary", VARY_HEADER_VALUE);
        } else {
            // disable caching
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Pragma", "no-cache");
            final Calendar calendar =
                new GregorianCalendar(1990, Calendar.JANUARY, 1);
            response.addDateHeader("Expires", calendar.getTimeInMillis());
        }
    }
    
    /**
     * Get mime type of passed file name.
     * It recognize mime type through file extension
     */
    public String getMimeType(String name) {
    	return requestContext.getServletContext().getMimeType(name);
    }
}
