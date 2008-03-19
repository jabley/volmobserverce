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

package com.volantis.map.sti.converters;

import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.sti.model.TranscodingRequest;

/**
 * Converts ResourceDescriptor to TranscodingRequest.
 * 
 * @mock.generate
 */
public interface ResourceDescriptor2TranscodingRequestConverter {
    /**
     * Converts specified resource descriptor to transcoding request. Returns
     * converted transcoding request.
     * 
     * @param resourceDescriptor the resource descriptor to convert.
     * @return converted transcoding request
     * @throws ConverterException in case conversion failed.
     */
    public TranscodingRequest convert(ResourceDescriptor resourceDescriptor)
            throws ConverterException;
}
