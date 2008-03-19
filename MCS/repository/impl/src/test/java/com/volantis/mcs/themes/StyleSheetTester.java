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
package com.volantis.mcs.themes;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import junit.framework.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

/**
 * A utility class for integration tests which need to test style sheets.
 * <p>
 * In particular, it provides the capability to translate style sheets from and
 * to CSS style sheet (string) values for easy of testing.
 *
 * todo: avoid static methods.
 */
public class StyleSheetTester {

    static StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();

    private final static CSSParserFactory CSS_PARSER_FACTORY =
            CSSParserFactory.getDefaultInstance();

    /**
     * Check that the "expected" theme supplied (in the form of a CSS style
     * sheet) equals equals the "actual" theme supplied.
     *
     * @param testName The test we are checking on behalf of.
     * @param expectedAsCss the expected theme as CSS to check.
     * @param actual the actual theme to check.
     */
    public static void assertStyleSheetEquals(String testName,
            String expectedAsCss, StyleSheet actual) throws IOException {

        // parse the expected CSS to canonicalise it.
        StyleSheet expectedStyleSheet =
                null;
        try {
            CSSParser parser = CSS_PARSER_FACTORY.createStrictParser();
            expectedStyleSheet =
                parser.parseStyleSheet(new StringReader(expectedAsCss), null);
        } catch (IllegalArgumentException e) {
            Assert.fail("Error occurred during parsing: " + expectedAsCss +
                ". Error: " + e.getMessage());
        }
        // do the comparison.
        assertStyleSheetEquals(testName, expectedStyleSheet, actual);
    }

    /**
     * Check that the "expected" theme supplied equals equals the "actual"
     * theme supplied.
     *
     * @param testName The test we are checking on behalf of.
     * @param expected the expected theme to check.
     * @param actual the actual theme to check.
     */
    public static void assertStyleSheetEquals(String testName,
            StyleSheet expected, StyleSheet actual)
            throws IOException {

        // Render the themes to CSS.
        String renderedExpected = renderStyleSheet(expected);
        String renderedActual = renderStyleSheet(actual);
        // Compare as strings.
//        System.out.println("Expected styleSheet: \n" + renderedExpected);
//        System.out.println("Actual styleSheet: \n" + renderedActual);
        Assert.assertEquals(testName, renderedExpected, renderedActual);
    }

    /**
     * Render a device theme into a CSS style sheet.
     *
     * @param theme the device theme to render.
     * @return the CSS style sheet as a string.
     * @throws IOException
     */
    public static String renderStyleSheet(StyleSheet theme) throws IOException {

        StringWriter writer = new StringWriter();
        RendererContext context = new RendererContext(writer, renderer);
        renderer.renderStyleSheet(theme, context);
        String styleSheet = writer.getBuffer().toString();
        return styleSheet;
    }

    public static String renderSelectors(List selectors) throws IOException {

        StringWriter writer = new StringWriter();
        RendererContext context = new RendererContext(writer, renderer);
        renderer.renderStyleSelectors(selectors, context);
        context.flushStyleSheet();
        String styleSheet = writer.getBuffer().toString();
        return styleSheet;
    }

    /**
     * Parse a set of CSS values into a StyleProperties.
     *
     * @param cssValues the CSS values to parse. This will be of the form
     *      "background-color: gray; color:purple" for example.
     * @return the style properties containing all the css values supplied.
     */
    public static StyleProperties parseProperties(String cssValues) {

        // Parse the declarations.
        StyleProperties properties = null;
        try {
            CSSParser parser = CSS_PARSER_FACTORY.createStrictParser();
            properties = parser.parseDeclarations(cssValues);
        } catch (IllegalArgumentException e) {
            Assert.fail("Error occurred during parsing: " + cssValues +
                ". Error: " + e.getMessage());
        }

        return properties;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 18-Aug-05	9331/2	gkoch	VBM:2005081603 vbm2005081603: refactoring to convert InputStreams into Readers

 16-Aug-05	9287/3	gkoch	VBM:2005080509 vbm2005080509 applied review comments

 10-Aug-05	9187/1	tom	VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file]

 09-Aug-05	9195/1	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 01-Aug-05	9114/3	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 19-Jul-05	8668/14	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/10	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
