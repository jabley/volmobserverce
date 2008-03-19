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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.sax.recorder.impl.attributes;

/**
 * The constants used to define the internal structure of the
 * {@link AttributesContainer}.
 * 
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface AttributesConstants {

    /**
     * The number of slots used within the data array per attribute.
     */
    int SLOTS_PER_ATTRIBUTE = 5;
    /**
     * The offset of the URI within the set of slots for an attribute.
     */
    int OFFSET_URI = 0;
    /**
     * The offset of the local name within the set of slots for an attribute.
     */
    int OFFSET_LOCAL = 1;
    /**
     * The offset of the qualified name within the set of slots for an
     * attribute.
     */
    int OFFSET_QNAME = 2;
    /**
     * The offset of the type within the set of slots for an attribute.
     */
    int OFFSET_TYPE = 3;
    /**
     * The offset of the value within the set of slots for an attribute.
     */
    int OFFSET_VALUE = 4;
}
