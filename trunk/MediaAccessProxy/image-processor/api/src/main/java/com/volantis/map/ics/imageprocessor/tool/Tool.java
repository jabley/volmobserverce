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

package com.volantis.map.ics.imageprocessor.tool;

import com.volantis.map.operation.ObjectParameters;

import javax.media.jai.RenderedOp;

/**
 * Represents image processing tool. The tool processes a set of images with
 * pointed processing params and generate output set of images.
 */
public interface Tool {

    /**
     * Processes a set of images with pointed processing params and generate
     * output set of images.
     *
     * @param ops    - Images for processing, must not be null.
     * @param params - Parameters for processing, must not be null.
     * @return RenderedOp[] - processed images.
     *
     * @throws ToolException if it is impossible to process images.
     */
    public RenderedOp[] process(RenderedOp[] ops, ObjectParameters params)
        throws ToolException;
}
