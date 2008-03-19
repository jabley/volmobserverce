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

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.MCSTextBlinkKeywords;
import com.volantis.mcs.themes.properties.MCSTextLineThroughStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextOverlineStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;

public class TextDecorationRendererTestCase
     extends GenericRendererTestAbstract {

    private MutableStyleProperties createMutableStyleProperties() {
        return ThemeFactory.getDefaultInstance().createMutableStyleProperties();
    }

    /**
     * Check that when none of the text-decoration related style properties
     * are set then nothing is output.
     */
    public void testNoneSet() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();

        TextDecorationRenderer renderer = new TextDecorationRenderer();
        checkRender(renderer, styleProperties, "");
    }

    /**
     * Check that when the mcs-text-blink style property is set to none
     * then the text-decoration is set to none.
     */
    public void testBlinkNone() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextBlinkKeywords.NONE;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_BLINK, value);

        doTestNone(styleProperties);
    }

    /**
     * Check that when the mcs-text-blink style property is set
     * then the text-decoration is blink.
     */
    public void testBlinkSet() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextBlinkKeywords.BLINK;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_BLINK, value);

        doTestSet(styleProperties, "blink");
    }

    private void doTestSet(
            MutableStyleProperties styleProperties, String keyword)
            throws Exception {

        TextDecorationRenderer renderer = new TextDecorationRenderer();
        checkRender(renderer, styleProperties, "text-decoration:" +
                                               keyword + ";");
    }

    private void doTestNone(MutableStyleProperties styleProperties)
            throws Exception {

        doTestSet(styleProperties, "none");
    }

    /**
     * Check that when the mcs-text-line-through-style property is set to none
     * then the text-decoration is set to none.
     */
    public void testLineThroughNone() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextLineThroughStyleKeywords.NONE;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE, value);

        doTestNone(styleProperties);
    }

    /**
     * Check that when the mcs-text-line-through-style property is set
     * then the text-decoration is line-through.
     */
    public void testLineThroughSet() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextLineThroughStyleKeywords.SOLID;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE, value);

        doTestSet(styleProperties, "line-through");
    }

    /**
     * Check that when the mcs-text-overline-style property is set to none
     * then the text-decoration is set to none.
     */
    public void testOverlineNone() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextOverlineStyleKeywords.NONE;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_OVERLINE_STYLE, value);

        doTestNone(styleProperties);
    }

    /**
     * Check that when the mcs-text-overline-style property is set
     * then the text-decoration is overline.
     */
    public void testOverlineSet() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextOverlineStyleKeywords.SOLID;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_OVERLINE_STYLE, value);

        doTestSet(styleProperties, "overline");
    }

    /**
     * Check that when the mcs-text-underline-style property is set to none
     * then the text-decoration is set to none.
     */
    public void testUnderlineNone() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextUnderlineStyleKeywords.NONE;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE, value);

        doTestNone(styleProperties);
    }

    /**
     * Check that when the mcs-text-underline-style property is set
     * then the text-decoration is underline.
     */
    public void testUnderlineSet() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextUnderlineStyleKeywords.SOLID;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE, value);

        doTestSet(styleProperties, "underline");
    }

    /**
     * Check that when many are set then the text-decoration contains them
     * all.
     */
    public void testManySet() throws Exception {

        MutableStyleProperties styleProperties = createMutableStyleProperties();
        StyleValue value;

        value = MCSTextBlinkKeywords.NONE;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_BLINK, value);

        value = MCSTextLineThroughStyleKeywords.SOLID;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE, value);

        value = MCSTextUnderlineStyleKeywords.SOLID;
        styleProperties.setStyleValue(
                StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE, value);

        doTestSet(styleProperties, "line-through underline");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 ===========================================================================
*/
