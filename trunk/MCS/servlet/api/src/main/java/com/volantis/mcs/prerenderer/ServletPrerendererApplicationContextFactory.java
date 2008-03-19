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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.prerenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.volantis.mcs.application.DeviceReader;
import com.volantis.mcs.application.PrerendererApplicationContextFactory;
import com.volantis.mcs.context.PrerendererPackageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The PrerendererApplicationContextFactory for use in the servlet environment.
 */
public class ServletPrerendererApplicationContextFactory extends
        PrerendererApplicationContextFactory {

    /**
     * The logger to use.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    ServletPrerendererApplicationContextFactory.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    ServletPrerendererApplicationContextFactory.class);
    
    /**
     * The name of the attribute, under which an instance of current
     * PrerendererPackageContext is bound to the HttpSession.
     */
    private static final String PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME = 
        PrerendererPackageContext.class.getName();
    
    /**
     * Resolve the device for the given connection and request context. This
     * method is a template method and must be implemented by its subclasses.
     *
     * @param deviceReader     the device reader.
     * @param requestContext the mariner request context.
     * @return the resolved device.
     * @throws RepositoryException if a Repository Exception occurs.
     */
    protected InternalDevice resolveDevice(
            DeviceReader deviceReader,
            MarinerRequestContext requestContext)
            throws RepositoryException {

        HttpServletRequest httpRequest =
                ((MarinerServletRequestContext) requestContext).
                getHttpRequest();

        String deviceName = httpRequest.getHeader("Mariner-DeviceName");

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
    
    /**
     * @inheritDoc
     */
    protected PrerendererPackageContext getPrerendererPackageContext(MarinerRequestContext requestContext) {
        HttpServletRequest httpRequest =
            ((MarinerServletRequestContext) requestContext).
            getHttpRequest();
        
        HttpSession session = httpRequest.getSession();

        PrerendererPackageContext prerendererPackageContext = (PrerendererPackageContext)
            session.getAttribute(PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME);

        if (prerendererPackageContext == null) {
            throw new IllegalStateException("No prerendering context. Session expired possibly.");
        }

        return prerendererPackageContext;
    }
}
