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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class provides a default means of rendering an image as a menu item 
 * group separator.
 */
public final class DefaultImageMenuItemGroupSeparatorRenderer
        implements SeparatorRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DefaultImageMenuItemGroupSeparatorRenderer.class);

    /**
     * The asset reference that resolves to the image to be output as a
     * separator image.
     */
    private final ImageAssetReference image;

    /**
     * The image renderer to delegate the actual rendering of the object to.
     */
    private final DeprecatedImageOutput imageOutput;

    /**
     * Initialise a new instance of this separator renderer using the supplied
     * value.
     *
     * @param image         An asset reference to the image to be output as a
     *                      separator image.
     * @param imageOutput The object to delegate actual low level image
     *                      rendering to.
     */
    public DefaultImageMenuItemGroupSeparatorRenderer(ImageAssetReference image,
            DeprecatedImageOutput imageOutput) {
        this.image = image;
        this.imageOutput = imageOutput;
    }

    // JavaDoc inherited
    public void render(OutputBuffer buffer) throws RendererException {
        // Get the output buffer
        DOMOutputBuffer dom = (DOMOutputBuffer)buffer;

        try {
            // Extract necessary info from the image reference
            String reference = image.getURL();

            if (reference != null) {
                // There is a url to use
                ImageAttributes attributes = new ImageAttributes();
                attributes.setSrc(reference);
                imageOutput.outputImage(dom, attributes);
            } else {
                logger.warn("render-reference-error", new Object[]{
                        ImageAssetReference.class.toString(),
                        image});
                // There is no text fallback here because the existing code
                // does not perform any.  Since images are now asset references
                // this could be supported in future.

                // No alt text is produced either because whilst it was
                // nominally handled with existing separator code, the actual
                // value of the string was never set up.
            }
        } catch (AssetReferenceException are) {
            logger.error("unexpected-exception", are);
            // Wrap and propogate the exception
            throw new RendererException(are);
        } catch (ProtocolException pe) {
            logger.error("unexpected-exception", pe);
            // Wrap and propogate the exception
            throw new RendererException(pe);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/3	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/1	claire	VBM:2004042204 Implemented remaining required WML renderers

 ===========================================================================
*/
