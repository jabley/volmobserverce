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

package com.volantis.styling.integration;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetSource;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.StringReader;
import java.util.Iterator;

/**
 * Integration test for styling.
 */
public class StylingTestCase
        extends TestCaseAbstract {
    
    private static final String NS = XDIMESchemata.CDM_NAMESPACE;

    /**
     * Perform a simple integration test.
     */
    public void testAllSorts() {

        String counterIdentifier1 = "testCounter";
        String counterIdentifier2 = "testCounterTwo";

        String css =
                "a {color: green; counter-increment: testCounter}\n" +
                "b {font-weight: normal; counter-increment: testCounterTwo}" +
                "* {color: blue}" +
                "p {color: red; font-weight: bold; counter-increment: testCounter}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        // <p>
        engine.startElement(NS, "p", attributes);
        assertStylesEqual(StylesBuilder.getSparseStyles(
                "color: red; " +
                "font-weight: bold; " +
                "counter-increment: testCounter"),
                engine);

        assertEquals("Counter testCounter mismatch", 1,
                engine.getCounterValue(counterIdentifier1));
        assertEquals("Counter testCounter mismatch", 0,
                engine.getCounterValue(counterIdentifier2));

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: green; " +
                    "font-weight: bold; " +
                    "counter-increment: testCounter"),
                    engine);

            assertEquals("Counter testCounter mismatch", 2,
                    engine.getCounterValue(counterIdentifier1));
            assertEquals("Counter testCounter mismatch", 0,
                    engine.getCounterValue(counterIdentifier2));

            // </a>
            engine.endElement(NS, "a");
        }

        {
            // <b>
            engine.startElement(NS, "b", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue; " +
                    "font-weight: normal; " +
                    "counter-increment: testCounterTwo"),
                    engine);

            assertEquals("Counter testCounter mismatch", 2,
                    engine.getCounterValue(counterIdentifier1));
            assertEquals("Counter testCounter mismatch", 1,
                    engine.getCounterValue(counterIdentifier2));

            // </b>
            engine.endElement(NS, "b");
        }

        // </p>
        engine.endElement(NS, "p");
    }

    private Attributes createAttributes() {
        Attributes attributes = new Attributes() {
            public String getAttributeValue(String namespace, String localName) {
                return null;
            }
        };
        return attributes;
    }

    private StylingEngine createStylingEngine(String css) {
        CSSParser parser =
                CSSParserFactory.getDefaultInstance().createStrictParser();
        StyleSheet styleSheet = parser.parseStyleSheet(new StringReader(css),
                null);

        StylingFactory factory = StylingFactory.getDefaultInstance();

        CompilerConfiguration configuration = factory.createCompilerConfiguration();
        configuration.setSource(StyleSheetSource.THEME);

        StyleSheetCompiler compiler = factory.createStyleSheetCompiler(configuration);
        CompiledStyleSheet sheet = compiler.compileStyleSheet(styleSheet);

        StylingEngine engine = factory.createStylingEngine();
        engine.pushStyleSheet(sheet);
        return engine;
    }

    public void testChildSelector() {

        String css =
                "a {color: blue}\n" +
                "a > * {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: yellow"),
                        engine);

                // </b>
                engine.endElement(NS, "b");
            }

            // </a>
            engine.endElement(NS, "a");
        }
    }

    public void testDescendantSelector() {

        String css =
                "a {color: blue}\n" +
                "a * {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: yellow"),
                        engine);

                {
                    // <c>
                    engine.startElement(NS, "c", attributes);
                    assertStylesEqual(StylesBuilder.getSparseStyles(
                            "color: yellow"),
                            engine);

                    // </c>
                    engine.endElement(NS, "c");
                }

                // </b>
                engine.endElement(NS, "b");
            }

            // </a>
            engine.endElement(NS, "a");
        }
    }

    public void testNestedDescendantSelector() {

        String css =
                "a {color: blue}\n" +
                "a * {color: yellow}\n" +
                "b * {color: green}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: yellow"),
                        engine);

                {
                    // <c>
                    engine.startElement(NS, "c", attributes);
                    assertStylesEqual(StylesBuilder.getSparseStyles(
                            "color: green"),
                            engine);

                    {
                        // <d>
                        engine.startElement(NS, "d", attributes);
                        assertStylesEqual(StylesBuilder.getSparseStyles(
                                "color: green"),
                                engine);

                        // </d>
                        engine.endElement(NS, "d");
                    }

                    // </c>
                    engine.endElement(NS, "c");

                    // <c>
                    engine.startElement(NS, "c", attributes);
                    assertStylesEqual(StylesBuilder.getSparseStyles(
                            "color: green"),
                            engine);

                    // </c>
                    engine.endElement(NS, "c");
                }

                // </b>
                engine.endElement(NS, "b");
            }

            // </a>
            engine.endElement(NS, "a");
        }
    }

    public void testPrecedingSiblingSelector() {

        String css =
                "a {color: blue}\n" +
                "b ~ * {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: blue"),
                        engine);

                // </b>
                engine.endElement(NS, "b");


                // <c>
                engine.startElement(NS, "c", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: yellow"),
                        engine);

                // </c>
                engine.endElement(NS, "c");

                // <c>
                engine.startElement(NS, "c", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: yellow"),
                        engine);

                // </c>
                engine.endElement(NS, "c");
            }

            // </a>
            engine.endElement(NS, "a");
        }

    }

    public void testImmediatePrecedingSiblingSelector() {
        String css =
                "a {color: blue}\n" +
                "b + * {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: blue"),
                        engine);

                // </b>
                engine.endElement(NS, "b");


                // <c>
                engine.startElement(NS, "c", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: yellow"),
                        engine);

                // </c>
                engine.endElement(NS, "c");

                // <c>
                engine.startElement(NS, "c", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: blue"),
                        engine);

                // </c>
                engine.endElement(NS, "c");
            }

            // </a>
            engine.endElement(NS, "a");
        }
    }

    public void testNestedImmediatePrecedingSiblingSelector() {

        String css =
                "a {color: blue}\n" +
                "b + * + * {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: blue"),
                        engine);

                // </b>
                engine.endElement(NS, "b");


                // <c>
                engine.startElement(NS, "c", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: blue"),
                        engine);

                // </c>
                engine.endElement(NS, "c");

                // <d>
                engine.startElement(NS, "d", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: yellow"),
                        engine);

                // </d>
                engine.endElement(NS, "d");
            }

            // </a>
            engine.endElement(NS, "a");
        }

    }

    public void testNestedUniversalChildSelectors() {

        String css =
                "a {color: blue}\n" +
                "a > * {color: red}\n" +
                "a > * > * {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();
        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: red"),
                        engine);

                {
                    // <c>
                    engine.startElement(NS, "c", attributes);
                    assertStylesEqual(StylesBuilder.getSparseStyles(
                            "color: yellow"),
                            engine);

                    // </c>
                    engine.endElement(NS, "c");
                }

                // </b>
                engine.endElement(NS, "b");
            }

            // </a>
            engine.endElement(NS, "a");
        }
    }

    public void testNestedTypeChildSelectors() {

        String css =
                "a {color: blue}\n" +
                "a > b {color: red}\n" +
                "a > b > c {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: red"),
                        engine);

                {
                    // <c>
                    engine.startElement(NS, "c", attributes);
                    assertStylesEqual(StylesBuilder.getSparseStyles(
                            "color: yellow"),
                            engine);

                    // </c>
                    engine.endElement(NS, "c");
                }

                // </b>
                engine.endElement(NS, "b");

                // <c>
                engine.startElement(NS, "c", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: blue"),
                        engine);

                // </c>
                engine.endElement(NS, "c");
            }

            // </a>
            engine.endElement(NS, "a");
        }
    }

    public void testChildWithinDescendantSelectors() {

        String css =
                "a {color: blue}\n" +
                "a c > d {color: green}\n" +
                "a > b {color: red}\n" +
                "a > b > c {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: red"),
                        engine);

                {
                    // <c>
                    engine.startElement(NS, "c", attributes);
                    assertStylesEqual(StylesBuilder.getSparseStyles(
                            "color: yellow"),
                            engine);

                    {
                        // <d>
                        engine.startElement(NS, "d", attributes);
                        assertStylesEqual(StylesBuilder.getSparseStyles(
                                "color: green"),
                                engine);

                        // </d>
                        engine.endElement(NS, "d");
                    }

                    // </c>
                    engine.endElement(NS, "c");
                }

                // </b>
                engine.endElement(NS, "b");

                // <c>
                engine.startElement(NS, "c", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: blue"),
                        engine);

                // </c>
                engine.endElement(NS, "c");
            }

            // </a>
            engine.endElement(NS, "a");
        }
    }

    public void testDescendantWithinChildSelector() {

        String css =
                "a {color: blue}\n" +
                "a > b {color: red}\n" +
                "a > b d {color: green}\n" +
                "a > b > c {color: yellow}";

        StylingEngine engine = createStylingEngine(css);
        Attributes attributes = createAttributes();

        {
            // <a>
            engine.startElement(NS, "a", attributes);
            assertStylesEqual(StylesBuilder.getSparseStyles(
                    "color: blue"),
                    engine);

            {
                // <b>
                engine.startElement(NS, "b", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: red"),
                        engine);

                {
                    // <c>
                    engine.startElement(NS, "c", attributes);
                    assertStylesEqual(StylesBuilder.getSparseStyles(
                            "color: yellow"),
                            engine);

                    {
                        // <d>
                        engine.startElement(NS, "d", attributes);
                        assertStylesEqual(StylesBuilder.getSparseStyles(
                                "color: green"),
                                engine);

                        // </d>
                        engine.endElement(NS, "d");
                    }

                    // </c>
                    engine.endElement(NS, "c");

                    // <d>
                    engine.startElement(NS, "d", attributes);
                    assertStylesEqual(StylesBuilder.getSparseStyles(
                            "color: green"),
                            engine);

                    // </d>
                    engine.endElement(NS, "d");
                }

                // </b>
                engine.endElement(NS, "b");

                // <c>
                engine.startElement(NS, "c", attributes);
                assertStylesEqual(StylesBuilder.getSparseStyles(
                        "color: blue"),
                        engine);

                // </c>
                engine.endElement(NS, "c");
            }

            // </a>
            engine.endElement(NS, "a");
        }
    }

    private void assertStylesEqual(Styles sparseStyles, StylingEngine engine) {
        PropertyValues expectedValues = sparseStyles.getPropertyValues();
        PropertyValues actualValues = engine.getStyles().getPropertyValues();
        Iterator iterator = expectedValues.stylePropertyIterator();
        while (iterator.hasNext()) {
            StyleProperty property = (StyleProperty) iterator.next();
            StyleValue specified = expectedValues.getSpecifiedValue(property);
            if (specified != null) {
                // Only interested in the specified values.
                StyleValue expected = expectedValues.getComputedValue(property);
                StyleValue actual = actualValues.getComputedValue(property);
                assertEquals(property.getName(), expected, actual);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/3	emma	VBM:2005111705 Interim commit

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/3	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10613/6	pduffin	VBM:2005112205 Porting forward changes from MCS 3.5

 06-Dec-05	10583/3	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 29-Nov-05	10465/1	geoff	VBM:2005112205 MCS35: Themes not overridng layout properties as expected at runtime

 29-Sep-05      9654/1  pduffin VBM:2005092817 Added support for expressions and functions in styles
 06-Dec-05	10613/2	pduffin	VBM:2005112205 Committing changes

 29-Nov-05	10465/1	geoff	VBM:2005112205 MCS35: Themes not overridng layout properties as expected at runtime

 29-Sep-05      9654/1  pduffin VBM:2005092817 Added support for expressions and functions in styles
 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/3	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 28-Sep-05      9487/6  pduffin VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 28-Sep-05      9487/4  pduffin VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05      9487/1  pduffin VBM:2005091203 Committing new CSS Parser

 22-Sep-05      9578/1  adrianj VBM:2005092102 Integrate counters into styling engine

 05-Sep-05      9407/3  pduffin VBM:2005083007 Removed old themes model

 31-Aug-05      9407/1  pduffin VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 22-Aug-05      9298/1  geoff   VBM:2005080402 Style portlets and inclusions correctly.

 18-Jul-05      9029/4  pduffin VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05      9067/3  geoff   VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 12-Jul-05      9011/1  pduffin VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 20-Jun-05      8483/1  emma    VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05      7997/1  pduffin VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
