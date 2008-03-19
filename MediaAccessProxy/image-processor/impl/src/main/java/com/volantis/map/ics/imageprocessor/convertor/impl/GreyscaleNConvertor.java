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

import com.volantis.map.ics.configuration.DitherMode;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.logging.ImageLogger;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.ColorCube;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.RenderedOp;

/**
 * Convert an image to an N level greyscale.
 */
public class GreyscaleNConvertor extends IndexedConvertor
    implements ImageConvertor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(GreyscaleNConvertor.class);

    /**
     * The number of bits needed to represent the greylevels in the output
     * image.
     */
    private final int bitDepth;

    /**
     * The number of output grey levels to produce.
     */
    private final int numLevels;

    /**
     * Constructor that allows you to set the number of grey levels to be
     * produced.
     *
     * @param bitDepth the number of bits needed to represent the number of
     *                 grey levels you wish to have in your output image. (e.g.
     *                 4 bits is 16 levels).
     */
    public GreyscaleNConvertor(int bitDepth) {

        if (bitDepth > 8) {
            throw new IllegalArgumentException(
                "bitDepths greater then 8 are not supported");
        }
        this.bitDepth = bitDepth;
        numLevels = 0x1 << bitDepth;
    }

    /**
     * This method returns an array of numLevel (mostly) evenly spaced values
     * in the range (0:255) inclusive. Note truncation errors mean things may
     * not be as evenly spaced as they could be.
     *
     * @param numLevels The number of grey levels you wish to use.
     * @return an array containing numLevels evenly spaced in the range (0:255)
     *         inclusive.
     */
    private byte[] createPalette(int numLevels) {
        byte[] palette = new byte[numLevels];
        int increment = 255 / (numLevels - 1);
        for (int i = 0; i < palette.length; i++) {
            palette[i] = (byte) ((i * increment) & 0xFF);
        }
        return palette;
    }

    /**
     * Convert an image to an N level greyscale using the dithering algorithm
     * specified in the parameters.
     *
     * @param src    - the image to convert.
     * @param params - the current parameters.
     * @return a dithered N level greyscale version of the input image.
     */
    public RenderedOp convert(RenderedOp src, Parameters params)
        throws ImageConvertorException {
        try {
            // Return input if it is monochrome.
            if (src.getColorModel().getPixelSize() == 1) {
                return src;
            }
            byte[] palette = createPalette(numLevels);

            DitherMode ditherMode2Bit = DitherMode.literal(
                (String)params.getParameterValue(ParameterNames.DITHER_MODE_2_BIT));

            // Convert the image to 256 level grey.
            ParameterBlock pb = new ParameterBlock();
            ImageConvertor cvt =
                ImageConvertorFactory.getInstance().
                getImageConvertor(ImageRule.GREY256);
            pb.addSource(cvt.convert(src, params));

            // Now dither the 256 greys down to 16.
            String opName = "errordiffusion";

            LookupTableJAI lut;

            if (ditherMode2Bit == DitherMode.FLOYD) {
                    lut = new LookupTableJAI(palette);
                    pb.add(lut);
                    pb.add(KernelJAI.ERROR_FILTER_FLOYD_STEINBERG);
            } else if (ditherMode2Bit == DitherMode.JARVIS) {
                    lut = new LookupTableJAI(palette);
                    pb.add(lut);
                    pb.add(KernelJAI.ERROR_FILTER_JARVIS);
            } else if (ditherMode2Bit == DitherMode.STUCKI) {
                    lut = new LookupTableJAI(palette);
                    pb.add(lut);
                    pb.add(KernelJAI.ERROR_FILTER_STUCKI);
            } else {
                    opName = "ordereddither";

                    ColorCube cube =
                        ColorCube.createColorCube(DataBuffer.TYPE_BYTE, 0,
                                                  new int[]{numLevels});
                    pb.add(cube);
                    pb.add(KernelJAI.DITHER_MASK_441);
            }

            // Create a layout containing an IndexColorModel which maps
            // to N grey levels.
            ImageLayout layout = new ImageLayout();
            ColorModel cm = new IndexColorModel(bitDepth, numLevels, palette,
                                                palette, palette);
            layout.setColorModel(cm);

            // Create a hint containing the layout.
            RenderingHints hints =
                new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

            // Dither the image.
            RenderedOp dst = JAI.create(opName, pb, hints);

            if (logger.isDebugEnabled()) {
                logger.debug("Convert to " + numLevels + " level greyscale");
                ImageLogger.log(dst);
            }

            return dst;
        } catch (Exception e) {
            throw new ImageConvertorException(e);
        }
    }
}
