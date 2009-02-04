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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom2theme.impl.normalizer;

import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Resolve any component references within the mcs-input-format property.
 */
public class MCSInputFormatResolver
        extends AbstractResolver {
    
    private static final EncodingCollection REQUIRED_ENCODINGS;
    static {
        EncodingCollectionFactory factory =
                EncodingCollectionFactory.getDefaultInstance();
        REQUIRED_ENCODINGS =
                factory.createEncodingCollection(TextEncoding.FORM_VALIDATOR);
    }

    private static final StyleProperty MCS_INPUT_FORMAT =
            StylePropertyDetails.MCS_INPUT_FORMAT;

    public MCSInputFormatResolver(
            ImmutableStylePropertySet supportedProperties,
            AssetResolver resolver) {
        super(supportedProperties, resolver);
    }

    public void normalize(MutablePropertyValues inputValues) {
        StyleValue value = getSupportedStyleValue(inputValues,
                MCS_INPUT_FORMAT);
        if (value != null) {
            StyleValue resolved = resolveText(MCS_INPUT_FORMAT, value, 
                    REQUIRED_ENCODINGS);
            if (resolved != value) {
                inputValues.setComputedValue(MCS_INPUT_FORMAT, resolved);
            }
        }
    }
}
