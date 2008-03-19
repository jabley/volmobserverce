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

package com.volantis.mcs.cli;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import com.volantis.mcs.repository.LocalRepository;

import java.util.Locale;
import java.util.List;

public class DeviceTransferAccessor
 extends TransferAccessor {

    private static final DeviceRepositoryAccessorFactory REPOSITORY_ACCESSOR_FACTORY =
        DeviceRepositoryAccessorFactory.getDefaultInstance();

    private final DeviceRepositoryAccessor accessor;

    public DeviceTransferAccessor(
            LocalRepositoryConnection connection,
            DeviceRepositoryLocation location) {
        super(connection);

        LocalRepository repository =
                connection.getLocalRepository();
        accessor = REPOSITORY_ACCESSOR_FACTORY.createDeviceRepositoryAccessor(
            repository, location, null);
    }

    public RepositoryEnumeration enumerateDevicePolicyNames()
            throws RepositoryException {
        return accessor.enumeratePolicyNames(connection);
    }

    public RepositoryEnumeration enumerateCategoryNames()
            throws RepositoryException {
        return accessor.enumerateCategoryNames(connection);
    }

    public void removePolicyDescriptor(String policyName)
            throws RepositoryException {
        accessor.removePolicyDescriptor(connection, policyName);
    }

    public void removeAllPolicyDescriptors()
            throws RepositoryException {
        accessor.removeAllPolicyDescriptors(connection);
    }

    public void removeCategoryDescriptor(String categoryName)
            throws RepositoryException {
        accessor.removeCategoryDescriptor(connection, categoryName);
    }

    public void removeAllCategoryDescriptors()
            throws RepositoryException {
        accessor.removeAllCategoryDescriptors(connection);
    }

    public PolicyDescriptor retrievePolicyDescriptor(
            String policyName, Locale locale) throws RepositoryException {
        return accessor.retrievePolicyDescriptor(connection,
                policyName, locale);
    }

    public void addPolicyDescriptor(
            String policyName, PolicyDescriptor policyDescriptor)
            throws RepositoryException {
        accessor.addPolicyDescriptor(connection, policyName,
                policyDescriptor);
    }

    public List retrievePolicyDescriptors(String policyName)
            throws RepositoryException {
        return accessor.retrievePolicyDescriptors(connection, policyName);
    }

    public void addCategoryDescriptor(
            String categoryName, CategoryDescriptor categoryDescriptor)
            throws RepositoryException {
        accessor.addCategoryDescriptor(connection, categoryName,
                categoryDescriptor);
    }

    public List retrieveCategoryDescriptors(
            String categoryName) throws RepositoryException {
        return accessor.retrieveCategoryDescriptors(connection, categoryName);
    }

    public RepositoryEnumeration enumerateDevicesChildren(String deviceName)
            throws RepositoryException {
        return accessor.enumerateDevicesChildren(connection,
                deviceName);
    }

    public DefaultDevice retrieveDevice(String deviceName)
            throws RepositoryException {
        return accessor.retrieveDevice(connection, deviceName);
    }

    public void updateDevice(Device device) throws RepositoryException {
        accessor.removeDevice(connection, device.getName());
        accessor.addDevice(connection, device);
    }

    public void addDevice(Device device) throws RepositoryException {
        accessor.addDevice(connection, device);
    }
}
