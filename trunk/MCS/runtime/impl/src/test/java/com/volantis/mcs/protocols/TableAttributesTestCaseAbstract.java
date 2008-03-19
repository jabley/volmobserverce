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

package com.volantis.mcs.protocols;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class TableAttributesTestCaseAbstract
    extends TestCaseAbstract {

    protected abstract AbstractTableAttributes getAbstractTableAttributes();


    protected StyleValue getStyleValue(StyleProperty property) {
        return getAbstractTableAttributes().getStyles().getPropertyValues()
                .getComputedValue(property);
    }

    protected void checkPropertyMatches(StyleProperty property, String css) {
        StyleValue actual = getStyleValue(property);
        StyleValue expected = StylesBuilder.getStyleValue(property, css);
        assertEquals(property.getName(), expected, actual);
    }

    protected void setBgColor(String value) {
        throw new UnsupportedOperationException();
    }

    protected void doTestSetBackgroundColorBasics() {

        checkPropertyMatches(StylePropertyDetails.BACKGROUND_COLOR,
                             "transparent");

        // test basic colours
        setBgColor("aqua");
        checkPropertyMatches(StylePropertyDetails.BACKGROUND_COLOR, "aqua");

        // test "template" colour
        try {
            setBgColor("ActiveBorder2");
            fail("Should throw an IllegalArgumentException when set to " +
                "illegal value.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test hex colour codes
        setBgColor("#00FFFF");
        checkPropertyMatches(StylePropertyDetails.BACKGROUND_COLOR, "#00ffff");

        // test different cases
        setBgColor("Aqua");
        checkPropertyMatches(StylePropertyDetails.BACKGROUND_COLOR, "aqua");

        // test keyword
        setBgColor("transparent");
        checkPropertyMatches(StylePropertyDetails.BACKGROUND_COLOR,
                             "transparent");

        // test invalid value
        try {
            setBgColor("asdfasdf");
            fail("Should throw an IllegalArgumentException when set to " +
                "illegal value.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testSetAlignBasics() {

        AbstractTableAttributes attributes = getAbstractTableAttributes();

        // test initial value
        assertEquals(TextAlignKeywords.LEFT,
                getStyleValue(StylePropertyDetails.TEXT_ALIGN));

        // test keywords
        attributes.setAlign("center");
        checkPropertyMatches(StylePropertyDetails.TEXT_ALIGN, "center");

        attributes.setAlign("left");
        checkPropertyMatches(StylePropertyDetails.TEXT_ALIGN, "left");

        attributes.setAlign("right");
        checkPropertyMatches(StylePropertyDetails.TEXT_ALIGN, "right");

        attributes.setAlign("justify");
        checkPropertyMatches(StylePropertyDetails.TEXT_ALIGN, "justify");

        // test different cases
        attributes.setAlign("Center");
        checkPropertyMatches(StylePropertyDetails.TEXT_ALIGN, "center");

        // test string value
        attributes.setAlign("asdasd");
        checkPropertyMatches(StylePropertyDetails.TEXT_ALIGN, "'asdasd'");

    }

    protected void setVAlign(String value) {
        throw new UnsupportedOperationException();
    }

    protected void doTestSetVerticalAlignBasics() {
        // test intial value

        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "baseline");

        // test percentages
        setVAlign("100%");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "100%");

        setVAlign("100 %");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "100%");

        // test length values
        setVAlign("10mm");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "10mm");

        setVAlign("10 mm");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "10mm");

        // test keywords
        setVAlign("baseline");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "baseline");

        setVAlign("sub");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "sub");

        setVAlign("super");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "super");

        setVAlign("top");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "top");

        setVAlign("text-top");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "text-top");

        setVAlign("middle");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "middle");

        setVAlign("bottom");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "bottom");

        setVAlign("text-bottom");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "text-bottom");


        // test cases
        setVAlign("Baseline");
        checkPropertyMatches(StylePropertyDetails.VERTICAL_ALIGN, "baseline");

        // test invalid value
        try {
            setVAlign("asdfasdf");
            fail("Should throw an IllegalArgumentException when set to " +
                "illegal value.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
