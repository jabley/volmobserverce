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

package com.volantis.mcs.devices;

import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;

/**
 * This interface represents a user perspective of a device and can be used to
 * access policy values associated withthat device.
 * 
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface Device {

    /**
     * Get the name of the device.
     * 
     * @return The name of the device
     */
    public String getName();

    /**
     * Return the value(s) of the specified policy for the current device as a
     * string.
     * <p>
     * If the policy is a composite policy then this returns a string containing
     * all the values of the policy separated by a comma and a single white
     * space character.
     * </p>
     *
     * @param policyName
     *            The name of the policy whose value is required
     * @return The value of the specified policy. If no value exists null will
     *         be returned.
     * @deprecated You should use getRealPolicyValue(policyName).getAsString()
     * instead.
     */
    public String getPolicyValue(String policyName);

    /**
     * Return the value(s) of the specified policy for the current device as a
     * {@link PolicyValue}.
     *
     * @param policyName
     *            The name of the policy whose value is required
     * @return The value of the specified policy. If no value exists null will
     *         be returned.
     *
     * @deprecated Use {@link #getPolicyMetaDataValue} instead.
     */
    public PolicyValue getRealPolicyValue(String policyName);

    /**
     * Return the value(s) of the specified policy for the current device as a
     * {@link ImmutableMetaDataValue}.
     *
     * @param policyName
     *            The name of the policy whose value is required
     * @return The value of the specified policy. If no value exists null will
     *         be returned.
     */
    public ImmutableMetaDataValue getPolicyMetaDataValue(String policyName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 08-Oct-04	5755/1	geoff	VBM:2004092209 NullPointerException thrown when Accessing DeviceRepository API

 28-Jul-04	4940/1	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 28-Jul-04	4952/3	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/
