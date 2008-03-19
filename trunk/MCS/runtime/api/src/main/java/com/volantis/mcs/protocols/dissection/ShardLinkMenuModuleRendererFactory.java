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

import com.volantis.mcs.protocols.menu.DelegatingMenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactory;

/**
 * A delegating menu module renderer factory which decorates all the classes
 * it creates with shard link versions which in turn decorate the markup that
 * they create with the various SHARD LINK elements.
 */ 
public final class ShardLinkMenuModuleRendererFactory
        extends DelegatingMenuModuleRendererFactory {

    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Create an instance of this class.
     * 
     * @param rendererFactory the factory to delegate to.
     */ 
    public ShardLinkMenuModuleRendererFactory(
            MenuModuleRendererFactory rendererFactory) {
        
        super(rendererFactory);
    }

    /**
     * Returns the delegate's factory decorated with a shard link version 
     * which in turn decorates the output it creates with the SHARD LINK 
     * CONDITIONAL element. 
     */
    // Other javadoc inherited.
    public MenuSeparatorRendererFactory createMenuSeparatorRendererFactory() {
        
        return new ShardLinkMenuSeparatorRendererFactory(
                delegate.createMenuSeparatorRendererFactory());
    }

    /**
     * Returns the delegate's factory decorated with a shard link version 
     * which in turn decorates the output it creates with the SHARD LINK 
     * ELEMENT element. 
     */
    // Other javadoc inherited.
    public MenuItemRendererFactory createMenuItemRendererFactory() {
        
        return new ShardLinkMenuItemRendererFactory(
                delegate.createMenuItemRendererFactory());
    }

    /**
     * Returns the delegate's factory decorated with a shard link version 
     * which in turn decorates the output it creates with the SHARD LINK 
     * GROUP element. 
     */
    // Other javadoc inherited.
    public MenuBracketingRenderer createMenuBracketingRenderer() {
        
        return new ShardLinkMenuBracketingRenderer(
                delegate.createMenuBracketingRenderer());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-May-04	4315/4	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
