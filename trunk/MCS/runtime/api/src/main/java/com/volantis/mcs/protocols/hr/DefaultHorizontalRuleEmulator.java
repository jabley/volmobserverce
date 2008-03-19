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
package com.volantis.mcs.protocols.hr;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.HeightKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.properties.WidthKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Base emulator for all other HorizontalRuleEmulators. Instances of this
 * class simply create a standard hr element
 */
public class DefaultHorizontalRuleEmulator
        implements HorizontalRuleEmulator {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private static final StylePercentage STYLE_PERCENTAGE_100 =
        STYLE_VALUE_FACTORY.getPercentage(null, 100);
    private static final StyleLength STYLE_LENGTH_1PX =
        STYLE_VALUE_FACTORY.getLength(null, 1, LengthUnit.PX);
    protected static final StyleLength STYLE_LENGTH_0 =
        STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX);

    /**
     * width property of the hr
     */
    protected StyleValue width;

    /**
     * align style of the hr
     */
    protected StyleValue align;

    /**
     * height value for the hr
     */
    protected StyleValue height;

    /**
     * color of the hr
     */
    protected StyleValue color;


    public final Element emulateHorizontalRule(DOMOutputBuffer domOutputBuffer,
                                         HorizontalRuleAttributes hrAttrs)
            throws ProtocolException {

        initialiseState(hrAttrs);
        return doEmulation(domOutputBuffer, hrAttrs);
    }

    /**
     * default method which simply renders a hr element
     * @return
     */
    protected Element doEmulation(DOMOutputBuffer domOutputBuffer,
                                         HorizontalRuleAttributes hrAttrs)
            throws ProtocolException {
        return domOutputBuffer.addStyledElement ("hr", hrAttrs);
    }

    /**
     * initialise the state of the emulator using a given set of attributes
     * @param hrAttrs
     */
    protected void initialiseState(HorizontalRuleAttributes hrAttrs) {

        MutablePropertyValues propertyValues =
                hrAttrs.getStyles().getPropertyValues();

        //read the style values from the hr attributes
        width = propertyValues
                .getComputedValue(StylePropertyDetails.WIDTH);

        if (propertyValues.wasExplicitlySpecified(StylePropertyDetails.TEXT_ALIGN)) {
            align = propertyValues
                    .getComputedValue(StylePropertyDetails.TEXT_ALIGN);

        } else {
            align = TextAlignKeywords.CENTER;
        }

        height = propertyValues
                .getComputedValue(StylePropertyDetails.HEIGHT);

        color = propertyValues
                .getComputedValue(StylePropertyDetails.COLOR);

        //set the default for any value not found
        if (width == WidthKeywords.AUTO) {
            width = STYLE_PERCENTAGE_100;
        }
        if (height == HeightKeywords.AUTO) {
            height = STYLE_LENGTH_1PX;
        }
        if (width == null || height == null) {
            throw new IllegalStateException(
                    "Neither width, nor height should be null");
        }
        if (color == null) {
            color = StyleColorNames.BLACK;
        }
    }

    protected static String getStyleValueAsString(StyleValue styleValue) {
        String value = null;

        if (styleValue != null) {
            if (styleValue instanceof StyleLength) {
                StyleLength length = (StyleLength) styleValue;
                if (length.getUnit() == LengthUnit.PX) {
                    value = length.getPixelsAsString();
                }
            } else if (styleValue instanceof StylePercentage) {
                return styleValue.getStandardCSS();
            } else {
                throw new IllegalArgumentException(
                        styleValue + " must be a StyleLength");
            }
        }

        return value;
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10812/1	pduffin	VBM:2005121322 Committing changes ported forward from 3.5

 13-Dec-05	10808/1	pduffin	VBM:2005121322 Fixed horizontal rule emulation for SonyEricsson-P900

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 25-Oct-05	9565/7	ibush	VBM:2005081219 Horizontal Rule Emulation

 24-Oct-05	9565/5	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 ===========================================================================
*/
