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

import com.volantis.map.ics.imageprocessor.ImageInformation;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.logging.ImageLogger;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.RenderingHints;
import java.awt.image.BandedSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * Convert an image to 256 level greyscale.
 */
public class GreyscaleConvertor extends IndexedConvertor
    implements ImageConvertor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(GreyscaleConvertor.class);

    private static final double[] WEIGHTS = {0.114D, 0.587D, 0.299D};

    /**
     * Convert an image to 256 level greyscale.
     *
     * @param src the image to convert.
     * @return a 256 level greyscale version of the input image.
     */
    public RenderedOp convert(RenderedOp src, Parameters params)
        throws ImageConvertorException {

        RenderedOp out = src;

        // Check if the image is already greyscale.
        if ((src.getColorModel().getPixelSize() == 8) &&
            (src.getColorModel().getNumColorComponents() == 1) &&
            (src.getSampleModel().getNumBands() == 1)) {
            return src;
        }

        // Convert the image to RGB as greyscaling indexed images does not
        // work very well at all.
        if (out.getSampleModel().getNumBands() != 3) {
            ImageConvertor cvt =
                ImageConvertorFactory.getInstance().getImageConvertor(
                    ImageRule.TRUECOLOUR);
            out = cvt.convert(out, null);
        }

        ImageLayout layout = new ImageLayout();
        layout.setSampleModel(new BandedSampleModel(DataBuffer.TYPE_BYTE,
                                                    out.getWidth(),
                                                    out.getHeight(),
                                                    out.getColorModel().
                                                    getNumColorComponents()));
        // Set up the rendering hints object that is passed to the format
        // operation to effect this change.
        RenderingHints rh = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

        // Reformat the image to something acceptable to "errordiffusion".
        ParameterBlock formatPB = new ParameterBlock();
        formatPB.addSource(out);
        formatPB.add(DataBuffer.TYPE_BYTE);
        out = JAI.create("format", formatPB, rh);
        if (logger.isDebugEnabled()) {
            logger.debug("format performed " + ImageInformation.asString(out));
        }

        ParameterBlock pb = new ParameterBlock();
        pb.addSource(out);

        // Combine the bands into one by weighting the red, green and blue
        // channels.
        double[][] matrix = new double[1][out.getNumBands() + 1];

        System.arraycopy(WEIGHTS, 0, matrix[0], 0, WEIGHTS.length);

        pb.add(matrix);
        out = JAI.create("bandcombine", pb, null);

        if (logger.isDebugEnabled()) {
            logger.debug("Convert to 256 level greyscale");
            ImageLogger.log(out);
        }

        return out;
    }
}
