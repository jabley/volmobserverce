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

import java.io.File;
import java.util.Dictionary;

/**
 * Internal configuration.
 */
public interface InternalConfiguration {

    /**
     * Get the pid.
     *
     * @return The pid.
     */
    String getPid();

    /**
     * Get the factory pid.
     *
     * @return The factory pid, may be null.
     */
    String getFactoryPid();

    /**
     * Get the file into which this is persisted.
     *
     * @return The persistent file.
     */
    File getPersistentFile();

    /**
     * Get the properties.
     *
     * @return The properties, may be null.
     */
    Dictionary getProperties();

    /**
     * Set the specified bundle location, i.e. the location provided by the
     * API.
     *
     * <p>This will also update the current location.</p>
     *
     * @param location The location.
     */
    void setSpecifiedLocation(String location);

    /**
     * Get the specified location.
     *
     * @return The specified location.
     */
    String getSpecifiedLocation();

    /**
     * Get the actual location to which this is bound.
     *
     * @return The bound location.
     */
    String getBundleLocation();

    /**
     * Ensure that this is not deleted.
     */
    void ensureNotDeleted();

    /**
     * Replaces the properties with those in the dictionary provided.
     *
     * <p>The "service.pid" and where necessary "service.factoryPid" properties
     * are also set
     *
     * @param newProperties The new properties.
     */
    void replaceProperties(Dictionary newProperties);

    /**
     * Begin deleting.
     */
    void beginDeleting();

    /**
     * Delete has completed.
     */
    void deleteCompleted();

    /**
     * Get the state.
     *
     * @return The state.
     */
    State getState();

    /**
     * Create a snapshot.
     *
     * @return The snapshot.
     */
    ConfigurationSnapshot createSnapshot();

    /**
     * Bind to the specified location.
     *
     * @param serviceLocation The location.
     * @return True if this succeeded, false otherwise.
     */
    boolean bindToLocation(String serviceLocation);

    /**
     * Unbind from the current location.
     */
    void unbindFromLocation();
}
