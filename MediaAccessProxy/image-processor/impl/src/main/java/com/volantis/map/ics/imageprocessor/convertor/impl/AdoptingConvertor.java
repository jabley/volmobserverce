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
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.ics.imageprocessor.utilities.ImageUtils;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * Converts an image to a colour indexed image with pointed palette.
 */
public class AdoptingConvertor extends IndexedConvertor
    implements ImageConvertor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(AdoptingConvertor.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(AdoptingConvertor.class);

    /**
     * Set palette by given components. All the components arrays should have
     * the same length.
     *
     * @param r - R-omponents of color palette.
     * @param g - G-components of color palette.
     * @param b - B-components of color palette.
     */
    public synchronized void setPalette(byte r[], byte g[], byte b[]) {
        if (r == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "rColorPalette");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }
        if (g == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "gColorPalette");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }
        if (b == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "bColorPalette");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }
        if (r.length != g.length || g.length != b.length) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-invalid",
                                                    "r, g, b");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }

    }

    /**
     * Converts an image to a 256 colour gif, using given palette. Each pixel
     * is chosen from the given palette based on minimal Euclidean distance.
     *
     * @param image the image to convert. It must have component color model.
     * @return a 256 colour version of the image.
     */
    public synchronized RenderedOp convert(RenderedOp image, Parameters params)
        throws ImageConvertorException {
        if (image == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "image");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }
        if (params == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "params");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }

        return ImageUtils.ditherFloydSteinberg(image);

       
    }
}
