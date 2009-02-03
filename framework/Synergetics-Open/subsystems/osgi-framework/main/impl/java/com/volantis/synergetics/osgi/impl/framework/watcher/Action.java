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

package com.volantis.synergetics.osgi.impl.framework.watcher;

import org.osgi.framework.Bundle;

import java.util.Map;

/**
 * An action that can be performed by the watcher when it detects a change in
 * the directory.
 */
public abstract class Action {

    /**
     * The location (URL as string) of the bundle upon which the action will
     * be performed.
     */
    protected final String location;

    /**
     * Initialise.
     *
     * @param location The location (URL as string) of the bundle.
     */
    protected Action(String location) {
        this.location = location;
    }

    /**
     * Perform the action.
     *
     * @param failedActions The map of failed actions.
     * @return The bundle upon which the action was performed or null if there
     *         was a problem.
     */
    public abstract Bundle takeAction(Map failedActions);

    /**
     * Handle the exception.
     *
     * @param message   The message to report.
     * @param throwable The root cause.
     */
    protected void handleException(String message, Throwable throwable) {
        IllegalStateException exception = new IllegalStateException(message);
        exception.initCause(throwable);
        exception.printStackTrace(System.err);
    }

    /**
     * Get the location of the bundle upon which the action will be performed.
     *
     * @return The string location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Report an event.
     *
     * @param event The event that occurred.
     */
    protected void report(final String event) {
        String message = event + " " + location;
        Reporter.report(message);
    }

}
