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

package com.volantis.mcs.protocols.menu.renderer;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBuffer;

/**
 * Base class of all menu buffer tests.
 * <p>Currently this simply tests that buffers never have a null
 * OutputBuffer and that the OutputBuffer returned is the same as the one
 * supplied..</p>
 */
public abstract class MenuBufferTestAbstract
    extends TestCaseAbstract {

    /**
     * Factory method for creating OutputBuffers.
     * @return The OutputBuffer that was created, may not be null.
     */
    protected abstract OutputBuffer createOutputBuffer();

    /**
     * Factory method for creating MenuBuffers.
     * @param buffer The OutputBuffer to wrap.
     * @return The MenuBuffer that was created, may not be null.
     */
    protected abstract MenuBuffer createMenuBuffer(OutputBuffer buffer);

    /**
     * Tests that the OutputBuffer contained within the MenuBuffer is not null.
     */
    public void testNotNullOutputBuffer() {
        MenuBuffer buffer = createMenuBuffer(createOutputBuffer());
        assertNotNull("MenuBuffer must not be null", buffer);

        assertNotNull("OutputBuffer must not be null", buffer.getOutputBuffer());
    }

    /**
     * Tests that the OutputBuffer contained within the MenuBuffer is not null.
     */
    public void testConstructionDetectsNullOutputBuffer() {
        try {
            createMenuBuffer(null);
            fail("Expected constructor to detect null OutputBuffer");
        } catch (IllegalArgumentException iae) {
            // Expected exception occurred.
        }
    }

    /**
     * Tests that the OutputBuffer returned is the same as the one supplied.
     */
    public void testOutputBufferSame() {
        OutputBuffer outputBuffer = createOutputBuffer();
        assertNotNull(outputBuffer);

        MenuBuffer buffer = createMenuBuffer(outputBuffer);
        assertNotNull(buffer);

        assertSame("OutputBuffer returned not same as that supplied",
                   outputBuffer, buffer.getOutputBuffer());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 26-Mar-04	3612/1	pduffin	VBM:2004032508 Definition of menu renderer API

 ===========================================================================
*/
