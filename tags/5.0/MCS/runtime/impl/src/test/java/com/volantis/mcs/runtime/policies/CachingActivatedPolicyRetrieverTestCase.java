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

package com.volantis.mcs.runtime.policies;

import com.volantis.cache.provider.CacheableObjectProviderMock;
import com.volantis.impl.mcs.runtime.policies.CachingActivatedPolicyRetriever;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraints;
import com.volantis.mcs.runtime.policies.cache.PolicyCache;
import com.volantis.mcs.runtime.policies.cache.PolicyCacheBuilder;
import com.volantis.mcs.runtime.policies.cache.PolicyCachePartitionConstraints;
import com.volantis.mcs.runtime.policies.cache.PolicyCachePartitionConstraintsImpl;
import com.volantis.mcs.runtime.policies.cache.ProjectSpecificKey;
import com.volantis.shared.system.SystemClockMock;
import com.volantis.shared.time.Time;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link CachingActivatedPolicyRetriever}.
 */
public class CachingActivatedPolicyRetrieverTestCase
        extends TestCaseAbstract {

    private final String projectRelativeName = "/foo.foo";
    private String remoteBaseWithoutSlash = "http://host:8080/project";
    private final String remoteName = remoteBaseWithoutSlash +
            projectRelativeName;
    private PolicyFactory policyFactory;
    private SystemClockMock clockMock;
    private PolicyCache policyCache;
    private CacheControlConstraints constraints;
    private ActivatedPolicyRetrieverMock retrieverMock;
    private RuntimeProjectMock projectMock;
    private CacheableObjectProviderMock providerMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        clockMock = new SystemClockMock("clockMock", expectations);
        policyFactory = PolicyFactory.getDefaultInstance();

        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(0));
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1));
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2));
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(3));
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(4));
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(5));

        PolicyCacheBuilder builder = new PolicyCacheBuilder(100, 1000,
                clockMock);

        RemotePolicyCacheConfiguration configuration =
                new RemotePolicyCacheConfiguration();
        configuration.setMaxTimeToLive(new Integer(200));
        configuration.setDefaultRetryInterval(new Integer(2));

        constraints = new CacheControlConstraints(configuration);

        PolicyCachePartitionConstraints partitionConstraints =
                new PolicyCachePartitionConstraintsImpl(50, 60, constraints);

        builder.setLocalPartitionConstraints(partitionConstraints);
        builder.setRemotePartitionConstraints(partitionConstraints);
        builder.addDefaultLocalPolicySpecificGroup(PolicyType.AUDIO, 5);

        policyCache = builder.getPolicyCache();

        retrieverMock = new ActivatedPolicyRetrieverMock("retrieverMock",
                expectations);

        projectMock = new RuntimeProjectMock("projectMock", expectations);

        providerMock = new CacheableObjectProviderMock(
                "providerMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.getCacheGroup()
                .returns(null).any();
    }

    /**
     * Ensure that after retrieving a local policy it is cached.
     */
    public void testRetrieve() throws Exception {

        VariablePolicyBuilder builder = createOriginalPolicyBuilder(projectRelativeName);

        ActivatedPolicy policy = new TestActivatedPolicy(builder.getPolicy(), projectMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.isRemote().returns(false).any();

        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(policy);

        // Don't care about the current time.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1000))
                .any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ActivatedPolicyRetriever retriever =
                new CachingActivatedPolicyRetriever(policyCache, retrieverMock);
        ActivatedPolicy actualPolicy;

        // Get the policy, this should invoke the underlying retriever.
        actualPolicy = retriever.retrievePolicy(projectMock, projectRelativeName);
        assertSame(policy, actualPolicy);

        // Get the policy, this should get the policy from the cache.
        actualPolicy = retriever.retrievePolicy(projectMock, projectRelativeName);
        assertSame(policy, actualPolicy);

        // Make sure that the object was stored in the cache.
        Object object = policyCache.retrieve(
                new ProjectSpecificKey(projectMock, projectRelativeName),
                providerMock);
        assertSame(policy, object);
    }

    /**
     * Ensure that after retrieving a remote policy it is cached.
     */
    public void testRetrieveRemote() throws Exception {

        VariablePolicyBuilder builder = createOriginalPolicyBuilder(remoteName);

        ActivatedPolicy policy = new TestActivatedPolicy(builder.getPolicy(),
                projectMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.isRemote().returns(true).any();
        projectMock.expects.getContainsOrphans().returns(false).any();

        projectMock.expects.makeAbsolutePolicyURL(projectRelativeName)
                .returns(remoteName).any();

        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(policy);

        // Don't care about the current time.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1000)).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ActivatedPolicyRetriever retriever =
                new CachingActivatedPolicyRetriever(policyCache, retrieverMock);
        ActivatedPolicy actualPolicy;

        // Get the policy, this should invoke the underlying retriever.
        actualPolicy = retriever.retrievePolicy(projectMock, projectRelativeName);
        assertSame(policy, actualPolicy);

        // Get the policy, this should get the policy from the cache.
        actualPolicy = retriever.retrievePolicy(projectMock, projectRelativeName);
        assertSame(policy, actualPolicy);

        // Make sure that the object was stored in the cache.
        Object object = policyCache.retrieve(remoteName, providerMock);
        assertSame(policy, object);
    }

    /**
     * Ensure that after failing to retrieve a policy that it remembers that it
     * could not find it.
     */
    public void testRetrieveFailure() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectMock.expects.isRemote().returns(false).any();
        projectMock.expects.getCacheControlDefaults().returns(null);

        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(null);

        // Don't care about the current time.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1000)).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ActivatedPolicyRetriever retriever =
                new CachingActivatedPolicyRetriever(policyCache, retrieverMock);
        ActivatedPolicy policy;

        // Get the policy, this should invoke the underlying retriever.
        policy = retriever.retrievePolicy(projectMock, projectRelativeName);
        assertNull(policy);

        // Get the policy, this should get the policy from the cache.
        policy = retriever.retrievePolicy(projectMock, projectRelativeName);
        assertNull(policy);
    }

    /**
     * Ensure that the caching retriever works properly.
     *
     * <ol>
     * <li>Requests policy.</li>
     * <li>Requests policy again.</li>
     * <li>Requests policy after it has expired, retry succeeds.</li>
     * <li>Requests policy after it has expired and retry fails.</li>
     * <li>Requests policy before retry interval passed.</li>
     * <li>Requests policy after retry interval passed and retry succeeds.</li>
     * </ol>
     */
    public void testAccessor()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project is assumed to be remote.
        projectMock.expects.isRemote().returns(true).any();
        projectMock.expects.getContainsOrphans().returns(false).any();

        projectMock.expects.makeAbsolutePolicyURL(projectRelativeName)
                .returns(remoteName).any();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        VariablePolicyBuilder builder = createOriginalPolicyBuilder(remoteName);

        ActivatedPolicy originalPolicy =
                new TestActivatedPolicy(builder.getPolicy(), projectMock);

        CacheControlBuilder cacheControlBuilder =
                builder.getCacheControlBuilder();
        cacheControlBuilder.setTimeToLive(2);
        cacheControlBuilder.setRetryMaxCount(1);

        ActivatedPolicy refreshedPolicy =
                new TestActivatedPolicy(builder.getPolicy(), projectMock);;
        assertNotEquals(originalPolicy, refreshedPolicy);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ActivatedPolicyRetriever retriever =
                new CachingActivatedPolicyRetriever(policyCache, retrieverMock);
        Policy actualPolicy;

        // ---------------------------------------------------------------------
        //   First Request
        // ---------------------------------------------------------------------

        // First request fails to find the policy in the cache so retrieves
        // it and stores it in the cache.

        // The policy returned by the retriever underlying the provider.
        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(originalPolicy);

        // The current time for the calculation of the expiration time.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1000));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertSame(originalPolicy, actualPolicy);

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Second Request
        // ---------------------------------------------------------------------

        // Second request finds the policy in the cache and it has not yet
        // expired so is returned without invoking the provider.

        // The current time used to determine whether the policy has expired.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1500));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertSame(originalPolicy, actualPolicy);

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Third Request
        // ---------------------------------------------------------------------

        // Third request finds the policy in the cache but it has expired so
        // the provider is invoked.

        // The current time used to determine whether the policy has expired.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2500));

        // The new policy returned by the retriever underlying the provider.
        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(refreshedPolicy);

        // The current time used to calculate the expiration time of the newly
        // retrieved policy. This is different to the above as it will
        // happen after the provider has returned the policy which will be
        // some time afterwards.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(3000));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertSame(refreshedPolicy, actualPolicy);

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Fourth Request
        // ---------------------------------------------------------------------

        // Fourth request occurs after the policy has expired and retry is
        // due but fails.

        // Time is after expiry time.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(6500));

        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(null);

        projectMock.expects.getCacheControlDefaults()
                .returns(constraints.getDefaultCacheControl());

        // Get the time after the retriever has failed to return a policy so
        // that it can calculate the expiry time for the retry.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(8000));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertSame(refreshedPolicy, actualPolicy);

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Fifth Request
        // ---------------------------------------------------------------------

        // Fifth request occurs before the retry period is due so should not
        // invoke the retriever.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(8999));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertSame(refreshedPolicy, actualPolicy);

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Sixth Request
        // ---------------------------------------------------------------------

        // Sixth request occurs after the retry period has passed and so should
        // invoke the retriever.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(11000));

        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(originalPolicy);

        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(12000));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertSame(originalPolicy, actualPolicy);
    }

    private VariablePolicyBuilder createOriginalPolicyBuilder(
            String policyName) {
        VariablePolicyBuilder builder =
                policyFactory.createVariablePolicyBuilder(PolicyType.AUDIO);
        builder.setName(policyName);

        CacheControlBuilder cacheControlBuilder =
                policyFactory.createCacheControlBuilder();
        cacheControlBuilder.setCacheThisPolicy(true);
        cacheControlBuilder.setTimeToLive(1);
        cacheControlBuilder.setRetryFailedRetrieval(true);
        cacheControlBuilder.setRetryInterval(1);
        cacheControlBuilder.setRetryMaxCount(2);
        builder.setCacheControlBuilder(cacheControlBuilder);
        constraints.applyConstraints(cacheControlBuilder);
        return builder;
    }

    /**
     * Ensure that the remote repository accessor works properly when the
     * object cannot be found on the first request.
     *
     * <ol>
     * <li>First request fails so unavailable component added to cache and
     * returns null.</li>
     * <li>Second request before retry interval has passed so returns
     * null.</li>
     * <li>Third request occurs after retry interval has passed so it is
     * retried which succeeds and returns the real policy.</li>
     * </ol>
     */
    public void testPolicyNotFound()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        VariablePolicyBuilder builder = createOriginalPolicyBuilder(remoteName);

        ActivatedPolicy policy = new TestActivatedPolicy(
                builder.getPolicy(), projectMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project is assumed to be remote.
        projectMock.expects.isRemote().returns(true).any();
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.getCacheControlDefaults().returns(null).any();

        projectMock.expects.makeAbsolutePolicyURL(projectRelativeName)
                .returns(remoteName).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ActivatedPolicyRetriever retriever =
                new CachingActivatedPolicyRetriever(policyCache, retrieverMock);

        Policy actualPolicy;

        // ---------------------------------------------------------------------
        //   First Request
        // ---------------------------------------------------------------------

        // First request fails to find the policy in the cache and cannot
        // retrieve one either.

        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(null);

        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1000));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertNull(actualPolicy);

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Second Request
        // ---------------------------------------------------------------------

        // Second request occurs before the policy has expired.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1500));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertNull(actualPolicy);

        expectations.verify();

        // ---------------------------------------------------------------------
        //   Third Request
        // ---------------------------------------------------------------------

        // Third request occurs after the policy has expired and a retry is
        // due and succeeds.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(4000));

        retrieverMock.expects
                .retrievePolicy(projectMock, projectRelativeName)
                .returns(policy);

        // Get the current time for calculating the expiration time.
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(5000));

        actualPolicy = retriever.retrievePolicy(projectMock,
                projectRelativeName);
        assertSame(policy, actualPolicy);
    }
}
