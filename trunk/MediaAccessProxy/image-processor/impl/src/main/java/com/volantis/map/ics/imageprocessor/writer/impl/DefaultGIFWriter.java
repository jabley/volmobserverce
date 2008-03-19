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

import com.sun.imageio.plugins.gif.GIFImageMetadata;
import com.sun.imageio.plugins.gif.GIFStreamMetadata;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.RenderedOp;

/**
 * Output methods common to all GIF output classes.
 */
public abstract class DefaultGIFWriter extends DefaultWriter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultGIFWriter.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(DefaultGIFWriter.class);

    protected RenderedOp[] getConvertedImage(RenderedOp[] image,
                                             Parameters params)
        throws ImageWriterException {
        return super.getConvertedImage(image, params);    // @todo implement this
    }

    // javadoc inherited
    protected NoFlushImageOutputStream outputImage(RenderedOp[] ops,
                                                   ObjectParameters params,
                                                   NoFlushImageOutputStream os)
        throws ImageWriterException {
        return outputImageSimple(ops, params, os);
    }

    // javadoc inherited
    protected long encodeImage(RenderedOp[] image, ObjectParameters params,
                               NoFlushImageOutputStream os)
        throws ImageWriterException, IOException {
        outputImageSimple(image, params, os);
        return os.getSize();
    }

    /**
     * Outputs image to GIF.
     *
     * @param ops    - image frames to output.
     * @param params - Parameters.
     * @param os     - output stream to write into.
     * @return output stream with GIF data.
     *
     * @throws ImageWriterException thrown if it is impossible to write image.
     */
    protected NoFlushImageOutputStream outputImageSimple(RenderedOp[] ops,
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

        os.mark();
        try {
            // If source wasn't animated gif we render only first frame.
            if (!params.containsName("GIFStreamMetadata") ||
                !params.containsName("GIFFrameMetadata")) {
                super.outputImage(new RenderedOp[]{ops[0]}, params, os);
            } else {
                RenderedOp singleBufferedImage;

                // Obtain the JAI writer.
                javax.imageio.ImageWriter writer;
                Iterator writers =
                    ImageIO.getImageWritersByMIMEType(mimeType());

                if (writers.hasNext()) {
                    writer = (javax.imageio.ImageWriter) writers.next();
                } else {
                    throw new IllegalStateException(
                        EXCEPTION_LOCALIZER.format(
                            "unsupported-mime-type", mimeType()));
                }

                if (writer.canWriteSequence()) {

                    writer.setOutput(os);

                    // Retrieve GIF metadata.
                    GIFStreamMetadata gifStreamMetadata =
                        (GIFStreamMetadata) params.getObject("GIFStreamMetadata");

                    GIFImageMetadata[] gifFrameMetadata =
                        (GIFImageMetadata[]) params.getObject("GIFFrameMetadata");

                    modifyMetadata(gifStreamMetadata,
                                   gifFrameMetadata,
                                   ops,
                                   params);

                    try {
                        writer.prepareWriteSequence(gifStreamMetadata);

                        ImageConvertor cnv = getImageConverter(ops[0], params);

                        for (int frameCount = 0; frameCount < ops.length;
                             frameCount++) {
                            singleBufferedImage =
                                cnv.convert(ops[frameCount], params);

                            IIOImage singleIIOImage =
                                new IIOImage(singleBufferedImage,
                                             null, null);
                            writer.writeToSequence(singleIIOImage, null);
                        }

                        writer.endWriteSequence();
                    } catch (Exception e) {
                        String msg = EXCEPTION_LOCALIZER.format(
                            "image-gif-encoding-error",
                            e.toString());
                        if (LOGGER.isErrorEnabled()) {
                            LOGGER.error(msg);
                        }
                        throw new ImageWriterException(e);
                    }
                    ImageOutputStream ios = (ImageOutputStream) writer.getOutput();
                    ios.flush();
                } else {
                    // If JAI writer doesn't support writing sequence
                    // we write only the first frame.
                    super.outputImage(new RenderedOp[]{ops[0]}, params, os);
                }
            }
            return os;
        } catch (Exception e) {
            throw new ImageWriterException(e);
        }
    }

    /**
     * Modifies GIF metadata with correspondence with processed images
     * characteristics.
     *
     * @param gifStreamMetadata - GIF stream metadata to modify.
     * @param gifFrameMetadata  - GIF frames metadata to modify.
     * @param ops               - processed images.
     * @param params            - Parameters.
     * @throws ImageWriterException if there is impossible to modify metadata.
     */
    protected void modifyMetadata(GIFStreamMetadata gifStreamMetadata,
                                  GIFImageMetadata gifFrameMetadata[],
                                  RenderedOp ops[],
                                  Parameters params) throws ImageWriterException {

        ImageConvertor cnv = getImageConverter(ops[0], params);

        //transform  color table
        IndexColorModel ct = null;
        try {
            ct = (IndexColorModel) cnv.convert(ops[0], params).
                getColorModel();
        } catch (ImageConvertorException e) {
            throw new ImageWriterException(e);
        }

        byte r[] = new byte[ct.getMapSize()];
        byte g[] = new byte[ct.getMapSize()];
        byte b[] = new byte[ct.getMapSize()];
        ct.getReds(r);
        ct.getGreens(g);
        ct.getBlues(b);
        gifStreamMetadata.colorResolution =
            (int) (Math.log((double) ct.getMapSize()) /
            Math.log(2d));
        gifStreamMetadata.globalColorTable = new byte[ct.getMapSize() * 3];
        for (int i = 0; i < ct.getMapSize(); i++) {
            gifStreamMetadata.globalColorTable[i * 3] = r[i];
            gifStreamMetadata.globalColorTable[i * 3 + 1] = g[i];
            gifStreamMetadata.globalColorTable[i * 3 + 2] = b[i];
        }

        //We have the same width and height for all frames.
        // Get them from 0-th frame.
        gifStreamMetadata.logicalScreenWidth = ops[0].getWidth();
        gifStreamMetadata.logicalScreenHeight = ops[0].getHeight();

        for (int frameCount = 0; frameCount < ops.length; frameCount++) {
            gifFrameMetadata[frameCount].imageHeight =
                ops[frameCount].getHeight();
            gifFrameMetadata[frameCount].imageWidth =
                ops[frameCount].getWidth();
            gifFrameMetadata[frameCount].transparentColorIndex =
                gifStreamMetadata.backgroundColorIndex;
        }
    }


    // Javadoc inherited
    protected String getFileFormat() {
        return ImageConstants.GIF_EXTENSION;
    }

    // Javadoc inherited
    protected ImageWriteParam getImageWriteParam(RenderedOp theImage,
                                                 Parameters params) {
        return null;
    }

    // javadoc inherited
    public String mimeType() {
        return "image/gif";
    }

}
