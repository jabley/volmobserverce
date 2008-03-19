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
package com.volantis.mcs.prerenderer.client;

/**
 * Class which is item in Queue and consist of remote and local url page or resource file 
 */

public class Pair {
    
    private String remoteUri;
    private String localUri;
    
    Pair(String remote, String local){
        this.remoteUri = remote;
        this.localUri = local;
    }

    /**
     * @return Returns the localUri.
     */
    public String getLocalUri() {
        return localUri;
    }

    /**
     * @param localUri The localUri to set.
     */
    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

    /**
     * @return Returns the remoteURI.
     */
    public String getRemoteUri() {
        return remoteUri;
    }

    /**
     * @param remoteURI The remoteURI to set.
     */
    public void setRemoteUri(String remoteUri) {
        this.remoteUri = remoteUri;
    }
}
