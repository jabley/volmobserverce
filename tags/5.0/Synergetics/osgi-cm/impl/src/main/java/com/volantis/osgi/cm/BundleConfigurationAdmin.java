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
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;

/**
 * A {@link ConfigurationAdmin} implementation.
 *
 * <p>An instance of this is created for each calling bundle and this just
 * simply delegates to an equivalent method on the manager passing the calling
 * bundle.</p>
 */
public class BundleConfigurationAdmin
        implements ConfigurationAdmin {

    /**
     * The manager.
     */
    private final ConfigurationManager manager;

    /**
     * The calling bundle.
     */
    private final Bundle bundle;

    /**
     * Initialise.
     *
     * @param manager The manager.
     * @param bundle  The calling bundle.
     */
    public BundleConfigurationAdmin(
            ConfigurationManager manager, Bundle bundle) {
        this.manager = manager;
        this.bundle = bundle;
    }

    /**
     * Wrap the internal configuration in a bundle specific configuration.
     *
     * @param configuration The internal configuration.
     * @return The bundle specific configuration.
     */
    private Configuration wrapConfiguration(
            InternalConfiguration configuration) {

        return new BundleConfiguration(manager, bundle, configuration);
    }

    // Javadoc inherited.
    public Configuration createFactoryConfiguration(String factoryPid)
            throws IOException {

        InternalConfiguration configuration =
                manager.createFactoryConfiguration(bundle, factoryPid);

        return wrapConfiguration(configuration);
    }

    // Javadoc inherited.
    public Configuration createFactoryConfiguration(
            String factoryPid, String location) throws IOException {
        InternalConfiguration configuration =
                manager.createFactoryConfiguration(bundle,
                        factoryPid, location);
        return wrapConfiguration(configuration);
    }

    // Javadoc inherited.
    public Configuration getConfiguration(String pid) throws IOException {
        InternalConfiguration configuration =
                manager.getConfiguration(bundle, pid);
        return wrapConfiguration(configuration);
    }

    // Javadoc inherited.
    public Configuration getConfiguration(String pid, String location)
            throws IOException {
        InternalConfiguration configuration =
                manager.getConfiguration(bundle, pid, location);
        return wrapConfiguration(configuration);
    }

    // Javadoc inherited.
    public Configuration[] listConfigurations(String filter)
            throws IOException, InvalidSyntaxException {
        InternalConfiguration[] internal =
                manager.listConfigurations(bundle, filter);
        if (internal == null) {
            return null;
        } else {
            Configuration[] external = new Configuration[internal.length];
            for (int i = 0; i < internal.length; i++) {
                external[i] = wrapConfiguration(internal[i]);
            }
            return external;
        }
    }
}
