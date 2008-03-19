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
 * This implementation of the Entry class stores a String as value.
 */
public class SimpleValueEntry extends Entry {
    /**
     * The value of the entry.
     */
    private final String value;

    public SimpleValueEntry(
            final String resolvedName, final String deviceType,
            final String query, final String value) {
        super(resolvedName, deviceType, query);
        this.value = value;
    }

    // javadoc inherited
    public String getValue() {
        return value;
    }

    // javadoc inherited
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SimpleValueEntry)) return false;
        if (!super.equals(o)) return false;

        final SimpleValueEntry that = (SimpleValueEntry) o;

        return value == null && that.value == null ||
            value != null && value.equals(that.value);
    }

    // javadoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
