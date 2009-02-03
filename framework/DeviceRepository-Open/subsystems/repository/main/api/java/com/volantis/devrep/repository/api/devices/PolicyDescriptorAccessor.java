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

package com.volantis.devrep.repository.api.devices;


import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.PolicyDescriptor;

import java.util.Locale;

/**
 * Provides access to {@link PolicyDescriptor} objects.
 *
 * <p>The purpose of this interface is to separate policy descriptor accessing
 * out of both {@link com.volantis.mcs.devices.DeviceRepository} and
 * {@link com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory} without
 * creating circular dependencies between them.</p>
 */
public interface PolicyDescriptorAccessor {

    /**
     * This method returns the {@link PolicyDescriptor} for the given Device
     * Policy.
     *
     * @param policyName
     *            the {@link PolicyDescriptor} for the give Device Policy.
     * @throws DeviceRepositoryException
     *             if there is a failure in accessing the underlying repository.
     */
    public PolicyDescriptor getPolicyDescriptor(String policyName, Locale locale)
    		throws DeviceRepositoryException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 ===========================================================================
*/
