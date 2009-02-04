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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/text/NormaliseWhitespaceWriterTestCase.java,v 1.3 2002/12/12 14:52:52 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Geoff           VBM:2002103005 - Created when refactoring the
 *                              old SMSWriterTestCase into multiple classes.
 * 22-Nov-02    Geoff           VBM:2002103005 - Add tests for bug I found in
 *                              write([]) with content which starts or ends 
 *                              with whitespace.
 * 27-Nov-02    Geoff           VBM:2002103005 - Refactoring after we changed
 *                              the way the WhitespaceWriters deal with errors.
 * 12-Dec-02    Geoff           VBM:2002121023 - Modify tests to discover bugs
 *                              found.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

import java.io.Writer;
import java.io.IOException;

public class NormaliseWhitespaceWriterTestCase extends WriterTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";
    
    private NormaliseWhitespaceWriter normal;
    
    public NormaliseWhitespaceWriterTestCase(String s) {
        super(s);
    }

    Writer createWriter() {
        normal = new NormaliseWhitespaceWriter(new LogicalWhitespaceWriter(
                        super.createWriter()));
        return normal;
    }

    public void testEmpty() throws IOException {
        String s = " \n \t  ";
        assertEqualsBoth(s, 0, s.length(), "");
    }

    public void testParagraph() throws IOException {
        String s = " hello there   you\nbig\t \t \n wally ";
        assertEqualsBoth(s, 0, s.length(), "hello there you big wally");
    }

    public void testTwoWords() throws IOException {
        //               01234567890
        assertEqualsBoth("  a  b  c  ", 5, 4, "b c");
    }

    public void testTwoPartialWords() throws IOException {
        //               01234567890
        assertEqualsBoth("  a .b  c. ", 5, 4, "b c");
    }
    
    public void testMixture() throws IOException {
        normal.write(' ');
        normal.write("a");
        normal.write("     long");
        normal.write("         ");
        normal.write("string;  ");
        normal.write(' ');
        
        normal.write("an".toCharArray());
        normal.write("     ugly".toCharArray());
        normal.write("         ".toCharArray());
        normal.write("array.   ".toCharArray());
        normal.write(' ');
        assertEqualsBuffer("a long string; an ugly array.");
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
