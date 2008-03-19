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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.sti.model;

/**
 * Job element model class.
 */
public class Job {
    
    /**
     * Job id attribute.
     */
    protected String jobID;

    /**
     * Extension data properties.
     */
    protected Properties extensionData;

    /**
     * Getter for job id attribute. 
     * 
     * @return job id.
     */
    public String getJobID() {
        return this.jobID;
    }

    /**
     * Setter for job id.
     * 
     * @param jobID
     */
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    /**
     * Getter for extension data properties. 
     * 
     * @return extension data properties.
     */
    public Properties getExtensionData() {
        return this.extensionData;
    }

    /**
     * Setter for extension data properties.
     * 
     * @param extensionData extension data properties.
     */
    public void setExtensionData(Properties extensionData) {
        this.extensionData = extensionData;
    }

}
