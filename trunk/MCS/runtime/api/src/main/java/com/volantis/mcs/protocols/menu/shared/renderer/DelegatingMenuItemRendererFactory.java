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

import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * Delegates to instance passed in to the constructor.
 */
public class DelegatingMenuItemRendererFactory
        implements MenuItemRendererFactory {

    /**
     * The factory to delegate to.
     */
    private final MenuItemRendererFactory delegate;

    /**
     * Initialise.
     * @param delegate The factory to delegate to.
     */
    protected DelegatingMenuItemRendererFactory(MenuItemRendererFactory delegate) {
        this.delegate = delegate;
    }

    /**
     * Delegate.
     */
    public MenuItemRenderer createMenuItemRenderer
            (MenuItemComponentRenderer renderer) {
        return delegate.createMenuItemRenderer(renderer);
    }

    /**
     * Delegate.
     */
    public MenuItemComponentRenderer createPlainImageRenderer(boolean provideAltText) {
        return delegate.createPlainImageRenderer(provideAltText);
    }

    /**
     * Delegate.
     */
    public MenuItemComponentRenderer createRolloverImageRenderer(boolean provideAltText) {
        return delegate.createRolloverImageRenderer(provideAltText);
    }

    /**
     * Delegate.
     */
    public MenuItemComponentRenderer createPlainTextRenderer() {
        return delegate.createPlainTextRenderer();
    }

    /**
     * Delegate.
     */
    public MenuItemComponentRenderer createFallbackTextRenderer() {
        return delegate.createFallbackTextRenderer();
    }

    /**
     * Delegate.
     */
    public MenuItemBracketingRenderer createInnerLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {

        return delegate.createInnerLinkRenderer(emulation);
    }

    /**
     * Delegate.
     */
    public MenuItemBracketingRenderer createOuterLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {

        return delegate.createOuterLinkRenderer(emulation);
    }

    /**
     * Delegate.
     */
    public MenuItemBracketingRenderer createOuterRenderer() {
        return delegate.createOuterRenderer();
    }

    /**
     * Delegate.
     */
    public MenuItemComponentRenderer createActiveAreaRenderer
            (MenuItemComponentRenderer first,
             SeparatorRenderer separator,
             MenuItemComponentRenderer second,
             ActiveMenuItemComponent defaultActiveComponent,
             NumericShortcutEmulationRenderer numericEmulation,
             MenuItemRendererFactory factory) {

        return delegate.createActiveAreaRenderer(first, separator, second,
                                                 defaultActiveComponent,
                                                 numericEmulation, factory);
    }

    public MenuItemComponentRenderer createActiveAreaRenderer
            (MenuItemComponentRenderer renderer,
             NumericShortcutEmulationRenderer numericEmulation,
             MenuItemRendererFactory factory) {

        return delegate.createActiveAreaRenderer(renderer, numericEmulation,
                                                 factory);
    }

    /**
     * Delegate.
     */
    public MenuItemComponentRenderer createFallbackRenderer
            (MenuItemComponentRenderer preferred,
             MenuItemComponentRenderer alternate) {
        return delegate.createFallbackRenderer(preferred, alternate);
    }

    /**
     * Delegate.
     */
    public NumericShortcutEmulationRenderer 
            createNumericShortcutEmulationRenderer() {
        
        return delegate.createNumericShortcutEmulationRenderer();
    }

    /**
     * Delegate.
     */
    public ShortcutLabelRenderer createShortcutLabelRenderer() {
        return delegate.createShortcutLabelRenderer();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
