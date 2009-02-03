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

import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.File;
import java.util.Dictionary;

/**
 * The basic configuration object.
 */
public class ConfigurationImpl
        implements InternalConfiguration {

    /**
     * The pid.
     */
    private final String pid;

    /**
     * The factory pid, may be null.
     */
    private final String factoryPid;

    /**
     * The file into which this is persisted.
     */
    private final File persistentFile;

    /**
     * The properties, may be null.
     */
    private CaseInsensitiveDictionary properties;

    /**
     * The bundle location that was specified through the {@link
     * ConfigurationAdmin} and {@link Configuration} APIs.
     */
    private String specifiedLocation;

    /**
     * The actual location to which this is bound.
     *
     * <p>This will be the same as {@link #specifiedLocation} unless that is
     * null in which case this will either be null indicating that it is
     * unbound, or the location of the first bundle that registered a service
     * with the matching pid.</p>
     */
    private String boundLocation;

    /**
     * The current state of this.
     */
    private State state;

    /**
     * Initialise.
     *
     * @param pid            The pid.
     * @param factoryPid     The factory pid, may be null.
     * @param persistentFile The file into which this is persisted.
     * @param bundleLocation The bundle location that was specified through the
     *                       {@link ConfigurationAdmin} and {@link
     *                       Configuration} APIs.
     * @param properties     The properties, may be null.
     */
    public ConfigurationImpl(
            String pid, String factoryPid, File persistentFile,
            String bundleLocation,
            CaseInsensitiveDictionary properties) {

        this.pid = pid;
        this.factoryPid = factoryPid;
        this.persistentFile = persistentFile;
        this.properties = properties;
        this.specifiedLocation = bundleLocation;
        this.boundLocation = bundleLocation;

        if (properties == null) {
            state = State.NEW;
        } else {
            state = State.ACTIVE;
        }
    }

    // Javadoc inherited.
    public void ensureNotDeleted() {

        if (state == State.DELETED) {
            throw new IllegalStateException(
                    "Configuration '" + pid + "' has been deleted");
        }
    }

    // Javadoc inherited.
    public synchronized String getPid() {
        ensureNotDeleted();

        return pid;
    }

    // Javadoc inherited.
    public synchronized String getFactoryPid() {
        ensureNotDeleted();

        return factoryPid;
    }

    // Javadoc inherited.
    public synchronized Dictionary getProperties() {
        ensureNotDeleted();

        if (properties == null) {
            return null;
        } else {
            CaseInsensitiveDictionary dictionary =
                    new CaseInsensitiveDictionary(properties);

            // Ensure the required properties are set correctly.
            dictionary.put(FrameworkConstants.SERVICE_PID, pid);
            if (factoryPid == null) {
                dictionary.remove(FrameworkConstants.SERVICE_FACTORYPID);
            } else {
                dictionary
                        .put(FrameworkConstants.SERVICE_FACTORYPID, factoryPid);
            }

            return dictionary;
        }
    }

    // Javadoc inherited.
    public void replaceProperties(Dictionary newProperties) {

        ensureNotDeleted();

        if (properties == null) {
            properties = new CaseInsensitiveDictionary();
        } else {
            properties.clear();
        }

        ValidationHelper.copyAndCheckProperties(newProperties, properties);

        // Copy all the new properties into this.
        properties.putAll(newProperties);

        state = State.ACTIVE;
    }

    // Javadoc inherited.
    public void beginDeleting() {
        this.state = State.DELETING;
    }

    public void deleteCompleted() {
        this.state = State.DELETED;
    }

    // Javadoc inherited.
    public State getState() {
        return state;
    }

    // Javadoc inherited.
    public ConfigurationSnapshot createSnapshot() {
        return new ConfigurationSnapshot(pid,
                new CaseInsensitiveDictionary(properties));
    }

    // Javadoc inherited.
    public boolean bindToLocation(String serviceLocation) {
        if (boundLocation == null) {
            this.boundLocation = serviceLocation;
        } else if (!boundLocation.equals(serviceLocation)) {
            // todo log message.
            return false;
        }

        return true;
    }

    // Javadoc inherited.
    public void unbindFromLocation() {
        this.boundLocation = specifiedLocation;
    }

    // Javadoc inherited.
    public String getBundleLocation() {
        ensureNotDeleted();
        return boundLocation;
    }

    // Javadoc inherited.
    public String getSpecifiedLocation() {
        return specifiedLocation;
    }

    // Javadoc inherited.
    public void setSpecifiedLocation(String location) {
        ensureNotDeleted();

        this.specifiedLocation = location;
        this.boundLocation = location;
    }

    // Javadoc inherited.
    public File getPersistentFile() {
        return persistentFile;
    }

    /**
     * @noinspection RedundantIfStatement
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigurationImpl configuration = (ConfigurationImpl) o;

        if (specifiedLocation != null ?
                !specifiedLocation.equals(configuration.specifiedLocation) :
                configuration.specifiedLocation != null) return false;
        if (factoryPid != null ? !factoryPid.equals(configuration.factoryPid) :
                configuration.factoryPid != null) return false;
        if (!pid.equals(configuration.pid)) return false;
        if (properties != null ? !properties.equals(configuration.properties) :
                configuration.properties != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = pid.hashCode();
        result = 31 * result + (factoryPid != null ? factoryPid.hashCode() : 0);
        result = 31 * result +
                (specifiedLocation != null ? specifiedLocation.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }


    public String toString() {
        return "{pid=" + pid + ",factoryPid=" + factoryPid + ",location=" +
                specifiedLocation + ",properties " + properties + "}";
    }

}
