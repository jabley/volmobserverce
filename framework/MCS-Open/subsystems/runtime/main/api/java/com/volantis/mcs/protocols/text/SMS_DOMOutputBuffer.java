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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.text;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.text.LogicalWhitespaceWriter;
import com.volantis.mcs.protocols.text.NormaliseWhitespaceWriter;
import com.volantis.mcs.protocols.text.QuietLogicalWhitespaceWriter;

import java.io.Writer;

/**
 * An output buffer for SMS protocol. This deals with the fact that
 * SMS protocols differ from normal markup protocols in it needs to
 * add whitespace to simulate the effects of markup elements.
 *
 * Note: a SMS_DOMOutputBuffer operating in preformatted mode behaves just like
 * a DOMOutputBuffer; but I haven't taken advantage of this fact because I'm
 * not sure if the infrastructure *guarantees* that other tags are not called
 * inside a PRE, and also it appears that protocols must have only one type of
 * output buffers, considering the presence of VP.getOutputBufferFactory. Feel
 * free to change this if it turns out these are not a problem.
 */
public class SMS_DOMOutputBuffer extends DOMOutputBuffer {

    /**
     * If true, we are taking preformatted input and only the default writer
     * is valid; if false, we are formatting according to the writer used to
     * send input to us.
     */
    private boolean preformatted;

    /**
     * The writer returned by default; this is used by the elements to write
     * their body content, and it's impl will vary depending on whether we
     * are in preformatted mode or not.
     */
    private Writer writer;

    /**
     * The writer that we use to write logical whitespace to surround the
     * tag body content; only valid when not in preformatted mode.
     */
    private QuietLogicalWhitespaceWriter logicalWriter;

    /**
     * Default constructor for the factory to call.
     */
    public SMS_DOMOutputBuffer() {
        this.preformatted = true;
        super.setElementIsPreFormatted(true);
    }

    /**
     * Sets the input mode; must be called before {@link #getWriter} or
     * {@link #getLogicalWriter}.
     * <p>
     * Yes this is a hack, but the alterative is hard, see the class comment.
     *
     * @param preformatted True for preformatted, false otherwise.
     */
    public void setPreformatted(boolean preformatted) {
        if (writer != null) {
            throw new IllegalStateException(
                    "attempt to set preformatted after getting writer");
        }
        this.preformatted = preformatted;
        super.setElementIsPreFormatted(preformatted);
    }

    // Javadoc inherited from super class.
    public Writer getWriter() {
        ensureWritersAllocated();
        return writer;
    }

    /**
     * Returns the writer for writing logical whitespace with the data to
     * be written; only valid to be called when we are not operating in
     * preformatted mode.
     * <p>
     * This is typically used to write the whitespace which simulates markup
     * for SMS.
     *
     * @return the logical whitespace writer.
     */
    QuietLogicalWhitespaceWriter getLogicalWriter() {
        ensureWritersAllocated();
        if (logicalWriter == null) {
            throw new UnsupportedOperationException(
                    "cannot write (logical) markup in a PRE tag");
        }
        return logicalWriter;
    }

    /**
     * "Lazily" allocate the writer(s) if they are not already allocated.
     */
    private void ensureWritersAllocated() {
        if(writer == null){
            logicalWriter = new QuietLogicalWhitespaceWriter(
                new LogicalWhitespaceWriter(super.getWriter()));
            writer = new NormaliseWhitespaceWriter(logicalWriter);
        }
    }

}
