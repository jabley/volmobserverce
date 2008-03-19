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

import java.util.HashMap;
import java.util.Map;

/**
 * Provide a bean implementation of the JDBC configuration.
 */
public class JDBCDriverConfiguration implements AnonymousDataSource {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Optional. Specify the name of the JDBC driver class to load.
     * <p>
     * If this attribute is set then it is loaded using Class.forName. If an
     * error occurs while trying to load it then the error is logged and the
     * data source is ignored.
     * <p>
     * If this attribute is not specified then it is assumed that the driver
     * class has been loaded in some other way, e.g. by adding it to the list
     * of drivers in the jdbc.drivers system property.
     */
    private String driverClass;

    /**
     * The driver specific URL that identifies the database to access. This is
     * the same as the URL passed to the DriverManager.getConnection ()
     * methods.
     */
    private String databaseURL;

    /**
     * A map of parameters associated with the jdbcDriver datasource.
     */
    private Map parameters = new HashMap();


    /**
     * Return the driver specific URL that identifies the database to access.
     *
     * @return      the driver specific URL that identifies the database to
     *              access.
     */
    public String getDatabaseURL() {
        return databaseURL;
    }

    /**
     * Set the driver specific URL that identifies the database to access
     *
     * @param databaseURL driver specific URL that identifies the database to
     *                    access
     */
    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    /**
     * Return the name of the JDBC driver class to load.
     *
     * @return      the the name of the JDBC driver class to load.
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * Set the name of the JDBC driver class to load.
     *
     * @param driverClass the name of the JDBC driver class to load.
     */
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * Get the map of parameters.
     *
     * @return      the map of parameters.
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * Add a parameter to the map. Note that the paramters are stored in a map
     * where the name is the key and the value the actual value.
     *
     * @param config the ParameterConfiguration object storing the name and
     *               value for a parameter.
     */
    public void addParameter(ParameterConfiguration config) {
        parameters.put(config.getName(), config.getValue());
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

 09-Mar-04	2867/2	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 13-Jun-03	316/7	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/5	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
