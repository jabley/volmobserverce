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
package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.protocols.menu.shared.renderer.DelegatingMenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StyleValue;

/**
 * A decorating menu separator renderer factory for shard links which 
 * decorates the menu item separator renderers returned with shard link 
 * separator renderers which add the special shard link markup expected by the 
 * dissector for the shard link menu separators. 
 */ 
public class ShardLinkMenuSeparatorRendererFactory 
        extends DelegatingMenuSeparatorRendererFactory {

    /**
     * Construct an instance of this class.
     * 
     * @param delegate the normal factory which we are decorating.
     */ 
    public ShardLinkMenuSeparatorRendererFactory(
            MenuSeparatorRendererFactory delegate) {
        super(delegate);
    }

    // Javadoc inherited.
    public SeparatorRenderer createHorizontalMenuSeparator(StyleValue separatorType) {
        return new ShardLinkSeparatorRenderer(
                super.createHorizontalMenuSeparator(separatorType));
    }

    // Javadoc inherited.
    public SeparatorRenderer createVerticalMenuSeparator() {
        return new ShardLinkSeparatorRenderer(
                super.createVerticalMenuSeparator());
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

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
