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
package com.volantis.vdp.scs.managers.request;

/**
 * Provides a unique requests ID in sent SCP packet. 
 */
public class RequestIdentifierManager {

    private static int requestId = 0;

    /**
     * Default constructor.
     */
    public RequestIdentifierManager() {
    }

    /**
     * Returns a current request ID.
     * @return a current request ID
     */
    public static int getCurrentRequestId() {
        return requestId;
    }

    /**
     * Returns a new request ID.
     * @return a new request ID
     */
    public static int getNewRequestId() {

        int reqId = 0;

        if( requestId+1>0 ) {
            reqId = ++requestId;
        } else {
            requestId = 0;
            reqId = ++requestId;
        }

        return reqId;
    }
    
}
