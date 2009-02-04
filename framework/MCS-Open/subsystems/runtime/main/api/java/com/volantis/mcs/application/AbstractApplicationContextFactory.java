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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.application;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerSessionContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;

/**
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in PrivateAPI
 */
public abstract class AbstractApplicationContextFactory
        implements ApplicationContextFactory {

    /**
     * Return the resolved device for the given request context.
     *
     * @param  volantisBean        the volantis bean.
     * @param  requestContext      the mariner request context.
     * @return                     the resolved device (should always be
     *                             non-null)
     * @throws RepositoryException if a repository excepton occurs.
     */
    protected InternalDevice resolveDevice(Volantis volantisBean,
                                           MarinerRequestContext requestContext)
            throws RepositoryException {

        InternalDevice device = null;
        MarinerSessionContext sessionContext =
                ContextInternals.getEnvironmentContext(requestContext)
                .getSessionContext();

        // We only want to resolve the device once per session.
        device = sessionContext.getDevice();
        if (device == null) {
            DeviceReader reader = volantisBean.getDeviceReader();
            device = resolveDevice(reader, requestContext);
        }

        return device;
    }

    /**
     * Resolve the device for the given connection and request context. This
     * method is a template method and must be implemented by its subclasses.
     *
     * @param  deviceReader        the device reader.
     * @param  requestContext      the mariner request context.
     * @return                     the resolved device.
     * @throws RepositoryException if a Repository Exception occurs.
     */
    protected abstract InternalDevice resolveDevice(
            DeviceReader deviceReader,
            MarinerRequestContext requestContext)
            throws RepositoryException;
}
