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
package com.volantis.vdp.scs.protocol;

/**
 * The keep-alive packet, sent periodically by SecureConnectionServer to sps to keep proxied http/https connection opened.
 */
public class KAPacket extends SCPHeader {

    /**
     * Creates keep-alive packet
     * @param reqId request id
     */
    public KAPacket(int reqId) {
        this.setRequestTypeIdetifier(KEEP_ALIVE);
        this.setRequestIdetifier(reqId);
        this.setSizeOfDatablock(0);
    }

    /**
     * Returns the whole keep-alive packet.
     * @return the whole keep-alive packet as byte array
     */
    public byte[] createKAPacket() {
        return this.getHeaderBytes();
    }
    
}
