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
/*----------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 *----------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.logging;

import com.volantis.map.ics.imageprocessor.ImageInformation;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.media.jai.RenderedOp;

/**
 * Utility class for creating a log message for an image.
 */
public class ImageLogger {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ImageLogger.class);

    /**
     * Utility method to write the information about a RenderedOp to the log
     * file.
     */
    public static void log(RenderedOp img) {
        if (logger.isDebugEnabled()) {
            logger.debug(ImageInformation.asString(img));
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jan-05	289/1	matthew	VBM:2005012617 Fix problems with transcoding gif/png with transparency and refactor test case

 29-Nov-04	235/1	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	36/2	claire	VBM:2004021902 Internationalization of messages

 ===========================================================================
*/
