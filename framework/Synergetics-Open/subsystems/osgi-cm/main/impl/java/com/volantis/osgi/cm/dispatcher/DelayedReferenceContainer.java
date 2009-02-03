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

package com.volantis.osgi.cm.dispatcher;

import java.util.concurrent.CountDownLatch;
import org.osgi.framework.ServiceReference;

/**
 * A {@link ServiceReferenceContainer} whose initialisation is delayed.
 *
 * <p>This is needed because a configuration event needs to contain a reference
 * to the configuration admin service but that reference is not available until
 * after the admin service has been registered at which point other bundles can
 * and do start using it immediately. The solution relies on the fact that
 * events are dispatched on a separate thread so that can wait until the service
 * reference has been made available.</p>
 */
public class DelayedReferenceContainer
        implements ServiceReferenceContainer {

    /**
     * A latch used to make threads asking for the reference to wait until it
     * has been set.
     */
    private final CountDownLatch latch;

    /**
     * The reference that is being waited upon.
     */
    private ServiceReference reference;

    /**
     * Initialise.
     */
    public DelayedReferenceContainer() {
        latch = new CountDownLatch(1);
    }

    /**
     * Set the reference.
     *
     * @param reference The reference to set.
     */
    public void setReference(ServiceReference reference) {

        // Store the reference, do this before invoking the latch.
        this.reference = reference;

        // Wake up any threads waiting for this.
        latch.countDown();
    }


    // Javadoc inherited.
    public ServiceReference getReference() {

        boolean interrupted = false;
        while (true) {
            try {
                latch.await();
                break;
            } catch (InterruptedException e) {
                // Clear the interrupted state of the thread but remember that
                // we were interrupted.
                Thread.interrupted();
                interrupted = true;
            }
        }

        // If we were interrupted then place the thread back in that state
        // before carrying on.
        if (interrupted) {
            Thread thread = Thread.currentThread();
            thread.interrupt();
        }

        return reference;
    }
}
