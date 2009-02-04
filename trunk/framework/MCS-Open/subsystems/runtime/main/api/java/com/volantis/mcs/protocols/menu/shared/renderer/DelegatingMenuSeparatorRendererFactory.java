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
 * Delegates all methods to the passed in delegate.
 */
public class DelegatingMenuSeparatorRendererFactory
        implements MenuSeparatorRendererFactory {

    private final MenuSeparatorRendererFactory delegate;

    protected DelegatingMenuSeparatorRendererFactory(
            MenuSeparatorRendererFactory delegate) {

        this.delegate = delegate;
    }

    public SeparatorRenderer createHorizontalMenuSeparator(StyleValue separatorType) {
        return delegate.createHorizontalMenuSeparator(separatorType);
    }

    public SeparatorRenderer createVerticalMenuSeparator() {
        return delegate.createVerticalMenuSeparator();
    }

    public SeparatorRenderer createHorizontalMenuItemSeparator(
            StyleValue separatorType) {
        return delegate.createHorizontalMenuItemSeparator(separatorType);
    }

    public SeparatorRenderer createVerticalMenuItemSeparator() {
        return delegate.createVerticalMenuItemSeparator();
    }

    public SeparatorRenderer createCharacterMenuItemGroupSeparator(
            String chars, int repeat) {
        return delegate.createCharacterMenuItemGroupSeparator(chars, repeat);
    }

    public SeparatorRenderer createImageMenuItemGroupSeparator(
            ImageAssetReference imageAssetReference) {
        return delegate.createImageMenuItemGroupSeparator(imageAssetReference);
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

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 ===========================================================================
*/
