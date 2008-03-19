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

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuEntry;

/**
 * A factory used by the {@link DefaultMenuBufferLocator} to construct and
 * initialize the {@link MenuBuffer MenuBuffers} required.
 */
public interface MenuBufferFactory {
    /**
     * Creates, initializes and returns a MenuBuffer based on the given output
     * buffer and any other required data defined by the given entry, the
     * format reference or the factory implementation's state data.
     *
     * @param entry the menu entry for which a menu buffer is to be created.
     *              May not be null. FOR TESTING PURPOSES ONLY
     * @param formatReference
     *              the format reference actually associated with the given
     *              entry. May not be null. FOR TESTING PURPOSES ONLY
     * @param outputBuffer
     *              the output buffer that the menu buffer is to wrap. May not
     *              be null
     * @return the required menu buffer. May not be null
     */
    MenuBuffer createMenuBuffer(MenuEntry entry,
                                FormatReference formatReference,
                                OutputBuffer outputBuffer);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 06-May-04	3999/4	philws	VBM:2004042202 Review updates

 06-May-04	3999/2	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
