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

import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;
import com.volantis.map.common.streams.NoFlushSeekableOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterException;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.map.common.param.Parameters;

import javax.media.jai.RenderedOp;

public class GIFMonochromeWriter extends DefaultGIFWriter {

    /**
     * Output an image in moncochrome. If a maximum image size is set, then the
     * image is scaled to fit that size.
     *
     * @note rest of javadoc inherited.
     */
    protected NoFlushSeekableOutputStream outputImage(RenderedOp[] ops,
                                                   ObjectParameters params,
                                                   NoFlushSeekableOutputStream os)
        throws ImageWriterException {

        // Explicitly calling super as the javadoc comment for this method
        // is different from the one defined in DefaultWriter.
        return super.outputImage(ops, params, os);
    }

    // javadoc inherited
    protected ImageConvertor getImageConverter(RenderedOp theImage,
                                               Parameters params) {
        return ImageConvertorFactory.getInstance().getImageConvertor(
            ImageRule.MONOCHROME);
    }

    // Javadoc inherited
    protected String getImageDescription() {
        return "monochrome image";
    }
}
