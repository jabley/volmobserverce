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
 * $Header: /src/voyager/com/volantis/mcs/servlet/DefaultServletApplicationContextFactory.java,v 1.5 2003/02/18 16:20:44 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Add setting packager on the
 *                              application context in
 *                              createApplicationContext.
 * 13-Feb-03    Byron           VBM:2003020305 - Removed
 *                              createApplicationContext and added
 *                              resolveDevice. Tidied imports.
 * 18-Feb-03    Phil W-S        VBM:2003021412 - Handle a null mimeType array
 *                              in findPackaging.
 * 30-May-03    Mat             VBM:2003042911 - Added WMLCSupported()
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.mcs.application.DefaultApplicationContextFactory;
import com.volantis.mcs.application.DeviceReader;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.http.HTTPHeadersHelper;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.http.servlet.HttpServletFactory;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.ServletRequestHeaders;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This is the default ApplicationContextFactory for the servlet environment
 * most of the initialisation for this environment was taken from
 * MarinerPageContext and that code replaced with calls to the resultant
 * ApplicationContext.
 */
public class DefaultServletApplicationContextFactory
        extends DefaultApplicationContextFactory {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DefaultServletApplicationContextFactory.class);

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();


    /**
     * Creates a new instance of DefaultApplicationContextFactory
     */
    public DefaultServletApplicationContextFactory() {
        super();
    }


    // javadoc inherited
    protected InternalDevice resolveDevice(DeviceReader deviceReader,
                                   MarinerRequestContext requestContext)
            throws RepositoryException {

        HttpServletRequest httpRequest =
                ((MarinerServletRequestContext) requestContext).getHttpRequest();

        InternalDevice device = resolveSessionDevice(httpRequest);

        if (device != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "Retrieved device " + device + " from the session");
            }
        } else {
            HttpHeaders httpHeaders = HttpServletFactory.getDefaultInstance().
                    getHTTPHeaders(httpRequest);

            device = deviceReader.getDevice(
                    new ServletRequestHeaders(httpHeaders));
        }

        return device;
    }

    /**
     * Try to resolve the device from the current session.
     * @param httpRequest the HttpServletRequest
     * @return the InternalDevice previously stored in the session or null
     * if there is none
     */
    protected InternalDevice
            resolveSessionDevice(HttpServletRequest httpRequest) {
        // Get the session if one exists.
        HttpSession session = httpRequest.getSession(false);
        InternalDevice device = null;

        // Get to see if MarinerServletApplication#getDevice has put a device
        // in the session.
        if (session != null) {
            DefaultDevice defaultDevice =
                (DefaultDevice) session.getAttribute(
                    MarinerServletApplication.SESSION_DEVICE_NAME);
            if (defaultDevice != null && defaultDevice.isValid()) {
                device = INTERNAL_DEVICE_FACTORY.createInternalDevice(defaultDevice);
            }
            // else, ignore devices which were constructed via serialisation
            // from persisted sessions, since we do not properly support
            // serialisation for now.
        }
        return device;
    }

    // javadoc inherited
    protected String findPackaging(VolantisProtocol protocol,
                                   MarinerRequestContext requestContext) {
        String result = null;
        String packagingType = protocol.getPackagingType();

        if (packagingType != null) {
            if (hasAcceptMimeType(requestContext, packagingType)) {
                result = packagingType;
            }
        }

        return result;
    }

    /**
     * Supporting method that returns <code>true</code> if the given request
     * actually has the specified mime type as an accepted mime type.
     *
     * @param requestContext
     *                 represents the request to be queried
     * @param mimeType the mime type to be checked
     * @return true if the given mime type is accepted
     */
    private boolean hasAcceptMimeType(MarinerRequestContext requestContext,
                                      String mimeType) {

        HttpServletRequest httpRequest =
                ((MarinerServletRequestContext) requestContext).
                getHttpRequest();

        HttpHeaders httpHeaders = HttpServletFactory.getDefaultInstance().
                getHTTPHeaders(httpRequest);

        return HTTPHeadersHelper.hasAcceptMimeType(httpHeaders, mimeType);
    }

    // javadoc inherited
    protected boolean checkWMLCSupport(MarinerRequestContext requestContext) {
        return hasAcceptMimeType(requestContext, "application/vnd.wap.wmlc");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	8685/1	allan	VBM:2005060307 User Alt-User-Agent instead of User-Agent

 06-Jun-05	8679/1	allan	VBM:2005060307 User Alt-User-Agent instead of User-Agent

 24-May-05	8415/1	emma	VBM:2005042012 Bug fix merged from 330 - now retrieves all available accept headers. Also added HttpHeadersHelper

 23-May-05	8413/1	emma	VBM:2005042012 Bug fix merged from 323 - now retrieves all available accept headers. Also added HttpHeadersHelper

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 13-Sep-04	5511/1	philws	VBM:2004091003 Port MIME packaging control from 3.2.2

 13-Sep-04	5495/1	philws	VBM:2004091003 Policy-based MIME packaging mode

 28-Jul-04	4940/1	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 25-Mar-04	3537/1	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice

 22-Mar-04	3484/1	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice (do it properly)

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 05-Jun-03	285/4	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
