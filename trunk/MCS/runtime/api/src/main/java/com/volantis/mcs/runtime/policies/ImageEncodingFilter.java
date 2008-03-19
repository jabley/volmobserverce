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

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;

public class ImageEncodingFilter
        extends EncodingFilter {

    public Variant filter(
            InternalDevice device, Variant variant,
            EncodingCollection requiredEncodings) {
        ImageMetaData metaData = (ImageMetaData) variant.getMetaData();

        // Convertible image variants can only be used if a transcoding rule
        // can be determined for this device.
        ImageConversionMode conversionMode = metaData.getConversionMode();
        if (conversionMode == ImageConversionMode.ALWAYS_CONVERT) {
            // Check the transcoding rule.
            if (MarinerPageContext.getTranscodingRule(device) == null) {
                variant = null;
            }
        } else {
            variant = super.filter(device, variant, requiredEncodings);
        }

        return variant;
    }

    protected EncodingCollection getSupportedEncodings(InternalDevice device) {
        return device.getSupportedImageEncodings();
    }

    protected Encoding getEncoding(Variant variant) {
        ImageMetaData metaData = (ImageMetaData) variant.getMetaData();
        return metaData.getImageEncoding();
    }
}
