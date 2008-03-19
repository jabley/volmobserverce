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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.cm;

import org.osgi.framework.Bundle;
import org.osgi.service.cm.Configuration;

import java.io.IOException;
import java.util.Dictionary;

/**
 * A {@link Configuration} that belongs to a specific bundle.
 *
 * <p>The main purpose of this is to identify the calling bundle so that it can
 * be checked to ensure that it has permission to change the bound location.
 * This also routes almost all requests (possibly should be all) through the
 * {@link ConfigurationManager} in order to simplify synchronization
 * issues.</p>
 */
public class BundleConfiguration
        implements Configuration {

    /**
     * The manager through which most requests are routed.
     */
    private final ConfigurationManager manager;

    /**
     * The bundle that created the configuration.
     */
    private final Bundle bundle;

    /**
     * The underlying configuration.
     */
    private final InternalConfiguration configuration;

    /**
     * The pid for this configuration.
     */
    private final String pid;

    /**
     * The factory pid for this configuration, may be null.
     */
    private final String factoryPid;

    /**
     * Initialise.
     *
     * @param manager       The manager through which most requests are routed.
     * @param bundle        The bundle that created the configuration.
     * @param configuration The underlying configuration.
     */
    public BundleConfiguration(
            ConfigurationManager manager,
            Bundle bundle, InternalConfiguration configuration) {
        this.manager = manager;
        this.bundle = bundle;
        this.configuration = configuration;
        this.pid = configuration.getPid();
        this.factoryPid = configuration.getFactoryPid();
    }

    private void ensureNotDeleted() {
        // todo Possible synchronization issue so resolve this.
        // Get the state of the configuration.
        State state = configuration.getState();
        if (state == State.DELETED || state == State.DELETING) {
            throw new IllegalStateException(
                    "Configuration '" + pid + "' has been deleted");
        }
    }

    // Javadoc inherited.
    public String getPid() {
        ensureNotDeleted();

        return pid;
    }

    // Javadoc inherited.
    public Dictionary getProperties() {
        ensureNotDeleted();

        return manager.getConfigurationProperties(configuration);
    }

    // Javadoc inherited.
    public void update(Dictionary properties) throws IOException {
        ensureNotDeleted();

        if (properties == null) {
            throw new IllegalArgumentException("properties cannot be null");
        }
        manager.updateConfiguration(configuration, properties);
    }

    // Javadoc inherited.
    public void delete() throws IOException {
        ensureNotDeleted();

        manager.deleteConfiguration(configuration);
    }

    // Javadoc inherited.
    public String getFactoryPid() {
        ensureNotDeleted();

        return factoryPid;
    }

    // Javadoc inherited.
    public void update() throws IOException {
        ensureNotDeleted();

        manager.updateConfiguration(configuration, null);
    }

    // Javadoc inherited.
    public void setBundleLocation(String bundleLocation) {
        SecurityHelper.ensureHasConfigurePermission(bundle);
        ensureNotDeleted();
        manager.setConfigurationBundleLocation(configuration, bundleLocation);
    }

    // Javadoc inherited.
    public String getBundleLocation() {
        SecurityHelper.ensureHasConfigurePermission(bundle);
        ensureNotDeleted();
        return manager.getConfigurationBundleLocation(configuration);
    }

    // Javadoc inherited.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BundleConfiguration)) {
            return false;
        }

        BundleConfiguration that = (BundleConfiguration) o;
        return pid.equals(that.pid);
    }

    // Javadoc inherited.
    public int hashCode() {
        return pid.hashCode();
    }
}
