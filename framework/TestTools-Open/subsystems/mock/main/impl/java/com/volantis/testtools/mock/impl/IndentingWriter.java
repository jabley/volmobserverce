/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Used for creating structured documents that are indented.
 *
 * <p>The difference between this and the PrintWriter is simply that it
 * supports indenting and always flushes.</p>
 */
public class IndentingWriter extends PrintWriter {
    protected String indent;
    protected boolean indentPending;
    private boolean newlinePending;
    private boolean insideWrite;
    private final String delta;

    public IndentingWriter(Writer out, String delta) {
        super(out);
        indent = "";
        indentPending = true;
        this.delta = delta;
    }

    public IndentingWriter(Writer out) {
        this(out, "    ");
    }

    public IndentingWriter(OutputStream out, String delta) {
        super(out);
        indent = "";
        indentPending = true;
        this.delta = delta;
    }

    public IndentingWriter(OutputStream out) {
        this(out, "    ");
    }

    public void write(char buf[], int off, int len) {
        write(new String(buf, off, len));
    }

    public void write(String s, int off, int len) {

        if (insideWrite) {
            super.write(s, off, len);
        } else {
            insideWrite = true;

            try {
                int start = off;
                int last = off + len;

                // Keep looping.
                while (true) {
                    int end;

                    // Break the string into lines.
                    int index = s.indexOf('\n', start);
                    if (index == -1 || index > last) {
                        end = off + len;
                        index = -1;
                    } else {
                        end = index;
                    }

                    if (end > start) {
                        writePendingIndent();
                        super.write(s, start, end - start);
                        newlinePending = true;
                    }

                    if (index != -1) {
                        println();

                        // Move onto the next part.
                        start = index + 1;
                    } else {
                        break;
                    }
                }

/*
                BreakIterator boundary = BreakIterator.getLineInstance();
                boundary.setText(new StringCharacterIterator(s, off, off + len,
                                                             off));
                int start = boundary.first();
                for (int end = boundary.next();
                     end != BreakIterator.DONE;
                     start = end, end = boundary.next()) {

                    writePendingIndent();
                    super.write(s, start, end - 1);
                    if (boundary.next() == BreakIterator.DONE) {
                        println();
                    } else if (end > start) {
                        newlinePending = true;
                    }
                }
*/
                flush();

            } finally {
                insideWrite = false;
            }
        }
    }

    public void println() {
        super.println();
        indentPending = true;
        newlinePending = false;
        flush();
    }

    /**
     * Write the indent if it is pending.
     */
    private void writePendingIndent() {
        if (indentPending) {
            indentPending = false;
            write(indent);
        }
    }

    /**
     * Start a new block indenting by some more.
     */
    public void beginBlock() {
        if (newlinePending) {
            println();
        }

        // todo make this more effificent.
        indent = indent + delta;
    }

    /**
     * End a block.
     */
    public void endBlock() {
        indent = indent.substring(delta.length());
        if (newlinePending) {
            println();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 ===========================================================================
*/
