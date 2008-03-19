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

import com.volantis.vdp.common.PacketUtilities;

/**
 * The authentication packet used within the communication between the SecureConnectionServer and sps.
 */
public class AUPacket extends SCPHeader {

    private String userid;

    private String password;
    
    private StringBuffer datablockString;

    /**
     * Constructs the AUPacket basing on data stored in the given SCP packet
     * @param packet SCP packet
     */
    public AUPacket(SCPPacket packet) {

        this.userid = null;
        this.password = null;

        this.requestTypeIdentifier = packet.getRequestTypeIdetifier();
        this.requestIdentifier = packet.getRequestIdetifier();
        this.sizeOfDatablock = packet.getSizeOfDatablock();

        String pairUserPasswd = new String(packet.getDatablock());

        String[] pair = pairUserPasswd.split(":");

        if(pair.length == 2) {
            this.userid = pair[0];
            this.password = pair[1];
        }
    }

    /**
     * Creates the authentication packet basing on the authentication data passed as parameters
     * @param userid user id
     * @param password password
     */
    public AUPacket(String userid, String password) {

        this.userid = userid;
        this.password = password;

        this.requestTypeIdentifier = AUTHENTICATION;
        this.requestIdentifier = 0;

        datablockString = new StringBuffer();
        datablockString.append(this.userid);
        datablockString.append(":");
        datablockString.append(this.password);

        this.sizeOfDatablock =  datablockString.length();
    }

    /**
     * Converts this AUPacket to a String
     * @return a string representation of this AUPacket
     */
    public String toString() {

        String retVal = "";

        retVal += "Type of request identifier: " +
                this.requestTypeIdentifier + "\n";
        retVal += "Request identifier: " + this.requestIdentifier + "\n";
        retVal += "Size of datablock: " + this.sizeOfDatablock + "\n";
        retVal += "Userid: " + this.userid + "\n";
        retVal += "Password: " + this.password;

        return retVal;
    }

    /**
     * Returns the password
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user id
     * @return the userid.
     */
    public String getUserId() {
        return userid;
    }

    /**
     * Returns the whole authentication packet
     * @return the whole authentication packet as byte array
     */
    public byte[] getBytes() {

        byte[] bytes = new byte[SCPHeader.SIZE_OF_HEADER+sizeOfDatablock];


        System.arraycopy(requestTypeIdentifier.getBytes(), 0, bytes, 0, 2);

        System.arraycopy(PacketUtilities.integerToByteArray(requestIdentifier),
                0, bytes, 2, 4);
        System.arraycopy(PacketUtilities.integerToByteArray(sizeOfDatablock),
                0, bytes, 6, 4);

        System.arraycopy(datablockString.toString().getBytes(),
                0, bytes, 10, datablockString.length());

        return bytes;
    }
}
