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
package com.volantis.map.ics.imageprocessor.tool.impl;

import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorException;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.media.jai.RenderedOp;

/**
 * Tool for converting images to component color model. It is assumed that all
 * the images is processed in RGB mode in ICS, thus this tool should be the
 * first tool in the pipeline to guarantee that all other tools works with RGB
 * images.
 */
public class RGBConverterTool extends DefaultTool {

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
     * Used for image conversion.
     */
    private ImageConvertor cnv;

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

        RenderedOp result = null;
        synchronized (this) {
            if (cnv == null) {
                cnv =
                    ImageConvertorFactory.getInstance().getImageConvertor(
                        ImageRule.TRUECOLOUR);
            }

            try {
                result = cnv.convert(image, params);

            } catch (ImageConvertorException e) {
                String msg = EXCEPTION_LOCALIZER.format("processing-error",
                                                        e.toString());
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }
                throw new ToolException(e);
            }
        }
        return result;
    }
}
