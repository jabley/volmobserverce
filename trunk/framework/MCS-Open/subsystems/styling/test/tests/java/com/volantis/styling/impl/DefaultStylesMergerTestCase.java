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
package com.volantis.styling.impl;

import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.FontStyleKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.StylesMock;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.StylingFactoryMock;
import com.volantis.styling.debug.DebugStyles;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Iterator;

/**
 * Verifies that the {@link com.volantis.styling.StylesMerger} behaves as
 * expected when merging StyleBitsets, StyleValues, PropertyValues and Styles.
 */
public class DefaultStylesMergerTestCase extends TestCaseAbstract {

    private StylingFactoryMock factoryMock;
    private DefaultStylesMerger merger;
    private StyleValueFactory styleValueFactory =
            StyleValueFactory.getDefaultInstance();

    /**
     * Constants used when testing property values.
     */
    private final StyleValue winnerValue =
            styleValueFactory.getLength(null, 1,LengthUnit.PX);
    private final StyleValue luserValue = styleValueFactory.getLength(null, 2,
            LengthUnit.PX);

    private final StyleValue red = styleValueFactory.getColorByPercentages(
            null, 100, 0, 0);
    private final StyleValue green = styleValueFactory.getColorByPercentages(
            null, 0, 100, 0);
    private final StyleValue white = styleValueFactory.getColorByPercentages(
            null, 100, 100, 100);
    private final StyleKeyword italic = FontStyleKeywords.ITALIC;


    public void setUp() throws Exception {
        super.setUp();
        factoryMock = new StylingFactoryMock("factory", expectations);
        merger = new DefaultStylesMerger(factoryMock);
    }

    /**
     * Verify that calling mergeStyleValues with two null values results in a
     * null StyleValue being returned.
     */
    public void testMergeTwoNullStyleValues() {
//        StyleValue merged = merger.mergeStyleValues(null, null, null);
//        assertNull(merged);
    }

    /**
     * Verify that calling mergeStyleValues with a null winner value results
     * in the luser StyleValue being returned.
     */
    public void testMergeStyleValuesWithNullWinnerAndNonNullLuser() {
//        StyleValue luser = new StyleLength(10, LengthUnit.PX);
//        StyleValue merged = merger.mergeStyleValues(null, null, luser);
//        assertNotNull(merged);
//        assertEquals(luser, merged);
    }

    /**
     * Verify that calling mergeStyleValues with a null luser value results
     * in the winner StyleValue being returned.
     */
    public void testMergeStyleValuesWithNonNullWinnerAndNullLuser() {
//        StyleValue winner = new StyleLength(10, LengthUnit.PX);
//        StyleValue merged = merger.mergeStyleValues(null, winner, null);
//        assertNotNull(merged);
//        assertEquals(winner, merged);
    }

    /**
     * Verify that calling mergeStyleValues with non null winner and luser
     * values results in the winner StyleValue being returned.
     */
    public void testMergeStyleValuesWithNonNullWinnerAndLuser() {
//        StyleValue winner = new StyleLength(10, LengthUnit.PX);
//        StyleValue luser = new StylePercentage(5);
//        StyleValue merged = merger.mergeStyleValues(null, winner, luser);
//        assertNotNull(merged);
//        assertEquals(winner, merged);
    }

    /**
     * Verify that calling mergePropertyValues with non null winner and luser
     * value results in the individual property values being merged, with the
     * winner 'winning' if both are set.
     *
     * todo remove when prove that not needed.
     */
    public void notestMergePropertyValuesWithNonNullWinnerAndLuser()
            throws NoSuchFieldException {
        // ===================================================================
        //   Initialise test objects and set expectations
        // ===================================================================
//        StylingFactory originalFactory = (StylingFactory)PrivateAccessor.
//                getField(DefaultPropertyValueFactory.class, "stylingFactory");
//        PrivateAccessor.setField(DefaultPropertyValueFactory.class,
//                "stylingFactory", factoryMock);

        MutablePropertyValuesMock winnerProps = createWinnerPropertyValues();
        MutablePropertyValuesMock luserProps = createLuserPropertyValues();
        MutablePropertyValuesMock result =
                new MutablePropertyValuesMock("result", expectations);

        factoryMock.expects.createPropertyValues(
                StylePropertyDetails.getDefinitions()).returns(result);
        result.expects.stylePropertyIterator().returns(
                StylePropertyDetails.getDefinitions().stylePropertyIterator());

        // expected values from winner
        result.expects.setComputedValue(
                StylePropertyDetails.BORDER_BOTTOM_WIDTH, winnerValue);
        result.expects.setComputedValue(
                StylePropertyDetails.BORDER_TOP_WIDTH, winnerValue);
        result.expects.setComputedValue(
                StylePropertyDetails.BORDER_LEFT_WIDTH, winnerValue);
        result.expects.setComputedValue(
                StylePropertyDetails.BORDER_RIGHT_WIDTH, winnerValue);
        result.expects.setComputedValue(StylePropertyDetails.COLOR, red);

        // expected values from luser
        result.expects.setComputedValue(StylePropertyDetails.PADDING_BOTTOM,
                luserValue);
        result.expects.setComputedValue(StylePropertyDetails.PADDING_TOP,
                luserValue);
        result.expects.setComputedValue(StylePropertyDetails.PADDING_LEFT,
                luserValue);
        result.expects.setComputedValue(StylePropertyDetails.PADDING_RIGHT,
                luserValue);

        result.expects.setComputedValue(
                StylePropertyDetails.BORDER_LEFT_COLOR, red);
        result.expects.setComputedValue
                (StylePropertyDetails.BORDER_RIGHT_COLOR, red);
        result.expects.setComputedValue(
                StylePropertyDetails.BACKGROUND_COLOR, green);
        result.expects.setComputedValue(StylePropertyDetails.WIDTH,
                luserValue);
        result.expects.setComputedValue(StylePropertyDetails.HEIGHT,
                luserValue);
        result.expects.setComputedValue(StylePropertyDetails.FONT_STYLE,
                italic);

        // ===================================================================
        //   Test behaviour.
        // ===================================================================
//        PropertyValues merged =
//                merger.mergePropertyValues(winnerProps, luserProps);
//
//        assertNotNull(merged);

//        PrivateAccessor.setField(DefaultPropertyValueFactory.class,
//                "stylingFactory", originalFactory);
    }

    /**
     * Verify that calling mergePropertyValues with null winner and non null
     * luser value results in the luser PropertyValues being returned.
     *
     * todo remove when prove that not needed.
     */
    public void notestMergePropertyValuesWithNullWinnerAndNonNullLuser() {
        // ===================================================================
        //   Create test objects
        // ===================================================================
        expectations = mockFactory.createUnorderedBuilder();

        // ===================================================================
        //   Initialise test objects and set expectations
        // ===================================================================
        MutablePropertyValuesMock luserProps = createLuserPropertyValues();

        // ===================================================================
        //   Test behaviour.
        // ===================================================================
//        PropertyValues merged = merger.mergePropertyValues(null, luserProps);
//
//        assertNotNull(merged);
//
//        assertEquals(luserValue, merged.getComputedValue(
//                StylePropertyDetails.PADDING_BOTTOM));
//        assertEquals(luserValue, merged.getComputedValue(
//                StylePropertyDetails.PADDING_TOP));
//        assertEquals(luserValue, merged.getComputedValue(
//                StylePropertyDetails.PADDING_LEFT));
//        assertEquals(luserValue, merged.getComputedValue(
//                StylePropertyDetails.PADDING_RIGHT));
//        assertEquals(red, merged.getComputedValue(
//                StylePropertyDetails.BORDER_LEFT_COLOR));
//        assertEquals(red, merged.getComputedValue(
//                StylePropertyDetails.BORDER_RIGHT_COLOR));
//        assertEquals(green, merged.getComputedValue(
//                StylePropertyDetails.BACKGROUND_COLOR));
//
//        assertEquals(luserValue, merged.getComputedValue(
//                StylePropertyDetails.WIDTH));
//        assertEquals(luserValue, merged.getComputedValue(
//                StylePropertyDetails.HEIGHT));
//        assertEquals(italic, merged.getComputedValue(
//                StylePropertyDetails.FONT_STYLE));
    }

    /**
     * Verify that calling mergePropertyValues with a non null winner and null
     * luser value results in the winner PropertyValues being returned.
     *
     * todo remove when prove that not needed.
     */
    public void notestMergePropertyValuesWithNonNullWinnerAndNullLuser() {
        // ===================================================================
        //   Create test objects
        // ===================================================================
        expectations = mockFactory.createUnorderedBuilder();

        // ===================================================================
        //   Initialise test objects and set expectations
        // ===================================================================
        MutablePropertyValuesMock winnerProps = createWinnerPropertyValues();

        // ===================================================================
        //   Test behaviour.
        // ===================================================================
//        PropertyValues merged = merger.mergePropertyValues(winnerProps, null);
//
//        assertNotNull(merged);
//
//         values from winner
//        assertEquals(winnerValue, merged.getComputedValue(
//                StylePropertyDetails.BORDER_BOTTOM_WIDTH));
//        assertEquals(winnerValue, merged.getComputedValue(
//                StylePropertyDetails.BORDER_TOP_WIDTH));
//        assertEquals(winnerValue, merged.getComputedValue(
//                StylePropertyDetails.BORDER_LEFT_WIDTH));
//        assertEquals(winnerValue, merged.getComputedValue(
//                StylePropertyDetails.BORDER_RIGHT_WIDTH));
//        assertEquals(red, merged.getComputedValue(
//                StylePropertyDetails.COLOR));
    }

    /**
     * Verify that calling mergePropertyValues with null winner and luser
     * value results in null being returned.
     *
     * todo remove when prove that not needed.
     */
    public void notestMergePropertyValuesWithNullWinnerAndLuser() {
//        assertNull(merger.mergePropertyValues(null, null));
    }

    /**
     * Verify that calling merge with null winner and luser value results in
     * null being returned.
     *
     * todo remove when prove that not needed.
     */
    public void notestMergeWithNullWinnerAndLuserStyles() {
        assertNull(merger.merge(null, null));
    }

    /**
     * Verify that calling merge with null winner and non null luser value
     * results in the luser Styles being returned.
     *
     * todo remove when prove that not needed.
     */
    public void notestMergeWithNullWinnerAndNonNullLuserStyles() {
        StylesMock luserStyles = new StylesMock("luserStyles", expectations);
        assertNotNull(merger.merge(null, luserStyles));
    }

    /**
     * Verify that calling merge with non null winner and null luser value
     * results in the winner Styles being returned.
     *
     * todo remove when prove that not needed.
     */
    public void notestMergeWithNonNullWinnerAndNullLuserStyles() {
        StylesMock winnerStyles = new StylesMock("winnerStyles", expectations);
        assertNotNull(merger.merge(winnerStyles, null));
    }

    /**
     * Verify that calling merge with non null winner and luser value results
     * in a non null being returned, where getPropertyValues has been called on
     * both winner and luser styles, and a new Styles has been created with the
     * newly merged property values.
     */
//    public void testMergeWithNonNullWinnerAndLuserStyles()
//            throws NoSuchFieldException {
//        // ===================================================================
//        //   Create test objects
//        // ===================================================================
//        StylesMock winnerStyles = new StylesMock("winnerStyles", expectations);
//        StylesMock luserStyles = new StylesMock("luserStyles", expectations);
//        StylesMock result = new StylesMock("luserStyles", expectations);
//
//        // ===================================================================
//        //   Set expectations
//        // ===================================================================
//        winnerStyles.expects.getPropertyValues().returns(null);
//        luserStyles.expects.getPropertyValues().returns(null);
//        factoryMock.expects.createStyles(null).returns(result).any();
//        PrivateAccessor.setField(DefaultPropertyValueFactory.class,
//                "stylingFactory", factoryMock);
//
//        // ===================================================================
//        //   Test behaviour
//        // ===================================================================
//        assertEquals(result, merger.merge(winnerStyles, luserStyles));
//    }

    /**
     * Convenience method which takes a populated StyleProperties and turns it
     * into a fully populated MutablePropertyValuesMock. I would have used a
     * concrete PropertyValues, but can't access one from this test.
     * @param properties    the StyleProperties to use to initialise the mock
     *                      property values
     * @return fully initialised mock property values.
     */
    private MutablePropertyValuesMock createTestablePropertyValues(
            StyleProperties properties) {

        MutablePropertyValuesMock values =
                new MutablePropertyValuesMock("values", expectations);

        for (Iterator i = StylePropertyDetails.getDefinitions().stylePropertyIterator();
             i.hasNext();) {
            
            StyleProperty prop = (StyleProperty) i.next();
            StyleValue value = properties.getStyleValue(prop);
            values.expects.getComputedValue(prop).returns(value).any();
//            values.expects.getSpecifiedValue(prop).returns(value).any();
            values.expects.getComputedValue(prop).returns(value).any();
        }
        return values;
    }

    /**
     * Return a newly created MutablePropertyValuesMock which is initialised
     * to return values for BORDER_*_WIDTH and COLOR properties, and null for
     * all others.
     * @return newly created initialised MutablePropertyValuesMock
     */
    private MutablePropertyValuesMock createWinnerPropertyValues() {

        MutableStyleProperties winnerProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        winnerProperties.setStyleValue(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                winnerValue);
        winnerProperties.setStyleValue(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                winnerValue);
        winnerProperties.setStyleValue(StylePropertyDetails.BORDER_TOP_WIDTH,
                winnerValue);
        winnerProperties.setStyleValue(StylePropertyDetails.BORDER_LEFT_WIDTH,
                winnerValue);
        winnerProperties.setStyleValue(StylePropertyDetails.BORDER_RIGHT_WIDTH,
                winnerValue);
        winnerProperties.setStyleValue(StylePropertyDetails.COLOR, red);

        MutablePropertyValuesMock winnerProps =
                createTestablePropertyValues(winnerProperties);
        return winnerProps;
    }

    /**
     * Return a newly created MutablePropertyValuesMock which is initialised
     * to return values for BORDER_*_WIDTH, PADDING_*_WIDTH,
     * BORDER_LEFT|RIGHT_COLOR, BACKGROUND_COLOR, COLOR, WIDTH, HEIGHT and
     * FONT_STYLE properties, and null for all others.
     *
     * @return newly created initialised MutablePropertyValuesMock
     */
    private MutablePropertyValuesMock createLuserPropertyValues() {
        MutableStyleProperties luserProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        luserProperties.setStyleValue(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.BORDER_TOP_WIDTH,
                luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.BORDER_LEFT_WIDTH,
                luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.BORDER_RIGHT_WIDTH,
                luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.PADDING_BOTTOM,
                luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.PADDING_TOP,
                luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.PADDING_LEFT,
                luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.PADDING_RIGHT,
                luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.BORDER_LEFT_COLOR,
                red);
        luserProperties.setStyleValue(StylePropertyDetails.BORDER_RIGHT_COLOR,
                red);
        luserProperties.setStyleValue(StylePropertyDetails.BACKGROUND_COLOR,
                green);
        luserProperties.setStyleValue(StylePropertyDetails.COLOR, white);
        luserProperties.setStyleValue(StylePropertyDetails.WIDTH, luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.HEIGHT, luserValue);
        luserProperties.setStyleValue(StylePropertyDetails.FONT_STYLE, italic);

        MutablePropertyValuesMock luserProps =
                createTestablePropertyValues(luserProperties);
        return luserProps;
    }

    public void testMergeWinnerAndLoser() {

        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        Styles expectedResult = stylingFactory.createStyles(null);
        factoryMock.expects.createStyles(null)
                .returns(expectedResult);

        Styles winner = StylesBuilder.getCompleteStyles(
                "background-color: green; font-size: medium");
        Styles loser = StylesBuilder.getCompleteStyles(
                "background-color: red; color: red");

        Styles result = merger.merge(winner, loser);
        assertSame(expectedResult, result);

        MutableStylePropertySet set = new MutableStylePropertySetImpl();
        set.add(StylePropertyDetails.BACKGROUND_COLOR);
        set.add(StylePropertyDetails.COLOR);
        set.add(StylePropertyDetails.FONT_SIZE);

        DebugStyles debugStyles = new DebugStyles(set, false);
        String resultCSS = debugStyles.output(result, "");
        assertEquals("{background-color: green; color: red; font-size: medium}",
                resultCSS);
    }

    public void testMergeWinnerNoLoser() {
        Styles winner = StylesBuilder.getCompleteStyles(
                "background-color: green; font-size: medium", true);

        Styles result = merger.merge(winner, null);
        assertSame(winner, result);

        MutableStylePropertySet set = new MutableStylePropertySetImpl();
        set.add(StylePropertyDetails.BACKGROUND_COLOR);
        set.add(StylePropertyDetails.COLOR);
        set.add(StylePropertyDetails.FONT_SIZE);

        DebugStyles debugStyles = new DebugStyles(set, true);
        String resultCSS = debugStyles.output(result, "");
        assertEquals("{background-color: green; font-size: medium}",
                resultCSS);
    }

    public void testMergeLoserNoWinner() {
        Styles loser = StylesBuilder.getCompleteStyles(
                "background-color: red; color: red", true);

        Styles result = merger.merge(null, loser);
        assertSame(loser, result);

        MutableStylePropertySet set = new MutableStylePropertySetImpl();
        set.add(StylePropertyDetails.BACKGROUND_COLOR);
        set.add(StylePropertyDetails.COLOR);
        set.add(StylePropertyDetails.FONT_SIZE);

        DebugStyles debugStyles = new DebugStyles(set, true);
        String resultCSS = debugStyles.output(result, "");
        assertEquals("{background-color: red; color: red}",
                resultCSS);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10647/1	ibush	VBM:2005113021 Fix Border Bottom Styling by fixing styles merger

 06-Dec-05	10628/1	ibush	VBM:2005113021 Fix Border Bottom Styling by fixing styles merger

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/11	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/7	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/3	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (3)

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 ===========================================================================
*/
