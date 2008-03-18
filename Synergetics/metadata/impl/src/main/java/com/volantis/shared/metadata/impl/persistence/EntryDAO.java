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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.impl.persistence;

import com.volantis.synergetics.path.Path;

/**
 * This class represents a record in the database. All InhibitorImpl instances
 * are mapped to one or more of these records
 */
public class EntryDAO {

    /**
     * The path of the record (primarily used for lists/sets/convienience
     */
    private String path;

    /**
     * The string representation of the class. This is obtained using a lookup
     * in the MetadataClassMapper class. (It avoids storing the actual class
     * name in the DB)
     */
    private String clazz;

    /**
     * True if this entry represents a null Inhibitor
     */
    private boolean isNull;

    /**
     * Initialize the EntryDAO
     *
     * @param path the path
     * @param clazz the string representation of the class
     * @param isNull true if the entry represents a null object
     */
    public EntryDAO(String path, String clazz, boolean isNull) {
        setPath(path);
        setClassName(clazz);
        setIsNull(isNull);
    }

    /**
     * Return the String representation of the class name
     *
     * @see MetadataClassMapper
     * @return the string representation of the class name
     */
    public String getClassName() {
        return clazz;
    }

    /**
     * Set the string representation of the class name
     * 
     * @see MetadataClassMapper
     * @param clazz the string representation of the class name. Must not be
     * null
     */
    public void setClassName(String clazz) {
        if (clazz == null) {
            throw new IllegalStateException("argument must not be null");
        }
        this.clazz = clazz;
    }

    /**
     * Return true if the entry represents a null value
     *
     * @return true if the entry represents a null value
     */
    public boolean isNull() {
        return isNull;
    }

    /**
     * Sets the entry to represent a null Inhibitor/value
     *
     * @param isNull true if the entry represents a null Inhibitor/value
     */
    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }

    /**
     * Returns the name/value of this entry. This is the last fragment of the
     * path.
     *
     * @return the name/value of the entry
     */
    public String getName() {
        return Path.parse(getPath()).getName();
    }

    /**
     * Returns the path
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the path
     *
     * @param path the path to set (must not be null)
     */
    public void setPath(String path) {
        if (path == null) {
            throw new IllegalStateException("argument must not be null");
        }
        this.path = path;
    }

    // Javadoc inherited
    public boolean equals(Object obj) {
        boolean equals = this == obj;
        if (!equals && obj != null && getClass() == obj.getClass()) {
            EntryDAO that = (EntryDAO) obj;
            equals = getPath().equals(that.getPath()) &&
                getClassName().equals(that.getClassName()) &&
                isNull() == that.isNull();
        }
        return equals;
    }

    // Javadoc inherited
    public int hashCode() {
        int result = 37;
        result = 29 * result + getPath().hashCode();
        result = 29 * result + getClassName().hashCode();
        result = 29 * result + (isNull() ? 124125: 5235235);
        return result;
    }
}
