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
 * Contains the information from the XML / SGML DOCTYPE declaration.
 *
 * <p>This is immutable as it is intended to be shared by multiple
 * documents.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface DocType {

    /**
     * Get the expected root element for the document.
     *
     * @return The root element.
     */
    String getRoot();

    /**
     * Get the public id.
     *
     * @return The public id.
     */
    String getPublicId();

    /**
     * Get the system id.
     *
     * @return The system id.
     */
    String getSystemId();

    /**
     * Get the internal DTD.
     *
     * @return The internal DTD.
     */
    String getInternalDTD();

    /**
     * Get the markup family.
     *
     * @return The markup family.
     */
    MarkupFamily getMarkupFamily();

    /**
     * Returns the String representation of the DocType as it would appear in
     * the markup.
     *
     * @return the String representation of the specified DocType. Never returns
     * null.
     */
    String getAsString();
}
