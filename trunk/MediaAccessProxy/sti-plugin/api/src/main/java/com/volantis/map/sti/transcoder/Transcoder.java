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

package com.volantis.map.sti.transcoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.volantis.map.operation.ResourceDescriptor;

/**
 * Resource transcoder.
 * 
 * @mock.generate
 */
public interface Transcoder {
    /**
     * Transcodes resource described by specified ResourceDescriptor.
     *  
     * @param resourceDescriptor The resource descriptor.
     * @param request The HTTP servlet request which initiated the transcoding request.
     * @param response The HTTP servlet response to write transcoding result to.
     * @throws TranscoderException in case transcoding failed.
     */
    void transcode(ResourceDescriptor resourceDescriptor, 
            HttpServletRequest request,
            HttpServletResponse response) throws TranscoderException;
}
