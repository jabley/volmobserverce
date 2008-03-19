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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Configuration for a policy cache partition.
 */
public class PolicyCachePartitionConfiguration {

    /**
     * The size of the cache, could be optional but at the moment it is
     * required in JIBX and schema.
     */
    Integer size;

    /**
     * The configuration of the cache control constraints that apply across
     * all policies within this partition.
     */
    RemotePolicyCacheConfiguration constraints;

    /**
     * A list of {@link PolicyTypePartitionConfiguration}, one for each policy
     * type that has its own sub partition configured.
     */
    List typeSpecificPartitions;

    /**
     * A dynamically created map from {@link PolicyType} to
     * {@link PolicyTypePartitionConfiguration}
     */
    private Map type2Partition;

    /**
     * Get the partition size.
     *
     * @return The partition size.
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Get the constraints configuration.
     *
     * @return The constraints configuration.
     */
    public RemotePolicyCacheConfiguration getConstraintsConfiguration() {
        return constraints;
    }

    /**
     * Get the number of type specific configurations.
     *
     * @return The number of type specific configurations.
     */
    public int getTypeSpecificPartitionCount() {
        return typeSpecificPartitions.size();
    }

    /**
     * Get the configuration for the specified policy type.
     *
     * @param policyType The policy type.
     * @return The configuration, may be null.
     */
    public PolicyTypePartitionConfiguration getTypePartition(
            PolicyType policyType) {
        Map map = getTypeSpecificPartitions();
        return (PolicyTypePartitionConfiguration) map.get(policyType);
    }

    /**
     * Get a map of {@link PolicyType} to
     * {@link PolicyTypePartitionConfiguration}.
     *
     * @return A map.
     */
    private Map getTypeSpecificPartitions() {
        if (type2Partition == null) {
            type2Partition = new HashMap();
            for (Iterator i = typeSpecificPartitions.iterator(); i.hasNext();) {
                PolicyTypePartitionConfiguration typePartition =
                        (PolicyTypePartitionConfiguration) i.next();
                List types = typePartition.getPolicyTypes();
                for (int j = 0; j < types.size(); j++) {
                    PolicyType policyType = (PolicyType) types.get(j);
                    type2Partition.put(policyType, typePartition);
                }
            }
        }

        return type2Partition;
    }
}
