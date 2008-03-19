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

import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.renderer.DOMRendererContext;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * A factory used by the
 * {@link com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuItemRendererSelector}
 * to create the different forms of MenuItemRenderer that it needs.
 */
public class DefaultMenuItemRendererFactory
        extends AbstractMenuItemRendererFactory {

     /**
     * A means of generating a span utilizing protocol specific information.
     */
    private final DeprecatedSpanOutput spanOutput;

    /**
     * A means of generating an image utilizing protocol specific information.
     */
    protected final DeprecatedImageOutput imageOutput;

    /**
     * A means of generating a href link using protocol specific information.
     */
    private final DeprecatedAnchorOutput anchorOutput;

    /**
     * The menu module customisation.
     */
    private final MenuModuleCustomisation customisation;

    private final RendererContext rendererContext;

    /**
     * Create an initialised instance of this factory, using the parameters
     * provided.  An instance of this factory can then be used to access the
     * appropriate renderers.
     *
     * @param context Contains contextual information.
     * @param outputLocator Contains references to markup generators.
     */
    public DefaultMenuItemRendererFactory(
            RendererContext context,
            DeprecatedOutputLocator outputLocator,
            MenuModuleCustomisation customisation) {

        super(context.getOutputBufferFactory());
        this.spanOutput = outputLocator.getSpanOutput();
        this.imageOutput = outputLocator.getImageOutput();
        this.anchorOutput = outputLocator.getAnchorOutput();
        this.customisation = customisation;

        this.rendererContext = context;
    }

    // Javadoc inherited.
    protected MenuItemComponentRenderer createPlainImageRendererImpl
            (boolean provideAltText) {
        return new DefaultImageMenuItemRenderer(imageOutput, provideAltText);
    }

    // JavaDoc inherited
    public MenuItemComponentRenderer createPlainTextRenderer() {
        return new DefaultStyledPlainTextMenuItemRenderer(spanOutput);
    }

    // JavaDoc inherited
    public MenuItemBracketingRenderer createInnerLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {
        return new DefaultAnchorMenuItemRenderer(anchorOutput,
                false, emulation);
    }

    // JavaDoc inherited
    public MenuItemBracketingRenderer createOuterLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {
        return new DefaultAnchorMenuItemRenderer(anchorOutput,
                true, emulation);
    }

    // JavaDoc inherited
    public MenuItemBracketingRenderer createOuterRenderer() {

        // Renders the style of the menu if it's not otherwise rendered in a
        // link.
        return new DefaultSpanMenuItemRenderer(spanOutput);
    }

    // JavaDoc inherited
    public NumericShortcutEmulationRenderer 
            createNumericShortcutEmulationRenderer() {
        
        // If the underlying protocol supports the access key attribute...
        if (customisation.supportsAccessKeyAttribute()) {
            // .. then we should try and emulate numeric shortcuts using
            // access keys.
            return new DefaultNumericShortcutEmulationRenderer(customisation);
        } else {
            // ... else there is no way to emulate it, so just give up.
            return null;
        }
    }

    // JavaDoc inherited
    public ShortcutLabelRenderer createShortcutLabelRenderer() {

        ShortcutLabelRenderer result = null;
        // only create a shortcut renderer if the device supports shortcut keys
        // but does not display them automatically.
        if(customisation.supportsAccessKeyAttribute() &&
                !customisation.automaticallyDisplaysAccessKey()){


           DOMRendererContext domRendererContext =
                   (DOMRendererContext)rendererContext;
           result = new DefaultShortcutLabelRenderer(
                   spanOutput, customisation, domRendererContext.getInserter());
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Feb-05	6129/6	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/4	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/2	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 17-May-04	4440/4	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/2	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 06-May-04	4153/6	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 05-May-04	4124/3	claire	VBM:2004042805 Refining menu renderer selectors

 30-Apr-04	4124/1	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 29-Apr-04	4013/5	pduffin	VBM:2004042210 Restructure menu item renderers

 27-Apr-04	4025/5	claire	VBM:2004042302 Refining numeric emulation interface

 27-Apr-04	4025/3	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 26-Apr-04	3920/3	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 20-Apr-04	3715/3	claire	VBM:2004040201 Improving WML menu item renderers

 16-Apr-04	3715/1	claire	VBM:2004040201 Enhanced Menu: WML Menu Item Renderers

 ===========================================================================
*/
