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
package com.volantis.mcs.devices;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
/**
 * Default implementation of the
 * {@link com.volantis.mcs.devices.InternalDeviceFactory}.
 */
public class DefaultInternalDeviceFactory extends InternalDeviceFactory {
    /**
     * Unique property name to store the internal device in the default device
     * as a property.
     */
    private static final String PROPERTY_INTERNAL_DEVICE =
        "@@MCS@@InternalDevice@@";

    // javadoc inherited
    public InternalDevice createInternalDevice(final DefaultDevice device) {
        InternalDevice internalDevice;
        synchronized(device) {
            internalDevice =
                (InternalDevice) device.getProperty(PROPERTY_INTERNAL_DEVICE);
            if (internalDevice == null) {
                internalDevice = new InternalDeviceImpl(device);
                device.setProperty(PROPERTY_INTERNAL_DEVICE, internalDevice);
            }
        }
        return internalDevice;
    }
}
