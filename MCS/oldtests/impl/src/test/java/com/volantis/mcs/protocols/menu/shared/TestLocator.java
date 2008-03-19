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
package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.menu.shared.renderer.MenuBufferLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBuffer;
import com.volantis.mcs.protocols.menu.model.MenuEntry;

/**
 * This is a simple buffer locator that provides basic functionality through
 * a generic get buffer operation.  The buffer used (and returned) is the
 * one provided when an instance is created.  To return a different buffer, a
 * new instance of this class needs to be created and initialised appropriately.
 * 
 * @todo this is a duplicate of {@link com.volantis.mcs.protocols.menu.shared.renderer.TestMenuBufferLocator}
 */
public class TestLocator implements MenuBufferLocator {

    /**
     * The buffer returned when requested from an instance of this class, and
     * initialised during construction of the instance.
     */
    private MenuBuffer buffer;

    /**
     * Create a new initialised instance of this class.
     *
     * @param buffer The buffer to use for this instance.
     */
    public TestLocator(MenuBuffer buffer) {
        this.buffer = buffer;
    }

    // JavaDoc inherited
    public MenuBuffer getMenuBuffer(MenuEntry entry) {
        return buffer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	3999/1	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 22-Apr-04	4004/1	claire	VBM:2004042204 Implemented remaining required WML renderers

 ===========================================================================
*/
