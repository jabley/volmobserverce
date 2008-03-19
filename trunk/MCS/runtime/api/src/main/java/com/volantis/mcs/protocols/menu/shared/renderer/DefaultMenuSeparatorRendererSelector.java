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

import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.css.StylePropertyResolver;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.shared.MenuInspector;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMenuItemOrientationKeywords;
import com.volantis.mcs.themes.properties.MCSMenuOrientationKeywords;
import com.volantis.mcs.themes.properties.MCSMenuSeparatorPositionKeywords;
import com.volantis.mcs.themes.properties.MCSMenuSeparatorTypeKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * Default implementation of the MenuSeparatorRendererSelector.
 */
public class DefaultMenuSeparatorRendererSelector
        implements MenuSeparatorRendererSelector {

    /**
     * Provides access to various methods that can be used to derive specific
     * information about a given menu.
     */
    private final MenuInspector inspector;

    /**
     * The abstract factory used to factor the required separator renderers.
     */
    private final MenuSeparatorRendererFactory factory;

    /**
     * The asset resolver required to resolve asset references in various
     * circumstances.
     */
    private final AssetResolver assetResolver;

    /**
     * Used to resolve menu related style info.
     */
    private final StylePropertyResolver styleResolver;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory the factory used to construct the actual separator
     *      renderer instances. Must not be null.
     * @param assetResolver the asset resolver used to resolve asset references
     *      in various circumstances.
     * @param styleResolver used to check menu related style info.
     */
    public DefaultMenuSeparatorRendererSelector(
            MenuSeparatorRendererFactory factory,
            AssetResolver assetResolver,
            StylePropertyResolver styleResolver) {
        if (factory == null) {
            throw new IllegalArgumentException("factory may not be null");
        } else if (assetResolver == null) {
            throw new IllegalArgumentException(
                    "assetResolver may not be null");
        }

        this.factory = factory;
        this.inspector = new MenuInspector();
        this.assetResolver = assetResolver;
        this.styleResolver = styleResolver;
    }

    // javadoc inherited
    public SeparatorRenderer selectMenuSeparator(Menu menu) {
        SeparatorRenderer separator = null;

        // The properties that we're interested in are applied to the menu
        final PropertyValues properties =
                menu.getElementDetails().getStyles().getPropertyValues();

        // Now see what separator should be used.
        final StyleProperty ORIENTATION_PROPERTY =
                StylePropertyDetails.MCS_MENU_ORIENTATION;

        StyleValue orientation = properties.getComputedValue(
                ORIENTATION_PROPERTY);
        if (orientation == MCSMenuOrientationKeywords.HORIZONTAL) {
            separator = factory.createHorizontalMenuSeparator(
                    properties.getComputedValue(
                            StylePropertyDetails.MCS_MENU_HORIZONTAL_SEPARATOR));
        } else if (orientation == MCSMenuOrientationKeywords.VERTICAL) {
            separator = factory.createVerticalMenuSeparator();
        } else {
            throw new IllegalStateException("Unknown keyword " +
                                            orientation + " for " +
                                            ORIENTATION_PROPERTY.getName());
        }

        return separator;
    }

    // javadoc inherited
    public SeparatorRenderer selectMenuItemGroupSeparator(
            MenuItemGroup group,
            boolean before) {
        SeparatorRenderer separator = null;

        // Explicitly targeted groups and groups within menus that are
        // performing automatic iteration allocation should not generate any
        // delimiters, so check for these cases
        if ((group.getPane() == null) &&
                (!performingAutoIteration((Menu)group.getContainer()))) {
            // The properties that we're interested in are applied to the group
            final PropertyValues properties =
                    group.getElementDetails().getStyles().getPropertyValues();

            // See if we should even bother to have a separator
            StyleValue position = properties.getComputedValue(
                    StylePropertyDetails.MCS_MENU_SEPARATOR_POSITION);

            if ((position == MCSMenuSeparatorPositionKeywords.BOTH) ||
                    (before && (position ==
                    MCSMenuSeparatorPositionKeywords.BEFORE)) ||
                    (!before && (position ==
                    MCSMenuSeparatorPositionKeywords.AFTER))) {

                // A separator is required for this position so determine the
                // required type of separator
                StyleValue type = properties.getComputedValue(StylePropertyDetails.MCS_MENU_SEPARATOR_TYPE);

                if (type == MCSMenuSeparatorTypeKeywords.NONE) {
                    // No separator is actually required because of this
                    // type selection
                } else if (type == MCSMenuSeparatorTypeKeywords.CHARACTERS) {
                    // Get the remaining properties required to configure
                    // the separator
                    String chars = styleResolver.getStringValue(properties, StylePropertyDetails.
                            MCS_MENU_SEPARATOR_CHARACTERS);
                    int repeat = styleResolver.getIntValue(properties,
                            StylePropertyDetails.MCS_MENU_SEPARATOR_REPEAT);

                    // An empty string or a negative repeat needs no
                    // separator
                    if ((chars != null) &&
                            !"".equals(chars) &&
                            repeat >= 0) {
                        if (repeat == 0) {
                            // The length must be adjusted to equal the
                            // length of the longest item in the menu to
                            // which the specified menu item group belongs.
                            chars = fabricatedSeparatorChars(group, chars);
                            repeat = 1;
                        }

                        separator = factory.
                                createCharacterMenuItemGroupSeparator(chars, repeat);
                    }
                } else if (type == MCSMenuSeparatorTypeKeywords.IMAGE) {
                    // Get the remaining properties required to configure
                    // the separator
                    ImageAssetReference imageAssetReference =
                            styleResolver.getImageAssetReferenceValue(properties, StylePropertyDetails.
                            MCS_MENU_SEPARATOR_IMAGE,
                                    assetResolver);

                    separator = factory.createImageMenuItemGroupSeparator(imageAssetReference);
                } else {
                    throw new IllegalStateException("Unknown keyword " + type +
                            " for " +
                            StylePropertyDetails.MCS_MENU_SEPARATOR_TYPE.
                            getName());
                }
            }
        }

        return separator;
    }

    /**
     * Supporting method that returns true if the specified menu is actually
     * performing automatic iteration allocation.
     *
     * @param menu the menu to be tested
     * @return true if the menu is performing automatic iteration allocation,
     *         false otherwise
     */
    private boolean performingAutoIteration(Menu menu) {
        return inspector.isAutoIterationAllocating(menu);
    }

    /**
     * When a zero repeat count is specified a bespoke separator character
     * sequence is required. This method determines the longest menu item in
     * the group's containing menu (including those items in groups in that
     * menu) and repeats the given chars to create a separator character
     * sequence of exactly the right length.
     *
     * @param group the menu item group that requires a bespoke separator
     *              character sequence
     * @param chars the single repeat version of the separator string
     * @return an adjusted version of the given separator character sequence
     *         that is exactly the length of the longest menu item label
     */
    private String fabricatedSeparatorChars(MenuItemGroup group,
                                         String chars) {
        // After discussion with Rhys it has been decided that
        // sub-menu labels and content will be ignored (this
        // approach meets the wording in AN004 in the most
        // direct sense [but probably not in the originally
        // intended sense!], when we assume that a sub-menu is
        // a separate menu and that sub-menu labels are not
        // menu items themselves).

        // This code assumes that the buffers contain plain
        // text
        Menu menu = (Menu)group.getContainer();
        MenuEntry entry;
        DOMOutputBuffer buffer;
        Text text;
        int max = 0;
        int len;

        // Determine the maximum length menu item label
        for (int i = 0; i < menu.getSize(); i++) {
            entry = menu.get(i);

            if (entry instanceof MenuItem) {
                buffer = (DOMOutputBuffer)((MenuItem)entry).
                        getLabel().getText().getText();

                // Assumes that there is just a single text node
                text = (Text)buffer.getRoot().getHead();

                len = text.getLength();

                if (max < len) {
                    max = len;
                }
            } else if (entry instanceof MenuItemGroup) {
                MenuItemGroup mig = (MenuItemGroup)entry;
                MenuItem item;
                for (int j = 0; j < mig.getSize(); j++) {
                    item = mig.get(j);

                    buffer = (DOMOutputBuffer)item.
                            getLabel().getText().getText();

                    // Assumes that there is just a single text node
                    text = (Text)buffer.getRoot().getHead();

                    len = text.getLength();

                    if (max < len) {
                        max = len;
                    }
                }
            }
        }

        // If necessary, adjust the character sequence to be of the required
        // length
        if (chars.length() != max) {
            StringBuffer newChars = new StringBuffer(chars);

            // Repeat the basic sequence as many times as required
            while (newChars.length() < max) {
                newChars.append(chars);
            }

            // Trim the sequence to exactly the right length
            newChars.setLength(max);

            chars = newChars.toString();
        }

        return chars;
    }

    // javadoc inherited
    public SeparatorRenderer selectMenuItemSeparator(Menu menu) {
        SeparatorRenderer separator = null;

        final PropertyValues properties =
                menu.getElementDetails().getStyles().getPropertyValues();

        // Now see what separator should be used.
        final StyleProperty ORIENTATION_PROPERTY =
                StylePropertyDetails.MCS_MENU_ITEM_ORIENTATION;

        StyleValue orientation = properties.getComputedValue(
                ORIENTATION_PROPERTY);
        if (orientation == MCSMenuItemOrientationKeywords.HORIZONTAL) {
            separator = factory.createHorizontalMenuItemSeparator(
                    properties.getComputedValue(
                            StylePropertyDetails.MCS_MENU_HORIZONTAL_SEPARATOR));
        } else if (orientation == MCSMenuItemOrientationKeywords.VERTICAL) {
            separator = factory.createVerticalMenuItemSeparator();
        } else {
            throw new IllegalStateException("Unknown keyword " +
                                            orientation + " for " +
                                            ORIENTATION_PROPERTY.getName());
        }

        return separator;
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

 10-Mar-05	7022/4	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4204/2	philws	VBM:2004042810 Suppress Menu Item Group separators when needed

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
