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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.debug;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Timer for debugging
 */
public class DebugTimer {

    private long startMillis;

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(DebugTimer.class);

    /**
     * start the timer running
     */
    public void beginTimer() {
        // Get the current time so we can calculate how long the process took
        startMillis = System.currentTimeMillis();

        if (logger.isDebugEnabled()) {
            logger.debug("DebugTimer started");
        }
    }

    /**
     * stop the timer running
     * @param msg - the msg to print on the debug log
     */
    public void stopTimer(String msg) {
        long endMillis = System.currentTimeMillis();
        long runtime = endMillis - startMillis;

        if (logger.isDebugEnabled()) {
            logger.debug(msg + runtime + "ms");
        }
    }
}
