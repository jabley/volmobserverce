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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Test the InputStreamFactory
 */
public class InputStreamFactoryTestCase extends TestCaseAbstract {
    /**
     * Test the get input stream.
     */
    public void testGetInputStream() throws Exception {
        InputStreamFactory factory = new InputStreamFactory();
        InputStream result;
        try {
            result = factory.getInputStream(null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // deliberately ignored.
        }
        byte[] buf = new byte[10];
        InputStream input = new ByteArrayInputStream(buf);
        result = factory.getInputStream(input, null);
        assertNotNull("Non-null result expected: ", result);
        assertEquals("Value should match", result, input);

        result = factory.getInputStream(input, "unknown");
        assertNotNull("Non-null result expected: ", result);
        assertEquals("Value should match", result, input);

        final String inputString = "compressed string";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(inputString.getBytes());
        gzip.flush();
        gzip.close();

        byte[] byteOutput = out.toByteArray();
        input = new ByteArrayInputStream(byteOutput);
        result = factory.getInputStream(input, "gZIP");
        assertNotNull("Non-null result expected: ", result);
        assertTrue("Value should match", result instanceof GZIPInputStream);

        byte[] bytesResult = new byte[byteOutput.length];
        result.read(bytesResult);
        result.close();
        assertEquals("Decompressed result should match original input",
                     inputString,
                     new String(bytesResult).trim());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Sep-04	888/1	byron	VBM:2004092003 DSB:HTTPS Requests not working

 ===========================================================================
*/
