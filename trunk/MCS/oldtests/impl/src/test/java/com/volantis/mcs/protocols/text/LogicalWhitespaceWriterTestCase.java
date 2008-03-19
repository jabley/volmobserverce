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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/text/LogicalWhitespaceWriterTestCase.java,v 1.3 2002/12/12 14:52:52 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Geoff           VBM:2002103005 - Created when refactoring the
 *                              old SMSWriterTestCase into multiple classes.
 * 27-Nov-02    Geoff           VBM:2002103005 - Refactoring after we changed
 *                              the way the WhitespaceWriters deal with errors,
 *                              and removed an unused test.
 * 12-Dec-02    Geoff           VBM:2002121023 - Modify tests to discover bugs
 *                              found.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

import java.io.Writer;
import java.io.IOException;

public class LogicalWhitespaceWriterTestCase extends WriterTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    private LogicalWhitespaceWriter logical;
    
    public LogicalWhitespaceWriterTestCase(String s) {
        super(s);
    }

    Writer createWriter() {
        logical = new LogicalWhitespaceWriter(super.createWriter());
        return logical;
    }

    public void testWhitespaceOnly() throws IOException {
        logical.writeSpace();
        logical.writeSpace();
        logical.writeLine();
        logical.writeLine();
        logical.writeSpace();
        logical.writeSpace();
        logical.writeLine();
        logical.writeSpace();
        assertEqualsBuffer("");
    }

    public void testSpacesInside() throws IOException {
        logical.write('a');
        logical.write('b');
        logical.writeSpace();
        logical.writeSpace();
        logical.write('c');
        logical.write("d");
        assertEqualsBuffer("ab cd");
    }

    public void testSpacesBefore() throws IOException {
        logical.writeSpace();
        logical.writeSpace();
        logical.write("a");
        logical.write("b");
        assertEqualsBuffer("ab");
    }

    public void testSpacesAfter() throws IOException {
        logical.write('a');
        logical.write('b');
        logical.writeSpace();
        logical.writeSpace();
        assertEqualsBuffer("ab");
    }
    
    public void testLinesInside() throws IOException {
        logical.write("a");
        logical.write("b");
        logical.writeLine();
        logical.writeLine();
        logical.writeLine();
        logical.write('c');
        logical.write('d');
        assertEqualsBuffer("ab\ncd");
    }

    public void testLinesBefore() throws IOException {
        logical.writeLine();
        logical.writeLine();
        logical.writeLine();
        logical.write("a");
        logical.write("b");
        assertEqualsBuffer("ab");
    }

    public void testLinesAfter() throws IOException {
        logical.write('a');
        logical.write('b');
        logical.writeLine();
        logical.writeLine();
        logical.writeLine();
        assertEqualsBuffer("ab");
    }
    
    public void testMultipleLinesAndSpaces() throws IOException {
        logical.writeSpace();
        logical.writeSpace();
        logical.write("a");
        logical.write("b");
        logical.writeSpace();
        logical.writeSpace();
        logical.writeLine();
        logical.writeLine();
        logical.writeSpace();
        logical.writeSpace();
        logical.writeLine();
        logical.writeLine();
        logical.writeSpace();
        logical.writeSpace();
        logical.writeLine();
        logical.writeLine();
        logical.write('c');
        logical.write('d');
        logical.writeSpace();
        logical.writeSpace();
        assertEqualsBuffer("ab\ncd");
    }

    public void testTrimmed() throws IOException {
        String s = "   \n  hello  there\t  ";
        assertEqualsTrimmed(s, 0, s.length(), "hello  there");
    }

    public void testTrimmedEmpty() throws IOException {
        String s = "   \n  \t  ";
        assertEqualsTrimmed(s, 0, s.length(), "");
    }
    
    void assertEqualsTrimmed(String input, int off, int len, String result) 
            throws IOException {
        char[] c = input.toCharArray();
        logical.writeTrimmed(c, off, len);
        assertEqualsBuffer(result);
    }
    
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
