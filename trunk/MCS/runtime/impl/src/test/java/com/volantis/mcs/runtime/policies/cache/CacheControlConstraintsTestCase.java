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

package com.volantis.mcs.runtime.policies.cache;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.CacheControl;

/**
 * Test cases for {@link CacheControlConstraints}.
 */
public class CacheControlConstraintsTestCase
        extends TestCaseAbstract {
    private PolicyFactory policyFactory;

    protected void setUp() throws Exception {
        super.setUp();

        policyFactory = PolicyFactory.getDefaultInstance();
    }

    /**
     * Ensure that the hard coded values are correct.
     */
    public void testHardCodedDefaults()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();
        checkCacheControl(cacheControl, true, true, true, 600, 5, 3600);
    }

    private void checkCacheControl(
            CacheControl cacheControl,
            final boolean expectedCacheThisPolicy,
            final boolean expectedRetainDuringRetry,
            final boolean expectedRetryFailedRetrieval,
            final int expectedRetryInterval,
            final int expectedRetryMaxCount,
            final int expectedTimeToLive) {

        assertEquals(expectedCacheThisPolicy, cacheControl.getCacheThisPolicy());
        assertEquals(expectedRetainDuringRetry, cacheControl.getRetainDuringRetry());
        assertEquals(expectedRetryFailedRetrieval,
                cacheControl.getRetryFailedRetrieval());
        assertEquals(expectedRetryInterval, cacheControl.getRetryInterval());
        assertEquals(expectedRetryMaxCount, cacheControl.getRetryMaxCount());
        assertEquals(expectedTimeToLive, cacheControl.getTimeToLive());
    }

    /**
     * Ensure that the hard coded limits are correct.
     */
    public void testHardCodedLimits()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setRetainDuringRetry(true);
        builder.setRetryFailedRetrieval(true);
        builder.setRetryInterval(-10);
        builder.setRetryMaxCount(100);
        builder.setTimeToLive(100000);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        checkCacheControl(cacheControl, true, true, true, 0, 10, 100000);
    }

    /**
     * Ensure that the configuration can disallow retrying failed retrievals
     * that have been defaulted.
     */
    public void testDisallowDefaultCacheThisPolicy()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setAllowCacheThisPolicy(Boolean.FALSE);
        configuration.setDefaultCacheThisPolicy(Boolean.TRUE);
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        checkCacheControl(cacheControl, false, false, false, 0, 0, 0);
    }

    /**
     * Ensure that the configuration can disallow retrying failed retrievals
     * that have been explicitly set.
     */
    public void testDisallowExplicitCacheThisPolicy()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setAllowCacheThisPolicy(Boolean.FALSE);
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setRetainDuringRetry(true);
        builder.setCacheThisPolicy(true);
        builder.setRetryInterval(-10);
        builder.setRetryMaxCount(100);
        builder.setTimeToLive(1000);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        checkCacheControl(cacheControl, false, false, false, 0, 0, 0);
    }

    /**
     * Ensure that the configuration can disallow retrying failed retrievals
     * that have been defaulted.
     */
    public void testDisallowDefaultRetryFailedRetrieval()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setAllowRetryFailedRetrieval(Boolean.FALSE);
        configuration.setDefaultRetryFailedRetrieval(Boolean.TRUE);
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        checkCacheControl(cacheControl, true, false, false, 0, 0, 3600);
    }

    /**
     * Ensure that the configuration can disallow retrying failed retrievals
     * that have been explicitly set.
     */
    public void testDisallowExplicitRetryFailedRetrieval()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setAllowRetryFailedRetrieval(Boolean.FALSE);
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setRetainDuringRetry(true);
        builder.setRetryFailedRetrieval(true);
        builder.setRetryInterval(-10);
        builder.setRetryMaxCount(100);
        builder.setTimeToLive(1000);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        checkCacheControl(cacheControl, true, false, false, 0, 0, 1000);
    }

    /**
     * Ensure that the configuration can disallow retain during retry
     * that have been defaulted.
     */
    public void testDefaultDisallowRetainDuringRetry()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setAllowRetainDuringRetry(Boolean.FALSE);
        configuration.setDefaultRetainDuringRetry(Boolean.TRUE);
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        checkCacheControl(cacheControl, true, false, true, 600, 5, 3600);
    }

    /**
     * Ensure that the configuration can disallow retain during retry
     * that has been explicitly set.
     */
    public void testExplicitDisallowRetainDuringRetry()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setAllowRetainDuringRetry(Boolean.FALSE);
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setRetainDuringRetry(true);
        builder.setRetryFailedRetrieval(true);
        builder.setRetryInterval(-10);
        builder.setRetryMaxCount(100);
        builder.setTimeToLive(1000);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        checkCacheControl(cacheControl, true, false, true, 0, 10, 1000);
    }

    /**
     * Ensure that the configuration can restrict the minimum retry interval
     * that has been defaulted.
     */
    public void testDefaultMinimumRetryInterval()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMinRetryInterval(new Integer(100));
        configuration.setDefaultRetryInterval(new Integer(50));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        assertEquals(100, cacheControl.getRetryInterval());
    }

    /**
     * Ensure that a valid retry interval that is defaulted is preserved.
     */
    public void testDefaultValidRetryInterval()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMinRetryInterval(new Integer(100));
        configuration.setDefaultRetryInterval(new Integer(150));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        assertEquals(150, cacheControl.getRetryInterval());
    }

    /**
     * Ensure that the configuration can restrict the minimum retry interval
     * that has been explicitly set.
     */
    public void testExplicitMinimumRetryInterval()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMinRetryInterval(new Integer(100));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setRetryInterval(50);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        assertEquals(100, cacheControl.getRetryInterval());
    }

    /**
     * Ensure that a valid retry interval that is explicitly set is preserved.
     */
    public void testExplicitValidRetryInterval()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMinRetryInterval(new Integer(100));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setRetryInterval(150);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        assertEquals(150, cacheControl.getRetryInterval());
    }

    /**
     * Ensure that the configuration can restrict the maximum retry max count
     * that has been defaulted.
     */
    public void testDefaultMaximumRetryMaxCount()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxRetryMaxCount(new Integer(100));
        configuration.setDefaultRetryMaxCount(new Integer(150));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        assertEquals(100, cacheControl.getRetryMaxCount());
    }

    /**
     * Ensure that a valid retry max count that is defaulted is preserved.
     */
    public void testDefaultValidRetryMaxCount()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxRetryMaxCount(new Integer(100));
        configuration.setDefaultRetryMaxCount(new Integer(50));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        assertEquals(50, cacheControl.getRetryMaxCount());
    }

    /**
     * Ensure that the configuration can restrict the maximum retry max count
     * that has been explicitly set.
     */
    public void testExplicitMaximumRetryMaxCount()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxRetryMaxCount(new Integer(100));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setRetryMaxCount(150);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        assertEquals(100, cacheControl.getRetryMaxCount());
    }

    /**
     * Ensure that a valid retry max count that is explicitly set is preserved.
     */
    public void testExplicitValidRetryMaxCount()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxRetryMaxCount(new Integer(100));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setRetryMaxCount(50);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        assertEquals(50, cacheControl.getRetryMaxCount());
    }

    /**
     * Ensure that the configuration can restrict the maximum time to live
     * that has been defaulted.
     */
    public void testDefaultMaximumTimeToLive()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxTimeToLive(new Integer(100));
        configuration.setDefaultTimeToLive(new Integer(150));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        assertEquals(100, cacheControl.getTimeToLive());
    }

    /**
     * Ensure that a valid time to live that is defaulted is preserved.
     */
    public void testDefaultValidTimeToLive()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxTimeToLive(new Integer(100));
        configuration.setDefaultTimeToLive(new Integer(50));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControl cacheControl = constraints.getDefaultCacheControl();

        assertEquals(50, cacheControl.getTimeToLive());
    }

    /**
     * Ensure that the configuration can restrict the maximum time to live
     * that has been explicitly set.
     */
    public void testExplicitMaximumTimeToLive()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxTimeToLive(new Integer(100));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setTimeToLive(150);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        assertEquals(100, cacheControl.getTimeToLive());
    }

    /**
     * Ensure that a valid time to live that is explicitly set is preserved.
     */
    public void testExplicitValidTimeToLive()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxTimeToLive(new Integer(100));
        CacheControlConstraints constraints = new CacheControlConstraints(configuration);
        CacheControlBuilder builder = policyFactory.createCacheControlBuilder();
        builder.setTimeToLive(50);
        constraints.applyConstraints(builder);

        CacheControl cacheControl = builder.getCacheControl();
        assertEquals(50, cacheControl.getTimeToLive());
    }
}
