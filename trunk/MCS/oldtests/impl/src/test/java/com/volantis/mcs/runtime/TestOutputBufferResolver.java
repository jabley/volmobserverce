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

package com.volantis.mcs.runtime;

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.DOMOutputBuffer;

/**
 * Test implementation that returns a DOM castable OutputBuffer from any call to
 * {link #resolvePaneOutputBuffer}.
 */
public class TestOutputBufferResolver
        implements OutputBufferResolver {

    private final OutputBuffer buffer;

    /**
     * Initialise a new instance of this test helper class.
     */
    public TestOutputBufferResolver(OutputBuffer buffer) {
        this.buffer = buffer;
    }

    // JavaDoc inherited
    public OutputBuffer resolvePaneOutputBuffer(
            FormatReference paneFormatReference) {
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

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
