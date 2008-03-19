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

import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;

public class FormatAttributeToStyleValueConverterImpl
        implements FormatAttributeToStyleValueConverter {

    /**
     * The object to use to create the value.
     */
    protected final StyleValueFactory styleValueFactory;

    /**
     * Initialise.
     *
     * @param styleValueFactory The object to use to create style values.
     */
    public FormatAttributeToStyleValueConverterImpl(
            StyleValueFactory styleValueFactory) {

        this.styleValueFactory = styleValueFactory;
    }

    protected double getMagnitude(String magnitudeString) {
        double magnitude;
        try {
            magnitude = Double.parseDouble(magnitudeString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Could not convert border width '" + magnitudeString +
                    "' because '" + e.getMessage() + "'");
        }
        return magnitude;
    }

    // Javadoc inherited.
    public StyleValueFactory getFactory() {
        return styleValueFactory;
    }

    // Javadoc inherited.
    public StyleColor getColorValue(String colour) {

        if (colour == null || colour.length() == 0) {
            return null;
        }

        StyleColor value = null;

        if (colour.startsWith("#")) {
            int rgb = -1;

            try {
                rgb = Integer.parseInt(colour.substring(1), 16);

                // If the colour is a short hand form, i.e. #123 then expand it
                // to #112233.
                if (colour.length() == 4) {
                    rgb = (rgb & 0xf00) * 0x1100
                            + (rgb & 0xf0) * 0x110
                            + (rgb & 0xf) * 0x11;
                }

                if (rgb != -1) {
                    value = styleValueFactory.getColorByRGB(null, rgb);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Could not convert string '" + colour +
                        "' to colour because '" + e.getMessage() + "'");
            }

        } else {
            value = StyleColorNames.getColorByName(colour);
        }

        return value;
    }

    // Javadoc inherited.
    public StyleValue getDimensionValue(String magnitudeString,
            String unitString) {

        if (magnitudeString == null || magnitudeString.length() == 0) {
            return null;
        }

        // Treat a magnitude of 0 as not being set.
        double magnitude = getMagnitude(magnitudeString);
        if (magnitude == 0) {
            return null;
        }

        if (FormatConstants.WIDTH_UNITS_VALUE_PERCENT.equals(unitString)) {
            return styleValueFactory.getPercentage(null, magnitude);
        } else {
            return styleValueFactory.getLength(null, magnitude, LengthUnit.PX);
        }
    }

    // Javadoc inherited.
    public StyleValue getLengthValue(String magnitudeString, 
            boolean zeroIsSignificant) {

        if (magnitudeString == null || magnitudeString.length() == 0) {
            return null;
        }

        // Treat a magnitude of 0 as the default and don't return a value for
        // it.
        double magnitude = getMagnitude(magnitudeString);
        if (magnitude == 0 && !zeroIsSignificant) {
            return null;
        }

        return styleValueFactory.getLength(null, magnitude, LengthUnit.PX);
    }

    public StyleValue getPairValue(StyleValue value) {
        return styleValueFactory.getPair(value, value);
    }

    // Javadoc inherited.
    public StyleComponentURI getComponentURI(String backgroundComponentName) {
        return styleValueFactory.getComponentURI(null, backgroundComponentName);
    }

    // Javadoc inherited.
    public StyleValue getHorizontalAlign(String align) {
        StyleKeyword alignKeyword = null;
        if (FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_CENTER.equals(
                align)) {
            alignKeyword = TextAlignKeywords.CENTER;
        } else if (FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_RIGHT.equals(
                align)) {
            alignKeyword = TextAlignKeywords.RIGHT;
        } else if (FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_LEFT.equals(
                align)) {
            alignKeyword = TextAlignKeywords.LEFT;
        } else if (FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_START.equals(
                align)) {
            alignKeyword = TextAlignKeywords.START;
        } else if (FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_END.equals(
                align)) {
            alignKeyword = TextAlignKeywords.END;
        }

        return alignKeyword;
    }

    // Javadoc inherited.
    public StyleValue getVerticalAlign(String align) {
        StyleKeyword alignKeyword = null;
        if (FormatConstants.VERTICAL_ALIGNMENT_VALUE_BOTTOM.equals(
                align)) {
            alignKeyword = VerticalAlignKeywords.BOTTOM;
        } else if (FormatConstants.VERTICAL_ALIGNMENT_VALUE_TOP.equals(
                align)) {
            alignKeyword = VerticalAlignKeywords.TOP;
        } else if (FormatConstants.VERTICAL_ALIGNMENT_VALUE_CENTER.equals(
                align)) {
            alignKeyword = VerticalAlignKeywords.MIDDLE;
        }

        return alignKeyword;
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

 01-Dec-05	10447/3	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Nov-05	10201/3	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10221/1	emma	VBM:2005102606 Forward port: fixing various styling bugs

 09-Nov-05	10201/3	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
