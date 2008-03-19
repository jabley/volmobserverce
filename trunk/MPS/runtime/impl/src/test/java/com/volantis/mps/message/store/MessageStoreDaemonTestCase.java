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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.message.store;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.TimerTask;

/**
 * This provides a unit test of some of the functionality of the generic
 * message store daemon.  A sample task is used to ensure that it operates
 * every so often reliably.
 */
public class MessageStoreDaemonTestCase extends TestCaseAbstract {

    /**
     * Used by the test timer task to indicate its activity which can then be
     * checked in the test case methods.
     */
    protected int count = 0;

    /**
     * Initialise a new instance of this test case.
     */
    public MessageStoreDaemonTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public MessageStoreDaemonTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This tests the functioning of the daemon with a simple task to
     * record activity.  The cleaning daemon task as defined in
     * {@link com.volantis.mps.servlet.MessageStoreServlet} is tested within
     * the unit test for that class.
     */
    public void testDaemon() throws Exception {
        // Initialise the count
        count = 0;

        // Create the daemon
        MessageStoreDaemon daemon = new MessageStoreDaemon(1,
                                                           createTimerTask());

        // Adjust the delay period for the purposes of the test to be 1 second
        daemon.delayAndPeriod = 1000;

        // Start the daemon
        daemon.start();

        // Wait for a bit (5 seconds)
        Thread.sleep(5000);

        // Ensure the task has been run a few times (not exact as that relies
        // too heavily on VM, machine, OS, other apps, ...)
        assertTrue("Task should have run", count > 0);
        int currentCount = count;

        // Stop the daemon
        daemon.stop();

        // Wait for a bit (5 seconds)
        Thread.sleep(5000);

        // Ensure the task has not been run
        assertEquals("Task should not have run anymore", count, currentCount);
    }

    /**
     * Create a very simple timer task to be executed every so often.
     *
     * @return An initialised and ready to run task.
     */
    private TimerTask createTimerTask() {
        return new TimerTask() {
            public void run() {
                count++;
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 ===========================================================================
*/
