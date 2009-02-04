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
 * A factory used by the {@link DefaultMenuItemRendererSelector} to create the
 * different forms of MenuItemRenderer that it needs.
 */
public interface MenuItemRendererFactory {

    /**
     * Create a MenuItemRenderer that wraps the specified component renderer.
     *
     * <p>The returned MenuItemRenderer will target all components of the menu
     * item at the same OutputBuffer.</p>
     *
     * @param renderer The MenuItemComponentRenderer to wrap.
     *
     * @return The newly created MenuItemRenderer.
     */
    public MenuItemRenderer createMenuItemRenderer
        (MenuItemComponentRenderer renderer);

    /**
     * Create a MenuItemComponentRenderer that can render a plain image.
     *
     * <p>This is used to render the &lt;img&gt; element in the structures in
     * {@link DefaultMenuItemRendererSelector}.</p>
     *
     * <p>If no suitable image could be found then the renderer returned by
     * this does nothing.</p>
     *
     * <p>If an image is found then the renderer may still need to provide some
     * alternate text for the image in the event that the user agent cannot or
     * does not retrieve the image.</p>
     *
     * <p>The alternate text that would be used is the text associated with
     * the menu item. However, the text may already be being rendered by
     * another renderer in which case the user could end up seeing the same
     * text twice which is not desirable. Therefore, alternate text will only
     * be provided if the provideAltText parameter is true, otherwise there
     * will be no alt text associated with the image.</p>
     *
     * @param provideAltText If true then the renderer should provide alternate
     * text, otherwise it should not.
     *
     * @return A plain image MenuItemComponentRenderer.
     */
    public MenuItemComponentRenderer createPlainImageRenderer(
            boolean provideAltText);

    /**
     * Create a MenuItemComponentRenderer that can render a rollover image.
     *
     * <p>This is used to render the &lt;img&gt; element in the structures in
     * {@link DefaultMenuItemRendererSelector}.</p>
     *
     * @param provideAltText See {@link #createPlainImageRenderer}.
     *
     * @return A rollover image MenuItemComponentRenderer.
     */
    public MenuItemComponentRenderer createRolloverImageRenderer(
            boolean provideAltText);

    /**
     * Create a MenuItemComponentRenderer that can render plain text.
     *
     * <p>This is used to render the &lt;text&gt; element in the structures in
     * {@link DefaultMenuItemRendererSelector}.</p>
     *
     * @return A plain text MenuItemComponentRenderer.
     */
    public MenuItemComponentRenderer createPlainTextRenderer();

    /**
     * Create a MenuItemComponentRenderer that will render the menu text in the
     * event that an image cannot be rendered.
     *
     * <p>If no suitable image could be found and the menu text is not written
     * out by default then the renderer returned by this method will be invoked
     * to render the menu item fallback text.</p>
     *
     * @return A MenuItemComponentRenderer for rendering the menu item text as
     * image fallback text.
     */
    public MenuItemComponentRenderer createFallbackTextRenderer();

    /**
     * Create a MenuItemComponentRenderer that can render an inner link.
     *
     * <p>This is used to render the &lt;link&gt; element in structure 4 in
     * {@link DefaultMenuItemRendererSelector}.</p>
     *
     * @param emulation A renderer to use for emulating numeric shortcuts, may
     *                  be null to indicate no emulation required.
     *
     * @return A link MenuItemComponentRenderer.
     */
    public MenuItemBracketingRenderer createInnerLinkRenderer(
            NumericShortcutEmulationRenderer emulation);

    /**
     * Create a MenuItemComponentRenderer that can render an outer link.
     *
     * <p>This is used to render the &lt;link&gt; element in structure 3 in
     * {@link DefaultMenuItemRendererSelector}.</p>
     *
     * @param emulation A renderer to use for emulating numeric shortcuts, may
     *                  be null to indicate no emulation required.
     * @return The MenuItemComponentRenderer than can render an outer link.
     */
    public MenuItemBracketingRenderer createOuterLinkRenderer(
            NumericShortcutEmulationRenderer emulation);

    /**
     * Create a MenuItemRenderer that can render the whole item.
     *
     * <p>This is used to render the &lt;span&gt; element in structure 4 in
     * {@link DefaultMenuItemRendererSelector}.</p>
     *
     * @return The MenuItemRenderer than can render an outer link.
     */
    public MenuItemBracketingRenderer createOuterRenderer();

    /**
     * Create an active area renderer.
     *
     * <p>An active area renderer is responsible for rendering the active part
     * of a menu item. The active part is that part that the user can interact
     * with in order to select the page referenced by the menu item.</p>
     *
     * <p>A menu item without an active area is useless and will not be written
     * out which could lead to problems for the user. Therefore, it is this
     * renderer's purpose to try and prevent this situation from arising.</p>
     *
     * <p>The exception to this is menu items that are created as labels for
     * a menu. They will not have an href and so will not have an active area
     * but they are still written out.</p>
     *
     * <p>A page author has a number of other approaches that they can take if
     * they want to hide a menu item for a specific device or devices.</p>
     *
     * @param first The renderer for the component that should be displayed
     * first.
     * @param separator The separator to use between the first and second
     * components.
     * @param second The renderer for the component that should be displayed
     * second.
     * @param defaultActiveComponent An enumerated value that indicates the
     * components that should be active.
     * @param numericEmulation The numeric menu emulation object. May not be
     * null.
     * @param factory The factory for creating the parts of the menu item
     * renderer.
     */
    public MenuItemComponentRenderer createActiveAreaRenderer
        (MenuItemComponentRenderer first,
         SeparatorRenderer separator,
         MenuItemComponentRenderer second,
         ActiveMenuItemComponent defaultActiveComponent,
         NumericShortcutEmulationRenderer numericEmulation,
         MenuItemRendererFactory factory);

    /**
     * See {@link #createActiveAreaRenderer}.
     */
    public MenuItemComponentRenderer createActiveAreaRenderer
            (MenuItemComponentRenderer renderer,
             NumericShortcutEmulationRenderer numericEmulation,
             MenuItemRendererFactory factory);

    /**
     * Create an object that will try the preferred component and if that does
     * not produce anything with then try the alternate component.
     * @param preferred The component renderer that should be tried first.
     * @param alternate The component renderer that should only be used if the
     * preferred did not render anything.
     * @return A fallback renderer.
     */
    public MenuItemComponentRenderer createFallbackRenderer
        (MenuItemComponentRenderer preferred,
         MenuItemComponentRenderer alternate);

    /**
     * Create a <code>NumericEmulationRenderer</code> that can be used to
     * emulate numeric shortcuts when rendering.  If this is not supported
     * a null value should be returned from this method.  All code using
     * the emulation object should be aware that it may be null and check
     * accordingly.
     *
     * @return A renderer to use for emulating numeric shortcuts, may be null
     * to indicate no emulation required.
     */
    public NumericShortcutEmulationRenderer 
            createNumericShortcutEmulationRenderer();


    /**
     * Create a <code>ShortcutLabelRenderer</code> that can be used to
     * render labels for generic shortcuts. If this is not supported then
     * a null will be returned.
     * @return A renderer that can be used to render labels for shortcuts.
     */
    public ShortcutLabelRenderer createShortcutLabelRenderer();

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

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 29-Apr-04	4013/5	pduffin	VBM:2004042210 Restructure menu item renderers

 27-Apr-04	4025/1	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 ===========================================================================
*/
