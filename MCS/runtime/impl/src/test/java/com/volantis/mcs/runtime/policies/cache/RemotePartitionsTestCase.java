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

import com.volantis.cache.group.GroupMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link RemotePartitionsImpl}.
 */
public class RemotePartitionsTestCase
        extends TestCaseAbstract {

    private static final String EXTENDED_PATH_1 = "http://host1/path/path";
    private static final String PATH_1 = "http://host1/path";
    private static final String PATH_2 = "http://host2/path";

    private GroupMock defaultRemoteGroupMock;
    private GroupMock partition1GroupMock;
    private GroupMock partition2GroupMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        defaultRemoteGroupMock =
                new GroupMock("defaultRemoteGroupMock", expectations);

        partition1GroupMock = new GroupMock("partition1GroupMock", expectations);

        partition2GroupMock = new GroupMock("partition2GroupMock", expectations);
    }

    /**
     * Ensure that getting some partitions works properly.
     */
    public void testGettingPartitions() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        defaultRemoteGroupMock.fuzzy
                .addGroup(PATH_1, mockFactory.expectsAny())
                .returns(partition1GroupMock);
        defaultRemoteGroupMock.fuzzy
                .addGroup(PATH_2, mockFactory.expectsAny())
                .returns(partition2GroupMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePartitionsBuilder builder = new RemotePartitionsBuilder();
        builder.addPartition(PATH_1, 100);
        builder.addPartition(PATH_2, 200);

        RemotePartitions collection = builder.buildRemotePartitions(
                defaultRemoteGroupMock);

        RemotePartition partition1 = collection.getRemotePartition(PATH_1);
        assertEquals(PATH_1, partition1.getPrefix());
        assertSame(partition1GroupMock, partition1.getGroup());

        RemotePartition partition2 = collection.getRemotePartition(PATH_2);
        assertEquals(PATH_2, partition2.getPrefix());
        assertSame(partition2GroupMock, partition2.getGroup());

        RemotePartition partition;

        // Getting a partition for a URL that has no prefix must return null.
        partition = collection.getRemotePartition("http://host3/path");
        assertNull(partition);

        // Getting a partition for a URL that is a prefix of one in the map must
        // also return null.
        partition = collection.getRemotePartition("http://host2");
        assertNull(partition);

        // Getting a partition for a URL that is prefixed by one in the map must
        // return the one in the map.
        partition = collection.getRemotePartition(EXTENDED_PATH_1);
        assertSame(partition1, partition);
    }
}
