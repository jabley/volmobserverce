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

package com.volantis.mcs.policies.variants.selection;

import com.volantis.mcs.policies.PolicyFactory;

/**
 * A reference to a device.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see TargetedSelection
 * @see PolicyFactory#createDeviceReference(String) 
 * @since 3.5.1
 */
public interface DeviceReference {

    /**
     * Get the name of the device being referenced.
     *
     * @return The name of the device being referenced.
     */
    String getDeviceName();

    /**
     * Two device references are equal if the contained device names are
     * equal.
     */
    boolean equals(Object object);

    /**
     * The hash code of the device reference is the hash code of the device
     * name.
     */
    int hashCode();
}
