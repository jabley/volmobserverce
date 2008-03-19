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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.impl.devices;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.devices.logging.IMEIEntry;
import com.volantis.devrep.repository.api.devices.logging.TACFACEntry;
import com.volantis.devrep.repository.api.devices.logging.UAProfileEntry;
import com.volantis.devrep.repository.api.devices.logging.UnknownDevicesLogger;
import com.volantis.devrep.repository.api.devices.PolicyDescriptorAccessor;
import com.volantis.devrep.repository.api.devices.DevicesHelper;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DeviceIdentificationResult;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A default implementation of {@link DeviceRepository}.
 * <p>
 * This delegates most of the heavy lifting to {@link DeviceRepositoryAccessor}.
 */
public class DefaultDeviceRepository implements DeviceRepository,
                                                PolicyDescriptorAccessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultDeviceRepository.class);

    /**
     * The repository.
     */
    private final LocalRepository repository;

    /**
     * The accessor to the device repository.
     */
    private final DeviceRepositoryAccessor accessor;

    /**
     * The repository type (either XML or JDBC).
     */
//    private final RepositoryConnectionType repositoryType;

    /**
     * Specifies whether experimental policies can be accessed through this
     * device repository.
     */
    private final boolean allowExperimentalPolicies;

    /**
     * A cache of the accessible policy names.
     * <p>
     * This will initially be null and will be lazy loaded when required.
     */
    private Set cachedAccessiblePolicyNames;

    private final Map cachedCategoryDescriptorRequests;

    /**
     * Logger for unknown and abstract devices. May be null.
     */
    private UnknownDevicesLogger unknownDevicesLogger;

    /**
     * Construct an instance of the DeviceRepository with the specified
     * parameters.
     *
     * @param repository the repository.
     * @param accessor   the device repository accessor.
     * @param allowExperimentalPolicies True if experimental policies can be
     */
    public DefaultDeviceRepository(
            LocalRepository repository,
            DeviceRepositoryAccessor accessor,
            boolean allowExperimentalPolicies) {
        this(repository, accessor, allowExperimentalPolicies, null);
    }

    /**
     * Construct an instance of the DeviceRepository with the specified
     * parameters.
     *
     * @param repository the repository.
     * @param accessor   the device repository accessor.
     * @param allowExperimentalPolicies True if experimental policies can be
     * @param logger     the logger for unknown and abstract devices,may be null
     */
    public DefaultDeviceRepository(
            LocalRepository repository,
            DeviceRepositoryAccessor accessor,
            boolean allowExperimentalPolicies,
            UnknownDevicesLogger logger) {

        this.repository = repository;
        this.accessor = accessor;
        this.allowExperimentalPolicies = allowExperimentalPolicies;
        cachedCategoryDescriptorRequests =
            Collections.synchronizedMap(new HashMap());
        this.unknownDevicesLogger = logger;
    }

    /**
     * Construct an instance of the DeviceRepository with the specified
     * parameters. By default, experimental policies are not accessible.
     *
     * @param repository the repository.
     * @param accessor   the device repository accessor.
     */
    public DefaultDeviceRepository(
            LocalRepository repository,
            DeviceRepositoryAccessor accessor) {
        this(repository, accessor, false);
    }

    /**
     * Construct an instance of the DeviceRepository with the specified
     * parameters. By default, experimental policies are not accessible.
     *
     * @param repository the repository.
     * @param accessor   the device repository accessor.
     * @param logger     the logger for unknown and abstract devices,may be null
     */
    public DefaultDeviceRepository(
            LocalRepository repository,
            DeviceRepositoryAccessor accessor,
            UnknownDevicesLogger logger) {
        this(repository, accessor, false, logger);
    }

    // javadoc inherited
    public Device getDevice(String deviceName)
            throws DeviceRepositoryException {
        Device device = null;

        if (deviceName != null) {
            try {
                RepositoryConnection connection = repository.connect();
                try {
                    device =
                        DevicesHelper.getDevice(connection, deviceName, accessor);
              } finally {
                    repository.disconnect(connection);
                }
            } catch (RepositoryException e) {
                throw new DeviceRepositoryException(e);
            }
        }

        return device;
    }

    // JavaDoc inherited
    public List getDevices(String deviceNamePattern)
            throws DeviceRepositoryException {
        List devices = null;
        if (deviceNamePattern != null) {
            try {
                RepositoryConnection connection = repository.connect();

                try {
                    devices = accessor.enumerateDeviceNames(connection,
                                                            deviceNamePattern);
                } finally {
                    repository.disconnect(connection);
                }
            } catch (RepositoryException e) {
                throw new DeviceRepositoryException(e);
            }
        }
        return devices;
    }

    // javadoc inherited
    public String getDeviceNameByIMEI(String imei)
            throws DeviceRepositoryException {
        String devName = null;
        if (imei != null) {
            if (imei.length() != 15) {
                throw new IllegalArgumentException("'" + imei +
                        "' is not a valid IMEI");
            } else {
                for (int i = 0; i < imei.length(); i++) {
                    final char ch = imei.charAt(i);
                    if (ch < '0' || ch > '9') {
                        throw new IllegalArgumentException("'" + imei +
                                "' is not a valid IMEI");
                    }
                }
            }
            try {
                RepositoryConnection connection = repository.connect();
                try {
                    long tacL = Long.parseLong(imei.substring(0,8));
                    devName = accessor.retrieveDeviceName(connection,
                            tacL);
                    if (devName == null) {
                        // Strip the last two digits
                        tacL /= 100;
                        devName = accessor.retrieveDeviceName(connection, tacL);
                    }
                } finally {
                    repository.disconnect(connection);
                }
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(imei + " is not a valid IMEI");
            } catch (RepositoryException re) {
                throw new DeviceRepositoryException(re);
            }
            if (devName == null) {
                if (unknownDevicesLogger != null) {
                    try {
                        unknownDevicesLogger.appendEntry(
                            new IMEIEntry(imei));
                    } catch (IOException e) {
                        LOGGER.warn("cannot-write-unknown-devices-log-file");
                    }
                }
            }
        }
        return devName;
    }

    // javadoc inherited
    public String getDeviceNameByTAC(String tac) throws DeviceRepositoryException {
        String devName = null;
        if (tac != null) {
            if (tac == null || (tac.length() != 8 && tac.length() != 6)) {
                throw new IllegalArgumentException(tac + " is not a valid TAC");
            }

            try {
                RepositoryConnection connection = repository.connect();
                try {
                    long tacL = Long.parseLong(tac);
                    devName = accessor.retrieveDeviceName(connection,
                            tacL);
                } finally {
                    repository.disconnect(connection);
                }
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(tac + " is not a valid TAC");
            } catch (RepositoryException re) {
                throw new DeviceRepositoryException(re);
            }
            if (devName == null) {
                if (unknownDevicesLogger != null) {
                    try {
                        unknownDevicesLogger.appendEntry(
                            new com.volantis.devrep.repository.api.devices.logging.TACEntry(tac));
                    } catch (IOException e) {
                        LOGGER.warn("cannot-write-unknown-devices-log-file");
                    }
                }
            }
        }
        return devName;
    }

    // javadoc inherited
    public String getDeviceNameByTACFAC(String tacfac)
            throws DeviceRepositoryException {
        String devName = null;
        if (tacfac != null) {
            if (tacfac == null || tacfac.length() != 8) {
                throw new IllegalArgumentException(tacfac +
                        " is not a valid TAC/FAC combo");
            }

            try {
                RepositoryConnection connection = repository.connect();
                try {
                    long tacL = Long.parseLong(tacfac);
                    devName = accessor.retrieveDeviceName(connection,
                            tacL);
                    if (devName == null) {
                        // Strip the last two digits
                        tacL /= 100;
                        devName = accessor.retrieveDeviceName(connection,
                                tacL);
                    }
                } finally {
                    repository.disconnect(connection);
                }
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(tacfac +
                        " is not a valid TAC/FAC combination");
            } catch (RepositoryException re) {
                throw new DeviceRepositoryException(re);
            }
            if (devName == null) {
                if (unknownDevicesLogger != null) {
                    try {
                        unknownDevicesLogger.appendEntry(
                            new TACFACEntry(tacfac));
                    } catch (IOException e) {
                        LOGGER.warn("cannot-write-unknown-devices-log-file");
                    }
                }
            }
        }
        return devName;
    }

    // javadoc inherited
    public String getDeviceNameByUAProfURL(URL uaprofUrl)
            throws DeviceRepositoryException {
        String deviceName = null;
        if (uaprofUrl != null) {
            try {
                deviceName = DevicesHelper.getDeviceName(accessor,
                        DevicesHelper.UAPROF_PREFIX,
                        uaprofUrl.toExternalForm(), null);
            } catch (RepositoryException e) {
                throw new DeviceRepositoryException(e);
            }
            if (deviceName == null) {
                if (unknownDevicesLogger != null) {
                    try {
                        unknownDevicesLogger.appendEntry(
                            new UAProfileEntry(
                                uaprofUrl.toString()));
                    } catch (IOException e) {
                        LOGGER.warn("cannot-write-unknown-devices-log-file");
                    }
                }
            }
        }
        return deviceName;
    }

    // javadoc inherited
    public List getDevicePolicyNames()
            throws DeviceRepositoryException {

        Set names = getCachedAccessiblePolicyNames();

        return new ArrayList(names);
    }

    // javadoc inherited
    public List getPolicyCategoryNames()
            throws DeviceRepositoryException {

        final List names = new LinkedList();
        try {
            final RepositoryConnection connection = repository.connect();
            try {
                final RepositoryEnumeration e =
                    accessor.enumerateCategoryNames(connection);
                try {
                    while (e.hasNext()) {
                        final String categoryName = (String) e.next();
                        names.add(categoryName);
                    }
                } finally {
                    e.close();
                }
            } finally {
                repository.disconnect(connection);
            }
        } catch (RepositoryException e) {
            throw new DeviceRepositoryException(e);
        }
        return names;
    }

    // javadoc inherited
    public List getDevicePolicyNamesByCategory(String category)
            throws DeviceRepositoryException {

        // NOTE: this is not cached but also probably not called very often
        // either.
        List names = null;
        if (category != null) {
            names = new ArrayList();
            try {
                RepositoryConnection connection = repository.connect();
                try {
                    RepositoryEnumeration e = accessor.enumeratePolicyNames(
                            connection, category);
                    try {
                        while (e.hasNext()) {
                            String policyName = (String) e.next();
                            if (isPolicyNameAccessible(policyName)) {
                                names.add(policyName);
                            }
                        }
                    } finally {
                        e.close();
                    }
                } finally {
                    repository.disconnect(connection);
                }
            } catch (RepositoryException e) {
                throw new DeviceRepositoryException(e);
            }
        }
        return names;
    }


    public PolicyDescriptor getPolicyDescriptor(String policyName, Locale locale)
            throws DeviceRepositoryException {
        PolicyDescriptor descriptor = null;

        if (getCachedAccessiblePolicyNames().contains(policyName)) {
            final AbstractDeviceRepositoryAccessor abstractAccessor =
                ((AbstractDeviceRepositoryAccessor) accessor);
            final PolicyDescriptorAccessor descriptorAccessor =
                abstractAccessor.getPolicyDescriptorAccessor();
            descriptor = descriptorAccessor.getPolicyDescriptor(policyName, locale);
        }
        // else, policy name is invalid.

        // If we can't find the descriptor, we just return null.
        // However, we should log in this case to help debugging.
        if (descriptor == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cannot find policy descriptor for policy: " +
                        policyName);
            }
        }
        return descriptor;
    }

    // javadoc inherited
    public CategoryDescriptor getCategoryDescriptor(
                String categoryName, Locale locale)
            throws DeviceRepositoryException {
        CategoryDescriptor descriptor = null;
        if (categoryName != null) {
            // get the right cache
            Map nameToDescriptor =
                (Map) cachedCategoryDescriptorRequests.get(locale);
            if (nameToDescriptor == null) {
                nameToDescriptor =
                    Collections.synchronizedMap(new HashMap());
                cachedCategoryDescriptorRequests.put(locale, nameToDescriptor);
            }
            // check if it's been cached
            descriptor = (CategoryDescriptor) nameToDescriptor.get(categoryName);
            if (descriptor == null) {
                // if not go to the repository and load it
                try {
                    RepositoryConnection connection = repository.connect();
                    try {
                        descriptor = accessor.retrieveCategoryDescriptor(
                                connection, categoryName, locale);
                        nameToDescriptor.put(categoryName, descriptor);
                    } finally {
                        repository.disconnect(connection);
                    }
                } catch (RepositoryException e) {
                    throw new DeviceRepositoryException(e);
                }
            }
        }

        // If we can't find the descriptor, we just return null.
        // However, we should log in this case to help debugging.
        if (descriptor == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cannot find category descriptor for policy: " +
                        categoryName);
            }
        }

        return descriptor;
    }

    // javadoc inherited.
    public Device getDevice(final HttpHeaders headers)
            throws DeviceRepositoryException {
        return getDevice(headers, null);
    }

    // javadoc inherited.
    public Device getDevice(final HttpHeaders headers, final String defaultDeviceName)
            throws DeviceRepositoryException {

        DefaultDevice device = null;
        if (headers != null) {
            try {
                RepositoryConnection connection = repository.connect();
                try {
                    final DeviceIdentificationResult identificationResult =
                        DevicesHelper.getDevice(
                            connection, headers, accessor, unknownDevicesLogger, defaultDeviceName);
                    device = identificationResult.getDevice();
                    if (device != null) {
                        device.setIdentificationHeaderNames(
                            identificationResult.getHeaderNamesUsed());
                    }
                } finally {
                    repository.disconnect(connection);
                }
            } catch (RepositoryException e) {
                throw new DeviceRepositoryException(e);
            }
        }
        return device;
    }

    public String getFallbackDeviceName(final String deviceName)
            throws DeviceRepositoryException {

        final Device device = getDevice(deviceName);
        if (device == null) {
            throw new IllegalArgumentException(deviceName +
                " is not a valid device name.");
        }
        return ((DefaultDevice) device).getFallbackDeviceName();
    }

    public List getChildrenDeviceNames(final String deviceName)
            throws DeviceRepositoryException {

        final List names = new LinkedList();
        try {
            final RepositoryConnection connection = repository.connect();
            try {
                final RepositoryEnumeration e = accessor.enumerateDevicesChildren(
                        connection, deviceName);
                try {
                    while (e.hasNext()) {
                        final String childName = (String) e.next();
                        names.add(childName);
                    }
                } finally {
                    e.close();
                }
            } finally {
                repository.disconnect(connection);
            }
        } catch (RepositoryException e) {
            throw new DeviceRepositoryException(e);
        }
        return names;
    }

    /**
     * Return the value of {@link #cachedAccessiblePolicyNames}, lazy loading
     * it if required.
     *
     * @return the set of all accessible policy names.
     * @throws DeviceRepositoryException
     */
    private Set getCachedAccessiblePolicyNames()
            throws DeviceRepositoryException {
        // Lazy load the cache of accessible policy names if necessary.
        if (cachedAccessiblePolicyNames == null) {
            try {
                cachedAccessiblePolicyNames = calculateAccessiblePolicyNames();
            } catch (RepositoryException e) {
                throw new DeviceRepositoryException(e);
            }
        }
        return cachedAccessiblePolicyNames;
    }

    /**
     * Return a set containing all "accessible" policy names.
     * <p>
     * This is used to create a cache.
     *
     * @return a Set which may be empty but not null.
     */
    private Set calculateAccessiblePolicyNames() throws RepositoryException {

        Set result = new HashSet();

        RepositoryConnection connection = repository.connect();
        try {
            RepositoryEnumeration policyNames =
                    accessor.enumeratePolicyNames(connection);
            try {
                while (policyNames.hasNext()) {
                    String policyName = (String) policyNames.next();
                    if (isPolicyNameAccessible(policyName)) {
                        result.add(policyName);
                    }
                }
            } finally {
                policyNames.close();
            }
        } finally {
            repository.disconnect(connection);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Finished copying PolicyNames to " +
                    "PolicyDescriptor cache, total number =" +
                    result.size());
        }

        return result;
    }

    /**
     * Checks to see whether a policy name should be accessible at runtime by
     * customer content.
     *
     * @param policyName The name of the policy being checked
     * @return True if the policy is accessible at runtime
     */
    private boolean isPolicyNameAccessible(String policyName) {
        boolean isAccessible = true;
        if (policyName == null) {
            isAccessible = false;
        } else if (!allowExperimentalPolicies && policyName.startsWith(
                DevicePolicyConstants.EXPERIMENTAL_POLICY_PREFIX)) {
            isAccessible = false;
        }
        return isAccessible;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-05	9780/6	pabbott	VBM:2005100504 PolicyDescriptor cache

 20-Oct-05	9780/4	pabbott	VBM:2005100504 PolicyDescriptor cache

 26-Sep-05	9593/1	adrianj	VBM:2005092209 Hide experimental device policies from customer code

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Sep-04	5408/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 03-Sep-04	5387/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 01-Sep-04	5343/1	byron	VBM:2004082406 DeviceRepository.getDevice(String name) not setting fallbacks

 01-Sep-04	5345/1	byron	VBM:2004082406 DeviceRepository.getDevice(String name) not setting fallbacks

 06-Aug-04	5121/5	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC (rework issues)

 06-Aug-04	5121/1	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC

 06-Aug-04	5088/2	byron	VBM:2004080301 Public API for device lookup: getDeviceNameByUAProfURL JDBC&XML

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4940/5	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 28-Jul-04	4940/3	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 28-Jul-04	4970/2	byron	VBM:2004072704 Public API for Device Repository: implement unit and/or integration tests

 28-Jul-04	4935/4	claire	VBM:2004072106 Public API for Device Repository: Retrieve devices with pattern matching; supermerge

 22-Jul-04	4935/1	claire	VBM:2004072106 Public API for Device Repository: Retrieve devices with pattern matching

 27-Jul-04	4937/6	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers - rework issues

 23-Jul-04	4937/3	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 26-Jul-04	4939/2	claire	VBM:2004072103 Fixed supermerge issues

 23-Jul-04	4945/1	geoff	VBM:2004072205 Public API for Device Repository: Common Metadata Infrastructure

 23-Jul-04	4959/1	philws	VBM:2004072307 Add TAC, TAC+FAC, IMEI and UAProf URL to device name resolution API

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/
