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
package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StyleValue;

/**
 * A menu separator renderer factory for Openwave menus.
 *
 * @todo this is unused - is this a bug, or really not required?
 */
public class OpenwaveMenuSeparatorRendererFactory
        extends DefaultMenuSeparatorRendererFactory {

    /**
     * Initialise a new instance of
     * <code>OpenwaveMenuSeparatorRendererFactory</code>
     */
    public OpenwaveMenuSeparatorRendererFactory() {
        super(null, null);
    }

    // javadoc inherited
    public SeparatorRenderer createHorizontalMenuSeparator(StyleValue separatorType) {
        // Openwave doesn't support menu orientation separation - an
        // Openwave menu is always a vertical list
        return null;
    }

    // javadoc inherited
    public SeparatorRenderer createVerticalMenuSeparator() {
        // Openwave doesn't support menu orientation separation - an
        // Openwave menu is always a vertical list
        return null;
    }

    // javadoc inherited
    public SeparatorRenderer createVerticalMenuItemSeparator() {
        // Openwave doesn't support vertical separation.
        return null;
    }

    // javadoc inherited
    public SeparatorRenderer createCharacterMenuItemGroupSeparator(
            String chars,
            int repeat) {
        // Openwave doesn't support menu item groups.
        return null;
    }

    // javadoc inherited
    public SeparatorRenderer createImageMenuItemGroupSeparator(
            ImageAssetReference imageAssetReference) {
        // Openwave doesn't support menu item groups.
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/3	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/3	claire	VBM:2004042204 Implemented remaining required WML renderers

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
