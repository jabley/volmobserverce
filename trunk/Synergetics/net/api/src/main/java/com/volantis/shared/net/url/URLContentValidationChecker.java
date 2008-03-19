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
package com.volantis.shared.net.url;

import com.volantis.cache.CacheEntry;
import com.volantis.cache.expiration.ExpirationChecker;
import com.volantis.shared.system.SystemClock;

/**
 * ExpirationChecker for caches storing URLContents. The cached entries are
 * expected to store {@link CachedUrlContentState} objects as extension objects
 * and these will be used to check entry staleness.
 */
public class URLContentValidationChecker implements ExpirationChecker {

    // javadoc inherited
    public boolean hasExpired(final SystemClock clock, final CacheEntry entry) {
        final CachedUrlContentState state =
            (CachedUrlContentState) entry.getExtensionObject();
        return state.isStale(clock.getCurrentTime());
    }
}
