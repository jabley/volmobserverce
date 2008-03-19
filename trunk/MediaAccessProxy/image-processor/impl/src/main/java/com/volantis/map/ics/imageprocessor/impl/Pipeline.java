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

import com.volantis.map.ics.imageprocessor.tool.Tool;
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.jai.RenderedOp;

/**
 * Represents a pipeline containing processing tools.
 */
public class Pipeline {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(Pipeline.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(Pipeline.class);

    /**
     * Tools containing in the pipeline.
     */
    private List tools = new ArrayList();

    /**
     * Adds a tool to the pipeline.
     *
     * @param tool - Processing tool to add.
     */
    public void addTool(Tool tool) {
        if (tool == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "tool");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }

        tools.add(tool);
    }

    /**
     * Removes a tool from the pipeline.
     *
     * @param tool - Processing tool to remove.
     */
    public void removeTool(Tool tool) {
        if (tool == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "tool");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }

        tools.remove(tool);
    }

    /**
     * Lists tools contained in the pipeline.
     *
     * @return com.volantis.map.ics.imageprocessor.tool.Tool[]
     *
     * @throws PipelineException
     */
    public Tool[] listTools() throws PipelineException {
        Tool[] result = new Tool[tools.size()];
        tools.toArray(result);

        return result;
    }

    /**
     * Process images with processing tool which are currently in the pipeline.
     * The function just passes images through the tools in the same order as
     * was defined during tool adding. It means that first tool has been added
     * to the pipeline processes image first.
     *
     * @param ops    - Images to process.
     * @param params - Parameters for processing.
     * @return javax.media.jai.RenderedOp[]
     *
     * @throws PipelineException
     */
    public RenderedOp[] process(RenderedOp[] ops, ObjectParameters params)
        throws PipelineException {
        RenderedOp[] currentOps = ops;
        try {
            for (Iterator iterator = tools.iterator(); iterator.hasNext();) {
                Tool tool = (Tool) iterator.next();
                currentOps = tool.process(currentOps, params);
            }
        } catch (ToolException e) {
            throw new PipelineException(e);
        }
        return currentOps;
    }
}
