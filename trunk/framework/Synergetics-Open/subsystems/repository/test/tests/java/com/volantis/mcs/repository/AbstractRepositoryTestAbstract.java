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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-2003  Allan           VBM:2003051303 - Created. The parent testcase
 *                              for AbstractRepository classes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.repository;

import com.volantis.mcs.repository.impl.RepositoryConnectionStub;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Parent testcase for Repository classes that derive from AbstractRepository.
 */ 
public abstract class AbstractRepositoryTestAbstract
        extends TestCaseAbstract {

    /**
     * Tests that the checkStatus() method throws a RepositoryException
     * if called when the terminating flag is true.
     */
    public void testCheckStatusTerminating() {
        AbstractRepository repository = createTestableRepository();
        repository.terminating = true;

        try {
            repository.checkStatus();
            fail("Expected checkStatus() to throw an exception since" +
                 " the terminating flag is true");
        } catch (RepositoryException e) {
            // Success.
        }
    }

    /**
     * Tests that the checkStatus() method throws a RepositoryException
     * if called when the terminated flag is true.
     */
    public void testCheckStatusTerminated() {
        AbstractRepository repository = createTestableRepository();
        repository.terminated = true;

        try {
            repository.checkStatus();
            fail("Expected checkStatus() to throw an exception since" +
                 " the terminated flag is true");
        } catch (RepositoryException e) {
            // Success.
        }
    }

    /**
     * Tests that the checkStatus() does not throw an exception when
     * neither terminating nor terminated flags are set.
     */
    public void testCheckStatusPositive() {
        AbstractRepository repository = createTestableRepository();
        repository.terminating = false;
        repository.terminated = false;
    }

    /**
     * Tests that the connect() method calls checkStatus().
     */
    public void testConnectCallsCheckStatus() {
        AbstractRepository repository = createTestableRepository();
        repository.terminating = true;
        repository.terminated = true;
        try {
            repository.connect();
            fail("The connect() method is not calling checkStatus() or " +
                 "checkStatus() is broken.");
        } catch (RepositoryException e) {
            // Success;
        }
    }

    /**
     * Tests that the disconnect() method calls checkStatus().
     */
    public void testDisconnectCallsCheckStatus() {
        AbstractRepository repository = createTestableRepository();
        repository.terminating = true;
        repository.terminated = true;

        try {
            repository.disconnect(new RepositoryConnectionStub());
            fail("The disconnect() method is not calling checkStatus() or " +
                 "checkStatus() is broken.");
        } catch (RepositoryException e) {
            // Success;
        }
    }

    /**
     * Tests that the terminate() method calls checkStatus().
     */
    public void testTerminateCallsCheckStatus() {
        AbstractRepository repository = createTestableRepository();
        repository.terminating = true;
        repository.terminated = true;

        try {
            repository.terminate();
            fail("The terminate() method is not calling checkStatus() or " +
                 "checkStatus() is broken.");
        } catch (RepositoryException e) {
            // Success;
        }
    }

    /**
     * Tests that the terminate() method sets the terminated flag.
     * @throws RepositoryException if there is a problem with terminate.
     */
    public void testTerminateSetsTerminated() throws RepositoryException {
        AbstractRepository repository = createTestableRepository();
        repository.terminated = false;

        repository.terminate();
        assertTrue("The terminated flag should be true.",
                   repository.terminated);
    }

    /**
     * Provide an AbstractRepository that can be used in tests.
     * @return an AbstractRepository for testing against.
     */
    protected abstract AbstractRepository createTestableRepository();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
