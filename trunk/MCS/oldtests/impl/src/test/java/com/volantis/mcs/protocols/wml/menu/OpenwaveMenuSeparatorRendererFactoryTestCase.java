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

import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuSeparatorRendererFactoryTestCase;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactory;
import com.volantis.mcs.themes.StyleKeyword;

/**
 * Tests {@link OpenwaveMenuSeparatorRendererFactory}.
 */
public class OpenwaveMenuSeparatorRendererFactoryTestCase
        extends DefaultMenuSeparatorRendererFactoryTestCase {
    // javadoc inherited
    protected MenuSeparatorRendererFactory createFactory() {
        return new OpenwaveMenuSeparatorRendererFactory();
    }

    // javadoc inherited
    protected Class getExpectedHorizontalMenuSeparatorRendererClass(
            StyleKeyword type) {
        return null;
    }

    // javadoc inherited
    protected Class getExpectedVerticalMenuSeparatorRendererClass() {
        return null;
    }

    // javadoc inherited
    protected Class getExpectedVerticalMenuItemSeparatorRendererClass() {
        return null;
    }

    // javadoc inherited
    protected Class getExpectedCharacterMenuItemGroupSeparatorRendererClass() {
        return null;
    }

    // javadoc inherited
    protected Class getExpectedImageMenuItemGroupSeparatorRendererClass() {
        return null;
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

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/3	claire	VBM:2004042204 Implemented remaining required WML renderers

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
