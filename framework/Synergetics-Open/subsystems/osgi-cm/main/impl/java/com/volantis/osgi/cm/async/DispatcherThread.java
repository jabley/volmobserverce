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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.cm.async;

import java.util.concurrent.BlockingQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Background thread that dispatches all the tasks.
 */
class DispatcherThread
        extends Thread {

    /**
     * The queue onto which tasks will be added.
     */
    private final BlockingQueue queue;

    /**
     * The throwables that were caught during the running of the thread.
     *
     * todo only used for testing so handle this in a better way as at the todo
     * moment it is essentially a memory leak.
     */
    private final List throwables;

    public DispatcherThread(BlockingQueue queue) {
        this.queue = queue;
        this.throwables = new ArrayList();
        setDaemon(true);
        start();
    }

    // Javadoc inherited.
    public void run() {
        try {
            while (true) {
                Runnable task = (Runnable) queue.take();
                try {
                    task.run();
                } catch (TerminateException e) {
                    // Terminate the loop.
                    break;
                } catch (Throwable t) {
                    t.printStackTrace(System.out);
                    synchronized (this) {
                        this.throwables.add(t);
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            // Just stop.
        }
    }

    /**
     * Get the throwables that were caught during the running of the thread.
     *
     * @return The throwables.
     */
    public synchronized List getThrowables() {
        return throwables;
    }
}
