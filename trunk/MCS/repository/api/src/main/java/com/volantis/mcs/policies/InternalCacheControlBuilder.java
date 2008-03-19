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

package com.volantis.mcs.policies;

/**
 * Provides access to some internal cache control information.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface InternalCacheControlBuilder
        extends CacheControlBuilder, CacheControlInfo {
    /**
     * Clear the cache this policy property.
     */
    void clearCacheThisPolicy();

    /**
     * Clear the retain during retry property.
     */
    void clearRetainDuringRetry();

    /**
     * Clear the retry failed retrieval property.
     */
    void clearRetryFailedRetrieval();

    /**
     * Clear the retry interval property.
     */
    void clearRetryInterval();

    /**
     * Clear the retry maximum count property.
     */
    void clearRetryMaxCount();

    /**
     * Clear the time to live property.
     */
    void clearTimeToLive();
}
