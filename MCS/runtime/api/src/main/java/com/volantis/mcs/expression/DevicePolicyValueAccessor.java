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
package com.volantis.mcs.expression;

import com.volantis.mcs.context.DeviceAncestorRelationship;
import com.volantis.mcs.context.DeviceAncestorRelationship;
import com.volantis.shared.dependency.Dependency;


/**
 * Allows access to Device Policy Values.
 *
 * @mock.generate 
 */
public interface DevicePolicyValueAccessor {

    /**
     * Return the relations ship to the given device
     * @param deviceName the device
     * @return the relationship.
     */
    public DeviceAncestorRelationship getRelationshipTo(String deviceName);

    /**
     * Returns the name of the Device
     * @return the name of the Device
     */
    public String getDeviceName();

    /**
     * Returns the value of the given policy
     * @param policyName the name of the policy value that is to be retrieved
     * @return the value of the given policy
     */
    public String getDevicePolicyValue(String policyName);

    /**
     * Returns the value of the given policy
     *
     * <p>As a side effect it adds a {@link Dependency} on the device policy
     * value to the expression context.</p>
     *
     * @param policyName the name of the policy value that is to be retrieved
     * @return the value of the given policy
     */
    public String getDependentPolicyValue(String policyName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jul-05	9121/1	doug	VBM:2005072219 Refactored device policy xpath functions so that they can be used in homedeck

 ===========================================================================
*/
