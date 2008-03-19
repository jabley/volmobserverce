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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.sti.converters;

import org.w3c.dom.Document;

import com.volantis.map.sti.model.TranscodingResponse;

/**
 * Converts W3C DOM document with STI transcoding response to
 * TranscodingResponse.
 * 
 * @mock.generate
 */
public interface DOM2TranscodingResponseConverter {
    /**
     * Converts specified W3C DOM document with STI transcoding response to
     * transcoding response. Returns converted transcoding response.
     * 
     * @param document the document to convert
     * @return converted transcoding response
     * @throws ConverterException in case conversion fails
     */
    TranscodingResponse convert(Document document)
            throws ConverterException;
}
