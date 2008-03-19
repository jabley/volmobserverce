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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * Default renderer that manages the active area of a menu item.
 *
 * <p>The active area of a menu item is the part that the user interacts with.
 * If a menu item does not have an active area then it is useless and so is
 * ignored.</p>
 */
public class DefaultActiveAreaRenderer
        implements MenuItemComponentRenderer {

    /**
     * A factory that will create OutputBuffers.
     */
    private final OutputBufferFactory outputBufferFactory;

    /**
     * The renderer for the first component, may not be null.
     */
    private final MenuItemComponentRenderer first;

    /**
     * The renderer for the separator between components, may be null.
     */
    private final SeparatorRenderer separator;

    /**
     * The renderer for the second component, may be null.
     */
    private final MenuItemComponentRenderer second;

    /**
     * A literal that indicates what parts of the menu item are active.
     */
    private final ActiveMenuItemComponent defaultActiveComponent;

    /**
     * The numeric emulation renderer to use, may be null.
     */
    private final NumericShortcutEmulationRenderer numericEmulation;

    /**
     * The renderer used to render the link if it is the outer most element of
     * the menu item. Typically the only difference between this and the inner
     * link will be that this one has stylistic information from the menu item.
     */
    private MenuItemBracketingRenderer outerLinkRenderer;

    /**
     * The renderer used to render the link it is not the outer most element of
     * the menu item.
     *
     * @see #outerLinkRenderer
     * @see #outerRenderer
     */
    private MenuItemBracketingRenderer innerLinkRenderer;

    /**
     * The renderer used to render the outer most element of the menu item that
     * has the menu item's stylistic information. This will be used in
     * conjunction with {@link #innerLinkRenderer}.
     */
    private MenuItemBracketingRenderer outerRenderer;

    /**
     * The renderer used to produce Shortcut Labels for menu items.
     */
    private ShortcutLabelRenderer shortcutRenderer;

    /**
     * The factory used to create renderers lazily.
     */
    private final MenuItemRendererFactory rendererFactory;

    /**
     * The buffer into which the first component is rendered.
     */
    private final OutputBuffer firstBuffer;

    /**
     * The buffer into which the second component is rendered.
     */
    private OutputBuffer secondBuffer;

    /**
     * Initialise with up to two component renderers.
     *
     * @param first The renderer of the first menu item component, may not be
     * null.
     * @param separator The separator between components, may be null.
     * @param second The renderer of the second menu item component, may be
     * null.
     * @param defaultActiveComponent The active component(s) of the menu item.
     * @param numericEmulation The numeric emulation, may not be null.
     * @param rendererFactory The factory that should be used to create
     * additional renderers.
     * @param outputBufferFactory The factory for creating output buffers.
     */
    public DefaultActiveAreaRenderer(MenuItemComponentRenderer first,
                                     SeparatorRenderer separator,
                                     MenuItemComponentRenderer second,
                                     ActiveMenuItemComponent defaultActiveComponent,
                                     NumericShortcutEmulationRenderer numericEmulation,
                                     MenuItemRendererFactory rendererFactory,
                                     OutputBufferFactory outputBufferFactory) {

        this.first = first;
        this.separator = separator;
        this.second = second;
        this.defaultActiveComponent = defaultActiveComponent;
        this.numericEmulation = numericEmulation;

        this.outputBufferFactory = outputBufferFactory;
        this.rendererFactory = rendererFactory;

        firstBuffer = outputBufferFactory.createOutputBuffer();
    }

    /**
     * Initialise with one component renderer.
     *
     * @param renderer The renderer for the menu item component.
     * @param numericEmulation The numeric emulation, may not be null.
     * @param rendererFactory The factory that should be used to create
     * additional renderers.
     * @param outputBufferFactory The factory for creating output buffers.
     */
    public DefaultActiveAreaRenderer(MenuItemComponentRenderer renderer,
                                     NumericShortcutEmulationRenderer numericEmulation,
                                     MenuItemRendererFactory rendererFactory,
                                     OutputBufferFactory outputBufferFactory) {

        this(renderer, null, null, null, numericEmulation,
             rendererFactory, outputBufferFactory);
    }

    /**
     * Render the menu item.
     *
     * <p>If rendering the menu item components produced nothing then this will
     * not produce any
     */
    public MenuItemRenderedContent render(OutputBuffer buffer,
                                          MenuItem item)
            throws RendererException {

        //OutputBuffer buffer = separatorManager.getOutputBuffer();

        MenuItemRenderedContent renderedContent;


        // Render the first and second components of the menu item.
        MenuItemRenderedContent firstContent = first.render(firstBuffer, item);
        boolean firstEmpty
                = (firstContent == MenuItemRenderedContent.NONE);

        MenuItemRenderedContent secondContent;
        boolean secondEmpty;
        if (second != null) {
            if (secondBuffer == null) {
                secondBuffer = outputBufferFactory.createOutputBuffer();
            }
            secondContent = second.render(secondBuffer, item);
            secondEmpty = (secondContent == MenuItemRenderedContent.NONE);
        } else {
            secondContent = null;
            secondEmpty = true;
        }

        if (firstEmpty && secondEmpty) {

            // Neither of them generated any content so ignore this menu item
            // completely.
            renderedContent = MenuItemRenderedContent.NONE;

        } else if (firstEmpty) {

            // Write an outer link for the whole menu item that wraps the
            // contents of the second menu item component.
            wrapBuffer(buffer, item, getOuterLinkRenderer(), secondBuffer);

            // Remember the type of content written.
            renderedContent = secondContent;

        } else if (secondEmpty) {

            // Write an outer link for the whole menu item that wraps the
            // contents of the first menu item component.
            wrapBuffer(buffer, item, getOuterLinkRenderer(), firstBuffer);

            // Remember the type of content written.
            renderedContent = firstContent;

        } else {

            // If both the parts are active then wrap them all in an outer
            // link, otherwise wrap them in an outer element.
            MenuItemBracketingRenderer outerRenderer;
            if (defaultActiveComponent == ActiveMenuItemComponent.BOTH) {
                outerRenderer = getOuterLinkRenderer();
            } else {
                outerRenderer = getOuterRenderer();
            }

            if (outerRenderer.open(buffer, item)) {

                // Both of these generated content so render them both, making the
                // appropriate part active.
                if (defaultActiveComponent == ActiveMenuItemComponent.FIRST) {
                    wrapBuffer(buffer, item, getInnerLinkRenderer(),
                               firstBuffer);
                } else {
                    buffer.transferContentsFrom(firstBuffer);
                }

                if (separator != null) {
                    // Always render the separator.
                    separator.render(buffer);
                }

                if (defaultActiveComponent == ActiveMenuItemComponent.SECOND) {
                    wrapBuffer(buffer, item, getInnerLinkRenderer(),
                               secondBuffer);
                } else {
                    buffer.transferContentsFrom(secondBuffer);
                }

                outerRenderer.close(buffer, item);
            }

            // We assume that one of the renderers rendered text and one
            // rendered an image in which case this renderer will render text.
            renderedContent = MenuItemRenderedContent.TEXT;
        }

        return renderedContent;
    }

    private void wrapBuffer(OutputBuffer destination, MenuItem item,
                                   MenuItemBracketingRenderer renderer,
                                   OutputBuffer source)
            throws RendererException {

        // If the shortcut is not marked "active" then render outside the
        // link
        if(!item.getMenu().getShortcutProperties().isActive()) {
            renderShortcutLabel(destination, item);
        }

        // This outer check is needed in case there is no href defined on the
        // item.  This is not allowed by the builder be specifically occurs when
        // {@link com.volantis.mcs.protocols.menu.shared.model.ConcreteMenu#getAsMenuItem}
        // is called.
        if (!item.isSubMenuItem()) {
            if (renderer.open(destination, item)) {

                // render inside the link if the menu is marked "active"
                if(item.getMenu().getShortcutProperties().isActive()) {
                    renderShortcutLabel(destination, item);
                }

                destination.transferContentsFrom(source);
                renderer.close(destination, item);
            }
        } else {
            destination.transferContentsFrom(source);
        }
    }

    /**
     * utility method to call the shortcut label renderer (if one exists)
     * @param buffer the output buffer to use
     * @param menuItem the item to render
     * @throws RendererException if an error occurs while rendering.
     */
    private void renderShortcutLabel(
           OutputBuffer buffer, MenuItem menuItem)
            throws RendererException{
        // set up the shortcut label renderer (if any)
        getShortcutLabelRenderer();
        if(shortcutRenderer!=null) {
            shortcutRenderer.render(buffer, menuItem);
        }
    }

    /**
     * @return a ShortcutLabelRenderer for use with this ActiveAreaRenderer.
     */
    private ShortcutLabelRenderer getShortcutLabelRenderer() {
        if(shortcutRenderer == null) {
            shortcutRenderer = rendererFactory.createShortcutLabelRenderer();
        }
        return shortcutRenderer;
    }

    private MenuItemBracketingRenderer getOuterLinkRenderer() {
        if (outerLinkRenderer == null) {
            outerLinkRenderer = rendererFactory.createOuterLinkRenderer(numericEmulation);
        }
        return outerLinkRenderer;
    }

    private MenuItemBracketingRenderer getInnerLinkRenderer() {
        if (innerLinkRenderer == null) {
            innerLinkRenderer = rendererFactory.createInnerLinkRenderer(numericEmulation);
        }
        return innerLinkRenderer;
    }

    private MenuItemBracketingRenderer getOuterRenderer() {
        if (outerRenderer == null) {
            outerRenderer = rendererFactory.createOuterRenderer();
        }
        return outerRenderer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 16-Feb-05	6129/7	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 27-Jan-05	6129/3	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/1	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 10-May-04	4225/3	claire	VBM:2004050705 Fix DefaultActiveAreaRenderer for inline submenus

 10-May-04	4225/1	claire	VBM:2004050705 Fix DefaultActiveAreaRenderer for inline submenus

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
