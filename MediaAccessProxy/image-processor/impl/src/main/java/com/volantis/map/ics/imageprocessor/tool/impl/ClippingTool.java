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
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.ics.imageprocessor.tool.clip.ClipXY;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * Tool for clipping images.
 */
public class ClippingTool extends DefaultTool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ClippingTool.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ClippingTool.class);

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

        RenderedOp clippedImage = image;
        int oldWidth = image.getWidth();

        int newWidth = -1;
        int preserveXLeft = -1;
        int preserveXRight = -1;
        try {
            if (params.containsName(ParameterNames.IMAGE_WIDTH)) {
                newWidth = params.getInteger(ParameterNames.IMAGE_WIDTH);
            }

            if (params.containsName(ParameterNames.PRESERVE_X_LEFT)) {
                preserveXLeft = params.getInteger(ParameterNames.PRESERVE_X_LEFT);
            }

            if (params.containsName(ParameterNames.PRESERVE_X_RIGHT)) {
                preserveXRight = params.getInteger(ParameterNames.PRESERVE_X_RIGHT);
            }
        } catch (MissingParameterException e) {
            String msg = EXCEPTION_LOCALIZER.format(
                "loading-param-failure", "newWidth, preserveXLeft, preserveXRight");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new ToolException(e);
        }
        if (newWidth > -1 && oldWidth > newWidth) {
            // should try to crop the image.

            ClipXY clipper = new ClipXY(image.getWidth(), preserveXLeft, preserveXRight,
                                        image.getHeight(), ImageConstants.NO_CLIP_LEFT,
                                        ImageConstants.NO_CLIP_RIGHT);

            clipper.clipTo(newWidth, image.getHeight());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ClipInfo: " +
                             image.getWidth() +
                             ", " +
                             preserveXLeft +
                             ", " +
                             preserveXRight);
                LOGGER.debug("ClipResultX: " +
                             clipper.getX().getSize() +
                             ", " +
                             clipper.getX().getClipOffset() +
                             ", " +
                             clipper.getX().getClipSize());
                LOGGER.debug("ClipResultX: " +
                             clipper.getY().getSize() +
                             ", " +
                             clipper.getX().getClipOffset() +
                             ", " +
                             clipper.getX().getClipSize());
            }

            ParameterBlock cropParams = new ParameterBlock();
            cropParams.addSource(clippedImage);
            cropParams.add((float) clipper.getX().getClipOffset());
            cropParams.add((float) clipper.getY().getClipOffset());
            cropParams.add((float) clipper.getX().getClipSize());
            cropParams.add((float) clipper.getY().getClipSize());

            try {
                clippedImage = JAI.create("crop", cropParams);
            } catch (Exception e) {
                String msg = EXCEPTION_LOCALIZER.format(
                    "jai-operation-failure", "crop");
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }
                throw new ToolException(e);
            }

            if (clipper.getX().getClipOffset() != 0 ||
                clipper.getY().getClipOffset() != 0) {
                // Move the image origin to 0,0
                // (this should not be necessary, but there is a bug
                // in the GIF writter and an exception is thrown if the
                // image has differen origin).
                ParameterBlock pb = new ParameterBlock();
                // The source image.
                pb.addSource(clippedImage);
                // The x translation.
                pb.add((float) -clipper.getX()
                                .getClipOffset());
                // The y translation.
                pb.add((float) -clipper.getY()
                                .getClipOffset());
                // The interpolation.
                pb.add(new InterpolationNearest());
                // Create the translate operation.
                try {
                    clippedImage = JAI.create("translate", pb, null);
                } catch (Exception e) {
                    String msg = EXCEPTION_LOCALIZER.format(
                        "jai-operation-failure", "translate");
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(msg);
                    }
                    throw new ToolException(e);
                }
            }
        }
        return clippedImage;
    }
}

