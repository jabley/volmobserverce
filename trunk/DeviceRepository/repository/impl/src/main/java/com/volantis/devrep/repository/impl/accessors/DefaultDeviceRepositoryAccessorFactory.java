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
package com.volantis.devrep.repository.impl.accessors;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.impl.accessors.jdbc.JDBCDeviceRepositoryAccessor;
import com.volantis.devrep.repository.impl.accessors.xml.JiBXDeviceRepositoryAccessor;
import com.volantis.mcs.devices.DeviceRepositoryConfiguration;
import com.volantis.mcs.devices.xml.XMLDeviceRepositoryConfiguration;
import com.volantis.mcs.repository.LocalJDBCRepository;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.LocalXMLRepository;

/**
 */
public class DefaultDeviceRepositoryAccessorFactory
    extends DeviceRepositoryAccessorFactory {

    public DeviceRepositoryAccessor createDeviceRepositoryAccessor(
            final LocalRepository repository,
            final DeviceRepositoryLocation location,
            final DeviceRepositoryConfiguration configuration) {

        final DeviceRepositoryAccessor result;
        if (repository instanceof LocalXMLRepository) {
            if (configuration == null) {
                result = new JiBXDeviceRepositoryAccessor(
                    repository, location);
            } else {
                final XMLDeviceRepositoryConfiguration xmlConfiguration =
                    ((XMLDeviceRepositoryConfiguration) configuration);
                result = new JiBXDeviceRepositoryAccessor(repository, location,
                    xmlConfiguration.getSchemaValidation());
            }
        } else if (repository instanceof LocalJDBCRepository) {
            // todo: update caching in the JDBDeviceRepositoryAccessor
            // we could create a JDBCConnectionReleasingDeviceRepositoryAccessor
            // at runtime, remove the related code from the accessor below and
            // implement the to do in JDBCConnectionReleasingPolicyBuilderAccessor
            // Note that this requires that caching is also split out which looks
            // a bit tricky.

            result = new JDBCDeviceRepositoryAccessor(
                (LocalJDBCRepository) repository, location);
        } else {
            throw new IllegalStateException("Unknown repository type.");
        }
        return result;
    }
}
