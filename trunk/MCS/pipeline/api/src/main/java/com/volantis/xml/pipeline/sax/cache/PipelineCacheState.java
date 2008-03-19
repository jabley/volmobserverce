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
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Comparator;
import com.volantis.shared.time.Time;


/**
 * Encapsulates all the state necessary to determine whether a cache entry in
 * the pipeline has expired.
 */
public class PipelineCacheState {

    /**
     * The expiration time.
     */
    private final Time expirationTime;

    /**
     * Initialise.
     *
     */
    public PipelineCacheState(final Time expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * Check whether the associated policy has expired.
     *
     * @param clock
     * @return True if it has false otherwise.
     */
    public boolean hasExpired(SystemClock clock) {
        final boolean hasExpired;

        if (expirationTime == Time.NEVER ) {
            hasExpired = false;
        } else {
            hasExpired = Comparator.GE.compare(
                    clock.getCurrentTime(), expirationTime);
        }

        return hasExpired;
    }
}
