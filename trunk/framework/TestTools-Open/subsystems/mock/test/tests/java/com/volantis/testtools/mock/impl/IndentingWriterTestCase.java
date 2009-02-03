/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import junit.framework.TestCase;

import java.io.StringWriter;

public class IndentingWriterTestCase extends TestCase {

    private StringWriter out;
    private IndentingWriter writer;

    protected void setUp() throws Exception {
        super.setUp();

        out = new StringWriter();
        writer = new IndentingWriter(out);
    }

    public void testSimpleBlock() throws Exception {
        writer.println("Some text");
        writer.println("Some more text");
        writer.beginBlock();
        writer.println("Some text\nsplit\nacross\nmultiple\nlines");
        writer.endBlock();
        writer.println("More text");

        assertEquals("Incorrect output",
                     "Some text\nSome more text\n    Some text\n    split"
                     + "\n    across\n    multiple\n    lines\nMore text\n",
                     out.getBuffer().toString());
    }

    public void testSeparateWritesOnOneLine() {
        writer.beginBlock();
        writer.print("A line ");
        writer.print("written in ");
        writer.print("a number of ");
        writer.print("separate writes");
        writer.endBlock();

        assertEquals("Incorrect output",
                     "    A line written in a number of separate writes\n",
                     out.getBuffer().toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 ===========================================================================
*/
