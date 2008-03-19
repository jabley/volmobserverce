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
import com.volantis.shared.time.Time;
import com.volantis.shared.system.SystemClock;

/**
 * Dependency with fixed age.
 */
public class FixedTTLDependency implements Dependency {

    /**
     * Time of expiry.
     */
    private final Time expires;

    /**
     * The clock to compute fresh TTL values.
     */
    private final SystemClock clock;

    /**
     * Creates a new dependency.
     *
     * @param clock the clock to compute time of expiry and fresh TTL
     * @param initialTTL the initial TTL value
     */
    public FixedTTLDependency(final SystemClock clock, final Period initialTTL) {
        this.clock = clock;
        expires = clock.getCurrentTime().addPeriod(initialTTL);
    }

    // javadoc inherited
    public Cacheability getCacheability() {
        return Cacheability.CACHEABLE;
    }

    // javadoc inherited
    public Period getTimeToLive() {
        return expires.getPeriodSince(clock.getCurrentTime());
    }

    // javadoc inherited
    public Freshness freshness(final DependencyContext context) {
        return getTimeToLive().inMillis() >= 0?
            Freshness.FRESH: Freshness.STALE;
    }

    // javadoc inherited
    public Freshness revalidate(final DependencyContext context) {
        return Freshness.STALE;
    }
}
