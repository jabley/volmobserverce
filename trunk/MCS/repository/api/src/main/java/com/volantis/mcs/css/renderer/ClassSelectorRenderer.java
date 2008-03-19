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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/css2/ClassSelectorRenderer.java,v 1.1 2002/04/27 18:26:15 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Allan           VBM:2002042404 - Created. A renderer for 
 *                              css2 class selectors.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.ClassSelector;

import java.io.IOException;
import java.io.Writer;

/**
 * A renderer for CSS Class selectors.
 */
public class ClassSelectorRenderer implements SelectorRenderer {
    /** 
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Render a ClassSelector
     * @param selector the ClassSelector to render
     * @param context the RendererContext used to render the selector
     */
    public void render(Selector selector,
                       RendererContext context)
        throws IOException {

        ClassSelector classSelector = (ClassSelector) selector;
        Writer writer = context.getWriter();
        writer.write('.');
        writer.write(classSelector.getCssClass());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9412/1	adrianj	VBM:2005083007 CSS renderer using new model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
