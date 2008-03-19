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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleListRenderer.java,v 1.3 2002/06/29 01:04:51 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - The renderer for style list
 *                              values. 
 * 28-Apr-02    Allan           VBM:2002042404 - Removed extraneous space after
 *                              ','.
 * 28-Jun-02    Paul            VBM:2002051302 - Made a singleton.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleValue;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Render a style value consisting of a list of other values.
 */
public class StyleListRenderer {

    /**
     * Render a StyleList.
     *
     * @param value the StyleList to render
     * @param context the RendererContext within which to render the StyleList
     */
    public void render(StyleList value, RendererContext context)
            throws IOException {

        Writer writer = context.getWriter();

        List list = value.getList();
        int size = list.size();
        for (int i = 0; i < size; i += 1) {
            StyleValue v = (StyleValue) list.get(i);
            if (i != 0) {
                writer.write(context.getListSeparator());
            }
            context.renderValue(v);
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
