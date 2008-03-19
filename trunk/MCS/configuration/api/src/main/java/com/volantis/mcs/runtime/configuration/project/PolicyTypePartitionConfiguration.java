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

package com.volantis.mcs.runtime.configuration.project;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;

import java.util.List;

/**
 * Configuration for a policy type specific
 */
public class PolicyTypePartitionConfiguration {

    /**
     * The policy type for which this applies.
     */
    List policyTypes;

    /**
     * The size of the partition.
     */
    int size;

    /**
     * The constraints for policies within this partition.
     */
    RemotePolicyCacheConfiguration constraints;

    /**
     * Get the policy types.
     *
     * @return The policy types as a list of {@link PolicyType}.
     */
    public List getPolicyTypes() {
        return policyTypes;
    }

    /**
     * Get the constraints.
     *
     * @return The constraints.
     */
    public RemotePolicyCacheConfiguration getConstraints() {
        return constraints;
    }

    /**
     * Get the size.
     *
     * @return The size.
     */
    public int getSize() {
        return size;
    }
}
