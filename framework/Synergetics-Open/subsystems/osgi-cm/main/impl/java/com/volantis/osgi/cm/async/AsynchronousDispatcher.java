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
import java.util.concurrent.LinkedBlockingQueue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Iterator;
import java.util.List;

/**
 * Dispatches tasks ({@link Runnable} instances) asynchronously on a background
 * thread.
 */
public class AsynchronousDispatcher {

    /**
     * The queue between the dispatcher and the background thread.
     */
    private final BlockingQueue taskQueue;

    /**
     * The background thread.
     */
    private final DispatcherThread thread;

    /**
     * Initialise.
     */
    public AsynchronousDispatcher() {

        // Create a background thread to process tasks added to the queue.
        taskQueue = new LinkedBlockingQueue();
        thread = new DispatcherThread(taskQueue);
    }

    /**
     * Queue a task to be processed by the background thread.
     *
     * @param task The task to process.
     */
    public void queueAsynchronousAction(Runnable task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            // Shouldn't really happen. If it does then exit.
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Stop the background thread.
     *
     * <p>This stops the thread cleanly making sure that it has run all its
     * currently queued tasks before exiting. It will not however prevent other
     * tasks from being added to the queue, they will just not be run.</p>
     *
     * todo Prevent other tasks from being queued while waiting for the todo
     * background thread to stop.
     */
    public void stop() {

        // Dispatch a task that will
        queueAsynchronousAction(new Runnable() {
            public void run() {
                throw new TerminateException();
            }
        });

        try {
            thread.join();

            List throwables = thread.getThrowables();
            int count = throwables.size();
            if (count == 1) {
                Throwable t = (Throwable) throwables.get(0);
                if (t != null) {
                    if (t instanceof Error) {
                        throw (Error) t;
                    } else if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    } else {
                        throw new UndeclaredThrowableException(t);
                    }
                }
            } else if (count > 1) {
                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                for (Iterator i = throwables.iterator(); i.hasNext();) {
                    Throwable t = (Throwable) i.next();
                    t.printStackTrace(printWriter);
                }

                throw new RuntimeException(
                        "The following errors occurred in background thread\n" +
                                writer.getBuffer());
            }
        } catch (InterruptedException e) {
            // Not much to do.
        }
    }
}
