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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom2theme.impl.normalizer;

import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Try and resolve the mcs-background-dynamic-visual and background-image into
 * a single background-image value.
 *
 * <p>The mcs-background-dynamic-visual overrides the background-image</p>
 */
public class BackgroundResolver
        extends AbstractResolver {

    /**
     * The background-image property.
     */
    private static final StyleProperty BACKGROUND_IMAGE =
            StylePropertyDetails.BACKGROUND_IMAGE;

    /**
     * Initialise.
     *
     * @param supportedProperties The set of supported properties.
     * @param resolver            The asset resolver.
     */
    public BackgroundResolver(
            ImmutableStylePropertySet supportedProperties,
            AssetResolver resolver) {
        super(supportedProperties, resolver);
    }

    // Javadoc inherited.
    public void normalize(MutablePropertyValues inputValues) {

        // Nothing to so if background images are not supported.
        if (!supportedProperties.contains(BACKGROUND_IMAGE)) {
            return;
        }

        // Get the value of the background dynamic visual.
        StyleValue inputVideoValue = inputValues.getStyleValue(
                StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL);
        StyleValue inputImageValue = getSupportedStyleValue(
                inputValues, BACKGROUND_IMAGE);

        StyleValue resolvedImage = null;
        if (inputVideoValue != null) {
            resolvedImage = resolveVideo(
                    StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL,
                    inputVideoValue);
        }

        // If the background-image has not been overridden by the background
        // dynamic visual then try and resolve it too.
        if (resolvedImage == null ||
                resolvedImage == BackgroundImageKeywords.NONE) {
            if (inputImageValue != null) {
                resolvedImage = resolveImage(BACKGROUND_IMAGE, inputImageValue);
            }
        }

        // Set the property if the image has not changed.
        if (resolvedImage != null && !resolvedImage.equals(inputImageValue)) {
            inputValues.setComputedValue(BACKGROUND_IMAGE, resolvedImage);
        }
    }
}
