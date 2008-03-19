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
package com.volantis.mcs.utilities;

import junit.framework.TestCase;

/**
 * ReusableStringBufferTestCase
 */
public class ReusableStringBufferTestCase extends TestCase {

    public static void main(String[] args) {
    }

    /*
     * Class to test for void ReusableStringBuffer()
     */
    public void testReusableStringBuffer() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        assertEquals("Incorrect Length", 0, buff.length());
        assertEquals("Incorrect Capacity", 16, buff.capacity());
        
        char[] chrs = buff.getChars();
        assertEquals("Incorrect array length", 16, chrs.length);
        
        int i = 0;
        try {
            for (i = 0; i < 17; i++) {
                if (chrs[i] != '\0') {
                    fail("All characters should be \\u00");
                }
            }
        } catch (RuntimeException e) {
            assertEquals("Exception expected after 15th element", 16, i);
        }
        
    }

    /*
     * Class to test for void ReusableStringBuffer(int)
     */
    public void testReusableStringBufferint() {
        ReusableStringBuffer buff = new ReusableStringBuffer(20);
        assertEquals("Incorrect Length", 0, buff.length());
        assertEquals("Incorrect Capacity", 20, buff.capacity());
        
        char[] chrs = buff.getChars();
        assertEquals("Incorrect array length", 20, chrs.length);
        
        int i = 0;
        try {
            for (i = 0; i < 21; i++) {
                if (chrs[i] != '\0') {
                    fail("All characters should be \\u00");
                }
            }
        } catch (RuntimeException e) {
            assertEquals("Exception expected after 20th element", 20, i);
        }
    }

    /*
     * Class to test for void ReusableStringBuffer(String)
     */
    public void testReusableStringBufferString() {
        String data = "Test Case";
        
        ReusableStringBuffer buff = new ReusableStringBuffer(data);
        assertEquals("Incorrect Length", data.length(), buff.length());
        assertEquals("Incorrect Capacity", data.length()* 2, buff.capacity());
        
        char[] chrs = buff.getChars();
        assertEquals("Incorrect array length", data.length()* 2, chrs.length);
        
        int i = 0;
        for (i = 0; i < data.length() * 2; i++) {
            if (i < data.length()) {
                assertEquals("Incorrect Character", data.charAt(i), chrs[i]);
            } else {
                assertEquals("Trailing characters should be \\u00", 0, chrs[i]);
            }
        }
    }

    public void testLength() {
        String data = "Test Case";

        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append(data);
        int length = data.length();
        assertEquals("Incorrect Length", length, buff.length());
        buff.append('s');
        length++;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(data.toCharArray());
        length += data.length();
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(data.toCharArray(), 1, 2);
        length += 2;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(true);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(5);
        length += 1;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(100L);
        length += 3;
        assertEquals("Incorrect Length", length, buff.length());
        ReusableStringBuffer rsb = new ReusableStringBuffer("Fred");
        buff.append(rsb);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());
        StringBuffer sb = new StringBuffer("Fred");
        buff.append(sb);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());        
    }

    public void testLengthInt() {
        String data = "Test Case";

        ReusableStringBuffer buff = new ReusableStringBuffer(200);
        buff.append(data);
        int length = data.length();
        assertEquals("Incorrect Length", length, buff.length());
        buff.append('s');
        length++;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(data.toCharArray());
        length += data.length();
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(data.toCharArray(), 1, 2);
        length += 2;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(true);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(5);
        length += 1;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(100L);
        length += 3;
        assertEquals("Incorrect Length", length, buff.length());
        ReusableStringBuffer rsb = new ReusableStringBuffer("Fred");
        buff.append(rsb);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());
        StringBuffer sb = new StringBuffer("Fred");
        buff.append(sb);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());        
    }
    
    public void testLengthString() {
        String data = "Test Case";

        ReusableStringBuffer buff = new ReusableStringBuffer(data);
        int length = data.length();
        assertEquals("Incorrect Length", length, buff.length());
        buff.append('s');
        length++;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(data.toCharArray());
        length += data.length();
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(data.toCharArray(), 1, 2);
        length += 2;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(true);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(5);
        length += 1;
        assertEquals("Incorrect Length", length, buff.length());
        buff.append(100L);
        length += 3;
        assertEquals("Incorrect Length", length, buff.length());
        ReusableStringBuffer rsb = new ReusableStringBuffer("Fred");
        buff.append(rsb);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());
        StringBuffer sb = new StringBuffer("Fred");
        buff.append(sb);
        length += 4;
        assertEquals("Incorrect Length", length, buff.length());        
    }
    
    public void testSetLengthAndCapacity() {
        String data = "Test Case";

        ReusableStringBuffer buff = new ReusableStringBuffer(data);
        int length = data.length();
        assertEquals("Incorrect Capacity", length * 2, buff.capacity());
        
        buff.setLength(length * 4);        
        assertEquals("Incorrect Length", length*4, buff.length());
        assertEquals("Incorrect Capacity", length * 4, buff.capacity());
        
        buff.setLength( 2 );
        assertEquals("Incorrect Length", 2, buff.length());
        assertEquals("Incorrect Capacity", length * 4, buff.capacity());
        
        char[] chrs = buff.getChars();
        assertEquals("Incorrect array length", buff.capacity(), chrs.length);
    }

    public void testCharAt() {
        String data = "Test Case";

        ReusableStringBuffer buff = new ReusableStringBuffer(data);
        assertEquals("Incorrect Character", 't', buff.charAt(3));
        try {
            buff.charAt(20);
            fail("Out of bounds exception expected.");
        } catch (RuntimeException e) {
            // Expected
        }        
    }

    public void testSetCharAt() {
        String data = "Test Case";

        ReusableStringBuffer buff = new ReusableStringBuffer(data);
        buff.setCharAt(4, 'y');
        assertEquals("Incorrect String", "TestyCase", buff.toString());
        try {
            buff.setCharAt(20, 'X');
            fail("Out of bounds exception expected.");
        } catch (RuntimeException e) {
            // Expected
        }        
    }

    public void testGetChars() {
        ReusableStringBuffer buff = new ReusableStringBuffer(10);
        char[] chrs = buff.getChars();
        assertEquals("Incorrect character length", 10, chrs.length);
    }

    /*
     * Class to test for ReusableStringBuffer append(Object)
     */
    public void testAppendObject() {
        
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append( new Object() {
                    public String toString() { 
                        return "I am a test.";
                    }
                } );
        assertEquals("Incorrect Value", "I am a test.", buff.toString());
        
    }

    /*
     * Class to test for ReusableStringBuffer append(String)
     */
    public void testAppendString() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append( "I am a test." );
        assertEquals("Incorrect Value", "I am a test.", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer append(StringBuffer)
     */
    public void testAppendStringBuffer() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        StringBuffer sb = new StringBuffer( "I am a test." );
        buff.append(sb);
        assertEquals("Incorrect Value", "I am a test.", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer append(ReusableStringBuffer)
     */
    public void testAppendReusableStringBuffer() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        ReusableStringBuffer sb = new ReusableStringBuffer( "I am a test." );
        buff.append(sb);
        assertEquals("Incorrect Value", "I am a test.", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer append(char[])
     */
    public void testAppendcharArray() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append("I am a test.".toCharArray() );
        assertEquals("Incorrect Value", "I am a test.", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer append(char[], int, int)
     */
    public void testAppendcharArrayintint() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append("I am a test".toCharArray(), 2, 5 );
        assertEquals("Incorrect Value", "am a ", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer append(char)
     */
    public void testAppendchar() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append('I');
        assertEquals("Incorrect Value", "I", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer append(boolean)
     */
    public void testAppendboolean() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append(true);
        buff.append(' ');
        buff.append(false);
        assertEquals("Incorrect Value", "true false", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer append(int)
     */
    public void testAppendint() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append(5);
        assertEquals("Incorrect Value", "5", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer append(long)
     */
    public void testAppendlong() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append(5L);
        assertEquals("Incorrect Value", "5", buff.toString());
    }

    public void testDelete() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append( "I am a test." );
        buff.delete(2,7);
        assertEquals("Incorrect Value", "I test.", buff.toString());
        
    }

    public void testDeleteCharAt() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append( "I am a test." );
        buff.deleteCharAt(5);
        assertEquals("Incorrect Value", "I am  test.", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer replace(int, int, char[])
     */
    public void testReplaceintintcharArray() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append( "I am a test." );
        buff.replace(2,4,"wibble".toCharArray());
        assertEquals("Incorrect Value", "I wibble a test.", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer replace(int, int, ReusableStringBuffer)
     */
    public void testReplaceintintReusableStringBuffer() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append( "I am a test." );
        ReusableStringBuffer rsb = new ReusableStringBuffer("wibble");
        buff.replace(2,4,rsb);
        assertEquals("Incorrect Value", "I wibble a test.", buff.toString());
    }

    /*
     * Class to test for ReusableStringBuffer replace(int, int, String)
     */
    public void testReplaceintintString() {
        ReusableStringBuffer buff = new ReusableStringBuffer();
        buff.append( "I am a test." );
        buff.replace(2,4,"wibble");
        assertEquals("Incorrect Value", "I wibble a test.", buff.toString());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 ===========================================================================
*/
