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
package com.volantis.devrep.repository.api.accessors;

import com.volantis.mcs.devices.DeviceRepositoryConfiguration;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 */
public abstract class DeviceRepositoryAccessorFactory {

    /**
     * Set up the meta default factory instance
     */
    private static MetaDefaultFactory metaDefaultFactory =
        new MetaDefaultFactory(
            "com.volantis.devrep.repository.impl.accessors.DefaultDeviceRepositoryAccessorFactory",
            DeviceRepositoryAccessorFactory.class.getClassLoader());

    /**
     * @return the default instance of the factory.
     */
    public static DeviceRepositoryAccessorFactory getDefaultInstance() {
        return (DeviceRepositoryAccessorFactory)
            metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Return the <code>DeviceRepositoryAccessor</code>.
     *
     * @return The <code>DeviceRepositoryAccessor</code>.
     * @param location
     */
    public abstract DeviceRepositoryAccessor createDeviceRepositoryAccessor(
        LocalRepository repository,
        DeviceRepositoryLocation location,
        DeviceRepositoryConfiguration configuration);
}
