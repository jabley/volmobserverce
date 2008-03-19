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

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * A renderer for CSS selector sequences.
 */
public class SelectorSequenceRenderer
        implements SelectorRenderer {

    // Javadoc inherited
    public void render(Selector selector, RendererContext context)
            throws IOException {

        SelectorSequence sequence = (SelectorSequence) selector;
        List selectors = sequence.getSelectors();

        if (selectors != null && selectors.size() > 0) {
            Iterator it = selectors.iterator();
            while (it.hasNext()) {
                context.renderSelector((Selector) it.next());
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 ===========================================================================
*/
