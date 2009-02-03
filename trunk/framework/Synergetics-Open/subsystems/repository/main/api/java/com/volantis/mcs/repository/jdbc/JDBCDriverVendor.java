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
 * A type safe enumeration of the JDBC drivers directly supported by MCS.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public final class JDBCDriverVendor {

    /**
     * Constant for the driver vendor to use when using the standard Oracle
     * supplied JDBC driver to access an Oracle database.
     */
    public static final JDBCDriverVendor ORACLE8 = new JDBCDriverVendor(
            "oracle");

    /**
     * Constant for the driver vendor to use when using the standard PostgreSQL
     * supplied JDBC driver to access a PostgreSQL database.
     */
    public static final JDBCDriverVendor POSTGRESQL = new JDBCDriverVendor(
            "postgres");

    /**
     * Constant for the driver vendor to use when using the MSSQLDATAD driver to
     * access an MSSQL database.
     */
    public static final JDBCDriverVendor MSSQL_DATAD = new JDBCDriverVendor(
            "mssql-datad");

    /**
     * Constant for the driver vendor to use when using the MSSQLJSQL driver to
     * access an MSSQL database.
     */
    public static final JDBCDriverVendor MSSQL_JSQL = new JDBCDriverVendor(
            "mssql-jsql");

    /**
     * Constant for the driver vendor to use when using the MSSQLMICROSOFT
     * driver to access an MSSQL database.
     */
    public static final JDBCDriverVendor MSSQL_MICROSOFT = new JDBCDriverVendor(
            "mssql-ms");

    /**
     * Constant for the driver vendor to use when using the MSSQLMICROSOFT
     * driver to access an MSSQL database.
     */
    public static final JDBCDriverVendor MSSQL_2005 = new JDBCDriverVendor(
            "mssql-2005");

    /**
     * Constant for the driver vendor to use when using the standard DB2
     * supplied JDBC driver to access a DB2 database.
     */
    public static final JDBCDriverVendor DB2 = new JDBCDriverVendor("db2");

    /**
     * Constant for the driver vendor to use when using the standard DB2 MVS
     * supplied JDBC driver to access a DB2 MVS database.
     */
    public static final JDBCDriverVendor DB2MVS = new JDBCDriverVendor(
            "db2-mvs");

    /**
     * Constant for the driver vendor to use when using the standard DB2
     * supplied JDBC Type 4 driver to access a DB2 database.
     */
    public static final JDBCDriverVendor DB2_TYPE4 =
        new JDBCDriverVendor("db2-type4");

    /**
     * Constant for the driver vendor to use when using the standard Hypersonic
     * supplied JDBC driver to access a Hypersonic database.
     */
    public static final JDBCDriverVendor HYPERSONIC = new JDBCDriverVendor(
            "hypersonic");

    /**
     * Constant for the driver vendor to use when using the standard MySQL
     * supplied JDBC driver to access a MySQL database.
     */
    public static final JDBCDriverVendor MYSQL = new JDBCDriverVendor("mysql");

    /**
     * Constant for the driver vendor to use when using the standard Apache
     * Derby supplied JDBC driver to access an Apache Derby database in server
     * mode.
     * <p> Derby also supports "embedded" mode but this has not been
     * implemented yet.</p>
     */
    public static final JDBCDriverVendor DERBY_SERVER = new JDBCDriverVendor(
            "derby-server");

    /**
     * Constant for the driver vendor to use when using the standard Sybase
     * supplied JDBC driver to access a Sybase database.
     */
    public static final JDBCDriverVendor SYBASE = new JDBCDriverVendor(
            "sybase");

    private final String name; // for debug only

    private JDBCDriverVendor(String name) {
        this.name = name;
    }

    /**
     * Get the name of the vendor.
     *
     * <p>The returned value is only supplied for debug purposes and cannot be
     * relied upon.</p>
     *
     * @return The name of the vendor.
     */
    String getName() {
        return name;
    }

    /**
     * Overridden to return the name.
     *
     * <p>The returned value is only supplied for debug purposes and cannot be
     * relied upon.</p>
     *
     * @return The name of the vendor.
     */
    public String toString() {
        return name;
    }
}
