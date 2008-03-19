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

import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactoryTestAbstract;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultHorizontalSeparatorRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultCharacterMenuItemGroupSeparatorRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultImageMenuItemGroupSeparatorRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultVerticalSeparatorRenderer;
import com.volantis.mcs.themes.StyleKeyword;

/**
 * Tests the wml separator renderer factory.
 */
public class DefaultMenuSeparatorRendererFactoryTestCase
        extends MenuSeparatorRendererFactoryTestAbstract {
    // javadoc inherited
    protected MenuSeparatorRendererFactory createFactory() {
        return new DefaultMenuSeparatorRendererFactory(
                new TestDeprecatedImageOutput(),
                new TestDeprecatedLineBreakOutput());
    }

    // javadoc inherited
    protected Class getExpectedHorizontalMenuSeparatorRendererClass(
            StyleKeyword type) {
        return DefaultHorizontalSeparatorRenderer.class;
    }

    // javadoc inherited
    protected Class getExpectedVerticalMenuSeparatorRendererClass() {
        return DefaultVerticalSeparatorRenderer.class;
    }

    // javadoc inherited
    protected Class getExpectedVerticalMenuItemSeparatorRendererClass() {
        return  DefaultVerticalSeparatorRenderer.class;
    }

    // javadoc inherited
    protected Class getExpectedHorizontalMenuItemSeparatorRendererClass(
            StyleKeyword type) {
        return DefaultHorizontalSeparatorRenderer.class;
    }

    // javadoc inherited
    protected Class getExpectedCharacterMenuItemGroupSeparatorRendererClass() {
        return DefaultCharacterMenuItemGroupSeparatorRenderer.class;
    }

    // javadoc inherited
    protected Class getExpectedImageMenuItemGroupSeparatorRendererClass() {
        return DefaultImageMenuItemGroupSeparatorRenderer.class;
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

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 26-Apr-04	3920/5	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/1	claire	VBM:2004042204 Implemented remaining required WML renderers

 22-Apr-04	3986/1	claire	VBM:2004042106 Creating a default menu item group character separator

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
