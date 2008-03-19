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
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.operation.ObjectParameters;

import javax.imageio.ImageWriteParam;
import javax.media.jai.RenderedOp;

/**
 * Output an image in WBMP. Monochrome only.
 */
public class WBMPImageWriter extends DefaultWriter {

    // javadoc inherited
    protected NoFlushImageOutputStream outputImage(RenderedOp[] ops,
                                                   ObjectParameters params,
                                                   NoFlushImageOutputStream os)
        throws ImageWriterException {
        // Explicitly calling super as the javadoc comment for this method
        // is different from the one defined in DefaultWriter.
        return super.outputImage(ops, params, os);
    }

    // Javadoc inherited
    protected ImageConvertor getImageConverter(RenderedOp theImage,
                                               Parameters params) {
        return ImageConvertorFactory.getInstance().getImageConvertor(
            ImageRule.MONOCHROME);
    }

    // javadoc inherited
    public String mimeType() {
        return "image/vnd.wap.wbmp";
    }

    // Javadoc inherited
    protected String getFileFormat() {
        return ImageConstants.WBMP_EXTENSION;
    }

    // Javadoc inherited
    protected String getImageDescription() {
        return "Monochrome WBMP image";
    }

    // Javadoc inherited
    protected ImageWriteParam getImageWriteParam(RenderedOp theImage,
                                                 Parameters params) {
        return null;
    }
}
