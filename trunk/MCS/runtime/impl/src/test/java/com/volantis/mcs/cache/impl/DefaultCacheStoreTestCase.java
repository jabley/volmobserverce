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


package com.volantis.mcs.cache.impl;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.mcs.cache.CacheStore;

import com.volantis.mcs.cache.CacheTimeMock;

import com.volantis.mcs.cache.CacheEntry;
import com.volantis.mcs.cache.CacheIdentity;
import com.volantis.mcs.cache.CacheTime;
import com.volantis.mcs.cache.css.CSSCacheEntry;
import com.volantis.mcs.runtime.css.WritableCSSEntityMock;

/**
 * This test case tests the functionality of {@link DefaultCacheStore}
 */

public class DefaultCacheStoreTestCase extends TestCaseAbstract {

    private ExpectationBuilder expectations;

    public void setUp() {
        expectations = mockFactory.createUnorderedBuilder();
    }

    /**
     * This test tests that cache entries are flushed after the correct amount
     * of time.
     * <p>The test ensures that entries are only deleted at the appropriate
     * millisecond boundary.</p>
     *
     * @throws Exception
     */
    public void testCacheExpiration()
            throws Exception {


        CacheTimeMock cacheTime = new CacheTimeMock("CacheTime", expectations);

        WritableCSSEntityMock writableCSSEntityMock1 =
                new WritableCSSEntityMock("WritableCSSEntityMock1", expectations);

        WritableCSSEntityMock writableCSSEntityMock2 =
                new WritableCSSEntityMock("WritableCSSEntityMock2", expectations);

        cacheTime.expects.getTimeInMillis().returns(0);
        cacheTime.expects.getTimeInMillis().returns(10);
        cacheTime.expects.getTimeInMillis().returns(20);
        cacheTime.expects.getTimeInMillis().returns(25);
        cacheTime.expects.getTimeInMillis().returns(30);
        cacheTime.expects.getTimeInMillis().returns(99);
        cacheTime.expects.getTimeInMillis().returns(100);
        cacheTime.expects.getTimeInMillis().returns(100);
        cacheTime.expects.getTimeInMillis().returns(100);
        cacheTime.expects.getTimeInMillis().returns(100);
        cacheTime.expects.getTimeInMillis().returns(110);
        cacheTime.expects.getTimeInMillis().returns(200);
        cacheTime.expects.getTimeInMillis().returns(210);
        cacheTime.expects.getTimeInMillis().returns(220);
        cacheTime.expects.getTimeInMillis().returns(Integer.MAX_VALUE);

        // Create a new DefaultCacheStore with a TTL of 100
        CacheStore defaultCacheStore = new DefaultCacheStore(cacheTime,100);

        CacheEntry entry1 = new CSSCacheEntry(writableCSSEntityMock1);
        CacheEntry entry2 = new CSSCacheEntry(writableCSSEntityMock2, 50);
        CacheEntry entry3 = new CSSCacheEntry(writableCSSEntityMock2, 150);

        // We add the first identity which should get a cacheTime of 0,
        // expiresTime of 100 and a sequence number of 0
        CacheIdentity identity1 = defaultCacheStore.store(entry1);
        assertEquals("T1", identity1.getCreateTime(),0);
        assertEquals("T2", identity1.getExpiresTime(),100);
        assertEquals("T3", identity1.getSequenceNo(),0);

        // We add the second identity which should get a cacheTime of 10,
        // expiresTime of max(10+50,10+100)=110 and a sequence number of 1
        CacheIdentity identity2 = defaultCacheStore.store(entry2);
        assertEquals("T4", identity2.getCreateTime(),10);
        assertEquals("T5", identity2.getExpiresTime(),110);
        assertEquals("T6", identity2.getSequenceNo(),1);

        // We add the third identity which should get a cacheTime of 20,
        // expiresTime of max(20+150,20+100)=170 and a sequence number of 2
        CacheIdentity identity3 = defaultCacheStore.store(entry3);
        assertEquals("T7", identity3.getCreateTime(),20);
        assertEquals("T8", identity3.getExpiresTime(),170);
        assertEquals("T9", identity3.getSequenceNo(),2);

        // Retrieve expired identities that are as old or older
        // than 25 - TTL (should be none).
        assertNull("T10", defaultCacheStore.retrieveOldestExpiredIdentity());

        // Remove entries that are as old or older
        // than 30 - TTL (Should be none).
        defaultCacheStore.removeExpiredIdentities();

        // Retrieve expired identities that are as old or older
        // than 99 - TTL (should be none).
        assertNull("T11", defaultCacheStore.retrieveOldestExpiredIdentity());

        // Retrieve expired identities that are as old or older
        // than 100 - TTL (should be identity1).
        assertEquals("T12", defaultCacheStore.retrieveOldestExpiredIdentity(),
                identity1);

        // Remove entries that are as old or older
        // than 100 - TTL (Should be identity1). Note we will call cacheTime
        // two times to look up to first non-expires entry.
        defaultCacheStore.removeExpiredIdentities();

        // Retrieve expired identities that are as old or older
        // than 100 - TTL (should be none as we deleted identity1).
        assertNull("T13", defaultCacheStore.retrieveOldestExpiredIdentity());

        // Retrieve expired identities that are as old or older
        // than 110 - TTL (should be identity2).
        assertEquals("T14", defaultCacheStore.retrieveOldestExpiredIdentity(),
                identity2);

        // Retrieve expired identities that are as old or older
        // than 200 - TTL (should be still identity2).
        assertEquals("T15", defaultCacheStore.retrieveOldestExpiredIdentity(),
                identity2);

        // Remove entries that are as old or older
        // than 210 - TTL (Should be identity1). Note we will call cacheTime
        // one time to look up to first non-expires entry.
        defaultCacheStore.removeExpiredIdentities();

        // Retrieve expired identities that are as old or older
        // than 220 - TTL (should be none).
        assertEquals("T16", defaultCacheStore.retrieveOldestExpiredIdentity(),
                null);
        
        // Final test for expected number of getTimeInMillis() calls.
        assertEquals("T17", Integer.MAX_VALUE, cacheTime.getTimeInMillis());
    }

    /**
     * This test checks that a retrieved entity is the same as the one placed
     * in the cache originally.
     *
     * @throws Exception
     */
    public void testCacheRetrieval() throws Exception {

        CacheTime cacheTime = new CacheTimeImpl();

        WritableCSSEntityMock writableCSSEntityMock1 =
                new WritableCSSEntityMock("WritableCSSEntityMock1", expectations);

        WritableCSSEntityMock writableCSSEntityMock2 =
                new WritableCSSEntityMock("WritableCSSEntityMock2", expectations);


        // Create a new DefaultCacheStore with a TTL of 100
        CacheStore defaultCacheStore = new DefaultCacheStore(cacheTime,100);


        CacheEntry entry1 = new CSSCacheEntry(writableCSSEntityMock1);
        CacheEntry entry2 = new CSSCacheEntry(writableCSSEntityMock2, 40);

        CacheIdentity identity1 = defaultCacheStore.store(entry1);
        CacheIdentity identity2 = defaultCacheStore.store(entry2);



        assertEquals(entry1,defaultCacheStore.retrieve(identity1));
        assertEquals(entry2,defaultCacheStore.retrieve(identity2));

    }
    /**
     * This test checks that the cache removal functionality removes the
     * specified object after confirming that it has been stored.
     * 
     * @throws Exception
     */
    public void testCacheRemoval() throws Exception {
        CacheTime cacheTime = new CacheTimeImpl();

        WritableCSSEntityMock writableCSSEntityMock1 =
                new WritableCSSEntityMock("WritableCSSEntityMock1", expectations);

        // Create a new DefaultCacheStore with a TTL of 100
        CacheStore defaultCacheStore = new DefaultCacheStore(cacheTime,100);


        CacheEntry entry1 = new CSSCacheEntry(writableCSSEntityMock1);

        CacheIdentity identity1 = defaultCacheStore.store(entry1);

        assertEquals(entry1,defaultCacheStore.retrieve(identity1));

        defaultCacheStore.remove(identity1);
        assertNull(defaultCacheStore.retrieve(identity1));

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8616/3	ianw	VBM:2005060103 Fixed Javadoc as per review comments

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
