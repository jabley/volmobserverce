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

package com.volantis.map.ics.imageprocessor.writer;

import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.operation.ObjectParameters;

import javax.media.jai.RenderedOp;

/**
 * Interface for writing images.
 */
public interface ImageWriter {

    /**
     * Translate single image or a number of images (in cases like parsing
     * animated GIFs) into output image's data.
     *
     * @param ops    - Image frames.
     * @param params - Parameters.
     * @param os     - stream to write image's data into.
     * @return ImageOutputStream - stream with written image's data.
     *
     * @throws ImageWriterException - of there is impossible to write image.
     */
    public NoFlushImageOutputStream process(RenderedOp[] ops, ObjectParameters params,
                                NoFlushImageOutputStream os)
        throws ImageWriterException;
}
