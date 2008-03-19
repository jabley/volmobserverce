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

package com.volantis.mcs.policies.variants.metadata;

import java.util.Iterator;

/**
 * Base for all encodings.
 *
 * <p>An encodings is an abstract representation of the encoding of a
 * resource. It may relate to a number of different mime types, and or common
 * file extensions.</p> 
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public interface Encoding {

    /**
     * Get the name of the encoding.
     *
     * <p>The returned value is only supplied for debug purposes and cannot be
     * relied upon.</p>
     *
     * @return The name of the encoding.
     */
    String getName();

    /**
     * Return an immutable iterator over the String representations of the
     * mime types related to this encoding.
     *
     * @return An immutable iterator.
     */
    public Iterator mimeTypes();

    /**
     * Return an immutable iterator over the String representation of the file
     * extensions typically related to this encoding.
     *
     * <p>The extensions do not start with a <code>'.'</code>.</p>
     *
     * @return An immutable iterator.
     */
    public Iterator extensions();
}
