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
package com.volantis.vdp.scs.proxy;

/**
 * The ProxyHTTP and ProxyHTTPs common functionality exposed as an interface.
 */
public interface Proxy {

    /**
     * Sends the given data to server
     * @param data the data sent to a server
     */
    public void send(byte[] data);

    /**
     * Sends the given data to server.
     * @param data the data sent to a server
     * @param off the offset of the sent part in the given data array
     * @param len how many bytes from the given data array should be sent
     */    
    public void send(byte[] data, int off, int len);

    /**
     * Closes open streams and socket.
     */
    public void close();
}


