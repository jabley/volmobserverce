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

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.dom2theme.AssetResolverMock;
import com.volantis.mcs.dom2theme.ExtractorContextMock;
import com.volantis.mcs.dom2theme.extractor.PropertyDetailsSetHelper;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.themes.ExtensionPriority;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.ShorthandSetMock;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderCollapseKeywords;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.device.DeviceStylingTestHelper;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.device.DeviceValuesMock;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

import java.util.Arrays;
import java.util.Collections;

/**
 * Test cases for {@link PropertiesOptimizer}.
 */
public class PropertiesOptimizerTestCase
        extends MockTestCaseAbstract {

    /**
     * Factory to use to create styling related objects.
     */
    private static final StylingFactory STYLING_FACTORY =
            StylingFactory.getDefaultInstance();

    private PropertyDetailsSet detailsSet;
    private ExtractorContextMock extractorContextMock;
    private AssetResolverMock assetResolverMock;
    private ShorthandSetMock shorthandSetMock;
    private PropertyValues parentProperties;
    private DeviceValuesMock deviceValuesMock;

    protected void setUp() throws Exception {
        super.setUp();

        detailsSet = PropertyDetailsSetHelper.getDetailsSet(
                Arrays.asList(new StyleProperty[]{
                    // An inherited property.
                    StylePropertyDetails.BORDER_COLLAPSE,

                    // Not an inherited property.
                    StylePropertyDetails.CAPTION_SIDE,

                    // A property upon which another property depends.
                    StylePropertyDetails.COLOR,

                    // A property whose initial value is dependent on color.
                    StylePropertyDetails.BORDER_TOP_COLOR,

                    // A property that may be normalized.
                    StylePropertyDetails.BORDER_TOP_STYLE,

                    // A property that may be normalized.
                    StylePropertyDetails.BORDER_TOP_WIDTH,

                    // A property that should always be ignored.
                    StylePropertyDetails.CONTENT,
                }));

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        extractorContextMock =
                new ExtractorContextMock("extractorContextMock", expectations);

        assetResolverMock =
                new AssetResolverMock("assetResolverMock", expectations);

        shorthandSetMock =
                new ShorthandSetMock("shorthandSetMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        extractorContextMock.expects.getAssetResolver()
                .returns(assetResolverMock).any();

        // No shorthands are supported.
        shorthandSetMock.fuzzy.contains(mockFactory.expectsAny())
                .returns(false).any();

        parentProperties = STYLING_FACTORY.createPropertyValues();

        deviceValuesMock = new DeviceValuesMock("deviceValuesMock",
                expectations);
    }

    /**
     * Ensure that this returns null if there are no significant properties.
     */
    public void testReturnsNullIfNoSignificantProperties() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        deviceValuesMock.fuzzy.getPriority(mockFactory.expectsAny())
                .returns(Priority.NORMAL).any();
        deviceValuesMock.expects
                .getStyleValue(StylePropertyDetails.BORDER_COLLAPSE)
                .returns(BorderCollapseKeywords.SEPARATE).any();
        deviceValuesMock.fuzzy.getStyleValue(mockFactory.expectsAny())
                .returns(DeviceValues.UNKNOWN).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        InputPropertiesOptimizer optimizer = new PropertiesOptimizer(detailsSet,
                extractorContextMock, shorthandSetMock);

        MutablePropertyValues values =
                STYLING_FACTORY.createPropertyValues();
        values.setComputedValue(StylePropertyDetails.BORDER_COLLAPSE,
                BorderCollapseKeywords.SEPARATE);

        MutableStyleProperties properties = optimizer.calculateOutputProperties(
                "a", PseudoStylePath.EMPTY_PATH,
                values, parentProperties, deviceValuesMock);

        System.out.println("properties: " + properties);

        assertNull(properties);
    }

    /**
     * Ensure that if the device has a rule that treats a property as medium
     * priority, i.e. higher than normal but lower than important, then the
     * property is made important.
     */
    public void testMakesHigherPriorityThanDevice()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        deviceValuesMock.expects
                .getPriority(StylePropertyDetails.BORDER_COLLAPSE)
                .returns(ExtensionPriority.MEDIUM).any();
        deviceValuesMock.fuzzy.getPriority(mockFactory.expectsAny())
                .returns(Priority.NORMAL).any();

        deviceValuesMock.fuzzy.getStyleValue(mockFactory.expectsAny())
                .returns(DeviceValues.UNKNOWN).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        InputPropertiesOptimizer optimizer = new PropertiesOptimizer(detailsSet,
                extractorContextMock, shorthandSetMock);

        MutablePropertyValues values = STYLING_FACTORY.createPropertyValues();
        values.setComputedValue(StylePropertyDetails.BORDER_COLLAPSE,
                BorderCollapseKeywords.SEPARATE);

        MutableStyleProperties properties = optimizer.calculateOutputProperties(
                "a", PseudoStylePath.EMPTY_PATH,
                values, parentProperties, deviceValuesMock);

        assertNotNull(properties);
        assertEquals("border-collapse:separate !important",
                properties.getStandardCSS());
    }

    /**
     * Ensure that if a border-top-color matches the color that it is cleared.
     */
    public void testBorderColorCleared()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        deviceValuesMock = DeviceStylingTestHelper.createDeviceValuesMock(
                mockFactory, expectations, DeviceValues.UNKNOWN);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        InputPropertiesOptimizer optimizer = new PropertiesOptimizer(detailsSet,
                extractorContextMock, shorthandSetMock);

        MutablePropertyValues values = STYLING_FACTORY.createPropertyValues();
        values.setComputedValue(StylePropertyDetails.COLOR,
                StyleColorNames.RED);
        values.setComputedValue(StylePropertyDetails.BACKGROUND_COLOR,
                StyleColorNames.RED);

        MutableStyleProperties properties = optimizer.calculateOutputProperties(
                "a", PseudoStylePath.EMPTY_PATH,
                values, parentProperties, deviceValuesMock);

        assertNotNull(properties);
        assertEquals("color:red", properties.getStandardCSS());
    }

    /**
     * Ensure that values for the content property are ignored.
     */
    public void testContentPropertyIgnored()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        deviceValuesMock = DeviceStylingTestHelper.createDeviceValuesMock(
                mockFactory, expectations, DeviceValues.UNKNOWN);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        InputPropertiesOptimizer optimizer = new PropertiesOptimizer(detailsSet,
                extractorContextMock, shorthandSetMock);

        MutablePropertyValues values = STYLING_FACTORY.createPropertyValues();
        values.setComputedValue(StylePropertyDetails.CONTENT,
            StyleValueFactory.getDefaultInstance().getList(
                Collections.singletonList(
                    StyleValueFactory.getDefaultInstance().getString(
                        null, "content"))));
        values.setComputedValue(StylePropertyDetails.COLOR,
                StyleColorNames.RED);

        MutableStyleProperties properties = optimizer.calculateOutputProperties(
                "a", PseudoStylePath.EMPTY_PATH,
                values, parentProperties, deviceValuesMock);

        assertNotNull(properties);
        assertEquals("color:red", properties.getStandardCSS());
    }

    /**
     * Ensure that properties are normalized.
     */
    public void testPropertiesNormalized()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        deviceValuesMock = DeviceStylingTestHelper.createDeviceValuesMock(
                mockFactory, expectations, DeviceValues.UNKNOWN);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        InputPropertiesOptimizer optimizer = new PropertiesOptimizer(detailsSet,
                extractorContextMock, shorthandSetMock);

        MutablePropertyValues values = STYLING_FACTORY.createPropertyValues();
        values.setComputedValue(StylePropertyDetails.BORDER_TOP_COLOR,
                StyleColorNames.RED);
        values.setComputedValue(StylePropertyDetails.BORDER_TOP_STYLE,
                BorderStyleKeywords.NONE);
        values.setComputedValue(StylePropertyDetails.BORDER_TOP_WIDTH,
                BorderWidthKeywords.THICK);

        MutableStyleProperties properties = optimizer.calculateOutputProperties(
                "a", PseudoStylePath.EMPTY_PATH,
                values, parentProperties, deviceValuesMock);

        assertNotNull(properties);
        assertEquals("border-top-style:none", properties.getStandardCSS());
    }
}
