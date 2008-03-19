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
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * This class is responsible for emulating the horizontal rule by applying
 * border styling to the hr element.
 * <p>
 * i.e
 * border-width: 1px 0px 0px 0px;
 * border-color: black;
 * border-style: solid;
 * </p>
 * <p>
 * This is required as some browsers do not honour standard styling on the
 * hr element, e.g: width, height etc, but do support border styling.
 * </p>
 */
public class HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule
        extends DefaultHorizontalRuleEmulator {

    //javadoc inherited
    public Element doEmulation(DOMOutputBuffer domOutputBuffer,
                                         HorizontalRuleAttributes hrAttrs)
            throws ProtocolException {

        Element hr = domOutputBuffer.addStyledElement("hr", hrAttrs);
        Styles styles = hr.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();

        // Don't need to set these as specified as these values are not the
        // initial values and hence can never be cleared.
        propertyValues.setComputedValue(
                StylePropertyDetails.BORDER_TOP_WIDTH, height);
        propertyValues.setComputedValue(
                StylePropertyDetails.BORDER_RIGHT_WIDTH, STYLE_LENGTH_0);
        propertyValues.setComputedValue(
                StylePropertyDetails.BORDER_BOTTOM_WIDTH, STYLE_LENGTH_0);
        propertyValues.setComputedValue(
                StylePropertyDetails.BORDER_LEFT_WIDTH, STYLE_LENGTH_0);
        propertyValues.setComputedValue(
                        StylePropertyDetails.BORDER_TOP_STYLE,
                        BorderStyleKeywords.SOLID);

        // Set this specified as if the color is the same as color then it
        // will be cleared unless it is explicitly specified and some devices
        // do not support inheriting border colors from the color property.
        propertyValues.setComputedAndSpecifiedValue(
                StylePropertyDetails.BORDER_TOP_COLOR, color);

        return hr;
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10812/1	pduffin	VBM:2005121322 Committing changes ported forward from 3.5

 13-Dec-05	10808/1	pduffin	VBM:2005121322 Fixed horizontal rule emulation for SonyEricsson-P900

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 25-Oct-05	9565/7	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 ===========================================================================
*/
