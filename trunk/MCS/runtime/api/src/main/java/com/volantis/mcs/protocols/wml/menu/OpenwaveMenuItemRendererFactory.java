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

import com.volantis.mcs.protocols.menu.shared.renderer.AbstractMenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultImageMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultStyledPlainTextMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedImageOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedSpanOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemComponentRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.NumericShortcutEmulationRenderer;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * A menu item renderer factory for Openwave menus.
 */
public class OpenwaveMenuItemRendererFactory
        extends AbstractMenuItemRendererFactory {

    /**
     * A means of rendering an image utilizing protocol specific information.
     */
    private final DeprecatedImageOutput imageOutput;

    /**
     * Renders the option tag around a menu item.
     */
    private MenuItemBracketingRenderer optionRenderer;

    /**
     * Renders "span" like markup around other markup, potentially used to
     * add stylistic values
     */
    private final DeprecatedSpanOutput spanOutput;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param context       Contains contextual information.
     * @param outputLocator Contains references to markup generators.
     */
    public OpenwaveMenuItemRendererFactory(
            RendererContext context,
            DeprecatedOutputLocator outputLocator) {

        super(context.getOutputBufferFactory());

        this.imageOutput = outputLocator.getImageOutput();
        this.spanOutput = outputLocator.getSpanOutput();
    }

    // Javadoc inherited.
    protected MenuItemComponentRenderer createPlainImageRendererImpl(
            boolean provideAltText) {

        return new DefaultImageMenuItemRenderer(imageOutput, provideAltText);
    }

    // Javadoc inherited.
    public MenuItemBracketingRenderer createInnerLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {

        // We don't support explicit control of the link rendering.
        // So return a renderer that does nothing.
        return MenuItemBracketingRenderer.NULL;
    }

    // Javadoc inherited.
    public MenuItemBracketingRenderer createOuterLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {

        // We don't support explicit control of the link rendering.
        // We always render the content in an option tag.
        return createOptionRenderer();
    }

    // Javadoc inherited.
    public MenuItemBracketingRenderer createOuterRenderer() {

        // We always render the content in an option tag.
        return createOptionRenderer();
    }

    // JavaDoc inherited
    public NumericShortcutEmulationRenderer
            createNumericShortcutEmulationRenderer() {

        return null;
    }

    // JavaDoc inherited
    public MenuItemComponentRenderer createPlainTextRenderer() {
        return new DefaultStyledPlainTextMenuItemRenderer(spanOutput);
    }

    /**
     * Create a renderer for the option tag.
     *
     * @return the created renderer.
     */
    private MenuItemBracketingRenderer createOptionRenderer() {
        if (optionRenderer == null) {
            optionRenderer = new OpenwaveOptionMenuItemRenderer();
        }
        return optionRenderer;
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

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 06-May-04	4153/3	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 30-Apr-04	4124/1	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 29-Apr-04	4013/5	pduffin	VBM:2004042210 Restructure menu item renderers

 27-Apr-04	4025/3	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 26-Apr-04	3920/5	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 21-Apr-04	3681/2	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 20-Apr-04	3715/1	claire	VBM:2004040201 Improving WML menu item renderers

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 ===========================================================================
*/
