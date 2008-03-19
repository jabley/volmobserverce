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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.utilities;

import com.volantis.map.ics.imageprocessor.ImageInformation;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.RenderingHints;
import java.awt.image.BandedSampleModel;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.DeferredProperty;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.CompositeDescriptor;

/**
 * This class provides some useful utility methods to use for processing
 * images.
 */
public class ImageUtils {

    /**
     * The logging object to use in this class for localised logging services.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ImageUtils.class);

    /**
     * Removes the alpha channel from an image by merging the image with a opaque
     * white background using the alpha channel. The channel is then removed.
     *
     * @param src  the image to process.
     * @param size the required size of the color component arrays, if the
     *             image has an indexed colour model. Note that size must be a
     *             power of two, otherwise the GIFImageWriter fails. If the
     *             image has no indexed colour model then size is ignored.
     * @return a new image representing the input image with alpha removed, or
     *         the input image if no alpha is present
     */
    public static RenderedOp applyAlphaChannel(RenderedOp src,
                                                int size) {
        RenderedOp result = src;

        final ColorModel cm = src.getColorModel();

        if (cm.hasAlpha()) {
            if (!(cm instanceof IndexColorModel)) {
                result = applyAlphaFromNonIndexedImage(src);
            } else {
                result = applyAlphaFromIndexedImage(src, size);
            }
        }
        return result;
    }

    /**
     * Retrieves alpha band from the image. If the original image has no
     * alpha band then this method returns a constant single plane alpha
     * with a value of 127
     *
     * @param src source image to retrieve the alpha channel from. The image
     *            must be in ComponentColorModel.
     * @return RenderedOp representing alpha band of the source image.
     */
    public static RenderedOp extractAlphaChannel(RenderedOp src) {
        ColorModel cm = src.getColorModel();

        if (!cm.getClass().equals(ComponentColorModel.class)) {
            throw new IllegalArgumentException(
                "Color model of the source " +
                "image isn't ComponentColorModel");
        }

        RenderedOp result = null;
        
        if (cm.getNumComponents() > cm.getNumColorComponents()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Performing band select and discarding alpha");
            }
            ParameterBlock selectPB = new ParameterBlock();
            selectPB.addSource(src);
            selectPB.add(new int[]{cm.getNumComponents() - 1});
            result = JAI.create("bandselect", selectPB);
        } else {
            ParameterBlockJAI constPb = new ParameterBlockJAI("constant");
            constPb.setParameter("width", new Float(src.getWidth()));
            constPb.setParameter("height", new Float(src.getHeight()));
            // half transparent
            constPb.setParameter("bandValues", new Byte[]{new Byte((byte)127)});
            result = JAI.create("constant", constPb);
        }
        return result;
    }

    /**
     * Creates constant image with pointed bands.
     *
     * @param width      width of the image to create.
     * @param height     height of the image to create
     * @param bandValues band values. This parameter defines the number of
     *                   bands to be in image and band values to fill up the
     *                   bands. For example, if this is three element array of
     *                   {1, 2, 3} then image with three bands will be created;
     *                   all pixels of the first band will have value 1, of the
     *                   second band - 2 and for 3-th band it will be 3.
     * @return created image.
     */
    public static RenderedOp getConstantImage(int width,
                                              int height,
                                              byte[] bandValues) {
        Byte bv[] = new Byte[bandValues.length];
        for (int i = 0; i < bandValues.length; i++) {
            bv[i] = new Byte(bandValues[i]);
        }
        ParameterBlock pb = new ParameterBlock();
        pb.add(new Float(width));   // The width
        pb.add(new Float(height));  // The height
        pb.add(bv);         // The band values

        // Create the constant operation.
        return JAI.create("constant", pb);
    }

    /**
     * Helper method to remove the alpha channel from an image with an indexed
     * colour model.
     *
     * @param src  the image to transform
     * @param size the required size of the color component arrays. Note that
     *             size must be a power of two, otherwise the GIFImageWriter
     *             fails.
     * @return the transformed image
     */
    private static RenderedOp applyAlphaFromIndexedImage(RenderedOp src,
                                                          int size) {
        final IndexColorModel oldModel = (IndexColorModel) src.getColorModel();

        if (logger.isDebugEnabled()) {
            logger.debug("Creating new colour model for image without " +
                         "alpha channel.");
        }

        int[] rgbs = new int[size];
        oldModel.getRGBs(rgbs);
        IndexColorModel newModel = new IndexColorModel(
            oldModel.getPixelSize(),
            size,
            rgbs,
            0,
            false,
            -1,
            oldModel.getTransferType());

        // Create an image layout from the input image (this makes the
        // layout have the same values as the imput image).
        ImageLayout layout = new ImageLayout(src);
        // Set the color model of the ImageLayout to the new ColorModel
        // without the alpha/transparency included
        layout.setColorModel(newModel);
        // Create renderinghints to tell the format operation what to do
        // i.e. use the image layout created above and use it during
        // the formatting operation.
        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT,
                                                  layout);
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(src);
        // Finally apply the formatting to the image
        RenderedOp result = JAI.create("format", pb, hints);

        return result;
    }

    /**
     * Remove the alpha channel from the src image. This method just rips
     * the alpha off. If you want to apply the alpha with a white
     * opaque background see {@link #applyAlphaChannel}. If the src
     * has not alpha band then the src is returned unchanged.
     *
     * @param src the source image
     * @return the image with the alpha channel removed
     */
    public static RenderedOp removeAlphaChannel(RenderedOp src) {
        final ColorModel cm = src.getColorModel();
        RenderedOp result = src;
        if (cm.getNumComponents() > cm.getNumColorComponents()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Performing band select and discarding alpha");
            }
            // select the first x bands assuming they are the color
            // channels.
            int[] bandSelects = new int[cm.getNumColorComponents()];
            for (int i = 0; i < cm.getNumColorComponents(); i++) {
                bandSelects[i] = i;
            }
            ParameterBlock selectPB = new ParameterBlock();
            selectPB.addSource(src);
            selectPB.add(bandSelects);
            result = JAI.create("bandselect", selectPB);
        }
        return result;
    }

    /**
     * Helper method to remove the alpha channel from an image with a
     * non-indexed colour model, using band selection.
     *
     * @param src the image to transform
     * @return the transformed image
     */
    private static RenderedOp applyAlphaFromNonIndexedImage(RenderedOp src) {
        RenderedOp result = src;

        final ColorModel cm = src.getColorModel();

        if (cm.getNumComponents() > cm.getNumColorComponents()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Performing band select and discarding alpha");
            }
            // get the color channels
            RenderedOp color = removeAlphaChannel(src);

            // select alpha band
            RenderedOp alpha = extractAlphaChannel(src);

            // create white background image
            ParameterBlock blankPB = new ParameterBlock();
            blankPB.add((float)src.getWidth());
            blankPB.add((float)src.getHeight());

            Object[] data;
            final int dataType = color.getSampleModel().getDataType();
            switch (dataType) {
                case DataBuffer.TYPE_BYTE:
                    data = new Byte[]{
                        new Byte((byte) 255), new Byte((byte) 255), new Byte((byte) 255)};
                    break;
                case DataBuffer.TYPE_USHORT:
                    data = new Short[]{
                        new Short((short) 65535), new Short((short) 65535), new Short((short) 65535)};
                    break;
                case DataBuffer.TYPE_INT:
                    data = new Integer[]{
                        new Integer(0xFFFFFFFF), new Integer(0xFFFFFFFF), new Integer(0xFFFFFFFF)};
                    break;
                case DataBuffer.TYPE_FLOAT:
                    data = new Float[]{
                        new Float(1.0f), new Float(1.0f), new Float(1.0f)};
                    break;
                case DataBuffer.TYPE_DOUBLE:
                    data = new Double[]{
                        new Double(1.0), new Double(1.0), new Double(1.0)};
                    break;
                default:
                    throw new IllegalArgumentException("Unknown data buffer type: " +
                        dataType);
            }
            blankPB.add(data);
            blankPB.addSource(color);
            RenderedOp blank = JAI.create("constant", blankPB);

            // apply alpha using color bands and background 
            ParameterBlock applyAlphaPB = new ParameterBlock();
            applyAlphaPB.addSource(color); 
            applyAlphaPB.addSource(blank);
            applyAlphaPB.add(alpha);
            applyAlphaPB.add(null);
            applyAlphaPB.add(Boolean.FALSE);
            applyAlphaPB.add(CompositeDescriptor.NO_DESTINATION_ALPHA);

            result = JAI.create("composite", applyAlphaPB);
        }

        return result;
    }

    /**
     * Dithers the image using the Floyd Steinberg algorithm and an colour map
     * derived using a median-cut algorithm (1). <p> 1) Color Image
     * Quantization for Frame Buffer Display, Paul Heckbert, SIGGRAPH
     * proceedings, 1982, pp. 297-307 </p>
     *
     * This will build a colour map with up to 256 entries in it and then
     * use error diffusion to map colours to it.
     *
     * @param src the image to dither
     * @return the dithered image
     */
    public static RenderedOp ditherFloydSteinberg(RenderedOp src) {
        if (logger.isDebugEnabled()) {
            logger.debug("Dithering image using Floyd Steinberg");
        }

        // The error-diffussion operator is very churlish wrt the in-memory
        // format of the image. This has caused numerous problems.
        // Therefore the brute force aprroach is here taken to change the
        // in-memory format of the image to a type that we know the
        // operator likes. This is not fool proof. It converts all images
        // into non-indexed representations that have a byte pixel width
        ImageLayout layout = new ImageLayout();
        layout.setSampleModel(
            new BandedSampleModel(DataBuffer.TYPE_BYTE,
                                  src.getWidth(),
                                  src.getHeight(),
                                  src.getColorModel().getNumColorComponents()));
        // set up the rendering hints object that is passed to the format
        // operation to effect this change.
        RenderingHints rh = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

        // reformat the image to something acceptable to "errordiffusion"
        ParameterBlock formatPB = new ParameterBlock();
        formatPB.addSource(src);
        formatPB.add(DataBuffer.TYPE_BYTE);
        src = JAI.create("format", formatPB, rh);
        if (logger.isDebugEnabled()) {
            logger.debug("format performed " + ImageInformation.asString(src));
        }

        // create an optimized color map for the dithering process. This helps
        // avoid speckled looking areas of uniform colour
        ParameterBlockJAI pb2 = new ParameterBlockJAI("ColorQuantizer");
        pb2.addSource(src);
        RenderedOp lookupTable = JAI.create("ColorQuantizer", pb2, null);
        // defer computation of the lookup table. Note that "LUT" should really
        // be "JAI.LookupTable" according to the docs
        DeferredProperty lupTable = new DeferredProperty(lookupTable, "LUT",
                                                         LookupTableJAI.class);

        // use the obtained lookup table to optimize the dithering process
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(src);
        // @todo later remove the following line when the line after it works
        pb.add(lupTable.getData());
        // @todo later uncomment this line when deferred blocks are accepted by
        // @todo later errordiffusion
        // pb.add(lupTable);
        pb.add(KernelJAI.ERROR_FILTER_FLOYD_STEINBERG);

        // Perform the error diffusion operation.
        RenderedOp result = JAI.create("errordiffusion", pb, null);

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Apr-05	388/3	pcameron	VBM:2005030810 Fixed cg8 rule when used with transparent PNGs

 24-Mar-05	386/1	pcameron	VBM:2005030810 Fixed cg8 rule when used with transparent PNGs

 ===========================================================================
*/
