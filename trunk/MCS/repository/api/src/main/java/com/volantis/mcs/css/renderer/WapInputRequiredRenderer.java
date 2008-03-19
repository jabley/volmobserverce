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

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.StyleValueFactory;

/**
 * The renderer for -wap-input-required
 */
public class WapInputRequiredRenderer extends
        AbstractMarinerInputFormatRenderer {

    /**
     * A StyleString representation of true
     */
    private static final StyleValue TRUE_VALUE =
        StyleValueFactory.getDefaultInstance().getIdentifier(null, "true");

    /**
     * A StyleString representation of false
     */
    private static final StyleValue FALSE_VALUE =
        StyleValueFactory.getDefaultInstance().getIdentifier(null, "false");

    /**
     * The protected constructor for this singleton class.
     */
    protected WapInputRequiredRenderer () {
    }

    // javadoc inherited
    public String getName () {
        return "-wap-input-required";
    }

     // javadoc inherited
    public StyleValue getValue(StyleProperties properties) {
        final StyleValue value = properties
                .getStyleValue(StylePropertyDetails.MCS_INPUT_FORMAT);

        StyleValue result = value;

        if (value instanceof StyleString) {
            final String stringValue = ((StyleString) value).getString();
            if (stringValue != null && stringValue.length() > 0) {
                if (Character.isLowerCase(stringValue.charAt(0))) {
                    result = FALSE_VALUE;
                } else {
                    result = TRUE_VALUE;
                }
            }
        }
        return result;
    }

    public PropertyValue getPropertyValue(StyleProperties properties) {
        StyleValue value = getValue(properties);
        if (value == null) {
            return null;
        } else {
            return ThemeFactory.getDefaultInstance().createPropertyValue(
                StylePropertyDetails.MCS_INPUT_FORMAT, value);
        }
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, also fixed string rendering as well

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/
