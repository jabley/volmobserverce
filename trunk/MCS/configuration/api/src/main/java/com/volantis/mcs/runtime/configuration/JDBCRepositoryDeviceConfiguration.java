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
 * Holds configuration information about a device repository configuration.
 * <p>
 * This corresponds to the mcs-config/devices/{custom|standard}/jdbc-repository
 * element.
 */
public class JDBCRepositoryDeviceConfiguration extends RepositoryDeviceConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The name of the project that contains the device repository information.
     */ 
    private String project;

    /**
     * Returns the name of the project that contains the device repository 
     * information.
     */ 
    public String getProject() {
        return project;
    }
    
    /**
     * Sets the name of the project that contains the device repository 
     * information.
     */ 
    public void setProject(String project) {
        this.project = project;
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

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 03-Mar-04	3277/1	claire	VBM:2004021606 Added devices to configuration and cli options

 ===========================================================================
*/
