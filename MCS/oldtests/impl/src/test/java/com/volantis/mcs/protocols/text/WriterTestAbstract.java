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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/text/WriterTestAbstract.java,v 1.2 2002/11/28 11:56:32 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Geoff           VBM:2002103005 - Created when refactoring the
 *                              old SMSWriterTestCase into multiple classes.
 * 22-Nov-02    Geoff           VBM:2002103005 - Small refactoring of 
 *                              assertEqualsBoth and assertEqualsBuffer for 
 *                              clarity. 
 * 27-Nov-02    Geoff           VBM:2002103005 - Refactoring after we changed
 *                              the way the WhitespaceWriters deal with errors.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

import junit.framework.TestCase;

import java.io.CharArrayWriter;
import java.io.Writer;
import java.io.IOException;

/**
 * Abstract superclass for the WhitespaceWriter tests.
 * <p>
 * Named thusly to avoid the *TestCase.class filter that JUnit applies to
 * find classes which need to be tested.
 */ 
public abstract class WriterTestAbstract extends TestCase {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";
    
    private CharArrayWriter buffer;
    
    private Writer writer;

    public WriterTestAbstract(String s) {
        super(s);
    }

    //
    // Infrastructure, shared with subclasses.
    //
    
    public void setUp() {
        buffer = new CharArrayWriter();
        writer = createWriter();
    }

    Writer createWriter() {
        return buffer;
    }

    void assertEqualsBuffer(String expected) {
        char[] result = buffer.toCharArray();
        assertEquals(expected, new String(result));
    }

    void assertEqualsBoth(String input, int off, int len, String expected) 
            throws IOException {
        char[] c = input.toCharArray();
        writer.write(c, off, len);
        assertEqualsBuffer(expected);
        
        // cheat and do the string version as well
        // this should always succeed if the above succeeds
        // since the implementation is supposedly identical
        setUp();
        writer.write(input, off, len);
        assertEqualsBuffer(expected);
    }

    //
    // Actual test methods
    //
    // NOTE: since these tests are inherited by the whitespace versions,
    // make sure if you include whitespace in the data that it is "normal".
    //
    
    public void testChar() throws IOException {
        writer.write('a');
        writer.write('b');
        writer.write('c');
        assertEqualsBuffer("abc");
    }

    public void testString() throws IOException {
        String s = "blah";
        writer.write(s);
        writer.write(s);
        writer.write(s);
        assertEqualsBuffer(s + s + s);
    }

    public void testArray() throws IOException {
        String s = "test me";
        assertEqualsBoth(s, 0, s.length(), s);
    }
    
    public void testArrayEmpty() throws IOException {
        assertEqualsBoth("abc", 0, 0, "");
    }

    public void testArrayFirstChar() throws IOException {
        assertEqualsBoth("abc", 0, 1, "a");
    }

    public void testArraySecondChar() throws IOException {
        assertEqualsBoth("abc", 1, 1, "b");
    }

    public void testArrayLastChar() throws IOException {
        assertEqualsBoth("abc", 2, 1, "c");
    }

    public void testMixture() throws IOException {
        writer.write("a string;");
        writer.write(' ');
        writer.write("an array".toCharArray());
        writer.write('.');
        assertEqualsBuffer("a string; an array.");
    }
    
    // @todo add some failure cases as well
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
