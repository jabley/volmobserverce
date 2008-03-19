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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css;

import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.css.version.DefaultCSSVersion;
import com.volantis.mcs.css.version.ManualCSS1VersionFactory;
import com.volantis.mcs.css.version.ManualCSS2VersionFactory;
import com.volantis.mcs.css.version.ManualCSSMobileVersionFactory;
import com.volantis.mcs.css.version.ManualCSSWAPVersionFactory;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.BackgroundAttachmentKeywords;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.FontFamilyKeywords;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.properties.ListStyleImageKeywords;
import com.volantis.mcs.themes.properties.ListStylePositionKeywords;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.mcs.themes.properties.MCSSystemFontKeywords;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Level;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * An integration style test for css rendering. This tests that we have
 * configured the CSSVersions correctly and that they are being rendered
 * correctly.
 * <p>
 * For now this includes all the tests that Paul wrote in TestCSSShortcuts
 * (pre CSSWAP) and tests for all the value type, keyword and shortcut
 * filtering specified for CSSWAP support (A913).
 * <p>
 * NOTE: currently border-*-color transparent and whitespace pre-* keywords
 * are not implemented in MCS CSS, despite A913 specifying that they should be
 * excluded.
 *
 * TODO: implement and test list-style-type synonyms for CSSWAP
 */
public class ManualCSSVersionTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    static StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton ();

    private DefaultCSSVersion css1;

    private DefaultCSSVersion css2;

    private DefaultCSSVersion cssMobile;

    private DefaultCSSVersion cssWap;

    private StyleSheet styleSheet;

    private Rule rule;

    public void setUp() {
        BasicConfigurator.configure();
        // Tests by default have logging turned off
        Category.getRoot().setLevel(Level.OFF);

        css1 = new ManualCSS1VersionFactory().createCSSVersion();
        css1.markImmutable();
        css2 = new ManualCSS2VersionFactory().createCSSVersion();
        css2.markImmutable();
        cssMobile = new ManualCSSMobileVersionFactory().createCSSVersion();
        cssMobile.markImmutable();
        cssWap = new ManualCSSWAPVersionFactory().createCSSVersion();
        cssWap.markImmutable();

        StyleSheetFactory styleSheetFactory =
                StyleSheetFactory.getDefaultInstance();

        // Create the styleSheet.
        styleSheet = styleSheetFactory.createStyleSheet();

        // Create the rule.
        rule = ThemeFactory.getDefaultInstance().createRule();
        styleSheet.addRule(rule);

        // Create the selector.
        StyleSheetFactory factory = StyleSheetFactory.getDefaultInstance();
        SelectorSequence sequence = factory.createSelectorSequence();
        TypeSelector t = factory.createTypeSelector();
        t.setType("p");
        sequence.addSelector(t);

        List selectors = new ArrayList();
        selectors.add(sequence);

        rule.setSelectors(selectors);

    }

    protected void tearDown() throws Exception {
        Category.shutdown();
    }

    /**
     * This test has been moved in from Paul's old TestCSSShorthands class.
     *
     * todo Move this to test the optimizers, not the renderers.
     */
    public void notestShorthands() {

        {
            // Test that the short hand is not used if only some of the
            // properties are set.

            // Create the properties.
            MutableStyleProperties properties =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();
            rule.setProperties(properties);

            // Set the line height
            properties.setStyleValue(StylePropertyDetails.LINE_HEIGHT,
                STYLE_VALUE_FACTORY.getLength(null, 12, LengthUnit.MM));

            // Set the font size.
            final StyleKeyword keywordSmall = FontSizeKeywords.SMALL;

            properties.setStyleValue(StylePropertyDetails.FONT_SIZE,
                    keywordSmall);

            // Set the font family.
            List values = new ArrayList();

            final StyleString string =
                STYLE_VALUE_FACTORY.getString(null, "Courier");
            values.add(string);

            final StyleKeyword keywordSansSerif = FontFamilyKeywords.SANS_SERIF;
            values.add(keywordSansSerif);

            final StyleList list = STYLE_VALUE_FACTORY.getList(values);

            properties.setStyleValue(StylePropertyDetails.FONT_FAMILY, list);

            // Check that the output is correct in css1.
            checkOutput(styleSheet, css1,
                    "p{font-size:small;line-height:12mm;font-family:'Courier',sans-serif}",
                    "CSS1 Font Shorthand Test 1");

            // Check that the output is correct in css2.
            checkOutput(styleSheet, css2,
                    "p{font-size:small;line-height:12mm;font-family:'Courier',sans-serif}",
                    "CSS2 Font Shorthand Test 1");

            // Check that the output is correct in cssMobile.
            checkOutput(styleSheet, cssMobile,
                    "p{font-size:small;font-family:'Courier',sans-serif}",
                    "CSS Mobile Font Shorthand Test 1");
        }

        {
            // Set the mariner system font
            MutableStyleProperties properties =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();
            rule.setProperties(properties);

            final StyleKeyword keywordMenu = MCSSystemFontKeywords.MENU;
            properties.setStyleValue(StylePropertyDetails.MCS_SYSTEM_FONT,
                    keywordMenu);

            // Check that the output is correct in css1.
            checkOutput(styleSheet, css1,
                    "",
                    "CSS1 System Font Test 1");

            // Check that the output is correct in css2.
            checkOutput(styleSheet, css2,
                    "p{font:menu}",
                    "CSS2 System Font Test 1");

            // Set the font size.
            final StyleKeyword keywordSmall = FontSizeKeywords.SMALL;

            properties.setStyleValue(StylePropertyDetails.FONT_SIZE,
                    keywordSmall);

            // Check that the output is correct in css1.
            checkOutput(styleSheet, css1,
                    "p{font-size:small}",
                    "CSS1 System Font Test 1");

            // Check that the output is correct in css2.
            checkOutput(styleSheet, css2,
                    "p{font-size:small;font:menu}",
                    "CSS2 System Font Test 1");

        }

        {
            final StyleInherit inherit = STYLE_VALUE_FACTORY.getInherit();

            // Create the properties.
            MutableStyleProperties properties =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();
            rule.setProperties(properties);

            // Set all the font properties to inherit.
            properties.setStyleValue(StylePropertyDetails.FONT_FAMILY, inherit);
            properties.setStyleValue(StylePropertyDetails.FONT_SIZE, inherit);
            properties.setStyleValue(StylePropertyDetails.FONT_SIZE_ADJUST, inherit);
            properties.setStyleValue(StylePropertyDetails.FONT_STRETCH, inherit);
            properties.setStyleValue(StylePropertyDetails.FONT_STYLE, inherit);
            properties.setStyleValue(StylePropertyDetails.FONT_VARIANT, inherit);
            properties.setStyleValue(StylePropertyDetails.FONT_WEIGHT, inherit);
            properties.setStyleValue(StylePropertyDetails.LINE_HEIGHT, inherit);

            // Check that the output is correct in css1.
            checkOutput(styleSheet, css1,
                    "",
                    "CSS1 Font Shorthand Test 2");

            // Check that the output is correct in css2.
            checkOutput(styleSheet, css2,
                    "p{font:inherit;font-size-adjust:inherit;font-stretch:inherit}",
                    "CSS2 Font Shorthand Test 2");

            // Create the properties.
            properties =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();
            rule.setProperties(properties);

            // Set the background-attachment.
            final StyleKeyword keyword = BackgroundAttachmentKeywords.SCROLL;

            properties.setStyleValue(StylePropertyDetails.BACKGROUND_ATTACHMENT,
                    keyword);

            // Check that the output is correct in css1.
            checkOutput(styleSheet, css1,
                    "p{background-attachment:scroll}",
                    "CSS1 Background Shorthand Test 1");

            // Check that the output is correct in css2.
            checkOutput(styleSheet, css2,
                    "p{background-attachment:scroll}",
                    "CSS2 Background Shorthand Test 1");
        }

        {
            // Create the properties.
            MutableStyleProperties properties =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();
            rule.setProperties(properties);

            // Set the list-style-image.
            final StyleKeyword keyword = ListStyleImageKeywords.NONE;

            properties.setStyleValue(StylePropertyDetails.LIST_STYLE_IMAGE, keyword);

            // Check that the output is correct in css1.
            checkOutput(styleSheet, css1,
                    "p{list-style-image:none}",
                    "CSS1 list-style-image Test 1");

            // Check that the output is correct in css2.
            checkOutput(styleSheet, css2,
                    "p{list-style-image:none}",
                    "CSS2 list-style-image Test 1");
        }

        {
            // Test that if all the individual properties are set to inherit that the
            // short hand is used correctly.

            MutableStyleProperties properties;
            final StyleInherit inherit = STYLE_VALUE_FACTORY.getInherit();

            // Create the properties.
            properties =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();
            rule.setProperties(properties);

            // Set all the background properties to inherit.
            properties.setStyleValue(StylePropertyDetails.BACKGROUND_ATTACHMENT,
                    inherit);
            properties.setStyleValue(StylePropertyDetails.BACKGROUND_COLOR,
                    inherit);
            properties.setStyleValue(StylePropertyDetails.BACKGROUND_IMAGE,
                    inherit);
            properties.setStyleValue(StylePropertyDetails.BACKGROUND_POSITION,
                    inherit);
            properties.setStyleValue(StylePropertyDetails.BACKGROUND_REPEAT,
                    inherit);

            // Check that the output is correct in css1.
            checkOutput(styleSheet, css1,
                    "",
                    "CSS1 Background Shorthand Test 2");

            // Check that the output is correct in css2.
            checkOutput(styleSheet, css2,
                    "p{background-attachment:inherit;background-color:inherit;background-image:inherit;background-position:inherit;background-repeat:inherit}",
                    "CSS2 Background Shorthand Test 2");
        }

        {
            // Create the properties.
            MutableStyleProperties properties =
                ThemeFactory.getDefaultInstance().createMutableStyleProperties();
            rule.setProperties(properties);

            // Set all the background properties to inherit.
            final StyleInherit inherit = STYLE_VALUE_FACTORY.getInherit();
            properties.setStyleValue(StylePropertyDetails.LIST_STYLE_IMAGE,
                    inherit);
            properties.setStyleValue(StylePropertyDetails.LIST_STYLE_TYPE,
                    inherit);
            properties.setStyleValue(StylePropertyDetails.LIST_STYLE_POSITION,
                    inherit);

            // Check that the output is correct in css1.
            checkOutput(styleSheet, css1,
                    "",
                    "CSS1 list-style Shorthand Test 2");

            // Check that the output is correct in css2.
            checkOutput(styleSheet, css2,
                    "p{list-style:inherit}",
                    "CSS2 list-style Shorthand  Test 2");
        }
    }

    /**
     * Test background-image keywords.
     */
    public void testBackgroundImageKeywords() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleKeyword keyword = BackgroundImageKeywords.NONE;
        properties.setStyleValue(StylePropertyDetails.BACKGROUND_IMAGE,
                keyword);

        // Check that the output is correct in css2.
        checkOutput(styleSheet, css2,
                "p{background-image:none}", null);

        checkOutput(styleSheet, cssWap,
                "", null);
    }

    /**
     * Test border-width shorthands.
     *
     * todo Move this to test the optimizers, not the renderers.
     */
    public void notestBorderWidthShorthands() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleKeyword keyword = BorderWidthKeywords.MEDIUM;
        properties.setStyleValue(StylePropertyDetails.BORDER_TOP_WIDTH,
                keyword);
        properties.setStyleValue(StylePropertyDetails.BORDER_LEFT_WIDTH,
                keyword);
        properties.setStyleValue(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                keyword);
        properties.setStyleValue(StylePropertyDetails.BORDER_RIGHT_WIDTH,
                keyword);

        checkOutput(styleSheet, css2,
                "p{border-width:medium}", null);

        checkOutput(styleSheet, cssWap,
                "p{border-top-width:medium;border-right-width:medium;" +
                  "border-bottom-width:medium;border-left-width:medium}", null);
    }

    /**
     * Test border-width value types.
     *
     * todo Move this to test the optimizers, not the renderers.
     */
    public void notestBorderWidthValueTypes() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleLength length =
            STYLE_VALUE_FACTORY.getLength(null, 1, LengthUnit.PX);
        properties.setStyleValue(StylePropertyDetails.BORDER_TOP_WIDTH,
                length);
        properties.setStyleValue(StylePropertyDetails.BORDER_LEFT_WIDTH,
                length);
        properties.setStyleValue(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                length);
        properties.setStyleValue(StylePropertyDetails.BORDER_RIGHT_WIDTH,
                length);

        checkOutput(styleSheet, css2,
                "p{border-width:1px}", null);

        checkOutput(styleSheet, cssWap,
                "", null);
    }

    /**
     * Test border-style shorthands.
     *
     * todo Move this to test the optimizers, not the renderers.
     */
    public void notestBorderStyleShorthands() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleKeyword keyword = BorderStyleKeywords.SOLID;
        properties.setStyleValue(StylePropertyDetails.BORDER_TOP_STYLE,
                keyword);
        properties.setStyleValue(StylePropertyDetails.BORDER_LEFT_STYLE,
                keyword);
        properties.setStyleValue(StylePropertyDetails.BORDER_BOTTOM_STYLE,
                keyword);
        properties.setStyleValue(StylePropertyDetails.BORDER_RIGHT_STYLE,
                keyword);

        checkOutput(styleSheet, css2,
                "p{border-style:solid}", null);

        checkOutput(styleSheet, cssWap,
                "p{border-top-style:solid;border-right-style:solid;" +
                  "border-bottom-style:solid;border-left-style:solid}", null);
    }

    /**
     * Test display keywords.
     */
    public void testDisplayKeywords() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleKeyword keyword = DisplayKeywords.MARKER;
        properties.setStyleValue(StylePropertyDetails.DISPLAY,
                keyword);

        checkOutput(styleSheet, css2,
                "p{display:marker}", null);

        checkOutput(styleSheet, cssWap,
                "", null);
    }

    /**
     * Test list-style shorthands.
     */
    public void testListStyleShorthands() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleURI uri = STYLE_VALUE_FACTORY.getURI(null, "URI");
        properties.setStyleValue(StylePropertyDetails.LIST_STYLE_IMAGE, uri);

        final StyleKeyword disc = ListStyleTypeKeywords.DISC;
        properties.setStyleValue(StylePropertyDetails.LIST_STYLE_TYPE,
                disc);

        final StyleKeyword inside = ListStylePositionKeywords.INSIDE;
        properties.setStyleValue(StylePropertyDetails.LIST_STYLE_POSITION,
                inside);

        checkOutput(styleSheet, css2,
                "p{list-style:url(URI) disc inside}", null);

        checkOutput(styleSheet, cssWap,
                "p{" +
                    "list-style-image:url(URI);" +
                    "list-style-type:disc;" +
                    "list-style-position:inside" +
                    "}", null);
    }


    /**
     * Test list-style-image keywords.
     */
    public void testListStyleImageKeywords() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleKeyword keyword =
                ListStyleImageKeywords.NONE;
        properties.setStyleValue(StylePropertyDetails.LIST_STYLE_IMAGE,
                keyword);

        checkOutput(styleSheet, css2,
                "p{list-style-image:none}", null);

        checkOutput(styleSheet, cssWap,
                "", null);
    }

    /**
     * Test list-style-type keywords.
     */
    public void testListStyleTypeKeywords() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleKeyword keyword = ListStyleTypeKeywords.HIRAGANA;
        properties.setStyleValue(StylePropertyDetails.LIST_STYLE_TYPE,
                keyword);

        // Check that the output is correct in css2.
        checkOutput(styleSheet, css2,
                "p{list-style-type:hiragana}", null);

        checkOutput(styleSheet, cssWap,
                "", null);
    }

    /**
     * Test text-align value types.
     */
    public void testTextAlignValueTypes() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleString string =
            STYLE_VALUE_FACTORY.getString(null, "STRING");
        properties.setStyleValue(StylePropertyDetails.TEXT_ALIGN,
                string);

        checkOutput(styleSheet, css2,
                "p{text-align:'STRING'}", null);

        checkOutput(styleSheet, cssWap,
                "", null);
    }

    /**
     * Test vertical-align value types.
     */
    public void testVerticalAlignValueTypes() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        final StyleLength length =
            STYLE_VALUE_FACTORY.getLength(null, 1, LengthUnit.PX);
        properties.setStyleValue(StylePropertyDetails.VERTICAL_ALIGN,
                length);

        checkOutput(styleSheet, css2,
                "p{vertical-align:1px}", null);

        checkOutput(styleSheet, css1,
                "", null);

        checkOutput(styleSheet, cssWap,
                "", null);
    }

    /**
     * Test vertical-align keywords.
     */
    public void testVerticalAlignKeywords() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        StyleKeyword keyword = VerticalAlignKeywords.TEXT_TOP;
        properties.setStyleValue(StylePropertyDetails.VERTICAL_ALIGN, keyword);

        checkOutput(styleSheet, css2,
                "p{vertical-align:text-top}", null);

        // todo this bit is wrong but it is as a result of the work around that Ian added into DefaultCSSStylePropertiesFilter
        checkOutput(styleSheet, cssWap,
                "p{vertical-align:text-top}", null);

        keyword = VerticalAlignKeywords.TOP;
        properties.setStyleValue(StylePropertyDetails.VERTICAL_ALIGN, keyword);
        checkOutput(styleSheet, cssMobile,
                "p{vertical-align:super}", null);
    }

    /**
     * Test margin shorthands.
     *
     * todo Move this to test the optimizers, not the renderers.
     */
    public void notestMarginShorthands() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        // NOTE: SpacingRenderer breaks unless values are separate objects.
        double length = 1;
        LengthUnit unit = LengthUnit.PX;
        final StyleLength lengthTop =
            STYLE_VALUE_FACTORY.getLength(null, length, unit);
        properties.setStyleValue(StylePropertyDetails.MARGIN_TOP,
                lengthTop);
        final StyleLength lengthLeft =
            STYLE_VALUE_FACTORY.getLength(null, length, unit);
        properties.setStyleValue(StylePropertyDetails.MARGIN_LEFT,
                lengthLeft);
        final StyleLength lengthBottom =
            STYLE_VALUE_FACTORY.getLength(null, length, unit);
        properties.setStyleValue(StylePropertyDetails.MARGIN_BOTTOM,
                lengthBottom);
        final StyleLength lengthRight =
            STYLE_VALUE_FACTORY.getLength(null, length, unit);
        properties.setStyleValue(StylePropertyDetails.MARGIN_RIGHT,
                lengthRight);

        checkOutput(styleSheet, css2,
                "p{margin:1px}", null);

        checkOutput(styleSheet, cssWap,
                "p{margin-top:1px;margin-right:1px;" +
                  "margin-bottom:1px;margin-left:1px}", null);
    }

    /**
     * Test padding shorthands.
     *
     * todo Move this to test the optimizers, not the renderers.
     */
    public void notestPaddingShorthands() {
        // Create the properties.
        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        rule.setProperties(properties);

        // NOTE: SpacingRenderer breaks unless values are separate objects.
        double length = 1;
        LengthUnit unit = LengthUnit.PX;
        final StyleLength lengthTop =
            STYLE_VALUE_FACTORY.getLength(null, length, unit);
        properties.setStyleValue(StylePropertyDetails.PADDING_TOP,
                lengthTop);
        final StyleLength lengthLeft =
            STYLE_VALUE_FACTORY.getLength(null, length, unit);
        properties.setStyleValue(StylePropertyDetails.PADDING_LEFT,
                lengthLeft);
        final StyleLength lengthBottom =
            STYLE_VALUE_FACTORY.getLength(null, length, unit);
        properties.setStyleValue(StylePropertyDetails.PADDING_BOTTOM,
                lengthBottom);
        final StyleLength lengthRight =
            STYLE_VALUE_FACTORY.getLength(null, length, unit);
        properties.setStyleValue(StylePropertyDetails.PADDING_RIGHT,
                lengthRight);

        checkOutput(styleSheet, css2,
                "p{padding:1px}", null);

        checkOutput(styleSheet, cssWap,
                "p{padding-top:1px;padding-right:1px;" +
                  "padding-bottom:1px;padding-left:1px}", null);
    }

    private static void checkOutput (StyleSheet styleSheet,
            CSSVersion cssVersion, String expectedOutput,
                                     String testName) {

      StringWriter writer = new StringWriter ();
      RendererContext context = new RendererContext (writer, renderer, null,
              cssVersion);

      try {
        renderer.renderStyleSheet (styleSheet, context);
      }
      catch (IOException ioe) {
        ioe.printStackTrace ();
      }

      String output = writer.getBuffer ().toString ();

      assertEquals(testName, expectedOutput, output);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10374/4	emma	VBM:2005111705 Interim commit

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 05-Dec-05	10581/3	pduffin	VBM:2005112407 Fixed pair rendering issue, valign="baseline" and also fixed string rendering as well

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, valign="baseline" and also fixed string rendering as well

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 02-Sep-05	9407/1	pduffin	VBM:2005083007 Committing resolved conflicts

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 06-May-05	8090/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 06-May-05	8048/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
