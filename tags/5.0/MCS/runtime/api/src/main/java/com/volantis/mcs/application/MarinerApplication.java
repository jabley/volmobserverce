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
 * $Header: /src/voyager/com/volantis/mcs/application/MarinerApplication.java,v 1.3 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Nov-02    Paul            VBM:2002091806 - Created to provide an
 *                              environment independent application level
 *                              interface which hides the Volantis bean.
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.application;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.devrep.repository.api.devices.InternalDeviceRepositoryFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.Repository;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.repository.remote.RemotePolicyPreloader;

import javax.sql.DataSource;

/**
 * This class provides an interface to information about the currently running
 * MarinerApplication.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate
 */
public abstract class MarinerApplication {

    private static final InternalDeviceRepositoryFactory DEVICE_REPOSITORY_FACTORY =
        InternalDeviceRepositoryFactory.getDefaultInstance();

    /**
     * The Volantis bean.
     */
    private Volantis volantisBean;

    /**
     * Device repository.
     */
    private DeviceRepository deviceRepository;

    /**
     * Create a new <code>MarinerApplication</code>.
     * <p/>
     * This is protected to prevent instances of this object being created
     * directly.
     * </p>
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    protected MarinerApplication() {
        deviceRepository = null;
    }

    /**
     * Set the value of the volantis bean property.
     * <h2>
     * You MUST NOT change the protection level on this method.
     * </h2>
     * <p/>
     * To access this from another package you must use
     * {@link ApplicationInternals#setVolantisBean}.
     * </p>
     *
     * @param volantisBean The new value of the volantis bean property.
     */
    void setVolantisBean(Volantis volantisBean) {
        this.volantisBean = volantisBean;
    }

    /**
     * Get the value of the volantis bean property.
     * <h2>
     * You MUST NOT change the protection level on this method.
     * </h2>
     * <p/>
     * To access this from another package you must use
     * {@link ApplicationInternals#getVolantisBean}.
     * </p>
     *
     * @return The value of the volantis bean property.
     */
    Volantis getVolantisBean() {
        return volantisBean;
    }

    /**
     * Retrieves the Project information for a given named project.
     *
     * @param name The name of the project to locate.
     * @return Project information for the given project or null if it does
     *         not exist.
     */
    public Project getPredefinedProject(String name) {
        return volantisBean.getPredefinedProject(name);
    }

    /**
     * Retrieves a Repository associated withe the configured runtime local
     * Repository. Note that currently only a JDBC Repository is supported.
     *
     * @return The local Repository.
     *
     * @deprecated This no longer sets up the default project correctly.
     *             This was deprecated in version 3.5.1.
     */
    public Repository getLocalRepository(String defaultProjectName)
            throws RepositoryException {

        Repository repository = null;

        // Check that the bean exists
        if (volantisBean != null) {
            // Retrieve the JDBC DataSource configured in the runtime
            DataSource ds =
                    volantisBean.getLocalRepositoryJDBCDataSource();
            if (ds != null) {

                JDBCRepositoryFactory factory =
                        JDBCRepositoryFactory.getDefaultInstance();
                JDBCRepositoryConfiguration configuration =
                        factory.createJDBCRepositoryConfiguration();
                configuration.setDataSource(ds);
                configuration.setReleaseConnectionsImmediately(false);
                configuration.setShortNames(
                        volantisBean.getJDBCRepositoryUsesShortNames());

                repository = factory.createJDBCRepository(configuration);
            }
        }
        return repository;
    }

    /**
     * This method returns the {@link DeviceRepository} for the the currently
     * running MCS instance.
     *
     * @throws DeviceRepositoryException if there is a failure in accessing the
     *                                   underlying repository.
     */
    public DeviceRepository getRuntimeDeviceRepository()
            throws DeviceRepositoryException {

        // Check that the bean exists
        if (volantisBean != null) {

            // Synchronize while lazily creating the device repository.
            synchronized (this) {
                if (deviceRepository == null) {
                    // Retrieve the DeviceRepository configured in the runtime.
                    LocalRepository repository =
                            volantisBean.getDeviceRepository();

                    DeviceRepositoryAccessor accessor =
                            volantisBean.getDeviceRepositoryAccessor();

                    // get logger for abstract and unknown devices
                    deviceRepository =
                        DEVICE_REPOSITORY_FACTORY.createDeviceRepository(
                            repository, accessor,
                            volantisBean.getUnknownDevicesLogger());
                }
            }
        }

        return deviceRepository;
    }

    /**
     * This method retrieves entities from a remote policy server and adds them
     * to the remote repository. This is achieved by sending a request for policies
     * at the supplied URL. The policies returned from the URL are added to the
     * remote repository caches.
     *
     * @param url The URL which will return a RemotePolicySetResponse which can be
     *            processed and added to the remote repository caches.
     * @throws RepositoryException An exception encountered while retrieving the
     *                             RemotePolicySetResponse from the URL or when processing the returned response.
     */
    public void preloadRemotePolicies(String url)
            throws RepositoryException {

        RemotePolicyPreloader preloader = volantisBean.getRemotePolicyPreloader();
        preloader.preloadPolicies(url);
    }

    /**
     * Flush a given remote policy cache where the URL of the policy starts with a path.
     *
     * @param cache The ID of the cache to flush specified by
     *              <code>RepositoryCacheType</code>.
     * @param path  The URL prefix of the items to remove from the cache.
     *
     * @deprecated There is only one cache for all remote policy types so this
     *             ignores the cache type. Use
     *             {@link #flushRemotePolicyCache(String)} instead.
     *             This was deprecated in version 3.5.1.
     */
    public void flushRemotePolicyCache(RepositoryCacheType cache, String path) {
        volantisBean.flushRemoteCache(path);
    }

    /**
     * Flush the remote policy cache of all entries that start with the
     * specified path.
     *
     * @param path  The URL prefix of the items to remove from the cache.
     */
    public void flushRemotePolicyCache(String path) {
        volantisBean.flushRemoteCache(path);
    }

    /**
     * Flush the remote policy cache of all entries.
     */
    public void flushRemotePolicyCache() {
        volantisBean.flushRemoteCache(null);
    }

    /**
     * Flush a given remote policy cache where the URL of the policy starts with a path.
     *
     * @param cache The ID of the cache to flush specified by
     *              <code>RepositoryCacheType</code>.
     *
     * @deprecated Use {@link #flushRemotePolicyCache()} instead.
     *             This was deprecated in version 3.5.1.
     */
    public void flushRemotePolicyCache(RepositoryCacheType cache) {
        flushRemotePolicyCache(cache, null);
    }

    /**
     * A method to flush all the cached content in Mariner.
     */
    public void flushAllCaches() {
        volantisBean.flushAllCaches();
    }

    /**
     * Flush all remote policy caches where the path of the policy matches a given path.
     *
     * @param path The URL prefix of the items to remove from all caches
     *
     * @deprecated Use {@link #flushRemotePolicyCache(String)} instead.
     *             This was deprecated in version 3.5.1.
     */
    public void flushAllRemotePolicyCaches(String path) {
        flushRemotePolicyCache(path);
    }

    /**
     * Flush all remote policy caches of all entries.
     *
     * @deprecated Use {@link #flushRemotePolicyCache()} instead.
     *             This was deprecated in version 3.5.1.
     */
    public void flushAllRemotePolicyCaches() {
        flushRemotePolicyCache();
    }

    /**
     * Flush the policies of the specified policy types from the local policy
     * cache.
     *
     * @param policyType The type of policies to flush.
     * @since 3.5.1
     */
    public void flushLocalPolicyCache(PolicyType policyType) {
        volantisBean.flushLocalPolicyCache(policyType);
    }

    /**
     * Flush all the policies from the local policy caches.
     *
     * @since 3.5.1
     */
    public void flushLocalPolicyCaches() {
        volantisBean.flushLocalPolicyCaches();
    }

    /**
     * A method to flush all the theme cached content in Mariner.
     *
     * @deprecated Use {@link #flushLocalPolicyCache(PolicyType)} instead.
     *             This was deprecated in version 3.5.1.
     */
    public void flushThemeCache() {
        volantisBean.flushLocalPolicyCache(PolicyType.THEME);
    }

    /**
     * A method to flush all the layout cached content in Mariner.
     *
     * @deprecated Use {@link #flushLocalPolicyCache(PolicyType)} instead.
     *             This was deprecated in version 3.5.1.
     */
    public void flushLayoutCache() {
        volantisBean.flushLocalPolicyCache(PolicyType.LAYOUT);
    }

    /**
     * A method to flush all the device cached content in Mariner.
     * Note that calling this method also flushes the device
     * patterns {@link #refreshDevicePatterns}.
     */
    public void flushDeviceCache() {
        volantisBean.flushDeviceCache();
    }

    /**
     * A method to clear the device patterns and re-initialise them.
     * This method will also clear the device cache.
     * @deprecated #flushDeviceCache does the same
     */
    public void refreshDevicePatterns() {
        volantisBean.flushDeviceCache();
    }

    /**
     * A method to flush all the external repository plugin cached
     * content in Mariner.
     *
     * @deprecated External repository plugins have not been supported in MCS
     *             for a while so this method does nothing.
     *             This was deprecated prior to version 3.5.1.
     */
    public void flushExternalRepositoryPluginCache() {
    }

    /**
     * A method to flush all the component cached content in Mariner.
     *
     * @deprecated Use {@link #flushLocalPolicyCache(PolicyType)} instead.
     *             This was deprecated in version 3.5.1.
     */
    public void flushComponentAssetCache() {
        volantisBean.flushComponentAssetCache();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9727/1	ianw	VBM:2005100506 Fixed up remote repositories layout issues

 01-Jul-05	8616/5	ianw	VBM:2005060103 New page level CSS servlet

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 27-Apr-05	7896/1	pduffin	VBM:2005042709 Removing PolicyPreference and all related classes

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 23-Dec-04	6518/3	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 02-Aug-04	5034/1	geoff	VBM:2004080201 Implement the missing runtime access for device repository

 27-Jul-04	4937/3	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers - fix build dependencies

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 04-May-04	4111/3	ianw	VBM:2004042908 Fixed always returning null in getLocalRepository

 30-Apr-04	4111/1	ianw	VBM:2004042908 Added new Public API to get a local JDBC Repository

 10-Feb-04	2931/1	claire	VBM:2004021008 Added named projects from the config

 ===========================================================================
*/
