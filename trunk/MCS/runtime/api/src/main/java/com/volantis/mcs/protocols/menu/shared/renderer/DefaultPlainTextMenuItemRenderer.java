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
import com.volantis.mcs.protocols.renderer.RendererException;


/**
 * This class provides a means of rendering the plain text part of menu item.
 * It does not handle all of the niceties that are necessary around such
 * information to make it into a functional menu item.  Higher level menu
 * renderers should be used for this purpose, and they should delegate the
 * lower level rendering (of plain text items) to this class.
 *
 * <p><strong>NOTE:</strong> This renderer is available for those protocols
 * that actually do nothing in their span rendering; this provides a garbage-
 * optimal way of handling these protocols since a singleton instance is
 * available for use in the required factory(ies). All protocols that actually
 * support some form of span markup should use the {@link
 * DefaultStyledPlainTextMenuItemRenderer} instead.</p>
 */
public class DefaultPlainTextMenuItemRenderer
        implements MenuItemComponentRenderer {

    /**
     * A shared menu item renderer for the text part of the menu item (since
     * it also has no state).
     */
    public static final DefaultPlainTextMenuItemRenderer DEFAULT_INSTANCE
            = new DefaultPlainTextMenuItemRenderer();

    /**
     * Initializes the new instance.
     */
    public DefaultPlainTextMenuItemRenderer() {
    }

    // JavaDoc inherited
    public MenuItemRenderedContent render(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        // Get the information from the menu item to output
        OutputBuffer text = item.getLabel().getText().getText();

        // Render the information using the writer
        buffer.transferContentsFrom(text);

        return MenuItemRenderedContent.TEXT;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 20-Apr-04	3715/6	claire	VBM:2004040201 Improving WML menu item renderers

 16-Apr-04	3715/1	claire	VBM:2004040201 Enhanced Menu: WML Menu Item Renderers

 ===========================================================================
*/
