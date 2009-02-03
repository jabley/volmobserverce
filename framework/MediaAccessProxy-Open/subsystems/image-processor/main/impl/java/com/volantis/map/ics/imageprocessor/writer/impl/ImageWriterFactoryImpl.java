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

import com.volantis.map.ics.configuration.OutputImageRules;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.imageprocessor.writer.ImageWriter;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactory;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactoryException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the ImageWriterFactory.
 */
public class ImageWriterFactoryImpl extends ImageWriterFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ImageWriterFactoryImpl.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            ImageWriterFactoryImpl.class);

    /**
     * Rules to writer class map.
     */
    public static final Map RULES;

    // Initialize
    static {
        Map rules = new HashMap();

        // BMP rules.
        rules.put(OutputImageRules.COLOURBMP24, new BMPImageWriter());
        // WMBP rules.
        rules.put(OutputImageRules.WBMP, new WBMPImageWriter());
        // TIFF rules
        rules.put(OutputImageRules.COLOURTIFF24, new TIFFImageWriter());
        // PNG rules.
        rules.put(OutputImageRules.GRAYSCALEPNG1, new PNGMonochromeWriter());
        rules.put(OutputImageRules.GRAYSCALEPNG2, new PNGGreyscale4Writer());
        rules.put(OutputImageRules.GRAYSCALEPNG4, new PNGGreyscale16Writer());
        rules.put(OutputImageRules.GRAYSCALEPNG8, new PNGGreyscaleWriter());
        rules.put(OutputImageRules.GRAYSCALEPNG16, new PNGGreyscaleWriter());
        rules.put(OutputImageRules.COLOURPNG8, new PNGIndexedWriter());
        rules.put(OutputImageRules.COLOURPNG24, new PNGColourWriter());
        // JPEG rules.
        rules.put(OutputImageRules.GREYSCALEJPEG8, new JPEGGreyscaleWriter());
        rules.put(OutputImageRules.COLOURJPEG24, new JPEGColourWriter());
        // GIF rules: gg1, gg2, gg4, gg8, and cg8.
        rules.put(OutputImageRules.GREYSCALEGIF1, new GIFMonochromeWriter());
        rules.put(OutputImageRules.GREYSCALEGIF2, new GIFGreyscale4Writer());
        rules.put(OutputImageRules.GREYSCALEGIF4, new GIFGreyscale16Writer());
        rules.put(OutputImageRules.GREYSCALEGIF8, new GIFGreyscaleWriter());
        rules.put(OutputImageRules.COLOURGIF8, new GIFIndexedWriter());
        RULES = Collections.synchronizedMap(rules);
    }

    //javadoc inherited
    public ImageWriter getWriter(String imageRule, Parameters params)
        throws ImageWriterFactoryException {
        if (imageRule == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "imageRule");
            throw new IllegalArgumentException(msg);
        }
        if (params == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "params");
            throw new IllegalArgumentException(msg);
        }
        if ("".equals(imageRule)) {
            String msg = EXCEPTION_LOCALIZER.format(
                "argument-value-is-empty-string",
                "imageRule");
            throw new IllegalArgumentException(msg);
        }

        try {
            // If required output gif image, we control supporting gif by
            // image.
            boolean isGifEnabled = params.getBoolean(ParameterNames.GIF_ENABLED);

            if (imageRule.length() >= 2 &&
                imageRule.charAt(1) == 'g' &&
                !isGifEnabled) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Writer requested for GIF rule " +
                                 imageRule +
                                 ", but GIF support disabled, returning null");
                }
                return null;
            }
            if (!RULES.containsKey(imageRule)) {
                String msg = EXCEPTION_LOCALIZER.format("rule-unknown",
                                                        "imageRule");
                throw new ImageWriterFactoryException(msg);
            }
            return (ImageWriter) RULES.get(imageRule);
        } catch (Exception e) {
            LOGGER.error("image-writing-failure", e.toString());
            throw new ImageWriterFactoryException(e);
        }
    }
}
