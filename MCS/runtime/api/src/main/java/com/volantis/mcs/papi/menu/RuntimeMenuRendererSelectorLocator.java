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

package com.volantis.mcs.papi.menu;

import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.context.MarinerPageContext;

/**
 * The MenuRendererSelectorLocator that is used at runtime.
 */
public class RuntimeMenuRendererSelectorLocator
    extends MenuRendererSelectorLocator {

    // JavaDoc inherited
    public MenuRendererSelector getMenuRendererSelector
            (MarinerPageContext pageContext) {

        MenuModule module = pageContext.getProtocol().getMenuModule();
        return module.getMenuRendererSelector();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 29-Apr-04	4091/1	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 15-Mar-04	3274/7	pduffin	VBM:2004022704 Minor formatting and javadoc changes

 11-Mar-04	3274/1	pduffin	VBM:2004022704 Initial API for menu renderers and markup generators

 ===========================================================================
*/
