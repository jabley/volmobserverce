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

package com.volantis.styling.integration.compiler;

import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.font.UnknownFontValue;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.properties.FontWeightKeywords;
import com.volantis.mcs.themes.properties.MCSSystemFontKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.impl.compiler.ValueCompilerMock;
import com.volantis.styling.impl.engine.PropertySplitter;
import com.volantis.styling.impl.engine.PropertySplitterImpl;
import com.volantis.styling.impl.engine.Prioritised;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class PropertySplitterTestCase
        extends TestCaseAbstract {
    private ValueCompilerMock valueCompilerMock;

    protected void setUp() throws Exception {
        super.setUp();

        valueCompilerMock = new ValueCompilerMock("valueCompilerMock",
                expectations);

    }

    public void testSplitter() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final StyleProperty normalProperty = StylePropertyDetails.COLOR;

        final StyleValue normalValue = StyleColorNames.RED;

        final StyleProperty importantProperty = StylePropertyDetails.BACKGROUND_IMAGE;

        final StyleValue importantValue =
            StyleValueFactory.getDefaultInstance().getLength(
                null, 10, LengthUnit.CM);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        PropertyValue normalPropertyValue =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                normalProperty, normalValue, Priority.NORMAL);

        PropertyValue importantPropertyValue =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                importantProperty, importantValue, Priority.IMPORTANT);

        valueCompilerMock.expects.compile(normalValue)
                .returns(normalValue);
        valueCompilerMock.expects.compile(importantValue)
                .returns(importantValue);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        properties.setPropertyValue(normalPropertyValue);
        properties.setPropertyValue(importantPropertyValue);

        doSplitterTest(properties, new PropertyValue[]{importantPropertyValue},
                new PropertyValue[]{normalPropertyValue});

    }

    /**
     * Do the splitter test.
     *
     * @param properties        The properties to split.
     * @param expectedImportant The expected important properties.
     * @param expectedNormal    The expected normal properties.
     */
    private void doSplitterTest(
            MutableStyleProperties properties,
            final PropertyValue[] expectedImportant,
            final PropertyValue[] expectedNormal) {
        PropertySplitter splitter = new PropertySplitterImpl(valueCompilerMock);
        Prioritised[] prioritised = splitter.split(properties);

        Map map = new HashMap();
        map.put(Priority.NORMAL, expectedNormal);
        map.put(Priority.IMPORTANT, expectedImportant);

        for (int i = 0; i < prioritised.length; i++) {
            Prioritised p = prioritised[i];
            Priority priority = p.getPriority();
            PropertyValue[] values = p.getValues();

            PropertyValue[] expected = (PropertyValue[]) map.get(priority);
            assertEquals(priority + " value mismatch", expected, values);
        }
    }

    /**
     * Compare two arrays.
     *
     * @param message  The message to display if the assetion failed.
     * @param expected The expected array.
     * @param actual   The actual array.
     */
    public void assertEquals(String message,
                                     Object[] expected, Object[] actual) {
        assertEquals(message, Arrays.asList(expected),
                Arrays.asList(actual));
    }

    /**
     * Ensure that splitting a set of properties that contain a system font
     * sets the other properties.
     */
    public void testSystemFontNormal() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        valueCompilerMock.fuzzy.compile(mockFactory.expectsAny())
                .does(new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        return event.getArgument(StyleValue.class);
                    }
                }).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertyValue fontSize =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                StylePropertyDetails.FONT_SIZE, FontSizeKeywords.LARGER,
                Priority.NORMAL);

        PropertyValue systemFont =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                StylePropertyDetails.MCS_SYSTEM_FONT,
                MCSSystemFontKeywords.CAPTION, Priority.NORMAL);

        PropertyValue fontWeight =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                StylePropertyDetails.FONT_WEIGHT,
                FontWeightKeywords.BOLD, Priority.IMPORTANT);

        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        properties.setPropertyValue(systemFont);
        properties.setPropertyValue(fontSize);
        properties.setPropertyValue(fontWeight);

        doSplitterTest(properties,
                new PropertyValue[]{
                    fontWeight,
                },
                new PropertyValue[]{
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.FONT_FAMILY,
                        UnknownFontValue.INSTANCE),
                    fontSize,
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.FONT_STYLE,
                            UnknownFontValue.INSTANCE),
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.FONT_VARIANT,
                            UnknownFontValue.INSTANCE),
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.LINE_HEIGHT,
                            UnknownFontValue.INSTANCE),
                    systemFont,
                });
    }

    /**
     * Ensure that splitting a set of properties that contain a system font
     * sets the other properties.
     */
    public void testSystemFontImportant() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        valueCompilerMock.fuzzy.compile(mockFactory.expectsAny())
                .does(new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        return event.getArgument(StyleValue.class);
                    }
                }).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertyValue fontSize =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                StylePropertyDetails.FONT_SIZE, FontSizeKeywords.LARGER,
                Priority.NORMAL);

        PropertyValue systemFont =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                StylePropertyDetails.MCS_SYSTEM_FONT,
                MCSSystemFontKeywords.CAPTION, Priority.IMPORTANT);

        PropertyValue fontWeight =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                StylePropertyDetails.FONT_WEIGHT,
                FontWeightKeywords.BOLD, Priority.IMPORTANT);

        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        properties.setPropertyValue(systemFont);
        properties.setPropertyValue(fontSize);
        properties.setPropertyValue(fontWeight);

        doSplitterTest(properties,
                new PropertyValue[]{
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.FONT_FAMILY,
                            UnknownFontValue.INSTANCE, Priority.IMPORTANT),
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.FONT_SIZE,
                            UnknownFontValue.INSTANCE, Priority.IMPORTANT),
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.FONT_STYLE,
                            UnknownFontValue.INSTANCE, Priority.IMPORTANT),
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.FONT_VARIANT,
                            UnknownFontValue.INSTANCE, Priority.IMPORTANT),
                    fontWeight,
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        StylePropertyDetails.LINE_HEIGHT,
                            UnknownFontValue.INSTANCE, Priority.IMPORTANT),
                    systemFont,
                },
                new PropertyValue[]{});
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/7	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/5	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Stopped copying style values in order to change whether they were explicitly specified or not

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
