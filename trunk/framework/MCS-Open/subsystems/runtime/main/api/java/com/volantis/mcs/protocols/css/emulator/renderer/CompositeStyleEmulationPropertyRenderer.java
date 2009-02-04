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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.themes.StyleValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class allows instances of {@link StyleEmulationPropertiesRenderer}'s
 * to be processed as a composite.
 * <p>
 * Structuring {@link StyleEmulationPropertiesRenderer}'s in this manner allows
 * multiple {@link StyleEmulationPropertiesRenderer}'s to be mapped with
 * a single css property.
 *
 * @see DefaultStyleEmulationPropertiesRenderer#register
 */
public final class CompositeStyleEmulationPropertyRenderer
        implements StyleEmulationPropertyRenderer {

    /**
     * Collection of {@link StyleEmulationPropertiesRenderer}'s.
     */
    private final List emulationPropertyRenderers = new ArrayList();

    /**
     * Adds a renderer to this composite.
     *
     * @param emulationPropertyRenderer the renderer to be added to
     * this composite.
     */
    public void add(StyleEmulationPropertyRenderer emulationPropertyRenderer) {
        emulationPropertyRenderers.add(emulationPropertyRenderer);
    }

    // Javadoc inherited.
    public void apply(Element element, StyleValue value) {
        // call open on the renders stored in the order they were added.
        Iterator openingOrder = openingDetails();
        while (openingOrder.hasNext()) {

            StyleEmulationPropertyRenderer renderer =
                    (StyleEmulationPropertyRenderer)openingOrder.next();
            if (renderer != null) {
                // Then emulate using that rule.
                renderer.apply(element, value);
            }
        }
    }

    /**
     * Return an iterator over the style emulation property renderers in
     * opening order, ie, the order in which they were added.
     */
    private Iterator openingDetails() {
        return emulationPropertyRenderers.iterator();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 ===========================================================================
*/
