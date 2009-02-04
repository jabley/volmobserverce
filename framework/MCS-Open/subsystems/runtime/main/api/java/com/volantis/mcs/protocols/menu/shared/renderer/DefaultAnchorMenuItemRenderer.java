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

import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.EventSupport;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * This renders a menu item as an anchor link. It will render a complete link 
 * and any contained content. It can also optionally handle numeric emulation.
 */ 
public final class DefaultAnchorMenuItemRenderer
        extends AbstractHrefMenuItemBracketingRenderer {

    /**
     * Reference to an object that is capable of generating href markup based
     * on protocol "knowledge"
     */
    private final DeprecatedAnchorOutput anchorOutput;

    /**
     * The anchor attributes used during the rendering of {@link #open} and
     * {@link #close}.
     */
    private AnchorAttributes attributes;

    /**
     * True if we are rendering an outer link, false if we are rendering an 
     * inner link.
     */ 
    private final boolean outer;

    /**
     * Used for emulating numeric shortcuts, may be null to indicate no 
     * emulation required.
     */ 
    private final NumericShortcutEmulationRenderer emulation;
    
    /**
     * Create a new initialised instance of this renderer.
     *
     * @param anchorOutput A protocol specific means of rendering an anchor.
     * @param outer true if this an outer link.
     * @param emulation  A renderer to use for emulating numeric shortcuts, may
     *                   be null to indicate no emulation required.
     */
    public DefaultAnchorMenuItemRenderer(
            DeprecatedAnchorOutput anchorOutput, boolean outer,
            NumericShortcutEmulationRenderer emulation) {

        this.anchorOutput = anchorOutput;
        this.outer = outer;
        this.emulation = emulation;
    }

    // JavaDoc inherited
    protected boolean open(OutputBuffer buffer, MenuItem item, String href)
            throws RendererException {

        try {
            // Extract the output buffer for use in createElement
            DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;

            // Numeric emulation as the emulation object is not null
            if (emulation != null) {
                // start the emulation wrapping element
                emulation.start(outputBuffer);
            }

            // Create the anchor attributes
            attributes = new AnchorAttributes();

            // Stylistic properties
            ElementDetails elementDetails = item.getElementDetails();
            if (elementDetails != null && outer) {
                attributes.setElementDetails(elementDetails);
            }

            // Events
            EventSupport.setEvents(item, attributes);

            // Link properties
            attributes.setHref(href);
            attributes.setShortcut(item.getShortcut());
            attributes.setTitle(item.getTitle());

            // Numeric emulation as the emulation object is not null
            if (emulation != null) {
                // Add the dummy shortcut.
                attributes.setShortcut(emulation.getShortcut());
            }

            // Ensure the segment is set for montage layouts/pages
            attributes.setSegment(item.getSegment());
            
            // Create the actual element
            anchorOutput.openAnchor(outputBuffer, attributes);

            // Add the dummy access key prefix to the text if necessary.
            if (emulation != null) {
                emulation.outputPrefix(outputBuffer);
            }
        } catch (ProtocolException pe) {
            throw new RendererException(pe);
        }

        return true;
    }

    // JavaDoc inherited
    public void close(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        // Extract the output buffer for use in createElement
        DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;

        // Close the anchor rendering
        anchorOutput.closeAnchor(outputBuffer, attributes);
        
        // Numeric emulation as the emulation object is not null
        if (emulation != null) {
            emulation.end(outputBuffer);
        }
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

 26-Oct-04	5980/1	claire	VBM:2004100104 mergevbm: Segment attribute rendered on menu items

 26-Oct-04	5966/1	claire	VBM:2004100104 Segment attribute rendered on menu items

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 14-May-04	4388/1	philws	VBM:2004050405 Handle events on menu items

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 ===========================================================================
*/
