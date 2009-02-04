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
package com.volantis.vdp.sps.response.impl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.vdp.scs.protocol.SCPPacket;
import com.volantis.vdp.sps.response.ISCPWriter;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * Class implements interface ISCPwriter
 * User: rstroz01
 * Date: 2005-12-30
 * Time: 13:40:38
 */
public class SCPWriter implements ISCPWriter {
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(SCPWriter.class);

    //public static Logger LOGGER = Logger.getLogger(SCPWriter.class);
    private DataOutputStream os;

    public SCPWriter() {
    }

    /**
     * Construct object used connection
     * @param connection
     * @throws IOException
     */
    public SCPWriter(Socket connection)
            throws IOException {
        os = new DataOutputStream(connection.getOutputStream());
    }

    /**
     * Construct object user DataOutputStream
     * @param os
     */
    public SCPWriter(DataOutputStream os) {
        this.os = os;
    }

    /**
     * Method write response to Secure Connection Server
     *
     * @param dataBlock     - response from Web Server
     * @param requestId     - reuest identifier same as request identifier
     * @param requestTypeId - request type ID
     */
    synchronized public void write(byte[] dataBlock, int requestId,
                                   byte[] requestTypeId) {
        SCPPacket packet = new SCPPacket(new String(requestTypeId), requestId, dataBlock);
        try {
            os.write(packet.getBytes());
            os.flush();
        } catch (IOException ex) {
            LOGGER.error("Error writting to Secure Connection Server");
        }
    }
}
