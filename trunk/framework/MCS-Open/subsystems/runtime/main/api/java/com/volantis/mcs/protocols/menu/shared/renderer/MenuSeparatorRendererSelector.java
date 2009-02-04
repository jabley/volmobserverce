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
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * Selects a separator renderer for the specified menu entries.
 */
public interface MenuSeparatorRendererSelector {

    /**
     * An instance that should be used anywhere that requires a non null
     * instance but which is never used.
     */
    public static final MenuSeparatorRendererSelector UNUSED
            = new MenuSeparatorRendererSelector() {

                public SeparatorRenderer selectMenuSeparator(Menu menu) {
                    throw new IllegalStateException("Selector failed");
                }

                public SeparatorRenderer selectMenuItemGroupSeparator(MenuItemGroup group,
                                                                      boolean before) {
                    throw new IllegalStateException("Selector failed");
                }

                public SeparatorRenderer selectMenuItemSeparator(Menu menu) {
                    throw new IllegalStateException("Selector failed");
                }
    };

    /**
     * Returns an initialized separator renderer for use in separating the menu
     * items and groups in the given menu. This should take the menu's
     * <code>mcs-menu-orientation</code> and
     * <code>mcs-menu-horizontal-separator</code> style properties into
     * account.
     *
     * <p>A null separator renderer may be returned if separation is not
     * appropriate.</p>

     * @param menu the menu for which the separator renderer is required
     * @return an appropriately initialized separator renderer or null
     */
    SeparatorRenderer selectMenuSeparator(Menu menu);

    /**
     * Returns an initialized separator renderer for use in delimiting the
     * given menu group in its menu. This should take the group's
     * <code>mcs-menu-separator</code> <code>-type</code>,
     * <code>-repeat</code>, <code>-position</code>, <code>-image</code> and
     * <code>-characters</code> style properties into account. It may also have
     * to take account of its containing menu's
     * <code>mcs-menu-orientation</code> and
     * <code>mcs-menu-horizontal-separator</code> style properties, depending
     * on the sophistication of the separator renderer implementation(s).
     *
     * <p>If the <code>before</code> and
     * <code>mcs-menu-separator-position</code> values are not compatible with
     * each other a null separator renderer should be returned.</p>
     *
     * <p>If the group has been explicitly targeted at a pane or its
     * containing menu is performing automatic iteration allocation then
     * a null separator renderer should be returned.</p>
     *
     * <p>A null separator renderer may be returned if separation is not
     * otherwise appropriate.</p>
     *
     * @param group  the group for which the separator renderer is required
     * @param before indicates whether a separator renderer is for use before
     *               or after the given menu item group
     * @return an appropriately initialized separator renderer or null
     */
    SeparatorRenderer selectMenuItemGroupSeparator(MenuItemGroup group,
                                                   boolean before);

    /**
     * Returns an initialized separator renderer for use in separating the
     * given menu's item's components. This should take the menu's
     * <code>mcs-menu-item-orientation</code> and
     * <code>mcs-menu-horizontal-separator</code> style properties into
     * account.
     *
     * <p>A null separator renderer may be returned if separation is not
     * appropriate.</p>
     *
     * @param menu the menu for which the separator renderer is required
     * @return an appropriately initialized separator renderer or null
     */
    SeparatorRenderer selectMenuItemSeparator(Menu menu);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4204/1	philws	VBM:2004042810 Suppress Menu Item Group separators when needed

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
