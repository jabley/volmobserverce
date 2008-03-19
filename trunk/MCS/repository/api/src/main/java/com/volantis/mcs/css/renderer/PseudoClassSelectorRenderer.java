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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.Selector;

import java.io.IOException;
import java.io.Writer;

/**
 * A renderer for CSS pseudo class selectors.
 */
public class PseudoClassSelectorRenderer implements SelectorRenderer {
    // Javadoc inherited
    public void render(Selector selector, RendererContext context) throws IOException {
        PseudoClassSelector pseudoElement = (PseudoClassSelector) selector;
        Writer writer = context.getWriter();

        if (PseudoClassTypeEnum.INVALID.equals(
                pseudoElement.getPseudoClassType())) {
            writer.write(pseudoElement.getInvalidPseudoClass());
        } else {
            writer.write(':');
            writer.write(pseudoElement.getPseudoClassType().getType());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9412/3	adrianj	VBM:2005083007 CSS renderer using new model

 ===========================================================================
*/
