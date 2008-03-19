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

import javax.sql.DataSource;

/**
 * Configuration needed to create a local JDBC Repository.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="dataSource">
 * <td align="right" valign="top" width="1%"><b>data&nbsp;source</b></td>
 * <td>The underlying data source.</td>
 * </tr>
 *
 * <tr id="anonymous">
 * <td align="right" valign="top" width="1%"><b>anonymous</b></td>
 * <td>indicates whether the supplied data source is anonymous.</td>
 * </tr>
 *
 * <tr id="username">
 * <td align="right" valign="top" width="1%"><b>username</b></td>
 * <td>the name of the user needed to access the data source, may be null
 * if the data source supports anonymous access, or if the data source
 * already encapsulates the username.</td>
 * </tr>
 *
 * <tr id="password">
 * <td align="right" valign="top" width="1%"><b>password</b></td>
 * <td>the password needed to access the data source, may be null
 * if the data source supports anonymous access, or if the data source
 * already encapsulates the password.</td>
 * </tr>
 *
 * <tr id="shortNames">
 * <td align="right" valign="top" width="1%"><b>short&nbsp;names</b></td>
 * <td>indicates whether the repository should use short names (less than
 * 18 characters) for tables and columns.
 * <p> Note: Some databases (e.g. DB2 z/OS 7.1) will only work with short
 * names. In these cases this configuration is ignored.</p></td>
 * </tr>
 *
 * <tr id="releaseConnectionsImmediately">
 * <td align="right" valign="top" width="1%"><b>release&nbsp;connections&nbsp;immediately</b></td>
 * <td>if true then the SQL connection will be retrieved from the data
 * source only when it is used and returned as soon as possible. This
 * should not be set if a large number of operations on the repository are
 * being performed together as this will have a detrimental effect on
 * performance as opening new connections is very costly. If connections
 * are pooled then this performance impact will be mitigated but will not
 * be eliminated.</td>
 * </tr>
 *
 * <tr id="jdbcDriverVendor">
 * <td align="right" valign="top" width="1%"><b>JDBC&nbsp;driver&nbsp;vendor</b></td>
 * <td>the JDBC driver vendor. If this is not specified then MCS will
 * attempt to determine the vendor automatically by querying the database
 * itself.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see JDBCRepositoryFactory#createJDBCRepositoryConfiguration()
 * @see JDBCRepositoryFactory#createJDBCRepository(JDBCRepositoryConfiguration)
 * @since 3.5.1
 */
public interface JDBCRepositoryConfiguration {

    /**
     * Getter for the <a href="#dataSource">data source</a> property.
     *
     * @return Value of the <a href="#dataSource">data source</a>
     *         property.
     */
    DataSource getDataSource();

    /**
     * Setter for the <a href="#dataSource">data source</a> property.
     *
     * @param dataSource New value of the
     *                   <a href="#dataSource">data source</a> property.
     */
    void setDataSource(DataSource dataSource);

    /**
     * Getter for the <a href="#anonymous">anonymous</a> property.
     *
     * @return Value of the <a href="#anonymous">anonymous</a>
     *         property.
     */
    boolean isAnonymous();

    /**
     * Setter for the <a href="#anonymous">anonymous</a> property.
     *
     * @param anonymous New value of the
     *                  <a href="#anonymous">anonymous</a> property.
     */
    void setAnonymous(boolean anonymous);

    /**
     * Getter for the <a href="#username">username</a> property.
     *
     * @return Value of the <a href="#username">username</a>
     *         property.
     */
    String getUsername();

    /**
     * Setter for the <a href="#username">username</a> property.
     *
     * @param username New value of the
     *                 <a href="#username">username</a> property.
     */
    void setUsername(String username);

    /**
     * Getter for the <a href="#password">password</a> property.
     *
     * @return Value of the <a href="#password">password</a>
     *         property.
     */
    String getPassword();

    /**
     * Setter for the <a href="#password">password</a> property.
     *
     * @param password New value of the
     *                 <a href="#password">password</a> property.
     */
    void setPassword(String password);

    /**
     * Getter for the <a href="#shortNames">short names</a> property.
     *
     * @return Value of the <a href="#shortNames">short names</a>
     *         property.
     */
    boolean isShortNames();

    /**
     * Setter for the <a href="#shortNames">short names</a> property.
     *
     * @param shortNames New value of the
     *                   <a href="#shortNames">short names</a> property.
     */
    void setShortNames(boolean shortNames);

    /**
     * Getter for the <a href="#releaseConnectionsImmediately">release connections immediately</a> property.
     *
     * @return Value of the <a href="#releaseConnectionsImmediately">release connections immediately</a>
     *         property.
     * @deprecated Internal use only.
     */
    boolean isReleaseConnectionsImmediately();

    /**
     * Setter for the <a href="#releaseConnectionsImmediately">release connections immediately</a> property.
     *
     * @param releaseConnectionsImmediately New value of the
     *                                      <a href="#releaseConnectionsImmediately">release connections immediately</a> property.
     * @deprecated Internal use only.
     */
    void setReleaseConnectionsImmediately(
            boolean releaseConnectionsImmediately);

    /**
     * Getter for the <a href="#jdbcDriverVendor">JDBC driver vendor</a> property.
     *
     * @return Value of the <a href="#jdbcDriverVendor">JDBC driver vendor</a>
     *         property.
     */
    JDBCDriverVendor getDriverVendor();

    /**
     * Setter for the <a href="#jdbcDriverVendor">JDBC driver vendor</a> property.
     *
     * @param driverVendor New value of the
     *                     <a href="#jdbcDriverVendor">JDBC driver vendor</a> property.
     */
    void setDriverVendor(JDBCDriverVendor driverVendor);
}
