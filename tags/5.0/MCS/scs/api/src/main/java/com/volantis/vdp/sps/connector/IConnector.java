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
package com.volantis.vdp.sps.connector;

import com.volantis.vdp.sps.response.ISCPWriter;

/**
 * Interface define api for connector class
 * User: rstroz01
 * Date: 2005-12-30
 * Time: 13:37:07
 */
public interface IConnector extends Runnable {

    /**
     * @return byte[] - response from destination server
     */
    public byte[] getResponse();

    /**
     * @return int requestId
     */
    public int getRequestId();

    /**
     * method set writer
     */
    public void setWriter(ISCPWriter writer);

    /**
     * return true if connector is connected and false if not
     * connected
     */
    public boolean isConnected();
}
