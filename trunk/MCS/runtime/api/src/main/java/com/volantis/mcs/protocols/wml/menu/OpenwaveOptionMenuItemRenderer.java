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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.renderer.AbstractHrefMenuItemBracketingRenderer;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * A menu item renderer which renders the option tag required for an Openwave
 * menu item.
 * <p/>
 * <p>This class is stateless so a single instance can be used from multiple
 * threads.</p>
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because select/option can only contain PCDATA, so even if we rendered it
 * it would be invalid and (almost always) removed by the transformer. The
 * only time it would not be removed would be if it was intercepted by the
 * "emulate emphasis" processing in the transformer and translated into just
 * text before and after (eg "[" & "]") - and it's not worth implementing just
 * for this edge condition.
 *
 * @see OpenwaveMenuItemRendererFactory
 */
class OpenwaveOptionMenuItemRenderer
        extends AbstractHrefMenuItemBracketingRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    OpenwaveOptionMenuItemRenderer.class);

    /**
     * Construct an instance of this class with the objects provided.
     */
    public OpenwaveOptionMenuItemRenderer() {
    }

    // Javadoc inherited.
    protected boolean open(OutputBuffer buffer, MenuItem item, String href)
            throws RendererException {

        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

        Element element = dom.openElement("option");

        // Write out the attributes.
        if (item.getTitle() != null) {
            element.setAttribute("title", item.getTitle());
        }
        // OK, get hold of the href that this menu item points to.
        element.setAttribute("onpick", href);

        // Tell the user a little of what we are doing.
        if (logger.isDebugEnabled()) {
            logger.debug("writing openwave menu item with href " +
                         href);
        }

        return true;
    }

    // Javadoc inherited.
    public void close(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

        dom.closeElement("option");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/4	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 ===========================================================================
*/
