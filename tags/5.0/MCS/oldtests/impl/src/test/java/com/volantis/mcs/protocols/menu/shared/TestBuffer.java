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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.separator.SeparatorManager;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBuffer;

/**
 * This is a very simplistic implementation of a MenuBuffer used in the tests.
 * It uses a DOM based output buffer.  Only one buffer exists for the lifetime
 * of an instance of this class.
 */
public class TestBuffer implements MenuBuffer {

    /**
     * The buffer returned when requesting the buffer from an instance of this
     * class.
     */
    private OutputBuffer buffer = new TestDOMOutputBuffer();

    /**
     * Create a new initialised instance of this class.
     */
    public TestBuffer() {
    }

    // JavaDoc inherited
    public OutputBuffer getOutputBuffer() {
        return buffer;
    }

    public SeparatorManager getSeparatorManager() {
        throw new UnsupportedOperationException();
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

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 22-Apr-04	4004/1	claire	VBM:2004042204 Implemented remaining required WML renderers

 ===========================================================================
*/
