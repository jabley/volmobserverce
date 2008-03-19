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

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RequestHeaders;

/**
 * Provides methods to retrieve devices for application contexts.
 *
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in PrivateAPI
 */
public interface DeviceReader {

    /**
     * Get the device with the specified name.
     * @param deviceName The device name.
     * @return The device, or null if it could not be found.
     */
    InternalDevice getDevice(String deviceName)
            throws RepositoryException;

    /**
     * Get the device with the specified headers.
     * @param headers The headers that identify the device.
     * @return The device, or null if it could not be found.
     */
    InternalDevice getDevice(RequestHeaders headers)
            throws RepositoryException;
}
