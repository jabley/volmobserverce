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

import com.volantis.mcs.protocols.menu.shared.renderer.AbstractMenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultSpanMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedSpanOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemComponentRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.NumericShortcutEmulationRenderer;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * A menu item renderer factory for VDXML menus.
 */
public class VDXMLMenuItemRendererFactory
        extends AbstractMenuItemRendererFactory {

    /**
     * Renders "span" like markup around other markup, potentially used to
     * add stylistic values.
     */
    private final DeprecatedSpanOutput spanOutput;

    /**
     * Renders the RACCOURCI markup.
     */
    private final DeprecatedExternalLinkOutput externalLinkOutput;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param context Contains contextual information.
     * @param outputLocator Contains references to markup generators.
     * @param externalLinkOutput Renderer for the RACCOURCI link.
     */
    public VDXMLMenuItemRendererFactory(RendererContext context,
            DeprecatedOutputLocator outputLocator,
            DeprecatedExternalLinkOutput externalLinkOutput) {
        
        super(context.getOutputBufferFactory());

        this.spanOutput = outputLocator.getSpanOutput();
        this.externalLinkOutput = externalLinkOutput;
    }

    // Javadoc inherited.
    protected MenuItemComponentRenderer createPlainImageRendererImpl(
            boolean provideAltText) {

        // VDXML doesn't support image rendering for menus.
        // So return null to indicate this.
        return null;
    }
    
    // Javadoc inherited.
    public MenuItemBracketingRenderer createInnerLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {

        // The active part of links are always rendered externally for VDXML,
        // and the menu item style must not be rendered for an inner link, as
        // it will be rendered by the outer renderer instead.
        return new VDXMLExternalLinkMenuItemRenderer(externalLinkOutput, null);
    }

    // Javadoc inherited.
    public MenuItemBracketingRenderer createOuterLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {

        // The active part of links are always rendered externally for VDXML,
        // and the menu item style must be rendered (via a span) for an outer
        // link.
        return new VDXMLExternalLinkMenuItemRenderer(externalLinkOutput,
                new DefaultSpanMenuItemRenderer(spanOutput));
    }

    // Javadoc inherited.
    public MenuItemBracketingRenderer createOuterRenderer() {

        // Render the menu item style outside the entire menu item if we are
        // going to render an inner link.
        return new DefaultSpanMenuItemRenderer(spanOutput);
    }

    // JavaDoc inherited
    public NumericShortcutEmulationRenderer 
            createNumericShortcutEmulationRenderer() {

        // No numeric shortcut emulation. Might be nice in future though...
        return null;
    }

    // JavaDoc inherited
    public MenuItemComponentRenderer createPlainTextRenderer() {

        // Return a text menu item renderer which adds the shortcut before
        // the text for the item so that the user knows what to enter in
        // order to trigger the externally rendered link.
        return new VDXMLTextMenuItemRenderer(spanOutput);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/3	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/
