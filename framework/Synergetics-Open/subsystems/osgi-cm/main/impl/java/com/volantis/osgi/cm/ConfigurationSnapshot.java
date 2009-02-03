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

import java.util.Dictionary;

/**
 * A snapshot of the configuration.
 *
 * <p>This is used to contain information that can then be passed to an
 * asynchronous task without worrying that the configuration will change state
 * before the task performs.</p>
 */
public class ConfigurationSnapshot {

    /**
     * The pid.
     */
    private final String pid;

    /**
     * The properties.
     */
    private final Dictionary properties;

    /**
     * Initialise.
     *
     * @param pid        The pid.
     * @param properties The properties.
     */
    public ConfigurationSnapshot(String pid, Dictionary properties) {
        if (pid == null) {
            throw new IllegalArgumentException("pid cannot be null");
        }
        if (properties == null) {
            throw new IllegalArgumentException("properties cannot be null");
        }

        this.pid = pid;
        this.properties = properties;
    }

    /**
     * Get the pid.
     *
     * @return The pid.
     */
    public String getPid() {
        return pid;
    }

    /**
     * Get the properties.
     *
     * @return The properties.
     */
    public Dictionary getProperties() {
        return properties;
    }

    /**
     * @noinspection RedundantIfStatement
     */ // Javadoc inherited.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigurationSnapshot that = (ConfigurationSnapshot) o;

        if (!pid.equals(that.pid)) return false;
        if (!properties.equals(that.properties)) return false;

        return true;
    }

    // Javadoc inherited.
    public int hashCode() {
        int result;
        result = pid.hashCode();
        result = 31 * result + properties.hashCode();
        return result;
    }

    // Javadoc inherited.
    public String toString() {
        return "{pid=" + pid + ",properties " + properties + "}";
    }
}
