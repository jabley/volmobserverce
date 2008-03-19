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
 * $Header: /src/mps/com/volantis/mps/application/MPSApplicationContextFactory.java,v 1.13 2003/03/26 17:43:13 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Oct-02    Ian             VBM:2002103010 - The superclass for the
 *                              Default application context factories.
 * 15-Nov-02    Mat             VBM:2002111306 - Added licenseString()
 * 18-Nov-02    Mat             VBM:2002111308 - Added checkVersion()
 * 25-Nov-02    Mike Jones      VBM:2002111307 - Added isMCSValidVersion and
 *                              make version ints public static declarations
 * 25-Nov-02    Mat             VBM:2002111308 - Added getFullVersion() to
 *                              calculate the version number in the same way
 *                              as Mandate.
 * 25-Nov-02    Mike Jones      VBM:2002111307 - Added checkVersionMessage()
 *                              to return a string for installer and runtime.
 * 28-Nov-02    Mike Jones      VBM:2002111307 - Added getMCSVolantisVersion
 *                              and modified methods so that version check can
 *                              be performed with any version of MCS
 * 29-Nov-02    Mike Jones      VBM:2002111307 - Modified version checking
 *                              since it's now handled by MPSVersion class.
 * 14-Feb-03    Mat             VBM:2003021401 - Some refactoring for the
 *                              createApplicationContextImpl methods.  Also added
 *                              resolveDevice() methods and
 *                              initializeApplicationContext()
 * 24-Feb-02    Ian             VBM:2003021904 - Moved code that sets
 *                              sessionDevice to MarinerPageContext.
 * 26-Mar-03    Sumit           VBM:2003032602 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.application;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.application.AbstractApplicationContextFactory;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.ImageURLModifier;
import com.volantis.mcs.integration.ImageURLModifierDetails;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.NamedProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.packagers.DefaultPackager;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This is the super class for the default application context factories.
 */
public abstract class MPSApplicationContextFactory
        extends AbstractApplicationContextFactory {

    /**
     * The log4j object to log to.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MPSApplicationContextFactory.class);

    /** The encoding manager for this application */
    private EncodingManager encodingManager;

    /**
     * Builds protocol instances for us.
     */
    protected NamedProtocolBuilder protocolBuilder;

    /** Creates a new instance of DefaultApplicationContextFactory */
    public MPSApplicationContextFactory() {
        this.encodingManager = new EncodingManager();

        protocolBuilder = new NamedProtocolBuilder();
        // Register the known protocols with the builder.
        ProtocolRegistry registry = new ProtocolRegistry();
        registry.register(protocolBuilder);
    }

    /**
     * This method instantiates an application specific context.
     *
     * @param  requestContext      The current request context.
     * @return                     The application specific context.
     * @throws RepositoryException if a repository exception occurs.
     */
    public ApplicationContext createApplicationContext(
            MarinerRequestContext requestContext)
                throws RepositoryException {

        ApplicationContext applicationContext = null;

        // Get the Volantis bean.
        Volantis volantisBean = Volantis.getInstance();
        if (volantisBean == null) {
            throw new IllegalStateException
                    ("Volantis bean has not been initialised");
        }

        // Resolve the device (once for this session).
        InternalDevice device = resolveDevice(
                volantisBean,
                requestContext);

        // Create the application context
        applicationContext = createApplicationContextImpl(requestContext);

        // Initialize the application context.
        initializeApplicationContext(requestContext, volantisBean,
                applicationContext, device);

        return applicationContext;
    }

    /**
     * The newly created application context. This method must be overridden to
     * create an alternative application context.
     *
     * @param  requestContext the mariner request context.
     * @return                the newly created application context.
     */
    protected abstract ApplicationContext createApplicationContextImpl(
            MarinerRequestContext requestContext);

    /**
     * Provide a template mechanism for subclasses to override the default
     * initialization of this application context. This should typically be
     * called after the application context has been created.
     *
     * @param volantisBean       the volantis bean.
     * @param applicationContext the newly created application context.
     * @param device             the device associated with this request.
     */
    protected void initializeApplicationContext(
            MarinerRequestContext requestContext,
            Volantis volantisBean,
            ApplicationContext applicationContext,
            InternalDevice device) {

      VolantisProtocol protocol;

      String protocolName;

        String preferredMessageProtocol =
            device.getPolicyValue("preferredmessageprotocol");
        if (null == preferredMessageProtocol ||
                "none".equals(preferredMessageProtocol)) {
            throw new UnsupportedOperationException("Device does not support messaging");
        }

        protocolName = device.getPolicyValue(
                    preferredMessageProtocol.toLowerCase()+"protocol");
        if(logger.isDebugEnabled()) {
            logger.debug("Protocol Name="+protocolName);
        }

        // Create a configured protocol.
        protocol = protocolBuilder.build(protocolName, device);

        applicationContext.setDevice(device);
        applicationContext.setPackager(new DefaultPackager());
        applicationContext.setProtocol(protocol);
        applicationContext.setDissectionSupported(false);
        applicationContext.setFragmentationSupported(false);
        applicationContext.setCanvasTagSupported(false);
        applicationContext.setEncodingManager(encodingManager);
        applicationContext.setImageURLModifier(new ImageURLModifier() {
            public void modifyImageURL(StringBuffer imageURLString,
                                         ImageURLModifierDetails imageURLModifierDetails) {

                if(imageURLModifierDetails.getMaxImageSize()!=null &&
                        !"-1".equals(imageURLModifierDetails.getMaxImageSize())) {

                    imageURLString.append("&maxmmsize=").
                            append(imageURLModifierDetails.getMaxImageSize());
                }


                imageURLString.append("&mcs.ie=").
                        append(imageURLModifierDetails.getEncoding());
            }

        });

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Sep-05	882/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 09-Feb-05	308/1	ianw	VBM:2005020205 IBM fixes

 17-Jan-05	297/3	allan	VBM:2005011403 Use an ImageURLModifier for MPS specific url params

 17-Jan-05	297/1	allan	VBM:2005011403 Use ImageURLModifier for MPS specific url params

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 29-Nov-04	243/2	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 18-Dec-03	77/1	mat	VBM:2003120106 Change Device to InternalDevice

 13-Oct-03	40/2	pcameron	VBM:2003100707 Removed all traces of licensing from MPS

 24-Jul-03	19/1	chrisw	VBM:2003070207 Make MPS build and run using librarian to get dependent jars

 ===========================================================================
*/
