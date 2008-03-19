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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;




/**
 * Test the functionality of the StringOutputBuffer
 */
public class StringOutputBufferTestCase extends OutputBufferTestAbstract {

    /**
     * Test writing to a string buffer, checking that the correct value is
     * written and that appending text works correctly.
     * @throws Exception if an error occurs
     */
    public void testWriteToBuffer() throws Exception {
        String testString1 = "Simple test string 1.";
        String testString2 = "Simple test string 2.";
        StringOutputBuffer buf = new StringOutputBuffer();
        assertTrue("Freshly created buffer should be empty", buf.isEmpty());
        buf.writeText(testString1);
        assertFalse("Buffer with content should not be empty", buf.isEmpty());
        assertEquals("Buffer should contain text written to it", testString1,
                buf.getBuffer().toString());
        buf.writeText(testString2);
        assertEquals("Additional written text should be appended",
                testString1 + testString2, buf.getBuffer().toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jul-04	4868/2	adrianj	VBM:2004070509 Implemented missing writeText(...) methods in StringOutputBuffer

 13-Jul-04	4862/1	adrianj	VBM:2004070509 Implemented missing writeText(...) methods in StringBuffer

 ===========================================================================
*/
