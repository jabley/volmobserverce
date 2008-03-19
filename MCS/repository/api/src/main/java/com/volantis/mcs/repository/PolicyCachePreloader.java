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
package com.volantis.mcs.repository;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class iterates through all the policies in a repository and loads
 * them in so as to populate the cache.
 */ 
public class PolicyCachePreloader {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PolicyCachePreloader.class);

    /**
     * List of policy classes we will load.
     */
    private static List POLICY_TYPES;

    static {
        POLICY_TYPES = new ArrayList();
        POLICY_TYPES.add(PolicyType.BASE_URL);
        POLICY_TYPES.add(PolicyType.AUDIO);
        POLICY_TYPES.add(PolicyType.BUTTON_IMAGE);
        POLICY_TYPES.add(PolicyType.CHART);
        POLICY_TYPES.add(PolicyType.VIDEO);
        POLICY_TYPES.add(PolicyType.IMAGE);
        POLICY_TYPES.add(PolicyType.ROLLOVER_IMAGE);
        POLICY_TYPES.add(PolicyType.TEXT);
        POLICY_TYPES.add(PolicyType.SCRIPT);
        POLICY_TYPES.add(PolicyType.LINK);
        POLICY_TYPES.add(PolicyType.THEME);
        POLICY_TYPES.add(PolicyType.LAYOUT);
    }

    /**
     * The repository.
     */
    private final LocalRepository repository;
    private final Project project;
    private final DeviceRepositoryAccessor deviceRepositoryAccessor;

    /**
     * Initialise.
     * 
     * @param repository the repository to load the caches from.
     * @param project
     * @param deviceRepositoryAccessor
     */ 
    public PolicyCachePreloader(
            LocalRepository repository,
            Project project,
            DeviceRepositoryAccessor deviceRepositoryAccessor) {

        this.repository = repository;
        this.project = project;
        this.deviceRepositoryAccessor = deviceRepositoryAccessor;
    }

    /**
     * Preload the caches as per the class comment.
     */ 
    public void run() {

        LocalRepositoryConnection connection = null;
        try {
            try {
                connection = (LocalRepositoryConnection) repository.connect();
                if (logger.isDebugEnabled()) {
                    logger.debug("Commencing Policy Preload...");
                }

                ProjectPolicyReader reader =
                        new ProjectPolicyReader(connection, project);

                Iterator i = POLICY_TYPES.iterator();
                while (i.hasNext()) {
                    PolicyType policyType = (PolicyType) i.next();
                    loadPolicies(reader, policyType);
                }

                // Load the devices.
                loadDevices(connection);

                if (logger.isDebugEnabled()) {
                    logger.debug("Policy Preload Complete");
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (Exception e) {
            logger.error("policy-preload-error", e);
            // and give up.
        }
    }

    private void loadDevices(LocalRepositoryConnection connection)
            throws RepositoryException {

        RepositoryEnumeration e = deviceRepositoryAccessor.enumerateDeviceNames(connection);
        try {
            while (e.hasNext()) {
                String deviceName = (String) e.next();

                loadDevice(connection, deviceRepositoryAccessor, deviceName);
            }
        } finally {
            e.close();
        }
    }

    private void loadDevice(
            RepositoryConnection connection, DeviceRepositoryAccessor accessor,
            String deviceName) throws RepositoryException {

        // Load in the object.
        DefaultDevice device = accessor.retrieveDevice(connection, deviceName);
        if (device == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("The device named " + deviceName +
                    " was not found in the repository. Skipping...");
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Preloading device " + deviceName);
        }
    }

    /**
     * Load policies of a given type.
     * 
     * @param reader
     * @param policyType the policy type.
     * @exception RepositoryException if an error occurs
     */
    private void loadPolicies(
            ProjectPolicyReader reader, PolicyType policyType)
            throws RepositoryException {

        if (logger.isDebugEnabled()) {
            logger.debug("Preloading all policies for " + policyType);
        }

        RepositoryEnumeration e = reader.enumeratePolicyNames(policyType);
        try {
            while (e.hasNext()) {
                String name = (String) e.next();

                loadPolicy(reader, name);
            }
        } finally {
            e.close();
        }
    }

    /**
     * Load a policy and any dependant children.
     * 
     * @param reader the reader for the policy
     * @param name the name of the policy
     * @exception RepositoryException if an error occurs
     */
    private void loadPolicy(
            ProjectPolicyReader reader,
            String name)
            throws RepositoryException {

        // Load in the object.
        Policy policy = reader.retrievePolicy(name);
        if (policy == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("The policy named " + name +
                    " was not found in the repository. Skipping...");
            }
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Preloading policy " + name);
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10550/1	geoff	VBM:2005113025 MCS35: mcsExport no longer works with the -all flag

 03-May-05	7963/1	pduffin	VBM:2005042906 Removed DDM components, e.g. ApplicationProperties, URLMappers, DDMProxy, etc

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 25-May-04	4507/2	geoff	VBM:2004051809 pre populate policy caches

 20-May-04	4480/4	geoff	VBM:2004051809 pre populate policy caches (use modified caches)

 19-May-04	4480/2	geoff	VBM:2004051809 pre populate policy caches

 ===========================================================================
*/
