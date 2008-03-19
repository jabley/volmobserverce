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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * This class provides a means of rendering a plain image as part of a menu
 * item.  It does not handle all of the niceties that are necessary around such
 * information to make it into a functional menu item.  Higher level menu
 * renderers should be used for this purpose, and they should delegate the lower
 * level rendering (of plain images) to this class.
 * <p>
 * This class can be used for both normal images and rollover images in those
 * protocols that do not support the concept of rollover images.
 * </p>
 */
public class DefaultImageMenuItemRenderer 
        extends AbstractMenuItemImageRenderer {

    /**
     * Used to render markup for image elements.
     */
    private final DeprecatedImageOutput imageOutput;

    /**
     * Construct an instance of this class with the object provided.
     *
     * @param imageOutput used to render markup for image elements.
     * @param provideAltText
     */
    public DefaultImageMenuItemRenderer(DeprecatedImageOutput imageOutput,
            boolean provideAltText) {

        super(provideAltText);

        this.imageOutput = imageOutput;
    }

    // Javadoc inherited.
    public MenuItemRenderedContent render(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        // Assume that nothing will be written.
        MenuItemRenderedContent renderedContent = MenuItemRenderedContent.NONE;

        // Give up straight away if there is no icon for this menu item.
        // Note that this will potentially "break" the enclosing renderer.
        MenuIcon icon = item.getLabel().getIcon();
        if (icon != null) {
            ImageAssetReference imageRef = icon.getNormalURL();

            // set the url this item points to
            try {
                // Extract the url from the image
                String imageUrl = imageRef.getURL();
                // If we were able to get one
                if (imageUrl != null) {

                    DOMOutputBuffer dom = (DOMOutputBuffer) buffer;
                    ImageAttributes attributes = new ImageAttributes();
                    // Stylistic properties
                    ElementDetails elementDetails = icon.getElementDetails();
                    if (elementDetails != null) {
                        attributes.setElementDetails(elementDetails);
                    }
                    // Image properties
                    attributes.setSrc(imageUrl);
                    attributes.setAltText(getAltText(item));
                    
                    // Extension point for subclasses.
                    addImageAttributes(icon, attributes);
                    
                    imageOutput.outputImage(dom, attributes);

                    // An image was written out.
                    renderedContent = MenuItemRenderedContent.IMAGE;
                }
            } catch (AssetReferenceException e) {
                throw new RendererException(e);
            } catch (ProtocolException pe) {
                throw new RendererException(pe);
            }
        }

        return renderedContent;
    }

   /**
     * Customise the attributes of the image being rendered.
     * <p>
     * This is an extension point for subclasses.
     *
     * @param icon the icon we are rendering.
     * @param attributes the attributes to add to. 
     * @throws RendererException if there was a rendering problem.
     * @throws AssetReferenceException if there was an asset problem.
     */    
    protected void addImageAttributes(MenuIcon icon, 
            ImageAttributes attributes) throws RendererException,
                AssetReferenceException {
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 10-May-04	4253/1	claire	VBM:2004051006 Fix invalid rollover component names NPE

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 ===========================================================================
*/
