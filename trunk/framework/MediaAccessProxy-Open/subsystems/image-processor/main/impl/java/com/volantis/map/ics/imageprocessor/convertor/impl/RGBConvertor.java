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
import com.volantis.map.ics.imageprocessor.logging.ImageLogger;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.RenderedOp;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;

/**
 * Convert images to TrueColour.
 */
public class RGBConvertor implements ImageConvertor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(RGBConvertor.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(RGBConvertor.class);

    /**
     * Convert an image to 24 bit colour.
     *
     * @param src the image to convert.
     * @return a true colour version of the input image.
     */
    public RenderedOp convert(RenderedOp src, Parameters params)
        throws ImageConvertorException {

        RenderedOp dst = src;
        ColorModel cm = src.getColorModel();

        if (cm instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) cm;
            byte[][] data = null;
            if (icm.hasAlpha()) {
                data = new byte[4][icm.getMapSize()];
                icm.getReds(data[0]);
                icm.getGreens(data[1]);
                icm.getBlues(data[2]);
                icm.getAlphas(data[3]);
            } else {
                data = new byte[3][icm.getMapSize()];
                icm.getReds(data[0]);
                icm.getGreens(data[1]);
                icm.getBlues(data[2]);
            }

            LookupTableJAI lut = new LookupTableJAI(data);

            try {
                dst = JAI.create("lookup", src, lut);
            } catch (Exception e) {
                LOGGER.error("jai-operation-failure", "lookup");
                throw new ImageConvertorException(e);
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Converted from indexed to RGB");
                ImageLogger.log(dst);
            }
        } else
        if (cm instanceof ComponentColorModel &&
            cm.getNumColorComponents() == 1) {
            //Transform image with one colour component into image with three
            //components.
            int[] bandIndices;

            //If we have no alpha data we should extend only colour components.
            if (cm.getNumColorComponents() == cm.getNumComponents()) {
                bandIndices = new int[]{
                    0, //Put the colour data into R(first) component
                    0, //Put the colour data into G(second) component
                    0};//Put the colour data into B(third) component
            } else {
                //In other case we should keep alpha data.
                bandIndices = new int[]{
                    0, //Put the colour data into R(first) component
                    0, //Put the colour data into G(second) component
                    0, //Put the colour data into B(third) component
                    1};//Put the alpha data into A(fourth) component
            }
            //Construct the ParameterBlock.
            ParameterBlock parameters = new ParameterBlock();
            parameters.addSource(src);
            parameters.add(bandIndices);

            try {
                dst = JAI.create("bandSelect", parameters);
            } catch (Exception e) {
                LOGGER.error("jai-operation-failure", "bandSelect");
                throw new ImageConvertorException(e);
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Number of colour components extended");
                ImageLogger.log(dst);
            }
        }
        return dst;
    }

    /**
     * Calculate the image size required to ensure that a true colour image
     * fits into a given amount of memory.
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

        // OK simply reduce the image by 25% on each axis. (halve the size of
        // the image). Floor is used, rather then round, to ensure that an
        // image of width = 1 will get scaled to size 0 rather then 0.75 which
        // would get rounded back to 1 (causing a loop)
        int newWidth = (int) Math.floor(imageSize.width * 0.75);
        int newHeight = (int) Math.floor(imageSize.height * 0.75);

        if (newWidth < 1 || newHeight < 1) {
            // can't resize more
            return imageSize;
        }

        newDim.setSize(newWidth, newHeight);
        return newDim;
    }
}
