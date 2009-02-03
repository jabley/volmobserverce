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
package com.volantis.devrep.repository.impl.devices.jdbc;

import com.volantis.devrep.repository.impl.devices.DefaultDeviceRepositoryConfiguration;
import com.volantis.mcs.devices.jdbc.JDBCDeviceRepositoryConfiguration;

import javax.sql.DataSource;

/**
 * A default implementation of {@link JDBCDeviceRepositoryConfiguration}.
 */
public class DefaultJDBCDeviceRepositoryConfiguration
        extends DefaultDeviceRepositoryConfiguration
        implements JDBCDeviceRepositoryConfiguration {


    /**
     * @see #getDataSource
     */
    private DataSource dataSource;

    /**
     * @see #getDefaultProject
     */
    private String defaultProject;

    /**
     * @see #useShortNames
     */
    private boolean useShortNames;

    /**
     * Name of the log file for abstract and unknown devices
     */
    private String logFileName;

    /**
     * Initialise.
     */
    public DefaultJDBCDeviceRepositoryConfiguration() {

    }

    // Javadoc inherited.
    public DataSource getDataSource() {

        return dataSource;
    }

    // Javadoc inherited.
    public void setDataSource(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    // Javadoc inherited.
    public String getDefaultProject() {

        return defaultProject;
    }

    // Javadoc inherited.
    public void setDefaultProject(String defaultProject) {

        this.defaultProject = defaultProject;
    }

    // Javadoc inherited.
    public boolean isUseShortNames() {

        return useShortNames;
    }

    // Javadoc inherited.
    public void setUseShortNames(boolean useShortNames) {

        this.useShortNames = useShortNames;
    }

    // javadoc inherited
    public String getUnknownDevicesLogFileName() {
        return logFileName;
    }

    // javadoc inherited
    public void setUnknownDevicesLogFileName(final String logFileName) {
        this.logFileName = logFileName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 ===========================================================================
*/
