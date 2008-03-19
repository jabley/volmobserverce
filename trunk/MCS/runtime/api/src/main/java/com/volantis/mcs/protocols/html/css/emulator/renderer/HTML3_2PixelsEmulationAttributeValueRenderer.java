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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationAttributeValueRenderer;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.utilities.StringConvertor;

/**
 * Renders any style value containing a pixel length as an emulated attribute
 * value for HTML 3.2
 * <p>
 * This translates the style value pixels value into a plain number, for 
 * example 20px becomes "20".
 * <p>
 * This is most useful because pixels is the assumed unit for most length 
 * values in HTML 3.2. 
 */ 
public class HTML3_2PixelsEmulationAttributeValueRenderer 
        implements StyleEmulationAttributeValueRenderer {

    // Javadoc inherited.
    public String render(StyleValue value) {
        
        String result = null;
        if (value instanceof StyleLength) {
            StyleLength length = (StyleLength) value;
            if (length.getUnit() == LengthUnit.PX) {
                result = StringConvertor.valueOf(length.pixels());
            }
        }
        return result;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/4	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
