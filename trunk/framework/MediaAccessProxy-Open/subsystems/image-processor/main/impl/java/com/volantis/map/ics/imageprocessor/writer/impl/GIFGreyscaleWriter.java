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

import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.OutputImageRules;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.common.streams.NoFlushSeekableOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactory;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.map.common.param.Parameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.media.jai.RenderedOp;

public class GIFGreyscaleWriter extends DefaultGIFWriter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(GIFGreyscaleWriter.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(GIFGreyscaleWriter.class);

    /**
     * Attempts to output an image in 256 level grayscale, however if a maximum
     * image size is set then the image is either reduced to 16 level greyscale
     * or scaled to fit the maximum image size.  The option chosen depends on
     * the parameters settings.
     *
     * @param ops    Images for writing.
     * @param params Parameters.
     * @param os     Output for image.
     * @return javax.imageio.stream.ImageOutputStream
     *
     * @throws ImageWriterException if an error occurs while writing.
     */
    protected NoFlushSeekableOutputStream outputImage(RenderedOp[] ops,
                                                   ObjectParameters params,
                                                   NoFlushSeekableOutputStream os)
        throws ImageWriterException {
        if (ops == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "ops");
            throw new IllegalArgumentException(msg);
        }
        if (params == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "params");
            throw new IllegalArgumentException(msg);
        }
        if (os == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "os");
            throw new IllegalArgumentException(msg);
        }

        try {
            if (params.containsName(ParameterNames.MAX_IMAGE_SIZE) &&
                params.containsName(ParameterNames.MINIMUM_BIT_DEPTH)) {

                int minimumBitDepth = params.getInteger(ParameterNames.MINIMUM_BIT_DEPTH);
                int maxImageSize = params.getInteger(ParameterNames.MAX_IMAGE_SIZE);

                long sizeOfConvertedImage = encodeImage(ops, params, os);

                if ((sizeOfConvertedImage > maxImageSize) &&
                    minimumBitDepth < 8) {
                    os.reset();
                    os.mark();
                    os = ImageWriterFactory.getInstance().
                        getWriter(OutputImageRules.GREYSCALEGIF4, params).process(ops, params, os);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Changing the ImageWriter to 'gg4'");
                    }
                } else {
                    super.outputImage(ops, params, os);
                }
            } else {
                super.outputImage(ops, params, os);
            }
        } catch (Exception e) {
            LOGGER.error("image-writing-failure", e.toString());
            throw new ImageWriterException(e);
        }
        return os;
    }

    // Javadoc inherited
    protected ImageConvertor getImageConverter(RenderedOp theImage,
                                               Parameters params) {

        return ImageConvertorFactory.getInstance().getImageConvertor(
            ImageRule.INDEXEDGREY256);
    }

    // Javadoc inherited
    protected String getImageDescription() {
        return "greyscale image";
    }
}
