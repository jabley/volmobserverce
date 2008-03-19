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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactoryMock;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link FormatAttributeToStyleValueConverter}.
 */
public class FormatAttributeToStyleValueConverterTestCase
        extends TestCaseAbstract {

    protected StyleValueFactoryMock styleValueFactoryMock;
    protected StyleColor styleColor;
    protected StylePercentage stylePercentage;
    protected StyleLength styleLength;

    protected void setUp() throws Exception {
        super.setUp();

        styleColor = StyleColorNames.RED;

        stylePercentage =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 99);

        styleLength =
            StyleValueFactory.getDefaultInstance().getLength(
                null, 0.0, LengthUnit.PX);

        styleValueFactoryMock = new StyleValueFactoryMock(
                "styleValueFactoryMock", expectations);
    }

    /**
     * Make sure that an unset vertical align value returns null.
     */
    public void testVerticalAlignUnset() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatAttributeToStyleValueConverter converter = createConverter();
        StyleValue actual = converter.getVerticalAlign(null);
        assertNull("Value", actual);
    }

    /**
     * Make sure that an unknown vertical align value is treated as middle.
     */
    public void testVerticalAlignUnknown() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatAttributeToStyleValueConverter converter = createConverter();
        StyleValue actual = converter.getVerticalAlign("unknown");
        assertNull("Value", actual);
    }

    /**
     * Make sure that an abbreviated color is expanded.
     */
    public void testBackgroundColourAbbreviated() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleValueFactoryMock.expects.getColorByRGB(null, 0x112233)
                .returns(styleColor);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatAttributeToStyleValueConverter converter = createConverter();
        StyleValue actual = converter.getColorValue("#123");
        assertSame("Value", styleColor, actual);
    }

    /**
     * Make sure that an RGB color is handled properly.
     */
    public void testBackgroundColourRGB() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleValueFactoryMock.expects.getColorByRGB(null, 0x112233)
                .returns(styleColor);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatAttributeToStyleValueConverter converter = createConverter();
        StyleValue actual = converter.getColorValue("#112233");
        assertSame("Value", styleColor, actual);
    }

    /**
     * Test that a percentage dimension works properly.
     */
    public void testPercentageDimension() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleValueFactoryMock.expects.getPercentage(null, 99)
                .returns(stylePercentage);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatAttributeToStyleValueConverter converter = createConverter();
        StyleValue actual = converter.getDimensionValue("99", "percent");
        assertSame("Value", stylePercentage, actual);
    }

    private FormatAttributeToStyleValueConverter createConverter() {
        return new FormatAttributeToStyleValueConverterImpl(
                styleValueFactoryMock);
    }

    /**
     * Verify that if getLength is called when zero values are significant then
     * it should not be assumed to be a default value that can be overridden,
     * and the method should return zero.
     */
    public void testGetLengthWhenZeroIsSignificant() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        styleValueFactoryMock.expects.getLength(null, 0.0, LengthUnit.PX)
                .returns(styleLength);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatAttributeToStyleValueConverter converter = createConverter();
        StyleValue actual = converter.getLengthValue("0", true);
        assertEquals(styleLength, actual);
    }

    /**
     * Verify that if getLength is called when zero values are not significant
     * then it is assumed to be a default value that can be overridden, and
     * the method should return null.
     */
    public void testGetLengthWhenZeroIsNotSignificant() {
        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatAttributeToStyleValueConverter converter = createConverter();
        StyleValue actual = converter.getLengthValue("0", false);
        assertNull(actual);
    }

    /**
     * Verify that null is returned if getLength is called with an empty
     * string or null.
     */
    public void testGetLengthWithEmptyString() {
        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatAttributeToStyleValueConverter converter = createConverter();
        StyleValue actual = converter.getLengthValue("", false);
        assertNull(actual);

        actual = converter.getLengthValue(null, false);
        assertNull(actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/2	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Nov-05	10201/3	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10221/1	emma	VBM:2005102606 Forward port: fixing various styling bugs

 09-Nov-05	10201/3	emma	VBM:2005102606 Fixing various styling bugs

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
