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

import com.volantis.synergetics.descriptorstore.ParameterNames;
import com.volantis.synergetics.descriptorstore.Parameters;
import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStore;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStoreException;

import java.util.Iterator;

/**
 */
public class ResourceDescriptorStoreStub implements ResourceDescriptorStore {
    public ResourceDescriptor getDescriptor(String string) throws ResourceDescriptorStoreException {
        throw new UnsupportedOperationException();
    }

    public ResourceDescriptor createDescriptor(final String resourceType,
                                               final Parameters inputParameters,
                                               final ParameterNames outputParameterNames,
                                               final long initialTimeToLive) {
        final ResourceDescriptorStub descriptor = new ResourceDescriptorStub(
            inputParameters.getParameterValue("external-id"), resourceType);
        final Parameters outputParameters = descriptor.getOutputParameters();
        for (Iterator iter = outputParameterNames.iterator(); iter.hasNext(); ) {
            final String name = (String) iter.next();
            outputParameters.setParameterValue(name, "value-of-" + name);
        }
        outputParameters.setParameterValue(
            "an-extra-output-parameter", "value-of-the-extra-param");
        descriptor.setTimeToLive(initialTimeToLive);
        return descriptor;
    }

    public void updateDescriptor(ResourceDescriptor resourceDescriptor) {
        throw new UnsupportedOperationException();
    }

    public Parameters createParameters() {
        return new ParametersImpl();
    }

    public ParameterNames createParameterNames() {
        return new ParameterNamesImpl();
    }

    // javadoc inherited
    public void updateDescriptorTimeToLive(final String externalId,
                                           final long timeToLive)
            throws ResourceDescriptorStoreException {
        // do nothing
    }

    public void shutdown() {
    }
}
