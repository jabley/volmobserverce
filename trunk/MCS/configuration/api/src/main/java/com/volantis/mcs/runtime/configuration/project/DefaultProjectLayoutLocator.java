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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.project;

/**
 * This class is responsible for providing the jibx binding to the default
 * layout as specified in the mcs-project.xml file
 */
public class DefaultProjectLayoutLocator {
    /**
     * the policy specifiying the location of the default layout
     */
    private String projectLayoutLocation;

    //javadoc unnecessary
    public String getDefaultProjectLocation() {
        return projectLayoutLocation;
    }

    //javadoc unnecessary
    public void setDefaultProjectLocation(String projectLayoutLocation) {
        this.projectLayoutLocation = projectLayoutLocation;
    }
}
