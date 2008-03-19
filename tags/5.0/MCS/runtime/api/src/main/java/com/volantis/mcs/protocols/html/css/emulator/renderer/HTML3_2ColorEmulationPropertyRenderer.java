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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementAttributeRenderer;
import com.volantis.mcs.themes.StyleValue;

/**
 * If the parent is 'canvas' element then use
 *
 * <pre>&lt;body text="xxx"&gt;</pre>
 *
 * otherwise use
 *
 * <pre>&lt;font color="xxx"&gt;</pre>
 *
 * Note that if the colour for the text is the same as the
 * <pre>&lt;body text="xxx"&gt;</pre> then there is no need to generate the
 * colour attribute. This is currently not supported using rules and will
 * be handled using a DOM transformer.
 */
public class HTML3_2ColorEmulationPropertyRenderer 
        extends StyleEmulationElementAttributeRenderer {

    public HTML3_2ColorEmulationPropertyRenderer() {
        
        super("font", "color", 
                new HTML3_2ColorEmulationAttributeValueRenderer());
    }

    // Javadoc inherited
    public void apply(Element element, StyleValue value) {

        if ("body".equals(element.getName())) {
            element.setAttribute("text",
                                 attributeValueRenderer.render(value));
        } else {
            super.apply(element, value);
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/4	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 29-Jun-04	4720/9	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 29-Jun-04	4720/5	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 28-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 ===========================================================================
*/
