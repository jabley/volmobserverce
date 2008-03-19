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
package com.volantis.mcs.protocols;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.StylesBuilder;

/**
 * Test cases for TableCellAttributes class.
 */
public class TableCellAttributesTestCase
        extends TableAttributesTestCaseAbstract {

    private TableCellAttributes attributes;

    protected void setUp() throws Exception {
        super.setUp();
        attributes = new TableCellAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
    }

    protected void setBgColor(String value) {
        attributes.setBgColor(value);
    }

    public void testSetBackgroundColorBasics() {
        doTestSetBackgroundColorBasics();
    }

    public void testSetHeightBasics() {

        // test intial value
        checkPropertyMatches(StylePropertyDetails.HEIGHT, "auto");

        // test percentages
        attributes.setHeight("100%");
        checkPropertyMatches(StylePropertyDetails.HEIGHT, "100%");

        attributes.setHeight("100 %");
        checkPropertyMatches(StylePropertyDetails.HEIGHT, "100%");

        // test length values
        attributes.setHeight("10mm");
        checkPropertyMatches(StylePropertyDetails.HEIGHT, "10mm");

        attributes.setHeight("10 mm");
        checkPropertyMatches(StylePropertyDetails.HEIGHT, "10mm");

        // test keyword
        attributes.setHeight("auto");
        checkPropertyMatches(StylePropertyDetails.HEIGHT, "auto");

        // test cases
        attributes.setHeight("Auto");
        checkPropertyMatches(StylePropertyDetails.HEIGHT, "auto");

        // test invalid value
        try {
            attributes.setHeight("asdfasdf");
            fail("Should throw an IllegalArgumentException when set to " +
                "illegal value.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testSetNoWrapBasics() {
        // test initial value

        checkPropertyMatches(StylePropertyDetails.WHITE_SPACE, "normal");

        // test keywords
        attributes.setNoWrap("normal");
        checkPropertyMatches(StylePropertyDetails.WHITE_SPACE, "normal");

        attributes.setNoWrap("pre");
        checkPropertyMatches(StylePropertyDetails.WHITE_SPACE, "pre");

        attributes.setNoWrap("nowrap");
        checkPropertyMatches(StylePropertyDetails.WHITE_SPACE, "nowrap");

        // test different cases
        attributes.setNoWrap("NoWrap");
        checkPropertyMatches(StylePropertyDetails.WHITE_SPACE, "nowrap");

        // test illegal value
        try {
            attributes.setNoWrap("wrap");
            fail("Should throw an IllegalArgumentException when set to " +
                "illegal value.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    protected void setVAlign(String value) {
        attributes.setVAlign(value);
    }

    public void testSetVerticalAlignBasics() {
        doTestSetVerticalAlignBasics();
    }

    public void testSetWidthBasics() {

        // test intial value
        checkPropertyMatches(StylePropertyDetails.WIDTH, "auto");

        // test percentages
        attributes.setWidth("100%");
        checkPropertyMatches(StylePropertyDetails.WIDTH, "100%");

        attributes.setWidth("100 %");
        checkPropertyMatches(StylePropertyDetails.WIDTH, "100%");

        // test length values
        attributes.setWidth("10mm");
        checkPropertyMatches(StylePropertyDetails.WIDTH, "10mm");

        attributes.setWidth("10 mm");
        checkPropertyMatches(StylePropertyDetails.WIDTH, "10mm");

        // test keyword
        attributes.setWidth("auto");
        checkPropertyMatches(StylePropertyDetails.WIDTH, "auto");

        // test cases
        attributes.setWidth("Auto");
        checkPropertyMatches(StylePropertyDetails.WIDTH, "auto");

        // test invalid value
        try {
            attributes.setWidth("asdfasdf");
            fail("Should throw an IllegalArgumentException when set to " +
                "illegal value.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Javadoc inherited.
    protected AbstractTableAttributes getAbstractTableAttributes() {
        return attributes;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9348/3	gkoch	VBM:2005081805 TableCellAttributes.noWrap property is stored in styles + inlined getters

 19-Aug-05	9245/3	gkoch	VBM:2005081006 vbm2005081006 attributes to store property values in styles, pt 2.

 ===========================================================================
*/
