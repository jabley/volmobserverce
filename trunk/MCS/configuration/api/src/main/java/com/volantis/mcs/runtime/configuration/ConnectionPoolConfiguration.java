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

package com.volantis.mcs.runtime.configuration;

/**
 * Provide a bean implementation of the ConnectionPool configuration.
 */
public class ConnectionPoolConfiguration implements AnonymousDataSource {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2004. ";

    /**
     * 
     */
    private Integer maximum; 

    private Boolean keepAlive;
    
    private Integer pollInterval;
    
    /**
     * Store the actual configuration as an object. This may be one of two
     * types, JDBCDriverConfiguration MCSDatabaseConfiguration.
     */
    private AnonymousDataSource configuration;

    /**
     * Get the actual configuration as an object.
     *
     * @return      the actual configuration as an object
     */
    public AnonymousDataSource getDataSourceConfiguration() {
        return configuration;
    }


    /**
     * Set the configuration as a AnonymouseDataSourceConfiguration object.
     *
     * @param config the configuration as a AnonymousDataSourceConfiguration object.
     */
    public void setDataSourceConfiguration(AnonymousDataSource config) {       
        configuration = config;
    }

    /**
     * Get the maximum number of pooled connections
     * @return the maximum number of pooled connections
     */
    public Integer getMaximum() {
        return maximum;
    }

    /**
     * Get the polling interval
     * @return The polling interval
     */
    public Integer getPollInterval() {
        return pollInterval;
    }

    /**
     * Set the maximum number of pooled connections
     * @param maximum The maximum number of pooled connections
     */
    public void setMaximum(Integer maximum) {
        this.maximum = maximum;         
    }

    /**
     * Set the polling interval
     * @param pollInterval The polling interval
     */
    public void setPollInterval(Integer  pollInterval) {
        this.pollInterval = pollInterval;
    }

    /**
     * Get the keep alive status for the connection pool
     * @return The keep alive status
     */
    public Boolean getKeepAlive() {
        return keepAlive;
    }

    /**
     * Set the keep alive status for the connection pool
     * @param keepAlive The keep alive status
     */
    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 16-Mar-04	2867/3	ianw	VBM:2004012603 Fixed some rework issues with datasource rationalisation

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
