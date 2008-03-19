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
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface CacheControlInfo {

    /**
     * Return a flag denoting if the cache this policy property was set
     *
     * @return boolean flag
     */
    boolean cacheThisPolicyWasSet();

    /**
     * Return a flag denoting if the retain during retry property was set
     *
     * @return boolean flag
     */
    boolean retainDuringRetryWasSet();

    /**
     * Return a flag denoting if the retry failed retrieval property was set
     *
     * @return boolean flag
     */
    boolean retryFailedRetrievalWasSet();

    /**
     * Return a flag denoting if the retry interval was set
     *
     * @return boolean flag
     */
    boolean retryIntervalWasSet();

    /**
     * Return a flag denoting if the retry maximum count property was set
     *
     * @return boolean flag
     */
    boolean retryMaxCountWasSet();

    /**
     * Return a flag denoting if the time to live was set.
     *
     * @return boolean flag
     */
    boolean timeToLiveWasSet();
}
