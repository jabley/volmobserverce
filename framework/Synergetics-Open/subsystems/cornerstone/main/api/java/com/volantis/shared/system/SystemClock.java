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

package com.volantis.shared.system;

import com.volantis.shared.time.Time;

/**
 * Abstracts away the system clock.
 *
 * @mock.generate 
 */
public class SystemClock {

    /**
     * The default instance of the system clock.
     */
    private static final SystemClock DEFAULT_INSTANCE = new SystemClock();

    /**
     * Get the default instance of the system clock.
     *
     * @return The default instance of the system clock.
     */
    public static SystemClock getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Get the current time in milliseconds.
     *
     * @return The current time in milliseconds.
     */
    public Time getCurrentTime() {
        return Time.inMilliSeconds(System.currentTimeMillis());
    }
}
