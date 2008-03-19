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

import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.media.jai.RenderedOp;

public class JPEGGreyscaleWriter extends JPEGColourWriter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(JPEGGreyscaleWriter.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            JPEGGreyscaleWriter.class);

    /**
     * Write a JPEG image to an output stream. If the parameters specifies a
     * maximum size, the image is degraded and scaled to make it fit the
     * specified memory.
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
            RenderedOp[] grey = new RenderedOp[1];
            grey[0] =
                ImageConvertorFactory.getInstance().getImageConvertor(
                    ImageRule.GREY256).
                convert(ops[0], params);
            return super.outputImage(grey, params, os);
        } catch (Exception e) {
            String msg = EXCEPTION_LOCALIZER.format("image-encoding-error",
                                                    e.toString());
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new ImageWriterException(e);
        }
    }

    // javadoc inherited
    public String mimeType() {
        return "image/jpeg";
    }
}
