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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StylePairRenderer.java,v 1.3 2002/10/02 11:07:09 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - Created. The renderer for
 *                              style pair values. 
 * 28-Jun-02    Paul            VBM:2002051302 - Made a singleton.
 * 01-Oct-02    Ian             VBM:2002092509 - Enabled rendering of a single
 *                              value within a pair.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.KeywordMapper;

import java.io.IOException;
import java.io.Writer;

/**
 * Render a style value consisting of a pair of values.
 */
public class StylePairRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static StylePairRenderer singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new StylePairRenderer();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static StylePairRenderer getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StylePairRenderer() {
    }

    /**
     * Render a StylePair.
     *
     * @param value   the StylePair to render
     * @param context the RendererContext within which to render the StylePair
     */
    public void render(StylePair value, RendererContext context)
            throws IOException {

        KeywordMapper firstKeywordMapper = context.getFirstKeywordMapper();
        KeywordMapper secondKeywordMapper = context.getSecondKeywordMapper();

        StyleValue first = value.getFirst();
        context.setKeywordMapper(firstKeywordMapper);
        context.renderValue(first);

        StyleValue second = value.getSecond();
        if (second != null && !second.equals(first)) {
            Writer writer = context.getWriter();
            writer.write(' ');
            context.setKeywordMapper(secondKeywordMapper);
            context.renderValue(second);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, also fixed string rendering as well

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
