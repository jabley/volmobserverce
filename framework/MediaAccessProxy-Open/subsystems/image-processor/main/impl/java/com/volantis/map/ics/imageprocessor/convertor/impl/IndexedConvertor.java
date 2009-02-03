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

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.ics.imageprocessor.ImageInformation;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.ics.imageprocessor.utilities.ImageUtils;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.*;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;

/**
 * Abstract base class for indexed and greyscale images. These images can all
 * use the same scaling calculation as they all use one byte per pixel.
 */
public abstract class IndexedConvertor
    extends AbstractImageConvertor implements ImageConvertor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(IndexedConvertor.class);

    /**
     * return the number of bits per pixel to use for the indexed lookup. The
     * returned image will have a colourmap that has a size of 2^getRequiredNumBits()
     * or less.
     *
     * @return an the number of bits per pixel to use. This value is used to
     * performed lookups in the the colourmap.
     */
    public abstract int getRequiredNumBits();

    /**
     * Return true if the resulting image should be colour; false if it should
     * be greyscale.
     *
     * @return true is the resulting image should be colour; false if it should
     * be greyscale.
     */
    public abstract boolean isColor();


     /**
     * Increase the size of the pallete to match that requested. This just
     * copies the original pallete into the first elements of the returned
     * image. This method can also greyscale  colour inedxed images.
     *
     * @param src the source image
     * @param maxNumEntries the number of entries there should be in the
     * resulting IndexedColorModels colourmap.
     * @return an IndexColorModel image that is colour or greyscale. Does
     * nothing if the src image has the correct number of entries in its index
     * and the image does not need to be converted to greyscale.
     */
    private RenderedOp increasePaletteSize(final RenderedOp src, final int maxNumEntries) {
        final int bitsPerPixel = getRequiredNumBits();

        final IndexColorModel icm = (IndexColorModel) src.getColorModel();
        RenderedOp dst = src;

        // skip the conversion if we do not need to make the colormap larger
        if (!isColor() || icm.getMapSize() != maxNumEntries) {
            // copy the colormap note that these arrays must be large enough to
            // hold the current size colourmap even if not all the entries in
            // the colormap are used
            byte[] reds = new byte[maxNumEntries];
            byte[] greens = new byte[maxNumEntries];
            byte[] blues = new byte[maxNumEntries];
            byte[] alphas = null;

            if (icm.getTransparency() == Transparency.BITMASK) {
                alphas = new byte[maxNumEntries];
                icm.getAlphas(alphas);
            }

            icm.getReds(reds);
            icm.getGreens(greens);
            icm.getBlues(blues);

            // convert it to greyscale if necessary (just a weighted sum of the
            // rgb values). This is a greyscale image but only because r == g == b
            if (!isColor()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Converting colourmap to greyscale");
                }

                double[] weights = getGreyscaleWeights();
                for (int i=0;i<reds.length; i++) {
                    // yes really truncate
                    int mapValue = (int)((reds[i] & 0xFF) * weights[0]
                        + (greens[i] & 0xFF) * weights[1]
                        + (blues[i] & 0xFF) * weights[2]);

                    reds[i] = blues[i] = greens[i] = (byte) (mapValue & 0xFF);
                }
            }

            IndexColorModel dstColorModel;

            // Note that he reds, greens, blues and alphas arrays can be larger
            // then the maxNumEntries. The IndexColorModel constructors handle
            // this by only copying the specified number of entries from the array
            if (icm.getTransparency() == Transparency.OPAQUE ||
                icm.getTransparency() == Transparency.TRANSLUCENT) {
                // it is safe to pass -1 as the transparent pixel index
                // which indicates that there no transparent pixel
                dstColorModel = new IndexColorModel(
                    bitsPerPixel, maxNumEntries, reds, greens, blues, icm.getTransparentPixel());
            } else {
                // bitmask
                dstColorModel = new IndexColorModel(
                    bitsPerPixel, maxNumEntries, reds, greens, blues, alphas);
            }

            ImageLayout il = new ImageLayout();
            il.setColorModel(dstColorModel);
            RenderingHints rh = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il);

            // reformat this image to use the new ColorModel (and its new color map)
            ParameterBlock formatPB = new ParameterBlock();
            formatPB.addSource(src);
            formatPB.add(DataBuffer.TYPE_BYTE);
            dst = JAI.create("format", formatPB, rh);
        }
        return dst;
    }

    /**
     * Convert an image to an indexed image with the correct number of bits per
     * pixel and either colour or greyscale.
     *
     * @param src the image to convert.
     * @return an indexed version of the source image
     */
    public RenderedOp convert(RenderedOp src, Parameters params)
        throws ImageConvertorException {

        if (logger.isDebugEnabled()) {
            logger.debug("Converting image to an indexed image:" +
                ImageInformation.asString(src));
        }

        int expectedMapSize = 0x1 << getRequiredNumBits();
        RenderedOp dstImage = null;
        if (src.getColorModel() instanceof IndexColorModel &&
            ((IndexColorModel)src.getColorModel()).getMapSize() <= expectedMapSize) {
            // this is a potentially optimized path for images that are
            // already indexed.
            return increasePaletteSize(src, expectedMapSize);
        } else  {
            // This is the generic and SLOW path used for non indexed images
            // and images that are indexed but that need to have thier colormap
            // size reduced.


            // convert to indexed (expensive operation)

            try {
                dstImage = ImageUtils.dither(src, params, expectedMapSize);
            } catch (MissingParameterException e) {
                throw new ImageConvertorException(e);
            }
            if (!isColor()) {
                // then push through the optimized route for grey scale and palette
                // size increase (won't do anything but convert to greyscale in this
                // case)
                dstImage = increasePaletteSize(dstImage, expectedMapSize);
            }
        }
        return dstImage;
    }

}
