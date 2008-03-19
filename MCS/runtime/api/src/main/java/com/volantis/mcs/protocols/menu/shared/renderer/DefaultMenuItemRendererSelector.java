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

import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMenuImageStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuItemActiveAreaKeywords;
import com.volantis.mcs.themes.properties.MCSMenuItemOrderKeywords;
import com.volantis.mcs.themes.properties.MCSMenuLinkStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuTextStyleKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * Selects the appropriate combination of components for the menu items within
 * a menu based on the following stylistic properties.
 *
 * <ul>
 *     <li>mcs-menu-item-active-area</li>
 *     <li>mcs-menu-item-order</li>
 *     <li>mcs-menu-item-orientation</li>
 *     <li>mcs-menu-image-style</li>
 *     <li>mcs-menu-text-style</li>
 * </ul>
 *
 * <p>These stylistic properties currently combine to specify one out of 27
 * possible combinations. 4 structural templates are shown below. Structure 1
 * only has one form. Structure 2 has two possible image types. Structure 3 has
 * two possible image types, two possible orientations and two possible orders
 * which means that it has 8 varieties. In addition to those variations
 * exhibited by Structure 3, Structure 4 also has two possible active areas
 * which means that it has 16 varieties.</p>
 *
 * <p>The following sections show an example of the structure and also show
 * what settings the stylistics properties must have in order for that structure
 * to be selected. The structure uses an XML representation of the structure
 * but it does not match any particular protocol. The class attributes are used
 * to indicate how the elements relate to the model. At the moment PAPI dpes not
 * support separate class attributes on text and image but the model and this
 * is capable of doing so.</p>
 *
 * <h4 id="structure-1">Structure 1</h4>
 *
 * <code><pre>   &lt;link class="menu-item-class"&gt;
 *     &lt;text class="menu-item-text-class"/&gt;
 * &lt;/link&gt;</pre></code>
 *
 * <table border="1">
 *     <tr><th>Property</th><th>Value</th></tr>
 *     <tr>
 *         <td><b><code>mcs-menu-text-style</code></b></td>
 *         <td><b><code>plain</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-image-style</code></b></td>
 *         <td><b><code>none</code></b></td>
 *     </tr>
 * </table>
 *
 * <h4 id="structure-2">Structure 2</h4>
 *
 * <code><pre>   &lt;link class="menu-item-class"&gt;
 *     &lt;img class="menu-item-image-class"/&gt;
 * &lt;/link&gt;</pre></code>
 *
 * <table border="1">
 *     <tr><th>Property</th><th>Value</th></tr>
 *     <tr>
 *         <td><b><code>mcs-menu-text-style</code></b></td>
 *         <td><b><code>none</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-image-style</code></b></td>
 *         <td><b><code>plain/rollover</code></b></td>
 *     </tr>
 * </table>
 *
 * <h4 id="structure-3">Structure 3</h4>
 *
 * <code><pre>   &lt;link class="menu-item-class"&gt;
 *     &lt;text class="menu-item-text-class"/&gt;
 *     &lt;separator/&gt;
 *     &lt;img class="menu-item-image-class"/&gt;
 * &lt;/link&gt;</pre></code>
 *
 * <table border="1">
 *     <tr><th>Property</th><th>Value</th></tr>
 *     <tr>
 *         <td><b><code>mcs-menu-text-style</code></b></td>
 *         <td><b><code>plain</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-image-style</code></b></td>
 *         <td><b><code>plain/rollover</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-item-order</code></b></td>
 *         <td><b><code>text-first/image-first</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-item-active-area</code></b></td>
 *         <td><b><code>both</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-item-orientation</code></b></td>
 *         <td><b><code>horizontal/vertical</code></b></td>
 *     </tr>
 * </table>
 *
 * <h4 id="structure-4">Structure 4</h4>
 *
 * <code><pre>   &lt;span class="menu-item-class"&gt;
 *     &lt;link&gt;
 *         &lt;text class="menu-item-text-class"/&gt;
 *     &lt;/link&gt;
 *     &lt;separator/&gt;
 *     &lt;img class="menu-item-image-class"/&gt;
 * &lt;/span&gt;</pre></code>
 *
 * <table border="1">
 *     <tr><th>Property</th><th>Value</th></tr>
 *     <tr>
 *         <td><b><code>mcs-menu-text-style</code></b></td>
 *         <td><b><code>plain</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-image-style</code></b></td>
 *         <td><b><code>plain/rollover</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-item-order</code></b></td>
 *         <td><b><code>text-first/image-first</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-item-active-area</code></b></td>
 *         <td><b><code>text-only/image-only</code></b></td>
 *     </tr>
 *     <tr>
 *         <td><b><code>mcs-menu-item-orientation</code></b></td>
 *         <td><b><code>horizontal/vertical</code></b></td>
 *     </tr>
 * </table>
 */
public class DefaultMenuItemRendererSelector
        implements MenuItemRendererSelector {

    /**
     * The factory for creating the individual parts of the menu item
     * renderers.
     */
    private MenuItemRendererFactory factory;

    /**
     * The selector used to obtain any separator needed between the menu item
     * renderers.
     */
    private MenuSeparatorRendererSelector selector;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory  The factory that this instance will use to create the
     *      individual parts of the renderers. May not be null.
     * @param selector The selector that this instance will use to obtain the
     *      separator(s) needed between the individual parts of the renderers.
     *      May not be null.
     */
    public DefaultMenuItemRendererSelector(
            MenuItemRendererFactory factory,
            MenuSeparatorRendererSelector selector) {

        if (factory == null) {
            throw new IllegalArgumentException("factory may not be null");
        } else if (selector == null) {
            throw new IllegalArgumentException("selector may not be null");
        }

        this.factory = factory;
        this.selector = selector;
    }

    // Javadoc inherited.
    public MenuItemRenderer selectMenuItemRenderer(Menu menu)
            throws RendererException {
        PropertyValues properties =
                menu.getElementDetails().getStyles().getPropertyValues();

        SeparatorRenderer separator;

        // Initialise the renderer to return.
        MenuItemRenderer itemRenderer = null;

        // Initialise the top component renderer.
        MenuItemComponentRenderer top = null;

        // Select the text renderer to use.
        MenuItemComponentRenderer textRenderer = selectTextRenderer(properties);

        // Select the image renderer to use, if there is no text renderer then
        // it will need to provide alternate text.
        boolean provideAltText = (textRenderer == null);
        MenuItemComponentRenderer imageRenderer
                = selectImageRenderer(properties,
                                      provideAltText);

        // Sort out the numeric emulation required (or not)...

        // Get the menu link style
        StyleValue menuLinkStyle = properties.getComputedValue(
                StylePropertyDetails.MCS_MENU_LINK_STYLE);

        // See if numeric shortcuts were specified in the style
        // Null indicates that no emulation is required - default to this
        NumericShortcutEmulationRenderer emulation = null;

        if (menuLinkStyle == MCSMenuLinkStyleKeywords.NUMERIC_SHORTCUT) {
            // Numeric shortcuts were specified so get an emulation object from
            // the factory and use this.  NOTE: this may still be a null object
            // if the factory/protocol does not support or require numeric
            // emulation it can return null from this call to indicate that.
            emulation = factory.createNumericShortcutEmulationRenderer();
        }

        // If they have both been specified then look at some other styles.
        if (imageRenderer != null && textRenderer != null) {

            // Now see what order they should be in.
            MenuItemComponentRenderer first;
            MenuItemComponentRenderer second;

            final StyleProperty ORDER_PROPERTY
                    = StylePropertyDetails.MCS_MENU_ITEM_ORDER;

            StyleValue order = properties.getComputedValue(ORDER_PROPERTY);
            if (order == MCSMenuItemOrderKeywords.IMAGE_FIRST) {
                    first = imageRenderer;
                    second = textRenderer;
            } else if (order == MCSMenuItemOrderKeywords.TEXT_FIRST) {
                    first = textRenderer;
                    second = imageRenderer;
            } else {
                    throw new IllegalStateException
                            ("Unknown keyword " + order + " for "
                             + ORDER_PROPERTY.getName());
            }

            // Determine which component, or components is active.
            final StyleProperty ACTIVE_AREA_PROPERTY
                    = StylePropertyDetails.MCS_MENU_ITEM_ACTIVE_AREA;

            // Get the enumeration value from the properties.
            StyleValue activeArea = properties.getComputedValue(ACTIVE_AREA_PROPERTY);

            ActiveMenuItemComponent activeComponent;
            if (activeArea == MCSMenuItemActiveAreaKeywords.IMAGE_ONLY) {
                if (first == imageRenderer) {
                    activeComponent = ActiveMenuItemComponent.FIRST;
                } else {
                    activeComponent = ActiveMenuItemComponent.SECOND;
                }
            } else if (activeArea == MCSMenuItemActiveAreaKeywords.TEXT_ONLY) {
                if (first == textRenderer) {
                    activeComponent = ActiveMenuItemComponent.FIRST;
                } else {
                    activeComponent = ActiveMenuItemComponent.SECOND;
                }
            } else if (activeArea == MCSMenuItemActiveAreaKeywords.BOTH) {
                activeComponent = ActiveMenuItemComponent.BOTH;
            } else {
                throw new IllegalStateException
                        ("Unknown keyword " + activeArea + " for "
                        + ACTIVE_AREA_PROPERTY.getName());
            }

            // Now see what separator should be used.
            separator = selector.selectMenuItemSeparator(menu);

            // Create the top level renderer that is responsible for managing
            // the active area.
            top = factory.createActiveAreaRenderer(first, separator, second,
                                                   activeComponent, emulation,
                                                   factory);

        } else if (imageRenderer != null) {
            // The menu items should by default only render images, however if
            // for some reason a menu item's image cannot be rendered then it
            // must fallback to text.
            MenuItemComponentRenderer fallbackTextRenderer
                    = factory.createFallbackTextRenderer();

            // Create a renderer that will fallback to the second renderer if
            // the first renderer does not work.
            MenuItemComponentRenderer fallbackRenderer
                    = factory.createFallbackRenderer(imageRenderer,
                                                     fallbackTextRenderer);

            // Wrap the image renderer inside a link.
            top = factory.createActiveAreaRenderer(fallbackRenderer,
                                                   emulation, factory);

        } else if (textRenderer != null) {

            // Wrap the text inside a link.
            top = factory.createActiveAreaRenderer(textRenderer, emulation,
                                                   factory);
        }

        // If no renderer could be found for any of the menu item components
        // then return immediately, otherwise create the item renderer to wrap
        // the component renderer.
        if (top != null) {
            itemRenderer = factory.createMenuItemRenderer(top);
        }

        return itemRenderer;
    }

    /**
     * Select the appropriate image renderer to use based on the style
     * properties.
     *
     * @param properties     The style properties that affect the choice of
     *                       renderer.
     * @param provideAltText Indicates that the image renderer if any should
     *                       provide alternate text.
     *
     * @return The MenuItemRenderer that will render the image part of the
     *         menu, or null if it should not be rendered.
     */
    private MenuItemComponentRenderer selectImageRenderer(PropertyValues properties,
                                                            boolean provideAltText) {

        MenuItemComponentRenderer renderer;

        // Use the correct property for this selection.
        final StyleProperty PROPERTY
                = StylePropertyDetails.MCS_MENU_IMAGE_STYLE;

        // Get the keyword value.
        StyleValue value = properties.getComputedValue(PROPERTY);

        // Choose the appropriate image renderer based on the
        // mcs-menu-image-style property.
        if (value == MCSMenuImageStyleKeywords.NONE) {
            renderer = null;
        } else if (value == MCSMenuImageStyleKeywords.PLAIN) {
            renderer = factory.createPlainImageRenderer(provideAltText);
        } else if (value == MCSMenuImageStyleKeywords.ROLLOVER) {
            renderer = factory.createRolloverImageRenderer(provideAltText);
        } else {
            throw new IllegalStateException
                    ("Unknown keyword " + value
                    + " for " + PROPERTY.getName());
        }

        return renderer;
    }

    /**
     * Select the appropriate text renderer to use based on the style
     * properties.
     * @param properties The style properties that affect the choice of
     * renderer.
     * @return The MenuItemRenderer that will render the text part of the menu,
     * or null if it should not be rendered.
     */
    private MenuItemComponentRenderer selectTextRenderer(PropertyValues properties) {

        MenuItemComponentRenderer renderer;

        // Use the correct property for this selection.
        final StyleProperty PROPERTY
                = StylePropertyDetails.MCS_MENU_TEXT_STYLE;

        // Get the keyword value.
        StyleValue value = properties.getComputedValue(PROPERTY);

        // Choose the appropriate text renderer based on the
        // mcs-menu-text-style.
        if (value == MCSMenuTextStyleKeywords.NONE) {
            renderer = null;
        } else if (value == MCSMenuTextStyleKeywords.PLAIN) {
            renderer = factory.createPlainTextRenderer();
        } else {
            throw new IllegalStateException
                    ("Unknown keyword " + value
                    + " for " + PROPERTY.getName());
        }

        return renderer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 29-Apr-04	4013/5	pduffin	VBM:2004042210 Restructure menu item renderers

 27-Apr-04	4025/1	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 22-Apr-04	3681/5	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 ===========================================================================
*/
