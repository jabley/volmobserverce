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

package com.volantis.mcs.dom;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate base="Node"
 */
public interface CharacterNode
        extends Node {

    /**
     * Append a single character to the contents
     * @param chr the character to append
     */
    void append(char chr);

    /**
     * Append a string to the contents
     * @param text the string to append
     */
    void append(String text);

    /**
     * Append a character array subset to the contents
     * @param text a character array holding the characters to append
     * @param off the index of the first character to append
     * @param len the number of characters to append staring from off
     */
    void append(char[] text, int off, int len);

    /**
     * Returns the contents of this node as a character array
     * @return the contents of this node
     */
    char[] getContents();

    /**
     * Return the number of characters in the contents of this node
     * @return the number of characters in the contents of this node
     */
    int getLength();

    /**
     * Returns true if this node holds nothing but whitespace characters
     * @return true if all characters are whitespace, false otherwise
     */
    boolean isWhitespace();

    /**
     * Clears the contents of this text node
     */
    void clearContents();
}
