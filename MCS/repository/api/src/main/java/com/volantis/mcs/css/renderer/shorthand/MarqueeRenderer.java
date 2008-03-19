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

package com.volantis.mcs.css.renderer.shorthand;

import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.KeywordMapper;

import java.io.IOException;
import java.io.Writer;

/**
 * Renders the marquee shorthand properties, remapping the direction keywords
 * if necessary.
 */
public class MarqueeRenderer extends ShorthandPropertyRenderer {

    /**
     * Initialize a new instance.
     */
    public MarqueeRenderer() {
        super(StyleShorthands.MARQUEE);
    }

    // Javadoc inherited.
    public void renderValue(StyleValue value, RendererContext context)
            throws IOException {
        if (value instanceof StyleKeyword) {
            renderKeyword((StyleKeyword)value, context);
        } else {
            super.renderValue(value, context);
        }
    }

    /**
     * Render a {@link StyleKeyword} value, remapping the direction keywords
     * to their external form if necessary.
     *
     * @param value
     * @param context
     * @throws IOException
     */
    public void renderKeyword(StyleKeyword value, RendererContext context)
            throws IOException {
        final String name = value.getName();
        // I decided to do this specifically for marquee (rather than
        // generalise it) because this is the only shorthand that needs to
        // remap keywords, and supporting it properly would make the
        // optimizers slower, and they are really intensively used. It
        // probably doesn't make a huge difference, but it's worth doing
        // this way until at least one other shorthand needs it.
        if("left".equals(name) || "right".equals(name)) {
            final KeywordMapper mapper = context.getKeywordMapper(
                    StylePropertyDetails.MCS_MARQUEE_DIRECTION);
            Writer writer = context.getWriter();
            writer.write(mapper.getExternalString(value));
        } else {
            super.renderValue(value, context);
        }
    }
}
