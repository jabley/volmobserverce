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
 * A collection of encodings.
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
 * @mock.generate
 * @since 3.5.1
 */
public interface EncodingCollection {

    /**
     * Determines whether the collection contains the specified encoding.
     *
     * @param encoding The encoding for which the collection is searched.
     * @return True if the encoding is contained, false otherwise.
     */
    public boolean contains(Encoding encoding);

    /**
     * An immutable iterator over the encodings.
     *
     * <p>The order in which the encodings are returned cannot be relied upon
     * and may change without notice.</p>
     *
     * @return An {@link Iterator} over the {@link Encoding}s.
     */
    public Iterator iterator();

    /**
     * Get the encoding for the specified file extension.
     *
     * @param extension The file extension (without the leading '.').
     * @return The encoding that commonly uses the file extension, or null if
     *         no such encoding could be found.
     */
    public Encoding getEncodingForExtension(String extension);
}
