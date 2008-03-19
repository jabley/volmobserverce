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

import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class InternalEncodingCollection
        implements EncodingCollection {

    private final List encodings;

    private final Map extension2encoding;

    /**
     * Initialise.
     *
     * <p>This is protected to prevent it from being output in the public API
     * documentation. The exclude tags below do not appear to work for
     * constructors.</p>
     *
     * @param encodings A list of {@link Encoding}.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public InternalEncodingCollection(List encodings) {
        this.encodings = Collections.unmodifiableList(encodings);

        extension2encoding = new HashMap();
        for (Iterator i = encodings.iterator(); i.hasNext();) {
            Encoding encoding = (Encoding) i.next();
            Iterator e = encoding.extensions();
            while (e.hasNext()) {
                String extension = (String) e.next();
                extension2encoding.put(extension, encoding);
            }
        }
    }

    /**
     * Determines whether the collection contains the specified encoding.
     *
     * @param encoding The encoding for which the collection is searched.
     * @return True if the encoding is contained, false otherwise.
     */
    public boolean contains(Encoding encoding) {
        return encodings.contains(encoding);
    }

    /**
     * An immutable iterator over the encodings.
     *
     * <p>The order in which the encodings are returned cannot be relied upon
     * and may change without notice.</p>
     *
     * @return An {@link Iterator} over the {@link Encoding}s.
     */
    public Iterator iterator() {
        return encodings.iterator();
    }

    /**
     * Get the encoding for the specified file extension.
     *
     * @param extension The file extension (without the leading '.').
     * @return The encoding that commonly uses the file extension, or null if
     *         no such encoding could be found.
     */
    public Encoding getEncodingForExtension(String extension) {
        return (Encoding) extension2encoding.get(extension);
    }

    /**
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public int hashCode() {
        return encodings.hashCode();
    }

    /**
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof InternalEncodingCollection)) {
            return false;
        }

        InternalEncodingCollection other = (InternalEncodingCollection) obj;
        return encodings.equals(other.encodings);
    }
}
