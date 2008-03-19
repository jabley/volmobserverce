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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration.pipeline;

import com.volantis.mcs.runtime.configuration.DataSourcesConfiguration;


/**
 * This class stores the configuration for all items found within the
 * <sql-driver> xml entry in the configuration file.
 *
 */
public class SQLDriverConfiguration {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    private DataSourcesConfiguration datasourcesConfiguration;

    /**
     * Get the DataSourcesConfiguration
     * @return the DataSourcesConfiguration.
     */
    public DataSourcesConfiguration getDataSourcesConfiguration() {
        return datasourcesConfiguration;
    }

    /**
     * Set the DataSourcesConfiguration
     * @param configuration The DataSourcesConfiguration to set.
     */
    public void setDataSourcesConfiguration(DataSourcesConfiguration configuration) {
        datasourcesConfiguration = configuration;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/4	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/3	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 02-Jul-03	478/1	byron	VBM:2003061401 Unfinished issues of 2003060403

 30-Jun-03	629/1	philws	VBM:2003062508 Rename sql-connector to sql-driver

 27-Jun-03	586/1	byron	VBM:2003062704 Username and password are not used if no connection pooling in mariner-config.xml

 24-Jun-03	497/1	byron	VBM:2003062302 Issues with Database configuring and sql connector

 13-Jun-03	316/4	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/1	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
