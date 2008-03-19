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
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleList;

import java.io.IOException;

/**
 * An abstract renderer for counter-* properties.
 * <p>
 * These include a StyleList which must be rendered with a space separator
 * rather than the default comma separator which style lists are normally
 * rendered with.
 * <p>
 * This is basically a hack but is the best we can do with the existing
 * assumptions made in the rendering infrastructure.
 *
 * @todo: enhance infrastructure to support different list renderings properly.
 */
public abstract class GenericCounterRenderer extends PropertyRenderer {

    // Javadoc inherited.
    public void renderValue(StyleValue value, RendererContext context)
            throws IOException {

        context.setListSeparator(" ");

        try {
            // Render the value.
            super.renderValue(value, context);
        } finally {
            context.resetListSeparator();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
