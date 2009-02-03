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

package com.volantis.mcs.repository.impl.jdbc.lock;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.repository.lock.Lock;

import java.security.Principal;

/**
 * Test cases for {@link JDBCLock}.
 */
public class JDBCLockTestCase
        extends TestCaseAbstract {

    private static final String RESOURCE_ID = "resource";

    private JDBCLockManagerMock lockManagerMock;
    private Principal fred;
    private Principal wilma;

    protected void setUp() throws Exception {
        super.setUp();

        lockManagerMock = new JDBCLockManagerMock("lockManagerMock",
                expectations);

        fred = new JDBCPrincipal("fred");
        wilma = new JDBCPrincipal("wilma");
    }

    /**
     * Test that a successful lock request invokes the lock manager correctly
     * and updates the state of the lock object.
     */
    public void testSuccessfulLock() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The lock manager reports that the lock is owned by fred.
        JDBCLockInfo lockInfo = new JDBCLockInfo(fred, 12345L);
        lockManagerMock.expects.acquireLock(RESOURCE_ID, fred)
                .returns(lockInfo);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Lock lock = new JDBCLock(lockManagerMock, RESOURCE_ID, null, -1);
        boolean locked = lock.acquire(fred);
        assertTrue("Locked", locked);

        assertEquals("Principal unexpected", fred, lock.getOwner());
        assertEquals("Acquisition time incorrect", 12345L,
                lock.getAcquisitionTime());
    }

    /**
     * Test that a failed lock request invokes the lock manager correctly
     * and updates the state of the lock object.
     */
    public void testFailedLock() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The lock manager reports that the lock is owned by wilma.
        JDBCLockInfo lockInfo = new JDBCLockInfo(wilma, 54321L);
        lockManagerMock.expects.acquireLock(RESOURCE_ID, fred)
                .returns(lockInfo);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Lock lock = new JDBCLock(lockManagerMock, RESOURCE_ID, null, -1);
        boolean locked = lock.acquire(fred);
        assertFalse("Unlocked", locked);

        assertEquals("Principal unexpected", wilma, lock.getOwner());
        assertEquals("Acquisition time incorrect", 54321L,
                lock.getAcquisitionTime());
    }
}
