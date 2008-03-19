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

import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.map.operation.ObjectParameters;

import java.util.Date;

/**
 * Simply delegate to the "real" resource descriptor
 */
public class DelegatingResourceDescriptor implements
    com.volantis.map.operation.ResourceDescriptor {

    /**
     * The underlying resource descriptor we delegate to.
     */
    private final ResourceDescriptor descriptor;

    private final ObjectParameters inputParameters;

    /**
     * Construct the delegating resource descriptor
     * @param descriptor
     */
    public DelegatingResourceDescriptor(ResourceDescriptor descriptor) {
        this.descriptor = descriptor;
        this.inputParameters =
            new ProxiedParameters(descriptor.getInputParameters());
    }

    // javadoc inherited
    public ObjectParameters getInputParameters() {
        return inputParameters;
    }

    // javadoc inherited
    public String getExternalID() {
        return descriptor.getExternalID();
    }

    // javadoc inherited
    public com.volantis.map.common.param.Parameters getOutputParameters() {
        // @todo note that these parameters are incorrect. There should be
        // a delegating parameters here to enable us to write the
        // output paramters back to the resource descriptor store.
        return new ProxiedParameters(descriptor.getOutputParameters());
    }

    // javadoc inherited
    public Date getLastAccess() {
        return descriptor.getLastAccess();
    }

    // javadoc inherited
    public Date getLastGenerated() {
        return descriptor.getLastGenerated();
    }

    // javadoc inherited
    public String getResourceType() {
        return descriptor.getResourceType();
    }

    // javadoc inherited
    public long getTimeToLive() {
        return descriptor.getTimeToLive();
    }

    // javadoc inherited
    public void setTimeToLive(long l) {
        descriptor.setTimeToLive(l);
    }

}
