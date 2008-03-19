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

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContextMock;
import com.volantis.shared.dependency.DependencyMock;
import com.volantis.shared.dependency.Freshness;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.impl.Dependencies;

import java.util.Arrays;

/**
 * Test cases for {@link com.volantis.xml.expression.impl.Dependencies}.
 */
public class DependenciesTestCase
        extends TestCaseAbstract {

    private DependencyMock dependency1Mock;
    private DependencyMock dependency2Mock;
    private DependencyContextMock contextMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        dependency1Mock = new DependencyMock("dependency1Mock",
                expectations);

        dependency2Mock = new DependencyMock("dependency2Mock",
                expectations);

        contextMock = new DependencyContextMock("contextMock", expectations);
    }

    /**
     * Ensure that the dependencies aggregates the freshness of the individual
     * dependency instances correctly.
     */
    public void testCheckFreshness() throws Exception {

        ensureAggregateFreshness(Freshness.FRESH, Freshness.FRESH,
                Freshness.FRESH);

        ensureAggregateFreshness(Freshness.FRESH, Freshness.REVALIDATE,
                Freshness.REVALIDATE);

        ensureAggregateFreshness(Freshness.FRESH, Freshness.STALE,
                Freshness.STALE);

        ensureAggregateFreshness(Freshness.REVALIDATE, Freshness.FRESH,
                Freshness.REVALIDATE);

        ensureAggregateFreshness(Freshness.REVALIDATE, Freshness.REVALIDATE,
                Freshness.REVALIDATE);

        ensureAggregateFreshness(Freshness.REVALIDATE, Freshness.STALE,
                Freshness.STALE);

        ensureAggregateFreshness(Freshness.STALE, Freshness.FRESH,
                Freshness.STALE);

        ensureAggregateFreshness(Freshness.STALE, Freshness.REVALIDATE,
                Freshness.STALE);

        ensureAggregateFreshness(Freshness.STALE, Freshness.STALE, 
                Freshness.STALE);
    }

    private void ensureAggregateFreshness(
            Freshness fresh1, Freshness fresh2, Freshness expected) {

        contextMock.expects.checkFreshness(dependency1Mock).returns(fresh1);

        // If the first freshness is stale then it is possible that the
        // aggregator does not have to check the second one.
        int min = fresh1 == Freshness.STALE ? 0 : 1;

        contextMock.expects.checkFreshness(dependency2Mock).returns(fresh2)
                .min(min).max(1);

        Dependency aggregate = new Dependencies(Arrays.asList(
                new Dependency[] {
                    dependency1Mock, dependency2Mock
                }));

        Freshness actual = aggregate.freshness(contextMock);
        assertEquals(expected, actual);
    }

}
