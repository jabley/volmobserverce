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

package com.volantis.mcs.css.renderer.shorthand;

import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.styling.properties.StyleProperty;

import java.io.IOException;

/**
 * Generic renderer for simple shorthands.
 */
public class ShorthandPropertyRenderer
        extends AbstractShorthandRenderer {

    /**
     * The shorthand to render.
     */
    private final StyleShorthand shorthand;

    /**
     * Initialise.
     *
     * @param shorthand The shorthand to render.
     */
    public ShorthandPropertyRenderer(StyleShorthand shorthand) {
        this.shorthand = shorthand;
    }

    // Javadoc inherited.
    public void render(StyleProperties properties, RendererContext context)
            throws IOException {

        if (!renderShorthand(context, shorthand, properties)) {
            final StyleProperty[] group = shorthand.getStandardProperties();
            renderProperties(properties, context, group);
        }
    }
}
