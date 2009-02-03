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

package com.volantis.map.ics.imageprocessor.writer.impl;

import com.sun.media.jai.operator.ImageWriteDescriptor;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.streams.NoFlushSeekableOutputStream;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.logging.ImageLogger;
import com.volantis.map.ics.imageprocessor.writer.ImageWriter;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class provides a template for all ImageWriter classes, ie, classes that
 * are responsible for writing a particular image type to an output stream.
 */
public abstract class DefaultWriter implements ImageWriter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultWriter.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(DefaultWriter.class);

    //javadoc inherited
    public NoFlushSeekableOutputStream process(RenderedOp[] ops,
                                            ObjectParameters params,
                                            NoFlushSeekableOutputStream os)
        throws ImageWriterException {
        try {

            params.setParameterValue(
                ParameterNames.OUTPUT_IMAGE_MIME_TYPE, mimeType());
            return outputImage(ops, params, os);
        } catch (Exception e) {
            LOGGER.error("image-writing-failure", e.toString());
            throw new ImageWriterException(e);
        }
    }

    /**
     * Output an image to a output stream. This method writes the first frame
     * only. Inheritors have to override this method to handle multiframe
     * images.
     *
     * @param ops    Images for writing.
     * @param params Parameters.
     * @param os     Output stream for writing image data into.
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

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Writing " + getImageDescription());
            }

            RenderedOp[] convertedImage = getConvertedImage(ops, params);
            os.mark();
            outputFrame(os, convertedImage[0], getFileFormat(), params);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Image offset: " + os.getFlushedPosition() +
                        " size: "+ os.getSize() + " bytes.");
            }

        } catch (Exception e) {
            LOGGER.error("image-writing-failure", e.toString());
            throw new ImageWriterException(e);
        }
        return os;
    }

    /**
     * Writes the supplied image to the supplied output stream in the supplied
     * format.
     *
     * @param os         output stream to be written to, must not be null.
     * @param image      the image to write, must not be null.
     * @param fileFormat the format that the image is to be written in, eg,
     *                   GIF, PNG, BMP etc, must not be null.
     * @param params
     * @throws ImageWriterException if an error occurs while writing.
     */
    protected void outputFrame(ImageOutputStream os,
                               RenderedOp image,
                               String fileFormat, Parameters params)
        throws ImageWriterException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Writing the image to ouputstream...");
            ImageLogger.log(image);
        }

        try {
            ParameterBlockJAI p = new ParameterBlockJAI("ImageWrite");
            p.addSource(image);
            p.setParameter("Output", os);
            p.setParameter("Format", fileFormat);
            p.setParameter("WriteParam", getImageWriteParam(image, params));
            JAI.create("ImageWrite", p);

        } catch (Exception e) {
            LOGGER.error("jai-operation-failure", "ImageWrite");
            throw new ImageWriterException(e);
        }
    }

    /**
     * Returns a string representation of image MIMEType.
     *
     * @return mimeType of output image.
     */
    protected abstract String mimeType();

    /**
     * Returns an ImageConverter that is able to convert <code>image</code> to
     * the desired format.
     *
     * @param image  the Image to be converted.  Some implementations of this
     *               method may need to examine properties of
     *               <code>image</code> when deciding which <code>ImageConverter</code>
     *               to return.
     * @param params - parameters to pass to convertor.
     * @return ImageConverter the image converter to be used.
     */
    protected abstract ImageConvertor getImageConverter(RenderedOp image,
                                                        Parameters params);

    /**
     * Returns a string representation of the file format that the ImageWriter
     * class writes. Eg GIF, JPEG, WMP..
     *
     * @return the file format that this class is responsible for writing.
     */
    protected abstract String getFileFormat();

    /**
     * Returns a description of the type of image being operated on.  This
     * information will be used for debugging only and is therefore not
     * localized.
     *
     * @return a description of the type of image being processed, eg,
     *         'monochrome image'.
     */
    protected abstract String getImageDescription();

    /**
     * Return an ImageWriteParam encapsulating any parameters that are required
     * as input to the ImageEncoder that will eventually write the image to the
     * response.
     *
     * @param image image to be encoded.
     * @param params
     * @return ImageWriteParam parameters that are required for the
     *         conversion.
     */
    protected abstract ImageWriteParam getImageWriteParam(RenderedOp image,
                                                          Parameters params);

    /**
     * Returns the size of the supplied image in bytes. This method handles the
     * first frame only. Inheritors have to override this method to handle
     * multiframe images.
     *
     * @param image  the image for which the size is required, must not be
     *               null.
     * @param params - parameters.
     * @param os     the outputStream into which the image should be encoded.
     * @return the size of the image in bytes.
     *
     * @throws ImageWriterException
     */
    protected long encodeImage(RenderedOp[] image, Parameters params,
                               NoFlushSeekableOutputStream os)
        throws ImageWriterException, IOException {
        String fileFormat = getFileFormat();
        outputFrame(os, image[0], fileFormat, params);
        return os.getSize();
    }

    /**
     * Converts the supplied image to the desired format, which is dependent on
     * {@link #getImageConverter} implemented by sub classes.
     *
     * @param image  the image to be converted, must not be null.
     * @param params the ICS parameters, must not be null.
     * @return the supplied image after it has been converted into the desired
     *         format.
     */
    protected RenderedOp[] getConvertedImage(RenderedOp[] image,
                                             Parameters params)
        throws ImageWriterException {
        try {
            ImageConvertor converter;
            RenderedOp[] convertedImage = new RenderedOp[image.length];
            for (int frameCount = 0; frameCount < image.length; frameCount++) {
                converter = getImageConverter(image[frameCount], params);
                RenderedOp tmp = converter.convert(image[frameCount], params);
                convertedImage[frameCount] = conditionImage(tmp, params);             
            }
            return convertedImage;
        } catch (Exception e) {
            throw new ImageWriterException(e);
        }
    }

    /**
     * Override this method to apply conditioning to the image to make it comply
     * with expectations the encoder has. At this stage the image may be
     * a Indexed image (if greyscale it will have 3 colour bands that all
     * contain the same data). If another format it should already have the
     * correct number of bands. The image should alreaddy have the correct
     * number of bits per pixel.
     * <p>
     * The default behaviour of this method is to do nothing.
     * </p>
     *
     * @param image the image to condition
     * @param params processing parameters
     * @return an image conditioned appropriate to the codec.
     */
    protected RenderedOp conditionImage(RenderedOp image, Parameters params) {
        return image;
    }
}
