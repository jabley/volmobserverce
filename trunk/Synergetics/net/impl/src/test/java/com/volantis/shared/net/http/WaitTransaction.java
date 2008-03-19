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

package com.volantis.shared.net.http;

import com.volantis.shared.throwable.ExtendedIOException;

import java.io.IOException;
import java.net.Socket;

/**
 * A transaction that simply waits for the specified period and then returns.
 */
public class WaitTransaction
        implements HttpTransaction {

    /**
     * The period to wait in milliseconds.
     */
    private final long periodInMillis;

    /**
     * Initialise.
     *
     * @param periodInMillis The period to wait in milliseconds.
     */
    public WaitTransaction(long periodInMillis) {
        this.periodInMillis = periodInMillis;
    }

    public void process(Socket socket) throws IOException {
        try {
            Thread.sleep(periodInMillis);
        } catch (InterruptedException e) {
            throw new ExtendedIOException(e);
        } finally {
            socket.close();
        }
    }
}
