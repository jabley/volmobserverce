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
package com.volantis.shared.net.url;

import com.volantis.shared.time.Time;

/**
 * Interface to detect stale cache entries.
 */
public interface CachedUrlContentState {

    /**
     * Returns true iff the cache entry is stale at the specified time.
     *
     * @param time the time, may not be null
     * @return true if the entry is stale
     */
    boolean isStale(Time time);
}
