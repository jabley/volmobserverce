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
import com.volantis.mcs.themes.StyleValue;

/**
 * The renderer for -wap-input-format
 */
public class WapInputFormatRenderer extends AbstractMarinerInputFormatRenderer {

    /**
     * The protected constructor for this singleton class.
     */
    protected WapInputFormatRenderer() {
    }

    // javadoc inherited
    public String getName() {
        return "-wap-input-format";
    }

    // javadoc inherited
    public StyleValue getValue(StyleProperties properties) {
        return properties.getStyleValue(StylePropertyDetails.MCS_INPUT_FORMAT);
    }

    public PropertyValue getPropertyValue(StyleProperties properties) {
        return properties.getPropertyValue(
                StylePropertyDetails.MCS_INPUT_FORMAT);
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/
