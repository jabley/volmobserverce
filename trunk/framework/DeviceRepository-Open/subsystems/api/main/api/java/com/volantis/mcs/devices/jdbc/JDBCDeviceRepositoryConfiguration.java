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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.devices.jdbc;

import com.volantis.mcs.devices.DeviceRepositoryConfiguration;

import javax.sql.DataSource;

/**
 * Encapsulates the information necessary to create and configure a new
 * {@link com.volantis.mcs.devices.DeviceRepository} instance.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong></p>
 *
 * <p>This has the following properties.</p>
 * <dl>
 *     <dt id="dataSource"><b>dataSource</b></dt>
 *     <dd>The {@link javax.sql.DataSource} within which the device repository is stored.
 * </dd>
 *     <dt id="defaultProject"><b>defaultProject</b></dt>
 *     <dd>The project within the specified data source in which the device
 * repository is stored.</dd>
 *     <dt id="useShortNames"><b>useShortNames</b></dt>
 *     <dd>Determines the table and column names used to retrieve the device
 * information. If true then short names are used, otherwise more descriptive
 * names are used. The short names are all 18 characters or less.</dd>
 * </dl>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface JDBCDeviceRepositoryConfiguration
        extends DeviceRepositoryConfiguration {

    /**
     * Get the value of the <a href="#dataSource">dataSource</a> property.
     * @return The value of the <a href="#dataSource">dataSource</a> property.
     */
    public DataSource getDataSource();

    /**
     * Set the value of the <a href="#dataSource">dataSource</a> property.
     * @param dataSource The new value of the
     * <a href="#dataSource">dataSource</a> property.
     */
    public void setDataSource(DataSource dataSource);

    /**
     * Get the value of the <a href="#defaultProject">defaultProject</a>
     * property.
     * @return The value of the <a href="#defaultProject">defaultProject</a>
     * property.
     */
    public String getDefaultProject();

    /**
     * Set the value of the <a href="#defaultProject">defaultProject</a>
     * property.
     * @param defaultProject The new value of the
     * <a href="#defaultProject">defaultProject</a> property.
     */
    public void setDefaultProject(String defaultProject);

    /**
     * Get the value of the <a href="#useShortNames">useShortNames</a>
     * property.
     * @return The value of the <a href="#useShortNames">useShortNames</a>
     * property.
     */
    public boolean isUseShortNames();

    /**
     * Set the value of the <a href="#useShortNames">useShortNames</a>
     * property.
     * @param useShortNames The new value of the
     * <a href="#useShortNames">useShortNames</a> property.
     */
    public void setUseShortNames(boolean useShortNames);

    /**
     * Returns the name of the log file for abstract or unknown devices. May
     * return null.
     *
     * @return the name of the log file or null
     */
    public String getUnknownDevicesLogFileName();

    /**
     * Sets the full name of the log file for abstract or unknown devices.
     *
     * @param logFileName the name of the log file, can be null
     */
    public void setUnknownDevicesLogFileName(String logFileName);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 ===========================================================================
*/
