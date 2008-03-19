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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

import com.volantis.mcs.protocols.OutputBuffer;

/**
 * A stack of {@link OutputBuffer}s.
 *
 * @mock.generate
 */
public interface OutputBufferStack {
    /**
     * PAPI: Push the specified OutputBuffer onto the top of the stack.
     * @param outputBuffer The OutputBuffer to push onto the stack.
     */
    void pushOutputBuffer (OutputBuffer outputBuffer);

    /** PAPI: Pop the current outputBuffer from the top of the stack.
     * @param expectedOutputBuffer The OutputBuffer which is expected to be
     * popped. If this is not null and not equal to the outputBuffer on the
     * top of the stack then throw an IllegalStateException.
     */
    void popOutputBuffer (OutputBuffer expectedOutputBuffer);

    /**
     * PAPI: Get the current outputBuffer from the top of the stack.
     * @return The OutputBuffer which is on the top of the stack.
     */
    OutputBuffer getCurrentOutputBuffer ();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
