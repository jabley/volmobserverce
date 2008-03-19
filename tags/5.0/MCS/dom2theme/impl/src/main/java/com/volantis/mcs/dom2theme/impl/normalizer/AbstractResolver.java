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
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;

/**
 * Base of all those {@link PropertiesNormalizer} that perform resolving of
 * policy references.
 */
public abstract class AbstractResolver
        extends AbstractNormalizer {

    /**
     * The asset resolver.
     */
    private final AssetResolver resolver;

    /**
     * The factory to use to create style values.
     */
    private final StyleValueFactory styleValueFactory =
            StyleValueFactory.getDefaultInstance();

    /**
     * Initialise.
     *
     * @param supportedProperties The set of supported properties.
     * @param resolver            The asset resolver.
     */
    protected AbstractResolver(
            ImmutableStylePropertySet supportedProperties,
            AssetResolver resolver) {
        super(supportedProperties);

        this.resolver = resolver;
    }

    /**
     * Resolve an value as an image reference.
     *
     * @param property The property whose value is being resolved.
     * @param value    The value to resolve.
     * @return The resolved value, either {@link StyleKeywords#NONE}, or a
     *         {@link StyleURI}.
     */
    protected StyleValue resolveImage(
            StyleProperty property, StyleValue value) {

        StyleValue image = StyleKeywords.NONE;
        if (value == StyleKeywords.NONE) {
            // Nothing to do.
        } else if (value instanceof StyleURI) {
            // Nothing to do.
            image = value;
        } else if (value instanceof StyleComponentURI) {
            StyleComponentURI componentURI = (StyleComponentURI) value;
            PolicyReference reference = resolver.evaluateExpression(
                    componentURI.getExpression());
            String uri = resolver.resolveImage(reference);
            if (uri != null) {
                image = styleValueFactory.getURI(null, uri);
            }
        } else if (value instanceof StyleTranscodableURI) {
            final StyleTranscodableURI transcodableURI =
                    (StyleTranscodableURI) value;
            final String uri = resolver.resolveTranscodableImage(
                    transcodableURI.getUri());
            if (uri != null) {
                image = styleValueFactory.getURI(null, uri);
            }
        } else {
            throw new IllegalStateException("Unknown " +
                    property.getName() +
                    " value " +
                    value);
        }
        return image;
    }

    /**
     * Resolve an value as a video reference.
     *
     * @param property The property whose value is being resolved.
     * @param value    The value to resolve.
     * @return The resolved value, either {@link StyleKeywords#NONE}, or a
     *         {@link StyleURI}.
     */
    protected StyleValue resolveVideo(
            StyleProperty property, StyleValue value) {

        StyleValue image = StyleKeywords.NONE;
        if (value == StyleKeywords.NONE) {
            // There is nothing to do, use the background image component if
            // there is one.
        } else if (value instanceof StyleURI) {
            // The value is already a URI so assume that it works and use
            // it instead of the image.
            image = value;
        } else if (value instanceof StyleComponentURI) {
            // The value is a component reference that needs resolving to
            // a URI.
            StyleComponentURI componentURI = (StyleComponentURI) value;
            PolicyReference reference = resolver.evaluateExpression(
                    componentURI.getExpression());
            String uri = resolver.resolveVideo(reference);
            if (uri != null) {
                image = styleValueFactory.getURI(componentURI, uri);
            }
        } else if (value instanceof StyleTranscodableURI) {
            throw new UnsupportedOperationException(
                    "Transcodable urls are not supported for dynamic visual contents");
        } else {
            throw new IllegalStateException(
                    "Unknown " + property.getName() + " value " + value);
        }
        return image;
    }

    /**
     * Resolve an value as a text reference.
     *
     * @param property The property whose value is being resolved.
     * @param value    The value to resolve.
     * @param requiredEncodings
     * @return The resolved value, either {@link StyleKeywords#NONE}, or a
     *         {@link StyleURI}.
     */
    protected StyleValue resolveText(
            StyleProperty property, StyleValue value,
            EncodingCollection requiredEncodings) {

        StyleValue text = null;
        if (value == StyleKeywords.NONE) {
            return null;
        } else if (value instanceof StyleString) {
            // The value is already a URI so assume that it works and use
            // it instead of the image.
            text = value;
        } else if (value instanceof StyleComponentURI) {
            // The value is a component reference that needs resolving to
            // a URI.
            StyleComponentURI componentURI = (StyleComponentURI) value;
            PolicyReference reference = resolver.evaluateExpression(
                    componentURI.getExpression());
            String string = resolver.resolveText(reference, requiredEncodings);
            if (string != null) {
                text = styleValueFactory.getString(value, string);
            }
        } else {
            throw new IllegalStateException(
                    "Unknown " + property.getName() + " value " + value);
        }
        return text;
    }
}
