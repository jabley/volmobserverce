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

import com.volantis.mcs.accessors.jdbc.JDBCAccessorHelper;

/**
 * Provides methods to create names for schemas.
 */
public class VolantisSchemaNamesFactory {

    /**
     * The default instance.
     */
    private static final VolantisSchemaNamesFactory DEFAULT_INSTANCE =
            new VolantisSchemaNamesFactory(
                    JDBCAccessorHelper.TABLE_NAME_PREFIX);

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static VolantisSchemaNamesFactory getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * The prefix to add before table names.
     */
    private final String tableNamePrefix;

    /**
     * Initialise.
     *
     * @param tableNamePrefix The prefix to add before table names.
     */
    public VolantisSchemaNamesFactory(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix;
    }

    /**
     * Create table names.
     *
     * @param normalName The normal name.
     * @param shortName  The short name.
     * @return The table names which have been prefixed with the table name
     *         prefix.
     */
    public AlternateNames createTableNames(
            String normalName, String shortName) {

        return new AlternateNames(tableNamePrefix + normalName,
                tableNamePrefix + shortName);
    }

    /**
     * Create table names.
     *
     * @param name The normal and short names.
     * @return The table names which have been prefixed with the table name
     *         prefix.
     */
    public AlternateNames createTableNames(String name) {
        return createTableNames(name, name);
    }

    /**
     * Create column names.
     *
     * @param name The normal and short names.
     * @return The column names.
     */
    public AlternateNames createColumnNames(String name) {
        return createColumnNames(name, name);
    }

    /**
     * Create column names.
     *
     * @param normalName The normal name.
     * @param shortName  The short name.
     * @return The column names.
     */
    public AlternateNames createColumnNames(
            String normalName, String shortName) {

        return new AlternateNames(normalName, shortName);
    }
}
