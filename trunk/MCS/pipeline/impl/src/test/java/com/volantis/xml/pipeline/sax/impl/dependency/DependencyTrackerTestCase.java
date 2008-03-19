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
import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.impl.DependencyTrackerImpl;
import com.volantis.xml.expression.impl.DependencyTracker;

/**
 * Test cases for {@link DependencyTracker}.
 */
public class DependencyTrackerTestCase
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
     * Ensure that if no dependencies are added that the tracker still
     * returns a default dependency.
     */
    public void testEmpty() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DependencyTracker tracker =
                new DependencyTrackerImpl();
        Dependency dependency = tracker.extractDependency();
        assertEquals(Cacheability.CACHEABLE, dependency.getCacheability());
        assertEquals(Period.INDEFINITELY, dependency.getTimeToLive());
        TestCaseAbstract.assertEquals(Freshness.FRESH, dependency.freshness(contextMock));
        try {
            dependency.revalidate(contextMock);
            fail("Did not detect invalid call to revalidate");
        } catch(IllegalStateException e) {
            // Expected.
        }

        // Add the dependency from the empty tracker to another, it should
        // be ignored by this tracker so it should behave just as if it only
        // had a single dependency added.
        tracker = new DependencyTrackerImpl();
        tracker.addDependency(dependency);
        tracker.addDependency(dependency1Mock);

        dependency = tracker.extractDependency();
        assertSame(dependency1Mock, dependency);
    }

    /**
     * Ensure that if only a single dependency is added to the
     * aggregator that it is returned.
     */
    public void testSingleDependency() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DependencyTracker tracker =
                new DependencyTrackerImpl();
        tracker.addDependency(dependency1Mock);
        Dependency aggregate = tracker.extractDependency();
        assertSame(dependency1Mock, aggregate);
    }

    /**
     * Ensure that dependencies added to the aggregator are
     * present in the extracted dependency.
     */
    public void testExtract() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        dependency1Mock.expects.getCacheability()
                .returns(Cacheability.CACHEABLE).optional();
        dependency1Mock.expects.getTimeToLive().returns(Period.inSeconds(100));
        dependency1Mock.expects.revalidate(contextMock)
                .returns(Freshness.FRESH);

        dependency2Mock.expects.getCacheability()
                .returns(Cacheability.UNCACHEABLE);
        dependency2Mock.expects.getTimeToLive().returns(Period.inSeconds(150));
        dependency2Mock.expects.revalidate(contextMock)
                .returns(Freshness.FRESH);

        contextMock.expects.checkFreshness(dependency1Mock)
                .returns(Freshness.FRESH);
        contextMock.expects.checkFreshness(dependency2Mock)
                .returns(Freshness.REVALIDATE);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        DependencyTracker tracker =
                new DependencyTrackerImpl();
        tracker.addDependency(dependency1Mock);
        tracker.addDependency(dependency2Mock);

        Dependency aggregate = tracker.extractDependency();

        Cacheability cacheability = aggregate.getCacheability();
        assertEquals(Cacheability.UNCACHEABLE, cacheability);

        Period timeToLive = aggregate.getTimeToLive();
        assertEquals(Period.inSeconds(100), timeToLive);

        Freshness freshness = aggregate.freshness(contextMock);
        assertEquals(Freshness.REVALIDATE, freshness);

        Freshness revalidate = aggregate.revalidate(contextMock);
        assertEquals(Freshness.FRESH, revalidate);
    }
}
