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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementSetAttributeRenderer;
import com.volantis.mcs.themes.StyleValue;

/**
 * Renders the marquee style properties as stylistic markup.
 */
public class MarqueeEmulationAttributePropertyRenderer
        extends StyleEmulationElementSetAttributeRenderer {

    /**
     * The only element to which marquee style properties apply.
     */
    private static final String[] VALID_MARQUEE_ELEMENTS =
            new String[] {"marquee"};

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param attributeName             name of the attribute to be rendered
     * @param supportsInfiniteKeyword   whether the infinite keyword is
     *                                  supported by this protocol 
     */
    public MarqueeEmulationAttributePropertyRenderer(String attributeName,
            boolean supportsInfiniteKeyword) {
        super(VALID_MARQUEE_ELEMENTS, attributeName,
                new MarqueeAttributeValueRenderer(supportsInfiniteKeyword));
    }

    // javadoc inherited
    public void apply(Element element, StyleValue value) {

        if (matchingElements.contains(element.getName())) {
            processAttribute(element, attributeName, value);
        } else if (element.getParent() != null) {
            super.apply(element.getParent(), value);
        }
    }
}
