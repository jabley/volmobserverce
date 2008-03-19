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
import com.volantis.synergetics.factory.MetaDefaultFactory;

import java.util.Map;

/**
 * Factory to create an internal device.
 */
public abstract class InternalDeviceFactory {

    /**
     * Set up the meta default factory instance
     */
    private static MetaDefaultFactory metaDefaultFactory =
        new MetaDefaultFactory(
            "com.volantis.mcs.devices.DefaultInternalDeviceFactory",
            InternalDeviceFactory.class.getClassLoader());

    /**
     * @return the default instance of the factory.
     */
    public static InternalDeviceFactory getDefaultInstance() {
        return (InternalDeviceFactory)
            metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Returns an <code>InternalDevice</code> for the given
     * {@link com.volantis.mcs.devices.DefaultDevice}.
     *
     * <p>The returned internal device is not necessarily a new object, as the
     * factory implementation may cache the created objects and return the same
     * internal device for the same default device.</p>
     */
    public abstract InternalDevice createInternalDevice(DefaultDevice device);
}
