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

package com.volantis.shared.dependency;

import com.volantis.shared.time.Period;

/**
 * Base class for all user implementations of
 */
public class DependencyImpl
        implements Dependency {

    /**
     * Default implementation.
     *
     * @return {@link Cacheability#CACHEABLE}.
     */
    public Cacheability getCacheability() {
        return Cacheability.CACHEABLE;
    }

    /**
     * Default implementation.
     *
     * @return {@link Period#INDEFINITELY}.
     */
    public Period getTimeToLive() {
        return Period.INDEFINITELY;
    }

    /**
     * Default implementation.
     *
     * @param context The context within which this dependency is being
     *                checked.
     * @return {@link Freshness#FRESH}.
     */
    public Freshness freshness(DependencyContext context) {
        return Freshness.FRESH;
    }

    /**
     * Default implementation.
     *
     * @param context The context within which this dependency is being
     *                checked.
     * @throws IllegalStateException As this should never be called unless
     *                               {@link #freshness(DependencyContext)}
     *                               returns {@link Freshness#REVALIDATE}.
     */
    public Freshness revalidate(DependencyContext context) {
        throw new IllegalStateException(
                "This method must be overridden by " + getClass() +
                " as freshness() returned Freshness.REVALIDATE");
    }
}
