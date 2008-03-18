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

package com.volantis.shared.net.impl;



/**
 * Attempts to timeout an operation by interrupting the thread on which it was
 * created.
 */
public class ThreadInterruptingTimingOutTask
        extends AbstractTimingOutTask {

    /**
     * The thread to interrupt.
     */
    private final Thread thread;

    /**
     * Initialise.
     *
     * <p>Will interrupt the current thread.</p>
     */
    public ThreadInterruptingTimingOutTask() {
        this(Thread.currentThread());
    }

    /**
     * Initialise.
     *
     * @param thread The thread to interrupt.
     */
    public ThreadInterruptingTimingOutTask(Thread thread) {
        this.thread = thread;
    }

    // Javadoc inherited.
    protected void doTimeOut() {
        thread.interrupt();
    }

    // Javadoc inherited.
    protected void doPostTimeOutCleanup() {
        // Make sure that the current thread is not marked as interrupted
        // as that could cause other I/O to fail.
        Thread.interrupted();
    }
}
