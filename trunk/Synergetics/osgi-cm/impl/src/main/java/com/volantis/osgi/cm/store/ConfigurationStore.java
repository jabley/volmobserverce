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

package com.volantis.osgi.cm.store;

import com.volantis.osgi.cm.InternalConfiguration;

import java.io.IOException;

/**
 * Responsible for storing configuration in a persistent set of files.
 *
 * <p> <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong> </p>
 *
 * @mock.generate
 */
public interface ConfigurationStore {

    /**
     * Load all the persisted configuration.
     *
     * @return The persisted configuration.
     * @throws IOException If there was a problem accessing the files.
     */
    InternalConfiguration[] load()
            throws IOException;

    /**
     * Update the configuration in the bundle data.
     *
     * <p>This must be called while synchronized on the configuration.</p>
     *
     * @param configuration
     * @throws IOException If there was a problem accessing the files.
     */
    void update(InternalConfiguration configuration)
            throws IOException;

    /**
     * Remove the configuration from the bundle data.
     *
     * <p>This must be called while synchronized on the configuration.</p>
     *
     * @param configuration
     * @throws IOException If there was a problem accessing the files.
     */
    void remove(InternalConfiguration configuration) throws IOException;

    /**
     * Create a factory configuration.
     *
     * <p>If this completes successfully then a file will have been created into
     * which the configuration can persist information but it will not have any
     * properties.</p>
     *
     * @param factoryPid     The pid of the factory.
     * @param bundleLocation The bundle location, may be null.
     * @return The newly created configuration with a dynamically generated
     *         pid.
     * @throws IOException If there was a problem accessing the files.
     */
    InternalConfiguration createFactoryConfiguration(
            String factoryPid, String bundleLocation)
            throws IOException;

    /**
     * Create a service configuration.
     *
     * <p>If this completes successfully then a file will have been created into
     * which the configuration can persist information but it will not have any
     * properties.</p>
     *
     * @param pid            The pid of the service.
     * @param bundleLocation The bundle location, may be null.
     * @return The newly created configuration.
     * @throws IOException If there was a problem accessing the files.
     */
    InternalConfiguration createConfiguration(
            String pid, String bundleLocation) throws IOException;
}
