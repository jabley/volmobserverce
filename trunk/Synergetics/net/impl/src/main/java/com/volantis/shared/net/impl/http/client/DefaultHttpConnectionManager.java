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

package com.volantis.shared.net.impl.http.client;

import our.apache.commons.httpclient.HostConfiguration;
import our.apache.commons.httpclient.HttpConnection;
import our.apache.commons.httpclient.HttpConnectionManager;

/**
 * A {@link HttpConnectionManager} that when it is asked to release a
 * connection closes it.
 */
public class DefaultHttpConnectionManager
        implements HttpConnectionManager {

    // Javadoc inherited.
    public HttpConnection getConnection(HostConfiguration hostConfiguration) {
        return getConnection(hostConfiguration, 0);
    }

    // Javadoc inherited.
    public HttpConnection getConnection(
            HostConfiguration hostConfiguration, long l) {

        HttpConnection connection = new HttpConnection(hostConfiguration);
        connection.setHttpConnectionManager(this);
        return connection;
    }

    // Javadoc inherited.
    public void releaseConnection(HttpConnection httpConnection) {
        httpConnection.close();
    }
}
