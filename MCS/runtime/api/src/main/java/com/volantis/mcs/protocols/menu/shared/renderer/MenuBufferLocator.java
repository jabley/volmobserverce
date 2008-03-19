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

import com.volantis.mcs.protocols.menu.model.MenuEntry;

/**
 * Implementations of this are responsible for providing {@link MenuBuffer}s
 * for various menu model entities.
 */
public interface MenuBufferLocator {
    /**
     * Get the buffer for the specified menu entry.
     *
     * @param entry The entry for which the buffer is required.
     * @return The menu buffer, may be null in which case the entry is ignored.
     */
    public MenuBuffer getMenuBuffer(MenuEntry entry);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-May-04	3999/1	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 21-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 08-Apr-04	3514/3	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 26-Mar-04	3612/1	pduffin	VBM:2004032508 Definition of menu renderer API

 ===========================================================================
*/
