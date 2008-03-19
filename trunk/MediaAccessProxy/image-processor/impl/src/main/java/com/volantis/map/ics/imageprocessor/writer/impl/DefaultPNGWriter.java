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

package com.volantis.map.ics.imageprocessor.writer.impl;

import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.common.param.Parameters;

import javax.imageio.ImageWriteParam;
import javax.media.jai.RenderedOp;

/**
 * Output methods common to all PNG output classes.
 */
public abstract class DefaultPNGWriter extends DefaultWriter {

    // javadoc inherited
    protected String getFileFormat() {
        return ImageConstants.PNG_EXTENSION;
    }

    // javadoc inherited
    protected ImageWriteParam getImageWriteParam(RenderedOp image,
                                                 Parameters params) {
        return null;
    }

    // javadoc inherited
    public String mimeType() {
        return "image/png";
    }
}
