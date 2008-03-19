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
package com.volantis.mcs.protocols.vdxml.menu;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.renderer.AbstractHrefMenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * A VDXML menu item renderer for links which splits the rendering of the link
 * into two separate parts. The active part of the link (the shortcut and href)
 * is rendered externally to the normal menu markup (as a non-visible RACCOURCI
 * element in a special buffer under the control of the protocol), and the style
 * associated with the link (if any) is rendered via a span element which is
 * output in the place that a link normally goes (in the normal menu markup).
 * <p>
 * Note that the stylistic rendering only applies is this is acting as an outer
 * renderer. Inner renderers do not need this.
 * <p>
 * Note that the text part of the link is normally handled separately anyway.
 *
 * @see VDXMLMenuItemRendererFactory
 */
class VDXMLExternalLinkMenuItemRenderer
        extends AbstractHrefMenuItemBracketingRenderer {

    /**
         * Used for logging
         */
        private static final LogDispatcher logger =
                LocalizationFactory.createLogger(
                            VDXMLExternalLinkMenuItemRenderer.class);

    /**
     * Callback to the protocol to actually render the RACCOURCI link.
     */
    private final DeprecatedExternalLinkOutput externalLinkOutput;

    /**
     * The menu item renderer to delegate to for rendering the style associated
     * with the menu.
     */
    private final MenuItemBracketingRenderer spanMenuItemRenderer;

    /**
     * Flag which indicates if a span was opened during the processing.
     * Will only ever be set if a span renderer was provided.
     */
    private boolean spanOpened;

    /**
     * Construct an instance of this class with the objects provided.
     *
     * @param externalLinkOutput Outputs the RACCOURCI markup.
     * @param spanMenuItemRenderer the menu item renderer to render the menu
     *      item's style, if this is an outer renderer. Null if this is an
     */
    public VDXMLExternalLinkMenuItemRenderer(
            DeprecatedExternalLinkOutput externalLinkOutput,
            MenuItemBracketingRenderer spanMenuItemRenderer) {

        this.externalLinkOutput = externalLinkOutput;
        this.spanMenuItemRenderer = spanMenuItemRenderer;
    }

    // Javadoc inherited.
    protected boolean open(OutputBuffer buffer, MenuItem item, String href)
            throws RendererException {

        try {
            String shortcutText = getTextFromReference(item.getShortcut());
            if (shortcutText != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("writing vdxml menu item link with " +
                            "shortcut '" + shortcutText + "' and href '" +
                            href + "'");
                }

                // Output the active part of the link associated with the menu
                // item.
                // This renders only the shortcut and the href, all the other
                // information associated with the menu item (such as it's
                // style) is ignored.
                // Also, it does not render to the buffer which was passed in
                // as the external link is rendered into a special buffer under
                // the control of the protocol.
                externalLinkOutput.outputExternalLink(shortcutText, href);

                if (spanMenuItemRenderer != null) {
                    // Output the style associated with the menu item.
                    // Since the link above was rendered externally to the
                    // normal menu, any style associated with the menu item
                    // which would normally be rendered with the link cannot be
                    // done there. So, we render a span out to the normal
                    // buffer which contains the style associated with the menu
                    // instead.
                    spanOpened = spanMenuItemRenderer.open(buffer, item);
                }

                return true;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("no shortcut for vdxml menu item link with " +
                            "href '" + href + "', ignoring");
                }
                return false;
            }
        } catch (ProtocolException e) {
            throw new RendererException(e);
        }
    }

    private String getTextFromReference(TextAssetReference reference) {
        return reference == null ? null : reference.getText(TextEncoding.PLAIN);
    }

    // Javadoc inherited.
    public void close(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        // No need to close the external link, it is atomic.

        if (spanMenuItemRenderer != null && spanOpened) {
            // Close the span for the menu item's style.
            spanMenuItemRenderer.close(buffer, item);
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

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 01-Oct-04	5635/3	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/
