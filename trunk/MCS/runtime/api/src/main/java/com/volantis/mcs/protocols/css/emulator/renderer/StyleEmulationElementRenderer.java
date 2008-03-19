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
import com.volantis.mcs.protocols.DOMHelper;
import com.volantis.mcs.themes.StyleValue;

/**
 * An abstract class for simple style emulation property renderers which only
 * need to render a simple element with no attributes - e.g. i and anti-i.
 * <p>
 * Subclasses just need to implement the {@link #getElementName} method to
 * return the name of the element associated with the value being emulated.
 */
public abstract class StyleEmulationElementRenderer
        implements StyleEmulationPropertyRenderer {

    /**
     * Return the name of the element to be rendered for the style value
     * provided.
     *
     * @param value the style value to be emulated.
     * @return the name of the element associated with the style value.
     */
    protected abstract String getElementName(StyleValue value);

    // Javadoc inherited.
    public void apply(Element element, StyleValue value) {

        final String elementName = getElementName(value);
        if (elementName != null) {
            DOMHelper.insertChildElement(element, elementName);
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/4	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
