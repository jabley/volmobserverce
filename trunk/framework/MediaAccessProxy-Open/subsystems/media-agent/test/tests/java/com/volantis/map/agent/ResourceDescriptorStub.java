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
package com.volantis.map.agent;

import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.synergetics.descriptorstore.Parameters;
import com.volantis.synergetics.descriptorstore.ParameterNames;

import java.util.Date;

/**
 */
public class ResourceDescriptorStub implements ResourceDescriptor {

    private String externalId;
    private final String resourceType;
    private final Parameters outputParameters;
    private long timeToLive;

    public ResourceDescriptorStub(final String externalId, final String resourceType) {
        this.externalId = externalId;
        this.resourceType = resourceType;
        outputParameters = new ParametersImpl();
    }

    public String getExternalID() {
        return externalId;
    }

    public void setExternalID(String externalId) {
        this.externalId = externalId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Date getLastAccess() {
        throw new UnsupportedOperationException();
    }

    public Date getLastGenerated() {
        throw new UnsupportedOperationException();
    }

    public Parameters getInputParameters() {
        throw new UnsupportedOperationException();
    }

    public Parameters getOutputParameters() {
        return outputParameters;
    }

    public ParameterNames getRequestedParameterNames() {
        throw new UnsupportedOperationException();
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }
}
