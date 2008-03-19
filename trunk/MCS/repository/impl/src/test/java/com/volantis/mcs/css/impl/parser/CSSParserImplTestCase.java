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

package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.css.parser.ExtensionHandlerMock;
import com.volantis.mcs.css.parser.DiagnosticListenerMock;
import com.volantis.mcs.themes.ExtensionPriority;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.values.MarkerStyleValue;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.themes.values.TimeUnit;
import com.volantis.mcs.model.impl.SourceLocationImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.StringReader;
import java.util.Arrays;

/**
 * Test some of the low level aspects of the CSS Parser.
 */
public class CSSParserImplTestCase
    extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private ExtensionHandlerMock extensionHandlerMock;
    private static final MarkerStyleValue NOT_SET = new MarkerStyleValue("not-set");
    private CSSParserImpl parser;

    protected void setUp() throws Exception {
        super.setUp();

        extensionHandlerMock =
                new ExtensionHandlerMock("extensionHandlerMock", expectations);

        parser = createStrictCSSParser();
    }

    /**
     * Test that an unescaped identifier is processed properly.
     */
    public void testUnescapedIdentifier() throws Exception {
        doTokenizeIdentifier("IDENT", "IDENT");
    }

    private void doTokenizeIdentifier(
            final String cssString, final String javaString) {
        doTokenize(cssString, javaString, CSSParserImpl.IDENTIFIER);
    }

    private void doTokenizeString(
            final String cssString, final String javaString) {
        doTokenize(cssString, javaString, CSSParserImpl.STRING);
    }

    private void doTokenize(
            final String cssString, final String javaString, final int kind) {
        parser.ReInit(new StringReader(cssString));
        Token token = parser.getNextToken();
        assertEquals("Token Kind", kind,
                     token.kind);
        assertEquals("Token Image", javaString, token.image);
    }

    private CSSParserImpl createStrictCSSParser() {
        CSSParserImpl parser = new CSSParserImpl(
                StyleSheetFactory.getDefaultInstance(),
                new StrictDiagnosticListener(),
                extensionHandlerMock);
        return parser;
    }

    private CSSParserImpl createLaxCSSParser(DiagnosticListenerMock listener) {
        CSSParserImpl parser = new CSSParserImpl(
                StyleSheetFactory.getDefaultInstance(),
                listener,
                extensionHandlerMock);
        return parser;
    }

    /**
     * Test unicode escape containing less than 6 digits and followed by a
     * space consumes the space.
     */
    public void testUnicodeShortFollowedBySpace() throws Exception {
        doTokenizeIdentifier("\\41 ", "A");
    }

    /**
     * Test unicode escape containing less than 6 digits and followed by a
     * non hex digit or space does not consume it.
     */
    public void testUnicodeShortFollowedByNonHexNonSpace() throws Exception {
        doTokenizeIdentifier("\\41X", "AX");
    }

    /**
     * Test unicode escape containing 6 digits and followed by a
     * space consumes the space.
     */
    public void testUnicodeFullFollowedBySpace() throws Exception {
        doTokenizeIdentifier("\\000041 ", "A");
    }

    /**
     * Test that a string wrapped in single quotes is tokenized properly.
     */
    public void testSingleQuotedString() throws Exception {
        doTokenizeString("'single \\\'s \\\"'", "single \'s \"");
    }

    /**
     * Test that a string wrapped in double quotes is tokenized properly.
     */
    public void testDoubleQuotedString() throws Exception {
        doTokenizeString("\"double \\\"s \\\'\"", "double \"s \'");
    }

    /**
     * Test that within a string a \ followed by a literal newline character
     * causes both the \ and the newline character to be consumed.
     */
    public void testEscapedNewlineString() throws Exception {
        doTokenizeString("\"string \\\nsplit\"", "string split");
    }

    public void testCounterFunctionWithIdentifier() throws Exception {
        parser.parseStyleValue(StylePropertyDetails.CONTENT,
                "counter(fred)");
    }

    /**
     * Ensure that extension values are passed to a handler if it is set.
     */
    public void testExtensionValue() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        extensionHandlerMock.expects.customValue("<not-set>")
                .returns(NOT_SET).any();
        extensionHandlerMock.expects.customValue("<blah>")
                .returns(null).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        doTestExtensionValue(StylePropertyDetails.FONT_SIZE, "<not-set>");
        doTestExtensionValue(StylePropertyDetails.TEXT_ALIGN, "<not-set>");
        doTestExtensionValue(StylePropertyDetails.VERTICAL_ALIGN, "<not-set>");

        // Ensure that when an invalid value is found and there are custom
        // values that the correct error is reported.
        try {
            extensionHandlerMock.expects.getCustomValues()
                    .returns(Arrays.asList(new String[]{
                        "\"<not-set>\""
                    }));
            doTestExtensionValue(StylePropertyDetails.TEXT_ALIGN, "<blah>");
        } catch (IllegalStateException expected) {
            String message = expected.getMessage();
            String wrappedMessage = "{" + message + "}";
            assertTrue(wrappedMessage + " should contain \"<blah>\"",
                    message.indexOf("\"<blah>\"") != -1);
            assertTrue(wrappedMessage + " should contain \"<not-set>\"",
                    message.indexOf("\"<not-set>\"") != -1);
            assertTrue(wrappedMessage + " should contain <VALUE>",
                    message.indexOf("<VALUE>") != -1);
        }

        // Ensure that when an invalid value is found and there are no custom
        // values that the correct error is reported.
        try {
            extensionHandlerMock.expects.getCustomValues()
                    .returns(null);
            doTestExtensionValue(StylePropertyDetails.TEXT_ALIGN, "<blah>");
        } catch (IllegalStateException expected) {
            String message = expected.getMessage();
            String wrappedMessage = "{" + message + "}";
            assertTrue(wrappedMessage + " should contain \"<blah>\"",
                    message.indexOf("\"<blah>\"") != -1);
            assertTrue(wrappedMessage + " should contain <VALUE>",
                    message.indexOf("<VALUE>") != -1);
        }
    }

    /**
     * Test that when parsing an extension value for a property that the
     * returned value is the one returned from the extension handler.
     * @param property The property that is being tested.
     * @param css
     */
    private void doTestExtensionValue(
            final StyleProperty property, final String css) {
        final StyleValue styleValue =
                parser.parseStyleValue(property, css);

        assertSame(NOT_SET, styleValue);
    }

    /**
     * Ensure that function calls can be used as parameters.
     */
    public void testFunctionCallAsParameter() {
        StyleValue call = parser.parseStyleValue(
                StylePropertyDetails.MCS_CONTAINER,
                "mcs-container-instance('alpha', counter(alpha))");

        assertEquals(STYLE_VALUE_FACTORY.getFunctionCall(
            null, "mcs-container-instance",
            Arrays.asList(new StyleValue[] {
                STYLE_VALUE_FACTORY.getString(null, "alpha"),
                STYLE_VALUE_FACTORY.getFunctionCall(null, "counter",
                    Arrays.asList(new StyleValue[] {
                        STYLE_VALUE_FACTORY.getIdentifier(null, "alpha")
                    }))
            })), call);
    }

    /**
     * Ensure that integers are treated as such.
     */
    public void testIntegers() {
        StyleValue value = parser.parseStyleValue(
                StylePropertyDetails.MCS_MARQUEE_REPETITION, "1");
        assertEquals(value, STYLE_VALUE_FACTORY.getInteger(null, 1));
    }

    /**
     * Ensure that integers are treated as such when used as function
     * parameters.
     */
    public void testIntegerAsParameter() {
        StyleValue call = parser.parseStyleValue(
                StylePropertyDetails.MCS_CONTAINER,
                "mcs-container-instance('alpha', 1)");
        assertEquals(STYLE_VALUE_FACTORY.getFunctionCall(
            null, "mcs-container-instance",
            Arrays.asList(new StyleValue[] {
                STYLE_VALUE_FACTORY.getString(null, "alpha"),
                STYLE_VALUE_FACTORY.getInteger(null, 1)
            })), call);
    }

    /**
     * Ensure that extension priorities are passed to a handler if it is set.
     */
    public void testExtensionPriority() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        extensionHandlerMock.expects.customPriority("-medium")
                .returns(ExtensionPriority.MEDIUM).any();
        extensionHandlerMock.expects.customPriority("-blah")
                .returns(null).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        doTestExtensionPriority(
                "color: red ! important", Priority.IMPORTANT);
        doTestExtensionPriority(
                "color: red !-medium", ExtensionPriority.MEDIUM);
        doTestExtensionPriority(
                "color: red ! -medium", ExtensionPriority.MEDIUM);

        // Ensure that when an invalid priority is found and there are custom
        // priorities that the correct error is reported.
        try {
            extensionHandlerMock.expects.getCustomPriorities()
                    .returns(Arrays.asList(new String[]{
                        "\"-medium\""
                    }));

            doTestExtensionPriority("color: red !-blah", null);
        } catch (IllegalStateException expected) {
            String message = expected.getMessage();
            String wrappedMessage = "{" + message + "}";
            assertTrue(wrappedMessage + " should contain \"-blah\"",
                    message.indexOf("\"-blah\"") != -1);
            assertTrue(wrappedMessage + " should contain \"important\"",
                    message.indexOf("\"important\"") != -1);
            assertTrue(wrappedMessage + " should contain \"-medium\"",
                    message.indexOf("\"-medium\"") != -1);
        }

        // Ensure that when an invalid priority is found and there are no
        // custom priorities that the correct error is reported.
        try {
            extensionHandlerMock.expects.getCustomPriorities()
                    .returns(null);

            doTestExtensionPriority("color: red !-blah", null);
        } catch (IllegalStateException expected) {
            String message = expected.getMessage();
            String wrappedMessage = "{" + message + "}";
            assertTrue(wrappedMessage + " should contain \"-blah\"",
                    message.indexOf("\"-blah\"") != -1);
            assertTrue(wrappedMessage + " should contain \"important\"",
                    message.indexOf("\"important\"") != -1);
        }
    }

    /**
     * Test that when parsing an extension value for a property that the
     * returned value is the one returned from the extension handler.
     * @param css
     */
    private void doTestExtensionPriority(
            final String css, Priority expectedPriority) {

        final MutableStyleProperties properties =
                parser.parseDeclarations(css);

        PropertyValue value = properties.getPropertyValue(
                StylePropertyDetails.COLOR);
        Priority priority = value.getPriority();

        assertSame(expectedPriority, priority);
    }

    /**
     * Ensure that the parser can parse the attr() function correctly.
     */
    public void testParseAttrFunction()
            throws Exception {

        StyleValue value =
                parser.parseStyleValue(StylePropertyDetails.MCS_INPUT_FORMAT,
                        "attr(validate)");

        StyleValueFactory factory = STYLE_VALUE_FACTORY;
        assertEquals(
            factory.getFunctionCall(null,
                "attr",
                Arrays.asList(new StyleValue[]{
                    STYLE_VALUE_FACTORY.getIdentifier(null, "validate")
                })),
            value);
    }

    /**
     * Verify that keywords are parsed as case insensitive.
     */
    public void testKeywordsAreCaseInsensitive() {

        // Test simple property value.
        doParseProperty("display:BlOck", StylePropertyDetails.DISPLAY,
                DisplayKeywords.BLOCK);

        // Test shorthand property value.
        final String cssString = "border-right:#0f0 DOTTED Thick";
        final StyleProperty[] properties = {
                StylePropertyDetails.BORDER_RIGHT_COLOR,
                StylePropertyDetails.BORDER_RIGHT_STYLE,
                StylePropertyDetails.BORDER_RIGHT_WIDTH
        };
        final StyleValue[] expectedValues = {
            STYLE_VALUE_FACTORY.getColorByRGB(null, 255 << 8),
                BorderStyleKeywords.DOTTED,
                BorderWidthKeywords.THICK,
        };
        doParseProperty(cssString, properties, expectedValues);
    }

    /**
     * Verify that units are parsed as case insensitive.
     */
    public void testUnitsAreCaseInsensitive() {
        final SourceLocationImpl location = new SourceLocationImpl(
                "http://test.doc", 1, 1);
        final StyleAngle expectedAngle =
            STYLE_VALUE_FACTORY.getAngle(null, 200, AngleUnit.DEG);
        final StyleLength expectedLength =
            STYLE_VALUE_FACTORY.getLength(null, 200, LengthUnit.PX);
        final StyleTime expectedTime =
            STYLE_VALUE_FACTORY.getTime(null, 200, TimeUnit.MS);
        final StyleFrequency expectedFrequency =
            STYLE_VALUE_FACTORY.getFrequency(null, 200, FrequencyUnit.HZ);

        StyleValue lower = parser.processDimension(
                location, "200", "px");
        StyleValue upper = parser.processDimension(location, "200", "PX");
        StyleValue mixed = parser.processDimension(location, "200", "pX");
        assertEquals(expectedLength, lower);
        assertEquals(expectedLength, upper);
        assertEquals(expectedLength, mixed);

        lower = parser.processDimension(location, "200", "deg");
        upper = parser.processDimension(location, "200", "DEG");
        mixed = parser.processDimension(location, "200", "Deg");
        assertEquals(expectedAngle, lower);
        assertEquals(expectedAngle, upper);
        assertEquals(expectedAngle, mixed);

        lower = parser.processDimension(location, "200", "ms");
        upper = parser.processDimension(location, "200", "MS");
        mixed = parser.processDimension(location, "200", "Ms");
        assertEquals(expectedTime, lower);
        assertEquals(expectedTime, upper);
        assertEquals(expectedTime, mixed);

        lower = parser.processDimension(location, "200", "hz");
        upper = parser.processDimension(location, "200", "HZ");
        mixed = parser.processDimension(location, "200", "hZ");
        assertEquals(expectedFrequency, lower);
        assertEquals(expectedFrequency, upper);
        assertEquals(expectedFrequency, mixed);
    }

    /**
     * Verify that property names are parsed as case insensitive.
     */
    public void testPropertyNamesAreCaseInsensitive() {
        // Test simple property value.
        doParseProperty("DiSPlay:block",
                StylePropertyDetails.DISPLAY, 
                DisplayKeywords.BLOCK);

        // Test shorthand property value.
        final String cssString = "BORDER-right:#0f0 dotted thick";
        final StyleProperty[] properties = {
                StylePropertyDetails.BORDER_RIGHT_COLOR,
                StylePropertyDetails.BORDER_RIGHT_STYLE,
                StylePropertyDetails.BORDER_RIGHT_WIDTH
        };
        final StyleValue[] expectedValues = {
                STYLE_VALUE_FACTORY.getColorByRGB(null, 255 << 8),
                BorderStyleKeywords.DOTTED,
                BorderWidthKeywords.THICK,
        };
        doParseProperty(cssString, properties, expectedValues);
    }

    /**
     * Verify that the supplied CSS string is parsed into the expected values.
     *
     * @param cssString         to be parsed
     * @param properties        properties that should be populated
     * @param expectedValues    expected values of the specified properties
     */
    private void doParseProperty(String cssString,
                                 StyleProperty[] properties,
                                 StyleValue[] expectedValues) {
        MutableStyleProperties propertyValues =
                parser.parseDeclarations(cssString);
        for (int i=0; i<properties.length; i++) {
            final StyleProperty property = properties[i];
            final StyleValue expectedValue = expectedValues[i];
            final StyleValue value = propertyValues.getStyleValue(property);
            assertEquals(expectedValue, value);
        }
    }

    /**
     * Verify that the supplied CSS string is parsed into the expected value.
     *
     * @param cssString         to be parsed
     * @param property          property that should be populated
     * @param expectedValue     expected value of the specified property
     */
     private void doParseProperty(String cssString,
                                 StyleProperty property,
                                 StyleValue expectedValue) {
         MutableStyleProperties propertyValues =
                 parser.parseDeclarations(cssString);      
         final StyleValue value = propertyValues.getStyleValue(property);
         assertEquals(expectedValue, value);
     }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
