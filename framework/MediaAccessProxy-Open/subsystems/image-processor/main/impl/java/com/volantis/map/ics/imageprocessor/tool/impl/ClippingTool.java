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

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.ImageInformation;
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.ics.imageprocessor.tool.clip.ClipXY;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

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


    /**
     * javadoc inherited
     *
     * Reada first collection image and calculates clip area if needed.
     * Calculated clip area is added to params to be used in further single frame processing
     */
    public RenderedOp[] process(final RenderedOp[] images, final ObjectParameters params) throws ToolException {

        RenderedOp firstImage = images[0];
        int oldWidth = firstImage.getWidth();

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
            LOGGER.error("loading-param-failure", "newWidth, preserveXLeft, preserveXRight");
            throw new ToolException(e);
        }
        if (newWidth > -1 && oldWidth > newWidth) {
            // should try to crop the image.
            ClipXY clipper = new ClipXY(firstImage.getWidth(), preserveXLeft, preserveXRight,
                                        firstImage.getHeight(), ImageConstants.NO_CLIP_LEFT,
                                        ImageConstants.NO_CLIP_RIGHT);

            clipper.clipTo(newWidth, firstImage.getHeight());
            params.setObject(ParameterNames.CLIP_AREA, clipper);
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

        RenderedOp clippedImage = image;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("OriginalImage:" + ImageInformation.asString(image));
        }

        ClipXY clipper = null;

        // should try to crop the image.
        if (params.containsName(ParameterNames.CLIP_AREA)) {
            clipper = (ClipXY) params.getObject(ParameterNames.CLIP_AREA);
        }

        if (clipper != null) {
            // should try to crop the image.

            // adjust clipper for selected frame. It has to be cloned to preserve
            // initial clip area for further processing
            ClipXY frameClipper = (ClipXY) clipper.clone();
            frameClipper.adjustTo(image.getMinX(), image.getWidth(), image.getMinY(), image.getHeight());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ClipInfo: " + clipper.toString());
                LOGGER.debug("FrameClipInfo: " + frameClipper.toString());
            }

            ParameterBlock cropParams = new ParameterBlock();
            cropParams.addSource(clippedImage);
            cropParams.add((float) frameClipper.getX().getClipOffset());
            cropParams.add((float) frameClipper.getY().getClipOffset());
            cropParams.add((float) frameClipper.getX().getClipSize());
            cropParams.add((float) frameClipper.getY().getClipSize());

            try {
                clippedImage = JAI.create("crop", cropParams);
            } catch (Exception e) {
                LOGGER.error("jai-operation-failure", "crop");
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
                    LOGGER.error("jai-operation-failure", "translate");
                    throw new ToolException(e);
                }
            }
        }
        return clippedImage;
    }
}

