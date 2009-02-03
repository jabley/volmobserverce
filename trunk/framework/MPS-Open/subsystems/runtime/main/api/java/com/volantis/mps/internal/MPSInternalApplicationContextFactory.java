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
 * $Header: /src/mps/com/volantis/mps/internal/MPSInternalApplicationContextFactory.java,v 1.9 2003/03/26 17:43:13 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002091806 - Created
 * 13-Nov-02    Mat             VBM:2002103010 - Changed to extend
 *                              MPSApplicationContextFactory
 * 15-Nov-02    sumit           VBM:2002102403 - Returns 
 *                              MPSInternalApplicationContext
 * 15-Jan-03    Mat             VBM:2002121109 - Enabled license check.
 * 21-Jan-03    Ian             VBM:2003012102 - Added check for connection
 *                              being null.
 * 29-Jan-03    Ian             VBM:2003011609 - Added canvasTagSupported and
 *                              attribute to ApplicationContext.
 * 14-Feb-03    Mat             VBM:2003021401 - Refactored out common code in
 *                              createApplicationcontext() to remove 
 *                              duplicate code.  Added resolveDevice()
 * 26-Mar-03    Sumit           VBM:2003032602 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.internal;

import com.volantis.mcs.application.DeviceReader;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.internal.InternalRequest;
import com.volantis.mcs.internal.MarinerInternalRequestContext;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mps.application.MPSApplicationContextFactory;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This is the ApplicationContextFactory for the internal environment within
 * MPS.
 */
public class MPSInternalApplicationContextFactory
        extends MPSApplicationContextFactory {

    /**
     * The logger to use.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    MPSInternalApplicationContextFactory.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    MPSInternalApplicationContextFactory.class);

    /**
     * The asset rewriter required within MPS.
     */
    private AssetURLRewriter assetURLRewriter =
            new MPSInternalAssetURLRewriter();

    /**
     * Creates a new instance of MPSApplicationContextFactory
     */
    public MPSInternalApplicationContextFactory() {
        super();
    }

    //javadoc inherited
    public ApplicationContext createApplicationContextImpl(
            MarinerRequestContext requestContext) {
        MPSInternalApplicationContext applicationContext =
                new MPSInternalApplicationContext(requestContext);

        applicationContext.setAssetURLRewriter(assetURLRewriter);

        return applicationContext;
    }

    protected InternalDevice resolveDevice(
            DeviceReader deviceReader, MarinerRequestContext requestContext)
            throws RepositoryException {

        InternalRequest internalRequest =
                ((MarinerInternalRequestContext) requestContext).getRequest();

        String deviceName = internalRequest.getDeviceName();
        if (logger.isDebugEnabled()) {
            logger.debug("Mariner-DeviceName=" + deviceName);
        }

        InternalDevice device = deviceReader.getDevice(deviceName);
        if (device == null) {
            throw new RepositoryException(exceptionLocalizer.format(
                    "unknown-device-exception",
                    deviceName));
        }

        return device;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	654/1	philws	VBM:2005040509 Port exception localization from 3.3

 29-Apr-05	647/1	philws	VBM:2005040509 Minor exception message changes and switch to MarlinSAXHelper

 02-Mar-05	355/1	emma	VBM:2005022804 mergevbm from MPS V3.3

 01-Mar-05	347/1	emma	VBM:2005022804 Added error message if device specified does not exist in repository

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 29-Nov-04	243/2	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 18-Dec-03	77/1	mat	VBM:2003120106 Change Device to InternalDevice

 13-Oct-03	40/1	pcameron	VBM:2003100707 Removed all traces of licensing from MPS

 ===========================================================================
*/
