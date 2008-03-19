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

import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.PseudoElementSelector;

import java.io.IOException;
import java.io.Writer;

/**
 * A renderer for CSS pseudo element selectors.
 */
public class PseudoElementSelectorRenderer implements SelectorRenderer {
    // Javadoc inherited
    public void render(Selector selector, RendererContext context) throws IOException {
        PseudoElementSelector pseudoElement = (PseudoElementSelector) selector;
        Writer writer = context.getWriter();

        PseudoElementTypeEnum type = pseudoElement.getPseudoElementType();
        if (type == PseudoElementTypeEnum.INVALID) {
            writer.write("::");
            writer.write(pseudoElement.getInvalidPseudoElement());
        } else if (type == PseudoElementTypeEnum.FIRST_LETTER ||
                   type == PseudoElementTypeEnum.FIRST_LINE) {
            writer.write(':');
            writer.write(type.getType());
        } else {
            writer.write("::");
            writer.write(type.getType());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9412/3	adrianj	VBM:2005083007 CSS renderer using new model

 ===========================================================================
*/
