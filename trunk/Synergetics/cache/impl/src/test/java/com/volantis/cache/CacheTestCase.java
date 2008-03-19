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

package com.volantis.cache;

import com.volantis.cache.expiration.ExpirationCheckerMock;
import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupBuilder;
import com.volantis.cache.impl.InternalCache;
import com.volantis.cache.notification.RemovalListenerMock;
import com.volantis.cache.provider.CacheableObjectProviderMock;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.cache.stats.StatisticsSnapshot;
import com.volantis.shared.system.SystemClockMock;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.shared.time.Time;
import com.volantis.shared.time.Period;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Test cases for {@link Cache}.
 */
public class CacheTestCase
        extends CacheTestAbstract {

    private static final ExpectedValue EXPECTED_CACHE_ENTRY =
            mockFactory.expectsInstanceOf(CacheEntry.class);

    private CacheFactory factory;
    private CacheableObjectProviderMock providerMock;
    private final String key1 = "key1";
    private final String value1 = "value1";
    private final String key2 = "key2";
    private final String value2 = "value2";
    private final String key3 = "key3";
    private final String value3 = "value3";
    private RemovalListenerMock removalListenerMock;
    private SystemClockMock clockMock;
    private ExpirationCheckerMock expirationCheckerMock;

    protected void setUp() throws Exception {
        super.setUp();

        factory = CacheFactory.getDefaultInstance();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        providerMock = new CacheableObjectProviderMock("providerMock",
                expectations);

        removalListenerMock = new RemovalListenerMock(
                "removalListenerMock", expectations);

        expirationCheckerMock = new ExpirationCheckerMock(
                "expirationCheckerMock", expectations);

        clockMock = new SystemClockMock("clockMock", expectations);

        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(0));
    }


    /**
     * Ensure that retrieving an object works.
     */
    public void testRetrieve() throws Exception {

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setClock(clockMock);
        builder.setObjectProvider(providerMock);
        builder.setMaxCount(Integer.MAX_VALUE);
        InternalCache cache = (InternalCache) builder.buildCache();
        Group root = cache.getRootGroup();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        addProviderExpectation(key1, null,
                new ProviderResult(value1, root, true, null));

        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(1010));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Object object;

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        object = cache.retrieve(key1);
        assertSame(value1, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        object = cache.retrieve(key1);
        assertSame(value1, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        StatisticsSnapshot snapshot = root.getStatisticsSnapshot();
        assertEquals(1, snapshot.getEntryCount());
        assertEquals(Time.inMilliSeconds(1010), snapshot.getGatherTime());
        assertEquals(Period.inMilliSeconds(1010), snapshot.getPeriod());
        assertEquals(1, snapshot.getHitCount());
        assertEquals(1, snapshot.getMissedAddedCount());
    }

    private void addProviderExpectation(
            final String expectedKey, final Object expectedValue,
            final ProviderResult result) {

        providerMock.fuzzy
                .retrieve(clockMock, expectedKey, EXPECTED_CACHE_ENTRY)
                .does(new CheckProviderReturn(expectedKey, expectedValue,
                        result));
    }

    private void addUncacheableProviderExpectation(
            final String expectedKey, final ProviderResult result) {

        providerMock.expects
                .retrieve(clockMock, expectedKey, null)
                .does(new CheckProviderReturn(result));
    }

    private void addProviderExpectation(
            final String expectedKey, final Object expectedValue,
            final Throwable throwable) {

        providerMock.fuzzy
                .retrieve(clockMock, expectedKey, EXPECTED_CACHE_ENTRY)
                .does(new CheckProviderThrow(expectedKey, expectedValue,
                        throwable));
    }

    private void addUncacheableProviderExpectation(
            final String expectedKey, final Throwable throwable) {

        providerMock.expects
                .retrieve(clockMock, expectedKey, null)
                .does(new CheckProviderThrow(throwable));
    }

    /**
     * Ensure that retrieving an object with an explicit provider works.
     */
    public void testRetrieveWithProvider() throws Exception {

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setClock(clockMock);
        builder.setMaxCount(Integer.MAX_VALUE);
        InternalCache cache = (InternalCache) builder.buildCache();
        Group root = cache.getRootGroup();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        addProviderExpectation(key1, null,
                new ProviderResult(value1, root, true, null));

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Object object;

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        object = cache.retrieve(key1, providerMock);
        assertSame(value1, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        object = cache.retrieve(key1, providerMock);
        assertSame(value1, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(100));

        StatisticsSnapshot snapshot = root.getStatisticsSnapshot();
        assertEquals(1, snapshot.getEntryCount());
        assertEquals(1, snapshot.getHitCount());
        assertEquals(1, snapshot.getMissedAddedCount());
    }

    /**
     * Ensure that when the cache exceeds the maximum count that one of the
     * items is removed.
     */
    public void testMaxCount() throws Exception {

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setClock(clockMock);
        builder.setMaxCount(2);
        builder.setObjectProvider(providerMock);
        InternalCache cache = (InternalCache) builder.buildCache();
        final Group root = cache.getRootGroup();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                addProviderExpectation(key1, null,
                        new ProviderResult(value1, root, true, null));
                addProviderExpectation(key2, null,
                        new ProviderResult(value2, root, true, null));

                addProviderExpectation(key3, null,
                        new ProviderResult(value3, root, true, null));
                removalListenerMock.fuzzy
                        .entryRemoved(EXPECTED_CACHE_ENTRY)
                        .does(new ExpectCacheEntry(key1, value1));

                addProviderExpectation(key1, null,
                        new ProviderResult(value1, root, true, null));
                removalListenerMock.fuzzy
                        .entryRemoved(EXPECTED_CACHE_ENTRY)
                        .does(new ExpectCacheEntry(key3, value3));
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        root.addRemovalListener(removalListenerMock);

        Object object;

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        object = cache.retrieve(key1);
        assertSame(value1, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        object = cache.retrieve(key2);
        assertSame(value2, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // At this point cache contains (key2, key1).

        // Retrieving this should discard the entry for key1.
        object = cache.retrieve(key3);
        assertSame(value3, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // At this point cache contains (key3, key2).

        object = cache.retrieve(key2);
        assertSame(value2, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // At this point cache contains (key2, key3).

        // Retrieving this should discard the entry for key3.
        object = cache.retrieve(key1);
        assertSame(value1, object);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // At this point cache contains (key1, key2).
        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2000));
        StatisticsSnapshot snapshot = root.getStatisticsSnapshot();
        assertEquals(2, snapshot.getEntryCount());
        assertEquals(1, snapshot.getHitCount());
        assertEquals(4, snapshot.getMissedAddedCount());
    }

    /**
     * Ensure that entries will expire properly.
     */
    public void testExpiration() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setClock(clockMock);
        builder.setExpirationChecker(expirationCheckerMock);
        builder.setMaxCount(2);
        builder.setObjectProvider(providerMock);
        InternalCache cache = (InternalCache) builder.buildCache();

        GroupBuilder groupBuilder = factory.createGroupBuilder();
        groupBuilder.setMaxCount(2);

        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds(2000));

        final Group root = cache.getRootGroup();
        final Group other = root.addGroup("group1", groupBuilder);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                addProviderExpectation(key1, null,
                        new ProviderResult(value1, root, true, null));

                // First time expiration checker reports that entry has not
                // expired.
                expirationCheckerMock.fuzzy
                        .hasExpired(clockMock, EXPECTED_CACHE_ENTRY)
                        .returns(false);

                // Second time expiration checker reports that entry has
                // expired.
                expirationCheckerMock.fuzzy
                        .hasExpired(clockMock, EXPECTED_CACHE_ENTRY)
                        .returns(true);

                // Second request retrieves different value but entry still
                // has old value in it. Different value is in different group.
                addProviderExpectation(key1, value1,
                        new ProviderResult(value2, other, true, null));
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        Object value;

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        value = cache.retrieve(key1);
        assertEquals(value1, value);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // Retrieve it again, this should invoke the expiration checker which
        // reports that it has not expired.
        value = cache.retrieve(key1);
        assertEquals(value1, value);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // Retrieve it again, this should invoke the expiration checker which
        // now reports that it has expired so the object will be retrieved
        // again and will return a different value.
        value = cache.retrieve(key1);
        assertEquals(value2, value);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);
    }

    /**
     * Ensure that if an error is thrown by a provider that the cache does
     * not block.
     */
    public void testProviderErrors() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setClock(clockMock);
        builder.setMaxCount(2);
        builder.setObjectProvider(providerMock);
        InternalCache cache = (InternalCache) builder.buildCache();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        IllegalStateException throwable = new IllegalStateException("expected");
        addProviderExpectation(key1, null, throwable);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // Retrieve the object, this will fail and should result in an
        // exception being thrown.
        try {
            cache.retrieve(key1);
            fail("Did not cause exception");
        } catch (IllegalStateException expected) {
            assertSame(throwable, expected);
        }

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // Retrieve it again, this should not invoke the provider again.
        try {
            cache.retrieve(key1);
            fail("Did not cause exception");
        } catch (ExtendedRuntimeException expected) {
            assertSame(throwable, expected.getCause());
        }

        // Check the cache integrity.
        ensureCacheIntegrity(cache);
    }

    /**
     * Ensure that if an error is thrown by a provider when it is updating an
     * entry that has already been added that the cache does not block.
     */
    public void testUpdateErrors() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setClock(clockMock);
        builder.setExpirationChecker(expirationCheckerMock);
        builder.setMaxCount(2);
        builder.setObjectProvider(providerMock);
        InternalCache cache = (InternalCache) builder.buildCache();
        final Group root = cache.getRootGroup();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        final IllegalStateException throwable =
                new IllegalStateException("expected");

        expectations.add(new OrderedExpectations() {
            public void add() {
                addProviderExpectation(key1, null,
                        new ProviderResult(value1, root, true, null));

                // Expiration check for second request reports that entry has
                // expired.
                expirationCheckerMock.fuzzy
                        .hasExpired(clockMock, EXPECTED_CACHE_ENTRY)
                        .returns(true);

                // Second provider request fails but the entry has the old
                // value.
                addProviderExpectation(key1, value1, throwable);
            }
        });


        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Object value;

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // Retrieve the object, this will work and should result in the entry
        // being added to the root group.
        value = cache.retrieve(key1);
        assertEquals(value1, value);

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // Retrieve it again, this should invoke the provider again as the
        // entry has expired. Although this fails it should not remove the
        // entry from the cache as it already belongs to a group.
        try {
            value = cache.retrieve(key1);
        } catch (IllegalStateException expected) {
            assertSame(throwable, expected);
        }

        // Check the cache integrity.
        ensureCacheIntegrity(cache);

        // Retrieve it again, this should not invoke the provider as the entry
        // is in the cache and has not expired but it should still error.
        try {
            value = cache.retrieve(key1);
        } catch (ExtendedRuntimeException expected) {
            assertSame(throwable, expected.getCause());
        }

        // Check the cache integrity.
        ensureCacheIntegrity(cache);
    }

    /**
     * Ensure that if the provider returns a null that the cache does not hang.
     */
    public void testProviderReturnedNull() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setClock(clockMock);
        builder.setMaxCount(2);
        builder.setObjectProvider(providerMock);
        InternalCache cache = (InternalCache) builder.buildCache();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        providerMock.fuzzy.retrieve(clockMock, key1, EXPECTED_CACHE_ENTRY)
                .returns(null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Retrieve the object, this will fail and should result in an
        // exception being thrown.
        try {
            cache.retrieve(key1);
            fail("Did not cause exception");
        } catch (IllegalStateException expected) {
            assertEquals("Result from provider is null", expected.getMessage());
        }
    }

    /**
     * Ensure that if the provider detects an error when retrieving the content
     * for an uncacheable entry that it reports it correctly.
     */
    public void testUncacheableError() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setClock(clockMock);
        builder.setMaxCount(2);
        builder.setObjectProvider(providerMock);
        InternalCache cache = (InternalCache) builder.buildCache();

        Group root = cache.getRootGroup();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Expect a request that initially assumes that the entry is cacheable
        // but returns a result that indicates that it is not.
        addProviderExpectation(key1, null,
                new ProviderResult(value1, root, false, null));

        // Retrieve the object, this will work and mark the entry as
        // uncacheable.
        Object value = cache.retrieve(key1);
        assertSame(value1, value);

        // Expect a request for an uncacheable entry that fails with an error.
        Throwable throwable = new Error();
        addUncacheableProviderExpectation(key1, throwable);

        // Retrieve the object, this will fail and should result in an
        // exception being thrown. However, it must not mark the entry as
        // in error because that would be equivalent to caching the result
        // and the entry is uncacheable.
        try {
            cache.retrieve(key1);
            fail("Did not cause exception");
        } catch (Error expected) {
            assertSame(throwable, expected);
        }

        // Expect a request for an uncacheable entry but return a result that
        // indicates that it is now cacheable.
        addUncacheableProviderExpectation(key1,
                new ProviderResult(value2, root, true, null));

        // Retrieve the object again, this will not fail this time and should
        // result in the object being returned.
        value = cache.retrieve(key1);
        assertSame(value2, value);

        // Retrieve the object again, this will use the cached version.
        value = cache.retrieve(key1);
        assertSame(value2, value);
    }
}
