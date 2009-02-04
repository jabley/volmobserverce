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

import java.util.List;

/**
 * Test cases for {@link RemotePartitionsBuilder}.
 */
public class RemotePartitionsBuilderTestCase
        extends TestCaseAbstract {

    private static final String EXTENDED_PATH_1 = "http://host1/path/path";
    private static final String PATH_1 = "http://host1/path";
    private static final String PATH_2 = "http://host2/path";

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Ensure that adding some non conflicting partitions works properly.
     */
    public void testAddingPartitions() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePartitionsBuilder builder = new RemotePartitionsBuilder();
        builder.addPartition(PATH_1, 100);
        builder.addPartition(PATH_2, 200);
        builder.addPartition(PATH_2, 300);

        List partitions = builder.getPartitions();
        assertEquals(2, partitions.size());

        RemotePartitionsBuilder.Partition partition;

        partition = (RemotePartitionsBuilder.Partition) partitions.get(0);
        assertEquals(PATH_1, partition.getPrefix());
        assertEquals(100, partition.getSize());

        partition = (RemotePartitionsBuilder.Partition) partitions.get(1);
        assertEquals(PATH_2, partition.getPrefix());
        assertEquals(200, partition.getSize());
    }

    /**
     * Ensure that adding conflicting partitions works properly when the long one
     * is added before the shorter one.
     */
    public void testConflictingPartitionsLongFirst() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePartitionsBuilder builder = new RemotePartitionsBuilder();
        builder.addPartition(EXTENDED_PATH_1, 50);
        builder.addPartition(PATH_1, 100);
        builder.addPartition(PATH_2, 200);

        List partitions = builder.getPartitions();
        assertEquals(2, partitions.size());

        RemotePartitionsBuilder.Partition partition;

        partition = (RemotePartitionsBuilder.Partition) partitions.get(0);
        assertEquals(PATH_1, partition.getPrefix());
        assertEquals(100, partition.getSize());

        partition = (RemotePartitionsBuilder.Partition) partitions.get(1);
        assertEquals(PATH_2, partition.getPrefix());
        assertEquals(200, partition.getSize());
    }

    /**
     * Ensure that adding conflicting partitions works properly when the short one
     * is added before the longer one.
     */
    public void testConflictingPartitionsShortFirst() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemotePartitionsBuilder builder = new RemotePartitionsBuilder();
        builder.addPartition(PATH_1, 100);
        builder.addPartition(PATH_2, 200);
        builder.addPartition(EXTENDED_PATH_1, 50);

        List partitions = builder.getPartitions();
        assertEquals(2, partitions.size());

        RemotePartitionsBuilder.Partition partition;

        partition = (RemotePartitionsBuilder.Partition) partitions.get(0);
        assertEquals(PATH_1, partition.getPrefix());
        assertEquals(100, partition.getSize());

        partition = (RemotePartitionsBuilder.Partition) partitions.get(1);
        assertEquals(PATH_2, partition.getPrefix());
        assertEquals(200, partition.getSize());
    }
}
