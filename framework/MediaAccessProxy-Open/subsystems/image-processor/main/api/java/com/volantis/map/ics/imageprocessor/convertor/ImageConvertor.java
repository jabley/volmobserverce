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

package com.volantis.map.ics.imageprocessor.convertor;

import com.volantis.map.common.param.Parameters;

import java.awt.Dimension;

import javax.media.jai.RenderedOp;

public interface ImageConvertor extends ImageRule {

    /**
     * Calculate the image size required to ensure that an image fits into a
     * given amount of memory.
     *
     * @param imageSize      the size of the original image.
     * @param compressedSize the size of the orignal image in bytes after
     *                       compression.
     * @param requiredSize   the size in bytes that the image needs to be
     *                       compressed to.
     * @return the image size that will fit into the given memory assuming the
     *         same compression ratio.
     */
    Dimension calcScale(Dimension imageSize,
                        long compressedSize, long requiredSize);

    /**
     * Convert an image to another format using the dithering algorithm
     * specified in the parameters.
     *
     * @param src    the image to convert.
     * @param params the current parameters.
     * @return the converted image.
     */
    RenderedOp convert(RenderedOp src, Parameters params)
        throws ImageConvertorException;
}
