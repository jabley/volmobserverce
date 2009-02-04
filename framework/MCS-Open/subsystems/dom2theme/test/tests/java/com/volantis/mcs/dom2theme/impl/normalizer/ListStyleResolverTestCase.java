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

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.StylesBuilder;

/**
 * Tests for {@link ListStyleResolver}.
 */
public class ListStyleResolverTestCase
        extends PropertiesNormalizerTestAbstract {

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

        // There is a background-image.
        inputValues.setComputedValue(
                StylePropertyDetails.LIST_STYLE_IMAGE,
                StylesBuilder.getStyleValue(
                        StylePropertyDetails.LIST_STYLE_IMAGE,
                        "mcs-component-url('fred')"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new ListStyleResolver(ALL_PROPERTIES, assetResolverMock);
        normalizer.normalize(inputValues);

        checkNormalized("list-style-image:url(/fred.gif)");
    }

    /**
     * Test that when normalizing properties containing an
     * unresolved mcs-background-dynamic-visual that it is cleared and the
     * background-image is set to none.
     */
    public void testNormalizeUnresolvedImage() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        assetResolverMock.expects.evaluateExpression(fred)
                .returns(policyReferenceMock).atLeast(1);
        assetResolverMock.expects.resolveImage(policyReferenceMock)
                .returns(null).atLeast(1);

        inputValues.setComputedValue(
                StylePropertyDetails.LIST_STYLE_IMAGE,
                StylesBuilder.getStyleValue(
                        StylePropertyDetails.LIST_STYLE_IMAGE,
                        "mcs-component-url('fred')"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new ListStyleResolver(ALL_PROPERTIES, assetResolverMock);
        normalizer.normalize(inputValues);
        checkNormalized("list-style-image:none");
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

        // There is a background-image.
        inputValues.setComputedValue(
            StylePropertyDetails.LIST_STYLE_IMAGE,
            StylesBuilder.getStyleValue(
                StylePropertyDetails.LIST_STYLE_IMAGE,
                "mcs-transcodable-url('http://localhost:8080/image.gif')"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new ListStyleResolver(ALL_PROPERTIES, assetResolverMock);
        normalizer.normalize(inputValues);
        checkNormalized(ALL_PROPERTIES, "list-style-image:url(" +
            "http://localhost:8080/cj24/image.gif?v.width=750)");
    }
}
