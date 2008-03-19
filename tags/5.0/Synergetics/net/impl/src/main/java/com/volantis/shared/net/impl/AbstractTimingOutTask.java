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
 * Base class for all {@link TimingOutTask}s.
 */
public abstract class AbstractTimingOutTask
        implements TimingOutTask {

    /**
     * Indicates whether the task is still active.
     *
     * <p>If it is not then it does nothing when it is run.</p>
     */
    private boolean active = true;

    /**
     * Indicates whether this has actually timed out.
     */
    private boolean timedOut;

    // Javadoc inherited.
    public boolean timedOut() {
        synchronized (this) {
            return timedOut;
        }
    }

    // Javadoc inherited.
    public void deactivate() {
        boolean timedOut;
        synchronized (this) {
            active = false;
            timedOut = this.timedOut;
        }

        if (timedOut) {
            doPostTimeOutCleanup();
        }
    }

    // Javadoc inherited.
    public void run() {
        synchronized (this) {
            if (active) {
                doTimeOut();
                active = false;
                timedOut = true;
            }
        }
    }

    /**
     * Perform whatever action is necessary to cause the current thread to
     * stop what it is doing.
     */
    protected abstract void doTimeOut();

    /**
     * Perform any cleanup necessary after the task has timed out.
     */
    protected void doPostTimeOutCleanup() {
    }
}
