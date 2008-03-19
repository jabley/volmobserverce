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

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.KeywordMapper;

import java.io.IOException;
import java.io.Writer;

/**
 * Render a style value representing a fraction.
 */
public class StyleFractionRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static StyleFractionRenderer singleton;

    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new StyleFractionRenderer ();
    }

    /**
     * Get the single allowable instance of this class.
     * @return The single allowable instance of this class.
     */
    public static StyleFractionRenderer getSingleton () {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StyleFractionRenderer () {
    }

    /**
     * Render a StyleFraction.
     * @param value the StyleFraction to render
     * @param context the RendererContext within which to render the StyleFraction
     */
    public void render(StyleFraction value, RendererContext context)
        throws IOException {

        // Retrieve the appropriate keyword renderers.
        KeywordMapper numeratorKeywordMapper = context.getFirstKeywordMapper();
        KeywordMapper denominatorKeywordMapper =
                context.getSecondKeywordMapper();

        // Render the numerator component.
        StyleValue numerator = value.getNumerator();
        context.setKeywordMapper(numeratorKeywordMapper);
        context.renderValue(numerator);

        // Render the denominator if required.
        StyleValue denominator = value.getDenominator();
        if (denominator != null) {
            Writer writer = context.getWriter();
            writer.write('/');
            context.setKeywordMapper(denominatorKeywordMapper);
            context.renderValue(denominator);
        }
    }
}
