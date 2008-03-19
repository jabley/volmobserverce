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
package com.volantis.mcs.eclipse.core;

import org.jdom.Element;

/**
 * A device policy (represented as an element) along with the name of the
 * device from which the policy originated.
 */
public class ResolvedDevicePolicy {
    /**
     * The name of the device from which the policy originated.
     */
    public final String deviceName;

    /**
     * The policy.
     */
    public final Element policy;

    /**
     * Construct a new ResolvedDevicePolicy.
     * @param deviceName The name of the device from which the policy
     * originated.
     * @param policy The policy.
     */
    public ResolvedDevicePolicy(String deviceName, Element policy) {
        this.deviceName = deviceName;
        this.policy = policy;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10539/3	adrianj	VBM:2005111712 fixed up merge conflicts

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Feb-04	2862/1	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
