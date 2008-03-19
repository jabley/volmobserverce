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

package com.volantis.map.sti.mime;

import com.volantis.map.operation.ResourceDescriptor;

/**
 * Retrieves MIME type of the resource to be transcoded from specified
 * ResourceDescriptor.
 * 
 * @mock.generate
 */
public interface MimeTypeRetriever {
    /**
     * Retrieves and returns MIME type of the resource to be transcoded from
     * specified ResourceDescriptor.
     * 
     * @param descriptor The resource descriptor.
     * @return The MIME type of the resource.
     * @throws MimeTypeRetrieverException in case MIME type can not be retrieved.
     */
    public String retrieveMIMEType(ResourceDescriptor descriptor)
            throws MimeTypeRetrieverException;
}
