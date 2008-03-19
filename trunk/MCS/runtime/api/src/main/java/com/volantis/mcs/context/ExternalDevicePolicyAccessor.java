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

package com.volantis.mcs.context;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;

/**
 * Provides external access to device policies.
 *
 * <p>Specifically this hides policies whose name starts with x-</p>
 */
public class ExternalDevicePolicyAccessor
        implements DevicePolicyAccessor {

    /**
     * The page context.
     */
    private MarinerPageContext context;

    /**
     * Initialise.
     *
     * @param context The page context.
     */
    public ExternalDevicePolicyAccessor(MarinerPageContext context) {
        this.context = context;
    }

    // javadoc inherited
    public DeviceAncestorRelationship
            getRelationshipTo(String deviceName) {
        return DeviceAncestorRelationship.get(
                context.getAncestorRelationship(deviceName));
    }

    // javadoc inherited
    public String getDeviceName() {
        return context.getDeviceName();
    }

    // javadoc inherited
    public String getDevicePolicyValue(String policyName) {
        if (isPolicyNameAccessible(policyName)) {
            return context.getDevicePolicyValue(policyName);
        } else {
            return null;
        }
    }

    /**
     * Checks to see whether a policy name should be accessible at runtime by
     * customer content.
     *
     * @param policyName The name of the policy being checked
     * @return True if the policy is accessible at runtime
     */
    private boolean isPolicyNameAccessible(String policyName) {
        boolean isAccessible = true;
        if (policyName == null) {
            isAccessible = false;
        } else if (policyName.startsWith(
                DevicePolicyConstants.EXPERIMENTAL_POLICY_PREFIX)) {
            isAccessible = false;
        }
        return isAccessible;
    }
}
