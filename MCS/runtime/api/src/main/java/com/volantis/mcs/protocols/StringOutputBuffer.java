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
 * $Header: /src/voyager/com/volantis/mcs/protocols/StringOutputBuffer.java,v 1.9 2002/04/27 18:26:15 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Feb-02    Paul            VBM:2002022804 - Modified to make it useful.
 * 08-Mar-02    Paul            VBM:2002030607 - Implemented getWriter method
 *                              and added some more append methods.
 * 13-Mar-02    Paul            VBM:2002031301 - Added getCurrentBuffer which
 *                              simply returns the current buffer.
 * 13-Mar-02    Paul            VBM:2002030104 - Removed deprecatedAdd method
 *                              as it is no longer needed.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Moved some of the static
 *                              methods to a helper class.
 * 26-Mar-02    Allan           VBM:2002022007 - Modified to use a
 *                              ReusableStringBuffer for the buffer. Also
 *                              stopped the StringBuffer and StringOutputBuffer
 *                              append methods from destructively affecting
 *                              the parameter they are given. Added a new
 *                              append() for ReusableStringBuffer.
 * 04-Apr-02    Allan           VBM:2002042404 - ReusableStringWriter moved
 *                              to com.volantis.mcs.utilities.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.mcs.utilities.WhitespaceUtilities;

import java.io.Writer;

/**
 * This class implements an OutputBuffer consisting of a single
 * StringBuffer.
 */
public class StringOutputBuffer
        extends AbstractOutputBuffer {

    /**
     * The StringBuffer.
     */
    private final ReusableStringBuffer buffer;

    /**
     * The Writer.
     */
    private Writer writer;

    /**
     * This flag indicates whether the buffers contents are all whitespace.
     * An empty buffer does not contain any characters but is treated as being
     * all whitespace.
     *
     * This flag starts off as true and is set to false as soon as the first
     * non white space character is added. This is safe because nothing is ever
     * removed from this buffer.
     */
    private boolean whitespace;

    /**
     * This flag controls whether this buffer will trim off any whitespace
     * characters at the start and end of the final buffer.
     */
    private boolean trim;

    /**
     * Create a new <code>StringOutputBuffer</code>.
     */
    public StringOutputBuffer() {
        this(new ReusableStringBuffer(40));
    }

    /**
     * Create a new <code>StringOutputBuffer</code>.
     *
     * @param buffer the ReusableStringBuffer for this StringOutputBuffer
     */
    private StringOutputBuffer(ReusableStringBuffer buffer) {
        this.buffer = buffer;
        whitespace = true;
    }


    public void initialise() {
    }

    /**
     * Get the ReusableStringBuffer contents of this buffer.
     *
     * @return The ReusableStringBuffer contents.
     */
    public ReusableStringBuffer getBuffer() {
        return buffer;
    }

    // Javadoc inherited from super class.
    public Writer getWriter() {
        if (writer == null) {
            writer = new ReusableStringWriter(buffer);
        }

        return writer;
    }

    // Javadoc inherited from super class.
    public OutputBuffer getCurrentBuffer() {
        return this;
    }

    private StringOutputBuffer append(StringOutputBuffer s) {

        // If the buffer we are adding is empty then we don't have to do anything.
        if (s.isEmpty()) {
            return this;
        }

        append(s.buffer);

        return this;
    }

    public StringOutputBuffer append(StringBuffer s) {

        // If the buffer we are adding is empty then we don't have to do anything.
        if (s.length() == 0) {
            return this;
        }

        if (isEmpty() && trim) {
            // This buffer is currently empty and we are trimming.
            // Find the first non whitespace character in the buffer and append
            // the rest of the buffer.
            int index = WhitespaceUtilities.getFirstNonWhitespaceIndex(s);
            buffer.append(s.toString().substring(index));

            // If we are trimming then the whitespace flag is only true if the
            // buffer is empty.
            whitespace = isEmpty();
        } else {
            // This buffer is not empty and so we do not need to worry about trimming
            // but we still need to keep track of whitespace if we are still all
            // whitespace.
            if (whitespace) {
                whitespace = WhitespaceUtilities.isWhitespace(s);
            }

            buffer.append(s);
        }

        return this;
    }

    private StringOutputBuffer append(ReusableStringBuffer s) {

        // If the buffer we are adding is empty then we don't have to do anything.
        if (s.length() == 0) {
            return this;
        }

        if (isEmpty() && trim) {
            // This buffer is currently empty and we are trimming.
            // Find the first non whitespace character in the buffer and append
            // the rest of the buffer.
            int index = WhitespaceUtilities.getFirstNonWhitespaceIndex(s);
            buffer.append(s.getChars(), index, s.length() - index);

            // If we are trimming then the whitespace flag is only true if the
            // buffer is empty.
            whitespace = isEmpty();
        } else {
            // This buffer is not empty and so we do not need to worry about trimming
            // but we still need to keep track of whitespace if we are still all
            // whitespace.
            if (whitespace) {
                whitespace = WhitespaceUtilities.isWhitespace(s);
            }

            buffer.append(s);
        }

        return this;
    }

    private StringOutputBuffer append(String s) {
        return append(s, 0, s.length());
    }

    private StringOutputBuffer append(String s, int off, int len) {

        // If the String we are adding is empty then we don't have to do anything.
        if (s.length() == 0) {
            return this;
        }

        if (isEmpty() && trim) {
            // This buffer is currently empty and we are trimming.
            // Find the first non whitespace character in the string and append
            // the rest of the string.
            int index =
                    WhitespaceUtilities.getFirstNonWhitespaceIndex(s, off, len);
            buffer.append(s.substring(index, off + len));

            // If we are trimming then the whitespace flag is only true if the
            // buffer is empty.
            whitespace = isEmpty();
        } else {
            // This buffer is not empty and so we do not need to worry about trimming
            // but we still need to keep track of whitespace if we are still all
            // whitespace.
            if (whitespace) {
                whitespace = WhitespaceUtilities.isWhitespace(s, off, len);
            }

            if (off == 0 && len == s.length()) {
                buffer.append(s);
            } else {
                buffer.append(s.substring(off, off + len));
            }
        }

        return this;
    }

    public StringOutputBuffer append(char[] cbuf) {
        return append(cbuf, 0, cbuf.length);
    }

    private StringOutputBuffer append(char[] cbuf, int off, int len) {

        // If the String we are adding is empty then we don't have to do anything.
        if (cbuf.length == 0) {
            return this;
        }

        if (isEmpty() && trim) {
            // This buffer is currently empty and we are trimming.
            // Find the first non whitespace character in the string and append
            // the rest of the string.
            int index = WhitespaceUtilities.getFirstNonWhitespaceIndex(cbuf,
                    off, len);
            buffer.append(cbuf, index, len - (index - off));

            // If we are trimming then the whitespace flag is only true if the
            // buffer is empty.
            whitespace = isEmpty();
        } else {
            // This buffer is not empty and so we do not need to worry about trimming
            // but we still need to keep track of whitespace if we are still all
            // whitespace.
            if (whitespace) {
                whitespace = WhitespaceUtilities.isWhitespace(cbuf, off, len);
            }

            buffer.append(cbuf, off, len);
        }

        return this;
    }

    public StringOutputBuffer append(boolean b) {
        // This will always add non whitespace characters.
        whitespace = false;

        buffer.append(b);
        return this;
    }

    public StringOutputBuffer append(char c) {

        if (isEmpty() && trim) {
            // This buffer is currently empty and we are trimming.
            if (!Character.isWhitespace(c)) {
                buffer.append(c);
            }

            // If we are trimming then the whitespace flag is only true if the
            // buffer is empty.
            whitespace = isEmpty(); // this call looks obselete
        } else {
            // This buffer is not empty and so we do not need to worry about trimming
            // but we still need to keep track of whitespace if we are still all
            // whitespace.
            if (whitespace) {
                whitespace = Character.isWhitespace(c);
            }

            buffer.append(c);
        }

        return this;
    }

    public StringOutputBuffer append(int i) {
        // This will always add non whitespace characters.
        whitespace = false;

        buffer.append(i);
        return this;
    }

    public StringOutputBuffer append(Object o) {
        if (o instanceof ReusableStringBuffer) {
            return append((ReusableStringBuffer) o);
        } else {
            return append(o.toString());
        }
    }

    // Javadoc inherited from super class
    public void writeText(String text, boolean preEncoded) {
        if (preEncoded) {
            throw new UnsupportedOperationException();
        }
        append(text);
    }

    // Javadoc inherited from super class
    public void writeText(String text) {
        this.writeText(text, false);
    }

    // Javadoc inherited from super class
    public void writeText(char[] cbuf, int off, int len, boolean preEncoded) {
        if (preEncoded) {
            throw new UnsupportedOperationException();
        }
        append(cbuf, off, len);
    }

    // Javadoc inherited from super class
    public void writeText(char[] cbuf, int off, int len) {
        this.writeText(cbuf, off, len, false);
    }

    // Javadoc inherited from super class.
    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    // Javadoc inherited from super class.
    public boolean isEmpty() {
        return (buffer == null || buffer.length() == 0);
    }

    // Javadoc inherited from super class.
    public boolean isWhitespace() {
        return whitespace;
    }

    private void clear() {
        buffer.setLength(0);
    }

    // Javadoc inherited.
    public void transferContentsFrom(OutputBuffer buffer) {
        StringOutputBuffer other = (StringOutputBuffer) buffer;
        append(other);
        other.clear();
    }

    //javadoc inherited
    public void handleOpenElementWhitespace() {
    }

    //javadoc inherited
    public void handleCloseElementWhitespace() {
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10562/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 01-Dec-05	10517/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 07-Feb-05	6833/1	ianw	VBM:2005020205 IBM fixes interim checkin

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 13-Jul-04	4868/1	adrianj	VBM:2004070509 Implemented missing writeText(...) methods in StringOutputBuffer

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
