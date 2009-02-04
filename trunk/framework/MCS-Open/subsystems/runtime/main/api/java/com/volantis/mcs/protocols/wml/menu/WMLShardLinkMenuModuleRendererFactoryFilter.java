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
package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.protocols.dissection.ShardLinkMenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;

/**
 * "Filters" a menu module renderer factory to add shard link related
 * decorators for WML.
 * <p/>
 * This means that the returned factory will return objects which render
 * SHARD LINK elements around the various parts of markup as required, and also
 * fix anything in particular which is necessary for WML shard link menus,
 * compared to normal WML menus.
 */
public class WMLShardLinkMenuModuleRendererFactoryFilter
        implements MenuModuleRendererFactoryFilter {

    // Javadoc inherited.
    public MenuModuleRendererFactory decorate(
            MenuModuleRendererFactory rendererFactory) {

        // Decorate the original factory with a simple class which will fix 
        // the menu bracketing renderer to <p mode=wrap> specifically for WML.
        WMLShardLinkMenuModuleRendererFactory wmlRendererFactory =
                new WMLShardLinkMenuModuleRendererFactory(rendererFactory);

        // Decorate the amended original factory with a class which will
        // decorate the classes it creates with shard link ones which will
        // in turn decorate the output markup with SHARD LINK elements. Phew!
        return new ShardLinkMenuModuleRendererFactory(wmlRendererFactory);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
