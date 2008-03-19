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
 * $Header: /src/voyager/com/volantis/mcs/servlet/MarinerServletApplication.java,v 1.5 2003/03/20 12:03:16 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Nov-02    Paul            VBM:2002091806 - Created to provide a
 *                              servlet specific application level interface
 *                              which hides the Volantis bean.
 * 22-Nov-02    Paul            VBM:2002091806 - Replaced references to
 *                              ServletConfig with ServletContext.
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 11-Mar-03    Geoff           VBM:2002112102 - Paul said getVolantisBean
 *                              should call initialiseInternal() rather than
 *                              initialise().
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.application.ApplicationRegistry;
import com.volantis.mcs.application.ApplicationRegistryContainer;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.http.servlet.HttpServletFactory;
import com.volantis.mcs.internal.DefaultInternalApplicationContextFactory;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.ExternalPathToInternalURLMapper;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class provides a servlet specific interface to the MarinerApplication.
 * <p>
 * Only one instance of a MarinerServletApplication must be created within
 * a single servlet context.
 * <p>
 * To use this class from within a jsp page use the following code:
 * <pre> &lt;jsp:useBean id="marinerApplication"
 *              class="com.volantis.mcs.servlet.MarinerServletApplication"
 *              scope="application"&gt;
 * &lt;/jsp:useBean&gt;
 * &lt;%marinerApplication.initialize (application);%&gt;</pre>
 * <p>
 * To use this class from within a servlet use the following code:
 * <pre>MarinerServletApplication marinerApplication = MarinerServletApplication.getInstance (context);</pre>
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate
 */
public class MarinerServletApplication
  extends MarinerApplication {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MarinerServletApplication.class);

  /**
   * The constant which is the name of the attribute in the ServletContext
   * which is used to hold the reference to the current instance of this
   * class.
   * <h2>
   * This MUST match the name specified in the jsp:useBean directive in the
   * JSP pages.
   * </h2>
   */
  private static final String MARINER_APPLICATION_NAME = "marinerApplication";

  /**
   * The constant which is the name of the attribute in the ServletContext
   * which is used to hold the reference to the current instance of the
   * Volantis bean.
   * <h2>
   * This MUST match the name specified in the old jsp:useBean directives
   * in the JSP pages.
   * </h2>
   */
  private static final String MARINER_VOLANTIS_NAME = "volantis";

  /**
   * A flag which indicates whether this class has completed successfully.
   * @see #checkInitializationState
   */
  private boolean initialized;

  /**
   * The name used to store the device in the session.  Normally MCS uses the
   * device stored in the MarinerSessionContext, but the public API method in
   * this class doesn't have access to it so has to store the resolved device
   * in the HttpSession.
   * It is not wasted however, as DefaultApplicationContextFactory#resolveDevice
   * can check the session for the device resolved here and reuse it.
   *
   * @see #getDevice
   */
  static final String SESSION_DEVICE_NAME = Device.class.getName();

    /**
     * Sometimes the initialization of this class may fail. If it does so then
     * this Throwable object contains a reference to the exception which was
     * thrown. Subsequent attempts to retrieve this instance will result in
     * a ServletException being thrown which has this as its root cause.
     * @see #checkInitializationState
     */
    private Throwable initializationFailure;

  /**
   * The ServletContext within which this application is running.
   */
  private ServletContext servletContext;

    /**
     * This is the current instance of the MarinerServletApplication
     */

    private static MarinerServletApplication instance;

    /**
     * The factory responsible for creating servlet specific instances of
     * objects.
     */
    private final HttpServletFactory servletFactory;

    /**
     * Get the MarinerServletApplication instance for the specified
     * ServletContext.
     * <p>
     * This method will create and initialise it if it is not already done so.
     * @param context The ServletContext within which the mariner application is
     * running.
     * @return The current instance of the MarinerServletApplication.
     * @throws ServletException If there was a problem retrieving the object.
     */
    public static MarinerServletApplication getInstance(ServletContext context)
            throws ServletException {
        MarinerServletApplication application;

        synchronized (context) {
            // Look in the ServletContext to see if an instance has already been
            // created for this ServletContext.
            application = (MarinerServletApplication) context.getAttribute
                    (MARINER_APPLICATION_NAME);
            if (application == null) {
                application = new MarinerServletApplication();
                context.setAttribute(MARINER_APPLICATION_NAME, application);
                application.initializeInternal(context);
            }
            instance = application;
        }

        return application;
    }

    /**
     * Get the MarinerServletApplication instance for the specified
     * ServletContext.
     * <p>
     * If requested this method will create and initialise it if it is not
     * already done so.
     * @param context The ServletContext within which the mariner application is
     * running.
     * @param create If true then this method should create the object if it is
     * not already present.
     * @return The current instance of the MarinerServletApplication, or null if
     * it was not present and was not created.
     * @throws ServletException If there was a problem retrieving the object.
     * @deprecated Use findInstance to determine the existance of or retrieve
     * an existing MarinerServletApplication for the ServletContext and
     * getInstance(context) to get and if necessary create a the
     * MarinerServletApplicationContext for the ServletContext.
     */
    public static MarinerServletApplication getInstance(ServletContext context,
                                                        boolean create)
            throws ServletException {

        MarinerServletApplication application;

        synchronized (context) {
            // Look in the ServletContext to see if an instance has already been
            // created for this ServletContext.
            application = findInstance(context);

            // If it could not be found then check whether we have been asked to
            // create it.
            if (application == null) {
                // Even though we have been asked not to create the
                // MarinerApplication it may be that the Volantis bean has
                // already been created. In this case we should create the
                // MarinerServletApplication to wrap the existing Volantis bean.
                if (!create) {
                    // Look for the Volantis bean and if it is available then
                    // create a MarinerServletApplication which wraps it.
                    final Volantis volantisBean = findVolantisBean(context);
                    if (volantisBean == null) {
                        // Return immediately to avoid dropping through to the
                        // following code.
                        return null;
                    }
                }

                // We have either been explicitly asked to create one, or need
                // to create one to maintain backwards compatability so do so
                // and store it away.
                application = new MarinerServletApplication();
                context.setAttribute(MARINER_APPLICATION_NAME, application);
            }

            // Ensure that the application is initialized.
            application.initializeInternal(context);
        }

        return application;
    }

    /**
     * This method will find the existing MarinerServletApplication for a
     * specified ServletContext.
     *
     * @param context The ServletContext within which the mariner application is
     * running.
     * @return the MarinerServletApplication in the specified ServletContext or
     * null if none was present.
     */
    private static MarinerServletApplication
            findInstance(ServletContext context) {
        return (MarinerServletApplication) context.getAttribute
                (MARINER_APPLICATION_NAME);
    }

    /**
     * Identify the Device from the HTTP Headers specified in the
     * HttpServletRequest.
     *
     * @param request The HttpServletRequest made by the Device to
     * be identified.
     * @return The identified Device.
     * @throws RepositoryException If there is a problem retrieving the Device.
     */
    public Device getDevice(HttpServletRequest request)
            throws RepositoryException {
        Volantis volantisBean = ApplicationInternals.getVolantisBean(this);

        if (volantisBean == null) {
            throw new IllegalStateException("MCS Application not initialised");
        }

        // Get the session
        HttpSession session = request.getSession(true);

        if (logger.isDebugEnabled()) {
            logger.debug("Session information: " + session.toString());
            logger.debug("Session max interval time: " +
                    session.getMaxInactiveInterval());
        }

        // See if the device has previously been stored in the session and
        // use it if it has.
        DefaultDevice device = (DefaultDevice)
                session.getAttribute(SESSION_DEVICE_NAME);
        // Throw away devices which were constructed via serialisation
        // from persisted sessions, since we do not properly support
        // serialisation for now.
        if (device != null && !device.isValid()) {
            device = null;
        }

        // No device in the session, so we need to ID it.
        if(device == null) {

            // Use the device repository to retrieve the device.
            DeviceRepository deviceRepository;
            try {
                deviceRepository = getRuntimeDeviceRepository();

                HttpHeaders headers = servletFactory.getHTTPHeaders(request);
                String defaultDeviceName = volantisBean.getDevicesConfiguration().getDefaultDeviceName();

                // null value of defaultDeviceName is OK, it means that
                // we let DeviceRepository subsystem use its own default                
                device = (DefaultDevice) deviceRepository
                        .getDevice(headers, defaultDeviceName);
            }
            catch(DeviceRepositoryException e) {
                throw new RepositoryException(e);
            }

            // Store the device in the session.
            try {
                session.setAttribute(SESSION_DEVICE_NAME, device);
            } catch (IllegalStateException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Unable to store the device in the session",
                            e);
                    e.printStackTrace();
                }
            }
        }

        return device;
    }

  /**
   * Create a new <code>MarinerServletApplication</code>.
   * <p>
   * This is only public because it has to be invoked by code generated from
   * jsp:useBean. Other users must use {@link #getInstance}.
   * </p>
   *
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  public MarinerServletApplication () {
      servletFactory = HttpServletFactory.getDefaultInstance();
  }

    /**
     * Initialize the object.
     * <p>
     * This must only be called by the jsp:useBean code, all user code must call
     * the {@link #getInstance} method.
     * </p>
     * @param context The ServletContext which is used to contexture the
     * application.
     */
    public void initialize(ServletContext context)
            throws ServletException {

        // Synchronize on the ServletContext as that is what the getInstance
        // method uses to synchronize on.
        synchronized (context) {
            initializeInternal(context);
        }
    }

    /**
     * Initialize the object.
     * <p>
     * <strong>NOTE: This method must be called while synchronized on the
     * ServletContext.</strong>
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    private void initializeInternal(ServletContext context)
            throws ServletException {

        // If the initialization of this object succeeded then there is nothing
        // more to do so return.
        if (checkInitializationState()) {
            return;
        }

        // Set the ServletContext.
        servletContext = context;

        try {

            // Register the default application.
            ApplicationRegistry ar = ApplicationRegistry.getSingleton();
            ApplicationRegistryContainer arc =
                    new ApplicationRegistryContainer(
                            new DefaultInternalApplicationContextFactory(),
                            new DefaultServletApplicationContextFactory());

            ar.registerApplication(
                    ApplicationRegistry.DEFAULT_APPLICATION_NAME, arc);
            if(logger.isDebugEnabled()){
                logger.debug("Registered default application");
            }

            // Get the Volantis bean, creating it if necessary.
            Volantis volantisBean = getVolantisBean(servletContext);

            // Store a reference to it in this MarinerApplication instance.
            ApplicationInternals.setVolantisBean(this, volantisBean);

            // The last thing to do is remember that this has been initialized
            // successfully.
            initialized = true;
        } catch (Throwable t) {
            initializationFailure = t;
            logger.error("unexpected-exception", t);
            throw new ServletException
                    ("Mariner application could not be initialised",
                            initializationFailure);
        }
    }

  /**
   * Check to see what the initialization state of this object is.
   * @return True if it has been successfully initialized, false if it has
   * not yet been initialized.
   * @throws ServletException If a previous attempt to initialize it failed.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  private boolean checkInitializationState () throws ServletException {

    // If the initialization of this object succeeded then there is nothing
    // more to do so return.
    if (initialized) {
      return true;
    }

    // If a previous attempt to initialize failed then throw a
    // ServletException which specifies the initialisation Throwable
    // as the root cause.
    if (initializationFailure != null) {
      throw new ServletException
        ("Mariner application was not correctly initialized",
         initializationFailure);
    }

    // This object has never been initialised.
    return false;
  }

    /**
     * Get the Volantis bean instance for the specified ServletContext.
     * <p>
     * If requested this method will create and initialise it if it is not
     * already done so.
     * <p>
     * <strong>NOTE: This method must be called while synchronized on the
     * ServletContext.</strong>
     *
     * @param context The ServletContext within which the Volantis bean is
     * running.
     * @return The current instance of the Volantis bean, or null if
     * it was not present and was not created.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    private Volantis getVolantisBean(ServletContext context)
            throws ConfigurationException {

        // Look in the ServletContext to see if an instance has already been
        // created for this ServletContext.
        Volantis volantis = findVolantisBean(context);

        // If it could not be found then create one and store it away.
        if (volantis == null) {
            volantis = new Volantis();
            context.setAttribute(MARINER_VOLANTIS_NAME, volantis);
        }

        // Create a mapper between external paths and internal URLs.
        ExternalPathToInternalURLMapper pathURLMapper;
        pathURLMapper = new ServletExternalPathToInternalURLMapper(
                servletContext);

        // Always call the initialize method to match the behaviour of the code
        // generated by the jsp:usebean directive.
        ConfigContext cc = new ServletConfigContext(context);
        volantis.initializeInternal(pathURLMapper, cc, this);

        return volantis;
    }

    /**
     * Look for an existing instance of the Volantis bean in the context
     * and return it if found.
     * @param context the ServletContext
     * @return the Volantis bean within the context if it exists; otherwise
     * null.
     */
    static Volantis findVolantisBean(ServletContext context) {
        return (Volantis) context.getAttribute(MARINER_VOLANTIS_NAME);
    }

    /*
     * Retrieve the servlet context in which we are running
     */
    public ServletContext getServletContext() {
        return(this.servletContext);
    }

    /**
     * This method turns MarinerApplicationContext into a global variable. It is a complete hack and is only being
     * used to enable the Volantis Bean to be moved to runtime.
     * @deprecated Do not use this method
     * @return  The current instance of thw MarinerServletApplication.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public static MarinerServletApplication getInstance() {
       return instance;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 08-Feb-05	6919/2	pduffin	VBM:2004122401 Fixed super merge issues

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 24-Jan-05	6716/8	pcameron	VBM:2005011801 Fixed incorrect way of obtaining Volantis bean

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 03-Sep-04	5408/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 03-Sep-04	5387/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4940/4	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 28-Jul-04	4940/2	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 23-Jul-04	4937/3	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 24-Jun-04	4737/4	allan	VBM:2004062202 MarinerServletApplication constructor should be public

 24-Jun-04	4737/2	allan	VBM:2004062202 Restrict volantis initialization.

 14-Apr-04	3849/1	byron	VBM:2004041401 IllegalStateException due to stress test

 14-Apr-04	3839/1	byron	VBM:2004041401 IllegalStateException due to stress test

 25-Mar-04	3537/1	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice

 22-Mar-04	3484/1	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice (do it properly)

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 ===========================================================================
*/
