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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.system;

/**
 * Abstracts away the system properties.
 *
 * @mock.generate
 */
public class SystemProperties {

    /**
     * The default instance.
     */
    private static final SystemProperties DEFAULT_INSTANCE =
            new SystemProperties();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static SystemProperties getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Gets the system property indicated by the specified key.
     *
     * @param key The key.
     * @return The system property value, or null.
     * @see System#getProperty(String, String)
     */
    public String getProperty(String key) {
        return System.getProperty(key);
    }

    /**
     * Gets the system property indicated by the specified key.
     *
     * @param key          The key.
     * @param defaultValue The default value.
     * @return The system property value, or the default value.
     * @see System#getProperty(String, String)
     */
    public String getProperty(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }
}
