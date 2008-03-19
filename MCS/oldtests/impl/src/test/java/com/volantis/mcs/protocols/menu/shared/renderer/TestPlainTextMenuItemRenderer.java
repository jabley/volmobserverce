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

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuText;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.styling.Styles;

public class TestPlainTextMenuItemRenderer
        implements MenuItemComponentRenderer {

    /**
     * The root element of the last content we created during rendering.
     */
    private Element lastElement;

    public MenuItemRenderedContent render(OutputBuffer buffer, MenuItem item)
        throws RendererException {

        MenuLabel label = item.getLabel();
        MenuText text = label.getText();
        ElementDetails elementDetails = text.getElementDetails();
        Styles styles = null;
        if (elementDetails != null) {
            styles = elementDetails.getStyles();
        }

        DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;
        Element element = outputBuffer.openElement("plain-text");
        if (styles != null) {
            element.setStyles(styles);
        }
        DOMOutputBuffer textOutputBuffer = (DOMOutputBuffer) text.getText();
        outputBuffer.addOutputBuffer(textOutputBuffer);
        outputBuffer.closeElement(element);
        lastElement = element;

        return MenuItemRenderedContent.TEXT;
    }

    /**
     * Returns the text of the last rendered image.
     */
    public String getLastRenderedText() {
        try {
            return DOMUtilities.toString(lastElement);
        } catch (Exception ex) {
            throw new ExtendedRuntimeException(ex);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 21-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 ===========================================================================
*/
