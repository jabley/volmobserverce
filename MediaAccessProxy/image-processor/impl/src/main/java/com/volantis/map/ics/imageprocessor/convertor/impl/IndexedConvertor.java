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

package com.volantis.map.ics.imageprocessor.convertor.impl;

import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;

import java.awt.Dimension;

/**
 * Abstract base class for indexed and greyscale images. These images can all
 * use the same scaling calculation as they all use one byte per pixel.
 */
public abstract class IndexedConvertor implements ImageConvertor {

    /**
     * Calculate the image size required to ensure that an indexed or greyscale
     * image fits into a given amount of memory.
     *
     * @param imageSize      the size of the original image.
     * @param compressedSize the size of the orignal image in bytes after
     *                       compression.
     * @param requiredSize   the size in bytes that the image needs to be
     *                       compressed to.
     * @return the image size that will fit into the given memory assuming the
     *         same compression ratio.
     */
    public Dimension calcScale(Dimension imageSize, long compressedSize,
                               long requiredSize) {
        Dimension newDim = new Dimension();

        long uncompressedSize = imageSize.width * imageSize.height;

        // Divide your whatsit by your thingy and kiss your arse goodbye.
        // In otherwords take a wild guess.
        if (uncompressedSize > (compressedSize * 2)) {
            requiredSize /= 3;
        }
        double percentage =
            (double) compressedSize / (double) uncompressedSize;

        double widthHeightRatio = (double) imageSize.width /
            (double) imageSize.height;

        double factor = widthHeightRatio * percentage;

        double height = Math.sqrt((double) requiredSize / factor);

        // Make sure we reduce the width by at least 1 to stop
        // the whole lot going into a loop.

        double widthDiff = imageSize.width - (height * widthHeightRatio);

        if ((widthDiff < 1) && (height > widthHeightRatio)) {
            height = height - widthHeightRatio;
        }
        newDim.setSize((int) (height * widthHeightRatio), (int) height);

        return newDim;
    }
}
