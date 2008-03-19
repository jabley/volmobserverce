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
import com.volantis.mcs.protocols.menu.model.MenuText;

/**
 * Base for all image renderers.
 *
 * <p>Provides support for handling alt text.</p>
 */
public abstract class AbstractMenuItemImageRenderer
        implements MenuItemComponentRenderer {

    /**
     * Indicates whether alternate text should be provided.
     */
    private final boolean provideAltText;

    /**
     * Initialise.
     * @param provideAltText Indicates whether alternate text should be
     * provided.
     */
    public AbstractMenuItemImageRenderer(boolean provideAltText) {
        this.provideAltText = provideAltText;
    }

    /**
     * Get the alternate text for the menu item.
     *
     * @param item The menu item whose text should be used as the alternate
     * text.
     * @return The menu text, or null if alternate text should not be provided.
     */
    protected String getAltText(MenuItem item) {
        String altText = null;
        if (provideAltText) {
            MenuText menuText = item.getLabel().getText();
            OutputBuffer text = menuText.getText();
            altText = text.stringValue();
        }
        return altText;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
