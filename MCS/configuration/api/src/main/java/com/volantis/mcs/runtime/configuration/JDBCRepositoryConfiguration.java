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
public class JDBCRepositoryConfiguration {
   
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";


    /**
     * Store the actual configuration as an object. 
     */
    private AnonymousDataSource configuration;

    /**
     * If set to true, will cause MCS to use short table and column names.
     */
    private Boolean useShortNames;

    /**
     * Get the actual configuration as an object.
     *
     * @return      the actual configuration as an object
     */
    public AnonymousDataSource getDataSourceConfiguration() {
        return configuration;
    }

    /**
     * Set the configuration as a JDBCDriverConfiguration object.
     *
     * @param config the configuration as a JDBCDriverConfiguration object.
     */
    public void setDataSourceConfiguration(AnonymousDataSource config) {
        configuration = config;
    }

    /**
     * @see #useShortNames
     */
    public Boolean getUseShortNames() {
        return useShortNames;
    }

    /**
     * @see #useShortNames
     */
    public void setUseShortNames(Boolean useShortNames) {
        this.useShortNames = useShortNames;
    }

    /**
     * Useful string representation of this object - used in test cases and
     * debug output (when necessary).
     */
    public String toString() {
        return super.toString() + " [config='" + configuration + "'," +
                "useShortNames=" + useShortNames + "]";
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

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
