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

package com.volantis.shared.io;

import java.io.PrintWriter;
import java.io.Writer;
import java.io.OutputStream;

/**
 * Used for creating structured documents that are indented.
 *
 * <p>The difference between this and the PrintWriter is simply that it
 * supports indenting and always flushes.</p>
 *
 * <p>This was copied from mock framework because it is needed by other classes
 * but unfortunately cornerstone depends on mock so mock cannot depend on
 * cornerstone.</p>
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
