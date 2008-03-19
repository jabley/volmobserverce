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
package com.volantis.mcs.protocols.html.menu;

import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultImageMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedImageOutput;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A menu item renderer for XHTML Full which renders a "rollover image" as per
 * {@link com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory#createRolloverImageRenderer}.
 * <p>
 * This renders the rollover image similarly to
 * {@link DefaultImageMenuItemRenderer} except that it adds javascript
 * event attributes to change the normal image to the over image and back
 * again as the user moves the mouse over and away from the image.
 */
final class XHTMLFullRolloverImageMenuItemRenderer extends
        DefaultImageMenuItemRenderer {

    /**
     * Object which can render event attributes for us.
     */
    private final DeprecatedEventAttributeUpdater eventRenderer;

    /**
     * Construct an instance of this class using the style attribute renderer
     * and event attribute renderer provided.
     *
     * @param imageOutput Outputs protocol specific images.
     * @param provideAltText
     * @param eventRenderer used to render the event attributes of the image.
     */
    public XHTMLFullRolloverImageMenuItemRenderer(
            DeprecatedImageOutput imageOutput, boolean provideAltText, 
            DeprecatedEventAttributeUpdater eventRenderer) {
        super(imageOutput, provideAltText);
        this.eventRenderer = eventRenderer;
    }

    // Javadoc inherited.
    protected void addImageAttributes(MenuIcon icon, ImageAttributes attributes)
            throws RendererException, AssetReferenceException {

        // Add events to swap the normal and over image when the user moves
        // their mouse over or out of the image we are rendering.
        
        // NOTE: The old XHTMLFull.doRolloverImage used to fix border="0"
        // This is a bogus way of doing this so it has been left out till
        // such time as we have the time to fix it properly.
        // @todo implement border=0 for devices that require it.
        
        ImageAssetReference overReference = icon.getOverURL();
        if (overReference != null) {
            String overUrl = overReference.getURL();
            if (overUrl != null) {
                // Normal url must be non null.
                String normalUrl = icon.getNormalURL().getURL();
                // These urls are in external form so we shouldn't need to
                // quote them (hopefully).
                eventRenderer.mergeEventAttribute(attributes,
                        EventConstants.ON_MOUSE_OUT,
                        "this.src='" + normalUrl + "'");
                eventRenderer.mergeEventAttribute(attributes,
                        EventConstants.ON_MOUSE_OVER,
                        "this.src='" + overUrl + "'");
            }
        }
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

 06-May-04	4153/4	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring (review feedback)

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/4	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 26-Apr-04	3920/2	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
