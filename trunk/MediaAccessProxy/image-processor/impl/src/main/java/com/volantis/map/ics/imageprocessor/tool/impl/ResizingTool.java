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

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.ImageInformation;
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.RenderingHints;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.BorderExtender;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * Tool for resizing images.
 */
public class ResizingTool extends DefaultTool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ResizingTool.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ResizingTool.class);

    /**
     * The rendering hints used when scaling an image.
     */
    private static final RenderingHints RENDERING_HINTS;

    // Create the BorderExtender and use it in the rendering hints.
    static {
        final BorderExtender borderExtender =
            BorderExtender.createInstance(BorderExtender.BORDER_COPY);
        RENDERING_HINTS =
            new RenderingHints(JAI.KEY_BORDER_EXTENDER, borderExtender);
    }

    //javadoc inherited
    public RenderedOp processSingleFrame(RenderedOp image, ObjectParameters params)
        throws ToolException {
        if (image == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "image");
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
        RenderedOp scaledImage = image;
        int oldWidth = image.getWidth();

        int newWidth = -1;
        int scaleMode = -1;
        boolean canScaleLarger = false;
        try {
            newWidth = params.getInteger(ParameterNames.IMAGE_WIDTH);
            scaleMode = params.getInteger(ParameterNames.SCALE_MODE);
            canScaleLarger = params.getBoolean(ParameterNames.SCALE_LARGER);
        } catch (MissingParameterException e) {
            String msg = EXCEPTION_LOCALIZER.format("loading-param-failure",
                                                    "newWidth, scaleMode, canScaleLarger");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new ToolException(e);
        }
        if ((newWidth > -1) && (newWidth != image.getWidth())) {

            int mode;

            switch (scaleMode) {
                case ImageConstants.SCALE_MODE_BICUBIC:
                    mode = Interpolation.INTERP_BICUBIC;
                    break;

                case ImageConstants.SCALE_MODE_NEAREST:
                    mode = Interpolation.INTERP_NEAREST;
                    break;

                default:
                    mode = Interpolation.INTERP_BILINEAR;
                    break;
            }

            if ((canScaleLarger && (newWidth > oldWidth))
                || (newWidth < oldWidth)) {
                float factor = (float) newWidth / (float) oldWidth;

                ParameterBlock createParams = new ParameterBlock();
                createParams.addSource(image);
                // X scale factor.
                createParams.add(factor);
                // Y scale factor.
                createParams.add(factor);
                // X translation.
                createParams.add(0f);
                // Y translation.
                createParams.add(0f);
                // Interpolation method.
                createParams.add(Interpolation.getInstance(mode));

                try {
                    scaledImage = JAI.create("scale",
                                             createParams,
                                             RENDERING_HINTS);
                } catch (Exception e) {
                    String msg = EXCEPTION_LOCALIZER.format(
                        "jai-operation-failure", "scale");
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(msg);
                    }
                    throw new ToolException(e);
                }
                // If the scaling enlarges the image we must crop off the
                // border extension. Reducing the size of the image
                // automatically gets rid of the border extension.
                if (canScaleLarger && newWidth > oldWidth) {
                    ParameterBlock cropParams = new ParameterBlock();
                    cropParams.addSource(scaledImage);
                    cropParams.add(0f);
                    cropParams.add(0f);
                    cropParams.add((float) scaledImage.getWidth());
                    cropParams.add((float) scaledImage.getHeight());
                    RenderedOp cropped = null;
                    try {
                        cropped = JAI.create("crop", cropParams);
                    } catch (Exception e) {
                        String msg = EXCEPTION_LOCALIZER.format(
                            "jai-operation-failure", "crop");
                        if (LOGGER.isErrorEnabled()) {
                            LOGGER.error(msg);
                        }
                        throw new ToolException(e);
                    }
                    scaledImage = cropped;
                }
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Scaled:" + ImageInformation.asString(image));
                LOGGER.debug("To:" + ImageInformation.asString(scaledImage));
            }
        }

        return scaledImage;
    }
}
