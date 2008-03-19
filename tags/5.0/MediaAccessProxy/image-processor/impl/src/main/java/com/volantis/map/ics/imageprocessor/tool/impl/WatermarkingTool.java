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
package com.volantis.map.ics.imageprocessor.tool.impl;

import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.reader.impl.ImageReader;
import com.volantis.map.ics.imageprocessor.tool.Tool;
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.ics.imageprocessor.utilities.ImageUtils;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.imageio.stream.ImageInputStream;
import javax.media.jai.*;
import javax.media.jai.operator.CompositeDescriptor;
import javax.media.jai.operator.TranslateDescriptor;

/**
 * Tool for watermarking images
 */
public class WatermarkingTool implements Tool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(WatermarkingTool.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(WatermarkingTool.class);


    //javadoc inherited
    public static RenderedOp processSingleFrame(RenderedOp image,
                                         ObjectParameters params,
                                         RenderedOp watermark)
        throws ToolException {

        ParameterBlockJAI pb = new ParameterBlockJAI("composite");
        pb.addSource(ImageUtils.removeAlphaChannel(watermark));
        pb.addSource(ImageUtils.removeAlphaChannel(image));
        pb.setParameter("source1Alpha", ImageUtils.extractAlphaChannel(watermark));
        pb.setParameter("source2Alpha", ImageUtils.extractAlphaChannel(image));
        pb.setParameter("alphaPremultiplied", Boolean.FALSE);
        pb.setParameter("destAlpha", CompositeDescriptor.DESTINATION_ALPHA_LAST);
        RenderedOp result;

        try {
            result = JAI.create("composite", pb);
        } catch (Exception e) {
            String[] msgError = new String[]{"composite", e.toString()};
            String msg =
                EXCEPTION_LOCALIZER.format("jai-operation-failure",
                                           msgError);
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new ToolException(e);
        }
        return result;
    }

    //javadoc inherited
    public RenderedOp[] process(RenderedOp[] ops, ObjectParameters params)
        throws ToolException {
        if (ops == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null", "ops");
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

        RenderedOp watermark = null;
        try {
            ImageInputStream iis = null;
            if (params.containsName(ParameterNames.WATERMARK_INPUT_STREAM)) {
                iis = (ImageInputStream) params.getObject(
                    ParameterNames.WATERMARK_INPUT_STREAM);
            }
            watermark = ImageReader.loadImage(iis)[0];

            ImageConvertor cnv =
                ImageConvertorFactory.getInstance().
                getImageConvertor(ImageRule.TRUECOLOUR);
            watermark = cnv.convert(watermark, params);
            params.setObject(ParameterNames.WATERMARK_IMAGE, watermark);
        } catch (Exception e) {
            throw new ToolException(
                EXCEPTION_LOCALIZER.format("loading-watermark-failure"));
        }
        
        //We use image to transform watermark (scale, add transparent border).
        //To improve performance we suppose all the frames have the same size.
        //So the parameters for watermark transformation is retrieved from the
        // first frams.
        RenderedOp image = ops[0];

        //Scale the watermark to fit into the source image.
        float xFactor = (float) image.getWidth() / watermark.getWidth();
        float yFactor = (float) image.getHeight() / watermark.getHeight();
        float factor;
        int leftPad = 0;
        int bottomPad = 0;

        // Get the smallest factor to guarantee that watermark will fit
        // into image entirely.
        if (xFactor > yFactor) {
            factor = yFactor;
            leftPad = (int) ((xFactor - yFactor) * watermark.getWidth() / 2.0);
        } else {
            factor = xFactor;
            bottomPad = (int) ((yFactor - xFactor) * watermark.getHeight()/ 2.0);
        }

        ParameterBlockJAI scalePb = new ParameterBlockJAI("scale");
        scalePb.addSource(watermark);
        // X scale factor
        scalePb.setParameter("xScale", new Float(factor));
        // Y scale factor
        scalePb.setParameter("yScale", new Float(factor));
        // Interpolation method (should really use something better then
        // NN here)
        scalePb.setParameter("interpolation",
                Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        try {
            watermark = JAI.create("scale",
                                   scalePb,
                                   null);
        } catch (Exception e) {
            String[] msgError = new String[]{"scale", e.toString()};
            String msg = EXCEPTION_LOCALIZER.format("jai-operation-failure",
                                                    msgError);
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new ToolException(e);
        }

        if (leftPad != 0 || bottomPad != 0) {
            ParameterBlockJAI boarderPb = new ParameterBlockJAI("border");
            boarderPb.addSource(watermark);

            if (leftPad != 0) {
                Integer padding = new Integer(leftPad);
                boarderPb.setParameter("leftPad", padding);
                boarderPb.setParameter("rightPad", padding);
            } else if (bottomPad != 0) {
                Integer padding = new Integer(bottomPad);
                boarderPb.setParameter("bottomPad", padding);
                boarderPb.setParameter("topPad", padding);
            }
            //Transparent border.
            boarderPb.setParameter("type", new BorderExtenderConstant(new double[]{1}));
            try {
                watermark = JAI.create("border", boarderPb);
            } catch (Exception e) {
                String[] msgError = new String[]{"border", e.toString()};
                String msg =
                    EXCEPTION_LOCALIZER.format("jai-operation-failure",
                                               msgError);
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }
                throw new ToolException(e);
            }

            // perform nearest neighbour interpolation as these should only be integer
            // translations
            watermark = TranslateDescriptor.create(watermark,
                    new Float(-watermark.getMinX()),
                    new Float(-watermark.getMinY()),
                    Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                    null);
        }

        RenderedOp[] imageAfterProcess =
            new RenderedOp[ops.length];

        // Process each frame with processSingleFrame.
        for (int i = 0; i < imageAfterProcess.length; i++) {
            imageAfterProcess[i] =
                processSingleFrame(ops[i], params, watermark);
        }
        return imageAfterProcess;
    }
}
