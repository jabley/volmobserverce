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

package com.volantis.mcs.xdime;

import java.io.IOException;

/**
 * A very simple writer that is sufficient for the purposes of writing
 * content within XDIME.
 *
 * <p>It should be faster than a standard I/O Writer as it does not
 * synchronize, or create any internal buffers.</p>
 */
public abstract class FastWriter {

    /**
     * Write the characters.
     *
     * @param characters The array of characters.
     * @param offset     The offset within the array of the first character to
     *                   write.
     * @param length     The length of the range to write.
     * @throws IOException If there was a problem writing the characters.
     */
    public abstract void write(char[] characters, int offset, int length)
            throws IOException;
}
