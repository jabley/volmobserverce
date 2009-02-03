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
 * (c) Copyright Volantis Systems Ltd. 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.reader.impl;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.map.localization.LocalizationFactory;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/**
 * This conditioner fixes JPEG files which do not strictly adhere to the standard
 *
 * See http://www.fileformat.info/format/jpeg/
 * See http://en.wikipedia.org/wiki/JPEG
 */
public class JPEGConditioner implements InputImageConditioner {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(JPEGConditioner.class);

    /**
     * Wraps into a conditioning stream if needed
     */
    public ImageInputStream process(ImageInputStream input) throws IOException {

        ImageInputStream output = null;

        // Mark the initial position
        input.mark();

        // Analyze the input if we cann pass through
        if (JPEGSegment.identify(input) != JPEGMarker.SOI) {
            // Not a JPEG image. Pass through.
            output = input;
        } else if (JPEGSegment.identify(input) == JPEGMarker.APP0) {
            JPEGSegment s = JPEGSegment.read(JPEGMarker.APP0, input);
            if (JPEGSegment.ID_JFIF.equals(s.getId())) {
                // APP0 JFIF segment present at the beginning.
                // No need for conditioning. Pass through.
                output = input;
            }
        }
        // Go back to the marked position
        input.reset();

        if (null == output) {
            if (logger.isDebugEnabled()) {
                logger.debug("Using JPEG conditioning stream.");
            }
            // Wrap in a conditioning stream
            output =  new JPEGConditioningInputStream(input);
        }

        return output;
    }
}
