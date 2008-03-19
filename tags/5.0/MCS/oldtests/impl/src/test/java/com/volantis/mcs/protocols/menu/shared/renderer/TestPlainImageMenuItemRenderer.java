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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.styling.Styles;

class TestPlainImageMenuItemRenderer
        extends AbstractMenuItemImageRenderer {

    public TestPlainImageMenuItemRenderer(boolean provideAltText) {
        super (provideAltText);
    }

    public MenuItemRenderedContent render(OutputBuffer buffer, MenuItem item)
        throws RendererException {

        MenuLabel label = item.getLabel();
        MenuIcon icon = label.getIcon();
        ElementDetails elementDetails = icon.getElementDetails();
        Styles styles = null;
        if (elementDetails != null) {
            styles = elementDetails.getStyles();
        }

        DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;
        Element element = outputBuffer.addElement("plain-image");
        if (styles != null) {
            element.setStyles(styles);
        }

        try {
            String url = icon.getNormalURL().getURL();
            element.setAttribute("src", url);
            String altText = getAltText(item);
            if (altText != null) {
                element.setAttribute("alt", altText);
            }
        } catch (AssetReferenceException e) {
            throw new RendererException(e);
        }

        return MenuItemRenderedContent.TEXT;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 21-Apr-04	3681/2	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 08-Apr-04	3514/3	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 ===========================================================================
*/
