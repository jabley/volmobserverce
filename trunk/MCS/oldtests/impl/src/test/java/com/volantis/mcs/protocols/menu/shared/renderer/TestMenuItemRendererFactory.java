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

import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRenderer;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.OutputBufferFactory;


public class TestMenuItemRendererFactory
        extends AbstractMenuItemRendererFactory {

    public TestMenuItemRendererFactory(OutputBufferFactory factory) {
        super(factory);
    }

    public MenuItemComponentRenderer createPlainImageRenderer(boolean provideAltText) {
        return new TestPlainImageMenuItemRenderer(provideAltText);
    }

    public MenuItemComponentRenderer createRolloverImageRenderer(boolean provideAltText) {
        return new TestRolloverImageMenuItemRenderer(provideAltText);
    }

    public MenuItemComponentRenderer createPlainTextRenderer() {
        return new TestPlainTextMenuItemRenderer();
    }

    public MenuItemBracketingRenderer createInnerLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {
        return new TestLinkMenuItemRenderer(false);
    }

    public MenuItemBracketingRenderer createOuterLinkRenderer(
             NumericShortcutEmulationRenderer emulation) {
        return new TestLinkMenuItemRenderer(true);
    }

    public MenuItemBracketingRenderer createOuterRenderer() {
        return new TestSpanMenuItemRendererPair();
    }

    public NumericShortcutEmulationRenderer 
            createNumericShortcutEmulationRenderer() {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 29-Apr-04	4013/4	pduffin	VBM:2004042210 Restructure menu item renderers

 27-Apr-04	4025/1	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 ===========================================================================
*/
