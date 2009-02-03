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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 07-May-2003  Sumit       VBM:2003050606 - Created
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.xml.utilities.sax.stream;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Tests the AddRootElementInputStream class
 */
public class AddRootElementInputStreamTestCase extends TestCase {

    /**
     * The namespaces to use
     */
    private  TreeMap namespaces;

    /**
     * 
     */
    public AddRootElementInputStreamTestCase(String name) {
        super(name);
    }

    /**
     * Set up the namespaces
     */
    public void setUp() {
        namespaces= new TreeMap();
        namespaces.put("", "http://defaultNS");
        namespaces.put("aaaa", "http://otherNS");
    }

    /**
     * Tests the reading of an empty buffer
     */
    public void testReadBufferEmpty() {
        InputStream is = doConfig("", null, null);
        String result = doRead(is, -1);
        assertEquals("<fragment></fragment>", result);
    }

    /**
     * Tests the reading of an empty buffer with namespaces defined
     */
    public void testReadBufferEmptyNS() {

        InputStream is = doConfig("", null, namespaces);
        String result = doRead(is, -1);
        assertEquals("<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\"></fragment>", result);
    }

    /**
     * Tests the reading of an empty buffer with namespaces and prefix defined
     */
    public void testReadBufferEmptyNSPF() {

        InputStream is = doConfig("", "jabba", namespaces);
        String result = doRead(is, -1);
        assertEquals("<jabba:fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\"></jabba:fragment>",
                     result);
    }

    /**
     * Tests the reading of exactly 10 characters
     */
    public void testReadBufferSize10() {
        String input = "1234567890";
        InputStream is = doConfig(input, null, null);
        String result = doRead(is, -1);

        assertEquals("<fragment>" + input + "</fragment>", result);
    }

    /**
     * Tests the reading of exactly 10 characters with namespaces
     */
    public void testReadBufferSize10NS() {
        String input = "1234567890";
        InputStream is = doConfig(input, null, namespaces);
        String result = doRead(is, -1);

        assertEquals("<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\">"+input+"</fragment>", result);
    }

    /**
     * Tests the reading of more than 10 characters but return only 6
     * available
     */
    public void testReadBufferSizeMoreThan10() {

        String input = "<numbers>123456789012</numbers>";
        ByteArrayInputStream bae =
            new ByteArrayInputStream(input.getBytes()) {
                /* (non-Javadoc)
                * @see java.io.ByteArrayInputStream#available()
                */
                public synchronized int available() {
                    // return only 6 characters available so that the buffer
                    // of the subject will be smaller than the actual stream
                    return 6;
                }
            };

        AddRootElementInputStream subject = null;
        try {
            subject = new AddRootElementInputStream(bae);
        } catch (IOException ie) {
            ie.printStackTrace();
            fail("Failed to create new AddRootElementInputStream: "
                 + ie.toString());
        }

        String result = doRead(subject, -1);
        assertEquals("<fragment><numbers>123456789012</numbers></fragment>", result);
    }

    /**
     * Tests the reading of more than 10 characters but return only 6
     * available with namespaces
     */
    public void testReadBufferSizeMoreThan10NS() {

        ByteArrayInputStream bae =
            new ByteArrayInputStream("<numbers>123456789012</numbers>".getBytes()) {
                /* (non-Javadoc)
                * @see java.io.ByteArrayInputStream#available()
                */
                public synchronized int available() {
                    // return only 6 characters available so that the buffer
                    // of the subject will be smaller than the actual stream
                    return 6;
                }
            };

        AddRootElementInputStream subject = null;
        try {
            subject = new AddRootElementInputStream(bae, null, namespaces);
        } catch (IOException ie) {
            ie.printStackTrace();
            fail("Failed to create new AddRootElementInputStream: "
                 + ie.toString());
        }

        String result = doRead(subject, -1);
        assertEquals("<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\"><numbers>123456789012</numbers></fragment>",
                     result);
    }

    /**
     * Test more than one read to the underlying InputStream
     */
    public void testMultipleRead() {

        InputStream is = doConfig("<numbers>123456789012</numbers>", null, null);
        StringBuffer answer = new StringBuffer();
        // read three bytes
        String result = doRead(is, 3);
        answer.append(result);
        // read the rest of the stream
        result = doRead(is, -1);
        answer.append(result);

        assertEquals("Output is not as expected",
                     "<fragment><numbers>123456789012</numbers></fragment>",
                     answer.toString());

        try {
            int returnValue = is.read();
            assertEquals("return value should be -1", -1, returnValue);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }

    }

    /**
     * Test more than one read to the underlying InputStream
     */
    public void testMultipleReadNS() {

        InputStream is = doConfig("<numbers>123456789012</numbers>", null, namespaces);
        StringBuffer answer = new StringBuffer();
        // read three bytes
        String result = doRead(is, 3);
        answer.append(result);
        // read the rest of the stream
        result = doRead(is, -1);
        answer.append(result);

        assertEquals("Output is not as expected",
                     "<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\"><numbers>123456789012</numbers></fragment>",
                     answer.toString());
        try {
            int returnValue = is.read();
            assertEquals("return value should be -1", -1, returnValue);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }

    }


    /**
     * Configure a inputstream ready for testing.
     * @param streamContents
     * @param fragmentNamespace the namepsace prefix to apply to the "fragment"
     * element
     * @param namespaces
     * @return
     */
    public InputStream doConfig(String streamContents,
                                String fragmentNamespace, Map namespaces) {
        ByteArrayInputStream bae = new ByteArrayInputStream(streamContents.getBytes());

        InputStream result = null;
        try {
            if (namespaces == null || namespaces.size() < 1) {
                result = new AddRootElementInputStream(bae);
            } else {
                result = new AddRootElementInputStream(
                    bae, fragmentNamespace, namespaces);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create new AddRootElementInputStream: "
                 + e.toString());
        }
        return result;
    }

    /**
     * Read the specified number of bytes from the specified inputstream. If
     * numBytes < 0 then read all from the input stream
     *
     * @param is the stream to read from
     * @param numBytes the number of bytes to read or -1 for all bytes
     * @return the contents of the inputstream as a string
     */
    public String doRead(InputStream is, int numBytes) {
        StringBuffer result = new StringBuffer();
        try {
            if (numBytes >= 0) {
                int value = 0;
                int count = 0;
                while (count < numBytes && (value = is.read()) != -1 ) {
                    result.append((char) value);
                    count++;
                }
            } else {
                // read all
                int value = 0;
                while ((value = is.read()) != -1) {
                    result.append((char) value);
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
            fail("Failed to read from AddRootElementInputStream: "
                 + ie.toString());
        }
        return result.toString();
    }


}
        
        
 
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
