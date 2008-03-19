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

import java.io.IOException;

/**
 * A packet sent between SecureConnectionServer and SCP, containing header and a block with user data.
 */
public class SCPPacket extends SCPHeader {

    private byte[] datablock;

    private int SIZE_OF_DATABLOCK = 10;

    /**
     * Default constructor
     */
    public SCPPacket() {
    }

    /**
     * Creates SCP packet from the given data.
     * @param inputDatagram the given data
     */
    public SCPPacket(byte[] inputDatagram) {

        byte[] headerBytes = new byte[SIZE_OF_HEADER];
        System.arraycopy(inputDatagram, 0, headerBytes, 0, 10);
        createHeader(headerBytes);

        this.SIZE_OF_DATABLOCK = getSizeOfDatablock();
        this.datablock = new byte[SIZE_OF_DATABLOCK];
        System.arraycopy(inputDatagram, 10, datablock, 0, SIZE_OF_DATABLOCK);
    }

    /**
     * Creates SCP packet from the given values
     * @param type the SCP packet type
     * @param requestId the request id
     * @param data content data
     */
    public SCPPacket(String type, int requestId, byte[] data) {

            this.SIZE_OF_DATABLOCK = data.length;

            this.setRequestTypeIdetifier(type);
            this.setRequestIdetifier(requestId);
            this.setSizeOfDatablock(data.length);
            this.setDatablock(data);

    }

    /**
     * Returns the whole SCP packet.
     * @return the whole SCP packet as a byte array
     */
    public byte[] getBytes() {

        byte[] joinArrays = new byte[SIZE_OF_HEADER + SIZE_OF_DATABLOCK];

        System.arraycopy(getHeaderBytes(), 0, joinArrays, 0, SIZE_OF_HEADER);
        System.arraycopy(datablock, 0, joinArrays,
                SIZE_OF_HEADER, SIZE_OF_DATABLOCK);

        return joinArrays;
    }

    /**
     * Returns the user data part of this packet.
     * @return the user data block
     */
    public byte[] getDatablock() {
        return this.datablock;
    }

    /**
     * Sets the user data block basing on the given value
     * @param data user data block given as a byte array 
     */
    public void setDatablock(byte[] data) {
        this.sizeOfDatablock = data.length;
        this.SIZE_OF_DATABLOCK = data.length;
        this.datablock = data;
    }

    /**
     * Converts this SCPPacket to a String
     * @return a string representation of this SCPPacket
     */
    public String toString() {

        String retVal = "";

        retVal = "Type of request: " + getRequestTypeIdetifier() + "\n";
        retVal += "Request ID: " + getRequestIdetifier() + "\n";
        retVal += "Size of datablock: " + getSizeOfDatablock() + "\n";
        retVal += "\n";
        retVal += new String(getDatablock()) + "\n";

        return retVal;

    }
    
}
