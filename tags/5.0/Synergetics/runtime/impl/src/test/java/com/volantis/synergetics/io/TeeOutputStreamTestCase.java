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

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Test the TeeOutputStream class.
 */
public class TeeOutputStreamTestCase extends TestCase {

    /**
     * Construct a new instance of TeeOutputStreamTestCase
     */
    public TeeOutputStreamTestCase(String name) {
        super(name);
    }

    /**
     * Test that data written to one stream is written to both.
     *
     * @throws Exception if an error occurs
     */
    public void testWriteData() throws Exception {
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

        // Write as single byte
        byte[] array = new byte[50];
        for (byte b = 0; b < 50; b++) {
            array[b] = b;
        }

        // Write as array of bytes with offset/length
        TeeOutputStream strm = new TeeOutputStream(baos1, baos2);
        for (int i = 0; i < 20; i++) {
            strm.write(array[i]);
        }
        strm.write(array, 20, 10);

        // Write as array of bytes
        byte[] arrayFrom30 = new byte[20];
        for (int i = 0; i < arrayFrom30.length; i++) {
            arrayFrom30[i] = array[i + 30];
        }
        strm.write(arrayFrom30);

        // Flush
        strm.flush();

        // Close
        strm.close();

        // Check that everything has been written to the byte array
        // correctly.
        byte[] res1 = baos1.toByteArray();
        byte[] res2 = baos2.toByteArray();

        assertTrue("Results should be same input", Arrays.equals(array, res1));
        assertTrue("Results should be same input", Arrays.equals(array, res2));
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
