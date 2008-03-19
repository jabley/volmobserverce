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

import com.volantis.mcs.dom2theme.AssetResolverMock;
import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.mcs.expression.PolicyExpressionFactory;
import com.volantis.mcs.policies.PolicyReferenceMock;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleValuesMock;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class PropertiesNormalizerTestAbstract
        extends TestCaseAbstract {

    protected static final ImmutableStylePropertySet ALL_PROPERTIES =
            StylePropertyDetails.getDefinitions()
            .getStandardDetailsSet()
            .getSupportedProperties();

    protected static final StylingFactory STYLING_FACTORY =
            StylingFactory.getDefaultInstance();

    protected static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    protected AssetResolverMock assetResolverMock;
    protected PolicyReferenceMock policyReferenceMock;
    protected PolicyExpression fred;
    protected MutablePropertyValues inputValues;
    protected StyleValuesMock parentValuesMock;

    protected void setUp() throws Exception {
        super.setUp();

        assetResolverMock = new AssetResolverMock("assetResolverMock",
                expectations);

        policyReferenceMock = new PolicyReferenceMock("policyReferenceMock",
                expectations);

        fred =
            PolicyExpressionFactory.getDefaultInstance().createExpression("fred");

        inputValues = STYLING_FACTORY.createPropertyValues();

        parentValuesMock = new StyleValuesMock("styleValuesMock", expectations);
    }

    protected void checkNormalized(final String expectedCSS) {
        assertEquals(expectedCSS, inputValues.getStandardCSS());
    }

    protected void checkNormalized(
            ImmutableStylePropertySet supportedProperties, String expectedCSS) {

        final MutablePropertyValues filteredValues =
                STYLING_FACTORY.createPropertyValues();
        supportedProperties.iterateStyleProperties(new StylePropertyIteratee() {
            public IterationAction next(StyleProperty property) {
                filteredValues.setComputedValue(property,
                        inputValues.getStyleValue(property));
                return IterationAction.CONTINUE;
            }
        });

        assertEquals(expectedCSS, filteredValues.getStandardCSS());
    }
}
