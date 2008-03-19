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
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;

import javax.media.jai.RenderedOp;

/**
 * Converts an image to a 256 colour indexed image.
 */
public class Indexed256Convertor extends IndexedConvertor
    implements ImageConvertor {

    /**
     * The logging object to use in this class for localised logging services.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(Indexed256Convertor.class);

    /**
     * Converts an image to a 256 colour gif, using Floyd Steinberg dithering
     * if necessary. Any alpha channel is removed.
     *
     * @param src - the image to convert.
     * @return a 256 colour version of the image.
     */
    public RenderedOp convert(RenderedOp src, Parameters params)
        throws ImageConvertorException {
        RenderedOp dst;

        if (logger.isDebugEnabled()) {
            logger.debug("Converting image using Indexed256Convertor.");
        }

        final ColorModel srcCM = src.getColorModel();

        if (srcCM.hasAlpha()) {
            // If the image has an alpha channel then remove the alpha channel
            // and use up to 256 colours if the image has an indexed colour
            // model.
            dst = ImageUtils.applyAlphaChannel(src, 256);
        } else {
            dst = src;
        }

        // Dithering converts an image to an indexed colour image, so only do
        // this if the colour model is not already indexed.
        if (!(srcCM instanceof IndexColorModel)) {
            dst = ImageUtils.ditherFloydSteinberg(dst);
        }

        return dst;
    }
}
