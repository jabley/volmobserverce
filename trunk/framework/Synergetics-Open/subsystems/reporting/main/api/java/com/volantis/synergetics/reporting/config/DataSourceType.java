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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.config;

import java.util.HashMap;

/**
 * Enum for Data source type
 */
public class DataSourceType {
    
    //javadoc unnecessary
    private static final HashMap entries = new HashMap();
    
    // javadoc unnecessary
    public static DataSourceType JDBC_DATASOURCE = 
        new DataSourceType("jdbc-datasource");
    
    //  javadoc unnecessary
    public static DataSourceType INTERNAL_POOL_DATASOURCE = 
        new DataSourceType("internal-pool-datasource");
    
    //  javadoc unnecessary
    public static DataSourceType JNDI_DATASOURCE = 
        new DataSourceType("jndi-datasource");

    /**
     * Data source type name
     */
    private final String name;
    
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the internal name for the new literal
     */
    private DataSourceType(String name) {
        this.name = name;
        
        entries.put(name, this);
    }
    
    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param name the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static DataSourceType literal(String name) {
        return (DataSourceType) entries.get(name);
    }
}
