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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StyleValue;

/**
 * A very simple implementation of {@link MenuSeparatorRendererFactory}.
 * <p> 
 * All it does is return separator renderers which render as a simple string 
 * which is easy to check for in a unit test.
 */ 
public class TestSimpleMenuSeparatorRendererFactory 
        implements MenuSeparatorRendererFactory {

    // Javadoc inherited.
    public SeparatorRenderer createHorizontalMenuSeparator(
            StyleValue separatorType) {
        
        return new TestSimpleSeparatorRenderer("[separator-menu-horizontal:" + 
                separatorType.getStandardCSS() + "]");
    }

    // Javadoc inherited.
    public SeparatorRenderer createVerticalMenuSeparator() {
        
        return new TestSimpleSeparatorRenderer("[separator-menu-vertical]");
    }

    // Javadoc inherited.
    public SeparatorRenderer createHorizontalMenuItemSeparator(
            StyleValue separatorType) {
        
        return new TestSimpleSeparatorRenderer("[separator-item-horizontal:" +
                separatorType.getStandardCSS() + "]");
    }

    // Javadoc inherited.
    public SeparatorRenderer createVerticalMenuItemSeparator() {
        
        return new TestSimpleSeparatorRenderer("[separator-item-vertical]");
    }

    // Javadoc inherited.
    public SeparatorRenderer createCharacterMenuItemGroupSeparator(
            String chars, int repeat) {
        
        return new TestSimpleSeparatorRenderer("[separator-character:" + chars +
                "," + repeat + "]");
    }

    // Javadoc inherited.
    public SeparatorRenderer createImageMenuItemGroupSeparator(
            ImageAssetReference imageAssetReference) {
        
        throw new UnsupportedOperationException("not implemented yet");
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

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
