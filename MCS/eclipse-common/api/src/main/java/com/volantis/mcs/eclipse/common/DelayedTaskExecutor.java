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

package com.volantis.mcs.eclipse.common;

/**
 * This abstract class is used to execute a task in an asynchronous manner
 * after a specified amount of time has elapsed.<p>
 */
public abstract class DelayedTaskExecutor extends Thread {
    /**
     * When the task execution is not required, the thread can sleep for a very
     * long time. This constant is used to represent that long delay.
     */
    private static final int QUIET_STATE_DELAY = 86400 * 1000; // 1 day

    /**
     * The number of milliseconds to wait before performing a task.
     */
    private int delay;

    /**
     * Indicates that task execution is required once the delay has elapsed.
     */
    private boolean executeTask;

    /**
     * Set true when the thread should terminate.
     */
    private boolean disposed = false;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the name of the task (i.e. the thread name). The name should
     * make it easy to locate both where the thread came from and what it is
     * doing. For example, the name of the class (not full path) that created
     * the task and the kind of thing it is doing - which can often be ascribed
     * using the name of the variable the DelayedTaskExecutor is assigned to.
     * See existing usages for examples.
     * @param delayInMillis the number of milliseconds that must pass
     *                      without interrupt before the task should be
     *                      executed.
     */
    public DelayedTaskExecutor(String name, int delayInMillis) {
        super(name);
        this.delay = delayInMillis;
    }

    /**
     * Must be called to allow the thread to terminate.
     */
    public void dispose() {
        disposed = true;

        // Wake the thread from its slumber
        interrupt();
    }

    /**
     * Can be used to cancel any pending task.
     */
    public void cancelTask() {
        executeTask = false;
    }

    // javadoc unnecessary
    public boolean isDisposed() {
        return disposed;
    }

    // javadoc inherited
    public void run() {
        int millis = QUIET_STATE_DELAY;

        while (!disposed) {
            try {
                sleep(millis);

                // If we got here, the sleep successfully completed (i.e.
                // the thread was not interrupted).
                if (executeTask) {
                    // Task execution has been is required, so execute this
                    // task.
                    executeTask();

                    // At this point task execution no longer needed.
                    executeTask = false;
                }

                // At this point task execution is not needed so a long delay
                // can be used (this may already be the case, but may not
                // be if task execution was cancelled).
                millis = QUIET_STATE_DELAY;
            } catch (InterruptedException e) {
                if (!disposed) {
                    // Task execution is now needed, so the required "short"
                    // sleep should be performed.
                    executeTask = true;
                    millis = delay;
                }
            }
        }
    }

    /**
     * This method is called when the specified time delay has elapsed.
     */
    abstract public void executeTask();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 27-Feb-04	3242/1	byron	VBM:2003121906 Image Asset Directory Scanning

 ===========================================================================
*/
