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
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;

import java.io.IOException;

/**
 * Render all the border related shorthands and properties.
 */
public class BorderRenderer
        extends AbstractShorthandRenderer {

    /**
     * All the border related shorthands.
     */
    private StyleShorthand[] BORDER_SHORTHANDS = new StyleShorthand[]{
        StyleShorthands.BORDER,

        StyleShorthands.BORDER_TOP,
        StyleShorthands.BORDER_RIGHT,
        StyleShorthands.BORDER_BOTTOM,
        StyleShorthands.BORDER_LEFT,

        StyleShorthands.BORDER_COLOR,
        StyleShorthands.BORDER_STYLE,
        StyleShorthands.BORDER_WIDTH,
    };

    // Javadoc inherited.
    public void render(StyleProperties properties, RendererContext context)
            throws IOException {

        // Iterate over all the shorthands.
        for (int i = 0; i < BORDER_SHORTHANDS.length; i++) {
            StyleShorthand shorthand = BORDER_SHORTHANDS[i];

            renderShorthand(context, shorthand, properties);
        }

        // Render any remaining properties.
        renderProperties(properties, context, PropertyGroups.BORDER_PROPERTIES);
    }
}
