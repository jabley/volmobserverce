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

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.CharacterNode;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.mcs.utilities.WhitespaceUtilities;

public abstract class CharacterNodeImpl
        extends NodeImpl
        implements CharacterNode {

    /**
     * Constant for the default length of the Text buffer.
     * Performance testing for Orange indicated that 48 was the optimum
     * length for this constant. Please do not change this without
     * due consideration.
     */
    private static final int DEFAULT_LENGTH = 48;
    /**
     * An empty char array for when there are no contents in the buffer.
     */
    private static final char[] EMPTY = new char[0];
    /**
     * This flag indicates whether the contents of this Text node have already
     * been encoded.
     */
    protected boolean encoded;
    /**
     * The contents of this text node.
     */
    protected ReusableStringBuffer contents;

    public CharacterNodeImpl(DOMFactory factory) {
        super(factory);
    }

    /**
     * Method to lazilly get/create the contents ReusableStringBuffer.
     */
    private ReusableStringBuffer createContents() {
        if (contents == null) {
            contents = createContents(DEFAULT_LENGTH);
        }
        return contents;
    }

    /**
     * Method to lazilly get/create the contents ResuableStringBuffer.
     *
     * @param initialSize the initial size.
     */
    private ReusableStringBuffer createContents(int initialSize) {
        if (contents == null) {
            contents = new ReusableStringBuffer(initialSize);
        }
        return contents;
    }

    public void append(char chr) {
        createContents().append(chr);
    }

    public void append(String text) {
        if (text.length() > 0) {
            if (text.length() > DEFAULT_LENGTH) {
                createContents(text.length()).append(text);
            } else {
                createContents().append(text);
            }
        }
    }

    public void append(char[] text, int off, int len) {
        if (text.length > 0 && len > 0) {
            if (len > DEFAULT_LENGTH) {
                createContents(len).append(text, off, len);
            } else {
                createContents().append(text, off, len);
            }
        }
    }

    public char[] getContents() {
        char[] result = null;
        if (contents == null) {
            result = EMPTY;
        } else {
            result = contents.getChars();
        }
        return result;
    }

    public int getLength() {
        return contents == null ? 0 : contents.length();
    }

    public boolean isWhitespace() {
        return WhitespaceUtilities.isWhitespace(getContents(), 0, getLength());
    }// Javadoc inherited.
    public void clearContents() {
        // this defeats the purpose of a reusable string buffer but delete does
        // not currently work on the reusable string buffer. todo: fix this.
        contents = new ReusableStringBuffer();
        encoded = false;
    }
}
