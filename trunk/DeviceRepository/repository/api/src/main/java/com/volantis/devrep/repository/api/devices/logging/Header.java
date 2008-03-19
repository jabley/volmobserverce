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
 * Helper class to store header information.
 */
public class Header implements Comparable {

    /**
     * Name of the header.
     */
    private final String name;

    /**
     * Value of the header.
     */
    private final String value;

    public Header(final String name, final String value) {
        if (name == null || value == null) {
            throw new IllegalArgumentException("Header name or value cannot " +
                "be null. Name: " + name + ", value: " + value);
        }
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the header
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of the header
     * @return the value
     */
    public String getValue() {
        return value;
    }

    // javadoc inherited
    public int compareTo(final Object o) {
        final Header other = (Header) o;
        int result = name.compareTo(other.name);
        if (result == 0) {
            result = value.compareTo(other.value);
        }
        return result;
    }

    // javadoc inherited
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Header other = (Header) o;
        return name.equals(other.name) && value.equals(other.value);
    }

    // javadoc inherited
    public int hashCode() {
        return 29 * name.hashCode() + value.hashCode();
    }
}
