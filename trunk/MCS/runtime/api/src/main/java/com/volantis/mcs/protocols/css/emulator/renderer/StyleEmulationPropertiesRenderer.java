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

import com.volantis.mcs.dom.Element;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Implementations of this interface render the stylistic markup required to 
 * emulate a set of style properties.
 *
 * @mock.generate 
 */
public interface StyleEmulationPropertiesRenderer {

    /**
     * Write stylistic markup to emulate of the style properties
     * provided.
     *
     * @param element            the element to write to.
     * @param propertyValues the style properties to start the emulation of.
     */
    public void applyProperties(Element element,
                     MutablePropertyValues propertyValues);

}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9184/3	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 24-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/6	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 29-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 ===========================================================================
*/
