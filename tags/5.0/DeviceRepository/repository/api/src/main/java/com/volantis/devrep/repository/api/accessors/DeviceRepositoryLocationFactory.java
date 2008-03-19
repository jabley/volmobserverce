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

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Abstract factory to create {@link DeviceRepositoryLocation}s.
 */
public abstract class DeviceRepositoryLocationFactory {

    /**
     * Set up the meta default factory instance
     */
    private static MetaDefaultFactory metaDefaultFactory =
        new MetaDefaultFactory(
            "com.volantis.devrep.repository.impl.DeviceRepositoryLocationFactoryImpl",
            DeviceRepositoryLocationFactory.class.getClassLoader());

    /**
     * @return the default instance of the factory.
     */
    public static DeviceRepositoryLocationFactory getDefaultInstance() {
        return (DeviceRepositoryLocationFactory)
            metaDefaultFactory.getDefaultFactoryInstance();
    }


    /**
     * Creates an encoding collection from the specified list of encodings.
     */
    public abstract DeviceRepositoryLocation createDeviceRepositoryLocation(String location);

}
