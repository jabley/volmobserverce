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

import com.volantis.map.ics.imageprocessor.ImageProcessor;
import com.volantis.map.ics.imageprocessor.ImageProcessorFactory;
import com.volantis.map.ics.imageprocessor.tool.ToolFactory;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactory;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Implementation of the ImageProcessorFactory.
 */
public class ImageProcessorFactoryImpl extends ImageProcessorFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ImageProcessorFactoryImpl.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            ImageProcessorFactoryImpl.class);

    /**
     * Initializes new ImageProcessor with given parameters.
     *
     * @param toolFactory   - tool factory.
     * @param writerFactory - writer factory.
     * @return ImageProcessor instance of the image processor.
     */
    public ImageProcessor createImageProcessor(ToolFactory toolFactory,
                                               ImageWriterFactory writerFactory) {
        if (toolFactory == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "toolFactory");
            throw new IllegalArgumentException(msg);
        }

        if (writerFactory == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "writerFactory");
            throw new IllegalArgumentException(msg);
        }

        return new ImageProcessorImpl(toolFactory,
                                      writerFactory);
    }
}
