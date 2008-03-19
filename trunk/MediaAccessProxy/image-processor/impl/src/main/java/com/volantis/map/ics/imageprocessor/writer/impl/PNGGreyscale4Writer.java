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

package com.volantis.map.ics.imageprocessor.writer.impl;

/**
 * This class is responsible for writing an image as a 4 level greyscale PNG.
 * <p>
 * If a maximum image size is set in the <code>DefaultConfiguration</code>
 * then the image may be written as a monochrome or scaled to fit
 * the maximum size set depending on the configuration settings.
 */

import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.OutputImageRules;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactory;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.media.jai.RenderedOp;

public class PNGGreyscale4Writer extends DefaultPNGWriter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(PNGGreyscale4Writer.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            PNGGreyscale4Writer.class);

    /**
     * Attempts to ouput an image in 4 level greyscale, however if a maximum
     * image size is set then the image is either reduced to monochrome or
     * scaled to fit the maximum image size. The option chosen depends on the
     * parameters settings.
     *
     * @param ops    Images for writing.
     * @param params Parameters.
     * @param os     Output for image.
     * @return javax.imageio.stream.ImageOutputStream
     *
     * @throws ImageWriterException if an error occurs while writing.
     */
    protected NoFlushImageOutputStream outputImage(RenderedOp[] ops,
                                                   ObjectParameters params,
                                                   NoFlushImageOutputStream os)
        throws ImageWriterException {
        if (ops == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "ops");
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
        if (os == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "os");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }

        try {
            if (params.containsName(ParameterNames.MAX_IMAGE_SIZE) &&
                params.containsName(ParameterNames.MINIMUM_BIT_DEPTH)) {

                int maxImageSize = params.getInteger(ParameterNames.MAX_IMAGE_SIZE);
                int minimumBitDepth = params.getInteger(ParameterNames.MINIMUM_BIT_DEPTH);

                RenderedOp[] convertedImage = getConvertedImage(ops, params);

                long sizeOfConvertedImage =
                    encodeImage(convertedImage, params, os);

                if ((sizeOfConvertedImage > maxImageSize) &&
                    minimumBitDepth < 2) {
                    os.reset();
                    os.mark();
                    ImageWriterFactory.getInstance().getWriter(OutputImageRules.GRAYSCALEPNG1, params).
                        process(ops, params, os);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Changing the ImageWriter to 'gp1'");
                    }
                } else {
                    super.outputImage(ops, params, os);
                }
            } else {
                super.outputImage(ops, params, os);
            }
        } catch (Exception e) {
            String msg = EXCEPTION_LOCALIZER.format("image-writing-failure",
                                                    e.toString());
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new ImageWriterException(e);
        }
        return os;
    }

    // Javadoc inherited
    protected ImageConvertor getImageConverter(RenderedOp theImage,
                                               Parameters params) {
        return ImageConvertorFactory.getInstance().getImageConvertor(
            ImageRule.GREY4);
    }

    // Javadoc inherited
    protected String getImageDescription() {
        return "greyscale 4 image";
    }
}
