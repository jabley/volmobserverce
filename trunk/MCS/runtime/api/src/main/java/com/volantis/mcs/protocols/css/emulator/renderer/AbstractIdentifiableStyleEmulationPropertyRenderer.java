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

/**
 * This class is responsible for providing a wrapper for instances of
 * {@link StyleEmulationPropertyRenderer} allowing them to be identified
 * as one of three types:
 * <ul>
 *  <li> {@link AttributeAndOrElementStyleEmulationPropertyRenderer} </li>
 *  <li> {@link AttributeAndOrElementStyleEmulationPropertyRenderer} </li>
 *  <li> {@link ElementOnlyStyleEmulationPropertyRenderer} </li>
 * </ul>
 * <p>
 * Identification of the different types of style emuation renderers is necessary
 * as we MUST ensure that they are processed in the current order.  See the
 * class comment in {@link DefaultStyleEmulationPropertiesRenderer} for more
 * details.
 */
public class AbstractIdentifiableStyleEmulationPropertyRenderer
    implements StyleEmulationPropertyRenderer {

    /**
     * The renderer being wrapped so that it can be identified as being one
     * of three types: {@link AttributeAndOrElementStyleEmulationPropertyRenderer},
     * {@link AttributeAndOrElementStyleEmulationPropertyRenderer} or
     * {@link ElementOnlyStyleEmulationPropertyRenderer}.
     * See the class comment for why this is useful.
     */
    private final StyleEmulationPropertyRenderer renderer;

    /**
     * Creates a new instance initialised with the supplied parameters.
     *
     * @param renderer the style emulation renderer to be wrapped.
     */
    protected AbstractIdentifiableStyleEmulationPropertyRenderer(
            StyleEmulationPropertyRenderer renderer) {
        this.renderer = renderer;
    }

    // Javadoc inherited.
    public void apply(Element element, StyleValue value) {
        renderer.apply(element, value);
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
