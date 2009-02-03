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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.tool.impl;


import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * Tool for cropping image.
 *
 * Following params will be used: - LeftX - TopY upper left coordinagets of
 * output image - RightX - BottomY bottom right coordinates of ouptut image. As
 * it was described in VBM 2007030119 point (RightX,BottomY) has special
 * meaning and normally is excluded from output except (1.0,1.0) that means
 * right-most column and bottom-most line of image that will not be excluded.
 *
 * 'crop' and 'translate' operations from Java Advanced Imaging wil be used to
 * crop image.
 */
public class CroppingTool extends DefaultTool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(CroppingTool.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(CroppingTool.class);


    /**
     * helper with float comparison
     */
    private static final float E = 0.000001f;


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
        int oldWidth = image.getWidth();
        int oldHeight = image.getHeight();

        float rightX = 1.0f;
        float leftX = 0.0f;
        float topY = 0.0f;
        float bottomY = 1.0f;
        try {
            if (params.containsName(ParameterNames.BOTTOM_Y)) {
                bottomY = params.getFloat(ParameterNames.BOTTOM_Y);
            }
            if (params.containsName(ParameterNames.TOP_Y)) {
                topY = params.getFloat(ParameterNames.TOP_Y);
            }
            if (params.containsName(ParameterNames.LEFT_X)) {
                leftX = params.getFloat(ParameterNames.LEFT_X);
            }
            if (params.containsName(ParameterNames.RIGHT_X)) {
                rightX = params.getFloat(ParameterNames.RIGHT_X);
            }
        } catch (MissingParameterException e) {
            LOGGER.error("loading-param-failure", "newWidth, preserveXLeft, preserveXRight");
            throw new ToolException(e);
        }

        // normalize points
        topY = topY < 0.0f ? 0.0f : topY;
        leftX = leftX < 0.0f ? 0.0f : leftX;
        rightX = rightX > 1.0f ? 1.0f : rightX;
        bottomY = bottomY > 1.0f ? 1.0f : bottomY;

        float newX = (float) Math.floor(leftX * oldWidth);
        float newY = (float) Math.floor(topY * oldHeight);
        float newWidth = (float) Math.floor((rightX - leftX) * oldWidth);
        float newHeight = (float) Math.floor((bottomY - topY) * oldHeight);

        try {
            /*
             * cropping
             */
            ParameterBlock cropParams = new ParameterBlock();
            cropParams.addSource(clippedImage);
            cropParams.add(newX);
            cropParams.add(newY);
            cropParams.add(newWidth);
            cropParams.add(newHeight);

            clippedImage = JAI.create("crop", cropParams);

            /*
             * image must be shifted after crop operation
             */
            ParameterBlock translateParams = new ParameterBlock();
            translateParams.addSource(clippedImage);
            translateParams.add(-1 * newX);
            translateParams.add(-1 * newY);

            clippedImage = JAI.create("translate", translateParams);

        } catch (Exception e) {
            LOGGER.error("jai-operation-failure", "crop");
            throw new ToolException(e);
        }
        return clippedImage;
    }

}
