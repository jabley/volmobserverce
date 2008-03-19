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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.vdp.sps.response;

/**
 * User: rstroz01
 * Date: 2005-12-30
 * Time: 13:25:34
 */
public interface ISCPWriter {
        /**
         * Write response to SecureConnectionServer
         * @param dataBlock - response from destination
         * @param requestId - request identifier
         * @param packetTypeId - packet type
         */
    public void write(byte[] dataBlock, int requestId, byte[] packetTypeId);
}

