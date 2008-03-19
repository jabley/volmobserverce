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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.jdbc;

/**
 * The alternate names of a JDBC entity.
 *
 * <p>Some databases have limits on the size of the table names so this object
 * encapsulates a normal and short name.</p>
 */
public class AlternateNames {

    /**
     * The normal name.
     */
    private final String normalName;

    /**
     * The short name.
     */
    private final String shortName;

    /**
     * Initialise.
     *
     * @param normalName The normal name.
     * @param shortName  The short name.
     */
    public AlternateNames(String normalName, String shortName) {
        if (normalName == null) {
            throw new IllegalArgumentException("normalName cannot be null");
        }
        if (shortName == null) {
            throw new IllegalArgumentException("shortName cannot be null");
        }
        checkShortName(shortName);
        this.normalName = normalName;
        this.shortName = shortName;
    }

    /**
     * Initialise the normal and short names to the same value.
     *
     * @param name The normal and short name.
     */
    public AlternateNames(String name) {
        this(name, name);
    }

    /**
     * Ensure that the name is short enough.
     *
     * @param shortName The short name.
     */
    private void checkShortName(String shortName) {
        if (shortName.length() > 18) {
            throw new IllegalArgumentException(
                    "Short name must be 18 characters or less");
        }
    }

    /**
     * Get the normal name.
     *
     * @return The normal name.
     */
    public String getNormalName() {
        return normalName;
    }

    /**
     * Get the short name.
     *
     * @return The short name.
     */
    public String getShortName() {
        return shortName;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AlternateNames)) {
            return false;
        }
        AlternateNames names = (AlternateNames) obj;
        return normalName.equals(names.normalName) &&
                shortName.equals(names.shortName);
    }

    public int hashCode() {
        int result = normalName.hashCode();
        result = 37 * result + shortName.hashCode();
        return result;
    }

    public String toString() {
        return "(" + normalName + "," + shortName + ")";
    }
}
