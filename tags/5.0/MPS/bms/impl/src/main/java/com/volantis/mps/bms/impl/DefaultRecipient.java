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

package com.volantis.mps.bms.impl;

import com.volantis.mps.bms.Address;
import com.volantis.mps.bms.Recipient;
import com.volantis.mps.bms.RecipientType;

public class DefaultRecipient implements Recipient {

    private Address address;

    private String channel;

    private String deviceName;

    // Default to RecipientType.TO
    private RecipientType type = RecipientType.TO;

    private String failureReason;

    public DefaultRecipient() {
        // constructor for JiBX
    }

    public DefaultRecipient(Address address, String deviceName) {
        setAddress(address);
        setDeviceName(deviceName);
    }

    // javadoc inherited
    public void setAddress(Address address) {
        if (null == address) {
            throw new IllegalArgumentException("address cannot be null");
        }
        this.address = address;
    }

    // javadoc inherited
    public Address getAddress() {
        return this.address;
    }

    // javadoc inherited
    public void setChannel(String channel) {
        this.channel = channel;
    }

    // javadoc inherited
    public String getChannel() {
        return this.channel;
    }

    // javadoc inherited
    public void setDeviceName(String name) {
        if (null == name) {
            throw new IllegalArgumentException("device name cannot be null");
        }

        this.deviceName = name;
    }

    // javadoc inherited
    public String getDeviceName() {
        return this.deviceName;
    }

    // javadoc inherited
    public void setRecipientType(RecipientType type) {
        this.type = type;
    }

    // javadoc inherited
    public RecipientType getRecipientType() {
        return this.type;
    }

    // javadoc inherited
    public void setFailureReason(String reason) {
        this.failureReason = reason;
    }

    // javadoc inherited
    public String getFailureReason() {
        return this.failureReason;
    }

    public String toString() {
        return "[recipient: address="
                + (null != this.address ? this.address.getValue() : "null")
                + ", device=" + this.deviceName
                + ", type="
                + (null != this.type ? this.type.name() : "null")
                + ", channel=" + this.channel
                + ", failureReason=" + this.failureReason
                + "]";
    }
}
