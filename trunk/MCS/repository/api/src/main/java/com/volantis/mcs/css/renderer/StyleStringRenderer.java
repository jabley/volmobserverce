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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleStringRenderer.java,v 1.2 2002/06/29 01:04:51 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - The renderer for style
 *                              string values.
 * 28-Jun-02    Paul            VBM:2002051302 - Made a singleton.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.utilities.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Render a style value representing string.
 */
public class StyleStringRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static StyleStringRenderer singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new StyleStringRenderer();
    }

    /**
     * Get the single allowable instance of this class.
     * @return The single allowable instance of this class.
     */
    public static StyleStringRenderer getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StyleStringRenderer() {
    }

    /**
     * Render a StyleString.
     *
     * @param value   the StyleString to render
     * @param context the RendererContext within which to render the
     *                StyleString
     */
    public void render(StyleString value, RendererContext context)
            throws IOException {

        String string = value.getString();

        PrintWriter writer = context.getPrintWriter();
        writer.print(StringUtils.addQuotesAndEscape(string));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, also fixed string rendering as well

 07-Jun-05	8637/2	pcameron	VBM:2005050402 Fixed quoting of font-family values

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
