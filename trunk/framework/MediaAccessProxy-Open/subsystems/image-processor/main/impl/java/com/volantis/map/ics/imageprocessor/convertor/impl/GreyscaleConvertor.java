/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Â  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.Â  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.convertor.impl;

import com.volantis.map.common.param.Parameters;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;

import javax.media.jai.RenderedOp;
import javax.media.jai.operator.BandCombineDescriptor;

/**
 * Convert an image to greyscale.
 */
public class GreyscaleConvertor extends AbstractImageConvertor
    implements ImageConvertor {

    /**
     * Convert an image to greyscale if it is not already greyscale
     *
     * @param src the image to convert.
     * @return a greyscale version of the input image.
     */
    public RenderedOp convert(RenderedOp src, Parameters params)
        throws ImageConvertorException {

        RenderedOp grey = src;
        if (src.getColorModel().getNumColorComponents() > 2) {
            double[] weights = getGreyscaleWeights();
            // Combine the bands into one by weighting the red, green and blue
            // channels.
            double[][] matrix = new double[1][src.getColorModel().getNumComponents() + 1];

            System.arraycopy(weights, 0, matrix[0], 0, weights.length);

            grey = BandCombineDescriptor.create(src, matrix, null);
        }
        return grey;
    }
}
