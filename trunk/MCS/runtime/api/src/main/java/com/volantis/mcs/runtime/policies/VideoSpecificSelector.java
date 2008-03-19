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
import com.volantis.mcs.policies.variants.video.VideoEncoding;

import java.util.Iterator;

/**
 * Given a selection of video variants and the target device select the best
 * video from the selection for that device.
 */
public class VideoSpecificSelector
        implements VariablePolicyTypeSpecificSelector {

    public Variant selectVariant(
            SelectionContext context, ActivatedVariablePolicy variablePolicy) {

        InternalDevice device = context.getDevice();

        EncodingCollection supportedEncodings = device.getSupportedVideoEncodings();
        Variant variant;

        // Try TV first if it is supported.
        if (supportedEncodings.contains(VideoEncoding.TV)) {
            variant = variablePolicy.getVariantWithEncoding(VideoEncoding.TV);
            if (variant != null) {
                return variant;
            }
        }

        // Now try all the other encodings.
        Iterator i = supportedEncodings.iterator();
        while (i.hasNext()) {
            Encoding encoding = (Encoding) i.next();

            // Ignore TV as it has either already been checked or is not
            // supported.
            if (encoding == VideoEncoding.TV) {
                continue;
            }

            variant = variablePolicy.getVariantWithEncoding(encoding);
            if (variant != null) {
                return variant;
            }
        }

        return null;
    }
}
