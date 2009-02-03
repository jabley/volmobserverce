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

package com.volantis.xml.pipeline.sax.impl.dependency;

import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.time.Period;

/**
 * A test dependency.
 */
class TestDependency implements Dependency {

    private final Cacheability cacheability;
    private final Period timeToLive;
    private final Freshness freshness;
    private final Freshness revalidated;

    public TestDependency(
            Cacheability cacheability, Period timeToLive, Freshness freshness,
            Freshness revalidated) {
        this.cacheability = cacheability;
        this.timeToLive = timeToLive;
        this.freshness = freshness;
        this.revalidated = revalidated;
    }

    public Cacheability getCacheability() {
        return cacheability;
    }

    public Period getTimeToLive() {
        return timeToLive;
    }

    public Freshness freshness(DependencyContext context) {
        return freshness;
    }

    public Freshness revalidate(DependencyContext context) {
        return revalidated;
    }
}
