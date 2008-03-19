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
 * A container for holding attributes from a sequence of SAX startElement
 * events.
 *
 * <p>The container does not store information about where the attributes for
 * one event end and the next one starts, that information is stored
 * externally.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface AttributesContainer {

    /**
     * Create a window on this container that only shows a range of the
     * attributes stored within this container.
     *
     * <p>The window can be moved over and resized so that it exposes
     * different sets of attributes.</p.
     *
     * @return A window on thie container.
     */
    AttributesWindow createWindow();
}
