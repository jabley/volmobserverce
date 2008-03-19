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

import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.imageio.ImageWriteParam;
import javax.media.jai.RenderedOp;

public class TIFFImageWriter extends DefaultWriter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(TIFFImageWriter.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(TIFFImageWriter.class);

    // javadoc inherited
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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Writing true colour image");
            }
            ImageConvertor cvt =
                ImageConvertorFactory.getInstance().getImageConvertor(
                    ImageRule.TRUECOLOUR);
            RenderedOp converted = cvt.convert(ops[0], null);
            return super.outputImage(new RenderedOp[]{converted}, params, os);
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
        return "image/tiff";
    }

    // javadoc inherited
    protected ImageConvertor getImageConverter(RenderedOp image,
                                               Parameters params) {
        return ImageConvertorFactory.getInstance().getImageConvertor(
            ImageRule.TRUECOLOUR);
    }

    // javadoc inherited
    protected String getFileFormat() {
        return ImageConstants.TIFF_EXTENSION;
    }

    // javadoc inherited
    protected String getImageDescription() {
        return "TIFF colour image";
    }

    // javadoc inherited
    protected ImageWriteParam getImageWriteParam(RenderedOp image,
                                                 Parameters params) {
        return null;
    }
}
