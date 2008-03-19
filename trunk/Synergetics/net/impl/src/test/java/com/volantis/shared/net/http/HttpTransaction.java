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

import java.io.IOException;
import java.net.Socket;

/**
 * Encapsulates a transaction (request / response) that may be performed by the
 * {@link HttpServerMock}.
 */
public interface HttpTransaction {

    /**
     * Process the transaction.
     *
     * @param socket The socket that has been created.
     * @throws IOException If there was a problem processing the transaction.
     */
    public void process(Socket socket) throws IOException;
}
