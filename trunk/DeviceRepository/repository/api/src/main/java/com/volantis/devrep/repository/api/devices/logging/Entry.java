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
package com.volantis.devrep.repository.api.devices.logging;

/**
 * Base class for log entries.
 */
public abstract class Entry {
    /**
     * The name of the device.
     */
    private final String resolvedName;

    /**
     * The type of the entry: abstract or unknown
     */
    private final String deviceType;

    /**
     * The type of the query that caused the log entry
     */
    private final String query;

    public Entry(final String resolvedName, final String deviceType,
                 final String query) {

        this.resolvedName = resolvedName;
        this.deviceType = deviceType;
        this.query = query;
    }

    /**
     * Returns the resolved device name.
     * @return the name of the device
     */
    public String getResolvedName() {
        return resolvedName;
    }

    /**
     * Returns the entry type
     * @return "abstract" or "unknown"
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Returns the type of the query.
     * @return the type of the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Returns the value used for the query.
     * @return the value
     */
    public abstract String getValue();

    // javadoc inherited
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Entry)) return false;

        final Entry entry = (Entry) o;

        if (deviceType != null && !deviceType.equals(entry.deviceType) ||
            deviceType == null && entry.deviceType != null)
        {
            return false;
        }
        if (query != null && !query.equals(entry.query) ||
            query == null && entry.query != null)
        {
            return false;
        }
        if (resolvedName != null && !resolvedName.equals(entry.resolvedName) ||
            resolvedName == null && entry.resolvedName != null)
        {
            return false;
        }
        return true;
    }

    // javadoc inherited
    public int hashCode() {
        int result = (resolvedName != null ? resolvedName.hashCode() : 0);
        result =
            29 * result + (deviceType != null ? deviceType.hashCode() : 0);
        result = 29 * result + (query != null ? query.hashCode() : 0);
        return result;
    }
}
