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
 * $Header: /src/voyager/com/volantis/mcs/application/DefaultApplicationContextFactory.java,v 1.7 2003/04/28 15:25:03 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 12-Feb-03    Geoff           VBM:2003021110 - Comment out staticness of
 *                              check variables temporarily till we fix
 *                              2003021205 so the test cases don't fail.
 * 03-Feb-03    Byron           VBM:2003020305 - Added createApplicationContext
 *                              and overloaded method with default generic
 *                              implementation. Added MULTIPART_XHTML,
 *                              MULTIPART_WAP constants, resolveDevice x 2,
 *                              initializeApplicationContext. Tidied imports.
 *                              Added findPackaging method.
 * 20-Feb-03    Mat             VBM:2003022002 - Removed unnecessary code from
 *                              initializeLicense()
 * 24-Feb-02    Ian             VBM:2003021904 - Moved code that sets
 *                              sessionDevice to MarinerPageContext.
 * 25-Apr-03    Mat             VBM:2003033108 - Get the correct encoding
 *                              from the EncodingManager and set in the
 *                              application context.
 * 31-Mar-03    Mat             VBM:2003033107 - Added EncodingManager
 *                              to the applicationContext
 * 13-May-03    Mat             VBM:2003033108 - Do not get the encoding as
 *                              it can be changed at runtime.  Instead set the
 *                              EncodingManager in the application context.
 * 30-May-03    Mat             VBM:2003042911 - Added WMLCSupported() and
 *                              called set method in application context.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.application;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.DefaultApplicationContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.MimePackagingMode;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.NamedProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.packagers.DefaultPackager;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.runtime.packagers.mime.MultipartApplicationContext;
import com.volantis.mcs.runtime.packagers.mime.MultipartPackageHandler;

/**
 * This is the super class for the default application context factories.
 */
public abstract class DefaultApplicationContextFactory
        extends AbstractApplicationContextFactory {

    /** The encodingManager for this application. */
    private EncodingManager encodingManager;

    /**
     * A shared instance of the multipart package handler that can be used
     * by multiple requests at the same time. This is lazily instantiated in
     * the {@link #initializeApplicationContext} method.
     */
    private static MultipartPackageHandler handler = null;

    /**
     * Builds protocol instances for us.
     */
    protected NamedProtocolBuilder protocolBuilder;

    public DefaultApplicationContextFactory() {
        // This may be slow if we have to preload BitSetEncoding charsets.
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
        applicationContext = createApplicationContext(
                volantisBean,
                requestContext);

        // Initialize the application context.
        initializeApplicationContext(requestContext, volantisBean,
                applicationContext, device);

        return applicationContext;
    }

    /**
     * The newly created application context. This method may be overridden to
     * create an alternative application context.
     *
     * @param  requestContext the mariner request context.
     * @param  volantisBean   the volantis bean.
     * @return                the newly created application context.
     */
    protected ApplicationContext createApplicationContext(
            Volantis volantisBean,
            MarinerRequestContext requestContext) {
        ApplicationContext applicationContext;

        // Create the multipart package handler if page packaging is enabled
        if (volantisBean.isPagePackagingMimeEnabled() ) {
            applicationContext = new MultipartApplicationContext(requestContext);
        } else {
            applicationContext = new DefaultApplicationContext(requestContext);
        }

        return applicationContext;
    }


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

        // The protocol policy value may be an ordered list. Here we select the
        // first protocol from the list that we know of. This allows us to
        // ignore any protocols which may added to later device repositories
        // but are not known in this version of the product.
        String protocolName = device.selectSingleKnownPolicyValue("protocol",
                protocolBuilder.getProtocolNames());
        if (protocolName == null) {
            throw new IllegalStateException("Device protocol policy missing");
        }

        // Create a configured protocol.
        VolantisProtocol protocol = protocolBuilder.build(protocolName, device);

        boolean packagerAssigned = false;
        String packagingType = null;

        // Determine whether we should attempt to set up a multipart packager
        if (volantisBean.isPagePackagingMimeEnabled()) {
            // NB: The mode can only be null if the device policy hasn't been
            // added to the repository or if an invalid value has been
            // supplied. In this case we want "default" behaviour (which we get
            // with the following code)
            MimePackagingMode mode = MimePackagingMode.literal(
                    device.getPolicyValue(
                            DevicePolicyConstants.MIME_PACKAGING_MODE));

            // If packaging mode is never on the device then we never set up
            // a multipart packager
            if (mode != MimePackagingMode.NEVER) {
                // If the protocol supports packaging AND the mode is always OR
                // the request contains the required accept header then use
                // a multipart packager
                packagingType = protocol.getPackagingType();

                if ((packagingType != null) &&
                        ((mode == MimePackagingMode.ALWAYS) ||
                        ((packagingType = findPackaging(protocol,
                                                        requestContext)) !=
                         null))) {
                    PackageResources packageResources;

                    synchronized (this) {
                        if (handler == null) {
                            handler = new MultipartPackageHandler(
                                    volantisBean.getAssetURLRewriter());
                        }
                    }

                    applicationContext.setPackager(handler);
                    applicationContext.setAssetURLRewriter(handler);
                    applicationContext.setPackagedURLEncoder(handler);
                    packagerAssigned = true;

                    // Must be fetched after setting the packager
                    packageResources =
                            applicationContext.getPackageResources();

                    // Since we're doing multipart packaging make sure the
                    // correct content type is assigned to the over-all package
                    // (This condition is expected to succeed)
                    if (packageResources != null) {
                        packageResources.setContentType(packagingType);
                    }
                }
            }
        }

        if (!packagerAssigned) {
            applicationContext.setAssetURLRewriter(
                    volantisBean.getAssetURLRewriter());
            // @todo later synchronize and store the default packager in a member
            applicationContext.setPackager(new DefaultPackager());
        }

        // Complete initialization of the application context
        applicationContext.setDevice(device);
        applicationContext.setProtocol(protocol);
        applicationContext.setDissectionSupported(true);
        applicationContext.setFragmentationSupported(true);
        applicationContext.setCanvasTagSupported(true);
        applicationContext.setWMLCSupported(checkWMLCSupport(requestContext));
        applicationContext.setEncodingManager(encodingManager);
    }

    /**
     * Find the packaging for this protocol and ensure that the current request
     * has indicated that that packaging type is accepted.
     *
     * @param protocol the protocol
     * @param requestContext
     *                 the request context
     * @return the packaging type associated with this protocol or null if the
     *         request doesn't accept the protocol's packaging type
     */
    protected String findPackaging(VolantisProtocol protocol,
                                   MarinerRequestContext requestContext) {
        return null;
    }

    /**
     * Look for application/wap.vnd.wmlc in the accept headers.  This indicates
     * that the device can accept WMLC.
     *
     * @param requestContext
     *         The request context.
     * @return True if the header was found.
     */
    protected boolean checkWMLCSupport(MarinerRequestContext requestContext) {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/4	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 21-Sep-04	5567/1	allan	VBM:2004092010 Handle multi-valued device policy selection.

 15-Sep-04	5521/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 14-Sep-04	5519/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 13-Sep-04	5511/1	philws	VBM:2004091003 Port MIME packaging control from 3.2.2

 13-Sep-04	5495/3	philws	VBM:2004091003 Policy-based MIME packaging mode

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 29-Oct-03	1704/1	geoff	VBM:2003102906 HTMLVersion4_0_IE protocol is throwing an Inetrnal error under Tomcat

 13-Oct-03	1517/2	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 05-Jun-03	285/6	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
