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

import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * Base class for many menu item renderer factories.
 */
public abstract class AbstractMenuItemRendererFactory
        implements MenuItemRendererFactory {

    /**
     * Factory for creating output buffers.
     */
    private final OutputBufferFactory outputBufferFactory;

    /**
     * A menu item renderer for the image part of a menu item.
     */
    private MenuItemComponentRenderer imageItemRendererWithAltText;
    private MenuItemComponentRenderer imageItemRendererWithoutAltText;

    public AbstractMenuItemRendererFactory(OutputBufferFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory must not be null");
        }
        this.outputBufferFactory = factory;
    }

    /**
     * Create a default implementation of the {@link MenuItemRenderer}.
     */
    public MenuItemRenderer createMenuItemRenderer
            (MenuItemComponentRenderer renderer) {

        return new DefaultMenuItemRenderer(renderer, outputBufferFactory);
    }

    /**
     * Delegate to {@link #createPlainImageRendererImpl} to create the renderer
     * and then cache the result based on the setting of the provideAltText
     * flag.
     *
     * <p>This assumes that image renderers can be reused within the same page.
     * If they cannot then this method must be overridden.</p>
     */
    public MenuItemComponentRenderer createPlainImageRenderer(boolean provideAltText) {
        if (provideAltText) {
            if (imageItemRendererWithAltText == null) {
                imageItemRendererWithAltText = createPlainImageRendererImpl(provideAltText);
            }
            return imageItemRendererWithAltText;
        } else {
            if (imageItemRendererWithoutAltText == null) {
                imageItemRendererWithoutAltText = createPlainImageRendererImpl(provideAltText);
            }
            return imageItemRendererWithoutAltText;
        }
    }

    /**
     * Create an image renderer.
     *
     * <p>Renderers created by this must be reusable within a page as they are
     * cached by {@link #createPlainImageRenderer}.</p>
     *
     * @param provideAltText See {@link #createPlainImageRenderer}
     *
     * @return The newly created image renderer.
     */
    protected MenuItemComponentRenderer createPlainImageRendererImpl(
            boolean provideAltText) {

        // Neither the createPlainImageRenderer, or this method has been overridden and this
        // method has not been implemented.
        throw new UnsupportedOperationException("Must override either createPlainImageRenderer() or createPlainImageRendererImpl()");
    }

    /**
     * Delegates to the {@link #createPlainImageRenderer} method.
     */
    public MenuItemComponentRenderer createRolloverImageRenderer(boolean provideAltText) {
        return createPlainImageRenderer(provideAltText);
    }

    // JavaDoc inherited
    public MenuItemComponentRenderer createPlainTextRenderer() {
        return DefaultPlainTextMenuItemRenderer.DEFAULT_INSTANCE;
    }

    /**
     * Delegates to the {@link #createPlainTextRenderer} method.
     */
    public MenuItemComponentRenderer createFallbackTextRenderer() {
        return createPlainTextRenderer();
    }

    /**
     * Creates a default implementation.
     */
    public MenuItemComponentRenderer createFallbackRenderer
            (MenuItemComponentRenderer preferred,
             MenuItemComponentRenderer alternate) {
        return new DefaultFallbackMenuItemRenderer(preferred, alternate);
    }

    /**
     * Creates a default implementation.
     */
    public MenuItemComponentRenderer createActiveAreaRenderer
            (MenuItemComponentRenderer first,
             SeparatorRenderer separator,
             MenuItemComponentRenderer second,
             ActiveMenuItemComponent defaultActiveComponent,
             NumericShortcutEmulationRenderer numericEmulation,
             MenuItemRendererFactory factory) {

        return new DefaultActiveAreaRenderer(first, separator, second,
                                             defaultActiveComponent,
                                             numericEmulation, factory,
                                             outputBufferFactory);
    }

    /**
     * Creates a default implementation.
     */
    public MenuItemComponentRenderer createActiveAreaRenderer
            (MenuItemComponentRenderer renderer,
             NumericShortcutEmulationRenderer numericEmulation, MenuItemRendererFactory factory) {

        return new DefaultActiveAreaRenderer(renderer, numericEmulation,
                                             factory, outputBufferFactory);
    }

    /**
     * Return null.
     */
    public NumericShortcutEmulationRenderer 
            createNumericShortcutEmulationRenderer() {
        
        return null;
    }

    /**
     * @return null.
     */
    public ShortcutLabelRenderer createShortcutLabelRenderer() {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
