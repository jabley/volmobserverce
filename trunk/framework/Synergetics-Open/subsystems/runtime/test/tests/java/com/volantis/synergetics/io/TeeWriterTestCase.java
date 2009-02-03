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

package com.volantis.synergetics.io;

import junit.framework.TestCase;

import java.io.StringWriter;

/**
 * Test the TeeWriter class.
 */
public class TeeWriterTestCase extends TestCase {

    /**
     * Construct a new instance of TeeOutputStreamTestCase
     */
    public TeeWriterTestCase(String name) {
        super(name);
    }

    /**
     * Test that data written to one writer is written to both.
     *
     * @throws Exception if an error occurs
     */
    public void testWriteData() throws Exception {
        StringWriter writer1 = new StringWriter();
        StringWriter writer2 = new StringWriter();

        TeeWriter writer = new TeeWriter(writer1, writer2);

        // Write as String
        writer.write("Testing");

        // Write as indexed String content
        writer.write("Romeo, O Romeo...", 5, 2);

        // Write as character
        writer.write('t');

        // Write as index from character array
        char[] arr1 = {
            'N', 'e', 's', 't', 'i', 'n', 'g', '!'
        };
        writer.write(arr1, 1, 6);

        // Write as character array
        char[] arr2 = {
            '.', ' ', 'O', 'n', 'e', ' ', 't', 'w', 'o', '.'
        };
        writer.write(arr2);

        // Flush
        writer.flush();

        // Close
        writer.close();

        // Check that everything has been written to the String writers
        String expected = "Testing, testing. One two.";
        assertEquals("Results should be same as input", expected,
                     writer1.toString());
        assertEquals("Results should be same as input", expected,
                     writer2.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Feb-05	393/1	adrianj	VBM:2005012506 Added TeeWriter and TeeOutputStream

 ===========================================================================
*/
