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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.reader.impl;

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.ics.imageprocessor.impl.GenericCollectionImage;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;
import java.awt.image.DataBuffer;

import javax.imageio.stream.ImageInputStream;
import javax.media.jai.CollectionImage;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.FormatDescriptor;
import javax.media.jai.operator.RescaleDescriptor;

/**
 * Simple utility class that assists in reading images
 */
public class ImageReader {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ImageReader.class);

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ImageReader.class);

    private ImageReader() {
        // hide constructor
    }

    /**
     * A utility method for loading images. This tries to load images by asking
     * the registered readers to identify it. If this fails then we try readers
     * registered against the mime type in the parameters (if it exists). If
     * this fails then we try the file name extension.
     *
     * @param inputData
     * @return
     *
     * @throws com.volantis.map.common.param.MissingParameterException
     *
     */
    public static RenderedOp[] loadImage(ImageInputStream inputData)
        throws MissingParameterException {
        if (inputData == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "inputData");
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new IllegalArgumentException(msg);
        }

        CollectionImage input = new GenericCollectionImage();
        ParameterBlockJAI parameters = new ParameterBlockJAI("ImageRead");

        parameters.setParameter("Input", inputData);
        try {
            input.add(JAI.create("ImageRead", parameters));
        } catch (Exception e) {
            String[] msgError = new String[]{"ImageRead", e.toString()};
            String msg = EXCEPTION_LOCALIZER.format("jai-operation-failure",
                                                    msgError);
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }
            throw new RuntimeException(e);
        }
        final RenderedOp[] result = new RenderedOp[input.size()];
        int i = 0;
        for (Iterator iter = input.iterator(); iter.hasNext(); i++) {
            RenderedOp op = (RenderedOp) iter.next();
            final int dataType = op.getSampleModel().getDataType();
            if (dataType != DataBuffer.TYPE_BYTE) {
                op = changeDataTypeToByte(op);
            }
            result[i] = op;
        }
        return result;
    }

    /**
     * Changes the data type to byte.
     *
     * @param op the RenderedOp to convert. Cannot be null.
     * @return the converted RenderedOp
     * @throws IllegalArgumentException if the data type of the original image
     * is not byte, short or int.
     */
    private static RenderedOp changeDataTypeToByte(final RenderedOp op) {
        final int dataType = op.getSampleModel().getDataType();
        final double maxValue;
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
                maxValue = 255.0;
                break;
            case DataBuffer.TYPE_USHORT:
                maxValue = 65535.0;
                break;
            case DataBuffer.TYPE_INT:
                maxValue = 0xFFFFFFFF;
                break;
            default:
                throw new IllegalArgumentException(
                    "Unsupported data buffer type: " + dataType);
        }

        final RenderedOp rescaled = RescaleDescriptor.create(
            op, new double[]{255.0 / maxValue}, new double[]{0}, null);

        final RenderedOp formatted = FormatDescriptor.create(
            rescaled, new Integer(DataBuffer.TYPE_BYTE), null);
        return formatted;
    }
}
