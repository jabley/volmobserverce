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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;

/**
 * This class provides a means of rendering the (potentially) styled text part
 * of menu item. It does not handle all of the niceties that are necessary
 * around such information to make it into a functional menu item.  Higher
 * level menu renderers should be used for this purpose, and they should
 * delegate the lower level rendering (of potentially styled text items) to
 * this class.
 */
public class DefaultStyledPlainTextMenuItemRenderer
        extends AbstractStyledTextMenuItemRenderer {

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param spanOutput the outputter for span markup required when style is
     *                   applied to the text
     */
    public DefaultStyledPlainTextMenuItemRenderer(
            DeprecatedSpanOutput spanOutput) {

        super(spanOutput);
    }

    // Javadoc inherited.
    protected void writeMenuTextToBuffer(OutputBuffer output, MenuItem item) {

        // Extract the text from the menu item.
        OutputBuffer text = item.getLabel().getText().getText();

        // Write it into the output buffer.
        output.transferContentsFrom(text);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/2	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 ===========================================================================
*/
