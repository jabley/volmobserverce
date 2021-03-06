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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.themes.StyleValue;

/**
 * Implementations of this interface render style values as "emulated"
 * attribute values.
 */
public interface StyleEmulationAttributeValueRenderer {

    /**
     * Render the style value provided, returning the attribute value to
     * emulate it with as a string.
     *
     * @param value the style value to emulate.
     * @return the attribute value to emulate the style value with.
     */
    String render(StyleValue value);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
