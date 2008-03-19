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

package com.volantis.map.ics.imageprocessor.impl;

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.ImageProcessor;
import com.volantis.map.ics.imageprocessor.ImageProcessorException;
import com.volantis.map.ics.imageprocessor.reader.impl.ImageReader;
import com.volantis.map.ics.imageprocessor.tool.Tool;
import com.volantis.map.ics.imageprocessor.tool.ToolFactory;
import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriter;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactory;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.io.IOUtils;

import java.awt.image.ColorModel;
import java.io.IOException;

import javax.imageio.stream.ImageInputStream;
import javax.media.jai.RenderedOp;

/**
 * Image processor implementation.
 */
public class ImageProcessorImpl implements ImageProcessor {

    /**
     * Used for producing tools.
     */
    private ToolFactory theToolFactrory;

    /**
     * Used for producing image writers.
     */
    private ImageWriterFactory theImageWriterFactory;

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ImageProcessorImpl.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ImageProcessorImpl.class);

    /**
     * Initializes new ImageProcessorImpl instance with given parameters.
     *
     * @param toolFactory   - tool factory.
     * @param writerFactory - writer factory.
     */
    public ImageProcessorImpl(ToolFactory toolFactory,
                              ImageWriterFactory writerFactory) {
        theImageWriterFactory = writerFactory;
        theToolFactrory = toolFactory;
    }

    //javadoc inherited
    public synchronized NoFlushImageOutputStream process(
        ImageInputStream inputData,
        NoFlushImageOutputStream outputData,
        ObjectParameters params)
        throws ImageProcessorException {

        if (inputData == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "inputData");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }

        if (outputData == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "outputData");
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

        // if there is no destination rule in parameter block it means
        // that we should just pass the input into output.
        if (params.containsName(ParameterNames.DESTINATION_FORMAT_RULE)) {
           // ImageReader reader = null;
            ImageWriter writer = null;
            Pipeline pipeline = null;

            try {
                writer = theImageWriterFactory.
                    getWriter(params.getParameterValue(
                        ParameterNames.DESTINATION_FORMAT_RULE), params);
            } catch (Exception e) {
                String msg = EXCEPTION_LOCALIZER.format("instantiating-error",
                                                        "image-writer");
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }
                throw new ImageProcessorException(e);
            }
            if (writer == null) {
                String msg = EXCEPTION_LOCALIZER.format("instantiating-error",
                                                        "image-writer");
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }
                throw new ImageProcessorException(msg);
            }

            try {
                // Build up processing pipeline.
                pipeline = new Pipeline();

                //Add cropping tool. when there are
                //all needed parameters, otherwise
                // tool will not be used
                if (params.containsName(ParameterNames.LEFT_X) &&
                    params.containsName(ParameterNames.RIGHT_X) &&
                    params.containsName(ParameterNames.BOTTOM_Y) &&
                    params.containsName(ParameterNames.TOP_Y)) {
                    Tool croppingTool =
                        theToolFactrory.getTool("CroppingTool");
                    pipeline.addTool(croppingTool);
                }


                // We should guarantee that all other tools works
                // with RGB images.
                pipeline.addTool(theToolFactrory.getTool("RGBConverterTool"));

                // Add clipping tool if it is needed.
                if (params.containsName(ParameterNames.PRESERVE_X_LEFT) ||
                    params.containsName(ParameterNames.PRESERVE_X_RIGHT)) {
                    Tool clippingTool =
                        theToolFactrory.getTool("ClippingTool");
                    pipeline.addTool(clippingTool);
                }

                // Add resizing tool if it is needed.
                if (params.containsName(ParameterNames.IMAGE_WIDTH)) {
                    Tool resizingTool =
                        theToolFactrory.getTool("ResizingTool");
                    pipeline.addTool(resizingTool);
                }

                
                //Add watermarking tool.
                if (params.containsName(ParameterNames.WATERMARK_URL)) {
                    Tool watermarkingTool =
                        theToolFactrory.getTool("WatermarkingTool");
                    pipeline.addTool(watermarkingTool);
                }

                RenderedOp[] ops = null;
                ops = ImageReader.loadImage(inputData);

                // hack to get around incorrect behaviour associated with
                // the scale operation vbm.2004121009
                // if source has an indexed colour model and has a
                // transparency which is not OPAQUE and has an Alpha then
                // switch back to nearest neighbour interpolation for the
                // scaling.
                if (ops.length > 0)
                {
                    ColorModel colorModel = ops[0].getColorModel();
                    if (colorModel.hasAlpha() && colorModel.getTransparency()
                        != ColorModel.OPAQUE) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Resetting scale mode to " +
                                         "SCALE_MODE_NEAREST");
                        }
                        params.setParameterValue(
                            ParameterNames.SCALE_MODE,
                            Integer.toString(ImageConstants.SCALE_MODE_NEAREST));
                    }
                }

                // Run the pipeline and process images.
                ops = pipeline.process(ops, params);

                // Write images into chosen format.
                outputData.mark();
                outputData = writer.process(ops, params, outputData);
            } catch (Exception e) {
                String msg = EXCEPTION_LOCALIZER.format("processor-failure");
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }
                throw new ImageProcessorException(e);
            }
        } else {
           
            //Pump the data.
            byte buffer[] = new byte[1024];
            int readBytes;
            try {
                while ((readBytes = inputData.read(buffer)) != -1) {
                    outputData.write(buffer, 0, readBytes);
                }
            } catch (IOException e) {
                throw new ImageProcessorException(e);
            }
            try {
                params.setParameterValue(ParameterNames.OUTPUT_IMAGE_MIME_TYPE,
                                params.getParameterValue(ParameterNames.SOURCE_IMAGE_MIME_TYPE));
            } catch (MissingParameterException e) {
                throw new ImageProcessorException(e);
            }
        }
        return outputData;
    }


}
