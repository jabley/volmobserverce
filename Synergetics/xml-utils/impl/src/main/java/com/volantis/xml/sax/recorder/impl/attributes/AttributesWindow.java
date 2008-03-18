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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.sax.recorder.impl.attributes;

import org.xml.sax.Attributes;

/**
 * Provides a view of a sub range of attributes stored within an
 * {@link AttributesContainer} object.
 *
 * <p>This is responsible for mapping client indexes that are relative to the
 * start of this window to and from the container indexes. It does this by
 * adding, and subtracting the offset respectively.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface AttributesWindow
        extends Attributes {

    /**
     * Move the window onto the specified range.
     *
     * @param offset The offset of the start of the window within the container.
     * @param length The length of the window.
     */
    void move(int offset, int length);
}
