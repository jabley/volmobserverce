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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.xml.utilities.sax.stream;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

/**
 * Tests the AddRootElementReader class
 */
public class AddRootElementReaderTestCase extends TestCase {

    /**
     * The namespaces to use
     */
    private TreeMap namespaces;

    public AddRootElementReaderTestCase(String name) {
        super(name);
    }

    /**
     * Set up the namespaces
     */
    public void setUp() {
        namespaces = new TreeMap();
        namespaces.put("", "http://defaultNS");
        namespaces.put("aaaa", "http://otherNS");
    }

    /**
     * Tests the reading of an empty reader
     */
    public void testReadBufferEmpty() {

        Reader reader = doConfig("", null, null);
        String result = doRead(reader, -1);
        assertEquals("<fragment></fragment>", result);
    }

    /**
     * Tests the reading of an empty reader with namespaces
     */
    public void testReadBufferEmptyNS() {
        Reader reader = doConfig("", null, namespaces);
        String result = doRead(reader, -1);
        assertEquals("<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\"></fragment>", result);
    }

    /**
     * Tests the reading of characters from the stream
     */
    public void testReadBuffer() {
        String input = "<numbers>123456789012</numbers>";
        Reader reader = doConfig(input, null, null);
        String result = doRead(reader, -1);
        assertEquals("<fragment>"+input+"</fragment>", result);
    }

    /**
     * Tests the reading of characters from the reader with Namespaces
     */
    public void testReadBufferNS() {
        String input = "<numbers>123456789012</numbers>";
        Reader reader = doConfig(input, null, namespaces);
        String result = doRead(reader, -1);
        assertEquals("<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\">"+input+"</fragment>",
                     result);
    }


    /**
     * Test more than one read to the underlying Reader
     */
    public void testMultipleRead() {
        String input = "<numbers>123456789012</numbers>";
        Reader reader = doConfig(input, null, null);
        StringBuffer answer = new StringBuffer();
        // read three bytes
        String result = doRead(reader, 3);
        answer.append(result);
        // read the rest of the stream
        result = doRead(reader, -1);
        answer.append(result);

        assertEquals("Output is not as expected",
                     "<fragment>"+input+"</fragment>",
                     answer.toString());
    }

    /**
     * Test more than one read to the underlying Reader with namespaces
     */
    public void testMultipleReadNS() {
        String input = "<numbers>123456789012</numbers>";
        Reader reader = doConfig(input, null, namespaces);
        StringBuffer answer = new StringBuffer();
        // read three bytes
        String result = doRead(reader, 3);
        answer.append(result);
        // read the rest of the stream
        result = doRead(reader, -1);
        answer.append(result);

        assertEquals("Output is not as expected",
                     "<fragment xmlns=\"http://defaultNS\" xmlns:aaaa=\"http://otherNS\">" + input + "</fragment>",
                     answer.toString());
    }

    /**
     * Configure a reader ready for testing.
     *
     * @param streamContents
     * @param namespaces
     * @return
     */
    public Reader doConfig(
        String streamContents, String namespacePrefix, Map namespaces) {
        ByteArrayInputStream bae = new ByteArrayInputStream(streamContents.getBytes());
        Reader r = new InputStreamReader(bae);

        Reader result = null;
        try {
            if (namespaces == null || namespaces.size() < 1) {
                result = new AddRootElementReader(r);
            } else {
                result = new AddRootElementReader(r, namespacePrefix, namespaces);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create new AddRootElementReader: "
                 + e.toString());
        }
        return result;
    }

    /**
     * Read the specified number of bytes from the specified reader. If
     * numBytes < 0 then read all from the input stream
     *
     * @param reader       the reader to read from
     * @param numBytes the number of bytes to read or -1 for all bytes
     * @return the contents of the inputstream as a string
     */
    public String doRead(Reader reader, int numBytes) {
        StringBuffer result = new StringBuffer();
        try {
            if (numBytes >= 0) {
                int value = 0;
                int count = 0;
                while (count < numBytes && (value = reader.read()) != -1 ) {
                    result.append((char) value);
                    count++;
                }
            } else {
                // read all
                int value = 0;
                while ((value = reader.read()) != -1) {
                    result.append((char) value);
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
            fail("Failed to read from AddRootElementReader: "
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

 16-Jun-03	23/4	byron	VBM:2003022819 Update to get jsp TLD files with correct merge

 13-Jun-03	23/2	byron	VBM:2003022819 Integration complete

 ===========================================================================
*/
