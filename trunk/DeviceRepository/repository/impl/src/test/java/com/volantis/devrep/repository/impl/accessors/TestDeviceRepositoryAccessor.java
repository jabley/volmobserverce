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
 * (c) Volantis Systems Ltd 2004,2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.impl.accessors;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.impl.devices.policy.DefaultPolicyDescriptor;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cache.GenericCache;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An implementation of {@link com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor} for testing.
 * <p>
 * Currently all that is implemented are some of the policy descriptor methods.
 */
public class TestDeviceRepositoryAccessor implements DeviceRepositoryAccessor {

    public void setDeviceCache(GenericCache deviceCache) {
    }

    public void refreshDeviceCache() {
    }

    public void addDevice(
            RepositoryConnection connection,
            Device device)
            throws RepositoryException {
    }

    public void removeDevice(
            RepositoryConnection connection,
            String deviceName)
            throws RepositoryException {
    }

    public void removeDevice(
            RepositoryConnection connection,
            String deviceName, boolean removeChildren)
            throws RepositoryException {
    }

    public DefaultDevice retrieveDevice(
            RepositoryConnection connection,
            String deviceName)
            throws RepositoryException {
        return null;
    }

    public void renameDevice(RepositoryConnection connection,
            String deviceName, String newName)
            throws RepositoryException {
    }

    public void removePolicy(RepositoryConnection connection,
            String policyName)
            throws RepositoryException {
    }

    public RepositoryEnumeration enumerateDeviceNames(RepositoryConnection connection)
            throws RepositoryException {
        return null;
    }

    public List enumerateDeviceNames(RepositoryConnection connection,
            String deviceNamePattern)
            throws RepositoryException {
        return null;
    }

    public RepositoryEnumeration enumerateDeviceFallbacks(
            RepositoryConnection connection)
            throws RepositoryException {
        return null;
    }

    public RepositoryEnumeration enumerateDevicesChildren(
            RepositoryConnection connection,
            String deviceName)
            throws RepositoryException {
        return null;
    }

    public RepositoryEnumeration
            enumerateDeviceTACs(RepositoryConnection connection)
            throws RepositoryException {
        return null;
    }

    public String
            retrieveDeviceName(
            RepositoryConnection connection,
            long TAC)
            throws RepositoryException {
        return null;
    }

    public void initializeDevicePatternCache(RepositoryConnection connection)
            throws RepositoryException {
    }

    public String retrieveMatchingDeviceName(String userAgent)
            throws RepositoryException {
        return null;
    }

    public DefaultDevice getDeviceFallbackChain(
            RepositoryConnection connection,
            String deviceName)
            throws RepositoryException {
        return null;
    }

    public void updatePolicyName(RepositoryConnection connection,
            String oldPolicyName, String newPolicyName)
            throws RepositoryException {
    }

    public RepositoryEnumeration enumeratePolicyNames(
            RepositoryConnection connection) throws RepositoryException {
        return null;
    }

    public RepositoryEnumeration enumeratePolicyNames(
            RepositoryConnection connection, String categoryName)
            throws RepositoryException {
        return null;
    }

    public RepositoryEnumeration enumerateCategoryNames(
            RepositoryConnection connection)
            throws RepositoryException {
        return null;
    }

    private Map policyDescriptors = new HashMap();

    /**
     * Retrieve a policy descriptor by name from memory.
     */
    public PolicyDescriptor retrievePolicyDescriptor(
            RepositoryConnection connection,
            String policyName,
            Locale locale)
            throws RepositoryException {

        return (PolicyDescriptor) policyDescriptors.get(policyName);
    }

    /**
     * Add a policy descriptor by name into memory.
     */
    public void addPolicyDescriptor(
            RepositoryConnection connection,
            String policyName,
            PolicyDescriptor descriptor)
            throws RepositoryException {

        policyDescriptors.put(policyName, descriptor);
    }

    /**
     * Convenience method to add a policy by just providing a policy type.
     * <p>
     * The policy descriptor will be created automagically containing just the
     * type provided.
     *
     * @param connection
     * @param policyName
     * @param type
     * @throws RepositoryException
     */
    public void addPolicyDescriptor(RepositoryConnection connection,
            String policyName, PolicyType type)
            throws RepositoryException {

        DefaultPolicyDescriptor descriptor = new DefaultPolicyDescriptor();
        descriptor.setPolicyType(type);
        addPolicyDescriptor(connection, policyName, descriptor);
    }

    public void removePolicyDescriptor(
            RepositoryConnection connection,
            String policyName) throws RepositoryException {
    }

    public void removeAllPolicyDescriptors(RepositoryConnection connection)
            throws RepositoryException {
    }

    public void removeCategoryDescriptor(
            RepositoryConnection connection,
            String categoryName) throws RepositoryException {
    }

    public void removeAllCategoryDescriptors(RepositoryConnection connection)
            throws RepositoryException {
    }

    public CategoryDescriptor retrieveCategoryDescriptor(
            RepositoryConnection connection, String categoryName, Locale locale)
            throws RepositoryException {

        return null;
    }

    public List retrievePolicyDescriptors(RepositoryConnection connection,
                                          String policyName)
            throws RepositoryException {
        return null;
    }

    public List retrieveCategoryDescriptors(
            final RepositoryConnection connection, final String categoryName)
            throws RepositoryException {
        return null;
    }

    public void addCategoryDescriptor(final RepositoryConnection connection,
                                      final String categoryName,
                                      final CategoryDescriptor descriptor)
            throws RepositoryException {

    }
}
