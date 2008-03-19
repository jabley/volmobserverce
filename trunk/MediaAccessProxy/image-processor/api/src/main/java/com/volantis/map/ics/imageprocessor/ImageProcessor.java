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

package com.volantis.map.ics.imageprocessor;

import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.operation.ObjectParameters;

import javax.imageio.stream.ImageInputStream;

/**
 * The controller class for image processing machine(IPM). The main
 * responsibilities are configuring IPM and getting the images through the
 * IPM.
 */
public interface ImageProcessor {

    /**
     * Processes input image with IPM and returns processed image.
     *
     * @param inputData  - Image data to be processed.
     * @param outputData - Output stream to be filled with processed image's
     *                   data.
     * @param params     - Processing parameters.
     * @return OutputStream filled with processed image's data.
     *
     * @throws ImageProcessorException - if there was something wrong during
     *                                 processing.
     */
    public NoFlushImageOutputStream process(ImageInputStream inputData,
        NoFlushImageOutputStream outputData,
        ObjectParameters params) throws ImageProcessorException;
}
