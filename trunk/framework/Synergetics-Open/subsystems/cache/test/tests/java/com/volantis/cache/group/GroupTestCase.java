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

package com.volantis.cache.group;

import com.volantis.cache.Cache;
import com.volantis.cache.CacheBuilder;
import com.volantis.cache.CacheEntry;
import com.volantis.cache.ExpectCacheEntry;
import com.volantis.cache.impl.CacheBuilderImpl;
import com.volantis.cache.impl.InternalCacheMock;
import com.volantis.cache.impl.group.GroupBuilderImpl;
import com.volantis.cache.impl.group.GroupImpl;
import com.volantis.cache.impl.group.InternalGroupBuilderMock;
import com.volantis.cache.impl.group.InternalGroupMock;
import com.volantis.cache.notification.RemovalListenerMock;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.shared.system.SystemClock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Test cases for {@link Group}.
 */
public class GroupTestCase
        extends TestCaseAbstract {
    private SystemClock clock = SystemClock.getDefaultInstance();

    /**
     * Ensure that adding a group works correctly.
     */
    public void testAddGroup() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final InternalCacheMock cacheMock =
                new InternalCacheMock("cacheMock", expectations);

        final InternalGroupBuilderMock builderMock =
                new InternalGroupBuilderMock("builderMock",
                        expectations);

        final InternalGroupMock group1Mock =
                new InternalGroupMock("group1Mock", expectations);

        final InternalGroupMock group2Mock =
                new InternalGroupMock("group2Mock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        GroupImpl group = new GroupImpl(cacheMock, null, 10, "", clock);

        builderMock.expects.buildGroup(cacheMock, group, "group1", clock)
                .returns(group1Mock);
        builderMock.expects.buildGroup(cacheMock, group, "group2", clock)
                .returns(group2Mock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Group group1 = group.addGroup("group1", builderMock);
        assertSame(group1, group1Mock);

        Group group2 = group.addGroup("group2", builderMock);
        assertSame(group2, group2Mock);

        try {
            group1 = group.addGroup("group1", builderMock);
            fail("Did not detect attempt to add same group multiple times");
        } catch (IllegalStateException expected) {
            assertEquals("Group 'group1' already exists",
                    expected.getMessage());
        }
    }

    /**
     * Ensure that getting a group works correctly.
     */
    public void testGetGroup() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final InternalCacheMock cacheMock =
                new InternalCacheMock("cacheMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        GroupImpl group = new GroupImpl(cacheMock, null, 10, "",
                clock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        try {
            group.getGroup("group1");
            fail("Did not detect attempt to add same group multiple times");
        } catch (IllegalStateException expected) {
            assertEquals("Group 'group1' does not exist",
                    expected.getMessage());
        }
    }

    public void test() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        final RemovalListenerMock listenerRootMock =
                new RemovalListenerMock("listenerRootMock", expectations);
        final RemovalListenerMock listener1Mock =
                new RemovalListenerMock("listener1Mock", expectations);
        final RemovalListenerMock listener2Mock =
                new RemovalListenerMock("listener2Mock", expectations);
        final RemovalListenerMock listener3Mock =
                new RemovalListenerMock("listener3Mock", expectations);
        final RemovalListenerMock listener4Mock =
                new RemovalListenerMock("listener4Mock", expectations);

        ExpectCacheEntry removeGroup2Key1 = new ExpectCacheEntry(
                "group2.key1", "value for (group2.key1)");
        ExpectedValue expectsCacheEntry =
                mockFactory.expectsInstanceOf(CacheEntry.class);
        listener2Mock.fuzzy
                .entryRemoved(expectsCacheEntry)
                .does(removeGroup2Key1);
        listenerRootMock.fuzzy
                .entryRemoved(expectsCacheEntry)
                .does(removeGroup2Key1);

        ExpectCacheEntry removeGroup2Key3 = new ExpectCacheEntry(
                "group2.key3", "value for (group2.key3)");
        listener2Mock.fuzzy
                .entryRemoved(expectsCacheEntry)
                .does(removeGroup2Key3);
        listenerRootMock.fuzzy
                .entryRemoved(expectsCacheEntry)
                .does(removeGroup2Key3);

        ExpectCacheEntry removeGroup3Key1 = new ExpectCacheEntry(
                "group3.key1", "value for (group3.key1)");
        listener3Mock.fuzzy
                .entryRemoved(expectsCacheEntry)
                .does(removeGroup3Key1);
        listenerRootMock.fuzzy
                .entryRemoved(expectsCacheEntry)
                .does(removeGroup3Key1);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CacheableObjectProvider objectProvider = new TestCacheableObjectProvider();

        CacheBuilder builder = new CacheBuilderImpl();
        builder.setObjectProvider(objectProvider);
        builder.setMaxCount(8);
        Cache cache = builder.buildCache();

        GroupBuilder groupBuilder = new GroupBuilderImpl();
        groupBuilder.setMaxCount(4);
        Group root = cache.getRootGroup();
        root.addRemovalListener(listenerRootMock);
        Group group1 = root.addGroup("group1", groupBuilder);
        group1.addRemovalListener(listener1Mock);
        Group group2 = root.addGroup("group2", groupBuilder);
        group2.addRemovalListener(listener2Mock);
        Group group3 = root.addGroup("group3", groupBuilder);
        group3.addRemovalListener(listener3Mock);
        Group group4 = root.addGroup("group4", groupBuilder);
        group4.addRemovalListener(listener4Mock);

        // Populate the cache with some values.
        doRetrieve(cache, "group1.key1");

        doRetrieve(cache, "group2.key1");
        doRetrieve(cache, "group2.key2");
        doRetrieve(cache, "group2.key3");
        doRetrieve(cache, "group2.key4");

        doRetrieve(cache, "group3.key1");
        doRetrieve(cache, "group3.key2");
        doRetrieve(cache, "group3.key3");

        // Get another new entry in the cache, this should cause the entry for
        // group2.key1 to be removed.
        doRetrieve(cache, "group4.key1");

        // Get an existing entry again, this should cause it to become the most
        // recently used in group2.
        doRetrieve(cache, "group2.key2");

        // Get another new entry in the cache, this should cause the entry for
        // group2.key3 to be removed as it is the least recently used.
        doRetrieve(cache, "group4.key2");

        // Group 2 is now less used than group 3 so getting another entry in
        // group 1 should cause the entry for group3.key to be removed.
        doRetrieve(cache, "group1.key2");
    }

    private void doRetrieve(Cache cache, final String key) {
        Object value = cache.retrieve(key);
        assertEquals("value for (" + key + ")", value);
    }

}
