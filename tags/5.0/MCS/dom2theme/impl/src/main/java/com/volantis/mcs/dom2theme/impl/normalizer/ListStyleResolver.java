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

package com.volantis.mcs.dom2theme.impl.normalizer;

import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Normalize the list-style-xxx output properties.
 *
 * <p>If an image policy either has not been specified, or it has been
 * specified but could not be resolved then the <code>list-style-image</code>
 * is set to <code>none</code>.</p>
 *
 * <p>If the image could be resolved then the <code>list-style-image</code> is
 * updated to contain a reference to the resolved policy.</p>
 */
public class ListStyleResolver
        extends AbstractResolver {

    /**
     * The list-style-image property.
     */
    private static final StyleProperty LIST_STYLE_IMAGE =
            StylePropertyDetails.LIST_STYLE_IMAGE;

    /**
     * Initialise.
     *
     * @param supportedProperties The set of supported properties.
     * @param resolver            The asset resolver.
     */
    public ListStyleResolver(
            ImmutableStylePropertySet supportedProperties,
            AssetResolver resolver) {
        super(supportedProperties, resolver);
    }


    // Javadoc inherited.
    public void normalize(MutablePropertyValues inputValues) {

        // Nothing to do if list style images are not supported.
        if (!supportedProperties.contains(LIST_STYLE_IMAGE)) {
            return;
        }

        StyleValue image = null;
        StyleValue styleValue = getSupportedStyleValue(inputValues,
                LIST_STYLE_IMAGE);
        if (styleValue != null) {
            image = resolveImage(LIST_STYLE_IMAGE, styleValue);

            inputValues.setComputedValue(LIST_STYLE_IMAGE, image);
        }
    }
}
