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

/**
 * A default menu separator renderer factory for menus.
 */
public class DefaultMenuSeparatorRendererFactory
        implements MenuSeparatorRendererFactory {

    /**
     * Reference to an object that is capable of rendering an image based on
     * protocol "knowledge"
     */
    private final DeprecatedImageOutput imageOutput;

    /**
     * An instance of a vertical separator renderer that can be used in all
     * cases where the factory is asked to provide one.
     */
    private final SeparatorRenderer verticalSeparatorRenderer;

    /**
     * Initialise a new instance of
     * <code>DefaultMenuSeparatorRendererFactory</code> using the supplied
     * parameter.
     *
     * @param imageOutput The means by which an image can be rendered. Required
     *      for some of the classes created by this factory.
     * @param lineBreakOutput The means by which a line break can be rendered.
     *      Required for some of the classes created by this factory.
     */
    public DefaultMenuSeparatorRendererFactory(
            DeprecatedImageOutput imageOutput,
            DeprecatedLineBreakOutput lineBreakOutput) {

        this.imageOutput = imageOutput;
        this.verticalSeparatorRenderer =
                new DefaultVerticalSeparatorRenderer(lineBreakOutput);
    }

    // javadoc inherited
    public SeparatorRenderer createHorizontalMenuSeparator(StyleValue separatorType) {
        return new DefaultHorizontalSeparatorRenderer(separatorType);
    }

    // javadoc inherited
    public SeparatorRenderer createVerticalMenuSeparator() {
        return verticalSeparatorRenderer;
    }

    // javadoc inherited
    public SeparatorRenderer createHorizontalMenuItemSeparator(
            StyleValue separatorType) {

        return new DefaultHorizontalSeparatorRenderer(separatorType);
    }

    // javadoc inherited
    public SeparatorRenderer createVerticalMenuItemSeparator() {
        return verticalSeparatorRenderer;
    }

    // javadoc inherited
    public SeparatorRenderer createCharacterMenuItemGroupSeparator(
            String chars,
            int repeat) {

        return new DefaultCharacterMenuItemGroupSeparatorRenderer(
                chars, repeat);
    }

    // javadoc inherited
    public SeparatorRenderer createImageMenuItemGroupSeparator(
            ImageAssetReference imageAssetReference) {
        return new DefaultImageMenuItemGroupSeparatorRenderer(
                imageAssetReference, imageOutput);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 26-Apr-04	3920/3	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/3	claire	VBM:2004042204 Implemented remaining required WML renderers

 22-Apr-04	3986/1	claire	VBM:2004042106 Creating a default menu item group character separator

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
