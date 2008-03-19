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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;

/**
 * Filters out variants based on their encoding.
 */
public abstract class EncodingFilter
        implements VariantFilter {

    public Variant filter(
            InternalDevice device, Variant variant,
            EncodingCollection requiredEncodings) {

        // Get the encoding from the variant.
        Encoding encoding = getEncoding(variant);

        // If the encoding is not supported by the device then ignore it.
        EncodingCollection supportedEncodings = getSupportedEncodings(device);
        if (!supportedEncodings.contains(encoding)) {
            variant = null;
        }

        // If the encoding is not in the required encodings then ignore it.
        if (requiredEncodings != null && !requiredEncodings.contains(encoding)) {
            variant = null;
        }

        return variant;
    }

    protected abstract EncodingCollection getSupportedEncodings(InternalDevice device);

    protected abstract Encoding getEncoding(Variant variant);
}
