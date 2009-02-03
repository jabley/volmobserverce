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
import javax.media.jai.CollectionImage;

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
    public RenderedOp[] process(final RenderedOp[] images,
                                   final ObjectParameters params)
         throws ToolException {

        try {
            // the height to which we would like to restrict the image
            int expectedHeight = -1;
            int newWidth = -1;
            boolean canScaleLarger;
            RenderedOp firstImage = images[0];
            if (params.containsName("ImageHeight")) {
                expectedHeight =
                     params.getInteger("ImageHeight");
            }
            newWidth = params.getInteger("ImageWidth");

            canScaleLarger = params.getBoolean("CanScaleLarger");
            int oldWidth = firstImage.getWidth();
            if ((newWidth > -1 && newWidth != firstImage.getWidth()) ||
                (expectedHeight != -1 && expectedHeight != firstImage.getHeight())) {
                float factor = (float) newWidth / (float) oldWidth;
                if (expectedHeight > 0) {
                    if ((firstImage.getHeight() * factor) > expectedHeight) {
                        factor = (float) expectedHeight / (float) firstImage.getHeight();
                        newWidth = (int) (oldWidth * factor);
                    }
                }
                if (factor <=1.0 || canScaleLarger) {
                    params.setObject("ScaleFactor", factor);
                }
            }
        } catch (MissingParameterException e) {
            LOGGER.error("loading-param-failure",
                "newWidth, scaleMode, canScaleLarger");
            throw new ToolException(e);
        }

        return super.process(images, params);
    }


    //javadoc inherited
    public RenderedOp processSingleFrame(RenderedOp image, ObjectParameters params)
        throws ToolException {
              if (image == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "image");
            throw new IllegalArgumentException(msg);
        }
        if (params == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                "params");
            throw new IllegalArgumentException(msg);
        }
        RenderedOp scaledImage = image;


        Float scaleFactor = null;

        if (params.containsName("ScaleFactor")) {
            try {
                scaleFactor = params.getFloat("ScaleFactor");
            } catch (MissingParameterException e) {
                /* OK, will use default */
            }
        }

        if (scaleFactor != null) {
            int scaleMode = -1;
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


            float factor = scaleFactor;
            // if any individual image would be scaled to < 1x1 pixel then just drop it
            if(!(factor * image.getWidth() < 1.0f || factor * image.getHeight() < 1.0f)) {
                        //factor = 1.0f;

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
               // createParams.add(Interpolation.getInstance(mode));
                 createParams.add(Interpolation.getInstance(Interpolation.INTERP_NEAREST));

                try {
                    scaledImage = JAI.create("scale",
                        createParams,
                        RENDERING_HINTS);
                } catch (Exception e) {
                    LOGGER.error("jai-operation-failure", "scale");
                    throw new ToolException(e);
                }

                // If the scaling enlarges the image we must crop off the
                // border extension. Reducing the size of the image
                // automatically gets rid of the border extension.
                if (factor > 1.0) {
                    ParameterBlock cropParams = new ParameterBlock();
                    cropParams.addSource(scaledImage);
                    cropParams.add((float) scaledImage.getMinX());
                    cropParams.add((float) scaledImage.getMinY());
                    cropParams.add((float) scaledImage.getWidth());
                    cropParams.add((float) scaledImage.getHeight());
                    RenderedOp cropped = null;
                    try {
                        cropped = JAI.create("crop", cropParams);
                    } catch (Exception e) {
                        LOGGER.error("jai-operation-failure", "crop");
                        throw new ToolException(e);
                    }
                    scaledImage = cropped;
                }
            } else {
                scaledImage = null;
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Scaled:" + ImageInformation.asString(image));
            LOGGER.debug("To:" + ImageInformation.asString(scaledImage));
        }


        return scaledImage;
    }
}
