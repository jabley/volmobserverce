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

package com.volantis.xml.expression.impl;

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.time.Comparator;
import com.volantis.shared.time.Period;

import java.util.Collection;

public class Dependencies
        implements Dependency {

    private final Dependency[] dependencies;

    public Dependencies(Collection collection) {
        this.dependencies = new Dependency[collection.size()];
        collection.toArray(dependencies);
    }

    // Javadoc inherited.
    public Cacheability getCacheability() {

        Cacheability cacheability = Cacheability.CACHEABLE;
        for (int i = 0; i < dependencies.length &&
                cacheability == Cacheability.CACHEABLE; i++) {
            Dependency dependency = dependencies[i];
            cacheability = dependency.getCacheability();
        }
        return cacheability;
    }

    public Period getTimeToLive() {
        Period min = Period.INDEFINITELY;
        for (int i = 0; i < dependencies.length &&
                Comparator.NE.compare(min, Period.ZERO); i++) {
            Dependency dependency = dependencies[i];
            Period timeToLive = dependency.getTimeToLive();
            min = Period.min(timeToLive, min);
        }

        return min;
    }

    public Freshness freshness(DependencyContext context) {

        DependencyContext internal =
                context;

        Freshness aggregate = Freshness.FRESH;
        for (int i = 0; i < dependencies.length &&
                aggregate != Freshness.STALE; i++) {

            Dependency dependency = dependencies[i];

            // Make sure that the dependency updates the revalidation list
            // correctly.
            Freshness freshness = internal.checkFreshness(dependency);
            aggregate = aggregate.combine(freshness);
        }

        return aggregate;
    }

    public Freshness revalidate(DependencyContext context) {

        Freshness aggregate = Freshness.FRESH;
        for (int i = 0; i < dependencies.length &&
                aggregate != Freshness.STALE; i++) {

            Dependency dependency = dependencies[i];
            Freshness freshness = dependency.revalidate(context);
            aggregate = aggregate.combine(freshness);
        }

        return aggregate;
    }
}
