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

import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.streams.NoFlushSeekableOutputStream;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.utilities.ImageUtils;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.imageio.ImageWriteParam;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.FormatDescriptor;
import java.awt.*;
import java.awt.image.SampleModel;

public class BMPImageWriter extends DefaultWriter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(BMPImageWriter.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(BMPImageWriter.class);

    /**
     * Output an image in truecolor.
     *
     * @param ops    Images for writing.
     * @param params Parameters.
     * @param os     Output for image.
     * @return ImageOutputStream stream with image's data.
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
            ImageConvertor cvt =
                ImageConvertorFactory.getInstance().getImageConvertor(
                    ImageRule.TRUECOLOUR);
            RenderedOp converted = cvt.convert(ops[0], params);
            try {
                // We remove alpha channel since bmp image does not keep
                // transparency.
                converted = ImageUtils.applyAlphaChannel(converted, 0);

                // now we coerce the data into a format that the BMP writer
                // seems happy with.
                ImageLayout layout = new ImageLayout();
                SampleModel sampleModel =
                    RasterFactory.createComponentSampleModel(
                        converted.getSampleModel(),
                        converted.getSampleModel().getDataType(),
                        converted.getWidth(),
                        converted.getHeight(),
                        3);
                layout.setSampleModel(sampleModel);
                RenderingHints hints = new RenderingHints(
                    JAI.KEY_IMAGE_LAYOUT, layout);
                converted = FormatDescriptor.create(
                    converted,
                        converted.getSampleModel().getDataType(),
                    hints);


            } catch (Exception e) {
                LOGGER.error("image-encoding-error", e.toString());
                throw new ImageWriterException(e);
            }
            return super.outputImage(new RenderedOp[]{converted}, params, os);
        } catch (Exception e) {
            LOGGER.error("image-writing-failure", e.toString());
            throw new ImageWriterException(e);
        }
    }

    // javadoc inherited
    public String mimeType() {
        return "image/bmp";
    }

    // javadoc inherited
    protected ImageConvertor getImageConverter(RenderedOp image,
                                               Parameters params) {
        return ImageConvertorFactory.getInstance().getImageConvertor(
            ImageRule.TRUECOLOUR);
    }

    // javadoc inherited
    protected String getFileFormat() {
        return ImageConstants.BMP_EXTENSION;
    }

    // javadoc inherited
    protected String getImageDescription() {
        return "BMP colour image";
    }

    // javadoc inherited
    protected ImageWriteParam getImageWriteParam(RenderedOp image,
                                                 Parameters params) {
        return null;
    }
}
