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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.separator.SeparatorManager;

/**
 * Implementations of this are used to annotate an OutputBuffer with additional
 * menu specific information.
 *
 * <p><strong>NOTE:</strong> it is likely that additional state data will be
 * added to handle separators and indentation.</p>
 */
public interface MenuBuffer {

    /**
     * Get the <code>OutputBuffer</code> that is wrapped by this menu specific
     * buffer.
     *
     * @return The wrapped <code>OutputBuffer</code>, will not be null.
     */
    public OutputBuffer getOutputBuffer();

    /**
     * Get the separator manager for menus.
     *
     * @return The SeparatorManager for menus.
     */
    public SeparatorManager getSeparatorManager();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 10-May-04	4164/3	pduffin	VBM:2004050404 Fixed problems with test cases, specifically those caused by ConcreteMenuBuffer throwing an UnsupportedOperationException

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 21-Apr-04	3681/4	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 08-Apr-04	3514/2	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 26-Mar-04	3612/2	pduffin	VBM:2004032508 Definition of menu renderer API

 ===========================================================================
*/
