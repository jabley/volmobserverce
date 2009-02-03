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
 * Configuration needed to create a data source wrapper around an MCS
 * supported JDBC driver.
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
 * <tr id="jdbcDriverVendor">
 * <td align="right" valign="top" width="1%"><b>JDBC&nbsp;driver&nbsp;vendor</b></td>
 * <td>the JDBC driver vendor.</td>
 * </tr>
 *
 * <tr id="host">
 * <td align="right" valign="top" width="1%"><b>host</b></td>
 * <td>the host name of the database server. If this is not explicitly set then
 * the {@link #getHost()} method will fail.</td>
 * </tr>
 *
 * <tr id="port">
 * <td align="right" valign="top" width="1%"><b>port</b></td>
 * <td>the port on which the database server is listening. If this is not
 * explicitly set then the {@link #getPort()} method will fail.</td>
 * </tr>
 *
 * <tr id="source">
 * <td align="right" valign="top" width="1%"><b>source</b></td>
 * <td>the database source, this is a database
 * vendor specific value that identifies an instance of a database schema
 * within the database server, i.e. a set of populated tables.</td>
 * </tr>
 *
 * <tr id="connectionPoolConfiguration">
 * <td align="right" valign="top" width="1%"><b>connection&nbsp;pool&nbsp;configuration</b></td>
 * <td>optional configuration for an MCS connection pool.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see JDBCRepositoryFactory#createMCSDriverConfiguration()
 * @see JDBCRepositoryFactory#createMCSDriverDataSource(MCSDriverConfiguration)
 * @since 3.5.1
 */
public interface MCSDriverConfiguration {

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

    /**
     * Getter for the <a href="#host">host</a> property.
     *
     * @return Value of the <a href="#host">host</a>
     *         property.
     */
    String getHost();

    /**
     * Setter for the <a href="#host">host</a> property.
     *
     * @param host New value of the
     *             <a href="#host">host</a> property.
     */
    void setHost(String host);

    /**
     * Getter for the <a href="#port">port</a> property.
     *
     * @return Value of the <a href="#port">port</a>
     *         property.
     */
    int getPort();

    /**
     * Setter for the <a href="#port">port</a> property.
     *
     * @param port New value of the
     *             <a href="#port">port</a> property.
     */
    void setPort(int port);

    /**
     * Getter for the <a href="#source">source</a> property.
     *
     * @return Value of the <a href="#source">source</a>
     *         property.
     */
    String getSource();

    /**
     * Setter for the <a href="#source">source</a> property.
     *
     * @param source New value of the
     *               <a href="#source">source</a> property.
     */
    void setSource(String source);

    /**
     * Getter for the <a href="#connectionPoolConfiguration">connection pool configuration</a> property.
     *
     * @return Value of the <a href="#connectionPoolConfiguration">connection pool configuration</a>
     *         property.
     */
    MCSConnectionPoolConfiguration getConnectionPoolConfiguration();

    /**
     * Setter for the <a href="#connectionPoolConfiguration">connection pool configuration</a> property.
     *
     * @param connectionPoolConfiguration New value of the
     *                                    <a href="#connectionPoolConfiguration">connection pool configuration</a> property.
     */
    void setConnectionPoolConfiguration(
            MCSConnectionPoolConfiguration connectionPoolConfiguration);
}
