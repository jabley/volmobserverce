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

package com.volantis.devrep.repository.impl.accessors;

import com.volantis.devrep.repository.api.devices.PolicyDescriptorAccessor;
import com.volantis.devrep.repository.impl.devices.policy.DefaultPolicyDescriptor;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.repository.RepositoryException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TestPolicyDescriptorAccessor
 implements PolicyDescriptorAccessor {

    private Map policyDescriptors = new HashMap();

    /**
     * Add a policy descriptor by name into memory.
     */
    public void addPolicyDescriptor(Object temporary, String policyName,
                                    PolicyDescriptor descriptor)
            throws RepositoryException {

        policyDescriptors.put(policyName, descriptor);
    }

    /**
     * Convenience method to add a policy by just providing a policy type.
     * <p>
     * The policy descriptor will be created automagically containing just the
     * type provided.
     *
     * @param policyName
     * @param type
     * @throws RepositoryException
     */
    public void addPolicyDescriptor(Object temporary, String policyName, PolicyType type)
            throws RepositoryException {

        DefaultPolicyDescriptor descriptor = new DefaultPolicyDescriptor();
        descriptor.setPolicyType(type);
        addPolicyDescriptor(null, policyName, descriptor);
    }

    public PolicyDescriptor getPolicyDescriptor(String policyName, Locale locale)
            throws DeviceRepositoryException {

        return (PolicyDescriptor) policyDescriptors.get(policyName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 ===========================================================================
*/
