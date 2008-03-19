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
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;

/**
 * Renders a 'border-spacing' style value as an emulated attribute value for 
 * HTML 3.2.
 * <p>
 * Unusually, border-spacing is a pair value containing two values. Here we 
 * just take the first value and render it's pixel value.  
 */ 
public final class HTML3_2BorderSpacingEmulationAttributeValueRenderer
        implements StyleEmulationAttributeValueRenderer {
    
    private final HTML3_2PixelsEmulationAttributeValueRenderer PIXELS_RENDERER =
            new HTML3_2PixelsEmulationAttributeValueRenderer();

    // Javadoc inherited. 
    public String render(StyleValue value) {
        
        String result = null;
        if (value instanceof StylePair) {
            final StylePair pair = (StylePair) value;
            final StyleValue first = pair.getFirst();
            result = PIXELS_RENDERER.render(first);
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

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
