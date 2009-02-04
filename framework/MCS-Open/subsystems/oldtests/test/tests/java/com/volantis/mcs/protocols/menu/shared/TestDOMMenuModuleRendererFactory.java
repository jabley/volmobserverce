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
package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.menu.TestMenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.TestDeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.TestMenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.TestMenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.renderer.TestRendererContext;

public class TestDOMMenuModuleRendererFactory 
        extends DefaultMenuModuleRendererFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    public TestDOMMenuModuleRendererFactory() {
        super(new TestRendererContext(null),
              new TestDeprecatedOutputLocator(),
              new TestMenuModuleCustomisation());
    }

    // Javadoc inherited.
    public MenuItemRendererFactory createMenuItemRendererFactory() {
        return new TestMenuItemRendererFactory(
                context.getOutputBufferFactory());
    }

    // Javadoc inherited.
    public MenuSeparatorRendererFactory createMenuSeparatorRendererFactory() {
        return new TestMenuSeparatorRendererFactory();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
