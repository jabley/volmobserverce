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

package com.volantis.osgi.j2ee.bridge.http.service;

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Base for resource and servlet registrations.
 */
public abstract class Registration {

    /**
     * The bundle for which this was created.
     */
    protected final Bundle bundle;

    /**
     * The supplied {@link HttpContext}.
     */
    protected final HttpContext httpContext;

    /**
     * The servlet context.
     */
    protected final InternalServletContext servletContext;

    /**
     * The alias for the registration.
     */
    protected final String alias;

    /**
     * Flag that indicates whether the registration is being shut down or not.
     *
     * <p>Used to quiesce the registration by preventing any more access to
     * it once it has started shutting down.</p>
     */
    protected boolean shuttingDown;

    /**
     * A count of the number of requests for this registration currently in
     * progress.
     *
     * <p>Used with shutting down flag to determine when shutdown has
     * completed.</p>
     */
    protected int inProgress;

    /**
     * Indicates whether the resource has been initialised.
     */
    protected boolean initialised;

    /**
     * Initialise.
     *
     * @param bundle         The bundle.
     * @param servletContext The servlet context.
     * @param alias          The alias.
     */
    public Registration(
            Bundle bundle, InternalServletContext servletContext, String alias) {
        this.bundle = bundle;
        this.httpContext = servletContext.getHttpContext();
        this.servletContext = servletContext;
        this.alias = alias;
    }

    /**
     * Get the alias.
     *
     * @return The alias.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Handle the request.
     *
     * @param request  The request.
     * @param response The response.
     * @return True if the request was handled, false if it could not be
     *         handled and so some other registration higher up the path should
     *         be checked.
     * @throws IOException      If there was a problem writing to the response.
     * @throws ServletException If there was a problem invoking the servlet.
     */
    public boolean handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        synchronized (this) {

            if (!initialised) {
                throw new IllegalStateException("Entity " + this +
                        " registered at " + alias +
                        " was not initialized successfully");
            }

            // If the servlet is shutting down then reject any further
            // requests.
            if (shuttingDown) {
                return false;
            }

            // Increment the number of requests in progress within this
            // servlet.
            inProgress += 1;
        }

        try {
            doRequest(request, response);

        } finally {
            synchronized (this) {

                // Decrement the number of requests in progress within this
                // servlet.
                inProgress -= 1;

                // If there are no requests in progress and the servlet is
                // shutting down then notify the thread performing the shutdown
                // that it is now safe to proceed.
                if (inProgress == 0 && shuttingDown) {
                    notifyAll();
                }
            }
        }

        return true;
    }

    /**
     * Get the bundle.
     *
     * @return The bundle.
     */
    public Bundle getBundle() {
        return bundle;
    }

    /**
     * Start shutting down.
     *
     * <p>Just sets a flag to prevent any more requests from being
     * processed.</p>
     */
    public void startShutDown() {
        synchronized (this) {
            shuttingDown = true;
        }
    }

    /**
     * Waits for all outstanding requests to be completed before it then
     * finishes shutting down.
     *
     * <p>Must only be called after {@link #startShutDown()}.</p>
     */
    public void completeShutDown() {
        synchronized (this) {
            shuttingDown = true;
            while (inProgress > 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    IllegalArgumentException exception =
                            new IllegalArgumentException(
                                    "Could not destroy servlet");
                    throw exception;
                }
            }
        }

        doShutDown();
    }

    /**
     * Do the shutdown, called only after all outstanding requests have been
     * processed.
     */
    protected abstract void doShutDown();

    /**
     * Get the context.
     *
     * @return The context.
     */
    public HttpContext getHttpContext() {
        return httpContext;
    }

    /**
     * Do the request.
     *
     * @param request  The request.
     * @param response The response.
     * @return True if the request was handled, false if it could not be
     *         handled and so some other registration higher up the path should
     *         be checked.
     * @throws IOException      If there was a problem writing to the response.
     * @throws ServletException If there was a problem invoking the servlet.
     */
    protected abstract boolean doRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
