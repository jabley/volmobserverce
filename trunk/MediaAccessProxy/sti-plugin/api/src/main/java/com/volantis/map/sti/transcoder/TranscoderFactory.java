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

/**
 * The factory of Transcoder instances.
 * 
 * @mock.generate
 */
public interface TranscoderFactory {
    /**
     * Creates and returns a new instance of Transcoder.
     * 
     * @return created instance of Transcoder.
     * @throws TranscoderFactoryException if creation failed.
     */
    Transcoder createTranscoder() throws TranscoderFactoryException;
}
