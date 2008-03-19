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
package com.volantis.vdp.sps.authentication;

import com.volantis.vdp.configuration.sps.ISPSConfiguration;
import com.volantis.vdp.scs.protocol.SCPPacket;
import com.volantis.vdp.scs.protocol.AUPacket;
import com.volantis.vdp.cli.SecurePublisher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * User: rstroz01
 * Date: 2006-01-05
 * Time: 11:08:39
 * <p/>
 * Class implements Authentication handshake between SCP and sps
 */
public class Auth {
    private DataInputStream iStream;
    private DataOutputStream oStream;
    private ISPSConfiguration config;
    private String userid;
    private String password;

    // Don't create class by default constructor
    private Auth() {
    }

    /**
     * Public constructor create instance of this class used DataInputStream,
     * DataOutputStream and ISPSConfiguration interface
     *
     * @param iStream
     * @param oStream
     * @param config
     */
    public Auth(DataInputStream iStream, DataOutputStream oStream,
                ISPSConfiguration config) {
        this.iStream = iStream;
        this.oStream = oStream;
        this.config = config;

        this.userid = config.getSCUser();
        this.password = config.getSCPassword();
    }

    /**
     * Method implements authorizaton handshake between Secure COnnector Server
     * and Secure Publisher Server
     *
     * @return int authorization code defined by AtuhenticationToken class and
     *         if any other error, method
     *         return SecurePublisher.LOGIN_FAILED_NETWORK_ERROR
     */
    public int auth() {
        AUPacket authRequest = new AUPacket(this.userid, this.password);
        SCPPacket authResponse = new SCPPacket();

        try {
            oStream.write(authRequest.getBytes());
            boolean run = true;
            byte[] responseHeader;
            while(run) {
                int size = iStream.available();
                if( size > 0 ) {
                    responseHeader = new byte[size];
                    iStream.read(responseHeader);
                    authResponse = new SCPPacket(responseHeader);
                    run = false;
                }
            }

            System.out.println(authResponse.toString());
            if (SCPPacket.AUTHENTICATION
                    .equals(authResponse.getRequestTypeIdetifier())) {
                if (authResponse.getSizeOfDatablock() == 1) {
                 /*   String baseUrl = new String(responseContent, 1,
                            responseContent.length - 1);*/
                    // @todo can't set url, ISPSConfigure no setter getWebServerURL()
                    //System.out.println(new String(authResponse.getDatablock()));
                    return 0;
                }
                return SecurePublisher.LOGIN_FAILED_NETWORK_ERROR;
            } else {
                return SecurePublisher.LOGIN_FAILED_NETWORK_ERROR;
            }
        } catch (IOException ex) {

            return SecurePublisher.LOGIN_FAILED_NETWORK_ERROR;
        }
    }
}
