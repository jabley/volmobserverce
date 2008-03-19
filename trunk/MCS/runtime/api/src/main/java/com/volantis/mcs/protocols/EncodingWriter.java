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
 * $Header: /src/voyager/com/volantis/mcs/protocols/EncodingWriter.java,v 1.3 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Feb-02    Paul            VBM:2002022804 - Created to support new
 *                              PAPI getContentWriter method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 23-May-02    Paul            VBM:2002042202 - Changed to wrap a Writer
 *                              and encode using a CharacterEncoder instance.
 *                              It should now be much more efficient as it no
 *                              longer has to create new Strings.
 * 01-May-03    Steve           VBM:2003041606 - Derive from OuputBufferWriter
 *                              so that PAPI element capabilities can be passed
 *                              on.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.output.CharacterEncoder;

import java.io.IOException;
import java.io.Writer;

/**
 * This class extends Writer in order to provide writer specific encoding
 * of the content.
 */
public final class EncodingWriter
        extends OutputBufferWriter {

    /**
     * The CharacterEncoder object which determines how the characters should be
     * encoded.
     */
    private final CharacterEncoder encoder;

    /**
     * Create a new <code>EncodingWriter</code>.
     *
     * @param writer  The Writer to wrap.
     * @param encoder The CharacterEncoder object which determines how the
     *                characters should be encoded.
     */
    public EncodingWriter(Writer writer, CharacterEncoder encoder) {
        super(writer);
        this.encoder = encoder;
    }

    // Javadoc inherited from super class.
    public void write(char[] cbuf)
            throws IOException {

        encoder.encode(cbuf, getWriter());
    }

    // Javadoc inherited from super class.
    public void write(char[] cbuf, int off, int len)
            throws IOException {

        encoder.encode(cbuf, off, len, getWriter());
    }

    // Javadoc inherited from super class.
    public void write(int c)
            throws IOException {

        encoder.encode(c, getWriter());
    }

    // Javadoc inherited from super class.
    public void write(String text)
            throws IOException {

        encoder.encode(text, getWriter());
    }

    // Javadoc inherited from super class.
    public void write(String string, int off, int len)
            throws IOException {

        encoder.encode(string, off, len, getWriter());
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
