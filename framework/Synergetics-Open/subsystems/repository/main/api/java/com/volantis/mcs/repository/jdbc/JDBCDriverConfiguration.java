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

import java.util.Properties;

/**
 * Configuration needed to create a data source wrapper around a standard JDBC
 * driver.
 *
 * <p>The information provided in this class is specific to a JDBC Driver
 * implementation so you will need to consult the documentation provided with
 * your JDBC Driver provider to determine the correct values to use.</p>
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
 * <tr id="driverProperties">
 * <td align="right" valign="top" width="1%"><b>driver&nbsp;properties</b></td>
 * <td>an optional set of driver specific properties.</td>
 * </tr>
 *
 * <tr id="driverClassName">
 * <td align="right" valign="top" width="1%"><b>driver&nbsp;class&nbsp;name</b></td>
 * <td>the name of the JDBC
 * driver class.</td>
 * </tr>
 *
 * <tr id="driverSpecificDatabaseURL">
 * <td align="right" valign="top" width="1%"><b>driver&nbsp;specific&nbsp;database&nbsp;URL</b></td>
 * <td>a driver specific URL that specifies the location.</td>
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
 * @see JDBCRepositoryFactory#createJDBCDriverConfiguration()
 * @see JDBCRepositoryFactory#createJDBCDriverDataSource(JDBCDriverConfiguration)
 * @since 3.5.1
 */
public interface JDBCDriverConfiguration {

    /**
     * Getter for the <a href="#driverProperties">driver properties</a> property.
     *
     * @return Value of the <a href="#driverProperties">driver properties</a>
     *         property.
     */
    Properties getDriverProperties();

    /**
     * Setter for the <a href="#driverProperties">driver properties</a> property.
     *
     * @param driverProperties New value of the
     *                         <a href="#driverProperties">driver properties</a> property.
     */
    void setDriverProperties(Properties driverProperties);

    /**
     * Getter for the <a href="#driverClassName">driver class name</a> property.
     *
     * @return Value of the <a href="#driverClassName">driver class name</a>
     *         property.
     */
    String getDriverClassName();

    /**
     * Setter for the <a href="#driverClassName">driver class name</a> property.
     *
     * @param driverClassName New value of the
     *                        <a href="#driverClassName">driver class name</a>
     *                        property.
     */
    void setDriverClassName(String driverClassName);

    /**
     * Getter for the <a href="#driverSpecificDatabaseURL">driver specific database URL</a> property.
     *
     * @return Value of the <a href="#driverSpecificDatabaseURL">driver specific database URL</a>
     *         property.
     */
    String getDriverSpecificDatabaseURL();

    /**
     * Setter for the <a href="#driverSpecificDatabaseURL">driver specific database URL</a> property.
     *
     * @param driverSpecificDatabaseURL New value of the
     *                                  <a href="#driverSpecificDatabaseURL">driver specific database URL</a> property.
     */
    void setDriverSpecificDatabaseURL(String driverSpecificDatabaseURL);

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
