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
import com.volantis.mcs.protocols.DOMHelper;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementAttributeRenderer;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;

/**
 * <p>
 * Marquee style emulation renderer which:
 * <ul>
 * <li>transforms a div element with mcs-marquee-style set (to anything but
 * none) into a marquee element</li>
 * <li>wraps any other element with mcs-marquee-style set (to anything but
 * none) in a new marquee element</li>
 * </ul>
 * and uses {@link MarqueeAttributeValueRenderer} to convert the style
 * properties to the appropriate attribute values.
 * </p>
 */
public class MarqueeEmulationRenderer
        extends StyleEmulationElementAttributeRenderer {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param attributeName             of the attribute to be rendered
     * @param supportsInfiniteKeyword   whether the infinite keyword is
     *                                  supported by this protocol 
     */
    public MarqueeEmulationRenderer(String attributeName,
                                    boolean supportsInfiniteKeyword) {
        
        super("marquee", attributeName,
                new MarqueeAttributeValueRenderer(supportsInfiniteKeyword));
    }

    // Javadoc inherited
    public void apply(Element element, StyleValue value) {

        Element currentElement = element;

        // Marquee styles have no effect if the marquee style is NONE.
        if (!MCSMarqueeStyleKeywords.NONE.equals(value)) {
            if ("div".equals(currentElement.getName())) {
                // div elements can be renamed
                currentElement.setName(elementName);
            } else {
                // Either need to insert the emulating element, or use an
                // existing parent one.
                if (!elementName.equals(element.getName())) {
                    Element parent = currentElement.getParent();
                    if (!elementName.equals(parent.getName())) {
                        // Wrap the current element in a marquee element.
                        currentElement = DOMHelper.insertParentElement(
                                currentElement, elementName);
                    } else {
                        // The current parent is a marquee element, so we can
                        // use that.
                        currentElement = parent;
                    }
                }
            }

            // Render the mcs-marquee-style property as an attribute.
            processAttribute(currentElement, attributeName, value);
        }
    }
}
