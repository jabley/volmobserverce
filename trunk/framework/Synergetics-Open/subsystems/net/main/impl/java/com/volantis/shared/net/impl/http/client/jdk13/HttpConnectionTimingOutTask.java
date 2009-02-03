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

package com.volantis.shared.net.impl.http.client.jdk13;

import com.volantis.shared.net.impl.AbstractTimingOutTask;
import com.volantis.shared.net.impl.AbstractTimingOutTask;
import org.apache.commons.httpclient.HttpConnection;

/**
 * A timer task that will close the {@link HttpConnection} when the timer
 * expires.
 *
 * <p>This is not particularly safe as the connection is not designed to be
 * closed aysnchronously. However, it is the only way to stop the
 * connection.</p>
 */
public class HttpConnectionTimingOutTask
        extends AbstractTimingOutTask {

    /**
     * The connection to close.
     */
    private final HttpConnection connection;

    /**
     * Initialise.
     *
     * @param connection The connection to close.
     */
    public HttpConnectionTimingOutTask(HttpConnection connection) {
        this.connection = connection;
    }

    // Javadoc inherited.
    protected void doTimeOut() {
        connection.close();
    }
}
