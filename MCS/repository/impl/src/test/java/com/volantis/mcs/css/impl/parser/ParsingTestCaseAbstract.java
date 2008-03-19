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

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.css.parser.DiagnosticListener;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleSheetTester;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.properties.FontFamilyKeywords;
import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.TimeUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Base for all classes that test parsing.
 *
 * @todo get rid of this and use mocks for all instead of round tripping.
 */
public abstract class ParsingTestCaseAbstract
        extends TestCaseAbstract {

    protected static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    protected static final StyleValue LENGTH_1PX = STYLE_VALUE_FACTORY
            .getLength(null, 1, LengthUnit.PX);
    protected static final StyleValue LENGTH_2CM = STYLE_VALUE_FACTORY
            .getLength(null, 2, LengthUnit.CM);
    protected static final StyleValue LENGTH_0PX = STYLE_VALUE_FACTORY
            .getLength(null, 0, LengthUnit.PX);
    protected static final StyleValue LENGTH_NEGATIVE_2PT = STYLE_VALUE_FACTORY
            .getLength(null, -2, LengthUnit.PT);

    protected static final StyleValue PERCENTAGE_25 = STYLE_VALUE_FACTORY
            .getPercentage(null, 25);
    protected static final StyleValue PERCENTAGE_50 = STYLE_VALUE_FACTORY
            .getPercentage(null, 50);
    protected static final StyleValue PERCENTAGE_100 = STYLE_VALUE_FACTORY
            .getPercentage(null, 100);
    protected static final StyleValue COLOR_GREEN = StyleColorNames.GREEN;
    protected static final StyleValue COLOR_10PC_20PC_30PC = STYLE_VALUE_FACTORY
            .getColorByPercentages(null, 10, 20, 30);
    protected static final StyleValue COLOR_3_7_11 = STYLE_VALUE_FACTORY
            .getColorByPercentages(null, 3, 7, 11);
    protected static final StyleValue COLOR_FFF = STYLE_VALUE_FACTORY
            .getColorByRGB(null, 0xFFFFFF);
    protected static final StyleValue COLOR_123456 = STYLE_VALUE_FACTORY
            .getColorByRGB(null, 0x123456);
    protected static final StyleValue IMAGE_MIMG = STYLE_VALUE_FACTORY
            .getComponentURI(null, "/image.mimg");
    protected static final StyleValue IMAGE_PNG = STYLE_VALUE_FACTORY
            .getURI(null, "/image.png");

    protected static final StyleValue ANGLE_10DEG = STYLE_VALUE_FACTORY
            .getAngle(null, 10, AngleUnit.DEG);

    protected static final StyleValue ANGLE_NEGATIVE_10DEG = STYLE_VALUE_FACTORY
            .getAngle(null, -10, AngleUnit.DEG);

    protected static final StyleValue ANGLE_20RAD = STYLE_VALUE_FACTORY
            .getAngle(null, 20, AngleUnit.RAD);

    protected static final StyleValue ANGLE_30GRAD = STYLE_VALUE_FACTORY
            .getAngle(null, 30, AngleUnit.GRAD);

    protected static final StyleValue ANGLE_0DEG = STYLE_VALUE_FACTORY
            .getAngle(null, 0, AngleUnit.DEG);

    protected static final StyleValue INHERIT = STYLE_VALUE_FACTORY
            .getInherit();

    protected static final StyleValue INTEGER_10 = STYLE_VALUE_FACTORY
            .getInteger(null, 10);

    protected static final StyleValue INTEGER_NEGATIVE_10 = STYLE_VALUE_FACTORY
            .getInteger(null, -10);

    protected static final StyleValue NUMBER_10 = STYLE_VALUE_FACTORY
            .getNumber(null, 10);

    protected static final StyleValue NUMBER_1_5 = STYLE_VALUE_FACTORY
            .getNumber(null, 1.5);

    protected static final StyleValue NUMBER_NEGATIVE_1_5 = STYLE_VALUE_FACTORY
            .getNumber(null, -1.5);

    protected static final StyleValue TIME_10S = STYLE_VALUE_FACTORY
            .getTime(null, 10, TimeUnit.S);

    protected static final StyleValue TIME_NEGATIVE_10MS = STYLE_VALUE_FACTORY
            .getTime(null, -10, TimeUnit.MS);

    protected static final StyleValue STRING_STRING = STYLE_VALUE_FACTORY
            .getString(null, "string");

    protected static final StyleValue FREQ_10HZ = STYLE_VALUE_FACTORY
            .getFrequency(null, 10, FrequencyUnit.HZ);

    protected static final StyleValue FREQ_10KHZ = STYLE_VALUE_FACTORY
            .getFrequency(null, 10, FrequencyUnit.KHZ);

    protected static final StyleValue FRACTION_FIFTH_CM_PER_SEC = STYLE_VALUE_FACTORY.
            getFraction(LENGTH_2CM, TIME_10S);

    protected static final StyleValue FRACTION_NEG_FIFTH_PT_PER_SEC = STYLE_VALUE_FACTORY.
            getFraction(LENGTH_NEGATIVE_2PT, TIME_10S);

    protected static final StyleValue FONT_ARIAL_SERIF;
    static {
        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getString(null, "Arial"));
        list.add(FontFamilyKeywords.SERIF);
        FONT_ARIAL_SERIF = STYLE_VALUE_FACTORY.getList(list);
    }

    protected static final StyleValue FONT_ARIAL;
    static {
        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getString(null, "Arial"));
        FONT_ARIAL = STYLE_VALUE_FACTORY.getList(list);
    }

    protected static final StyleValue FONT_SERIF;
    static {
        List list = new ArrayList();
        list.add(FontFamilyKeywords.SERIF);
        FONT_SERIF = STYLE_VALUE_FACTORY.getList(list);
    }

    protected static final StyleValue FONT_COURIER;
    static {
        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getIdentifier(null, "Courier"));
        FONT_COURIER = STYLE_VALUE_FACTORY.getList(list);
    }

    protected static final CSSParserFactory CSS_PARSER_FACTORY =
            CSSParserFactory.getDefaultInstance();

    private static final DiagnosticListener DIAGNOSTIC_LISTENER =
            new DiagnosticListener() {
                public void startParsing() {
                }

                public void message(Diagnostic diagnostic) {
                    throw new IllegalStateException(
                            "Unexpected diagnostic: " + diagnostic);
                }

                public void endParsing() {
                }
            };

    protected StyleSheet parseStyleSheet(String css) {
        return parseStyleSheet(new StringReader(css), null);
    }

    protected StyleSheet parseStyleSheet(Reader reader, String url) {
        CSSParser parser = createParser();
        return parser.parseStyleSheet(reader, url);
    }

    protected List parseSelectorGroup(String css) {
        CSSParser parser = createParser();
        return parser.parseSelectorGroup(css);
    }

    protected Selector parseSelector(String css) {
        CSSParser parser = createParser();
        return parser.parseSelector(css);
    }

    protected MutableStyleProperties parseDeclarations(String css) {
        CSSParser parser = createParser();
        return parser.parseDeclarations(css);
    }

    protected CSSParser createParser() {
        return CSS_PARSER_FACTORY.createParser(
                getStyleSheetFactory(), DIAGNOSTIC_LISTENER);
    }

    protected StyleSheetFactory getStyleSheetFactory() {
        return StyleSheetFactory.getDefaultInstance();
    }

    protected void doRoundTripTest(String inputCSS, String expectedCSS)
            throws Exception {

        StyleSheet styleSheet = parseStyleSheet(inputCSS);

        String result = StyleSheetTester.renderStyleSheet(styleSheet);
        assertEquals(expectedCSS, result);
    }

    protected void doDeclarationsRoundTrip(
            String inputDeclarations, String expectedDeclarations)
            throws Exception {

        doRoundTripTest("element {" + inputDeclarations + "}",
                        "element{" + expectedDeclarations + "}");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/4	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
