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

import com.volantis.mcs.dom2theme.extractor.PropertyDetailsSetHelper;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.StyleProperty;

import java.util.Arrays;

/**
 * Test cases for {@link MCSInputFormatResolver}.
 *
 * <p>{@link MCSInputFormatResolver} can handle component uri references but
 * they are not allowed to be specified on the input.</p>
 */
public class MCSInputFormatResolverTestCase
        extends PropertiesNormalizerTestAbstract {

    private ImmutableStylePropertySet supportedProperties;
    private PropertiesNormalizer resolver;
    private static final StyleProperty MCS_INPUT_FORMAT =
            StylePropertyDetails.MCS_INPUT_FORMAT;
    private EncodingCollection expectedEncodings;

    protected void setUp() throws Exception {
        super.setUp();

        final PropertyDetailsSet backgroundDetails =
                PropertyDetailsSetHelper.getDetailsSet(
                        Arrays.asList(new StyleProperty[]{
                                MCS_INPUT_FORMAT
                        }));
        supportedProperties = backgroundDetails.getSupportedProperties();

        resolver = new MCSInputFormatResolver(
                supportedProperties, assetResolverMock);

        EncodingCollectionFactory factory =
                EncodingCollectionFactory.getDefaultInstance();
        expectedEncodings =
                factory.createEncodingCollection(TextEncoding.FORM_VALIDATOR);
    }

    /**
     * Ensure that a none value is cleared.
     */
    public void testNone() throws Exception {

        inputValues.setComputedValue(MCS_INPUT_FORMAT,
                StyleKeywords.NONE);

        resolver.normalize(inputValues);

        checkNormalized(supportedProperties, "");
    }

    /**
     * Ensure that a string value is preserved.
     */
    public void testString() throws Exception {

        inputValues.setComputedValue(MCS_INPUT_FORMAT,
                STYLE_VALUE_FACTORY.getString(null, "string"));

        resolver.normalize(inputValues);

        checkNormalized(supportedProperties,
                "mcs-input-format:\"string\"");
    }

    /**
     * Ensure that a component uri value that cannot be resolved results in the
     * property being cleared.
     */
    public void testUnresolvableComponentURI() throws Exception {

        inputValues.setComputedValue(MCS_INPUT_FORMAT,
                STYLE_VALUE_FACTORY.getComponentURI(null, "fred"));

        assetResolverMock.expects.evaluateExpression(fred)
                .returns(policyReferenceMock);

        assetResolverMock.expects
                .resolveText(policyReferenceMock, expectedEncodings)
                .returns(null);

        resolver.normalize(inputValues);

        checkNormalized(supportedProperties, "");
    }

    /**
     * Ensure that a component uri value that can be resolved results in the
     * property being set to the resolved value.
     */
    public void testResolvableComponentURI() throws Exception {

        inputValues.setComputedValue(MCS_INPUT_FORMAT,
                STYLE_VALUE_FACTORY.getComponentURI(null, "fred"));

        assetResolverMock.expects.evaluateExpression(fred)
                .returns(policyReferenceMock);

        assetResolverMock.expects
                .resolveText(policyReferenceMock, expectedEncodings)
                .returns("text");

        resolver.normalize(inputValues);

        checkNormalized(supportedProperties, "mcs-input-format:\"text\"");
    }
}
