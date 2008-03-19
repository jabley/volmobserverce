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
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.menu.renderer.MenuBufferTestAbstract;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * Test the ConcreteMenuBuffer implementation.
 */
public class ConcreteMenuBufferTestCase
    extends MenuBufferTestAbstract {

    /**
     * Create an OutputBuffer that can be wrapped within a MenuBuffer.
     * @return The OutputBuffer.
     */
    protected OutputBuffer createOutputBuffer() {
        return new TestDOMOutputBuffer();
    }

    /**
     * Create a new ConcreteMenuBuffer that wraps an OutputBuffer.
     * @return The new ConcreteMenuBuffer.
     */
    protected MenuBuffer createMenuBuffer(OutputBuffer outputBuffer) {
        return new ConcreteMenuBuffer(outputBuffer, SeparatorRenderer.NULL);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 26-Mar-04	3612/1	pduffin	VBM:2004032508 Definition of menu renderer API

 ===========================================================================
*/
