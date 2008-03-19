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

package com.volantis.mcs.devices;

import com.volantis.mcs.policies.variants.metadata.Encoding;

/**
 * Contains information about a boolean device policy that determines the
 * supported encodings.
 *
 * <p>Basically, if the device policy is true then the encoding is supported,
 * otherwise it is not.</p>
 */
public class EncodingDevicePolicy {

    /**
     * The name of the policy.
     */
    private final String policy;

    /**
     * The old integer encoding
     */
    private final int oldEncoding;

    /**
     * The new encoding.
     */
    private final Encoding newEncoding;

    public EncodingDevicePolicy(String policy, int oldEncoding, Encoding newEncoding) {
        this.policy = policy;
        this.oldEncoding = oldEncoding;
        this.newEncoding = newEncoding;
    }

    public String getPolicy() {
        return policy;
    }

    public int getOldEncoding() {
        return oldEncoding;
    }

    public Encoding getNewEncoding() {
        return newEncoding;
    }
}
