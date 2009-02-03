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
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.streams.NoFlushSeekableOutputStream;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.logging.ImageLogger;
import com.volantis.map.ics.imageprocessor.tool.ToolFactory;
import com.volantis.map.ics.imageprocessor.utilities.ImageUtils;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.imageio.ImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.media.jai.ColorSpaceJAI;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ColorConvertDescriptor;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;

public class JPEGColourWriter extends DefaultWriter {

    /**
     * The default quality level at which to encode the JPEG.
     */
    private static final float DEFAULT_QUALITY = 0.90F;

    /**
     * Value for calculating new image size.
     */
    private long lastLength = 0;

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(JPEGColourWriter.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(JPEGColourWriter.class);

    // javadoc inherited
    public String mimeType() {
        return "image/jpeg";
    }

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

            RenderedOp noTrans = ImageUtils.applyAlphaChannel(ops[0], 0);

            if (LOGGER.isDebugEnabled()) {
                ImageLogger.log(noTrans);
            }

            if (!params.containsName(ParameterNames.MAX_IMAGE_SIZE)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No maximum size set. Writing image at " +
                                 DEFAULT_QUALITY + " quality.");
                }

                outputFrame(os, noTrans, getFileFormat(), params);
            } else {
                int maxImageSize = params.getInteger(ParameterNames.MAX_IMAGE_SIZE);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Making image fit into " +
                                 maxImageSize + " bytes.");
                }
                shrinkImage(noTrans, params, DEFAULT_QUALITY, os);
            }
            return os;
        } catch (Exception e) {
            LOGGER.error("image-writing-failure", e.toString());
            throw new ImageWriterException(e);
        }
    }

    /**
     * Remove transparency and alpha information from an IndexColorModel image
     * if it exists.  Otherwise the original image is returned.
     *
     * @param input the RenderedOp to process.
     * @return a new image representing the input image with transparency and
     *         alpha removed or the input image if applying this operation is
     *         inappropriate.
     */
    private static RenderedOp removeTransparency(RenderedOp input) {
        // Return the passed in operation.
        RenderedOp result = input;

        // If it is appropriate to process this image then do so.
        if (input.getColorModel() instanceof IndexColorModel
            && input.getColorModel().hasAlpha()) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transparency information being removed " +
                             "from the image");
            }

            IndexColorModel oldModel = (IndexColorModel) input.getColorModel();
            // Set the colour you wish transparent pixels to become.
            int transIndex = oldModel.getTransparentPixel();
            int substitute = 0xFFFFFFFF;  // is white.

            // Create a new colour map from the r, g and b components of the
            // input ColorModel.
            int[] rgbs = new int[oldModel.getMapSize()];
            oldModel.getRGBs(rgbs);

            // replace transparent pixel value.
            if (transIndex >= 0) {
                rgbs[transIndex] = substitute;
            }

            // Create a new color model the same as the old one.  The 5th and
            // 6th operands indicate no alpha and provide an invalid
            // transparency type.  Together these make the image opaque with
            // no alpha channel.
            ColorModel newModel =
                new IndexColorModel(oldModel.getPixelSize(),
                                    oldModel.getMapSize(),
                                    rgbs,
                                    0,
                                    false,
                                    -1,
                                    oldModel.getTransferType());

            // Create an image layout from the input image (this makes the
            // layout have the same values as the imput image).
            ImageLayout layout = new ImageLayout(input);
            // Set the color model of the ImageLayout to the new ColorModel
            // without the alpha/transparency included.
            layout.setColorModel(newModel);
            // Create renderinghints to tell the format operation what to do
            // i.e. use the image layout created above and use it during
            // the formatting operation.
            RenderingHints rHint = new RenderingHints(JAI.KEY_IMAGE_LAYOUT,
                                                      layout);
            ParameterBlock tmpPb = new ParameterBlock();
            tmpPb.addSource(input);
            // Finally apply the formatting to the image.
            result = JAI.create("Format", tmpPb, rHint);
        }
        return result;
    }

    /**
     * Shrink an image to make it fit in a given amount of memory.
     *
     * @param src     the RenderedOp to process.
     * @param params  Parameters.
     * @param quality Image quality.
     * @param os      Output for image.
     * @return a new image representing the input image with transparency and
     *         alpha removed or the input image if applying this operation is
     *         inappropriate.
     *
     * @throws ImageWriterException if an error occurs while writing.
     */
    private NoFlushSeekableOutputStream shrinkImage(RenderedOp src,
                                     ObjectParameters params,
                                     float quality,
                                     NoFlushSeekableOutputStream os)
        throws ImageWriterException {
        try {
            /*
             * Degrade the image by reducing the image quality until it either
             * fits in the required memory or it has reached the minimum
             * quality allowed.
             */
            ImageConvertor cvt =
                ImageConvertorFactory.getInstance().getImageConvertor(
                    ImageRule.TRUECOLOUR);

            /**
             * This converts the color model to linear RGB. This causes the
             * JPEG writer to NOT write out a ICC color profile as Linear RGB
             * is recognized as a standard. This is not necessary on JDK 1.6
             * and later. VBM.2008011813. Not writing the ICC color profile
             * saves about 3.5Kb in file size allowing us to produce larger
             * images for a given maxfilesize
             */
            if (src.getColorModel().getNumColorComponents() > 1) {
                SampleModel sm = src.getColorModel().
                    createCompatibleSampleModel(src.getWidth(), src.getHeight());
                ColorSpace sc = ColorSpaceJAI.getInstance(ColorSpaceJAI.CS_sRGB);
                ColorModel cm = new ComponentColorModel(sc, sm.getSampleSize(),
                    src.getColorModel().hasAlpha(),
                    src.getColorModel().isAlphaPremultiplied(),
                    src.getColorModel().getTransparency(),
                    src.getColorModel().getTransferType());
                src = ColorConvertDescriptor.create(src, cm, null);                
            }

            // ensure the minimum quality is never more then the default as
            // otherwise we won't terminate
            float minQuality =
                (float) (params.
                    getInteger(ParameterNames.MINIMUM_JPEG_QUALITY));
            if (minQuality >= DEFAULT_QUALITY * 100f) {
                LOGGER.warn("invalid-parameter-value", new Object[] {
                    ParameterNames.MINIMUM_JPEG_QUALITY,
                        Math.round(DEFAULT_QUALITY * 100f)});
                params.setObject(ParameterNames.MINIMUM_JPEG_QUALITY,
                        Math.round(DEFAULT_QUALITY * 100f));
            }

            RenderedOp resizedSrc = src;
            while (!degradeImage(os, resizedSrc, params, quality)) {
                /*
                 * Degradation did not get the image small enough so scale it
                 * and try again.
                 */
                quality = params.getInteger(ParameterNames.MINIMUM_JPEG_QUALITY) / 100F;
                Dimension d = new Dimension(src.getWidth(), src.getHeight());
                int maxImageSize = params.getInteger(ParameterNames.MAX_IMAGE_SIZE);
                Dimension newD = cvt.calcScale(d, lastLength, maxImageSize);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Scaling image to width " + newD.width);
                }
                RenderedOp[] img = new RenderedOp[1];
                img[0] = src;
                int oldImageWidth = -1;
                if (params.containsName(ParameterNames.IMAGE_WIDTH)) {
                    oldImageWidth = params.getInteger(ParameterNames.IMAGE_WIDTH);
                }
                params.setParameterValue(ParameterNames.IMAGE_WIDTH, Integer.toString(newD.width));

                RenderedOp[] tmpImgs = ToolFactory.getInstance().getTool("ResizingTool").process(img, params);
                if (tmpImgs == null || tmpImgs.length == 0) {
                    // cannot resize more
                    break;
                } else {
                    src = tmpImgs[0];
                }
                resizedSrc = src;
                img = null;
                if (oldImageWidth != -1) {
                    params.setParameterValue(ParameterNames.IMAGE_WIDTH, Integer.toString(oldImageWidth));
                } else {
                    params.removeParameterValue(ParameterNames.IMAGE_WIDTH);
                }
            }
            os.reset();
            os.mark();
            outputFrame(os, src, getFileFormat(), params);
        } catch (Exception e) {
            LOGGER.error("image-writing-failure", e.toString());
            throw new ImageWriterException(e);
        }
        return os;
    }

    /**
     * Degrading image by minimizing image quality.
     *
     * @param stream   - Image data.
     * @param src      - the RenderedOp to process.
     * @param params   - Parameters.
     * @param quality- Image quality.
     * @return true if image can be degrading.
     *
     * @throws ImageWriterException
     */
    private boolean degradeImage(NoFlushSeekableOutputStream stream,
                                 RenderedOp src,
                                 Parameters params,
                                 float quality) throws ImageWriterException {
        try {
            float currentQuality = quality;
            float minQuality = params.getInteger(ParameterNames.MINIMUM_JPEG_QUALITY) / 100F;

            JPEGImageWriteParam jpegParam = new JPEGImageWriteParam(null);
            jpegParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegParam.setCompressionQuality(currentQuality);
            jpegParam.setOptimizeHuffmanTables(true);
            if (params.containsName(ParameterNames.JPEG_MODE)) {
                int mode = params.getInteger(ParameterNames.JPEG_MODE);
                if (mode == ImageConstants.JPEG_PROGRESSIVE) {
                    jpegParam.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
                }
            }

            stream.reset();
            stream.mark();

            ParameterBlockJAI paramBlock = new ParameterBlockJAI("ImageWrite");
            paramBlock.addSource(src);
            paramBlock.setParameter("Format", "jpeg");
            paramBlock.setParameter("WriteParam", jpegParam);
            paramBlock.setParameter("Output", stream);
            JAI.create("ImageWrite", paramBlock);

            int maxLength = params.getInteger(ParameterNames.MAX_IMAGE_SIZE);
            long length = stream.getSize();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Compression quality is " + currentQuality +
                             " image size is " + length + " bytes.");
            }

            float nextQuality;
            float diff;

            while ((length > maxLength) && (currentQuality > minQuality)) {
                stream.reset();
                stream.mark();
                nextQuality =
                    (currentQuality * (float) maxLength) / (float) length;
                diff = (currentQuality - nextQuality) * 1.5F;

                if (diff < 0.01F) {
                    diff = 0.01F;
                }

                currentQuality = currentQuality - diff;

                if (currentQuality < minQuality) {
                    currentQuality = minQuality;
                }

                paramBlock = new ParameterBlockJAI("ImageWrite");
                paramBlock.addSource(src);
                paramBlock.setParameter("Format", "jpeg");
                paramBlock.setParameter("WriteParam", jpegParam);
                paramBlock.setParameter("Output", stream);
                JAI.create("ImageWrite", paramBlock);

                length = stream.getSize();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Compression quality is " + currentQuality +
                                 " image size is " + length + " bytes.");
                }
            }

            lastLength = length;

            if ((length <= maxLength) && (currentQuality >= minQuality)) {

                if (LOGGER.isDebugEnabled()) {
                    ImageLogger.log(src);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ImageWriterException(e);
        }
    }

    // Javadoc inherited
    protected ImageConvertor getImageConverter(RenderedOp theImage,
                                               Parameters params) {
        String message = "This method is not required for JPEGColourWriter";
        throw new UnsupportedOperationException(message);
    }

    // Javadoc inherited
    protected String getFileFormat() {
        return ImageConstants.JPEG_EXTENSION;
    }

    // Javadoc inherited
    protected String getImageDescription() {
        return "JPEG Colour Image";
    }

    // Javadoc inherited
    protected ImageWriteParam getImageWriteParam(RenderedOp theImage,
                                                 Parameters params) {
        // we use this to ensure that the Huffman tables are generated
        // correctly. OK to pass in a null Locale to the JPEGImageWriteParam
        // ctor
        JPEGImageWriteParam param = new JPEGImageWriteParam(null);
        param.setOptimizeHuffmanTables(true);

        return param;
    }
}
