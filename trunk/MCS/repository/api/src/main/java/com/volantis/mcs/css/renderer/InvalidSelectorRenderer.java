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
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.InvalidSelector;

import java.io.IOException;
import java.io.Writer;

/**
 * Render the contents of an invalid selector as the unaltered text content of
 * the invalid portion of CSS.
 */
public class InvalidSelectorRenderer implements SelectorRenderer {
    // Javadoc inherited
    public void render(Selector selector, RendererContext context) throws IOException {
        InvalidSelector invalid = (InvalidSelector) selector;
        Writer writer = context.getWriter();
        writer.write(invalid.getText());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
