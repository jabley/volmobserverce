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

import com.volantis.map.ics.imageprocessor.tool.Tool;
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.media.jai.RenderedOp;

/**
 * Default tool implementation of the Tool interface.
 */
public abstract class DefaultTool implements Tool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultTool.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(DefaultTool.class);

    /**
     * Reads input RenederedOp image and transforms it as required.
     *
     * @param image  - Image data.
     * @param params - Parameters.
     * @return RenderedOp processed imagel.
     *
     * @throws ToolException if there is impossible to process image.
     */
    public abstract RenderedOp processSingleFrame(RenderedOp image,
                                                  ObjectParameters params)
        throws ToolException;

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
        RenderedOp[] imageAfterProcess =
            new RenderedOp[ops.length];

        // Process each frame with processSingleFrame.
        for (int i = 0; i < imageAfterProcess.length; i++) {
            imageAfterProcess[i] =
                processSingleFrame(ops[i], params);
        }
        return imageAfterProcess;
    }
}
