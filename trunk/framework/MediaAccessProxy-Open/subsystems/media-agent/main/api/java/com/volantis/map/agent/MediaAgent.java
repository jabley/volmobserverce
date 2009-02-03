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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.agent;

import java.util.Set;

/**
 * The interface to the Media Conversion Program.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 * 
 * @mock.generate
 */
public interface MediaAgent {

    public static final String OUTPUT_URL_PARAMETER_NAME = "MA_OutputURL";

    /**
     * Register a request for information. This information will be provided
     * via a callback mechanism. It is guarenteed that these callbacks will
     * be executed on the thread that calls the {@link #waitForComplete} method.
     *
     * @param request the request parameters
     * @param callback the callback that will handle the reponse parameters
     * when they become available.
     * @return An ID for the request which can later be used to wait for that
     *         request to complete.
     */
    public AgentRequestId requestURL(Request request, ResponseCallback callback);

    /**
     * Ensures that the items handled by this media agent have a time to live
     * period of at least the amount specified.
     *
     * @param timeToLive the minimum time to live
     */
    public void ensureMinTimeToLive(long timeToLive);

    /**
     * Calling into this method will cause the thread to block until all
     * registered call backs have been executed. This method MUST be called
     * inside a finally block to enable it to clean up if an error occurs.
     */
    public void waitForComplete() throws MediaAgentException;

    /**
     * Calling into this method will cause the thread to block until the
     * specified call back has been executed.
     *
     * @param requestId the ID of the call back to wait for
     */
    public void waitForComplete(AgentRequestId requestId)
            throws MediaAgentException;

    /**
     * Calling into this method will cause the thread to block until the
     * specified call backs have been executed.
     *
     * @param requestIds a set of {@link AgentRequestId}s identifying the call
     *                   backs to wait for
     */
    public void waitForComplete(Set requestIds) throws MediaAgentException;
}
