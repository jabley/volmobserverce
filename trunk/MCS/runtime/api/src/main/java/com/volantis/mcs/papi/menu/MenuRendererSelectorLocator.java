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
import com.volantis.mcs.context.MarinerPageContext;

/**
 * Class that is responsible for locating a MenuRendererSelector.
 *
 * <p>The purpose of this is to allow test cases to customise this part of the
 * behaviour of PAPI without also having to customise MarinerPageContext.</p>
 *
 * @mock.generate 
 */
public abstract class MenuRendererSelectorLocator {

    /**
     * The default menu renderer selector instance.
     * <p>This can be changed using the {@link #setDefaultInstance} method.
     */
    private static MenuRendererSelectorLocator defaultInstance
        = new RuntimeMenuRendererSelectorLocator();

    /**
     * Get the default instance of the menu renderer selector.
     *
     * @return The default instance of the menu renderer selector.
     */
    public static MenuRendererSelectorLocator getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Set the default MenuRendererSelectorLocator instance.
     * @param locator The new default MenuRendererSelectorLocator.
     * @return The previous version.
     */
    public static MenuRendererSelectorLocator setDefaultInstance
        (MenuRendererSelectorLocator locator) {

        if (locator == null) {
            throw new IllegalStateException
                ("Default MenuRendererSelectorLocator may not be null");
        }

        MenuRendererSelectorLocator old = defaultInstance;
        defaultInstance = locator;

        return old;
    }

    /**
     * Get the menu renderer selector from the MarinerPageContext.
     * @param context The MarinerPageContext from which the selector should
     * be retrieved, may not be null.
     * @return The MenuRendererSelector instance to use, may not be null.
     */
    public abstract
        MenuRendererSelector getMenuRendererSelector(MarinerPageContext context);
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Mar-04	3274/8	pduffin	VBM:2004022704 Minor formatting and javadoc changes

 11-Mar-04	3274/2	pduffin	VBM:2004022704 Initial API for menu renderers and markup generators

 ===========================================================================
*/
