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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.operation.impl;

import com.volantis.map.operation.ObjectParameters;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.common.param.Parameters;

import java.util.Date;

/**
 * A default implementation of the ResourceDescriptor interface.
 */
public class DefaultResourceDescriptor implements ResourceDescriptor {

    /**
     * Time to live
     */
    private long ttl = -1;

    /**
     * The current date/time
     */
    private Date now = new Date();

    /**
     * The external ID used for communication with the MAP
     */
    private String externalID = null;

    /**
     * The type of the resource (visual/audio/graph)
     */
    private String resourceType = null;

    /**
     * Input Parameters
     */
    private ObjectParameters input = new ProxiedParameters();

    /**
     * Output Parameters
     */
    private Parameters output = new ProxiedParameters();

    /**
     * Constructor
     *
     * @param externalID the external ID to use
     * @param resourceType the resource type
     */
    public DefaultResourceDescriptor(String externalID, String resourceType) {
        this.externalID = externalID;
        this.resourceType = resourceType;
    }

    // javadoc inherited
    public String getExternalID() {
        return externalID;
    }

    // javadoc inherited
    public String getResourceType() {
        return resourceType;
    }

    // javadoc inherited
    public Date getLastAccess() {
        return now;
    }

    // javadoc inherited
    public Date getLastGenerated() {
        return now;
    }

    // javadoc inherited
    public ObjectParameters getInputParameters() {
        return input;
    }

    // javadoc inherited
    public Parameters getOutputParameters() {
        return output;
    }

    // javadoc inherited
    public long getTimeToLive() {
        return ttl;
    }

    // javadoc inherited
    public void setTimeToLive(long timeToLive) {
        this.ttl = timeToLive;
    }
}
