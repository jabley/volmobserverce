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

package com.volantis.mcs.runtime.configuration;

/**
 * Provide a bean implementation of the NamedDataSource configuration.
 */
public class NamedDataSourceConfiguration {
   
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The name of the <code>DataSource</code>.
     */
    private String name;
    
    /**
     * Determine if the <code>Datasource</code> should be connected at startup.
     * This attribute is optional and as such is defaulted to false.
     */
    private Boolean connectAtStartUp = Boolean.FALSE;
    
    /**
     * Store the actual configuration as an object. This may be one of two
     * types, JDBCDriverConfiguration MCSDatabaseConfiguration.
     */
    private AnonymousDataSource configuration;

    /**
     * Return the name of the <code>DataSource</code>.
     *
     * @return      the name of the <code>DataSource</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the <code>DataSource</code>.
     *
     * @param name the name of the <code>DataSource</code>.
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the actual configuration as an object.
     *
     * @return      the actual configuration as an object
     */
    public AnonymousDataSource getDataSourceConfiguration() {
        return configuration;
    }

    /**
     * Set the configuration as a AnonymousDataSource object.
     *
     * @param config the configuration as a AnonymousDataSource object.
     */
    public void setDataSourceConfiguration(AnonymousDataSource config) {
        configuration = config;
    }
    
    /**
     * Useful string representation of this object - used in test cases and
     * debug output (when necessary).
     */
    public String toString() {
        return super.toString() + " [name='" + name + "', connectAtStartUp='" +
            connectAtStartUp + "', config='" + configuration + "']";
    }
    /**
     * @return
     */
    public Boolean getConnectAtStartUp() {
        return connectAtStartUp;
    }

    /**
     * @param connectAtStartUp
     */
    public void setConnectAtStartUp(Boolean connectAtStartUp) {
        this.connectAtStartUp = connectAtStartUp;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Apr-05	7632/1	doug	VBM:2005041110 Fixed issue with SQLDriver configuration not being read in from MCS config

 12-Apr-05	7625/1	doug	VBM:2005041110 Fixed issue with SQLDriver configuration not being read in from MCS config

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/3	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 24-Jun-03	497/2	byron	VBM:2003062302 Issues with Database configuring and sql connector

 13-Jun-03	316/4	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/1	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
