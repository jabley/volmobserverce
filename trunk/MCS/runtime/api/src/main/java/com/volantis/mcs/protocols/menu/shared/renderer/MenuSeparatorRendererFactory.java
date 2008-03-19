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

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMenuHorizontalSeparatorKeywords;

/**
 * A factory that can be used to retrieve appropriate separator renderers for
 * specified menus, menu item groups and menu items. Since separator renderers
 * are required to be immutable, the factory can create and cache appropriate
 * instances as required.
 */
public interface MenuSeparatorRendererFactory {
    /**
     * Returns an initialized separator renderer for use in horizontally
     * separating a menu's components. The type of separation requested must be
     * one of:
     *
     * <ul>
     *
     * <li>{@link MCSMenuHorizontalSeparatorKeywords#NONE
     * NONE}</li>
     *
     * <li>{@link MCSMenuHorizontalSeparatorKeywords#SPACE
     * SPACE}</li>
     *
     * <li>{@link MCSMenuHorizontalSeparatorKeywords#NON_BREAKING_SPACE
     * NON-BREAKING SPACE}</li>
     *
     * </ul>
     *
     * <p>The type requested does not have to be honoured if the protocol in
     * question does not support the given type.</p>
     *
     * @param separatorType
     *         the requested type of separation to be used
     * @return the appropriately initialized separator renderer
     */
    SeparatorRenderer createHorizontalMenuSeparator(StyleValue separatorType);

    /**
     * Returns an initialized separator renderer for use in vertically
     * separating a menu's components.
     *
     * @return the appropriately initialized separator renderer
     */
    SeparatorRenderer createVerticalMenuSeparator();

    /**
     * Returns an initialized separator renderer for use in horizontally
     * separating a menu item's components. The type of separation requested
     * must be one of:
     *
     * <ul>
     *
     * <li>{@link MCSMenuHorizontalSeparatorKeywords#NONE
     * NONE}</li>
     *
     * <li>{@link MCSMenuHorizontalSeparatorKeywords#SPACE
     * SPACE}</li>
     *
     * <li>{@link MCSMenuHorizontalSeparatorKeywords#NON_BREAKING_SPACE
     * NON-BREAKING SPACE}</li>
     *
     * </ul>
     *
     * <p>The type requested does not have to be honoured if the protocol in
     * question does not support the given type.</p>
     *
     * @param separatorType
     *         the requested type of separation to be used
     * @return the appropriately initialized separator renderer
     */
    SeparatorRenderer createHorizontalMenuItemSeparator(StyleValue separatorType);

    /**
     * Returns an initialized separator renderer for use in vertically
     * separating a menu item's components.
     *
     * @return the appropriately initialized separator renderer
     */
    SeparatorRenderer createVerticalMenuItemSeparator();

    /**
     * Returns an initialized separator renderer for use in textually
     * delimiting a menu item group.
     *
     * @param chars  the sequence of characters to be rendered
     * @param repeat the number of times that the given character sequence
     *               should be repeated. Must be a natural number
     * @return the appropriately initialized separator renderer
     */
    SeparatorRenderer createCharacterMenuItemGroupSeparator(String chars,
                                                            int repeat);

    /**
     * Returns an initialized separator renderer for use in graphically
     * delimiting a menu item group.
     *
     * @param imageAssetReference
     *         the image that is to be rendered
     * @return the appropriately initialized separator renderer
     */
    SeparatorRenderer createImageMenuItemGroupSeparator(
            ImageAssetReference imageAssetReference);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
