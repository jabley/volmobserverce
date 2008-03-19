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
package com.volantis.map.agent.impl;

import com.volantis.map.agent.Request;
import com.volantis.map.agent.ResponseCallback;

/**
 * A class representing a request made to the MediaAgent and its current state.
 */
public class AgentRequest {
    private Request request;
    private ResponseCallback responseCallback;
    private AgentRequestState state;

    public AgentRequest(Request request, ResponseCallback responseCallback) {
        this.request = request;
        this.responseCallback = responseCallback;
        this.state = AgentRequestState.PENDING;
    }

    // Javadoc not required
    public Request getRequest() {
        return request;
    }

    // Javadoc not required
    public ResponseCallback getResponseCallback() {
        return responseCallback;
    }

    // Javadoc not required
    public AgentRequestState getState() {
        return state;
    }

    /**
     * Flags a request as being processed if possible.
     *
     * @return True if the request was available to be processed, false
     *         otherwise (if it was already processing or completed).
     */
    public synchronized boolean beginProcessing() {
        boolean success = false;
        if (state == AgentRequestState.PENDING) {
            state = AgentRequestState.PROCESSING;
            success = true;
        }
        return success;
    }

    /**
     * Flags a request as completed if possible.
     *
     * @return True if the request was available to be completed, false
     *         otherwise (if it was already completed).
     */
    public synchronized boolean completeProcessing() {
        boolean success = false;
        if (state == AgentRequestState.PROCESSING) {
            state = AgentRequestState.COMPLETE;
            success = true;
        }
        return success;
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o != null && o.getClass() == getClass()) {
            AgentRequest other = (AgentRequest) o;
            equal = other.request.equals(request) &&
                    other.responseCallback.equals(responseCallback) &&
                    other.state == state;
        }
        return equal;
    }

    // Javadoc inherited
    public int hashCode() {
        int hash = request.hashCode();
        hash = hash * 37 + responseCallback.hashCode();
        hash = hash * 37 + state.hashCode();
        return hash;
    }
}
