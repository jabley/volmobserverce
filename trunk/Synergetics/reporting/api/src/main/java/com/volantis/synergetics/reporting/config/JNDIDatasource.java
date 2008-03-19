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

/**
 * JNDI configuration parameters
 *
 */
public class JNDIDatasource implements DatasourceConfiguration {

    /**
     * datasource name
     */
    private String name;

    /**
     * JNDI name
     */
    private String jndiName;

    // javadoc unnecessary
    public void setName(String name) {
        this.name = name;
    }

    // javadoc unnecessary
    public String getName() {
        return name;
    }

    // javadoc unnecessary
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    // javadoc unnecessary
    public String getJndiName() {
        return jndiName;
    }

    // javadoc inherited
    public ConnectionStrategy createConnectionStrategy() throws ConfigurationException {
        return new JNDIConnectionStrategy(this);
    }

    // javadoc inherited
    public DataSourceType getType() {
        return DataSourceType.JNDI_DATASOURCE;
    }
}
