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

import java.io.UnsupportedEncodingException;

/**
 * The header of all types of packets sent between SecureConnectionServer and sps.
 */
public class SCPHeader {

    public final static int SIZE_OF_HEADER = 10;

    /**
     * HT - A HTTP Request/ResponseHeader.
     */
    public final static String HTTP_REQUEST_RESPONSE = "HT";

    /**
     * HS - A HTTPS Request/ResponseHeader.
     */
    public final static String HTTPS_REQUEST_RESPONSE = "HS";

    //KA - A Keep Alive.
    public final static String KEEP_ALIVE = "KA";

    //AU - Authentication.
    public final static String AUTHENTICATION = "AU";

    protected String requestTypeIdentifier;

    protected int requestIdentifier;

    protected int sizeOfDatablock;

    /**
     * Default constructor
     */
    protected SCPHeader() {
    }

    /**
     * Creates SCP header from the given data.
     * @param header SCP header given as a byte array
     */
    public SCPHeader(byte[] header) {
        createHeader(header);    
    }

    /**
     * Sets this header's attributes
     * @param headerBytes copied header given
     */
    protected void createHeader(byte[] headerBytes) {

        byte[] type = new byte[2];
        byte[] requestId = new byte[4];
        byte[] size = new byte[4];

        System.arraycopy(headerBytes, 0, type, 0, 2);
        setRequestTypeIdetifier(new String(type));

        System.arraycopy(headerBytes, 2, requestId, 0, 4);
        setRequestIdetifier(requestId);

        System.arraycopy(headerBytes, 6, size, 0, 4);
        setSizeOfDatablock(size);

    }

    /**
     * Returns the header content.
     * @return the header content as a byte array
     */
    protected byte[] getHeaderBytes() {

        byte[] headerByteArray = new byte[10];

        System.arraycopy(requestTypeIdentifier.getBytes(), 0,
                headerByteArray, 0, 2);
        System.arraycopy(
                PacketUtilities.integerToByteArray(this.requestIdentifier), 0,
                headerByteArray, 2, 4);
        System.arraycopy(
                PacketUtilities.integerToByteArray(this.sizeOfDatablock), 0,
                headerByteArray, 6, 4);

        return headerByteArray;
    }

    /**
     * Sets the request type
     * @param type new request type value
     */
    protected void setRequestTypeIdetifier(String type) {

        if(type.equals(HTTP_REQUEST_RESPONSE))
            this.requestTypeIdentifier = new String(HTTP_REQUEST_RESPONSE);
        else if(type.equals(HTTPS_REQUEST_RESPONSE))
            this.requestTypeIdentifier = new String(HTTPS_REQUEST_RESPONSE);
        else if(type.equals(KEEP_ALIVE))
            this.requestTypeIdentifier = new String(KEEP_ALIVE);
        else if(type.equals(AUTHENTICATION))
            this.requestTypeIdentifier = new String(AUTHENTICATION);

    }

    /**
     * Sets the request id
     * @param requestId new request id
     */
    protected void setRequestIdetifier(int requestId) {
        this.requestIdentifier = requestId;
    }

    /**
     * Sets the request id
     * @param requestId new request id given as a byte array
     */
    protected void setRequestIdetifier(byte[] requestId) {
        this.requestIdentifier = PacketUtilities.toInteger(requestId);
    }

    /**
     * Sets the datablock size
     * @param size new request size
     */
    protected void setSizeOfDatablock(int size) {
        this.sizeOfDatablock = size;
    }

    /**
     * Sets the datablock size
     * @param size new request size given as a byte array
     */
    protected void setSizeOfDatablock(byte[] size) {
        this.sizeOfDatablock = PacketUtilities.toInteger(size);
    }

    /**
     * Sets the header content basing on given data
     * @param headerByte new header content
     */
    public void setHeaderByte(byte[] headerByte) {
        try {
            this.requestTypeIdentifier =
                    new String(headerByte, 0, 2, "UTF-8");

            byte[] reqId = new byte[4];
            System.arraycopy(headerByte, 2, reqId, 0, 4);
            this.requestIdentifier = PacketUtilities.toInteger(reqId);

            byte[] sizeOfDb = new byte[4];
            System.arraycopy(headerByte, 6, sizeOfDb, 0, 4);
            this.sizeOfDatablock =  PacketUtilities.toInteger(sizeOfDb);
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
     * Returns the request type.
     * @return the request type
     */
    public String getRequestTypeIdetifier() {
        return this.requestTypeIdentifier;
    }

    /**
     * Returns the request identifier
     * @return the request identifier
     */
    public int getRequestIdetifier() {
        return this.requestIdentifier;
    }

    /**
     * Returns the size of the datablock
     * @return the size of the datablock
     */
    public int getSizeOfDatablock() {
        return this.sizeOfDatablock;
    }

}
