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

import com.volantis.mcs.dom2theme.extractor.PropertyDetailsSetHelper;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.PropertyDetailsSet;

import java.util.Arrays;

/**
 * Tests for {@link BackgroundNormalizer}.
 */
public class BackgroundResolverTestCase
        extends PropertiesNormalizerTestAbstract {

    private ImmutableStylePropertySet backgroundProperties;

    protected void setUp() throws Exception {
        super.setUp();

        final PropertyDetailsSet backgroundDetails =
            PropertyDetailsSetHelper.getDetailsSet(
                Arrays.asList(PropertyGroups.BACKGROUND_PROPERTIES));
        backgroundProperties = backgroundDetails.getSupportedProperties();
    }

    /**
     * Test that when normalizing properties containing a
     * resolvable mcs-background-dynamic-visual that it is cleared and the
     * background-image is set to the resolved URI.
     */
    public void testNormalizeResolvableDynamicVisual() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        assetResolverMock.expects.evaluateExpression(fred)
                .returns(policyReferenceMock).atLeast(1);
        assetResolverMock.expects.resolveVideo(policyReferenceMock)
                .returns("/fred.real").atLeast(1);

        inputValues.setComputedValue(
                StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL,
                StylesBuilder.getStyleValue(
                        StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL,
                        "mcs-component-url('fred')"));

        inputValues.setComputedValue(
                StylePropertyDetails.BACKGROUND_IMAGE,
                StylesBuilder.getStyleValue(
                        StylePropertyDetails.BACKGROUND_IMAGE,
                        "url('/fred.real')"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BackgroundResolver(backgroundProperties, assetResolverMock);
        normalizer.normalize(inputValues);

        checkNormalized(backgroundProperties,
                "background-image:url(/fred.real)");
    }

    /**
     * Test that when normalizing properties containing an
     * unresolved mcs-background-dynamic-visual that it is cleared and the
     * background-image is set to none.
     */
    public void testNormalizeUnresolvedDynamicVisual() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        assetResolverMock.expects.evaluateExpression(fred)
                .returns(policyReferenceMock).atLeast(1);
        assetResolverMock.expects.resolveVideo(policyReferenceMock)
                .returns(null).atLeast(1);

        inputValues.setComputedValue(
                StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL,
                StylesBuilder.getStyleValue(
                        StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL,
                        "mcs-component-url('fred')"));

        inputValues.setComputedValue(
                StylePropertyDetails.BACKGROUND_IMAGE, null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BackgroundResolver(backgroundProperties, assetResolverMock);
        normalizer.normalize(inputValues);

        // The background-image should be set to none and the
        // mcs-background-dynamic-visual property should be cleared.
        checkNormalized(backgroundProperties, "background-image:none");
    }

    /**
     * Test that when normalizing properties containing a resolvable
     * background-image that it is set to the resolved URI.
     */
    public void testNormalizeResolvableImage() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        assetResolverMock.expects.evaluateExpression(fred)
                .returns(policyReferenceMock).atLeast(1);
        assetResolverMock.expects.resolveImage(policyReferenceMock)
                .returns("/fred.gif").atLeast(1);

        // There is no mcs-background-dynamic-visual.
        inputValues.setComputedValue(
                StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL, null);

        // There is a background-image.
        inputValues.setComputedValue(
                StylePropertyDetails.BACKGROUND_IMAGE,
                StylesBuilder.getStyleValue(
                        StylePropertyDetails.BACKGROUND_IMAGE,
                        "mcs-component-url('fred')"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BackgroundResolver(backgroundProperties, assetResolverMock);
        normalizer.normalize(inputValues);
        checkNormalized(backgroundProperties,
                "background-image:url(/fred.gif)");
    }

    /**
     * Ensure that when background-image is none that the property does not
     * change.
     */
    public void testNormalizeNoImage() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // There is no background-image.
        inputValues.setComputedValue(StylePropertyDetails.BACKGROUND_IMAGE,
                BackgroundImageKeywords.NONE);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BackgroundResolver(backgroundProperties, assetResolverMock);
        normalizer.normalize(inputValues);

        checkNormalized(backgroundProperties, "background-image:none");
    }

    /**
     * Ensure that when neither is specified that no properties are changed.
     */
    public void testNormalizeNeither() {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BackgroundResolver(backgroundProperties, assetResolverMock);
        normalizer.normalize(inputValues);

        checkNormalized(backgroundProperties, "");
    }

    public void testTranscodableURL() {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        assetResolverMock.expects.resolveTranscodableImage(
            "http://localhost:8080/image.gif").returns(
            "http://localhost:8080/cj24/image.gif?v.width=750");

        // There is no mcs-background-dynamic-visual.
        inputValues.setComputedValue(
                StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL, null);

        // There is a background-image.
        inputValues.setComputedValue(
            StylePropertyDetails.BACKGROUND_IMAGE,
            StylesBuilder.getStyleValue(
                StylePropertyDetails.BACKGROUND_IMAGE,
                "mcs-transcodable-url('http://localhost:8080/image.gif')"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BackgroundResolver(backgroundProperties, assetResolverMock);
        normalizer.normalize(inputValues);
        checkNormalized(backgroundProperties, "background-image:url(" +
            "http://localhost:8080/cj24/image.gif?v.width=750)");
    }
}
