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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.dependency;

import com.volantis.shared.time.Period;

/**
 * Singleton uncacheable dependency
 */
public class UncacheableDependency implements Dependency {

    /**
     * Singleton instance.
     */
    private static final Dependency INSTANCE = new UncacheableDependency();

    /**
     * Returns the singleton instance of the uncacheable dependency.
     *
     * @return the uncacheable dependency
     */
    public static Dependency getInstance() {
        return INSTANCE;
    }

    private UncacheableDependency() {
        // hide it, there is no use to create new instances
    }

    // javadoc inherited
    public Cacheability getCacheability() {
        return Cacheability.UNCACHEABLE;
    }

    // javadoc inherited
    public Period getTimeToLive() {
        return Period.ZERO;
    }

    // javadoc inherited
    public Freshness freshness(final DependencyContext context) {
        return Freshness.STALE;
    }

    // javadoc inherited
    public Freshness revalidate(final DependencyContext context) {
        throw new UnsupportedOperationException(
            "Uncacheable dependencies cannot be revalidated.");
    }
}
